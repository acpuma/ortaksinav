package net.yazsoft.ors.orderForm;

import net.sf.jasperreports.engine.JRParameter;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.mail.Email;
import net.yazsoft.frame.report.Report;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.sms.SmsDao;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.OrderForm;
import net.yazsoft.ors.entities.OrderFormProducts;
import net.yazsoft.ors.entities.Products;
import net.yazsoft.ors.settings.SettingsDao;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
@Transactional
public class OrderFormDao extends BaseGridDao<OrderForm> implements Serializable{
    private static final Logger logger = Logger.getLogger(OrderFormDao.class);
    OrderForm selected;
    OrderForm orderForm;
    Map<Products,String> products;
    List<OrderForm> orderForms;

    @Inject OrderFormProductsDao orderFormProductsDao;
    @Inject Report report;
    @Inject Email email;
    @Inject SmsDao smsDao;
    @Inject SettingsDao settingsDao;

    @Transactional
    public void delete(OrderForm of) {
        try {
            setItem(of);
            for (OrderFormProducts ofp : getItem().getOrderFormProductsCollection()) {
                orderFormProductsDao.delete(ofp);
            }
            super.delete(of);
            //getSession().flush();
            init();
            Util.setFacesMessage("SİLİNDİ");
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }

    public void preparePrint(OrderForm of) {
        orderForm=of;
    }

    public void reportOrderForm() {
        Map<String, Object> params = new HashMap<>();
        params.put("pNow",orderForm.getCreated());
        params.put("pOrderForm",orderForm.getTid());
        params.put("pName",orderForm.getName());
        params.put("pEmail",orderForm.getEmail());
        params.put("pPhone",orderForm.getPhone());
        params.put("pAddress",orderForm.getAddress());
        params.put("pDiscount",orderForm.getDiscount());
        params.put("pInstallment",orderForm.getInstallment());
        params.put("pInstallmentDay",orderForm.getInstallmentDay());
        params.put("pComment",orderForm.getComment());
        //params.put("p",orderForm.);

        Locale trlocale= Locale.forLanguageTag("tr-TR");
        params.put(JRParameter.REPORT_LOCALE, trlocale);
        init(); //reset form
        report.pdf("repOrderForm", params, "SiparisFormu");
    }

    @Transactional
    public void submitOrder() {
        logger.info("LOG01840: VALUES : " + orderForm.toString());
        try {
            orderForm.setActive(true);
            orderForm.setCreated(Calendar.getInstance().getTime());
            Long pk=create(orderForm);
            if (pk!=null) {
                logger.info("LOG02230: PRODUCTS : " + products);
                for (Map.Entry<Products,String> entry: products.entrySet()) {
                    logger.info("LOG01860: ENTRY : " + entry.getValue().getClass().getName());
                    logger.info("LOG02240: ENTRY VALUE : " + entry.getValue());
                    if (entry.getValue().equals("")) continue;
                    OrderFormProducts product=new OrderFormProducts();
                    product.setActive(true);
                    Products oproduct=(Products)getSession().load(Products.class,entry.getKey());
                    product.setRefProduct(oproduct);
                    Integer productCount=null;
                    try {
                        productCount=Integer.valueOf(entry.getValue());
                    } catch (Exception e) {
                        continue;
                    }
                    if (productCount==0) continue;
                    product.setCount(productCount);
                    product.setPrice(oproduct.getPrice());
                    product.setName(oproduct.getNameTr());
                    if (orderForm.getDiscount()==0) {
                        product.setPriceDiscount(oproduct.getPrice());
                    } else {
                        product.setPriceDiscount(oproduct.getPrice() -
                                (oproduct.getPrice() * orderForm.getDiscount() / 100));
                    }
                    product.setRefOrderform((OrderForm) getSession().load(OrderForm.class, pk));
                    product.setCreated(Calendar.getInstance().getTime());
                    orderFormProductsDao.create(product);
                }
                String body=" Yeni siparisiniz var : "
                        +"\nAd : "  + orderForm.getName()
                        +"\nAdres : " + orderForm.getAddress()
                        +"\nTelefon : " + orderForm.getPhone()
                        +"\nEposta : " + orderForm.getEmail()
                        +"\nAciklama : " + orderForm.getComment();
                email.sendMail(Constants.EMAIL_NOTIFICATION, "Ortak Sinav Yeni Siparis Formu",body);
                //email.sendMail("info@ortaksinav.com.tr", "Ortak Sinav Yeni Siparis Formu",body);

                String systemCellphone=settingsDao.findByName("systemCellphone").getValueStr();
                ArrayList<String> phones = new ArrayList<>();
                phones.add(systemCellphone);
                phones.add(orderForm.getPhone());
                smsDao.setPhones(phones);
                smsDao.setMesaj("Siparisiniz alindi."
                        +" Ad : "  + orderForm.getName()
                    +" Eposta : " + orderForm.getEmail());
                smsDao.sendSms();

            }
            //Util.setFacesMessage("SIPARISINIZ ALINDI");
            //reportOrderForm();
            //init(); //reset form
        } catch (Exception e) {
            Util.setFacesMessageError(e.getMessage());
            e.printStackTrace();
        }
    }



    public List<OrderForm> findOrderForms() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.addOrder(Order.desc("created"));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @PostConstruct
    public void init() {
        orderForm=new OrderForm();
        orderForm.setDiscount(0);
        products=new HashMap<>();
        orderForms=null;
    }

    public OrderFormDao() {
        super(OrderForm.class);
    }

    public Map<Products, String> getProducts() {
        return products;
    }

    public void setProducts(Map<Products, String> products) {
        this.products = products;
    }

    public OrderForm getOrderForm() {
        return orderForm;
    }

    public void setOrderForm(OrderForm orderForm) {
        this.orderForm = orderForm;
    }

    public List<OrderForm> getOrderForms() {
        if (orderForms==null) {
            orderForms = findOrderForms();
        }
        return orderForms;
    }

    public void setOrderForms(List<OrderForm> orderForms) {
        this.orderForms = orderForms;
    }

    public OrderForm getSelected() {
        return selected;
    }

    public void setSelected(OrderForm selected) {
        this.selected = selected;
    }
}

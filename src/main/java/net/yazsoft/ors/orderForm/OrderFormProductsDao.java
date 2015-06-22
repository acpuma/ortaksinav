package net.yazsoft.ors.orderForm;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.OrderForm;
import net.yazsoft.ors.entities.OrderFormProducts;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class OrderFormProductsDao extends BaseGridDao<OrderFormProducts> implements Serializable{
    private static final Logger logger = Logger.getLogger(OrderFormProductsDao.class);
    OrderFormProducts selected;
    List<OrderFormProducts> products;

    OrderForm orderForm;

    public List<OrderFormProducts> findByOrderForm() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refOrderform", orderForm));
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

    public Float findTotalPrice() {
        Float total=0f;
        for (OrderFormProducts product:products){
            total=total+product.getPrice();
        }
        return total;
    }
    public Float findTotalDiscount() {
        Float total=0f;
        for (OrderFormProducts product:products){
            total=total+product.getPriceDiscount();
        }
        return total;
    }

    public OrderFormProductsDao() {
        super(OrderFormProducts.class);
    }

    public OrderFormProducts getSelected() {
        return selected;
    }

    public void setSelected(OrderFormProducts selected) {
        this.selected = selected;
    }

    public List<OrderFormProducts> getProducts() {
        products=findByOrderForm();
        return products;
    }

    public void setProducts(List<OrderFormProducts> products) {
        this.products = products;
    }

    public OrderForm getOrderForm() {
        return orderForm;
    }

    public void setOrderForm(OrderForm orderForm) {
        this.orderForm = orderForm;
    }
}

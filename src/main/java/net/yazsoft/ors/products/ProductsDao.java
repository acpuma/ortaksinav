package net.yazsoft.ors.products;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Products;
import net.yazsoft.ors.entities.ProductsCategory;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.CellEditEvent;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Named
@ViewScoped
public class ProductsDao extends BaseGridDao<Products> implements Serializable{
    private static final Logger logger = Logger.getLogger(ProductsDao.class);
    Products selected;
    List<Products> products;
    Boolean productsChanged=true;
    List<Products> popular;
    List<Products> news;

    /*
    public void addProduct() {
        Products temp=new Products();
        temp.setActive(true);
        temp.setNameTr("Yeni Urun");
        temp.setShowOrderForm(true);
        create(temp);
        selected=temp;
        productsChanged=true;
    }
    */

    public void checkboxChange(Products product) {
        try {
            update(product);
            //Util.setFacesMessage("CHANGED");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if(newValue != null && !newValue.equals(oldValue)) {
            Util.setFacesMessage("Cell Changed : " +  "Old: " + oldValue + ", New:" + newValue);
        }
        logger.info("LOG02220:" +"Cell Changed : " +  "Old: " + oldValue + ", New:" + newValue);
    }

    public List<Products> findNews() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("showMainNew", true));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            c.setMaxResults(8);
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Integer> getProductStars(Products product) {
        List<Integer> stars=null;
        if (product.getStars()==null) {
            return null;
        }
        stars=new ArrayList<>();
        for (int i=0; i<product.getStars(); i++) {
            stars.add(i);
        }
        return stars;
    }

    public List<Products> findMostPopular() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("showMainMost", true));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public Long save() {
        productsChanged=true;
        return super.save();
    }

    public List<Products> findByCategory(ProductsCategory category) {
        List list=null;
        list=new ArrayList<>();
        for (Products product:getAll()) {
            if (product.getRefProductCategory().getTid().equals(category.getTid())) {
                list.add(product);
            }
        }

        /*
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refProductCategory", category));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        */
        return list;
    }

    public void reset(){
        super.reset();
        getItem().setActive(true);
        getItem().setShowOrderForm(true);
        getItem().setShowShop(true);
    }


    public Float findCategoryPrice(ProductsCategory category) {
        Float price=0f;
        List<Products> list=findByCategory(category);
        for (Products product:list) {
            price=price+product.getPrice();
        }
        return price;
    }

    public Integer findCategoryCount(ProductsCategory category) {
        int count=0;
        List<Products> list=findByCategory(category);
        count=list.size();
        return count;
    }

    public void delete() {
        super.delete();
        productsChanged=true;
    }

    public ProductsDao() {
        super(Products.class);
    }

    public Products getSelected() {
        return selected;
    }

    public void setSelected(Products selected) {
        this.selected = selected;
    }

    public List<Products> getProducts() {
        if (productsChanged==true) {
            products=getAll();
        }
        productsChanged=false;
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public List<Products> getPopular() {
        if (popular==null) {
            popular=findMostPopular();
        }
        return popular;
    }

    public void setPopular(List<Products> popular) {
        this.popular = popular;
    }

    public List<Products> getNews() {
        if (news==null) {
            news=findNews();
        }
        return news;
    }

    public void setNews(List<Products> news) {
        this.news = news;
    }
}

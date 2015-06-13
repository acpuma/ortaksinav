package net.yazsoft.ors.products;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Products;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ProductsDao extends BaseGridDao<Products> implements Serializable{
    Products selected;
    List<Products> products;
    Boolean productsChanged=true;

    public void addProduct() {
        Products temp=new Products();
        temp.setActive(true);
        temp.setNameTr("Yeni Urun");
        temp.setShowOrderForm(true);
        create(temp);
        selected=temp;
        productsChanged=true;
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
}

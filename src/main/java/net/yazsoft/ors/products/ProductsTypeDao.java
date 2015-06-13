package net.yazsoft.ors.products;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.ProductsType;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ProductsTypeDao extends BaseGridDao<ProductsType> implements Serializable{
    private ProductsType selected;
    private List<ProductsType> types;
    private String newtype;

    public Long create() {
        Long pk=null;
        try {
            ProductsType productsType = new ProductsType();
            productsType.setActive(true);
            productsType.setNameTr(newtype);
            pk = create(productsType);
        } catch (Exception e) {
            e.printStackTrace();
            Util.setFacesMessageError(e.getMessage());
        }
        return pk;
    }

    public String update() {
        super.update(selected);
        return null;
    }

    public ProductsTypeDao() {
        super(ProductsType.class);
    }

    public ProductsType getSelected() {
        return selected;
    }

    public void setSelected(ProductsType selected) {
        this.selected = selected;
    }

    public List<ProductsType> getTypes() {
        return types;
    }

    public void setTypes(List<ProductsType> types) {
        this.types = types;
    }

    public String getNewtype() {
        return newtype;
    }

    public void setNewtype(String newtype) {
        this.newtype = newtype;
    }
}

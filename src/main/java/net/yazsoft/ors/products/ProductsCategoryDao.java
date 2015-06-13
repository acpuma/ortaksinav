package net.yazsoft.ors.products;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.ProductsCategory;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ProductsCategoryDao extends BaseGridDao<ProductsCategory> implements Serializable {
    private static final Logger logger = Logger.getLogger(ProductsCategoryDao.class);
    ProductsCategory selected;
    List<ProductsCategory> categories;
    TreeNode root;
    String newcategory;
    DefaultTreeNode selectedNode;
    Boolean categoriesChanged=true;

    public List<ProductsCategory> findCategories() {
        List list=null;
        try {
            Criteria c = getCriteria();
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


    public void findCategoriesTree() {
        categories=findCategories();
        logger.info("LOG01800:CATEGORIES : " + categories);
        root = new DefaultTreeNode("Kategoriler",null);
        ProductsCategory rootCategory=(ProductsCategory)getSession().load(ProductsCategory.class,1L);
        logger.info("LOG01760: ROOT : " + rootCategory);
        TreeNode rootNode = new DefaultTreeNode(rootCategory,root);
        findSubCategories(rootNode, rootCategory);

    }

    public void findSubCategories(TreeNode treeNode,ProductsCategory category) {
        try {
            List<ProductsCategory> subcategories = new ArrayList<>();
            for (ProductsCategory item : categories) {
                //logger.info("LOG01780: ITEM, ref : " + item + " , " + item.getRefProductCategory());
                if ( (item.getRefProductCategory()!=null) &&
                (item.getRefProductCategory().getTid().equals(category.getTid())) ) {
                    //logger.info("LOG01790: Adding category : " + category);
                    subcategories.add(item);
                    TreeNode node = new DefaultTreeNode(item, treeNode);
                    findSubCategories(node, item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String update() {
        super.update(selected);
        categoriesChanged=true;
        return null;
    }

    public void delete() {
        logger.info("LOG01810: DELETING : " + selected);
        super.delete(selected);
        categoriesChanged=true;
    }

    public Long create() {
        Long pk=null;
        try {
            ProductsCategory temp = new ProductsCategory();
            temp.setNameTr(newcategory);
            temp.setActive(true);
            temp.setRefProductCategory(selected);
            pk = super.create(temp);
            categoriesChanged=true;
        } catch (Exception e) {
            e.printStackTrace();
            Util.setFacesMessageError(e.getMessage());
        }
        return pk;
    }

    public ProductsCategoryDao() {
        super(ProductsCategory.class);
    }

    public ProductsCategory getSelected() {
        return selected;
    }

    public void setSelected(ProductsCategory selected) {
        this.selected = selected;
    }

    public List<ProductsCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ProductsCategory> categories) {
        this.categories = categories;
    }

    public TreeNode getRoot() {
        if (categoriesChanged==true) {
            findCategoriesTree();
            categoriesChanged=false;
        }
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public String getNewcategory() {
        return newcategory;
    }

    public void setNewcategory(String newcategory) {
        this.newcategory = newcategory;
    }

    public DefaultTreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(DefaultTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }
}

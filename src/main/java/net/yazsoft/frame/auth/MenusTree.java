package net.yazsoft.frame.auth;

import net.yazsoft.frame.hibernate.BaseDao;
import net.yazsoft.frame.menus.MenusDao;
import net.yazsoft.frame.menus.UsersMenusDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.security.UsersDao;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Menus;
import net.yazsoft.ors.entities.Users;
import net.yazsoft.ors.entities.UsersMenus;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class MenusTree extends BaseDao<Menus> implements Serializable{
    private static final Logger logger = Logger.getLogger(MenusTree.class);

    TreeNode root;
    List<UsersMenus> allmenus;
    DefaultTreeNode selectedNode;
    Boolean listChanged=true;
    List<UsersMenus> userMenus;
    Users user;

    @Inject
    MenusDao menusDao;
    @Inject
    UsersMenusDao usersMenusDao;
    @Inject UsersDao usersDao;

    public void select() {
        user=usersDao.getItem();
        listChanged=true;
        logger.info("LOG02000: USER : " + user + " , " + user.getName());
    }

    public void saveParentMenu(UsersMenus menu) {
        for (UsersMenus item:allmenus) {
            if (item.getRefMenu().getTid().equals(menu.getRefMenu().getMainId().getTid())){
                logger.info("LOG02040:item.getRefMenu().getTid() :" +item.getRefMenu().getTid());
                logger.info("LOG02050:menu.getRefMenu().getMainId().getTid() : " + menu.getRefMenu().getMainId().getTid());
                item.setPsuper(true);
                item.setPadd(true);
                item.setPupdate(true);
                item.setPreport(true);
                item.setPdelete(true);
                item.setActive(true);
                usersMenusDao.saveOrUpdate(item);
                logger.info("LOG02030: SAVING PARENT MENU : " );
                logger.info("tid=" + item.getTid() + ", act=" + item.getActive() + ", sup=" + item.getPsuper() +
                        ", add=" + item.getPadd() + ", upd=" + item.getPupdate() + ", del=" + item.getPdelete() +
                        ", rep=" + item.getPreport() + ", menu=" + item.getRefMenu() + ", user=" + item.getRefUser() + '}');
            }
        }
    }

    public void saveMenus() {
        try {
            for (UsersMenus item : allmenus) {
                /*
                logger.info("tid=" + item.getTid() + ", act=" + item.getActive() + ", sup=" + item.getPsuper() +
                        ", add=" + item.getPadd() + ", upd=" + item.getPupdate() + ", del=" + item.getPdelete() +
                        ", rep=" + item.getPreport() + ", menu=" + item.getRefMenu() + ", user=" + item.getRefUser() + '}');
                        */
                if (item.getTid() != null) {
                    //if none selected remove from db
                    if (item.getPadd() == false && item.getPupdate() == false
                            && item.getPdelete() == false && item.getPreport() == false
                            && item.getPsuper() == false) {
                        usersMenusDao.delete(item);
                        
                    } else {
                        logger.info("tid=" + item.getTid() + ", act=" + item.getActive() + ", sup=" + item.getPsuper() +
                                ", add=" + item.getPadd() + ", upd=" + item.getPupdate() + ", del=" + item.getPdelete() +
                                ", rep=" + item.getPreport() + ", menu=" + item.getRefMenu() + ", user=" + item.getRefUser() + '}');
                        usersMenusDao.saveOrUpdate(item);
                        saveParentMenu(item);
                    }
                } else {
                    if (item.getPadd() == true || item.getPupdate() == true
                            || item.getPdelete() == true || item.getPreport() == true
                            || item.getPsuper() == true) {
                        item.setActive(true);
                        logger.info("tid=" + item.getTid() + ", act=" + item.getActive() + ", sup=" + item.getPsuper() +
                                ", add=" + item.getPadd() + ", upd=" + item.getPupdate() + ", del=" + item.getPdelete() +
                                ", rep=" + item.getPreport() + ", menu=" + item.getRefMenu() + ", user=" + item.getRefUser() + '}');
                        usersMenusDao.saveOrUpdate(item);
                        saveParentMenu(item);
                        logger.info("LOG01980: NEW USERMENU : " + item.getRefMenu().getTid());
                    }
                }
            }
            listChanged=true;
            Util.setFacesMessage("KAYIT EDILDI");
        } catch (Exception e) {
            logger.error("LOG01970:", e);
            Util.setFacesMessageError(e.getMessage());
        }
    }

    public UsersMenus findUsermenu(Menus menu) {
        userMenus=usersMenusDao.findByUser(user);
        for (UsersMenus item:userMenus) {
            if (item.getRefMenu().getTid().equals(menu.getTid())) {
                return item;
            }
        }
        return null;
    }

    public void prepareAllMenus() {
        allmenus=new ArrayList<>();
        List<Menus> menus=menusDao.getMenus(1L);

        for (Menus menu:menus) {
            UsersMenus item=findUsermenu(menu);
            if (item==null) {
                item=new UsersMenus();
                item.setRefMenu(menu);
                item.setRefUser(user);
                item.setPadd(false);
                item.setPupdate(false);
                item.setPdelete(false);
                item.setPreport(false);
                item.setPsuper(false);
            }
            allmenus.add(item);
        }
    }

    public void findMenusTree() {
        //user=usersDao.getSelected();
        if (user!=null) logger.info("LOG02020: FINDING MENUS :  USER : " + user.getName());
        prepareAllMenus();
        //logger.info("LOG01910: MENUS : " + allmenus);
        root = new DefaultTreeNode("MENULER",null);
        UsersMenus rootItem=allmenus.get(0); //(Menus)getSession().load(Menus.class,1L);
        logger.info("LOG01760: ROOT : " + rootItem);
        TreeNode rootNode = new DefaultTreeNode(rootItem,root);
        rootNode.setExpanded(true);
        rootNode.setSelectable(false);
        findSubItems(rootNode, rootItem);
    }

    public int findSubItems(TreeNode treeNode,UsersMenus menu) {
        int i=0; //count sublist size
        try {
            List<UsersMenus> sublist = new ArrayList<>();
            for (UsersMenus item : allmenus) {
                //logger.info("LOG01780: ITEM, ref : " + item + " , " + item.getMainId());
                if ( (item.getRefMenu().getMainId()!=null) &&
                        (item.getRefMenu().getMainId().getTid().equals(menu.getRefMenu().getTid())) ) {
                    //logger.info("LOG01790: Adding category : " + category);
                    i++;//increase sublist size
                    sublist.add(item);
                    TreeNode node = new DefaultTreeNode(item, treeNode);
                    node.setExpanded(true);
                    int subsize= findSubItems(node, item);
                    if (subsize>0) node.setSelectable(false); //if have submenus set nonselecteable
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    public MenusTree() {
        super(Menus.class);
    }

    public DefaultTreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(DefaultTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public TreeNode getRoot() {
        if (listChanged==true) {
            findMenusTree();
            listChanged=false;
        }
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}

/* *  YAZSOFT  */
/** @author fec */
package net.yazsoft.frame.menus;

import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Menus;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@Transactional
public class MenusBus implements Serializable{
    private MenuModel model = new DefaultMenuModel();
    private List<Menus> menus;

    @Autowired
    MenusDao menusDao;
    
    public MenuModel getMenuModel(Long menutype) {
        menus=menusDao.getMenus(menutype);
        getSubmenus(1L,null);
        return model;
    }

    public List<Menus> getSubmenus(Long menuid,DefaultSubMenu subm) {
        
        List<Menus> list=null;
        try {            
            list=findSubmenus(menuid);//dao.getSubmenus(menuid);
            for (Menus menu:list) {
                List<Menus> submenus=findSubmenus(menu.getTid());
                if (submenus.isEmpty()) {
                    DefaultMenuItem item = new DefaultMenuItem(menu.getNameTr());
                    item.setOutcome(menu.getForm());
                    if (subm==null) {
                        model.addElement(item);                         
                    } else {
                        subm.addElement(item);
                    }
                } else {
                    DefaultSubMenu submenu = new DefaultSubMenu(menu.getNameTr());
                    getSubmenus(menu.getTid(),submenu);
                    model.addElement(submenu);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }
    
    public List<Menus> findSubmenus(Long menuid) {
        List<Menus> submenus=new ArrayList<>();
        for (Menus menu:menus) {
            if (menu.getMainId().getTid().equals(menuid)) {
                submenus.add(menu);
            }
        }
        return submenus;
    }
    
}

/* *  YAZSOFT  */
/**
 * @author fec
 */
package net.yazsoft.frame.menus;

import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class MenusSer implements Serializable {

    private MenuModel model;
    private MenuModel adminmodel;

    @Autowired
    MenusBus menusBus;

    @PostConstruct
    private void init() {
        model=menusBus.getMenuModel(1L);
        adminmodel=menusBus.getMenuModel(2L);
    }

    public MenuModel getModel() {
        return model;
    }
    
    public MenuModel getAdminmodel() {
        return adminmodel;
    }
    
}

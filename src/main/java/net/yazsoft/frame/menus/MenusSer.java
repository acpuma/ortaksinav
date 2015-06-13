/* *  YAZSOFT  */
/**
 * @author fec
 */
package net.yazsoft.frame.menus;

import net.yazsoft.frame.scopes.ViewScoped;
import org.apache.log4j.Logger;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class MenusSer implements Serializable {

    private static final Logger logger = Logger.getLogger(MenusSer.class);
    private MenuModel model;
    private MenuModel webmodel;

    @Autowired
    MenusBus menusBus;

    @PostConstruct
    private void init() {
        logger.info("MENUSSER CONSTRUCTOR");
    }

    public MenuModel getModel() {
        if (model==null) {
            model=menusBus.getMenuModel(1L);
            logger.info("LOG01710: model : " + model.getElements().size());
        }
        return model;
    }
    
    public MenuModel getWebmodel() {
        if (webmodel==null) {
            webmodel=menusBus.getMenuModel(2L);
        }
        return webmodel;
    }

}

package net.yazsoft.ors.zloglogin;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.ZlogLogin;
import org.apache.log4j.Logger;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ZLogLoginDao extends BaseGridDao<ZlogLogin> implements Serializable{
    private static final Logger logger = Logger.getLogger(ZLogLoginDao.class);
    ZlogLogin selected;

    public ZLogLoginDao() {
        super(ZlogLogin.class);
    }

    public ZlogLogin getSelected() {
        return selected;
    }

    public void setSelected(ZlogLogin selected) {
        this.selected = selected;
    }

}

package net.yazsoft.ors.settings;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.ProductsType;
import net.yazsoft.ors.entities.Settings;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class SettingsDao extends BaseGridDao<Settings> implements Serializable{
    private Settings selected;
    private List<Settings> settings;

    public Settings findByName(String name) {
        Settings setting=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("name", name));
            setting = (Settings) c.uniqueResult();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return setting;
    }

    public Long save() {
        try {
            for (Settings setting:settings) {
                super.saveOrUpdate(setting);
            }
            Util.setFacesMessage("AYARLAR GUNCELLENDI");

        } catch (Exception e) {
            Util.catchException(e);
        }

        return null;
    }

    public SettingsDao() {
        super(Settings.class);
    }

    public Settings getSelected() {
        return selected;
    }

    public void setSelected(Settings selected) {
        this.selected = selected;
    }

    public List<Settings> getSettings() {
        settings=getAll();
        return settings;
    }

    public void setSettings(List<Settings> settings) {
        this.settings = settings;
    }
}

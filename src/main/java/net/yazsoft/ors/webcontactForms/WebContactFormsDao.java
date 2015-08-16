package net.yazsoft.ors.webcontactForms;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.mail.Email;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.WebContactForms;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class WebContactFormsDao extends BaseGridDao<WebContactForms> implements Serializable{
    private static final Logger logger = Logger.getLogger(WebContactFormsDao.class);
    WebContactForms selected;
    List<WebContactForms> items;
    Boolean itemsChanged=true;

    @Inject Email email;

    public void sendMessage() {
        try {
            getItem().setActive(true);
            getItem().setCreated(Util.getNow());
            create();
            String body=" İletişim Formu - Yeni Mesaj : "
                    +"\nAd : "  + getItem().getName()
                    +"\nTelefon : " + getItem().getPhone()
                    +"\nEposta : " + getItem().getEmail()
                    +"\nMesaj : " + getItem().getMessage();
            email.sendMail(Constants.EMAIL_NOTIFICATION, "Ortak Sinav Iletisim Formu Yeni Mesaj", body);
            //Util.setFacesMessage("MESAJ GÖNDERİLDİ");
            reset();
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void delete() {
        super.delete();
        itemsChanged=true;
    }

    public List<WebContactForms> findItems() {
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
            itemsChanged=false;
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public WebContactFormsDao() {
        super(WebContactForms.class);
    }

    public WebContactForms getSelected() {
        return selected;
    }

    public void setSelected(WebContactForms selected) {
        this.selected = selected;
    }

    public List<WebContactForms> getItems() {
        if (itemsChanged) {
            items=findItems();
        }
        return items;
    }

    public void setItems(List<WebContactForms> items) {
        this.items = items;
    }


}

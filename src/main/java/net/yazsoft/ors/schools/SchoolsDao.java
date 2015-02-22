package net.yazsoft.ors.schools;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.ors.entities.Schools;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
@Transactional
public class SchoolsDao extends BaseGridDao<Schools> implements Serializable{
    Schools selected;

    public SchoolsDao() {
        super(Schools.class);
    }

    @Override
    public Long create() {
        getItem().setActive(Boolean.TRUE);
        return super.create();
    }

    public Schools getSelected() {
        return selected;
    }

    public void setSelected(Schools selected) {
        this.selected = selected;
    }
}

package net.yazsoft.frame.hibernate;

import org.hibernate.envers.RevisionListener;

/** * @author fec */
public class HibRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object o) {
        HibRevisionEntity entity=(HibRevisionEntity) o;
        //entity.setUser(Util.getUserInfo());
        entity.setUpdate_date(entity.getRevisionDate());
    }
}

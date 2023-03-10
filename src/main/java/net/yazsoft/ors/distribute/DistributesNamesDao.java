package net.yazsoft.ors.distribute;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.optic.OpticDao;
import net.yazsoft.ors.schools.SchoolsClassDao;
import net.yazsoft.ors.schools.SchoolsClassDto;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


@Named
@ViewScoped
public class DistributesNamesDao extends BaseGridDao<DistributesNames> implements Serializable{

    private static final Logger log = Logger.getLogger(DistributesNamesDao.class);

    @Inject DistributeDao distributeDao;
    @Inject OpticDao opticDao;

    public DistributesNamesDao() {
        super(DistributesNames.class);
    }

    @Transactional
    public void delete() {

        for (Distributes distribute:distributeDao.getDistributeName().getDistributesCollection()) {
            distributeDao.delete(distribute);
        }
        for (Optics optic:distributeDao.getDistributeName().getOpticsCollection()) {
            optic.setRefDistributeName(null);
            opticDao.update(optic);
        }
        super.delete(distributeDao.getDistributeName());
    }


}

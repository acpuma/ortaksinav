package net.yazsoft.ors.schools;

import net.yazsoft.frame.hibernate.BaseSer;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Schools;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
@Transactional
public class SchoolsSer extends BaseSer<Schools> {
    private Logger logger = Logger.getLogger(SchoolsSer.class);

    @Inject
    SchoolsDao schoolsDao;

    @Inject
    SchoolsLazy lazyModel;

    @Transactional
    public LazyDataModel<Schools> getLazyModel() {
        return lazyModel;
    }
}

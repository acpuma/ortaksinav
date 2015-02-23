package net.yazsoft.ors.lessonsName;

import net.yazsoft.frame.hibernate.BaseSer;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.LessonsName;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class LessonsNameSer extends BaseSer<LessonsName> {
    private Logger logger = Logger.getLogger(LessonsNameSer.class);

    @Inject
    LessonsNameDao lessonsNameDao;

    @Inject
    LessonsNameLazy lazyModel;

    @Transactional
    public LazyDataModel<LessonsName> getLazyModel() {
        return lazyModel;
    }
}

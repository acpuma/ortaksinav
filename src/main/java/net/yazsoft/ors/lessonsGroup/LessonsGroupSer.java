package net.yazsoft.ors.lessonsGroup;

import net.yazsoft.frame.hibernate.BaseSer;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.LessonsGroup;
import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class LessonsGroupSer extends BaseSer<LessonsGroup> {
    private Logger logger = Logger.getLogger(LessonsGroupSer.class);

    @Inject
    LessonsGroupDao lessonsGroupDao;

    @Inject
    LessonsGroupLazy lazyModel;

    @Transactional
    public LazyDataModel<LessonsGroup> getLazyModel() {
        return lazyModel;
    }
}

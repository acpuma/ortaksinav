package net.yazsoft.ors.exams;

import net.yazsoft.frame.hibernate.BaseSer;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.Exams;
import org.primefaces.model.LazyDataModel;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class ExamsSer extends BaseSer<Exams> {

    @Inject
    ExamsDao examsDao;

    @Inject
    ExamsLazy lazyModel;

    @Transactional
    public LazyDataModel<Exams> getLazyModel() {
        return lazyModel;
    }
}

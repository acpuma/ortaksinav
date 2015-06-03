package net.yazsoft.ors.answers;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.AnswersQuestionType;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class AnswersQuestionTypeDao extends BaseGridDao<AnswersQuestionType> implements Serializable{
    AnswersQuestionType selected;

    public AnswersQuestionTypeDao() {
        super(AnswersQuestionType.class);
    }

    public AnswersQuestionType getSelected() {
        return selected;
    }

    public void setSelected(AnswersQuestionType selected) {
        this.selected = selected;
    }
}

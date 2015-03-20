package net.yazsoft.ors.answers;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.ors.entities.AnswersCancelType;

import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class AnswersCancelTypeDao extends BaseGridDao<AnswersCancelType> implements Serializable{
    AnswersCancelType selected;

    public AnswersCancelTypeDao() {
        super(AnswersCancelType.class);
    }

    public AnswersCancelType getSelected() {
        return selected;
    }

    public void setSelected(AnswersCancelType selected) {
        this.selected = selected;
    }
}

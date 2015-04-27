package net.yazsoft.ors.answers;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.exams.ExamsAnswerTypeDao;
import net.yazsoft.ors.lessons.LessonsDao;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class AnswersDao extends BaseGridDao<Answers> implements Serializable{
    private static final Logger logger = Logger.getLogger(AnswersDao.class);
    Answers selected;
    Exams exam;
    Lessons lesson;
    ExamsBookletType booklet;
    Integer bookletCount;
    Integer questionCount;

    List<AnswersDto> answersDtos;
    List<AnswersDto> deletedDtos;
    List<Lessons> lessons;
    List<AnswersSubjectType> subjects;
    List<ExamsAnswerType> answerTypes;
    List<AnswersAuto> answersAutos; //for reading answers from file

    @Inject LessonsDao lessonsDao;
    @Inject AnswersSubjectTypeDao answersSubjectTypeDao;
    @Inject AnswersQuestionTypeDao answersQuestionTypeDao;
    @Inject AnswersCancelTypeDao answersCancelTypeDao;
    @Inject ExamsAnswerTypeDao examsAnswerTypeDao;

    public AnswersDao() {
        super(Answers.class);
    }

    @PostConstruct
    public void init() {
        answersDtos=new ArrayList<>();
        deletedDtos=new ArrayList<>();
        subjects=new ArrayList<>();
        answerTypes=new ArrayList<>();
        answersAutos=new ArrayList<>();
        getItem().setRefAnswerSubject(new AnswersSubjectType());
        getItem().setRefAnswerQuestion(new AnswersQuestionType());
        getItem().setRefAnswerCancel(new AnswersCancelType());
        getItem().setRefLesson(new Lessons());
        bookletCount=1;
    }

    @Override
    public Long create() {
        try {
            logger.info("CREATE ANSWERS: " + answersDtos);
            logger.info("CREATE lesson : " + lesson);
            getItem().setActive(Boolean.TRUE);
            DtosToEntities(lesson);
            reset();
        } catch (Exception e) {
            logger.error("EXCEPTION", e);
            Util.setFacesMessage("HATA: " +e.getMessage());
        }
        return null;
    }

    public void onLessonChange() {
        logger.info("LESSON CHANGE : " + lesson.getTid() + " : " +lesson.getRefLessonName().getNameTr());
        logger.info("LESSON EXAM : "+ exam.getTid() + " : " + exam.getNameTr());
        subjects=answersSubjectTypeDao.getLessonSubjects(lesson.getRefLessonName());
        questionCount=lesson.getQuestionCount();
        bookletCount=exam.getRefBookletType().getTid().intValue();
        answerTypes=examsAnswerTypeDao.getExamAnswerTypes(exam);
        logger.info("LESSON QUESTION COUNT : " + questionCount);
        logger.info("EXAM BOOKLET COUNT : " + bookletCount);
        getLessonAnswers(exam, lesson);
        if (answersDtos.size()==0) {
            AnswersDto dto;
            //AnswersSubjectType subjectType=answersSubjectTypeDao.getById(1L);
            AnswersQuestionType questionType=answersQuestionTypeDao.getById(1L);
            AnswersCancelType cancelType=answersCancelTypeDao.getById(1L);
            ExamsAnswerType answerType=examsAnswerTypeDao.getById(1L);
            for (int i=1; i<=questionCount; i++) {
                dto=new AnswersDto();
                //dto.setRefAnswerSubject(subjectType);
                dto.setRefAnswerQuestion(questionType);
                dto.setRefAnswerCancel(cancelType);
                dto.setRank(i);
                dto.setAnsA(answerType.getName());
                answersDtos.add(dto);
            }
        }
    }

    public List<Answers> getLessonAnswers(Exams exam1,Lessons lesson1) {
        answersDtos=new ArrayList<>();
        //logger.info("EXAM,LESSON : "+ exam1+","+lesson1);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refExam", exam1));
            c.add(Restrictions.eq("refLesson", lesson1));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        List<Answers> answers=list;
        for (Answers answer:answers) {
            AnswersDto dto=new AnswersDto(answer);
            answersDtos.add(dto);
        }
        logger.info("ANSWERS SIZE :" + answersDtos.size());
        return list;
    }

    public List<Answers> getExamAnswers(Exams exam1) {
        answersDtos=new ArrayList<>();
        //logger.info("EXAM,LESSON : "+ exam1+","+lesson1);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refExam", exam1));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        List<Answers> answers=list;
        for (Answers answer:answers) {
            AnswersDto dto=new AnswersDto(answer);
            answersDtos.add(dto);
        }
        logger.info("ANSWERS SIZE :" + answersDtos.size());
        return list;
    }

    private AnswersDto findAnswer(Long exam1,Long lesson1,int rank) {
        for (AnswersDto dto:answersDtos) {
            Long examTid=exam1;
            Long dtoTid=dto.getRefExam().getTid();
            Long lessonTid=dto.getRefLesson().getTid();
            if (dtoTid.equals(exam1) &&
                    (lessonTid.equals(lesson1))
                    && (dto.getRank()==rank)){
                //logger.info("ANSWER FOUND : " + dto);
                return dto;
            }
        }
        //logger.info("ANSWER NOT FOUND");
        return null;
    }

    public void prepareAutoAnswers(Exams exam1, Lessons lesson1,String booklet,String answersStr){
        AnswersAuto auto=new AnswersAuto();
        auto.setExam(exam1);
        auto.setLesson(lesson1);
        auto.setBooklet(booklet);
        auto.setAnswers(answersStr);
        answersAutos.add(auto);
        logger.info("AUTO ANSWER : " + auto);
    }

    @Transactional
    public void saveAutoAnswers(Exams exam1) {
        getExamAnswers(exam1);
        for (AnswersAuto auto:answersAutos) {
            setLessonAnswers(auto.getExam(),auto.getLesson(),auto.getBooklet(),auto.getAnswers());
        }
        Answers entity=null;
        for (AnswersDto dto:answersDtos) {
            if (dto.getTid() != null) {
                entity = (Answers) getSession().load(Answers.class, dto.getTid());
            }
            saveOrUpdate(dto.toEntity(entity));
        }
        answersAutos.clear();
        answersDtos.clear();
    }

    @Transactional
    public void setLessonAnswers(Exams exam1, Lessons lesson1,String booklet,String answersStr) {
        List<AnswersDto> dtoList;
        AnswersDto dto;
        Boolean addDto;
        for (int i=0; i<lesson1.getQuestionCount(); i++) {
            addDto=false;
            if (answersDtos==null) {
                dto=new AnswersDto();
                addDto=true;
            } else {
                dto=findAnswer(exam1.getTid(),lesson1.getTid(),i+1);
                if (dto==null) {
                    dto=new AnswersDto();
                    addDto=true;
                }
            }
            dto.setRank(i + 1);
            dto.setRefExam( (Exams) getSession().load(Exams.class, exam1.getTid()) );
            dto.setRefLesson( (Lessons) getSession().load(Lessons.class, lesson1.getTid()) );
            switch (booklet) {
                case "A" : dto.setAnsA(String.valueOf(answersStr.charAt(i))); break;
                case "B" : dto.setAnsB(String.valueOf(answersStr.charAt(i))); break;
                case "C" : dto.setAnsC(String.valueOf(answersStr.charAt(i))); break;
                case "D" : dto.setAnsD(String.valueOf(answersStr.charAt(i))); break;
                case "E" : dto.setAnsE(String.valueOf(answersStr.charAt(i))); break;
                case "F" : dto.setAnsF(String.valueOf(answersStr.charAt(i))); break;
                case "G" : dto.setAnsG(String.valueOf(answersStr.charAt(i))); break;
                case "H" : dto.setAnsH(String.valueOf(answersStr.charAt(i))); break;
            }
            dto.setActive(Boolean.TRUE);
            if (addDto){
                answersDtos.add(dto);
            }
        }
    }

    /**
     * Gets lessons answers as map which includes booklet number and booklet answers as string
     * @param exam1
     * @param lesson1
     * @return
     */
    public Map<Integer,String> getLessonAnswersMap(Exams exam1,Lessons lesson1) {
        getLessonAnswers(exam1, lesson1);
        Map<Integer,String> answersMap = new LinkedHashMap<>();
        String answers;
        for (int i=0; i<exam1.getRefBookletType().getTid();i++) {
            answers="";
            for (AnswersDto dto : answersDtos) {
                switch (i){
                    case 0: answers = answers.concat(dto.getAnsA());break;
                    case 1: answers = answers.concat(dto.getAnsB());break;
                    case 2: answers = answers.concat(dto.getAnsC());break;
                    case 3: answers = answers.concat(dto.getAnsD());break;
                    case 4: answers = answers.concat(dto.getAnsE());break;
                    case 5: answers = answers.concat(dto.getAnsF());break;
                    case 6: answers = answers.concat(dto.getAnsG());break;
                    case 7: answers = answers.concat(dto.getAnsH());break;
                }
            }
            answersMap.put(i,answers);
        }
        return answersMap;
    }

    public void DtosToEntities(Lessons lesson) {
        Answers entity;
        for (AnswersDto dto:answersDtos) {
            if (dto.getTid()!=null) {
                entity=(Answers)getSession().get(Answers.class,dto.getTid());
            } else {
                entity=new Answers();
            }
            entity=dto.toEntity(entity);
            entity.setRefExam(exam);
            entity.setRefLesson(lesson);
            entity.setActive(Boolean.TRUE);
            logger.info("ANSWER ENTITY :" + entity);
            saveOrUpdate(entity);
        }
    }

    public void addAnswers() {
        logger.info("ANSWERS :" + answersDtos);
        int maxrank=0;
        if (answersDtos==null) {
            answersDtos=new ArrayList<AnswersDto>();
        }
        AnswersDto newdto=new AnswersDto();
        answersDtos.add(newdto);
        logger.info("LESSONS :" + answersDtos);
    }

    public void delete(AnswersDto deleteDto) {
        logger.info("REMOVING : " + deleteDto);
        answersDtos.remove(deleteDto);
        if (deleteDto.getTid()!=null) {
            deletedDtos.add(deleteDto);
        }
        //Util.setFacesMessage(deleteDto+" SILINDI");
    }

    public void deleteEntites() {
        Answers entity;
        for (AnswersDto dto:deletedDtos) {
            entity = new Answers();
            entity=(Answers)getSession().get(Answers.class,dto.getTid());
        }
    }

    public void setExam(Exams exam) {
        this.exam = exam;
        lessons=lessonsDao.getExamLessons(exam);
    }

    public Answers getSelected() {
        return selected;
    }

    public void setSelected(Answers selected) {
        this.selected = selected;
    }

    public Exams getExam() {
        return exam;
    }


    public Lessons getLesson() {
        return lesson;
    }

    public void setLesson(Lessons lesson) {
        this.lesson = lesson;
    }

    public List<AnswersDto> getAnswersDtos() {
        return answersDtos;
    }

    public void setAnswersDtos(List<AnswersDto> answersDtos) {
        this.answersDtos = answersDtos;
    }

    public ExamsBookletType getBooklet() {
        return booklet;
    }

    public void setBooklet(ExamsBookletType booklet) {
        this.booklet = booklet;
    }

    public Integer getBookletCount() {
        return bookletCount;
    }

    public void setBookletCount(Integer bookletCount) {
        this.bookletCount = bookletCount;
    }

    public List<AnswersDto> getDeletedDtos() {
        return deletedDtos;
    }

    public void setDeletedDtos(List<AnswersDto> deletedDtos) {
        this.deletedDtos = deletedDtos;
    }

    public List<Lessons> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lessons> lessons) {
        this.lessons = lessons;
    }

    public LessonsDao getLessonsDao() {
        return lessonsDao;
    }

    public void setLessonsDao(LessonsDao lessonsDao) {
        this.lessonsDao = lessonsDao;
    }

    public Integer getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(Integer questionCount) {
        this.questionCount = questionCount;
    }

    public List<AnswersSubjectType> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<AnswersSubjectType> subjects) {
        this.subjects = subjects;
    }

    public List<ExamsAnswerType> getAnswerTypes() {
        return answerTypes;
    }

    public void setAnswerTypes(List<ExamsAnswerType> answerTypes) {
        this.answerTypes = answerTypes;
    }

    public List<AnswersAuto> getAnswersAutos() {
        return answersAutos;
    }

    public void setAnswersAutos(List<AnswersAuto> answersAutos) {
        this.answersAutos = answersAutos;
    }
}


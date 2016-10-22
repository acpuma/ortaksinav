package net.yazsoft.ors.results;

import net.sf.jasperreports.engine.JRParameter;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.mail.Email;
import net.yazsoft.frame.mail.EmailAttach;
import net.yazsoft.frame.report.Report;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.security.SessionInfo;
import net.yazsoft.frame.sms.SmsDao;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.students.StudentsAnswersDao;
import org.apache.log4j.Logger;
import org.hibernate.Query;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class ResultsSendDao extends BaseGridDao<ResultsSend> implements Serializable{
    private static final Logger logger = Logger.getLogger(ResultsSendDao.class);

    ResultsSend selected;
    List<ResultsSend> items;

    Boolean bEmail;
    Boolean bSms;
    Boolean bReportLesson;
    Boolean bReportLessonAverage;
    Boolean bReportLessonScore;

    List<String> selectedClasses;
    List<String> selectedLessons;
    List<Users> selectedUsers;
    Lessons selectedLesson;
    List<String> mailFiles;

    String mailTitle;
    String mailMessage;

    Users newUser;

    String selectedClassesLabel="Seçiniz";
    String selectedLessonsLabel="Seçiniz";

    @Inject ResultsDao resultsDao;
    @Inject EmailAttach emailAttach;
    @Inject Email email;
    @Inject StudentsAnswersDao studentsAnswersDao;
    @Inject Report report;
    @Inject SmsDao smsDao;

    public void populateSelectedLessonsLabel() {
        selectedLessonsLabel = new String("");
        if (selectedLessons.size() == 0) {
            selectedLessonsLabel = "Seçiniz";
        } else {
            for (int i = 0; i < selectedLessons.size(); i++) {
                Lessons slesson=(Lessons)getSession().load(Lessons.class,Long.valueOf(selectedLessons.get(i)));
                if (selectedLessonsLabel.length() == 0) {
                    selectedLessonsLabel = slesson.getRefLessonName().getNameTr();
                } else {
                    if (selectedLessonsLabel.length()<18) {
                        selectedLessonsLabel = selectedLessonsLabel + ","
                                + slesson.getRefLessonName().getNameTr();
                    }
                }
            }
        }
    }

    public void populateSelectedClassLabel() {
        selectedClassesLabel = new String("");
        if (selectedClasses.size() == 0) {
            selectedClassesLabel = "Seçiniz";
        } else {
            for (int i = 0; i < selectedClasses.size(); i++) {
                SchoolsClass sclass=(SchoolsClass)getSession().load(SchoolsClass.class,Long.valueOf(selectedClasses.get(i)));
                if (selectedClassesLabel.length() == 0) {
                    selectedClassesLabel = sclass.getName();
                } else {
                    if (selectedClassesLabel.length()<18) {
                        selectedClassesLabel = selectedClassesLabel + "," + sclass.getName();
                    }
                }
            }
        }
    }

    public void addUser() {
        System.out.println("ADDING USER : ");
        smsDao.getUsers().add(newUser);
        newUser=new Users();
    }

    /**
     * report lessons according to reportType as defined in Constants class
     * @param reportType REPORT_LESSON_CLASS group by class, REPORT_LESSON_SCORE list by score
     */
    public void reportLesson(int reportType) {
        if (selectedLesson==null) {
            Util.setFacesMessageError("Ders Seciniz");
            return;
        }
        if (selectedClasses==null) {
            Util.setFacesMessageError("Sinif Seciniz");
            return;
        }
        //mailFiles=new ArrayList<>();
        logger.info("LOG01600: REPORT LESSON : " + selectedLesson + " : " + selectedLesson.getRefLessonName().getNameTr());
        logger.info("LOG01610: selelectedClasses : " + selectedClasses);
        Map<String, Object> params = new HashMap<>();
        params.put("pSchoolName", Util.getActiveSchool().getName());
        params.put("pexam", Util.getActiveExam().getTid());
        params.put("plesson", selectedLesson.getTid());
        params.put("pLessonName", selectedLesson.getRefLessonName().getNameTr());
        params.put("pdate",Util.getActiveExam().getDate());
        Date now=Calendar.getInstance(new Locale("TR")).getTime();
        params.put("pnow",now);
        if (Util.getActiveSchool().getRefCity()!=null) {
            params.put("pil", Util.getActiveSchool().getRefCity().getName().toUpperCase());
        }
        if (Util.getActiveSchool().getRefTown()!=null) {
            params.put("pilce",Util.getActiveSchool().getRefTown().getName().toUpperCase());
        }
        params.put("pyil",Util.getActiveExam().getRefExamYear().getName());
        params.put("pdonem",Util.getActiveExam().getRefExamSeason().getNameTr());
        params.put("psinavno",Util.getActiveExam().getRefExamSeasonNumber().getName());
        if (Util.getActiveSchool().getRefImage()!=null) {
            params.put("plogo", "http://www.ortaksinav.com.tr/images/school/" + Util.getActiveSchool().getTid()
                    + "." + Util.getActiveSchool().getRefImage().getExtension());
        }
        Locale trlocale= Locale.forLanguageTag("tr-TR");
        params.put(JRParameter.REPORT_LOCALE, trlocale);

        /*
        for (String sclassStr:selectedClasses) {
            logger.info("LOG02580: CLASSSTR : " + sclassStr );
            SchoolsClass sclass=(SchoolsClass) getSession().load(SchoolsClass.class,Long.valueOf(sclassStr));
            if (studentsAnswersDao.findByExamAndLessonAndClass(Util.getActiveExam(),selectedLesson,sclass).size()>0) {
                List<String> selectedC=new ArrayList<>();
                selectedC.add(sclassStr);
                params.put("pclasses",selectedC);
                report.pdfFile("repLesson", params, sclass.getName()+"_"+selectedLesson.getRefLessonName().getNameTr());
                mailFiles.add(sclass.getName()+"_"+selectedLesson.getRefLessonName().getNameTr()+".pdf");
            }
        }
        */

        params.put("pclasses",selectedClasses);
        if (reportType == Constants.REPORT_LESSON_CLASS) {
            report.pdfFile("repLesson", params, selectedLesson.getRefLessonName().getNameTr());
            mailFiles.add(selectedLesson.getRefLessonName().getNameTr() + ".pdf");
        }
        if (reportType == Constants.REPORT_LESSON_SCORE) {
            report.pdfFile("repLessonScore", params, selectedLesson.getRefLessonName().getNameTr() + "Puan");
            mailFiles.add(selectedLesson.getRefLessonName().getNameTr() + "Puan.pdf");
        }
    }

    public void reportLessonAverage() {
        if (selectedLesson==null) {
            Util.setFacesMessageError("Ders Seciniz");
            return;
        }
        if (selectedClasses==null) {
            Util.setFacesMessageError("Sinif Seciniz");
            return;
        }

        logger.info("LOG01610: selelectedClasses : " + selectedClasses);
        //logger.info("LOG01610: CLASSESSTR : " + selectedClassesStr);
        logger.info("LOG01600: REPORT LESSON : " + selectedLesson.getRefLessonName().getNameTr());

        Map<String, Object> params = new HashMap<>();
        params.put("pSchoolName", Util.getActiveSchool().getName());
        params.put("pexam", Util.getActiveExam().getTid());
        params.put("plesson", selectedLesson.getTid());
        params.put("pLessonName", selectedLesson.getRefLessonName().getNameTr());
        List<Long> pclasseslong=new ArrayList<>();
        for (String sclass:selectedClasses) {
            pclasseslong.add(Long.valueOf(sclass));
        }
//        params.put("pclasses",pclasseslong);
        params.put("pclasses",selectedClasses);
        List<String> pclassesstr=new ArrayList<>();
        for (Long pid:pclasseslong) {
            SchoolsClass sclass=(SchoolsClass)getSession().load(SchoolsClass.class,pid);
            pclassesstr.add(sclass.getName());
        }
        params.put("pclassesstr",pclassesstr.toString());
        params.put("pdate",Util.getActiveExam().getDate());
        Date now=Calendar.getInstance(new Locale("TR")).getTime();
        params.put("pnow",now);
        if (Util.getActiveSchool().getRefCity()!=null) {
            params.put("pil", Util.getActiveSchool().getRefCity().getName().toUpperCase());
        }
        if (Util.getActiveSchool().getRefTown()!=null) {
            params.put("pilce", Util.getActiveSchool().getRefTown().getName().toUpperCase());
        }
        params.put("pyil",Util.getActiveExam().getRefExamYear().getName());
        params.put("pdonem",Util.getActiveExam().getRefExamSeason().getNameTr());
        params.put("psinavno",Util.getActiveExam().getRefExamSeasonNumber().getName());
        if (Util.getActiveSchool().getRefImage()!=null) {
            params.put("plogo", "http://www.ortaksinav.com.tr/images/school/" + Util.getActiveSchool().getTid()
                    + "." + Util.getActiveSchool().getRefImage().getExtension());
        }
        Locale trlocale= Locale.forLanguageTag("tr-TR");
        params.put(JRParameter.REPORT_LOCALE, trlocale);
        report.pdfFile("repLessonOrtalama", params, selectedLesson.getRefLessonName().getNameTr() + "Ortalama");
        mailFiles.add(selectedLesson.getRefLessonName().getNameTr()+"Ortalama.pdf");
    }



    public void send() {
        try {
            logger.info("LOG02540: SELECTED : " + selectedLessons);
            mailFiles=new ArrayList<>();
            if (bEmail) {
                //System.setProperty("mail.mime.decodetext.strict","false"); //for filename encoding problem
                for (String lessonStr : selectedLessons) {
                    Lessons lesson = (Lessons) getSession().load(Lessons.class, Long.valueOf(lessonStr));
                    if ((bReportLesson) && (selectedClasses.size() > 0)) {
                        selectedLesson = lesson;
                        if (bReportLesson) reportLesson(Constants.REPORT_LESSON_CLASS);
                        if (bReportLessonScore) reportLesson(Constants.REPORT_LESSON_SCORE);
                        if (bReportLessonAverage) reportLessonAverage();
                    }
                }

                logger.info("LOG02540: " + this.toString());
                String mailFolder = "temp";
                emailAttach.setMailFolder(mailFolder);
                emailAttach.setMailFiles(mailFiles);
                String[] emails=new String[selectedUsers.size()];
                for (int i=0; i<selectedUsers.size(); i++) {
                    emails[i]=selectedUsers.get(i).getEmail();
                }
                //emailAttach.sendMailAtachment("cumanji@hotmail.com", "ATCH TEST", "testtt");

                emailAttach.sendMailAtachment("info@ortaksinav.com.tr", mailTitle, mailMessage,emails);

                for (String mailFile : mailFiles) {
                    File file = new File(Util.getUploadsFolder() + "/" + mailFolder + "/" + mailFile);
                    file.delete();
                }
            }

            if (bSms) {
                if (Util.getSessionInfo().getUser().getSmsCount()-selectedUsers.size()<0) {
                    Util.setFacesMessageError("SMS göndermek için Kalan SMS miktarınız yetersizdir.");
                } else {
                    ArrayList<String> phones = new ArrayList<>();
                    for (Users user : selectedUsers) {
                        phones.add(user.getPhone());
                    }
                    smsDao.setPhones(phones);
                    smsDao.sendSms();
                    if (Util.getSessionInfo().getUser().getSmsCount() > 0) {
                        Util.getSessionInfo().getUser().setSmsCount(Util.getSessionInfo().getUser().getSmsCount() - phones.size());
                        Util.getSessionInfo().getUser().setSmsTotal(Util.getSessionInfo().getUser().getSmsTotal() + phones.size());
                    }
                }
            }

            selected.setActive(true);
            selected.setEmail(bEmail);
            selected.setEmailTitle(mailTitle);
            selected.setEmailMessage(mailMessage);
            selected.setSms(bSms);
            selected.setSmsTitle(smsDao.getSender());
            selected.setSmsMessage(smsDao.getMesaj());
            selected.setReportLesson(bReportLesson);
            selected.setReportAverage(bReportLessonAverage);
            List<String> classesstr=new ArrayList<>();
            for (String pid:selectedClasses) {
                SchoolsClass sclass=(SchoolsClass)getSession().load(SchoolsClass.class,Long.valueOf(pid));
                classesstr.add(sclass.getName());
            }
            selected.setClasses(classesstr.toString());
            List<String> lessonsstr=new ArrayList<>();
            for (String pid:selectedLessons) {
                Lessons slesson=(Lessons)getSession().load(Lessons.class,Long.valueOf(pid));
                lessonsstr.add(slesson.getRefLessonName().getNameTr());
            }
            selected.setLessons(lessonsstr.toString());
            List<String> usersstr=new ArrayList<>();
            for (Users user:selectedUsers) {
                usersstr.add(user.getName() + " " + user.getSurname() + " : "+ user.getEmail() +" : "+  user.getPhone());
            }
            selected.setUsers(usersstr.toString());
            selected.setRefSchool(Util.getActiveSchool());
            selected.setRefExam(Util.getActiveExam());
            selected.setRefUser(Util.getActiveUser());
            selected.setCreated(Util.getNow());
            create(selected);
            //email.sendMail("cumacakmak@gmail.com", "ATCH TEST", "testtt");
            //Util.setFacesMessage(this.toString());
            Util.setFacesMessage("GONDERILDI");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    @PostConstruct
    public void init() {
        bEmail=true;
        bSms=true;
        bReportLesson=true;
        bReportLessonAverage=true;
        bReportLessonScore=true;
        selectedLessons=new ArrayList<>();
        selectedClasses=new ArrayList<>();
        selectedUsers=new ArrayList<>();
        mailFiles=new ArrayList<>();
        selected=new ResultsSend();
        newUser=new Users();
    }

    public void delete(ResultsSend rs) {
        super.delete(rs);
        items=null;
    }

    public ResultsSendDao() {
        super(ResultsSend.class);
    }

    public Boolean getbEmail() {
        return bEmail;
    }

    public void setbEmail(Boolean bEmail) {
        this.bEmail = bEmail;
    }

    public Boolean getbSms() {
        return bSms;
    }

    public void setbSms(Boolean bSms) {
        this.bSms = bSms;
    }

    public Boolean getbReportLesson() {
        return bReportLesson;
    }

    public void setbReportLesson(Boolean bReportLesson) {
        this.bReportLesson = bReportLesson;
    }

    public Boolean getbReportLessonAverage() {
        return bReportLessonAverage;
    }

    public void setbReportLessonAverage(Boolean bReportLessonAverage) {
        this.bReportLessonAverage = bReportLessonAverage;
    }

    public List<String> getSelectedClasses() {
        return selectedClasses;
    }

    public void setSelectedClasses(List<String> selectedClasses) {
        this.selectedClasses = selectedClasses;
    }

    public List<String> getSelectedLessons() {
        return selectedLessons;
    }

    public void setSelectedLessons(List<String> selectedLessons) {
        this.selectedLessons = selectedLessons;
    }

    public List<Users> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<Users> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public String getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(String mailMessage) {
        this.mailMessage = mailMessage;
    }

    public List<ResultsSend> getItems() {
        if (items==null) {
            items=getAll();
        }
        return items;
    }

    public Users getNewUser() {
        return newUser;
    }

    public void setNewUser(Users newUser) {
        this.newUser = newUser;
    }

    public void setItems(List<ResultsSend> items) {
        this.items = items;
    }

    public Boolean getbReportLessonScore() {
        return bReportLessonScore;
    }

    public void setbReportLessonScore(Boolean bReportLessonScore) {
        this.bReportLessonScore = bReportLessonScore;
    }

    public String getSelectedClassesLabel() {
        return selectedClassesLabel;
    }

    public void setSelectedClassesLabel(String selectedClassesLabel) {
        this.selectedClassesLabel = selectedClassesLabel;
    }

    public String getSelectedLessonsLabel() {
        return selectedLessonsLabel;
    }

    public void setSelectedLessonsLabel(String selectedLessonsLabel) {
        this.selectedLessonsLabel = selectedLessonsLabel;
    }

    @Override
    public String toString() {
        return
                "email=" + bEmail +
                ", sms=" + bSms +
                ", reportLesson=" + bReportLesson +
                ", reportLessonAverage=" + bReportLessonAverage +
                ", selectedClasses=" + selectedClasses +
                ", selectedLessons=" + selectedLessons +
                ", selectedUsers=" + selectedUsers ;
    }
}

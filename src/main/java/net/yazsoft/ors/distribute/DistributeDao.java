package net.yazsoft.ors.distribute;

import net.sf.jasperreports.engine.JRParameter;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.report.Report;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.schools.SchoolsClassDao;
import net.yazsoft.ors.schools.SchoolsClassDto;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import sun.rmi.log.LogInputStream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.text.Collator;
import java.util.*;


@Named
@ViewScoped
public class DistributeDao extends BaseGridDao<Distributes> implements Serializable{

    private static final Logger log = Logger.getLogger(DistributeDao.class);
    Distributes selected;
    List<SchoolsClassDto> schoolsClassDtos;
    Boolean bUseRoomName;
    Boolean bRandom;
    Boolean bButterfly;
    DistributesNames distributeName;
    String distributeNameInput;
    String distributeType;

    List<SchoolsClassDto> studentsClasses=null;
    List<SchoolsClassType> studentsClassTypes=null;
    List<SchoolsClassDto> distributeClasses=null;
    List<Distributes> distributes;
    List<DistributesNames> distributesNames;
    List<Students> orderedStudents;
    List<ClassTypeStudents> classTypeStudents;

    int studentsCount=0;
    int previousDistClassNo=0;
    int previousDistTypeClassNo=0;

    int selectedStudentsCount,selectedRoomsCapacity;

    @Inject SchoolsClassDao schoolsClassDao;
    @Inject Report report;

    @PostConstruct
    public void init() {
        distributeType="y";
    }

    public void calculateSelected() {
        selectedStudentsCount=0;
        selectedRoomsCapacity=0;
        for (SchoolsClassDto sdto:schoolsClassDtos) {
            if (sdto.getbIncludeStudents()) {
                selectedStudentsCount += sdto.getStudentsSize();
            }
            if (sdto.getbUseRoom()) {
                selectedRoomsCapacity += sdto.getCapacity();
            }
        }
        log.info("selectedStudentsCount :" + selectedStudentsCount);
        log.info("selectedRoomsCapacity :" + selectedRoomsCapacity );
    }


    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();


        log.info("SELECTED : " + schoolsClassDao.getItem());
        calculateSelected();

        if(newValue != null && !newValue.equals(oldValue)) {
            //SchoolsClassDto entity =(SchoolsClassDto) ((DataTable)event.getComponent()).getRowData();
            //entity.setRefSchoolClassType(schoolsClassTypeDao.getSelected());
        }
    }


    public void saveClasses() {
        try {
            for (SchoolsClassDto sdto : schoolsClassDtos) {
                schoolsClassDao.saveOrUpdate(sdto.toEntity());
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void distributeChanged() {
        distributes=findDistributes();
    }

    public List<Distributes> findDistributes() {
        List list=null;
        log.info("LOG03130: DISTRIBUTE NAME : " + distributeName.getName());
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refDistributeName", distributeName));
            list=c.list();
        } catch (Exception e) {
            log.error("LOG03090:",e);
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public Long save() {
        try {
            Integer distno = findMaxDistributeNo() + 1;
            DistributesNames distName=new DistributesNames();
            distName.setNo(distno);
            distName.setName(distributeNameInput);
            distName.setCreated(Util.getNow());
            distName.setRefSchool(Util.getActiveSchool());
            distName.setActive(true);
            Long distId=(Long)getSession().save(distName);
            distName=(DistributesNames)getSession().load(DistributesNames.class,distId);
            log.info("LOG03120: DIST NAME : " + distName);
            getSession().flush();
            for (Distributes distribute : distributes) {
                distribute.setRefDistributeName(distName);
                distribute.setCreated(Util.getNow());
                super.saveOrUpdate(distribute);
            }
            distributesNames=null;
            Util.setFacesMessage("KAYIT EDILDI");
        } catch (Exception e) {
            Util.catchException(e);
        }
        return null;
    }

    public Integer findMaxDistributeNo() {
        try {
            Criteria c = getSession().createCriteria(DistributesNames.class);
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refSchool", Util.getActiveSchool()));
            c.setProjection(Projections.max("no"));
            //c.createCriteria("refRole").add(Restrictions.eq("name", Constants.ROLE_ADMIN));
            //c.add(Restrictions.eq("isDeleted", false));
            Integer distNo=(Integer) c.uniqueResult();
            if (distNo==null) distNo=0;
            return distNo;
        } catch (Exception e) {
            log.error("LOG03090:",e);
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<DistributesNames> findDistributesNames() {
        log.info("LOG03100: FINDING SCHOOL DISTRIBUTES");
        List list=null;
        try {
            Criteria c = getSession().createCriteria(DistributesNames.class);
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("refSchool", Util.getActiveSchool()));
            list=c.list();
            log.info("LOG03110: SCHOOL DISTRIBUTES : " + list);
        } catch (Exception e) {
            log.error("LOG03090:",e);
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * find selected classes which have students to distribute
     * @return
     */
    public void findStudentsClasses() {
        List<SchoolsClassDto> templist=new ArrayList<>();
        for (SchoolsClassDto cdto:schoolsClassDtos) {
            if (cdto.getbIncludeStudents()) {
                templist.add(cdto);
            }
        }
        studentsClasses=templist;
        log.info("LOG03040: STUDENT CLASSES : " + templist);
    }

    /**
     * find rooms which will be include in the exams
     * @return
     */
    public void findDistributeClasses() {
        List<SchoolsClassDto> tempList=new ArrayList<>();
        for (SchoolsClassDto cdto:schoolsClassDtos) {
            if (cdto.getbUseRoom()) {
                tempList.add(cdto);
            }
        }
        distributeClasses=tempList;
        log.info("LOG03030: DISTRIBUTE CLASSES : " + tempList);
    }

    /**
     * find students count to be distributed (sum of students in all classes)
     * @return
     */
    public int findStudentsCount() {
        int count=0;
        for (SchoolsClassDto cdto:studentsClasses) {
            count=count+cdto.getStudentsSize();
        }
        return count;
    }

    public SchoolsClassDto findDistributeClass(int count) {
        int capacity=0;
        for (SchoolsClassDto cdto:distributeClasses) {
            if (count>capacity) {
                return cdto;
            }
            capacity=capacity+cdto.getCapacity();

        }
        return null;

    }

    /**
     * Find Selected Student Classes type
     * @return List of Class Types
     */
    private List<SchoolsClassType> findStudentsClassTypes() {
        List<SchoolsClassType> classTypes=new ArrayList<>();
        List<Long> typeids=new ArrayList<>();
        //find type ids
        for (SchoolsClassDto classDto:studentsClasses) {
            if ((classDto.getRefSchoolClassType()!=null)
                    && (!typeids.contains(classDto.getRefSchoolClassType().getTid()))) {
                typeids.add(classDto.getRefSchoolClassType().getTid());
                classTypes.add(classDto.getRefSchoolClassType());
            }
        }
        log.info("TYPE IDS : " + typeids);
        /*
        for (SchoolsClassDto sdto:studentsClasses) {
            if ( (sdto.getRefSchoolClassType()!=null)
                    && (!typeids.contains(sdto.getRefSchoolClassType().getTid())) ) {

            }
        }
        */
        studentsClassTypes=classTypes;
        log.info("CLASS TYPES : " + studentsClassTypes);
        return classTypes;
    }

    /**
     * Order students according to distributeType
     * @return orderedStudents : ordered students
     */
    public List<Students> findOrderedStudents() {
        classTypeStudents=new ArrayList<>();
        List<Students> students=null;
        ClassTypeStudents typeStudents=null;
        if ((distributeType!=null) && (!distributeType.equals("")) ) {
            findStudentsClassTypes();
            for (SchoolsClassType schoolsClassType:studentsClassTypes) {
                students=new ArrayList<>();
                for (SchoolsClassDto scdto:studentsClasses) {
                    if (scdto.getRefSchoolClassType().getTid().equals(schoolsClassType.getTid())) {
                        students.addAll(scdto.getStudentsCollection());
                        orderedStudents.addAll(scdto.getStudentsCollection());
                    }
                }

                //BeanComparator fieldComparator=null;
                if (distributeType.equals("n")) {
                    BeanComparator fieldComparator=new BeanComparator("schoolNo");
                    Collections.sort(students,fieldComparator);
                }
                if (distributeType.equals("a")) {
                    BeanComparator fieldComparator=new BeanComparator("name",Collator.getInstance(new Locale("tr","TR")));
                    Collections.sort(students,fieldComparator);
                }
                if (distributeType.equals("s")) {
                    BeanComparator fieldComparator=new BeanComparator("surname",Collator.getInstance(new Locale("tr","TR")));
                    Collections.sort(students,fieldComparator);
                }
                if (distributeType.equals("k")) {
                    BeanComparator fieldComparator=new BeanComparator("mernis");
                    Collections.sort(students,fieldComparator);
                }

                if (bRandom) {
                    Collections.shuffle((List<?>) students);
                }
                typeStudents=new ClassTypeStudents();
                typeStudents.setClassType(schoolsClassType);
                typeStudents.setStudents(students);
                classTypeStudents.add(typeStudents);

                //Collections.sort(orderedStudents,fieldComparator);
                log.info("CLASS TYPE : " + schoolsClassType);
                int x=1;
                for (Students ostudents:students) {
                    log.info("ORDERED STUDENT : " + x + " : " +ostudents.getSchoolNo() + " "
                            +ostudents.getName() + " " + ostudents.getMernis() + " "
                            +ostudents.getRefSchoolClass().getRefSchoolClassType().getName());
                    x++;
                }
            }
        }
        return orderedStudents;
    }

    /**
     * Find next student for distribute
     * @return Students : next distribute student
     */
    public Students getNextStudent() {
        Students student=null;

        for (int i=0; i<studentsClasses.size(); i++) {
            if (previousDistClassNo==studentsClasses.size()) previousDistClassNo=0;
            SchoolsClassDto cdto= studentsClasses.get(previousDistClassNo);
            //ArrayList<Students> students=new ArrayList<>(cdto.getStudentsCollection());
            //for (Students tempStudent:cdto.getStudentsCollection()) {
            Iterator<Students> studentsIterator=cdto.getStudentsCollection().iterator();
            if (studentsIterator.hasNext()) {
                if (bButterfly) {
                    previousDistClassNo = previousDistClassNo + 1;
                }
                while (studentsIterator.hasNext()) {
                    student = studentsIterator.next();
                    log.info("LOG03070: DISTRIBUTE STUDENT : " + student.getRefSchoolClass().getName() + " "
                            + student.getName() + " " + student.getFullname());
                    studentsIterator.remove();
                    return student;
                }
            } else {
                previousDistClassNo = previousDistClassNo + 1;
                continue;
            }
        }
        return null;
    }

    public Students getNextOrderedStudent() {
        Students student=null;
        ClassTypeStudents typeStudents;
        //int i=previousDistTypeClassNo;  //start from last class type
        for (int i=0; i<studentsClassTypes.size(); i++) { //search all class types
            if (previousDistTypeClassNo==studentsClassTypes.size()) previousDistTypeClassNo=0;
            typeStudents=classTypeStudents.get(previousDistTypeClassNo);

            if (typeStudents.getStudents().size()==0) {
                previousDistTypeClassNo = previousDistTypeClassNo + 1;
                continue;
            } else {
                if (bButterfly) {
                    previousDistTypeClassNo = previousDistTypeClassNo + 1;
                }
                student = typeStudents.getStudents().get(0); //get first student
                if (student != null) {
                    log.info("STUDENT : " + " " + student.getName() + " " + student.getSurname());
                    typeStudents.getStudents().remove(0);
                    return student;
                } else {
                    log.info("STUDENT NULL !!!");
                }
            }
        }
        log.info("STUDENT NULL !!!");
        return null;
    }


    /**
     *
     */
    public void orderStudentClassesByType() {
        List<SchoolsClassDto> tempList=new ArrayList<>();
        int t=0;
        log.info("CLASS TYPES : " + studentsClassTypes);
        for (int i=0; i<studentsClasses.size(); i++) {
            for (SchoolsClassDto sdto:studentsClasses) {
                log.info("t : " + t+" sdto type : " +sdto.getRefSchoolClassType().getTid()
                        + " types type : " + studentsClassTypes.get(t).getTid());
                if (sdto.getRefSchoolClassType().getTid().equals(studentsClassTypes.get(t).getTid())){
                    log.info("SDTO : " + sdto);
                    tempList.add(sdto);
                    studentsClasses.remove(sdto);
                    t++;
                    if (t==studentsClassTypes.size()) t=0;
                    break;
                }
            }
        }
        studentsClasses=tempList;
        log.info("STUDENT CLASSES : " + tempList);
    }



    /**
     * Distributes students to the classes according to selected options
     */
    public void distribute() {
        log.info("LOG03050: DISTRIBUTE START ");

        Distributes distribute=null;
        int distrank=0;
        orderedStudents=new ArrayList<>();
        try {
            if (distributes==null) {
                distributes = new ArrayList<>();
            }
            distributes.clear();
            findStudentsClasses();
            findDistributeClasses();

            for (SchoolsClassDto cdto:studentsClasses) { //refresh students from database
                getSession().refresh(cdto.getEntity());
                cdto.setStudentsCollection(cdto.getEntity().getStudentsCollection());
                if (bRandom) {
                   Collections.shuffle((List<?>) cdto.getStudentsCollection());
               }
            }
            if (bButterfly) {

            }
            findStudentsClassTypes();
            //orderStudentClassesByType();
            studentsCount=findStudentsCount();

            log.info("DISTRIBUTE TYOE : " + distributeType);
            if ((distributeType!=null) && (!distributeType.equals("")) && (!distributeType.equals("y")) ) {

                findOrderedStudents();
            }


            for (SchoolsClassDto cdto:distributeClasses) {
                log.info("LOG03060: DISTRIBUTE  ROOM : " + cdto.getName());
                for (int i=0; i<cdto.getCapacity(); i++) {
                    distribute = new Distributes();
                    distribute.setRefSchool(Util.getActiveSchool());
                    distribute.setActive(true);
                    distribute.setRoomRank(i + 1);
                    Students student = null;
                    if ((distributeType!=null) && (!distributeType.equals("")) && (!distributeType.equals("y")) ) {
                        student=getNextOrderedStudent();
                    } else {
                        student=getNextStudent();
                    }
                    if (student!=null) {
                        distrank++;
                        if (bUseRoomName) {
                            distribute.setRoom(cdto.getRoomName());
                        } else {
                            distribute.setRoom(cdto.getName());
                        }
                        distribute.setDistributeRank(distrank);
                        distribute.setName(student.getName());
                        distribute.setSurname(student.getSurname());
                        distribute.setClassName(student.getRefSchoolClass().getName());
                        distribute.setMernis(student.getMernis());
                        distribute.setSchoolNo(String.valueOf(student.getSchoolNo()) );
                        distributes.add(distribute);
                    }
                }
            }
            log.info("LOG03020: DISTRIBUTES : " + distributes);

        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            SchoolsClassDto rowObject=(SchoolsClassDto) event.getObject();
            DataTable table = (DataTable) event.getSource();
            rowObject = (SchoolsClassDto) table.getRowData();
            log.info("LOG03080: ROW UPDATED : " + rowObject.getName() + " capacity : " + rowObject.getCapacity());
            SchoolsClass sclass=schoolsClassDao.getById(rowObject.getTid());
            schoolsClassDao.update(rowObject.toEntity(sclass));
            //super.update(lesson);
            //Util.setFacesMessage("Edited :" + lesson+", " +lesson.getQuestionCount());
        } catch (Exception e) {
            Util.setFacesMessage(e.getMessage());
        }
    }

    public void onRowCancel(RowEditEvent event) {
        //Util.setFacesMessage("Edit Cancelled : " + ((LessonsDto) event.getObject()).getTid());
    }


    /**
     * report recorded distribute for student signature
     */
    public void reportDistribute() {
        Long distributeNameId=distributeName.getTid();
        try {
            if (distributeNameId == null) {
                Util.setFacesMessageError("Dağıtım Adı Seciniz");
                return;
            }
            //mailFiles=new ArrayList<>();
            log.info("REPORT DISTRIBUTE : " + distributeNameId);
            Map<String, Object> params = new HashMap<>();
            params.put("pSchoolName", Util.getActiveSchool().getName());
            Date now = Calendar.getInstance(new Locale("TR")).getTime();
            params.put("pnow", now);
            if (Util.getActiveSchool().getRefTown() != null) {
                params.put("pilce", Util.getActiveSchool().getRefTown().getName().toUpperCase());
            }
            if (Util.getActiveExam()!=null) {
                params.put("pyil", Util.getActiveExam().getRefExamYear().getName());
                params.put("pdonem", Util.getActiveExam().getRefExamSeason().getNameTr());
            }
            if (Util.getActiveSchool().getRefImage() != null) {
                params.put("plogo", "http://www.ortaksinav.com.tr/images/school/" + Util.getActiveSchool().getTid()
                        + "." + Util.getActiveSchool().getRefImage().getExtension());
            }
            params.put("pDistributeNameId", distributeNameId);
            Locale trlocale = Locale.forLanguageTag("tr-TR");
            params.put(JRParameter.REPORT_LOCALE, trlocale);
            report.pdf("repDistribute", params, "Dagitim");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public DistributeDao() {
        super(Distributes.class);
    }

    public Distributes getSelected() {
        return selected;
    }

    public void setSelected(Distributes selected) {
        this.selected = selected;
    }

    public List<SchoolsClassDto> getSchoolsClassDtos() {
        if (schoolsClassDtos==null) {
            schoolsClassDtos=new ArrayList<>();
            for (SchoolsClass sclass:schoolsClassDao.findByActiveSchool()) {
                SchoolsClassDto sclassDto=new SchoolsClassDto(sclass);
                //sclassDto.setCapacity(40);
                if (sclassDto.getbUseRoom()==null) {
                    sclassDto.setbUseRoom(false);
                }
                if (sclassDto.getbIncludeStudents()==null) {
                    sclassDto.setbIncludeStudents(false);
                }
                schoolsClassDtos.add(sclassDto);
            }
            calculateSelected();
        }
        return schoolsClassDtos;
    }

    public void setSchoolsClassDtos(List<SchoolsClassDto> schoolsClassDtos) {
        this.schoolsClassDtos = schoolsClassDtos;
    }

    public Boolean getbUseRoomName() {
        return bUseRoomName;
    }

    public void setbUseRoomName(Boolean bUseRoomName) {
        this.bUseRoomName = bUseRoomName;
    }

    public Boolean getbRandom() {
        return bRandom;
    }

    public void setbRandom(Boolean bRandom) {
        this.bRandom = bRandom;
    }

    public Boolean getbButterfly() {
        return bButterfly;
    }

    public void setbButterfly(Boolean bButterfly) {
        this.bButterfly = bButterfly;
    }

    public List<Distributes> getDistributes() {
        return distributes;
    }

    public void setDistributes(List<Distributes> distributes) {
        this.distributes = distributes;
    }

    public DistributesNames getDistributeName() {
        return distributeName;
    }

    public void setDistributeName(DistributesNames distributeName) {
        this.distributeName = distributeName;
    }

    public String getDistributeNameInput() {
        return distributeNameInput;
    }

    public void setDistributeNameInput(String distributeNameInput) {
        this.distributeNameInput = distributeNameInput;
    }

    public List<DistributesNames> getDistributesNames() {
        if (distributesNames==null) {
            distributesNames=findDistributesNames();
        }
        return distributesNames;
    }

    public void setDistributesNames(List<DistributesNames> distributesNames) {
        this.distributesNames = distributesNames;
    }

    public int getSelectedStudentsCount() {
        return selectedStudentsCount;
    }

    public void setSelectedStudentsCount(int selectedStudentsCount) {
        this.selectedStudentsCount = selectedStudentsCount;
    }

    public int getSelectedRoomsCapacity() {
        return selectedRoomsCapacity;
    }

    public void setSelectedRoomsCapacity(int selectedRoomsCapacity) {
        this.selectedRoomsCapacity = selectedRoomsCapacity;
    }

    public String getDistributeType() {
        return distributeType;
    }

    public void setDistributeType(String distributeType) {
        this.distributeType = distributeType;
    }

}

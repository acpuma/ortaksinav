package net.yazsoft.ors.distribute;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Distributes;
import net.yazsoft.ors.entities.DistributesNames;
import net.yazsoft.ors.entities.SchoolsClass;
import net.yazsoft.ors.entities.Students;
import net.yazsoft.ors.schools.SchoolsClassDao;
import net.yazsoft.ors.schools.SchoolsClassDto;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import sun.rmi.log.LogInputStream;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
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

    List<SchoolsClassDto> studentsClasses=null;
    List<SchoolsClassDto> distributeClasses=null;
    List<Distributes> distributes;
    List<DistributesNames> distributesNames;
    int studentsCount=0;
    int previousDistClass=0;

    @Inject SchoolsClassDao schoolsClassDao;


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
     * Find next student for distribute
     * @return Students : next distribute student
     */
    public Students getNextStudent() {
        Students student=null;
        if (previousDistClass==studentsClasses.size()) previousDistClass=0;
        for (int i=previousDistClass; i<studentsClasses.size(); i++) {
            SchoolsClassDto cdto= studentsClasses.get(i);
            //ArrayList<Students> students=new ArrayList<>(cdto.getStudentsCollection());
            //for (Students tempStudent:cdto.getStudentsCollection()) {
            Iterator<Students> studentsIterator=cdto.getStudentsCollection().iterator();
            previousDistClass=i+1;
            while (studentsIterator.hasNext()) {
                student=studentsIterator.next();
                log.info("LOG03070: DISTRIBUTE STUDENT : " + student.getRefSchoolClass().getName() + " "
                        + student.getName() + " " + student.getFullname());
                studentsIterator.remove();
                return student;
            }
        }
        return null;
    }

    /**
     * Distributes students to the classes according to selected options
     */
    public void distribute() {
        log.info("LOG03050: DISTRIBUTE START ");
        Distributes distribute=null;
        int distrank=0;
        try {
            if (distributes==null) {
                distributes = new ArrayList<>();
            }
            distributes.clear();
            findStudentsClasses();
            findDistributeClasses();
            if (bRandom) {
               for (SchoolsClassDto cdto:studentsClasses) {
                   Collections.shuffle((List<?>) cdto.getStudentsCollection());
               }
            }
            studentsCount=findStudentsCount();
            for (SchoolsClassDto cdto:distributeClasses) {
                log.info("LOG03060: DISTRIBUTE  ROOM : " + cdto.getName());
                for (int i=0; i<cdto.getCapacity(); i++) {
                    distribute = new Distributes();
                    distribute.setRefSchool(Util.getActiveSchool());
                    distribute.setActive(true);
                    distribute.setRoomRank(i + 1);
                    Students student = getNextStudent();
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
}

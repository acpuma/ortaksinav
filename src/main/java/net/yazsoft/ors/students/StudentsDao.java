package net.yazsoft.ors.students;

import com.google.common.io.Files;
import net.sf.jasperreports.engine.JRParameter;
import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.report.Report;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Excel;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.SchoolsClass;
import net.yazsoft.ors.entities.Students;
import net.yazsoft.ors.schools.SchoolsClassDao;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.FileUploadEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.zeroturnaround.zip.ZipUtil;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


@Named
@ViewScoped
public class StudentsDao extends BaseGridDao<Students> implements Serializable{
    private static final Logger logger = Logger.getLogger(StudentsDao.class);
    private static final int BUFFER_SIZE = 6124;
    private static String FILENAME="ogrenciler.xls";

    Students selected;
    List<Students> students;
    List<SchoolsClass> schoolsClasses;
    List<Students> gridStudents;
    List<StudentsDto> studentsDtos;
    List<Students> foundStudents;
    SchoolsClass selectedClass;

    @Inject SchoolsClassDao schoolsClassDao;
    @Inject Excel excel;
    @Inject private BCryptPasswordEncoder encoder;
    @Inject Report report;


    public void downloadAllPhotos() throws IOException {
        try {

            File tempFolder =Files.createTempDir();
            //new File(Util.getUploadsFolder()+"/temp");
            //Util.createDirectory(tempFolder);
            logger.info("TEMP FOLDER : " + tempFolder);

            String imagesFolder = Util.getImagesFolder()+"/student";
            int i=0;
            int count=0;
            String notcopied="";
            for (Students student:Util.getActiveSchool().getStudentsCollection()) {
                count++;
                try {
                    if (student.getRefImage()!=null) {
                        File source = new File(imagesFolder + "/" + Util.getActiveSchool().getTid() + "/"
                                + student.getTid() + "." + student.getRefImage().getExtension());
                        File dest = new File(tempFolder + "/" + student.getRefSchoolClass().getName() + "/"
                                + student.getSchoolNo() + "." + student.getRefImage().getExtension());
                        logger.debug("source : " + source);
                        logger.debug("dest : " + dest);
                        //Util.createDirectory(tempFolder+"/"+student.getRefSchoolClass().getName());
                        Files.createParentDirs(dest);

                        Files.copy(source, dest);
                        i++;
                    } else {
                        notcopied=notcopied.concat(student.getRefSchoolClass().getName() +"|"+ student.getSchoolNo()+" ");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //if (i>5) break;
            }
            File bilgi=new File(tempFolder+"/bilgi.txt");
            FileWriter fw=new FileWriter(bilgi);
            fw.write("Ogrenci sayisi : " +count + " kopyalanan : " + i + "\n");
            fw.write("Kopyalanmayanlar : " + notcopied);
            fw.close();
            logger.info("Students count : " +count + " copied : " + i);
            logger.info("Not copied : " + notcopied);

            ZipUtil.pack(tempFolder,new File(Util.getUploadsFolder()+"/"+Util.getActiveSchool().getTid()+"Foto.zip"));


            File file = new File(Util.getUploadsFolder() + "/"
                    + Util.getActiveSchool().getTid().toString() + "Foto.zip");
            HttpServletResponse httpServletResponse = (HttpServletResponse)
                    FacesContext.getCurrentInstance().getExternalContext().getResponse();
            httpServletResponse.addHeader("Content-disposition", "attachment; filename="
                    + Util.getActiveSchool().getTid().toString() + "Foto.zip");

            FacesContext.getCurrentInstance().responseComplete();

            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();

            BufferedInputStream input = null;
            //BufferedOutputStream output = null;
            input = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);

            // Write file contents to response.
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                servletOutputStream.write(buffer, 0, length);
            }

            servletOutputStream.flush();
            servletOutputStream.close();
            input.close();
            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            logger.error("EXCEPTION: ", e);
            Util.setFacesMessageError(e.getMessage());
            throw e;
        }
    }
    public void resetCredits() {
        String credit;
        try {
            for (Students student : findBySchool(Util.getActiveSchool())) {
                if (Util.getActiveSchool().getUseMernis()) {
                    credit = student.getMernis();
                } else {
                    credit = Util.getActiveSchool().getMebCode().concat(student.getSchoolNo().toString());
                }
                //logger.info("LOG02720: CREDIT : " + credit);
                student.setUsername(credit);
                if (credit != null) {
                    student.setPassword(encoder.encode(credit));
                }
                update(student);
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void handleClassChange() {
        logger.info("selected class : " + selectedClass);
    }
    public void reportPhotos() {
        try {

            logger.info("LOG01610: selelectedClass : " + selectedClass);
            Map<String, Object> params = new HashMap<>();
            params.put("pSchoolName", Util.getActiveSchool().getName());
            params.put("pschool", Util.getActiveSchool().getTid());
            if (selectedClass == null) {
                //Util.setFacesMessageError("Sinif Seciniz");
                //return;
                params.put("pclass", null);
            } else {
                params.put("pclass", selectedClass);
            }
            params.put("pdate", Util.getActiveExam().getDate());
            Date now = Calendar.getInstance(new Locale("TR")).getTime();
            params.put("pnow", now);
            if (Util.getActiveSchool().getRefCity() != null) {
                logger.info("CITY : " + Util.getActiveSchool().getRefCity().getName());
                params.put("pil", Util.getActiveSchool().getRefCity().getName().toUpperCase());
            }
            if (Util.getActiveSchool().getRefTown() != null) {
                params.put("pilce", Util.getActiveSchool().getRefTown().getName().toUpperCase());
            }
            params.put("pyil", Util.getActiveExam().getRefExamYear().getName());
            params.put("pdonem", Util.getActiveExam().getRefExamSeason().getNameTr());
            if (Util.getActiveSchool().getRefImage() != null) {
                params.put("plogo", "http://www.ortaksinav.com.tr/images/school/" + Util.getActiveSchool().getTid()
                        + "." + Util.getActiveSchool().getRefImage().getExtension());
            }
            Locale trlocale = Locale.forLanguageTag("tr-TR");
            params.put(JRParameter.REPORT_LOCALE, trlocale);
            report.pdf("repPhotosStudent", params, Util.getActiveSchool().getName());
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    @Override
    public void delete() {
        super.delete();
        gridStudents=null;
    }

    @Override
    public Long create() {
        logger.info("CREATING STUDENT");
        getItem().setActive(Boolean.TRUE);
        getItem().setRefSchool(Util.getActiveSchool());
        //getItem().setAccountNonExpired(Boolean.TRUE);
        //getItem().setAccountNonLocked(Boolean.TRUE);
        //getItem().setCredentialsNonExpired(Boolean.TRUE);
        if ((getItem().getPassword()==null) || getItem().getPassword().equals("")) {
            Util.setFacesMessage("SIFRE alanini doldurunuz");
            return null;
        }
        getItem().setPassword(encoder.encode(getItem().getPassword()));

        //Collection<Roles> roles=new HashSet<>();
        //roles.add(rolesDao.getById(3L));
        Long pk=super.create();

        //rolesDao.getById(3L).getUsersCollection().add(getItem());
        //rolesDao.update();
        //getSession().save(roles);
        gridStudents=null;
        return pk;
    }


    public String update() {
        try {
            logger.info("LOG02680: STUDENT UPDATE : " + getItem().getTid() + " : " + getItem().getUsername()
                    + ":" + getItem().getName() + ":" + getItem().getSurname() + ":" +getItem().getRefSchoolClass() );
            if ( (getItem().getPassword() == null) || (getItem().getPassword().length()==0) ) {
                String hql = "update Students u set username=:username,name=:name,surname=:surname," +
                        "phone=:phone,fullname=:fullname,schoolNo=:schoolno,refSchoolClass=:sclass," +
                        "mernis=:mernis,gender=:gender where tid = :tid";
                Query query = getSession().createQuery(hql);
                query.setLong("tid", getItem().getTid());
                query.setString("username", getItem().getUsername());
                query.setLong("sclass", getItem().getRefSchoolClass().getTid());
                query.setInteger("schoolno", getItem().getSchoolNo());
                query.setString("fullname", getItem().getFullname());
                query.setString("mernis", getItem().getMernis());
                query.setString("name", getItem().getName());
                query.setString("surname", getItem().getSurname());
                query.setString("phone", getItem().getPhone());
                query.setString("gender", getItem().getGender());

                //query.setLong("role", getItem().getRefRole().getTid());
                query.executeUpdate();
                Util.setFacesMessage("KAYIT GÜNCELLENDİ");
            } else {
                getItem().setPassword(encoder.encode(getItem().getPassword()));
                super.update();
            }
            logger.info("LOG02680: STUDENT UPDATE : " + getItem().getTid() + " : " + getItem().getUsername()
                    + ":" + getItem().getName() + ":" + getItem().getSurname() + ":" +getItem().getRefSchoolClass() );
        } catch (Exception e) {
            Util.catchException(e);
        }
        gridStudents=null;
        return null;
    }

    public String updateFields(Students student) {
        try {
            logger.info("LOG02680: STUDENT UPDATE : "
                    + ":" + getItem().getName() + ":" + getItem().getSurname() );

                String hql = "update Students u set username=:username,name=:name,surname=:surname," +
                        "phone=:phone,fullname=:fullname,schoolNo=:schoolno,refSchoolClass=:sclass," +
                        "mernis=:mernis,gender=:gender where tid = :tid";
                Query query = getSession().createQuery(hql);
                query.setLong("tid", getItem().getTid());
                query.setString("username", getItem().getUsername());
                query.setLong("sclass", getItem().getRefSchoolClass().getTid());
                query.setInteger("schoolno", getItem().getSchoolNo());
                query.setString("fullname", getItem().getFullname());
                query.setString("mernis", getItem().getMernis());
                query.setString("name", getItem().getName());
                query.setString("surname", getItem().getSurname());
                query.setString("phone", getItem().getPhone());
                query.setString("gender", getItem().getGender());

                //query.setLong("role", getItem().getRefRole().getTid());
                query.executeUpdate();
                Util.setFacesMessage("KAYIT GÜNCELLENDİ");

            logger.info("LOG02680: STUDENT UPDATE : " + getItem().getUsername() + ":" + getItem().getPassword()+ ":"
                    + ":" + getItem().getName() + ":" + getItem().getSurname() );
        } catch (Exception e) {
            Util.catchException(e);
        }
        gridStudents=null;
        return null;
    }

    public Students findByNoAndSchool(Integer schoolNo,Schools school) {
        Students temp=null;
        if (school==null) {
            school = Util.getActiveSchool();
        }
        try {
            Criteria c = getCriteria();
            //c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("schoolNo", schoolNo));
            c.add(Restrictions.eq("refSchool", school));
            temp = (Students)c.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * find student from active school
     * @param schoolNo
     * @return
     */

    public Students findByNo(Integer schoolNo) {
        if (foundStudents==null) {
            foundStudents=findBySchool(Util.getActiveSchool());
        }
        for (Students student:foundStudents) {
            if (student.getSchoolNo().equals(schoolNo)) {
                return student;
            }
        }
        return null;
    }

    /**
     * find student by mernis from active school
     * @param mernis
     * @return
     */

    public Students findByMernis(String mernis) {
        if (foundStudents==null) {
            foundStudents=findBySchool(Util.getActiveSchool());
        }
        for (Students student:foundStudents) {
            if (student.getMernis().equals(mernis)) {
                return student;
            }
        }
        return null;
    }

    public void saveClasses() {
        try {
            //save classes
            schoolsClassDao.findBySchool(Util.getActiveSchool()); //find old classes
            for (StudentsDto studentDto : studentsDtos) {
                //if class does not exist
                if (studentDto.refSchoolClass.getTid() == null) {
                    SchoolsClass sclass = schoolsClassDao.findByNameFromList(studentDto.refSchoolClass.getName());
                    //if class not created yet, §create it
                    if (sclass == null) {
                        sclass = new SchoolsClass();
                        sclass.setName(studentDto.refSchoolClass.getName());
                        sclass.setRefSchool(Util.getActiveSchool());
                        sclass.setActive(Boolean.TRUE);
                        sclass.setVersion(1);
                        if ((sclass.getName()==null) || (sclass.getName().equals("")) ){
                            throw new Exception("SINIF ADI BOS");
                        }
                        schoolsClassDao.saveOrUpdate(sclass);
                        //sclass=(SchoolsClass)getSession().load(SchoolsClass.class,pkclas);
                        schoolsClassDao.getFoundClasses().add(sclass);
                        studentDto.setRefSchoolClass(sclass);
                        logger.info("LOG02750: SCLASS : " + sclass.getTid());
                    }
                }
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void saveDtos() {
        saveClasses();
        saveStudentDtos();
    }

    public void saveStudentDtos() {
        Students tempStudent=null;
        try {
            saveClasses();

            //save students
            for (StudentsDto studentDto:studentsDtos) {
                //logger.info("LOG02730: STUDENT : " + studentDto.getName()+ studentDto.getSurname());
                //get schoolclass from saved one
                studentDto.setRefSchoolClass(schoolsClassDao.findByNameFromList(studentDto.getRefSchoolClass().getName()));
                studentDto=prepareCredits(studentDto);
                if (studentDto.getTid()==null) {
                    studentDto.setVersion(1);
                    getSession().save(studentDto.toEntity());
                } else {
                    tempStudent=(Students)getSession().load(Students.class, studentDto.getTid());
                    saveOrUpdate(studentDto.toEntity(tempStudent));
                }
                //logger.info("LOG02740: SAVEDDDDDD STUDENT : " + studentDto.getName()+ studentDto.getSurname());
            }
            logger.info("LOG02740: SAVEDDDDDD");
            Util.setFacesMessage("KAYIT EDILDI");
            gridStudents=null;
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void readStudents() {
        List<StudentsDto> persons=null;
        int personCount;
        try {
            excel.readExcel(Util.getUploadsFolder() + "/" + FILENAME);
            Workbook workbook = excel.getWorkbook();

            //Get the number of sheets in the xlsx file
            int numberOfSheets = workbook.getNumberOfSheets();
            personCount=0;

            StudentsDto person;
            //loop through each of the sheets
            for(int i=0; i < numberOfSheets; i++){

                //Get the nth sheet from the workbook
                Sheet sheet =workbook.getSheetAt(i);

                //every sheet has rows, iterate over them
                Iterator<Row> rowIterator = sheet.iterator();
                persons = new ArrayList<>();
                long j=0;
                SchoolsClass tempClass=null;
                rowIterator.next();
                while (rowIterator.hasNext())
                {
                    j++;
                    //Get the row object
                    Row row = rowIterator.next();
                    person=new StudentsDto();
                    person.setId(j);
                    person.setRefSchool(Util.getActiveSchool());
                    person.setActive(true);
                    //Every row has columns, get the column iterator and iterate over them
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int index=0;
                    String value=null;
                    tempClass=null;
                    while (index<8) {
                        Cell cell = row.getCell(index);
                        index++;
                        if (cell==null) {
                            logger.info("LOG02650: NULL CELL : " + index  );
                            continue;
                        }
                        value=null;

                        try {
                            switch(cell.getCellType()){
                                case Cell.CELL_TYPE_STRING:
                                    value=cell.getStringCellValue().trim();
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    value=String.valueOf((long)cell.getNumericCellValue());
                                    break;
                            }
                            //logger.info("LOG02650: CELL TYPE : " + index + " : " + cell.getCellType() + " : " + value);

                            if (value!=null ) {
                                switch (index) {
                                    case 1:
                                        tempClass=schoolsClassDao.findByNameFromList(value);
                                        //findBySchoolAndName(Util.getActiveSchool(),value);
                                        if (tempClass==null) {
                                            tempClass=new SchoolsClass();
                                            tempClass.setName(value);
                                        }
                                        person.setRefSchoolClass(tempClass);
                                        break;
                                    case 2:person.setSchoolNo(Double.valueOf(value).intValue());break;
                                    case 3:person.setName(value);break;
                                    case 4:person.setSurname(value);break;
                                    case 5:person.setMernis(value);break;
                                    case 6:person.setFullname(value);break;
                                    case 7:person.setGender(value);break;
                                    case 8:person.setPhone(value);break;
                                    default:
                                        logger.info("LOG01270: INDEX NONE : " + index);
                                        break;
                                }
                            }
                            logger.info("LOG02620: INDEX/VALUE : " + index + " / " + value );
                        } catch (Exception e) {
                            Util.catchException(e);
                        }

                        //logger.info("LOG01240:VALUE : " + value);
                    } //end of cell iterator
                    //person=prepareCredits(person); //cpu %100

                    Students oldstudent=null;
                    if (Util.getActiveSchool().getUseMernis().equals(true) ) {
                        if (person.getMernis()!=null) {
                            oldstudent = findByMernis(person.getMernis());
                        }
                    } else {
                        if (person.getSchoolNo() != null) {
                            oldstudent = findByNo(person.getSchoolNo());
                        }
                    }
                    if (oldstudent != null) {
                        person.setTid(oldstudent.getTid());
                    }
                    if ( (person.getSchoolNo()==null) && (person.getMernis()==null) ){
                        logger.info("LOG02630: PERSON NO NUMBER : " + person);
                    } else {
                        persons.add(person);
                        personCount++;
                    }
                }
            } //end of rows iterator
            studentsDtos=persons;
            //logger.info("LOG02610: STUDENTS : " + persons );
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    /**
     * Sets the student credits according to school useMernis field
     * @param person
     * @return same person with new credits
     */
    public StudentsDto prepareCredits(StudentsDto person) {
        String credit;
        if (Util.getActiveSchool().getUseMernis()) {
            credit = person.getMernis();
        } else {
            credit = Util.getActiveSchool().getMebCode().concat(person.getSchoolNo().toString());
        }
        //logger.info("LOG02720: CREDIT : " + credit);
        person.setUsername(credit);
        if (credit!=null) {
            person.setPassword(encoder.encode(credit));
        }
        return person;
    }

    public void downloadExcel(){
        excel.downloadExcel(FILENAME);
    }
    public void downloadExcelTemplate(){
        excel.downloadExcel("ogrencilerbos.xls");
    }

    @Transactional
    public void handleFileUpload(FileUploadEvent event) {
        excel.handleFileUpload(event,FILENAME,null);
    }

    public List<Students> findBySchool(Schools school) {
        logger.info("SCHOOL : " + school);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refSchool", school));
            //c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public Students findBySchoolAndNumber(Schools school,String schoolNo) {
        logger.info("SCHOOL : " + school);
        Students temp=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refSchool", school));
            c.add(Restrictions.eq("schoolNo", schoolNo));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            temp = (Students)c.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return temp;
    }

    public List<Students> findBySchoolClass(SchoolsClass sclass) {
        logger.info("SCHOOLCLASS : " + sclass);
        List list=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refSchoolClass", sclass));
            c.add(Restrictions.eq("active", true));
            //c.add(Restrictions.eq("isDeleted", false));
            list = c.list();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    public Students findBySchoolClassAndNo(Schools school,SchoolsClass sclass,Integer schoolNo) {
        logger.info("SCHOOLCLASS : " + sclass);
        Students tempstudent=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("refSchoolClass", sclass));
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("schoolNo",schoolNo));
            //c.add(Restrictions.eq("isDeleted", false));
            tempstudent = (Students) c.uniqueResult();
        } catch (Exception e) {
            logger.error(e.getMessage());
            Util.setFacesMessage(e.getMessage());
            e.printStackTrace();
        }
        return tempstudent;
    }

    public Students findUserByMernis(String mernis) {
        Students tempstudent=null;
        try {
            Criteria c = getCriteria();
            c.add(Restrictions.eq("active", true));
            c.add(Restrictions.eq("mernis", mernis));
            tempstudent = (Students) c.uniqueResult();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return tempstudent;
    }

    public void checkboxChange(Students student) {
        try {
            update(student);
            //Util.setFacesMessage("CHANGED");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }


    /**
     * Updates student info according to selected fields
     * @param student
     * @return
     */
    public String updateStudentFields(Students student,boolean updateNameSurname,boolean updateFullname,
                                      boolean updateGender,boolean updatePhone,boolean updateMernis) {
        try {
            // logger.info("LOG02680: STUDENT UPDATE : " + student.getTid()
            //             + ":" + student.getName() + ":" + student.getSurname() );

            String hql = "update Students u set ";
            if (updateNameSurname) hql=hql+"name=:name,surname=:surname,";
            if (updatePhone) hql=hql+ "phone=:phone,";
            if (updateFullname) hql=hql+ "fullname=:fullname,";
            if (updateMernis) hql=hql+ "mernis=:mernis," ;
            if (updateGender) hql=hql+ "gender=:gender,";

            hql=hql + "active=:active where tid = :tid ";

            Query query = getSession().createQuery(hql);
            query.setLong("tid", student.getTid());
            query.setBoolean("active",true);
            if (updateFullname) query.setString("fullname", student.getFullname());
            if (updateMernis) query.setString("mernis", student.getMernis());
            if (updateNameSurname) {
                query.setString("name", student.getName());
                query.setString("surname", student.getSurname());
            }
            if (updatePhone) query.setString("phone", student.getPhone());
            if (updateGender) query.setString("gender", student.getGender());

            //query.setLong("role", getItem().getRefRole().getTid());
            query.executeUpdate();
            //Util.setFacesMessage("KAYIT GÜNCELLENDİ");

        } catch (Exception e) {
            Util.catchException(e);
        }
        return null;
    }

    public StudentsDao() {
        super(Students.class);
    }

    public Students getSelected() {
        return selected;
    }

    public void setSelected(Students selected) {
        this.selected = selected;
    }

    public List<Students> getStudents() {
        return students;
    }

    public void setStudents(List<Students> students) {
        this.students = students;
    }

    public List<SchoolsClass> getSchoolsClasses() {
        if (schoolsClasses==null) {
            schoolsClasses=schoolsClassDao.findBySchool(Util.getActiveSchool());
        }
        return schoolsClasses;
    }

    public void setSchoolsClasses(List<SchoolsClass> schoolsClasses) {
        this.schoolsClasses = schoolsClasses;
    }

    public List<Students> getGridStudents() {
        if (gridStudents==null) {
            gridStudents=findBySchool(Util.getActiveSchool());
        }
        return gridStudents;
    }

    public void setGridStudents(List<Students> gridStudents) {
        this.gridStudents = gridStudents;
    }

    public Students findByUserName(String username) {
        List<Students> users;
        users = getSession()
                .createQuery("from Students where username=?")
                .setParameter(0, username)
                .list();

        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }

    public List<StudentsDto> getStudentsDtos() {
        return studentsDtos;
    }

    public void setStudentsDtos(List<StudentsDto> studentsDtos) {
        this.studentsDtos = studentsDtos;
    }


    public SchoolsClass getSelectedClass() {
        return selectedClass;
    }

    public void setSelectedClass(SchoolsClass selectedClass) {
        this.selectedClass = selectedClass;
    }
}

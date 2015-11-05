package net.yazsoft.frame.security;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Constants;
import net.yazsoft.frame.utils.Excel;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.Roles;
import net.yazsoft.ors.entities.Schools;
import net.yazsoft.ors.entities.Users;
import net.yazsoft.ors.users.UsersDto;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Query;
import org.primefaces.event.FileUploadEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

@Named
@ViewScoped
@Transactional
public class UsersDao extends BaseGridDao<Users> {
    Logger logger= Logger.getLogger(UsersDao.class);
    private static String FILENAME="kullanicilar.xls";

    Users selected;

    List<UsersDto> usersDtos;

    @Inject RolesDao rolesDao;
    @Inject Excel excel;
    @Inject private BCryptPasswordEncoder encoder;

    public UsersDao() {
        super(Users.class);
    }

    @Override
    @PostConstruct
    public void init() {
        getItem().setSchoolsCollection(new HashSet<Schools>());
    }

    @Override
    public Long create() {
        logger.info("CREATING USER");
        getItem().setActive(Boolean.TRUE);
        getItem().setAccountNonExpired(Boolean.TRUE);
        getItem().setAccountNonLocked(Boolean.TRUE);
        getItem().setCredentialsNonExpired(Boolean.TRUE);
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
        return pk;
    }


    public void readUsers() {
        List<UsersDto> persons=null;
        List<Schools> schools=null;
        int personCount;
        try {
            //prepare default role for user
            Roles teacher=rolesDao.findByName(Constants.ROLE_TEACHER);

            excel.readExcel(Util.getUploadsFolder() + "/" + FILENAME);
            Workbook workbook = excel.getWorkbook();

            //Get the number of sheets in the xlsx file
            int numberOfSheets = workbook.getNumberOfSheets();
            personCount=0;

            UsersDto person;
            //loop through each of the sheets
            for(int i=0; i < numberOfSheets; i++){

                //Get the nth sheet from the workbook
                Sheet sheet =workbook.getSheetAt(i);

                //every sheet has rows, iterate over them
                Iterator<Row> rowIterator = sheet.iterator();
                persons = new ArrayList<>();
                long j=0;
                rowIterator.next(); //skip first row
                while (rowIterator.hasNext())
                {
                    j++;
                    //Get the row object
                    Row row = rowIterator.next();
                    person=new UsersDto();
                    person.setId(j);
                    schools=new ArrayList<>();
                    schools.add(Util.getActiveSchool());
                    person.setSchoolsCollection(schools);
                    person.setActive(true);
                    person.setAccountNonExpired(true);
                    person.setAccountNonLocked(true);
                    person.setCredentialsNonExpired(true);
                    //Every row has columns, get the column iterator and iterate over them
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int index=0;
                    String value=null;
                    while (index<5) {
                        Cell cell = row.getCell(index);
                        index++;
                        if (cell==null) {
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

                            if (value!=null) {
                                switch (index) {
                                    case 1:person.setName(value);break;
                                    case 2:person.setSurname(value);break;
                                    case 3:person.setUsername(value);break;
                                    case 4:person.setPhone(value);break;
                                    case 5:person.setEmail(value);break;
                                    default:
                                        logger.info("LOG01270: INDEX NONE : " + index);
                                        break;
                                }
                            }
                            //logger.info("LOG02620: INDEX/VALUE : " + index + " / " + value );
                        } catch (Exception e) {
                            Util.catchException(e);
                        }

                        //logger.info("LOG01240:VALUE : " + value);
                    } //end of cell iterator
                    if (person.getUsername()!=null) {
                        Users olduser=findByUserName(person.getUsername());
                        if (olduser!=null) {
                            person.setTid(olduser.getTid());
                        } else {
                            //if user does not exist in db, only set password for new user
                            if ((person.getName()!=null) && (person.getSurname()!=null)) {
                                String uname=person.getName().substring(0, 1).concat(person.getSurname());
                                person.setPassword(uname);
                                person.setPassword(encoder.encode(uname));
                                person.setRefRole(teacher);
                            }
                        }
                    } else {
                        //if username is not defined, create username and password
                        if ((person.getName()!=null) && (person.getSurname()!=null)) {
                            String uname=person.getName().substring(0, 1).concat(person.getSurname());
                            person.setUsername(uname);
                            person.setPassword(uname);
                            person.setPassword(encoder.encode(uname));
                            person.setRefRole(teacher);
                        }
                    }
                    logger.info("LOG02630: PERSON : " + person);
                    persons.add(person);
                    personCount++;
                }
            } //end of rows iterator
            usersDtos=persons;
            logger.info("LOG02610: USERS : " + persons );
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    @Transactional
    public void saveDtos() {
        Users tempPerson=null;
        try {
            for (UsersDto userDto:usersDtos) {
                //if class does not exist
                if (userDto.getTid()==null) {
                    getSession().save(userDto.toEntity());
                } else {
                    tempPerson=(Users)getSession().load(Users.class, userDto.getTid());
                    saveOrUpdate(userDto.toEntity(tempPerson));
                }
            }
            Util.setFacesMessage("KAYIT EDILDI");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    //Update aktif/pasif user
    public void checkboxChange(Users user) {
        try {
            update(user);
            //Util.setFacesMessage("CHANGED");
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public String update() {
        try {
            logger.info("LOG02680: USER UPDATE : " + getItem().getUsername() + ":" + getItem().getPassword()+ ":"
                    + ":" + getItem().getName() + ":" + getItem().getSurname() );
            if ( (getItem().getPassword() == null) || (getItem().getPassword().length()==0) ) {
                String hql = "update Users u set username=:username,name=:name,surname=:surname," +
                        "phone=:phone,email=:email,refRole=:role where tid = :tid";
                Query query = getSession().createQuery(hql);
                query.setLong("tid", getItem().getTid());
                query.setString("username", getItem().getUsername());
                query.setString("name", getItem().getName());
                query.setString("surname", getItem().getSurname());
                query.setString("phone", getItem().getPhone());
                query.setString("email", getItem().getEmail());
                query.setLong("role", getItem().getRefRole().getTid());
                query.executeUpdate();
                Util.setFacesMessage("KAYIT GÜNCELLENDİ");
            } else {
                getItem().setPassword(encoder.encode(getItem().getPassword()));
                super.update();
            }
            logger.info("LOG02680: USER UPDATE : " + getItem().getUsername() + ":" + getItem().getPassword()+ ":"
                    + ":" + getItem().getName() + ":" + getItem().getSurname() );
        } catch (Exception e) {
            Util.catchException(e);
        }
        return null;
    }

    public void checkUserExists() {
        logger.info("username : "+getItem().getUsername());
        Users user=findByUserName(getItem().getUsername());
        if (user!=null) {
            Util.setFacesMessage("KULLANICI ADI KULLANILIYOR");
        }
    }

    @SuppressWarnings("unchecked")
    public Users findByUserName(String username) {

        List<Users> users;

        users = getSession()
                .createQuery("from Users where username=?")
                .setParameter(0, username)
                .list();

        if (users.size() > 0) {
            return users.get(0);
        } else {
            return null;
        }
    }


    public void downloadExcelTemplate(){
        excel.downloadExcel("kullanicilarbos.xls");
    }


    @Transactional
    public void handleFileUpload(FileUploadEvent event) {
        excel.handleFileUpload(event,FILENAME,null);
    }

    public void downloadExcel(){
        excel.downloadExcel(FILENAME);
    }

    public Users getSelected() {
        return selected;
    }

    public void setSelected(Users selected) {
        this.selected = selected;
    }

    public List<UsersDto> getUsersDtos() {
        return usersDtos;
    }

    public void setUsersDtos(List<UsersDto> usersDtos) {
        this.usersDtos = usersDtos;
    }
}
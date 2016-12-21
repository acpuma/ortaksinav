package net.yazsoft.ors.optic;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.distribute.DistributeDao;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.schedule.ScheduleDao;
import net.yazsoft.ors.settings.SettingsDao;
import net.yazsoft.ors.students.StudentsDao;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.component.barcode.Barcode;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class OpticDao extends BaseGridDao<Optics> implements Serializable{
    private static final Logger log = Logger.getLogger(OpticDao.class);
    private Optics selected;
    private boolean itemsChanged;
    private String svg;
    private String svgprint;
    private String svgpreview;
    private String fmt;
    private List<String> lines;
    private List<OpticsPartsDto> partsDtos;
    private float ratio;

    private List<OpticsFieldsType> fieldsTypes;
    private List<OpticsFields> fields;
    private List<String> fonts;
    private List<Optics> optics;

    @Inject OpticFieldsTypeDao opticFieldsTypeDao;
    @Inject OpticFieldsDao opticFieldsDao;
    @Inject DistributeDao distributeDao;
    @Inject StudentsDao studentsDao;
    @Inject OpticsPartsDao opticsPartsDao;
    @Inject SettingsDao settingsDao;
    @Inject ScheduleDao scheduleDao;

    public void svgAppendImage(OpticsFields field,StringBuffer sb,String filepath,float x, float y) {
        File file = new File(Util.getImagesFolder() + filepath);
        if (file.exists()) {
            sb.append("<image xlink:href='http://www.ortaksinav.com.tr/images" + filepath); //TODO: for remote
            //sb.append("<image xlink:href='/"+Util.getContextPath().toLowerCase() +"/images" + filepath); //TODO: for local
            String width = "50";
            String height = "50";
            if (field.getValue1() != null) width = field.getValue1();
            if (field.getValue2() != null) height = field.getValue2();

            sb.append("' x='" + x + "' y='" + y + "' width='" + width
                    + "px' height='" + height + "px' />");
        }
    }

    public void distributeChanged() {
        distributeDao.setDistributeName(getItem().getRefDistributeName());
        distributeDao.distributeChanged();
    }

    public boolean showValue1(OpticsFields field) {
        if ((field.getRefFieldType()==null) || (field.getRefFieldType().getNameDist()==null) ) {
            return false;
        }
        switch (field.getRefFieldType().getNameDist()) {
            case "text" : return true;
            case "barcode" : return true;
            case "photo" : return true;
            case "lesson" : return true;
            case "logo" : return true;
            default:return false;
        }
    }

    public boolean showValue2(OpticsFields field) {
        if ((field.getRefFieldType()==null) || (field.getRefFieldType().getNameDist()==null) ) {
            return false;
        }
        switch (field.getRefFieldType().getNameDist()) {
            case "photo" : return true;
            case "barcode" : return true;
            case "logo" : return true;
            default:return false;
        }
    }

    public List<Optics> findOptics() {
        List<Optics> list=null;
        try {
            //log.info("ACTIVE USER : " + Util.getActiveUser());
            //log.info("USER SCHOOLS : " + Util.getActiveUser().getSchoolsCollection());
            if (!Util.getActiveUser().getSchoolsCollection().isEmpty()) {
                Criteria c = getCriteria();
                c.add(Restrictions.eq("active", true));
                c.add(Restrictions.in("refSchool", Util.getActiveUser().getSchoolsCollection()));
                list = c.list();
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
        return list;
    }
    @Override
    @Transactional
    public void delete(Optics optic) {
        for (OpticsFields ofield:optic.getOpticsFieldsCollection()) {
            opticFieldsDao.delete(ofield);
        }
        for (OpticsParts opart:optic.getOpticsPartsCollection()) {
            opticsPartsDao.delete(opart);
        }
        super.delete(optic);
    }

    public  Optics findByName(String name) {
        Optics optic=null;
        try {
            Criteria c=getCriteria();
            c.add(Restrictions.eq("name",name));
            optic=(Optics)c.uniqueResult();
        } catch (Exception e) {
            Util.catchException(e);
        }
        return optic;
    }

    public void addField() {
        OpticsFields field= new OpticsFields();
        field.setFontsize(10f);
        field.setLeftx(8f);
        field.setTopy(1f);
        field.setActive(true);
        field.setRefOptic(getItem());
        fields.add(field);
    }

    public void deleteField(OpticsFields field){
        fields.remove(field);
    }

    @PostConstruct
    public void init() {
        fonts=new ArrayList<>();
        fonts.add("Arial");
        fonts.add("Impact");
        fonts.add("Lucida");
        fonts.add("Tahoma");
        fonts.add("Verdana");
        fonts.add("Times");
    }

    public void reset() {
        super.reset();
        getSession().clear();
        ratio=settingsDao.findByName("opticRatio").getValueFloat();
        getItem().setMarginx(10);
        getItem().setMarginy(10);
        lines=null;
        if (partsDtos!=null) { partsDtos.clear();}
            else { partsDtos = new ArrayList<>();}
        if (fields!=null) {fields.clear();}
            else { fields=new ArrayList<>(); }
        addDefaultFields();
        createSvg();
    }

    public void handleOpticChange(SelectEvent selectEvent) {
        selected=(Optics)selectEvent.getObject();
        if (selected!=null) {
            partsDtos=new ArrayList<>();
            fields.clear();
            setItem(selected);
            fields=new ArrayList(opticFieldsDao.findFieldsByOptic(selected));
            List<OpticsParts> parts=new ArrayList(selected.getOpticsPartsCollection());
            for (OpticsParts part:parts) {
                OpticsPartsDto partDto=new OpticsPartsDto(part);
                partsDtos.add(partDto);
            }
            distributeChanged();
            itemsChanged=true;
            createSvg();
            status=Status.UPDATE;
        }
    }

    public Long saveas() {
        try {
            if (getItem().getTid()==null) {
                throw new Exception("ONCE KAYITLI BIR OPTIK SECINIZ");
            }
            Optics optic = (Optics) getItem().clone();
            Long tid=super.create(optic);
            for (OpticsParts opart:optic.getOpticsPartsCollection()) {
                opart.setRefOptic(optic);
            }
            for (OpticsFields ofield:optic.getOpticsFieldsCollection()) {
                ofield.setRefOptic(optic);
            }
            update(optic);
            setItem(optic);
            //log.info("CLONE OPTIC ID : " + tid);
            Util.setFacesMessage("FARKLI KAYIT EDILDI");
        } catch (Exception e) {
            Util.catchException(e);
        }
        return null;
    }

    @Transactional
    public Long save() {
        try {
            if (status== Status.UPDATE) {
                getItem().setOpticsFieldsCollection(fields);
                List<OpticsParts> parts=new ArrayList<>();
                for (OpticsPartsDto dto:partsDtos) {
                    parts.add(dto.toEntity());
                }
                getItem().setOpticsPartsCollection(parts);
                update();
                for (OpticsPartsDto part: partsDtos) {
                    //log.info("PART CHARS: " + part.getChars());
                    log.info("OPTICPARTDTO x,y,w,h: " + part.getTitle() + " : " + part.getX() + "," + part.getY()
                            + "," + part.getW() + "," + part.getH());
                    getSession().merge(part.toEntity());
                }
                Util.setFacesMessage("KAYIT GUNCELLENDI");
            } else {
                getItem().setCreated(Util.getNow());
                getItem().setActive(true);
                getItem().setRefSchool(Util.getActiveSchool());
                Long opticId=super.create();
                setItem((Optics)getSession().load(Optics.class,opticId));
                //log.info("OPTIC SAVED NAME : " + getItem().getName());
                getSession().flush();
                for (OpticsFields ofield: fields) {
                    ofield.setRefOptic(getItem());
                    ofield.setCreated(Util.getNow());
                    ofield.setActive(true);
                    getSession().saveOrUpdate(ofield);
                }
                for (OpticsPartsDto part: partsDtos) {
                    part.setRefOptic(getItem());
                    part.setCreated(Util.getNow());
                    part.setActive(true);
                    //log.info("PART CHARS: " + part.getChars());
                    getSession().saveOrUpdate(part.toEntity());
                }
                getSession().flush();

                Util.setFacesMessage("YENI KAYIT EKLENDI");
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
        return null;
    }

    public void onFmtSelect(SelectEvent event) {
        Uploads upload=(Uploads) event.getObject();
        lines = Util.readFileLines("/FMT/" + upload.getTid() + ".fmt");
        partsDtos =new ArrayList<>();
        OpticsPartsDto opticPart;
        for (String line:lines) {
            int i=0;
            opticPart=new OpticsPartsDto();
            opticPart.setActive(true);
            while (line.contains("=")) {
                String value;
                int equalpos = 0;
                equalpos = line.indexOf("=");
                value = line.substring(0, equalpos);
                line = line.substring(equalpos + 1);
                switch (i) {
                    case(0) : opticPart.setY(Integer.valueOf(value)); break;
                    case(1) : opticPart.setH(Integer.valueOf(value)); break;
                    case(2) : opticPart.setX(Integer.valueOf(value)); break;
                    case(3) : if (StringUtils.isNumeric(value)) opticPart.setW(Integer.valueOf(value)); break;
                    case(4) : opticPart.setCharType(value); break;
                    case(5) :
                        if (value.equals("Y")) {
                        opticPart.setHorizontal(true);
                    }else {
                        opticPart.setHorizontal(false);
                    }
                        break;
                    case(6) : opticPart.setChars(value); break;
                    case(7) :  break;
                    case(8) : opticPart.setTitle(value); break;
                    default: break;

                }
                opticPart.setPrint(true);
                i++;
            }
            partsDtos.add(opticPart);
        }
        createSvg();
    }

    /*
    61=43=02=D=*= =
    01=01=35=41=K=Y=A B C D=X2=KİTAPÇIK=
    11=20=33=43=S=D=0123456789=X2=TC KİMLİK=
    24=33=33=43=S=D=0123456789=X2=GSM NO=
    38=47=39=43=S=D=0123456789=X2=ÖĞRENCİ NO=

    05=33=02=25=K=D=ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ=X2=ADI SOYADI=
    05=14=27=28=S=D=0123456789=X2=SINIF=
    05=33=29=31=K=D=ABCÇDEFGĞHIİJKLMNOÖPRSŞTUÜVYZ=X2=ŞUBE=
    50=56=39=39=K=D=1 2 3 4=X2=ALANI BÖLÜMÜ=
    59=61=39=39=K=D=K E=X2=KIZ ERKEK=
    37=61=03=07=K=Y=ABCDE=X2=DERS 1=
    37=61=09=13=K=Y=ABCDE=X2=DERS 2=
    37=61=15=19=K=Y=ABCDE=X2=DERS 3=
    37=61=21=25=K=Y=ABCDE=X2=DERS 4=
    37=61=27=31=K=Y=ABCDE=X2=DERS 5=
    37=61=33=37=K=Y=ABCDE=X2=DERS 6=
     */
    public void createSvg() {
        Optics optic=getItem();
        if (getItem()==null) {
            optic = new Optics();
            optic.setMarginx(10);
            optic.setMarginy(10);
        }
        //StringBuffer sb=new StringBuffer("<svg width=\"420\" height=\"597\">");
        //StringBuffer sb=new StringBuffer("<svg width='210mm' height='297mm' viewBox='0 0 420 597'>");
        //StringBuffer sb=new StringBuffer("<svg width='100%' height='100%' >");

        StringBuffer sb=new StringBuffer("<svg width='210mm' height='295mm' style='transform:scale(0.55);transform-origin:0 0;'>");

        //draw markers
        float recty;
        if (!partsDtos.isEmpty()) {
            for (int i = 0; i <= partsDtos.get(0).getY(); i++) {
                recty = optic.getMarginy()+ i * ratio;
                sb.append("<rect class='rect' x='1' y='" + recty + "' width='" + ratio + "' height='" + (ratio/2) + "' fill='black' />");
            }
        }

        for (OpticsPartsDto opticPart: partsDtos) {
            //log.info("OpticsPartsDto : " + opticPart);
            opticPart.setOptic(optic);
            opticPart.setRatio(ratio);
            /*
            sb.append("<g> <rect class='rect' x='" + opticPart.getX() + "' y='" +opticPart.getY()
                    + "' width='" + opticPart.getW() + "' height='" + opticPart.getH()
                    + "' stroke=\"green\" stroke-width=\"1\" fill='none' />");
            */
            Random rand=new Random();
            int rgb1=rand.nextInt(255);
            int rgb2=rand.nextInt(255);
            int rgb3=rand.nextInt(255);
            for (int i=0; i<(opticPart.getW()-opticPart.getX()+1); i++) {
                for (int j = 0; j <(opticPart.getH()-opticPart.getY()+1); j++) {
                    float x = opticPart.findX() + (i * ratio) + 1;

                    float y = opticPart.findY() + (j * ratio) + 1;

                    sb.append("<rect class='rect' x='" + x + "' y='" + y
                            + "' width='" + (ratio - 2) + "' height='" + (ratio - 2)
                            + "' stroke='rgb("+rgb1+","+rgb2+","+rgb3+")' stroke-width='1' fill='none' />");
                }
            }
            sb.append("</g>");
        }


        sb.append("</svg>");
        svg=sb.toString();
    }

    public void printPreview() {
        try {
            //StringBuffer sb=new StringBuffer("<svg width=\"420\" height=\"597\">");
            StringBuffer sb = new StringBuffer("<svg width='210mm' height='295mm' style='transform:scale(0.55);transform-origin:0 0;'>");
            List<Distributes> distributes = distributeDao.getDistributes();
            if ((distributes != null) && (distributes.size() > 0)) {
                if (getItem().getFirstno() != null) {
                    printPage(sb, distributes.get(getItem().getFirstno() - 1), true);
                } else {
                    printPage(sb, distributes.get(0), true);
                }
            }
            sb.append("</svg>");
            svgpreview = sb.toString();
        } catch (Exception e) {
            Util.catchException(e);
        }
    }


    public void createPrintSvg() {
        try {
            //StringBuffer sb=new StringBuffer("<svg width=\"420\" height=\"597\">");
            StringBuffer sb = new StringBuffer("");
//        sb.append("<pageSet>");
            int pagecount = 0;
            //getItem().setRatio(21);
            for (Distributes dist : distributeDao.getDistributes()) {
                pagecount++;
                //log.info("PAGECOUNT: " + pagecount);
                if ((getItem().getFirstno() != null) && (pagecount < getItem().getFirstno())) {
                    continue;
                }
                if ((getItem().getLastno() != null) && (pagecount > getItem().getLastno())) {
                    continue;
                }
//            sb.append("<page>");
                //log.info("PAGECOUNT: " + pagecount);
                sb.append("<svg width='210mm' height='295mm' class='page-break'>");
                printPage(sb, dist, false);
                sb.append("</svg>");
//            sb.append("<div class='page-break'></div>");
//            sb.append("</page>");


            }
//        sb.append("</pageSet>");
//        sb.append("</svg>");
            svgprint = sb.toString();
        } catch (Exception e) {
            Util.catchException(e);
        }
    }

    public void downloadOptic() {
        HttpServletResponse response = (HttpServletResponse)
                FacesContext.getCurrentInstance().getExternalContext().getResponse();

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition","attachment;filename=optic.html");
        try {
            ServletOutputStream out = response.getOutputStream();
            String head = "<!DOCTYPE html> <html lang='tr'>"
                    + "<head> <meta charset='utf-8'>"
                    + "<meta http-equiv='content-type' content='text/html; charset=UTF-8'/>"
                    + "<link type='text/css' rel='stylesheet' "
                    + "href='http://www.ortaksinav.com.tr/javax.faces.resource/pages/optic.css?ln=css' />";
            out.write(head.getBytes());
            out.write("</head>".getBytes());
            out.write(svgprint.getBytes("UTF-8"));
            out.write("</html>".getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void printPage(StringBuffer sb,Distributes dist,Boolean preview) {
        int recty;
        Optics optic = getItem();
        Students student;
        try {
            if (dist.getRefSchool().getUseMernis()) {
                student = studentsDao.findByMernis(dist.getMernis());
            } else {
                student = studentsDao.findByNoAndSchool(Integer.valueOf(dist.getSchoolNo()), dist.getRefSchool());
            }
            for (OpticsFields field : fields) {
                log.info("DIST NAME : " + field.getRefFieldType().getNameDist());
                float x = (field.getLeftx()-2) * ratio;// + optic.getMarginx();
                float y = field.getTopy() * ratio;// + optic.getMarginy();
                if ((field.getActive()) &&(field.getRefFieldType().getNameDist()!=null)){
                    if (field.getRefFieldType().getNameDist().equals("barcode")) {
                        Barcode barcode = new Barcode();
                        barcode.setValue(solveText(field.getValue1(),student,dist));
                        String codeType=settingsDao.findByName("opticBarcode").getValueStr();
                        if (codeType!=null) {
                            barcode.setType(codeType);
                        } else {
                            barcode.setType("code128");
                        }
                        BarcodeRendererBean renderer = new BarcodeRendererBean();
                        sb.append("<image xlink:href='http://www.ortaksinav.com.tr");
                        sb.append(renderer.encodeSrc(FacesContext.getCurrentInstance(), barcode));
                        String width= "50" ;
                        if (field.getValue2()!=null) {
                            width=field.getValue2();
                        };
                        sb.append("' x='" + x + "' y='" + y + "' width='" + width
                                + "' height='" + width + "' />");
                    }else if ((field.getRefFieldType().getNameDist().equals("photo")) ) { // && (student.getRefImage()!=null) ) {
                        if (student.getRefImage()!=null) {
                            String filepath = "/student/" + student.getRefSchool().getTid() + "/"
                                    + student.getTid() + "." + student.getRefImage().getExtension();
                            svgAppendImage(field,sb,filepath,x,y);
                        }
                    }else if ((field.getRefFieldType().getNameDist().equals("logo")) ) { // && (student.getRefImage()!=null) ) {
                        if (Util.getActiveSchool().getRefImage()!=null) {
                            String filepath = "/school/" + Util.getActiveSchool().getTid()
                                    + "." + Util.getActiveSchool().getRefImage().getExtension();
                            svgAppendImage(field,sb,filepath,x,y);
                        }
                    } else {
                        sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='" + field.getFontsize() + "'"
                                + " font-family='" + optic.getFontname() + "'");
                        if (field.isHorizontal()) {
                            sb.append(" transform='rotate(270," + x+ "," + y + ")'" );
                            //sb.append(" transform='translate(" + ")'");
                        }
                        sb.append(">");
                        switch (field.getRefFieldType().getNameDist()) {
                            case "mernis":
                                if (dist.getMernis() != null)
                                    appendText(sb,dist.getMernis());
                                break;
                            case "school":
                                if (dist.getRefSchool() != null)
                                    appendText(sb,dist.getRefSchool().getName());
                                break;
                            case "fullname":
                                if (dist.getName() != null)
                                    sb.append(dist.getName() + " " + dist.getSurname());
                                break;
                            case "name":
                                if (dist.getName() != null)
                                    sb.append(dist.getName());
                                break;
                            case "surname":
                                if (dist.getSurname() != null)
                                    sb.append(dist.getSurname());
                                break;
                            case "schoolNo":
                                if (dist.getSchoolNo() != null)
                                    sb.append(dist.getSchoolNo());
                                break;
                            case "room":
                                if (dist.getRoom() != null)
                                    appendText(sb,dist.getRoom());
                                break;
                            case "roomRank":
                                if (dist.getRoomRank() != 0)
                                    sb.append(dist.getRoomRank());
                                break;
                            case "booklet":
                                if (dist.getBooklet() != null)
                                    sb.append(dist.getBooklet());
                                break;
                            case "gender":
                                if ((student != null) && (student.getGender() != null))
                                    appendText(sb,student.getGender());
                                break;
                            case "email":
                                if ((student != null) && (student.getEmail() != null))
                                    appendText(sb,student.getEmail());
                                break;
                            case "phone":
                                if ((student != null) && (student.getPhone() != null))
                                    appendText(sb,student.getPhone());
                                break;
                            case "classroom":
                                if ((student != null) && (student.getRefSchoolClass() != null))
                                    appendText(sb,Util.findClassLevelAndBranch(student.getRefSchoolClass().getName(),true));
                                break;
                            case "branch":
                                if ((student != null) && (student.getRefSchoolClass() != null))
                                    appendText(sb,Util.findClassLevelAndBranch(student.getRefSchoolClass().getName(),false));
                                break;
                            case "address":
                                if ((student != null) && (student.getAddress() != null))
                                    appendText(sb,student.getAddress());
                                break;
                            case "text":
                                if (field.getValue1() != null)
                                    appendText(sb,solveText(field.getValue1(),student,dist));
                                break;
                            case "lesson1":
                                if (student != null)
                                    appendText(sb,findLessonName(student,0));
                                break;
                            case "lesson2":
                                if (student != null)
                                    appendText(sb,findLessonName(student,1));
                                break;
                            case "lesson3":
                                if (student != null)
                                    appendText(sb,findLessonName(student,2));
                                break;
                            case "lesson4":
                                if (student != null)
                                    appendText(sb,findLessonName(student,3));
                                break;
                            case "lesson5":
                                if (student != null)
                                    appendText(sb,findLessonName(student,4));
                                break;
                            case "lesson6":
                                if (student != null)
                                    appendText(sb,findLessonName(student,5));
                                break;

                        }
                        sb.append("</text>");
                    }
                }
            }

            for (OpticsPartsDto part : partsDtos) {
                part.setOptic(optic);
                part.setRatio(ratio);
                log.info("part : " + part);
                log.info("fieldtype : " + part.getRefFieldType());
                if (part.getRefFieldType()!=null) {
                    //log.info("namedist : " + part.getRefFieldType().getNameDist());
                }
                log.info("student : " + student);
                if (part.getActive() && part.getRefFieldType()!=null
                        && part.getRefFieldType().getNameDist()!=null
                        && student!=null) {
                    switch (part.getRefFieldType().getNameDist()) {
                        case "mernis" : printCode(sb,part,dist.getMernis()); break;
                        case "fullname" : printCode(sb,part,dist.getName() + " " + dist.getSurname());break;
                        case "name" : printCode(sb,part,dist.getName()); break;
                        case "surname" : printCode(sb,part,dist.getSurname()); break;
                        case "schoolNo" : printCode(sb,part,dist.getSchoolNo()); break;
                        case "room" : printCode(sb,part,dist.getRoom()); break;
                        case "roomRank" : printCode(sb,part,dist.getRoomRank()); break;
                        case "booklet" : printCode(sb,part,dist.getBooklet()); break;
                        case "gender" : printCode(sb,part,student.getGender()); break;
                        case "email" : printCode(sb,part,student.getEmail()); break;
                        case "phone" : printCode(sb,part,student.getPhone()); break;
                        case "classroom" : printCode(sb,part,Util.findClassLevelAndBranch(student.getRefSchoolClass().getName(),true)); break;
                        case "address" : printCode(sb,part,student.getAddress()); break;
                        case "branch" : printCode(sb,part,Util.findClassLevelAndBranch(student.getRefSchoolClass().getName(),false)); break;
                        case "lesson1" : printCodeTitle(sb,part,findLessonName(student,0)); break;
                        case "lesson2" : printCodeTitle(sb,part,findLessonName(student,1));break;
                        case "lesson3" : printCodeTitle(sb,part,findLessonName(student,2)); break;
                        case "lesson4" : printCodeTitle(sb,part,findLessonName(student,3)); break;
                        case "lesson5" : printCodeTitle(sb,part,findLessonName(student,4)); break;
                        case "lesson6" : printCodeTitle(sb,part,findLessonName(student,5)); break;
                    }
                }
            }
        } catch (Exception e) {
            Util.catchException(e);
        }

    }

    public String solveText(String text, Students student,Distributes dist) {
        if (text.contains("#")) {
            int counter = 0;
            for( int i=0; i<text.length(); i++ ) {
                if( text.charAt(i) == '#' ) {
                    counter++;
                }
            }


            for (int i=0; i<counter; i++) {
                int x=text.indexOf("#");
                int y=-1;
                if (text.substring(x).indexOf(" ")>-1) {
                    y=x+text.substring(x).indexOf(" ");
                }
                //log.info("X : " + x + " , Y : " + y);

                String texttype=null;
                if (y>-1) {
                    texttype=text.substring(x+1,y);
                } else {
                    texttype=text.substring(x+1);
                }
                String textvalue=null;
                switch (texttype) {
                    case "kimlik" : textvalue = student.getMernis(); break;
                    case "no" : textvalue = student.getSchoolNo().toString(); break;
                    case "adsoyad" : textvalue = student.getName() + " " + student.getSurname(); break;
                    case "ad" : textvalue = student.getName(); break;
                    case "soyad" : textvalue = student.getSurname(); break;
                    case "okul" : textvalue = Util.getActiveSchool().getName(); break;
                    case "salon" : textvalue = dist.getRoom(); break;
                    case "sira" : textvalue = String.valueOf(dist.getRoomRank()); break;
                    case "cins" : textvalue = student.getGender(); break;
                    case "sinif" : textvalue = Util.findClassLevelAndBranch(student.getRefSchoolClass().getName(),true); break;
                    case "sube" : textvalue = Util.findClassLevelAndBranch(student.getRefSchoolClass().getName(),false); break;
                    case "kitapcik" : textvalue = dist.getBooklet(); break;
                    case "tel" : textvalue = student.getPhone(); break;
                    case "email" : textvalue = student.getEmail(); break;
                    case "adres" : textvalue = student.getAddress(); break;
                    case "ders1" : textvalue = dist.getLesson1(); break;
                    case "ders2" : textvalue = dist.getLesson2(); break;
                    case "ders3" : textvalue = dist.getLesson3(); break;
                    case "ders4" : textvalue = dist.getLesson4(); break;
                    case "il" : textvalue = Util.getActiveSchool().getRefCity().getName(); break;
                    case "ilce" : textvalue = Util.getActiveSchool().getRefTown().getName(); break;
                    case "okulkodu" : textvalue = Util.getActiveSchool().getMebCode(); break;

                    case "tarih" :
                        Calendar calendar=Calendar.getInstance();
                        calendar.setTime(getItem().getScheduleDate());
                        textvalue =  calendar.get(Calendar.DAY_OF_MONTH) + " "
                                + calendar.getDisplayName(Calendar.MONTH,Calendar.LONG_STANDALONE,new Locale("TR"));
                }
                //log.info("texttype : " + texttype + ", textvalue : " + textvalue);
                if (y > -1 ) {
                    if (textvalue==null) {
                        text = text.substring(0, x).concat(text.substring(y));
                    } else {
                        text = text.substring(0, x).concat(textvalue).concat(text.substring(y));
                    }
                } else {
                    if (textvalue==null) {
                        text = text.substring(0, x);
                    } else {
                        text = text.substring(0, x).concat(textvalue);
                    }
                }
            }
        }

        return text;
    }

    public void appendText(StringBuffer sb, String text){
        if (text!=null) {
            sb.append(text);
        }
    }

    public String findLessonName(Students student, int lessonIndex) {
        String lessonName=null;
        List<Schedules> schedules=scheduleDao.findDaySchedules(getItem().getScheduleDate());
        log.info("SCHEDULES : " + schedules);
        ArrayList<Schedules> studentSchedules=new ArrayList<>();
        for (Schedules schedule:schedules) {
            log.info("SCHEDULE CLASS TYPE : " + schedule.getRefLessonGroup().getRefSchoolClassType());
            log.info("STUDENT CLASS TYPE : " + student.getRefSchoolClass().getRefSchoolClassType());
            if (schedule.getRefLessonGroup().getRefSchoolClassType().getTid().equals(
                    student.getRefSchoolClass().getRefSchoolClassType().getTid())) {
                studentSchedules.add(schedule);
            }
        }
        log.info("STUDENT SCHEDULES : " + studentSchedules);
        for (int i=0; i<studentSchedules.size(); i++) {
            if ( (i==lessonIndex) && (studentSchedules.get(i).getRefLessonName()!=null)) {
                if ((studentSchedules.get(i).getRefLessonName().getShortnameTr()!=null) &&
                    (!studentSchedules.get(i).getRefLessonName().getShortnameTr().equals("")) ) {
                    lessonName = studentSchedules.get(i).getRefLessonName().getShortnameTr();
                } else {
                    lessonName = studentSchedules.get(i).getRefLessonName().getNameTr();
                }
            }
        }
        log.info("LESSON INDEX : " + lessonIndex + " , NAME : |" + lessonName + "|");
        return lessonName;
    }

    public void printCodeTitle(StringBuffer sb,OpticsPartsDto part,Object code) {
        if (part.isPrintTitle() && code!=null)  {
            float x = ((part.getX() - 2) * ratio) + part.getOptic().getMarginx();
            float y = (part.getY() * ratio) + part.getOptic().getMarginy() - 2;
            String codeStr = String.valueOf(code);
            //log.info("CODESTR : |" + codeStr + "|");
            if ( (codeStr != null) && (!codeStr.equals("null")) ) {
                for (int i = 0; i < codeStr.length(); i++) {
                    char charc = codeStr.charAt(i);
                    float xc = x + (i * ratio) + 4;
                    sb.append("<text x='" + xc + "' y='" + y + "' fill='black' font-size='" + ratio + "' > "
                            + charc + "</text>");
                }
            }
        }
    }

    public void printCode(StringBuffer sb,OpticsPartsDto part,Object code) {
        log.info("DIST NAME : " + part.getRefFieldType().getNameDist());
        float x = part.findX();
        float y = part.findY();
        //if (part.getCharType().equals("K")) {
        String codeStr=String.valueOf(code);
        if (part.isPrintTitle()) {
            printCodeTitle(sb,part,code);

        }
        if (part.isPrint()) {
            log.info("CODESTR : " + codeStr);
            if (codeStr != null) {
                for (int i = 0; i < codeStr.length(); i++) {
                    char charc = codeStr.charAt(i);
                    for (int j = 0; j < part.getChars().length(); j++) {
                        char chart = part.getChars().charAt(j);
                        if (charc == chart) {
                            float xc,yc;
                            if (part.isHorizontal()) {
                                xc = x + j * ratio + ratio / 2;
                                yc = y + i * ratio + ratio / 2;
                            } else {
                                xc = x + i * ratio + ratio / 2;
                                yc = y + j * ratio + ratio / 2;
                            }
                            sb.append("<circle class='rect' cx='" + xc + "' cy='" + yc
                                    + "' r='" + ((ratio / 2) - 1.8)
                                    + "' stroke-width='0' fill='black' />");
                        }
                    }
                }
            }
        }

    }

    public void addDefaultFields() {
        log.info("Addind Defaults");
        try {
            fieldsTypes =new ArrayList<>();
            fieldsTypes =opticFieldsTypeDao.findDefaultFields();
            log.info("Default Fields : " + fieldsTypes);
            fields=new ArrayList<>();
            OpticsFields ofield;
            for (OpticsFieldsType ftype:fieldsTypes) {
                ofield=new OpticsFields();
                ofield.setRefFieldType(ftype);
                ofield.setLeftx(ftype.getLeftx());
                ofield.setTopy(ftype.getTopy());
                ofield.setFontsize(ftype.getFontsize());
                fields.add(ofield);
            }
        } catch (Exception e) {
            Util.catchException(e);
        }
    }


    public OpticDao() {
        super(Optics.class);
    }

    public List<OpticsFieldsType> getFieldsTypes() {
        if (fieldsTypes ==null) addDefaultFields();
        return fieldsTypes;
    }

    public String getSvg() {
        if ((svg==null) && (partsDtos !=null)) {
            createSvg();
        }
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }

    public void setFieldsTypes(List<OpticsFieldsType> fieldsTypes) {
        this.fieldsTypes = fieldsTypes;
    }

    public Optics getSelected() {
        return selected;
    }

    public void setSelected(Optics selected) {
        this.selected = selected;
    }

    public List<OpticsPartsDto> getPartsDtos() {
        return partsDtos;
    }

    public void setPartsDtos(List<OpticsPartsDto> partsDtos) {
        this.partsDtos = partsDtos;
    }

    public List<OpticsFields> getFields() {
        return fields;
    }

    public void setFields(List<OpticsFields> fields) {
        this.fields = fields;
    }

    public String getSvgprint() {
        return svgprint;
    }

    public void setSvgprint(String svgprint) {
        this.svgprint = svgprint;
    }

    public String getSvgpreview() {
        return svgpreview;
    }

    public void setSvgpreview(String svgpreview) {
        this.svgpreview = svgpreview;
    }


    public List<String> getFonts() {
        return fonts;
    }

    public void setFonts(List<String> fonts) {
        this.fonts = fonts;
    }

    public List<Optics> getOptics() {
        optics=findOptics();
        return optics;
    }

    public void setOptics(List<Optics> optics) {
        this.optics = optics;
    }

}

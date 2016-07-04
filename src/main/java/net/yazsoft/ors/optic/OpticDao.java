package net.yazsoft.ors.optic;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.distribute.DistributeDao;
import net.yazsoft.ors.entities.*;
import net.yazsoft.ors.students.StudentsDao;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private Integer firstno,lastno;

    private List<OpticsFieldsType> fieldsTypes;
    private List<OpticsFields> fields;

    @Inject OpticFieldsTypeDao opticFieldsTypeDao;
    @Inject OpticFieldsDao opticFieldsDao;
    @Inject DistributeDao distributeDao;
    @Inject StudentsDao studentsDao;


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

    public void reset() {
        getSession().clear();
        setItem(new Optics());
        getItem().setMarginx(10);
        getItem().setMarginy(10);
        getItem().setRatio(9);
        lines=null;
        if (partsDtos!=null) { partsDtos.clear();}
            else { partsDtos = new ArrayList<>();}
        if (fields!=null) {fields.clear();}
            else { fields=new ArrayList<>(); }
        addDefaultFields();
        createSvg();
    }

    @PostConstruct
    public void init() {
        setItem(new Optics());
        getItem().setMarginx(10);
        getItem().setMarginy(10);
        getItem().setRatio(9);
        lines=null;
        partsDtos=new ArrayList<>();
        fields=new ArrayList<>();
    }

    public void handleOpticChange() {
        if (selected!=null) {
            partsDtos=new ArrayList<>();
            fields.clear();
            setItem(selected);
            fields=new ArrayList(selected.getOpticsFieldsCollection());
            List<OpticsParts> parts=new ArrayList(selected.getOpticsPartsCollection());
            for (OpticsParts part:parts) {
                OpticsPartsDto partDto=new OpticsPartsDto(part);
                partsDtos.add(partDto);
            }
            itemsChanged=true;
            createSvg();
        }
    }

    @Transactional
    public Long save() {
        try {
            if (findByName(getItem().getName())!=null) { //if same name then update
                getItem().setOpticsFieldsCollection(fields);
                List<OpticsParts> parts=new ArrayList<>();
                for (OpticsPartsDto dto:partsDtos) {
                    parts.add(dto.toEntity());
                }
                getItem().setOpticsPartsCollection(parts);
                update();
                for (OpticsPartsDto part: partsDtos) {
                    log.info("PART CHARS: " + part.getChars());
                    getSession().merge(part.toEntity());
                }
                //Util.setFacesMessage("KAYIT GUNCELLENDI");
            } else {
                getItem().setCreated(Util.getNow());
                getItem().setActive(true);
                getItem().setRefSchool(Util.getActiveSchool());
                Long opticId=super.create();
                setItem((Optics)getSession().load(Optics.class,opticId));
                log.info("OPTIC SAVED NAME : " + getItem().getName());
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
                    log.info("PART CHARS: " + part.getChars());
                    getSession().saveOrUpdate(part.toEntity());
                }
                getSession().flush();

                Util.setFacesMessage("KAYIT EDILDI");
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
            optic.setRatio(9);
            optic.setMarginx(10);
            optic.setMarginy(10);
        }
        //StringBuffer sb=new StringBuffer("<svg width=\"420\" height=\"597\">");
        //StringBuffer sb=new StringBuffer("<svg width='210mm' height='297mm' viewBox='0 0 420 597'>");
        StringBuffer sb=new StringBuffer("<svg width='100%' height='100%' >");
        int recty;
        for (int i=0; i<60; i++) {
            recty=(i*9)+30;
            sb.append("<rect class='rect' x='1' y='" +recty + "' width='10' height='3' fill='black' />");
        }
        for (OpticsPartsDto opticPart: partsDtos) {
            log.info("OpticsPartsDto : " + opticPart);
            opticPart.setOptic(optic);
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
                    int x = opticPart.findX() + (i * optic.getRatio()) + 1;
                    int y = opticPart.findY() + (j * optic.getRatio()) + 1;

                    sb.append("<rect class='rect' x='" + x + "' y='" + y
                            + "' width='" + (optic.getRatio() - 2) + "' height='" + (optic.getRatio() - 2)
                            + "' stroke='rgb("+rgb1+","+rgb2+","+rgb3+")' stroke-width='1' fill='none' />");
                }
            }
            sb.append("</g>");
        }


        sb.append("</svg>");
        svg=sb.toString();
    }

    public void printPreview() {

        //StringBuffer sb=new StringBuffer("<svg width=\"420\" height=\"597\">");
        StringBuffer sb=new StringBuffer("<svg width='100%' height='100%'>");
        if (firstno!=null) {
            printPage(sb,distributeDao.getDistributes().get(firstno-1),true);
        } else {
            printPage(sb, distributeDao.getDistributes().get(0),true);
        }
        sb.append("</svg>");
        svgpreview=sb.toString();
    }


    public void createPrintSvg() {
        //StringBuffer sb=new StringBuffer("<svg width=\"420\" height=\"597\">");
        StringBuffer sb = new StringBuffer(""); //<svg width='210mm' height='297mm' >");
//        sb.append("<pageSet>");
        int pagecount=1;
        //getItem().setRatio(21);
        for (Distributes dist:distributeDao.getDistributes()) {
            if ((firstno!=null) && (pagecount<firstno)){
                continue;
            }
            if ((lastno!=null) && (pagecount>lastno)) {
                continue;
            }
//            sb.append("<page>");
            sb.append("<svg width='210mm' height='297mm' >");
            printPage(sb,dist,false);
            sb.append("</svg>");
//            sb.append("</page>");
            pagecount++;

        }
//        sb.append("</pageSet>");
//        sb.append("</svg>");
        svgprint=sb.toString();
    }

    public void printPage(StringBuffer sb,Distributes dist,Boolean preview) {
        int recty;
        Optics optic = getItem();
        int ratio=getItem().getRatio();
        if (!preview) ratio=getItem().getRatiop(); //print ratio
        Students student;
        if (dist.getRefSchool().getUseMernis()) {
            student = studentsDao.findByMernis(dist.getMernis());
        } else {
            student = studentsDao.findByNoAndSchool(Integer.valueOf(dist.getSchoolNo()), dist.getRefSchool());
        }
        for (OpticsFields field : fields) {
            log.info("DIST NAME : " + field.getRefFieldType().getNameDist());
            float x = field.getLeftx() * ratio + optic.getMarginx();
            float y = field.getTopy() * ratio + optic.getMarginy();
            if ((field.getActive()) &&(field.getRefFieldType().getNameDist()!=null)){
                switch (field.getRefFieldType().getNameDist()) {
                    case "mernis" : if (dist.getMernis()!=null)
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + dist.getMernis() + "</text>"); break;
                    case "school" : if (dist.getRefSchool()!=null)
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + dist.getRefSchool().getName() + "</text>"); break;
                    case "fullname" :if (dist.getName()!=null)
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + dist.getName() + " " + dist.getSurname() + "</text>"); break;
                    case "name" : if (dist.getName()!=null)
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + dist.getName() + "</text>"); break;
                    case "surname" : if (dist.getSurname()!=null)
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + dist.getSurname() + "</text>"); break;
                    case "schoolNo" : if (dist.getSchoolNo()!=null)
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + dist.getSchoolNo() + "</text>"); break;
                    case "room" : if (dist.getRoom()!=null)
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + dist.getRoom() + "</text>"); break;
                    case "roomRank" : if (dist.getRoomRank()!=0)
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + dist.getRoomRank() + "</text>");break;
                    case "gender" : if ((student!=null) && (student.getGender()!=null))
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + student.getGender() + "</text>");break;
                    case "email" : if ((student!=null) && (student.getEmail()!=null))
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + student.getEmail() + "</text>");break;
                    case "phone" : if ((student!=null) && (student.getPhone()!=null))
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + student.getPhone() + "</text>");break;
                    case "classroom" : if ((student!=null) && (student.getRefSchoolClass()!=null))
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + student.getRefSchoolClass().getName() + "</text>");break;
                    case "address" : if ((student!=null) && (student.getAddress()!=null))
                                sb.append("<text x='" + x + "' y='" + y + "' fill='black' font-size='"
                                + field.getFontsize() + "'>" + student.getAddress() + "</text>");break;
                }
            }
        }

        for (OpticsPartsDto part : partsDtos) {
            part.setOptic(optic);
            if ((part.getActive()) &&(part.getRefFieldType()!=null)
                    && (part.getRefFieldType().getNameDist()!=null)
                    && (student!=null)) {
                switch (part.getRefFieldType().getNameDist()) {
                    case "mernis" : printCode(sb,part,dist.getMernis(),ratio); break;
                    case "fullname" : printCode(sb,part,dist.getName() + " " + dist.getSurname(),ratio);break;
                    case "name" : printCode(sb,part,dist.getName(),ratio); break;
                    case "surname" : printCode(sb,part,dist.getSurname(),ratio); break;
                    case "schoolNo" : printCode(sb,part,dist.getSchoolNo(),ratio); break;
                    case "room" : printCode(sb,part,dist.getRoom(),ratio); break;
                    case "roomRank" : printCode(sb,part,dist.getRoomRank(),ratio); break;
                    case "gender" : printCode(sb,part,student.getGender(),ratio); break;
                    case "email" : printCode(sb,part,student.getEmail(),ratio); break;
                    case "phone" : printCode(sb,part,student.getPhone(),ratio); break;
                    case "classroom" : printCode(sb,part,student.getRefSchoolClass().getName(),ratio); break;
                    case "address" : printCode(sb,part,student.getAddress(),ratio); break;
                }
            }
        }
    }



    public void printCode(StringBuffer sb,OpticsPartsDto part,Object code,int ratio) {
        log.info("DIST NAME : " + part.getRefFieldType().getNameDist());
        float x = part.getX()* ratio + part.getOptic().getMarginx();
        float y = part.getY() * ratio + part.getOptic().getMarginy();
        //if (part.getCharType().equals("K")) {
        String codeStr=String.valueOf(code);
        if (part.isPrintTitle()) {
            sb.append("<text x='" + x + "' y='" + (y-ratio/2) + "' fill='black' font-size='" + ratio+ "' > "
                    + codeStr + "</text>");
        }
        if (part.isPrint()) {
            log.info("CODESTR : " + codeStr);
            if (codeStr != null) {
                for (int i = 0; i < codeStr.length(); i++) {
                    char charc = codeStr.charAt(i);
                    for (int j = 0; j < part.getChars().length(); j++) {
                        char chart = part.getChars().charAt(j);
                        if (charc == chart) {
                            float xc = x + i * ratio + ratio / 2 + 1;
                            float yc = y + j * ratio + ratio / 2 + 1;
                            sb.append("<circle class='rect' cx='" + xc + "' cy='" + yc
                                    + "' r='" + (ratio / 2)
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

    public Integer getFirstno() {
        return firstno;
    }

    public void setFirstno(Integer firstno) {
        this.firstno = firstno;
    }

    public Integer getLastno() {
        return lastno;
    }

    public void setLastno(Integer lastno) {
        this.lastno = lastno;
    }
}

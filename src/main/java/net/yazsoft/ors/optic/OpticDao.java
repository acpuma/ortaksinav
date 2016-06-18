package net.yazsoft.ors.optic;

import net.yazsoft.frame.hibernate.BaseGridDao;
import net.yazsoft.frame.scopes.ViewScoped;
import net.yazsoft.frame.upload.UploadsDao;
import net.yazsoft.frame.utils.Util;
import net.yazsoft.ors.entities.OpticsFieldsType;
import net.yazsoft.ors.entities.Optics;
import net.yazsoft.ors.entities.Uploads;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Named
@ViewScoped
public class OpticDao extends BaseGridDao<Optics> implements Serializable{
    private static final Logger log = Logger.getLogger(OpticDao.class);
    private Optics selected;
    private String svg;
    private String fmt;
    List<String> lines;
    List<OpticPart> parts;

    List<OpticsFieldsType> fields;

    @Inject OpticFieldsTypeDao opticFieldsTypeDao;
    @Inject UploadsDao uploadsDao;


    public void selectFmt() {
        lines = Util.readFileLines("/FMT/" + uploadsDao.getSelected().getTid() + ".fmt");
        parts =new ArrayList<>();
        OpticPart opticPart;
        for (String line:lines) {
            int i=0;
            opticPart=new OpticPart();
            while (line.contains("=")) {
                String value;
                int equalpos = 0;
                equalpos = line.indexOf("=");
                value = line.substring(0, equalpos);
                line = line.substring(equalpos + 1);
                switch (i) {
                    case(0) : opticPart.y=Integer.valueOf(value); break;
                    case(1) : opticPart.h=Integer.valueOf(value); break;
                    case(2) : opticPart.x=Integer.valueOf(value); break;
                    case(3) : if (StringUtils.isNumeric(value)) opticPart.w=Integer.valueOf(value); break;
                    case(4) : opticPart.valueType=value; break;
                    case(5) :
                        if (value.equals("Y")) {
                        opticPart.horizontal = true;
                    }else {
                        opticPart.horizontal = false;
                    }
                        break;
                    case(6) : opticPart.values=value; break;
                    case(7) :  break;
                    case(8) : opticPart.title=value; break;
                    default: break;

                }
                i++;
            }
            parts.add(opticPart);
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
        OpticPart.ratio=9;
        StringBuffer sb=new StringBuffer("<svg width=\"420\" height=\"597\">");
        int recty;
        for (int i=0; i<60; i++) {
            recty=(i*9)+30;
            sb.append("<rect class='rect' x='1' y='" +recty + "' width='10' height='3' fill='black' />");
        }
        for (OpticPart opticPart: parts) {
            log.info("OpticPart : " + opticPart);
            /*
            sb.append("<g> <rect class='rect' x='" + opticPart.getX() + "' y='" +opticPart.getY()
                    + "' width='" + opticPart.getW() + "' height='" + opticPart.getH()
                    + "' stroke=\"green\" stroke-width=\"1\" fill='none' />");
            */
            Random rand=new Random();
            int rgb1=rand.nextInt(255);
            int rgb2=rand.nextInt(255);
            int rgb3=rand.nextInt(255);
            for (int i=0; i<(opticPart.w-opticPart.x+1); i++) {
                for (int j = 0; j <(opticPart.h-opticPart.y+1); j++) {
                    int x = opticPart.getX() + (i * OpticPart.ratio) + 1;
                    int y = opticPart.getY() + (j * OpticPart.ratio) + 1;

                    sb.append("<rect class='rect' x='" + x + "' y='" + y
                            + "' width='" + (OpticPart.ratio - 2) + "' height='" + (OpticPart.ratio - 2)
                            + "' stroke='rgb("+rgb1+","+rgb2+","+rgb3+")' stroke-width='1' fill='none' />");
                }
            }
            sb.append("</g>");
        }


        sb.append("</svg>");
        svg=sb.toString();
    }

    public void addDefaultFields() {
        log.info("Addind Defaults");
        try {
            fields=new ArrayList<>();
            fields=opticFieldsTypeDao.findDefaultFields();
            log.info("Default Fields : " + fields);
        } catch (Exception e) {
            Util.catchException(e);
        }
    }


    public OpticDao() {
        super(Optics.class);
    }

    public List<OpticsFieldsType> getFields() {
        if (fields==null) addDefaultFields();
        return fields;
    }

    public String getSvg() {
        if ((svg==null) && (parts!=null)) {
            createSvg();
        }
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }

    public void setFields(List<OpticsFieldsType> fields) {
        this.fields = fields;
    }

    public Optics getSelected() {
        return selected;
    }

    public void setSelected(Optics selected) {
        this.selected = selected;
    }

    public List<OpticPart> getParts() {
        return parts;
    }

    public void setParts(List<OpticPart> parts) {
        this.parts = parts;
    }
}

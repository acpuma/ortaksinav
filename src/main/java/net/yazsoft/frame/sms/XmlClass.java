package net.yazsoft.frame.sms;

import java.util.ArrayList;
import java.util.List;

public class XmlClass {
    BILGI BILGI;
    List ISLEM;
    //Islem ISLEM;

    public XmlClass() {
        ISLEM = new ArrayList<>();
        //ISLEM = new Islem();
        BILGI = new BILGI();
        BILGI.setKULLANICI_ADI("21B18");
        BILGI.setSIFRE("Y7e0jhv0.");
        BILGI.setBASLIK("ORTAKSINAVM");
    }

    public void addYolla(Yolla YOLLA) {
        ISLEM.add(YOLLA);
    }

    public List getISLEM() {
        return ISLEM;
    }

    public static class BILGI {
        String KULLANICI_ADI;
        String SIFRE;
        String GONDERIM_TARIH;
        String BASLIK;

        public String getKULLANICI_ADI() {
            return KULLANICI_ADI;
        }

        public void setKULLANICI_ADI(String KULLANICI_ADI) {
            this.KULLANICI_ADI = KULLANICI_ADI;
        }

        public String getSIFRE() {
            return SIFRE;
        }

        public void setSIFRE(String SIFRE) {
            this.SIFRE = SIFRE;
        }

        public String getGONDERIM_TARIH() {
            return GONDERIM_TARIH;
        }

        public void setGONDERIM_TARIH(String GONDERIM_TARIH) {
            this.GONDERIM_TARIH = GONDERIM_TARIH;
        }

        public String getBASLIK() {
            return BASLIK;
        }

        public void setBASLIK(String BASLIK) {
            this.BASLIK = BASLIK;
        }
    }

    public XmlClass.BILGI getBILGI() {
        return BILGI;
    }

    public void setBILGI(XmlClass.BILGI BILGI) {
        this.BILGI = BILGI;
    }


}

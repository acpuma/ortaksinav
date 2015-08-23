package net.yazsoft.frame.sms;

public class Yolla {
    String MESAJ;
    String NO;

    public Yolla() {
    }

    public Yolla(String TELNO, String MESAJ) {
        this.MESAJ = MESAJ;
        this.NO = TELNO;
    }

    public String getMESAJ() {
        return MESAJ;
    }

    public void setMESAJ(String MESAJ) {
        this.MESAJ = MESAJ;
    }

    public String getNO() {
        return NO;
    }

    public void setNO(String NO) {
        this.NO = NO;
    }
}

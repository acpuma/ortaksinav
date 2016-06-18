package net.yazsoft.ors.optic;

/**
 * Created by fec on 18/06/16.
 */
public class OpticPart {
    static int ratio=16;
    static int marginx=10;
    static int marginy=10;
    int x,y,w,h;
    boolean horizontal;
    String values;
    String title;
    String valueType;

    public int getX() {
        return marginx+(x*ratio);
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return marginy+(y*ratio);
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return ((w+1)-x)*ratio;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return ((h+1)-y)*ratio;
    }

    public void setH(int h) {
        this.h = h;
    }

    @Override
    public String toString() {
        return "OpticPart{" +
                "x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                ", horizontal=" + horizontal +
                ", values='" + values + '\'' +
                ", title='" + title + '\'' +
                ", valueType='" + valueType + '\'' +
                '}';
    }
}

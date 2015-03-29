package net.yazsoft.ors.barcode;

import net.yazsoft.frame.scopes.ViewScoped;

import javax.inject.Named;

@Named
@ViewScoped
public class BarcodeSer {
    String barcode;

    public void change() {}

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}

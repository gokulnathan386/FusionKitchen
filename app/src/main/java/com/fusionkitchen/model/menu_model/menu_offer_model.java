package com.fusionkitchen.model.menu_model;

public class menu_offer_model {

    private String offertitle;
    private String offerdesc;

    public menu_offer_model(String offertitle, String offerdesc) {
        this.offertitle = offertitle;
        this.offerdesc = offerdesc;
    }


    public String getOffertitle() {
        return offertitle;
    }

    public void setOffertitle(String offertitle) {
        this.offertitle = offertitle;
    }

    public String getOfferdesc() {
        return offerdesc;
    }

    public void setOfferdesc(String offerdesc) {
        this.offerdesc = offerdesc;
    }
}

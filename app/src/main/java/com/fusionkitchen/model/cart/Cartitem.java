package com.fusionkitchen.model.cart;

public class Cartitem {


    private String name, desc, qty, amount, itemamount, id, finalamt, itemid, addonnameid, addonextraid,categoryname,subcategoryname,specialinstruction;


    public Cartitem(String name, String desc, String qty, String amount, String itemamount, String id, String finalamt, String itemid, String addonnameid, String addonextraid, String categoryname, String subcategoryname,String specialinstruction) {

        this.name = name;
        this.desc = desc;
        this.qty = qty;
        this.amount = amount;
        this.itemamount = itemamount;
        this.id = id;
        this.finalamt = finalamt;
        this.itemid = itemid;
        this.addonnameid = addonnameid;
        this.addonextraid = addonextraid;
        this.categoryname = categoryname;
        this.subcategoryname = subcategoryname;
        this.specialinstruction = specialinstruction;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getItemamount() {
        return itemamount;
    }

    public void setItemamount(String itemamount) {
        this.itemamount = itemamount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFinalamt() {
        return finalamt;
    }

    public void setFinalamt(String finalamt) {
        this.finalamt = finalamt;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getAddonnameid() {
        return addonnameid;
    }

    public void setAddonnameid(String addonnameid) {
        this.addonnameid = addonnameid;
    }

    public String getAddonextraid() {
        return addonextraid;
    }

    public void setAddonextraid(String addonextraid) {
        this.addonextraid = addonextraid;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getSubcategoryname() {
        return subcategoryname;
    }

    public void setSubcategoryname(String subcategoryname) {
        this.subcategoryname = subcategoryname;
    }


    public String getSpecialinstruction() {
        return specialinstruction;
    }

    public void setSpecialinstruction(String specialinstruction) {
        this.specialinstruction = specialinstruction;
    }
}

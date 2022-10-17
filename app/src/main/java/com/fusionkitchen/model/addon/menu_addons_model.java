package com.fusionkitchen.model.addon;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class menu_addons_model {

    //AccountResultModel
    @SerializedName("STATUS")
    private String STATUS;

    @SerializedName("DATA")
    private menu_addons_model.dataval DATA;


    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }


    public menu_addons_model.dataval getDATA() {
        return DATA;
    }

    public void setDATA(menu_addons_model.dataval DATA) {
        this.DATA = DATA;
    }


    public class dataval {

        @SerializedName("orderitemname")
        private String orderitemname;

        @SerializedName("addondescription")
        private String addondescription;


        @SerializedName("orderitemprice")
        private String orderitemprice;

        @SerializedName("orderback")
        private String orderback;

        @SerializedName("nextaid")
        private String nextaid;

        @SerializedName("btndisabledcon")
        private String btndisabledcon;

        @SerializedName("btnaddoncontinue")
        private String btnaddoncontinue;

        @SerializedName("addonextra")
        private menu_addons_model.dataval.addonextramod addonextra;

        @SerializedName("addonitems")
        private List<menu_addons_model.dataval.addonitemslist> addonitems;


        @SerializedName("selecteditem")
        private List<menu_addons_model.dataval.selecteditemlist> selecteditem;


        @SerializedName("addonlimit")
        private String addonlimit;


        public String getOrderitemname() {
            return orderitemname;
        }

        public void setOrderitemname(String orderitemname) {
            this.orderitemname = orderitemname;
        }

        public String getAddondescription() {
            return addondescription;
        }

        public void setAddondescription(String addondescription) {
            this.addondescription = addondescription;
        }


        public String getOrderitemprice() {
            return orderitemprice;
        }

        public void setOrderitemprice(String orderitemprice) {
            this.orderitemprice = orderitemprice;
        }

        public String getOrderback() {
            return orderback;
        }

        public void setOrderback(String orderback) {
            this.orderback = orderback;
        }

        public String getNextaid() {
            return nextaid;
        }

        public void setNextaid(String nextaid) {
            this.nextaid = nextaid;
        }

        public String getBtndisabledcon() {
            return btndisabledcon;
        }

        public void setBtndisabledcon(String btndisabledcon) {
            this.btndisabledcon = btndisabledcon;
        }

        public String getBtnaddoncontinue() {
            return btnaddoncontinue;
        }

        public void setBtnaddoncontinue(String btnaddoncontinue) {
            this.btnaddoncontinue = btnaddoncontinue;
        }

        public addonextramod getAddonextra() {
            return addonextra;
        }

        public void setAddonextra(addonextramod addonextra) {
            this.addonextra = addonextra;
        }


        public List<addonitemslist> getAddonitems() {
            return addonitems;
        }

        public void setAddonitems(List<addonitemslist> addonitems) {
            this.addonitems = addonitems;
        }


        public List<selecteditemlist> getSelecteditem() {
            return selecteditem;
        }

        public void setSelecteditem(List<selecteditemlist> selecteditem) {
            this.selecteditem = selecteditem;
        }


        public String getAddonlimit() {
            return addonlimit;
        }

        public void setAddonlimit(String addonlimit) {
            this.addonlimit = addonlimit;
        }

        public class addonextramod {

            @SerializedName("Noid")
            private String Noid;

            @SerializedName("Lessid")
            private String Lessid;

            @SerializedName("Halfid")
            private String Halfid;

            @SerializedName("Onid")
            private String Onid;

            @SerializedName("Withid")
            private String Withid;

            @SerializedName("OnBurgerid")
            private String OnBurgerid;

            @SerializedName("OnChipsid")
            private String OnChipsid;


            public String getNoid() {
                return Noid;
            }

            public void setNoid(String noid) {
                Noid = noid;
            }

            public String getLessid() {
                return Lessid;
            }

            public void setLessid(String lessid) {
                Lessid = lessid;
            }

            public String getHalfid() {
                return Halfid;
            }

            public void setHalfid(String halfid) {
                Halfid = halfid;
            }

            public String getOnid() {
                return Onid;
            }

            public void setOnid(String onid) {
                Onid = onid;
            }

            public String getWithid() {
                return Withid;
            }

            public void setWithid(String withid) {
                Withid = withid;
            }

            public String getOnBurgerid() {
                return OnBurgerid;
            }

            public void setOnBurgerid(String onBurgerid) {
                OnBurgerid = onBurgerid;
            }

            public String getOnChipsid() {
                return OnChipsid;
            }

            public void setOnChipsid(String onChipsid) {
                OnChipsid = onChipsid;
            }
        }

        public class addonitemslist {

            public boolean student;
            @SerializedName("name")
            private String name;
            @SerializedName("price")
            private String price;
            @SerializedName("btnnext")
            private String btnnext;
            @SerializedName("itemprice")
            private String itemprice;
            @SerializedName("itemid")
            private String itemid;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getBtnnext() {
                return btnnext;
            }

            public void setBtnnext(String btnnext) {
                this.btnnext = btnnext;
            }

            public String getItemprice() {
                return itemprice;
            }

            public void setItemprice(String itemprice) {
                this.itemprice = itemprice;
            }

            public String getItemid() {
                return itemid;
            }

            public void setItemid(String itemid) {
                this.itemid = itemid;
            }


        }

        public class selecteditemlist {

            @SerializedName("items")
            private String items;

            public String getItems() {
                return items;
            }

            public void setItems(String items) {
                this.items = items;
            }
        }


    }
}

package com.fusionkitchen.model.order_history;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class reorder_details_show {

    public class item {


        @SerializedName("qty")
        private String qty;


        @SerializedName("notavaiableitem")
        private String notavaiableitem;


        @SerializedName("avaiableitems")
        private reorder_details_show.item.avaiableitems avaiableitems;


        @SerializedName("addon")
        private List<reorder_details_show.item.addonlist> addon;


        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getNotavaiableitem() {
            return notavaiableitem;
        }

        public void setNotavaiableitem(String notavaiableitem) {
            this.notavaiableitem = notavaiableitem;
        }


        public item.avaiableitems getAvaiableitems() {
            return avaiableitems;
        }

        public void setAvaiableitems(item.avaiableitems avaiableitems) {
            this.avaiableitems = avaiableitems;
        }

        public List<addonlist> getAddon() {
            return addon;
        }

        public void setAddon(List<addonlist> addon) {
            this.addon = addon;
        }

        public class avaiableitems {

            @SerializedName("name")
            private String name;


            @SerializedName("id")
            private String id;

            @SerializedName("catname")
            private String catname;

            @SerializedName("subname")
            private String subname;

            @SerializedName("price")
            private String price;



            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCatname() {
                return catname;
            }

            public void setCatname(String catname) {
                this.catname = catname;
            }

            public String getSubname() {
                return subname;
            }

            public void setSubname(String subname) {
                this.subname = subname;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }


        public class addonlist {

            @SerializedName("addon_name")
            private String addon_name;


            @SerializedName("addonid")
            private String addonid;

            @SerializedName("addonextra")
            private String addonextra;


            @SerializedName("price")
            private String price;

            @SerializedName("addonitem")
            private addonlist.addonitemprs addonitem;



            public String getAddon_name() {
                return addon_name;
            }

            public void setAddon_name(String addon_name) {
                this.addon_name = addon_name;
            }


            public String getAddonid() {
                return addonid;
            }

            public void setAddonid(String addonid) {
                this.addonid = addonid;
            }

            public String getAddonextra() {
                return addonextra;
            }

            public void setAddonextra(String addonextra) {
                this.addonextra = addonextra;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }


            public addonitemprs getAddonitem() {
                return addonitem;
            }

            public void setAddonitem(addonitemprs addonitem) {
                this.addonitem = addonitem;
            }

            public class addonitemprs{

                @SerializedName("price")
                private String price;

                public String getPrice() {
                    return price;
                }

                public void setPrice(String price) {
                    this.price = price;
                }
            }






        }
    }
}

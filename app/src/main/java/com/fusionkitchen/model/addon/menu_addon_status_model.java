package com.fusionkitchen.model.addon;

import com.google.gson.annotations.SerializedName;

public class menu_addon_status_model {

    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("error_code")
    private String error_code;

    @SerializedName("error_message")
    private menu_addon_status_model.error_message_cls error_message;


    @SerializedName("addonId")
    private String addonId;



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public error_message_cls getError_message() {
        return error_message;
    }

        public void setError_message(error_message_cls error_message) {
        this.error_message = error_message;
    }

    public String getAddonId() {
        return addonId;
    }

    public void setAddonId(String addonId) {
        this.addonId = addonId;
    }

    public class error_message_cls {

        @SerializedName("img")
        private String img;
        @SerializedName("msg")
        private String msg;



        @SerializedName("takeawayclosed")
        private menu_addon_status_model.error_message_cls.takeawayclosed_cls takeawayclosed;

        @SerializedName("chooseMode")
        private menu_addon_status_model.error_message_cls.chooseMode_cls chooseMode;


        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public takeawayclosed_cls getTakeawayclosed() {
            return takeawayclosed;
        }

        public void setTakeawayclosed(takeawayclosed_cls takeawayclosed) {
            this.takeawayclosed = takeawayclosed;
        }

        public chooseMode_cls getChooseMode() {
            return chooseMode;
        }

        public void setChooseMode(chooseMode_cls chooseMode) {
            this.chooseMode = chooseMode;
        }

        public class takeawayclosed_cls {

            @SerializedName("dbname")
            private String dbname;

            @SerializedName("currentlytakeaway")
            private String currentlytakeaway;

            @SerializedName("imgshoplogo")
            private String imgshoplogo;



            @SerializedName("imgdelivery_close")
            private String imgdelivery_close;

            @SerializedName("popupdelivery")
            private String popupdelivery;

            @SerializedName("text")
            private String text;


            public String getDbname() {
                return dbname;
            }

            public void setDbname(String dbname) {
                this.dbname = dbname;
            }

            public String getCurrentlytakeaway() {
                return currentlytakeaway;
            }

            public void setCurrentlytakeaway(String currentlytakeaway) {
                this.currentlytakeaway = currentlytakeaway;
            }

            public String getImgshoplogo() {
                return imgshoplogo;
            }

            public void setImgshoplogo(String imgshoplogo) {
                this.imgshoplogo = imgshoplogo;
            }


            public String getImgdelivery_close() {
                return imgdelivery_close;
            }

            public void setImgdelivery_close(String imgdelivery_close) {
                this.imgdelivery_close = imgdelivery_close;
            }

            public String getPopupdelivery() {
                return popupdelivery;
            }

            public void setPopupdelivery(String popupdelivery) {
                this.popupdelivery = popupdelivery;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }
        }

        public class chooseMode_cls {

            @SerializedName("popupdelivery")
            private String popupdelivery;

            @SerializedName("onlycollection")
            private String onlycollection;

            @SerializedName("imgdelivery_close")
            private String imgdelivery_close;

            public String getPopupdelivery() {
                return popupdelivery;
            }

            public void setPopupdelivery(String popupdelivery) {
                this.popupdelivery = popupdelivery;
            }

            public String getOnlycollection() {
                return onlycollection;
            }

            public void setOnlycollection(String onlycollection) {
                this.onlycollection = onlycollection;
            }

            public String getImgdelivery_close() {
                return imgdelivery_close;
            }

            public void setImgdelivery_close(String imgdelivery_close) {
                this.imgdelivery_close = imgdelivery_close;
            }
        }
    }
}

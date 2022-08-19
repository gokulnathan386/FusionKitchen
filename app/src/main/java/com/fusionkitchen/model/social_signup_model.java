package com.fusionkitchen.model;

import com.google.gson.annotations.SerializedName;

import com.fusionkitchen.model.loginsignup.login_model;

public class social_signup_model {

    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("user_detail")
    private social_signup_model.loginuserdetails userdetails;

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


    public loginuserdetails getUserdetails() {
        return userdetails;
    }

    public void setUserdetails(loginuserdetails userdetails) {
        this.userdetails = userdetails;
    }

    public class loginuserdetails {

        @SerializedName("id")
        private String id;

        @SerializedName("cid")
        private String cid;

        @SerializedName("vcode")
        private String vcode;

        @SerializedName("type_of_login")
        private String type_of_login;

        @SerializedName("email")
        private String email;



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getVcode() {
            return vcode;
        }

        public void setVcode(String vcode) {
            this.vcode = vcode;
        }

        public String getType_of_login() {
            return type_of_login;
        }

        public void setType_of_login(String type_of_login) {
            this.type_of_login = type_of_login;
        }


        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

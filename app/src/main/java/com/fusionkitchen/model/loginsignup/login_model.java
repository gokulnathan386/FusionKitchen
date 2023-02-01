package com.fusionkitchen.model.loginsignup;

import com.google.gson.annotations.SerializedName;

public class login_model {

    //AccountResultModel
/*    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("userdetails")
    private login_model.loginuserdetails userdetails;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String message) {
        this.message = message;
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

        @SerializedName("fname")
        private String fname;

        @SerializedName("phone")
        private String phone;

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


        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getPhone() {
            return phone;
        }

        public void setphone(String phone) {
            this.phone = phone;
        }
    }*/

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private login_model.data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String message) {
        this.message = message;
    }


    public data getData() {
        return data;
    }

    public void setData(data data) {
        this.data = data;
    }


    public class data {


        @SerializedName("login")
        private String login;

        @SerializedName("user_data")
        private login_model.user_data user_data;

        public void setLogin(String login) {
            this.login = login;
        }


        public String getLogin() {
            return login;
        }

        public user_data getUserdetails() {
            return user_data;
        }

        public void setUserdetails(user_data user_data) {
            this.user_data = user_data;
        }

    }

    public class user_data {


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

        @SerializedName("fname")
        private String fname;

        @SerializedName("phone")
        private String phone;


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


        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getPhone() {
            return phone;
        }

        public void setphone(String phone) {
            this.phone = phone;
        }



    }


    }

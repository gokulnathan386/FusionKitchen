package com.fusionkitchen.model.myaccount;

import com.google.gson.annotations.SerializedName;

public class get_account_modal {

    //AccountResultModel
    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String msg;

    @SerializedName("RESPONSE_CODE")
    private String RESPONSE_CODE;

    @SerializedName("data")
    private get_account_modal.data data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRESPONSE_CODE() {
        return RESPONSE_CODE;
    }

    public void setRESPONSE_CODE(String RESPONSE_CODE) {
        this.RESPONSE_CODE = RESPONSE_CODE;
    }


    public get_account_modal.data getData() {
        return data;
    }

    public void setData(get_account_modal.data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class data {


        @SerializedName("fname")
        private String fname;

        @SerializedName("lname")
        private String lname;

        @SerializedName("email")
        private String email;

        @SerializedName("phone")
        private String phone;

        @SerializedName("password")
        private String password;


        @SerializedName("ssologin")
        private get_account_modal.data.ssologin ssologin;


        @SerializedName("logindata")
        private get_account_modal.data.logindata logindata;


        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getLname() {
            return lname;
        }

        public void setLname(String lname) {
            this.lname = lname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public get_account_modal.data.ssologin getSsologin() {
            return ssologin;
        }

        public void setSsologin(get_account_modal.data.ssologin ssologin) {
            this.ssologin = ssologin;
        }

        public get_account_modal.data.logindata getLogindata() {
            return logindata;
        }

        public void setLogindata(get_account_modal.data.logindata logindata) {
            this.logindata = logindata;
        }

        public class logindata {

            @SerializedName("password")
            private String password;

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }
        }

        public class ssologin {


            @SerializedName("fname")
            private String fname;

            @SerializedName("lname")
            private String lname;

            @SerializedName("email")
            private String email;

            @SerializedName("phone")
            private String phone;

            public String getFname() {
                return fname;
            }

            public void setFname(String fname) {
                this.fname = fname;
            }

            public String getLname() {
                return lname;
            }

            public void setLname(String lname) {
                this.lname = lname;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }
        }
    }
}

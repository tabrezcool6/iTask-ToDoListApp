package com.example.itask;

public class ClassUserInfo {

    public String f_name, e_mail, mob_number, pass_word, u_key;

    public String getF_name() {
        return f_name;
    }

    public String getE_mail() {
        return e_mail;
    }

    public String getMob_number() {
        return mob_number;
    }

    public String getU_key() {
        return u_key;
    }

    public ClassUserInfo(){

    }

    public ClassUserInfo(String f_name, String e_mail, String mob_number, String pass_word, String u_key){
        this.f_name = f_name;
        this.e_mail = e_mail;
        this.mob_number = mob_number;
        this.pass_word = pass_word;
        this.u_key = u_key;
    }
}

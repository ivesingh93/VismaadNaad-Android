package com.vismaad.naad.rest.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ivesingh on 1/3/18.
 */

public class UserCredentials {

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;


    @SerializedName("account_id")
    private String account_id;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("dob")
    private String dob;

    @SerializedName("gender")
    private String gender;

    @SerializedName("source_of_login")
    private String source_of_login;

    @SerializedName("source_of_account")
    private String source_of_account;

    @SerializedName("account")
    private String account;


    public UserCredentials(String first_name, String last_name,
                           String account_id, String username,
                           String password, String source_of_account) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.account_id = account_id;
        this.username = username;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.source_of_account = source_of_account;
    }

    public UserCredentials(String username, String password, String source_of_account) {
        this.username = username;
        this.password = password;
        this.source_of_account = source_of_account;
    }

    public UserCredentials(String account_id, String source_of_account) {
        this.account_id = account_id;
        this.source_of_account = source_of_account;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getAccountID() {
        return account_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSource_of_login() {
        return source_of_login;
    }

    public void setSource_of_login(String source_of_login) {
        this.source_of_login = source_of_login;
    }

    public String getSource_of_account() {
        return source_of_account;
    }

    public void setSource_of_account(String source_of_account) {
        this.source_of_account = source_of_account;
    }

}

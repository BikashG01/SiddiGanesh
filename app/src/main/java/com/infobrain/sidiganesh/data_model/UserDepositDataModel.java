package com.infobrain.sidiganesh.data_model;

/**
 * Created by frank on 12/3/2017.
 */

public class UserDepositDataModel {

    String account_type, account_number, account_total_amount;

    public UserDepositDataModel(String account_type, String account_number, String account_total_amount) {
        this.account_type = account_type;
        this.account_number = account_number;
        this.account_total_amount = account_total_amount;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getAccount_total_amount() {
        return account_total_amount;
    }

    public void setAccount_total_amount(String account_total_amount) {
        this.account_total_amount = account_total_amount;
    }
}

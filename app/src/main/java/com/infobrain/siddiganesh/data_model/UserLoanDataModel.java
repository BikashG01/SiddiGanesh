package com.infobrain.siddiganesh.data_model;

/**
 * Created by frank on 12/3/2017.
 */

public class UserLoanDataModel {

    String loan_account_type, loan_account_number, loan_account_total_amount_principal,loan_interest_amount,loan_fine_amount;

    public UserLoanDataModel(String loan_account_type, String loan_account_number, String loan_account_total_amount_principal, String loan_interest_amount, String loan_fine_amount) {
        this.loan_account_type = loan_account_type;
        this.loan_account_number = loan_account_number;
        this.loan_account_total_amount_principal = loan_account_total_amount_principal;
        this.loan_interest_amount = loan_interest_amount;
        this.loan_fine_amount = loan_fine_amount;
    }

    public String getLoan_account_type() {
        return loan_account_type;
    }

    public void setLoan_account_type(String loan_account_type) {
        this.loan_account_type = loan_account_type;
    }

    public String getLoan_account_number() {
        return loan_account_number;
    }

    public void setLoan_account_number(String loan_account_number) {
        this.loan_account_number = loan_account_number;
    }

    public String getLoan_account_total_amount_principal() {
        return loan_account_total_amount_principal;
    }

    public void setLoan_account_total_amount_principal(String loan_account_total_amount_principal) {
        this.loan_account_total_amount_principal = loan_account_total_amount_principal;
    }

    public String getLoan_interest_amount() {
        return loan_interest_amount;
    }

    public void setLoan_interest_amount(String loan_interest_amount) {
        this.loan_interest_amount = loan_interest_amount;
    }

    public String getLoan_fine_amount() {
        return loan_fine_amount;
    }

    public void setLoan_fine_amount(String loan_fine_amount) {
        this.loan_fine_amount = loan_fine_amount;
    }
}

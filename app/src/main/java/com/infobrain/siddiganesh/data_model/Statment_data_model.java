package com.infobrain.siddiganesh.data_model;

/**
 * Created by bikas on 12/4/2017.
 */

public class Statment_data_model {
    private String statement_date;
    private String statement_blc;
    private String statement_cramt;
    private String statement_dramt;
    private String statement_reference;
    private String statement_trans_blc;

    public Statment_data_model(String statement_date, String statement_blc, String statement_cramt, String statement_dramt, String statement_reference, String statement_trans_blc) {
        this.statement_date = statement_date;
        this.statement_blc = statement_blc;
        this.statement_cramt = statement_cramt;
        this.statement_dramt = statement_dramt;
        this.statement_reference = statement_reference;
        this.statement_trans_blc = statement_trans_blc;
    }

    public String getStatement_date() {
        return statement_date;
    }

    public String getStatement_blc() {
        return statement_blc;
    }

    public String getStatement_cramt() {
        return statement_cramt;
    }

    public String getStatement_dramt() {
        return statement_dramt;
    }

    public String getStatement_reference() {
        return statement_reference;
    }

    public String getStatement_trans_blc() {
        return statement_trans_blc;
    }
}

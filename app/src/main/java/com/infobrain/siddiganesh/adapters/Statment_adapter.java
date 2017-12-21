package com.infobrain.siddiganesh.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.infobrain.siddiganesh.R;
import com.infobrain.siddiganesh.data_model.Statment_data_model;

import java.util.List;

/**
 * Created by bikas on 12/4/2017.
 */

public class Statment_adapter extends BaseAdapter {
    List<Statment_data_model> Statment_data_models;
    Context context;

    public Statment_adapter(List<Statment_data_model> Statment_data_models, Context context) {
        this.Statment_data_models = Statment_data_models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return Statment_data_models.size();
    }

    @Override
    public Object getItem(int i) {
        return Statment_data_models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return Statment_data_models.indexOf(getItem(i));
    }

    public class StatementHolder{
        TextView stat_date,stat_ref,stat_blc,stat_tran_blc,stat_status;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        StatementHolder statementHolder=null;
        LayoutInflater inflater= (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(view==null){
            view=inflater.inflate(R.layout.custom_statement,null);
            statementHolder= new StatementHolder();
            statementHolder.stat_date=(TextView)view.findViewById(R.id.statement_date);
            statementHolder.stat_tran_blc=(TextView)view.findViewById(R.id.statement_blc);
            statementHolder.stat_ref=(TextView)view.findViewById(R.id.statement_ref);
            statementHolder.stat_blc=(TextView)view.findViewById(R.id.balance_total);
            statementHolder.stat_status=(TextView)view.findViewById(R.id.statement_status);
            view.setTag(statementHolder);
        }
        else{
            statementHolder=(StatementHolder) view.getTag();
        }
        Statment_data_model data_model= (Statment_data_model) getItem(i);
        statementHolder.stat_date.setText(data_model.getStatement_date());
        statementHolder.stat_tran_blc.setText(data_model.getStatement_trans_blc());
        //statementHolder.stat_status.setText(data_model.getStatement_cramt());
        statementHolder.stat_ref.setText(data_model.getStatement_reference());
        statementHolder.stat_blc.setText(data_model.getStatement_blc());
        return view;
    }
}

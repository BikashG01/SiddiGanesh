package com.infobrain.sidiganesh.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.infobrain.sidiganesh.R;
import com.infobrain.sidiganesh.data_model.UserLoanDataModel;

import java.util.List;

/**
 * Created by frank on 12/3/2017.
 */

public class UserLoanAdapter extends BaseAdapter {
    List<UserLoanDataModel> userLoanDataModels;
    Context context;

    public UserLoanAdapter(List<UserLoanDataModel> userLoanDataModels, Context context) {
        this.userLoanDataModels = userLoanDataModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userLoanDataModels.size();
    }

    @Override
    public Object getItem(int i) {
        return userLoanDataModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return userLoanDataModels.indexOf(getItem(i));
    }

    public class LoanViewHolder{
        TextView loan_account_type;
        TextView loan_account_number;
        TextView loan_account_total_amount_principal;
        TextView loan_interest_amount;
        TextView loan_fine_amount;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LoanViewHolder mViewholder=null;
        LayoutInflater minflator=(LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view==null){
            view= minflator.inflate(R.layout.loan_list_holder,null);
            mViewholder= new LoanViewHolder();
            mViewholder.loan_account_type=(TextView)view.findViewById(R.id.loan_account_type);
            mViewholder.loan_account_number=(TextView)view.findViewById(R.id.loan_account_number);
            mViewholder.loan_account_total_amount_principal=(TextView)view.findViewById(R.id.loan_account_balance);
            mViewholder.loan_interest_amount=(TextView)view.findViewById(R.id.loan_interest_amount);
            mViewholder.loan_fine_amount=(TextView)view.findViewById(R.id.fine_amount);

            view.setTag(mViewholder);
        }
        else{
            mViewholder=(LoanViewHolder)view.getTag();
        }
        UserLoanDataModel userLoanDataModel= (UserLoanDataModel) getItem(i);
        mViewholder.loan_account_type.setText(userLoanDataModel.getLoan_account_type());
        mViewholder.loan_account_number.setText(userLoanDataModel.getLoan_account_number());
        mViewholder.loan_account_total_amount_principal.setText(userLoanDataModel.getLoan_account_total_amount_principal());
        mViewholder.loan_interest_amount.setText(userLoanDataModel.getLoan_interest_amount());
        mViewholder.loan_fine_amount.setText(userLoanDataModel.getLoan_fine_amount());
        return view;
    }
}

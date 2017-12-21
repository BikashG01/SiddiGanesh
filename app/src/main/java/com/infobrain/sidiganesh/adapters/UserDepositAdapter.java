package com.infobrain.sidiganesh.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.infobrain.sidiganesh.R;
import com.infobrain.sidiganesh.data_model.UserDepositDataModel;

import java.util.List;

/**
 * Created by frank on 12/3/2017.
 */

public class UserDepositAdapter extends BaseAdapter {
    List<UserDepositDataModel> userDepositDataModels;
    Context context;

    public UserDepositAdapter(List<UserDepositDataModel> userDepositDataModels, Context context) {
        this.userDepositDataModels = userDepositDataModels;
        this.context = context;
    }

    @Override
    public int getCount() {
        return userDepositDataModels.size();
    }

    @Override
    public Object getItem(int i) {
        return userDepositDataModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return userDepositDataModels.indexOf(getItem(i));
    }

    public class DepositViewHolder {
        TextView account_type;
        TextView account_number;
        TextView account_total_amount;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        DepositViewHolder mViewholder = null;
        LayoutInflater minflator = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = minflator.inflate(R.layout.deposit_list_holder, null);
            mViewholder = new DepositViewHolder();
            mViewholder.account_type = (TextView) view.findViewById(R.id.deposit_account_type);
            mViewholder.account_number = (TextView) view.findViewById(R.id.deposit_account_number);
            mViewholder.account_total_amount = (TextView) view.findViewById(R.id.deposit_account_balance_total);
            view.setTag(mViewholder);
        } else {
            mViewholder = (DepositViewHolder) view.getTag();
        }
        UserDepositDataModel userDepositDataModel = (UserDepositDataModel) getItem(i);
        mViewholder.account_type.setText(userDepositDataModel.getAccount_type());
        mViewholder.account_number.setText(userDepositDataModel.getAccount_number());
        mViewholder.account_total_amount.setText(userDepositDataModel.getAccount_total_amount());
        return view;
    }
}

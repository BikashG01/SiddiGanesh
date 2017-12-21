package com.infobrain.sidiganesh.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infobrain.sidiganesh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by bikas on 12/3/2017.
 */

public class Account_info_frag extends Fragment {
    private SharedPreferences preferences;
    private TextView product_name, opening_date, duration, matured_date, principal_blc, interest_rate, interest_type, acc_branch, status, min_blc, installment_type, limit_amt, fine_penalty, inerest_blc;
    private String user_number;
    private String user_mpin;
    private String acc_no, account_type;
    String exact_no;
    private String list_value, acc_no_only, account_name;
    private String user_acc_no;
    private Spinner acc_no_spin;
    List<String> items = new ArrayList<>();
    private List<String> account_no_list;
    private List<String> account_no_lists;
    private ProgressDialog progressDialog;
    private String PRODUCTNAME, DURATION, OPENING_DATE, MATURED_DATE, INT_RATE, MIN_BAL, STATUS, ACC_BRANCH, PRINICPAL_BAL, INT_TYPE, FINE_PENALTY, LIMIT_AMT, INT_BAL,INT_SCHEME;
    private ArrayList<String> acc_list = new ArrayList<String>();
    private String[] mStringArray;
    private String rs = "Rs. ";
    private String[] part2;
    private ArrayAdapter<CharSequence> arrayAdapter;
    private LinearLayout fine_and_penalty_layout, interest_type_layout, interest_blc_layout, min_blc_layout, limit_amount_layout, installment_type_layout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View some = inflater.inflate(R.layout.account_frag, container, false);
        getActivity().setTitle("Account Info");
        return some;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        account_no_lists = new ArrayList<>();
        acc_no_spin = (Spinner) view.findViewById(R.id.account_no_spin);
        fine_and_penalty_layout = (LinearLayout) view.findViewById(R.id.fine_and_penalty_layout);

        interest_type_layout = (LinearLayout) view.findViewById(R.id.interest_type_layout);
        interest_blc_layout = (LinearLayout) view.findViewById(R.id.interest_blc_layout);
        installment_type_layout = (LinearLayout) view.findViewById(R.id.installment_type_layout);
        min_blc_layout = (LinearLayout) view.findViewById(R.id.min_blc_layout);
        limit_amount_layout = (LinearLayout) view.findViewById(R.id.limit_amount_layout);






        acc_no_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                acc_no = acc_no_spin.getItemAtPosition(i).toString();
                String sub_acc_no = "--";
                String[] parts = acc_no.split(sub_acc_no);
                exact_no = parts[0];
                account_type = parts[1];
                layoutControl(account_type);
                load_account(exact_no);

                Toast.makeText(getContext(), exact_no, Toast.LENGTH_SHORT).show();

                ((TextView) acc_no_spin.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        preferences = this.getActivity().getSharedPreferences("LOGIN", 0);
        product_name = (TextView) view.findViewById(R.id.product_name);
        opening_date = (TextView) view.findViewById(R.id.opening_date);
        duration = (TextView) view.findViewById(R.id.duration);
        matured_date = (TextView) view.findViewById(R.id.matured_date);
        principal_blc = (TextView) view.findViewById(R.id.principal_blc);
        interest_rate = (TextView) view.findViewById(R.id.interest_rate);
        inerest_blc = (TextView) view.findViewById(R.id.interest_blc);
        interest_type = (TextView) view.findViewById(R.id.interest_type);
        acc_branch = (TextView) view.findViewById(R.id.account_branch);
        status = (TextView) view.findViewById(R.id.status);
        min_blc = (TextView) view.findViewById(R.id.min_blc);
        installment_type = (TextView) view.findViewById(R.id.installment_type);
        limit_amt = (TextView) view.findViewById(R.id.limit_amount);

        fine_penalty = (TextView) view.findViewById(R.id.fine_and_penalty);


        user_number = preferences.getString("user_number", "");
        user_mpin = preferences.getString("user_mpin", "");
        list_value = preferences.getString("list_value", "");
        acc_no_only = preferences.getString("acc_no_only", "");


        String str = list_value.replaceAll("\\[|\\]", "");
        String acc = acc_no_only.replaceAll("\\[|\\]", "");
        items = Arrays.asList(str.split("\\s*,\\s*"));
        account_no_list = Arrays.asList(acc.split("\\s*,\\s*"));
        mStringArray = new String[account_no_list.size()];
        mStringArray = account_no_list.toArray(mStringArray);

        spinner_load();
        load_account(exact_no);
    }

    public void spinner_load() {
        final String login_url = "http://inet.siddiganesh.com.np/services/webservice.asmx/Validate_UserLogin?Mobile_No=" + user_number + "&MPin=" + user_mpin;
        final StringRequest stringRequest2 = new StringRequest(Request.Method.GET, login_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array1 = jsonObject.getJSONArray("Table2");
                    JSONObject contain = array1.getJSONObject(0);

                    for (int i = 0; i < array1.length(); i++) {
                        JSONObject object = array1.getJSONObject(i);
                        String acc_nos = object.getString("AC_NO");
                        account_name = object.getString("AC_TYPE");
                        account_no_lists.add(acc_nos + "--" + account_name);
                        //account_no_lists.add(acc_nos);

                    }
                    acc_no_spin.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, account_no_lists));


                } catch (JSONException e) {
                    Log.e("ERROR:", e.getMessage());
                    progressDialog.dismiss();


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest2);


    }


    public void load_account(String accNo) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.show();

        final String ACC_URL = "http://inet.siddiganesh.com.np/services/webservice.asmx/Get_Accounts_Info?Mobile_No=" + user_number + "&" + "MPin=" + user_mpin + "&Acc_No=" + accNo;
        Toast.makeText(getContext(), ACC_URL, Toast.LENGTH_SHORT).show();
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, ACC_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response2) {
                try {

                    JSONObject jsonObject = new JSONObject(response2);
                    JSONArray array = jsonObject.getJSONArray("Table1");
                    JSONObject contain = array.getJSONObject(0);
                    PRODUCTNAME = contain.getString("PRODUCT_NAME");
                    Log.e("PRODUCT NAME", PRODUCTNAME);
                    DURATION = contain.getString("PERIOD");
                    Log.e("DURATION", DURATION);
                    OPENING_DATE = contain.getString("OPEN_DATE");
                    INT_RATE = contain.getString("INT_RATE");
                    String split = "T";
                    String[] part1 = OPENING_DATE.split(split);
                    INT_TYPE = contain.getString("INST_TYPE");
                    LIMIT_AMT = contain.getString("LIMIT_AMT");

                    Log.e("OPENING DATE", OPENING_DATE);
                    MATURED_DATE = contain.getString("EXPIRY_DATE");
                    part2 = MATURED_DATE.split(split);
                    Log.e("MATURED DATE",part2[0].toString());
                    //String[] part2 = MATURED_DATE.split(split);
                    Log.e("EXPIRY DATE", MATURED_DATE);
                    MIN_BAL = contain.getString("MIN_BAL");
                    Log.e("MIN_BAL", MIN_BAL);
                    STATUS = contain.getString("AC_STATUS");
                    Log.e("AC_STATUS", STATUS);
                    ACC_BRANCH = contain.getString("BRANCH");
                    PRINICPAL_BAL = contain.getString("Prn_Bal");
                    FINE_PENALTY = contain.getString("Fine");
                    INT_BAL = contain.getString("Int_Bal");
                    INT_SCHEME=contain.getString("INT_SCHEME");


                    product_name.setText(PRODUCTNAME);
                    opening_date.setText(part1[0]);
                    duration.setText(DURATION);
                    matured_date.setText(part2[0]);
                    principal_blc.setText(rs + PRINICPAL_BAL);
                    interest_rate.setText(INT_RATE);
                    interest_type.setText(INT_SCHEME);
                    installment_type.setText(INT_TYPE);
                    min_blc.setText(rs + MIN_BAL);
                    limit_amt.setText(rs + LIMIT_AMT);
                    status.setText(STATUS);
                    inerest_blc.setText(rs + INT_BAL);
                    fine_penalty.setText(rs + FINE_PENALTY);
                    acc_branch.setText(ACC_BRANCH);
                    check();
                    progressDialog.dismiss();


                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage());
                    progressDialog.dismiss();


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);


    }

    public void check() {
        if (DURATION.isEmpty() || DURATION.equals("0")) {
            matured_date.setText("N/A");
            duration.setText("N/A");
        } else {
            matured_date.setText(part2[0]);
            duration.setText(DURATION);
        }
        if (INT_TYPE.isEmpty() || INT_TYPE.equals("")) {
            interest_type_layout.setVisibility(View.GONE);
            installment_type_layout.setVisibility(View.GONE);

        } else {
            interest_type.setText(INT_SCHEME);
            interest_type_layout.setVisibility(View.VISIBLE);
            installment_type_layout.setVisibility(View.VISIBLE);
        }
        /*if (MIN_BAL.isEmpty() || MIN_BAL.equals(null)) {
            min_blc.setVisibility(View.GONE);
        } else {
            min_blc.setText(MIN_BAL);
        }
        if (INT_TYPE.isEmpty() || INT_TYPE.equals("0.00")) {
            interest_type_layout.setVisibility(View.GONE);
            interest_blc_layout.setVisibility(View.GONE);
        } else {
            interest_type.setText(INT_TYPE);
            interest_type_layout.setVisibility(View.VISIBLE);
            interest_blc_layout.setVisibility(View.VISIBLE);
        }
        if (FINE_PENALTY.isEmpty() || FINE_PENALTY.equals(0.00)) {
            fine_and_penalty_layout.setVisibility(View.GONE);
        } else {
            fine_penalty.setText(FINE_PENALTY);
            fine_and_penalty_layout.setVisibility(View.VISIBLE);

        }*/


    }

    public void layoutControl(String account_type) {
        if (account_type.equals("DEPOSIT")) {
            Log.e("ACCOUNT TYPE", account_type);
            fine_and_penalty_layout.setVisibility(View.GONE);
            min_blc_layout.setVisibility(View.VISIBLE);
            interest_type_layout.setVisibility(View.GONE);
            interest_blc_layout.setVisibility(View.GONE);
            limit_amount_layout.setVisibility(View.GONE);


        } else {
            Log.e("ACCOUNT TYPE", account_type);
            fine_and_penalty_layout.setVisibility(View.VISIBLE);
            min_blc_layout.setVisibility(View.GONE);
            interest_type_layout.setVisibility(View.VISIBLE);
            interest_blc_layout.setVisibility(View.VISIBLE);
            limit_amount_layout.setVisibility(View.VISIBLE);


        }

    }
}

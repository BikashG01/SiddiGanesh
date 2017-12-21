package com.infobrain.sidiganesh.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infobrain.sidiganesh.R;
import com.infobrain.sidiganesh.adapters.Statment_adapter;
import com.infobrain.sidiganesh.data_model.Statment_data_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Account_Statements_frag extends Fragment {
    private Button show_statement;
    private LinearLayout linearLayout_stat_list;
    private Spinner statment_type_spin;
    private String statement_value;
    private ListView statement_list_view;
    private String date[] = {"2017/09/07", "2017/09/07", "2017/09/07", "2017/09/07"};
    private String ref[] = {"367348", "893403403", "82939", "8938493"};
    private String trans_blc[] = {"100", "100", "100", "100"};
    private String total_blc[] = {"2000", "2000", "900", "4894"};
    private String status[] = {"withdraw", "deposit", "withdraw", "deposit"};
    private ArrayList statment_value_list = new ArrayList();
    private String SHOWS_URL;
    private List<Statment_data_model> statment_data_models = new ArrayList<>();
    private Statment_adapter statment_adapters;
    private int year, month, day;
    private int fyear, fmonth, fday;
    private Calendar calendar1, calendar2;
    private String fromDate, toDate,olddate;
    private TextView from_date, to_date;
    private SimpleDateFormat sdf;

    private ArrayAdapter<CharSequence> statement_adapater;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        statment_type_spin = (Spinner) view.findViewById(R.id.statement_type);
        statement_list_view = (ListView) view.findViewById(R.id.list_statement);
        show_statement = (Button) view.findViewById(R.id.show_statement_btn);
        linearLayout_stat_list = (LinearLayout) view.findViewById(R.id.list_layout);
        from_date = (TextView) view.findViewById(R.id.frm_date);
        to_date = (TextView) view.findViewById(R.id.to_date);
        statment_adapters = new Statment_adapter(statment_data_models, getContext());
        //statement_list_view.setAdapter(new Statment_adapter(statment_value_list, getContext()));
        statement_adapater = ArrayAdapter.createFromResource(getContext(), R.array.statement, android.R.layout.simple_spinner_item);
        statement_adapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statment_type_spin.setAdapter(statement_adapater);
        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();

            }
        });
        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateFrom();
            }
        });
        show_statement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout_stat_list.setVisibility(View.VISIBLE);
                statment_data_models.clear();
                load_statements();
                //statement_list_view = (ListView) view.findViewById(R.id.list_statement);
                statment_adapters = new Statment_adapter(statment_data_models, getContext());
                statement_list_view.setAdapter(statment_adapters);
                statment_adapters.notifyDataSetChanged();

            }
        });

        statment_type_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statement_value = statment_type_spin.getSelectedItem().toString();
                ((TextView) statment_type_spin.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Date currentDate= new Date();

        calendar1 = Calendar.getInstance();
        year = calendar1.get(Calendar.YEAR);
        month = calendar1.get(Calendar.MONTH);
        day = calendar1.get(Calendar.DAY_OF_MONTH);
        toDate = year + "-" + new StringBuilder().append(month+1) + "-" + day;
        to_date.setText(toDate);
        Log.e("AJA KO DATE", toDate);
        calendar2=Calendar.getInstance();
        fyear = calendar1.get(Calendar.YEAR);


        calendar2 = Calendar.getInstance();
        calendar2.setTime(currentDate);
        fyear = calendar2.get(Calendar.YEAR);
        fmonth = calendar2.get(Calendar.MONTH);
        calendar2.add(Calendar.DAY_OF_MONTH,-35);
        Date addDate=calendar2.getTime();
        //olddate = fyear + "-" + new StringBuilder().append(fmonth+1) + "-" + fday;
        from_date.setText(dateFormat.format(addDate));
        //Log.e("ADD DATE", addDate);
    }

    public void showDateFrom() {
        DialogFragment dialogFragment = new SelectDateFragment();
        dialogFragment.show(getActivity().getFragmentManager(), "Khai");
    }


    @SuppressLint("ValidFragment")
    public class SelectDateFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMinDate(calendar2.getTimeInMillis());
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            fyear = yy;
            fmonth = mm;
            fday = dd;
            toDate = fyear + "-" + new StringBuilder().append(fmonth+1) + "-" + fday;
            from_date.setText(toDate);
            /*from_date.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));*/
        }


    }



    public void showDatePickerDialog() {
        DialogFragment dialogFragment = new SelectDateFragments();
        dialogFragment.show(getActivity().getFragmentManager(), "Khai");
    }


    @SuppressLint("ValidFragment")
    public class SelectDateFragments extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //dialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            year = yy;
            month = mm;
            day = dd;
            fromDate = year + "-" + new StringBuilder().append(month+1) + "-" + day;
            to_date.setText(fromDate);
            /*from_date.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));*/
        }


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View some = inflater.inflate(R.layout.fragment_account__statements_frag, container, false);
        getActivity().setTitle("My Statements");
        return some;
    }

    public void load_statements() {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        SHOWS_URL = "http://inet.siddiganesh.com.np/services/webservice.asmx/Get_Accounts_Statement?Mobile_No=9856022323&MPin=1234&Acc_No=0037NS&Date1=2017-11-06&Date2=2017-12-05&Statement_Type=PRN";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SHOWS_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("Table1");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject contain = array.getJSONObject(i);
                        Statment_data_model stat_dataModel = new Statment_data_model(
                                contain.getString("VCH_NDATE"),
                                contain.getString("Balance"),
                                contain.getString("DrAmt"),
                                contain.getString("CrAmt"),
                                contain.getString("AC_DESC"),
                                contain.getString("Balance")
                        );
                        statment_data_models.add(stat_dataModel);
                        //Toast.makeText(MainActivity.this, String.valueOf(model.add(data_data)), Toast.LENGTH_SHORT).show();
                    }
                    if (statment_data_models != null) {
                        progressDialog.dismiss();

                    }

                } catch (JSONException e) {
                    Log.e("ERROR:", e.getMessage());
                    progressDialog.dismiss();
                }
                statment_adapters.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(BusActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }

        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(90000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}

package com.infobrain.siddiganesh.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infobrain.siddiganesh.R;
import com.infobrain.siddiganesh.adapters.Statment_adapter;
import com.infobrain.siddiganesh.data_model.Statment_data_model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class Account_Statements_frag extends Fragment {
    private Button show_statement;
    private LinearLayout linearLayout_stat_list;
    private Spinner statment_type_spin,account_type_spin;
    private String statement_value,acc_no;
    private ListView statement_list_view;
    private ArrayList statment_value_list = new ArrayList();
    private String SHOWS_URL;
    private List<Statment_data_model> statment_data_models = new ArrayList<>();
    private Statment_adapter statment_adapters;
    private int year, month, day;
    private int fyear, fmonth, fday;
    private Calendar calendar1, calendar2;
    private String fromDate, toDate = "0";
    private TextView from_date, to_date;
    private List<String> account_no_lists;
    private String account_name;
    private SimpleDateFormat sdf;
    private String user_number;
    private String user_mpin;
    private SharedPreferences preferences;

    private ArrayAdapter<CharSequence> statement_adapater;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        statment_type_spin = (Spinner) view.findViewById(R.id.statement_type);
        account_type_spin=(Spinner)view.findViewById(R.id.account_spin);
        account_no_lists = new ArrayList<>();
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

        preferences = getActivity().getSharedPreferences("LOGIN", 0);
        user_number = preferences.getString("user_number", "");
        user_mpin = preferences.getString("user_mpin", "");
        spinner_load();


        Date currentDate = new Date();

        calendar1 = Calendar.getInstance();
        year = calendar1.get(Calendar.YEAR);
        month = calendar1.get(Calendar.MONTH);
        day = calendar1.get(Calendar.DAY_OF_MONTH);
        toDate = year + "-" + new StringBuilder().append(month + 1) + "-" + day;
        to_date.setText(toDate);
        Log.e("AJA KO DATE", toDate);
        calendar2 = Calendar.getInstance();
        fyear = calendar1.get(Calendar.YEAR);


        //calendar2 = Calendar.getInstance();
        calendar2.setTime(currentDate);
        //fyear = calendar2.get(Calendar.YEAR);
        //fmonth = calendar2.get(Calendar.MONTH);
        //fday=calendar2.get(Calendar.DAY_OF_MONTH);
        calendar2.add(Calendar.DAY_OF_MONTH, -35);
        Date addDate = calendar2.getTime();

        //olddate = fyear + "-" + new StringBuilder().append(fmonth+1) + "-" + fday;
        fromDate = dateFormat.format(addDate);
        String split = "-";
        String[] parts = fromDate.split(split);
        fyear = Integer.parseInt(parts[0]);
        fmonth = Integer.parseInt(parts[1]) - 1;
        fday = Integer.parseInt(parts[2]);
        from_date.setText(dateFormat.format(addDate));
        Log.e("SELECT NAHUDA KO DATE", fromDate);


        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToDateDatePicker();

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
                int dateDifference = (int) getDateDiff(new SimpleDateFormat("yyyy-MM-dd"), fromDate, toDate);
                Math.abs(dateDifference);
                Log.e("DATE DIFFERENCE", Integer.toString(dateDifference));
                Log.e("FROMDATE", fromDate);
                Log.e("TODATE", toDate);
                //System.out.println("dateDifference: " + dateDifference);

                if (dateDifference > 35) {
                    Toast.makeText(getContext(), "Please select less than 35 days", Toast.LENGTH_SHORT).show();

                } else {
                    linearLayout_stat_list.setVisibility(View.VISIBLE);
                    statment_data_models.clear();
                    load_statements();
                    //statement_list_view = (ListView) view.findViewById(R.id.list_statement);
                    statment_adapters = new Statment_adapter(statment_data_models, getContext());
                    statement_list_view.setAdapter(statment_adapters);
                    statment_adapters.notifyDataSetChanged();
                }


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
        account_type_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                acc_no = account_type_spin.getItemAtPosition(i).toString();

                Toast.makeText(getContext(), acc_no, Toast.LENGTH_SHORT).show();

                ((TextView) account_type_spin.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

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
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, fyear, fmonth, fday);
            //dialog.getDatePicker().setMinDate(calendar2.getTimeInMillis());
            //dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            fyear = yy;
            fmonth = mm;
            fday = dd;
            fromDate = fyear + "-" + new StringBuilder().append(fmonth + 1) + "-" + fday;
            Log.e("FROM SELECT GARDA", fromDate);
            from_date.setText(fromDate);
            /*from_date.setText(new StringBuilder().append(month + 1)
                    .append("-").append(day).append("-").append(year)
                    .append(" "));*/
        }


    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {

        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Difference Date", e.getMessage());
            return 0;
        }
    }


    public void ToDateDatePicker() {
        DialogFragment dialogFragments = new SelectDateFragments();
        dialogFragments.show(getActivity().getFragmentManager(), "Khai");
    }


    @SuppressLint("ValidFragment")
    public class SelectDateFragments extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog dialogs = new DatePickerDialog(getActivity(), this, year, month, day);
            //dialog.getDatePicker().setMinDate(calendar1.getTimeInMillis());
            //dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialogs;
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            year = yy;
            month = mm;
            day = dd;
            toDate = year + "-" + new StringBuilder().append(month + 1) + "-" + day;
            Log.e("TO SELECT GARDA", toDate);
            to_date.setText(toDate);
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
                        account_no_lists.add(acc_nos);
                        //account_no_lists.add(acc_nos);

                    }
                    account_type_spin.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, account_no_lists));


                } catch (JSONException e) {
                    Log.e("ERROR:", e.getMessage());


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
}

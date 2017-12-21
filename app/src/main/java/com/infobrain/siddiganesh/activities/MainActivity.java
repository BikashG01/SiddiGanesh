package com.infobrain.siddiganesh.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infobrain.siddiganesh.Networks_files.Network_connection;
import com.infobrain.siddiganesh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences,preferences, checkpref;
    private Button login_btn;
    private ProgressDialog progressDialog;
    private TextView brain_info_call, brain_info_info;
    private EditText user_mobile, mpin;
    private CheckBox checkBox;
    private String user_number;
    private String user_mpin;
    private String acc_nos;
    private String login_url;
    private String account_type;
    private String list_value, only_acc_no;
    private String loaded_number;
    private Boolean checkBoxValue;
    private String state = "1";
    private String boolbool;
    private String push_url;

    private List<String> account_no_list = new ArrayList<>();
    private List<String> account_nos_only = new ArrayList<>();
    //private String login_url = "http://192.168.0.104:81/BloodHub/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        push_url = "http://www.lifespassenger.com/siddhiganesh/fcm_insert.php";
        preferences = getSharedPreferences("LOGIN", 0);
        checkpref = getSharedPreferences("CHECK", 0);
        login_btn = (Button) findViewById(R.id.login_btn);
        user_mobile = (EditText) findViewById(R.id.user_mobile_no);
        mpin = (EditText) findViewById(R.id.user_pass);
        brain_info_call=(TextView)findViewById(R.id.info_phone);
        brain_info_info=(TextView)findViewById(R.id.info_info);
        checkBox = (CheckBox) findViewById(R.id.check_remember);
       /* loaded_number =checkpref.getString("user_number", "");*/
        checkBoxValue = checkpref.getBoolean("CheckBox_Value",false );
        loaded_number=preferences.getString("user_number","");
        /*Log.e("Checkbox",checkBoxValue);
        if (checkBoxValue.equals("1")) {
            loadNumber();
        }else {
            user_mobile.setText("");
        }*/
        loadNumber(checkBoxValue);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                user_number = String.valueOf(user_mobile.getText());
                user_mpin = String.valueOf(mpin.getText());
                login_url = "http://inet.siddiganesh.com.np/services/webservice.asmx/Validate_UserLogin?Mobile_No=" + user_number + "&MPin=" + user_mpin;
                //Toast.makeText(MainActivity.this, login_url, Toast.LENGTH_SHORT).show();
                if (!checkValidation()) {
                    progressDialog.dismiss();


                } else {

                    load_data();
                    push_device_token();
                }
            }


        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funcheckbox(user_number, true);
            }
        });
    }

    public void funcheckbox(String number, boolean check_state) {
        SharedPreferences.Editor editor = checkpref.edit();
        editor.putString("userLogin", number);
        editor.putBoolean("CheckBox_Value", check_state);
        editor.commit();


    }

    public void loadNumber(boolean value) {
        if (checkBox.isChecked()){
            loaded_number = preferences.getString("userLogin", "");
        }
        else{
            user_mobile.setText(loaded_number);
        }

    }

    public void push_device_token() {

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString(getString(R.string.FCM_TOKEN), "");

        final String user_phone = user_number;
        Log.e("This is Token", token);
        Log.e("This is Phone Number", user_phone);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, push_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        })


        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_phone", user_phone);
                params.put("user_fcm_token", token);
                Log.e("Params ", token);
                return params;
            }
        };
        Network_connection.getInstance(MainActivity.this).addToRequestque(stringRequest);
    }

    public void load_data() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, login_url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               /* progressDialog.dismiss();*/
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // JSONArray array2 = jsonObject.getJSONArray("Table1");
                    JSONArray array = jsonObject.getJSONArray("Table");

                    // JSONArray array1 = jsonObject.getJSONArray("Table2");
                    JSONObject contain = array.getJSONObject(0);
                    String code = contain.getString("Response_Code");
                    String message = contain.getString("Message");
                    //JSONObject info=array2.getJSONObject(1);
                    /*for (int i = 0; i < array1.length(); i++) {
                        JSONObject object = array1.getJSONObject(i);
                        acc_nos = object.getString("AC_NO");
                        account_type = object.getString("AC_TYPE");
                        account_no_list.add(acc_nos + "--" + account_type);
                        account_nos_only.add(acc_nos);
                    }*/

                   /* list_value = String.valueOf(account_no_list);
                    only_acc_no = String.valueOf(account_nos_only);*/
                   /* SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user_number", user_number);
                    editor.putString("user_mpin", user_mpin);
                    editor.putString("list_value", list_value);
                    editor.putString("acc_no_only", only_acc_no);
                    editor.commit();*/

                    /*String phone_number=info.getString("MOBILE_NO");
                    String userName=info.getString("FULL_NAME");
                    String userAddress=info.getString("ADDRESS");*/
                    if (code.equals("100")) {
                        Intent intent = new Intent(getApplicationContext(), Siddhi_Nav_Drawer.class);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user_number", user_number);
                        editor.putString("user_mpin", user_mpin);
                        editor.putString("list_value", list_value);
                        editor.putString("acc_no_only", only_acc_no);
                        editor.commit();
                        progressDialog.dismiss();

                        startActivity(intent);
                        finish();
                    } else if (code.equals("103")) {
                        progressDialog.dismiss();

                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    } else if (code.equals("101")) {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    // JSONArray array1=jsonObject.getJSONArray("Table1");

                  /*  for (int i=0;i<array.length();i++){
                        JSONObject contain=array.getJSONObject(i);
                        JSONObject contain1 = array.getJSONObject(i);

                    }*/
                } catch (JSONException e) {
                    Log.e("ERROR:", e.getMessage());
//                    progressDialog.dismiss();


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    public void success_Login() {
        Intent intent = new Intent(getApplicationContext(), Siddhi_Nav_Drawer.class);
        startActivity(intent);
        finish();
    }

    public void savePassword() {
        String user_phone_number;
        String user_mpin;
    }

    public boolean checkValidation() {
        boolean validation_flag = true;
        user_number = String.valueOf(user_mobile.getText());
        user_mpin = String.valueOf(mpin.getText());
        if (user_number.isEmpty()) {
            user_mobile.setError("Please enter your mobile phone number.");
            validation_flag = false;
        } else if (user_number.length() > 10 || user_number.length() < 10) {
            user_mobile.setError("Please enter your valid mobile no.");
            validation_flag = false;
        } else if (user_mpin.isEmpty()) {
            mpin.setError("Please enter your mpin.");
            validation_flag = false;
        } else if (user_mpin.length() > 4 || user_mpin.length() < 4) {
            mpin.setError("Please enter 4 digit mpin. ");
            validation_flag = false;
        }
        return validation_flag;

    }
}

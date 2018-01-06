import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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
    private SharedPreferences sharedPreferences, preferences, checkpref;
    private Button login_btn;
    private ProgressDialog progressDialog;
    private TextView brain_info_call, brain_info_info;
    private EditText user_mobile, mpin;
    private CheckBox checkBox;
    private String user_number;
    private String user_mpin;
    private String login_url;
    private String list_value, only_acc_no;
    private String loaded_number;
    private Boolean checkBoxValue;
    private String state = "1";
    private String push_url;
    private int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        push_url = "..........................";
        preferences = getSharedPreferences("LOGIN", 0);
        checkpref = getSharedPreferences("CHECK", 0);
        login_btn = findViewById(R.id.login_btn);
        user_mobile = findViewById(R.id.user_mobile_no);
        mpin = findViewById(R.id.user_pass);
        brain_info_call = findViewById(R.id.info_phone);
        brain_info_info = findViewById(R.id.info_info);
        checkBox = findViewById(R.id.check_remember);
        checkBoxValue = checkpref.getBoolean("CheckBox_Value", false);
        loaded_number = preferences.getString("user_number", "");

        loadNumber(checkBoxValue);
        brain_info_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9843401316"));

                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.CALL_PHONE)) {
                        startActivity(callIntent);
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{android.Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }
                } else {
                    startActivity(callIntent);
                }


            }
        });
        brain_info_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), infobrainProfile.class);
                startActivity(i);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(true);
                progressDialog.show();
                user_number = String.valueOf(user_mobile.getText());
                user_mpin = String.valueOf(mpin.getText());
                login_url = "................" + user_number + "&MPin=" + user_mpin;
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
        if (checkBox.isChecked()) {
            loaded_number = preferences.getString("userLogin", "");
        } else {
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
                    JSONArray array = jsonObject.getJSONArray("Table");
                    JSONObject contain = array.getJSONObject(0);
                    String code = contain.getString("Response_Code");
                    String message = contain.getString("Message");
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
                } catch (JSONException e) {
                    Log.e("ERROR:", e.getMessage());


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, "No Internet!", Toast.LENGTH_SHORT).show();

            }
        }) {
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

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

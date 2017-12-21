package com.infobrain.siddiganesh.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infobrain.siddiganesh.activities.MainActivity;
import com.infobrain.siddiganesh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bikas on 12/3/2017.
 */

public class Change_password_frag extends Fragment {
    String change_password_url,phone;
    EditText old_pass, new_pass, confirm_pass;
    Button confirm_btn;
    String old_password;
    String new_password;
    String confirm_password;
    SharedPreferences get_phone,preferences;
    ProgressDialog progressDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        preferences = getActivity().getSharedPreferences("LOGIN", 0);
        phone = preferences.getString("user_number", "");
        old_pass = (EditText) view.findViewById(R.id.old_password);
        new_pass = (EditText) view.findViewById(R.id.new_password);
        confirm_pass = (EditText) view.findViewById(R.id.confirm_password);
        confirm_btn = (Button) view.findViewById(R.id.confirm_btn);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (!checkField()) {

                } else {
                    final Intent intent = new Intent(getContext(), MainActivity.class);
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getContext());
                    }
                    builder.setMessage("Update your MPIN?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    progressDialog = new ProgressDialog(getContext());
                                    progressDialog.setMessage("Please Wait...");
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();
                                    //http://inet.siddiganesh.com.np/services/webservice.asmx/Change_MPin?Mobile_No=9851132290&Old_MPin=1234&New_Mpin=2345

                                    change_password_url = "http://inet.siddiganesh.com.np/services/webservice.asmx/Change_MPin?Mobile_No="+phone+"&Old_MPin="+old_password+"&New_Mpin="+new_password;
                                    change_password(change_password_url);
                                    Log.e("CHANGE PASSWORD URL",change_password_url);
                                    startActivity(intent);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View some = inflater.inflate(R.layout.password_change_frag, container, false);
        getActivity().setTitle("Change MPIN");
        return some;
    }

    public boolean checkField() {
        boolean check_flag = true;
        old_password = String.valueOf(old_pass.getText());
        new_password = String.valueOf(new_pass.getText());
        confirm_password = String.valueOf(confirm_pass.getText());
        if (old_password.isEmpty() || new_password.isEmpty() || confirm_password.isEmpty()) {
            old_pass.setError("Required");
            check_flag = false;

        } else if (new_password.isEmpty()) {
            new_pass.setError("Required");
            check_flag = false;

        } else if (confirm_password.isEmpty()) {
            confirm_pass.setError("Required");
            check_flag = false;

        } else if (new_password.length() > 4 || new_password.length() < 4) {
            new_pass.setError("Must be 4 digits");
            check_flag = false;
        } else if (old_password.length() > 4 || old_password.length() < 4) {
            old_pass.setError("Must be 4 digits");
            check_flag = false;
        } else if (!new_password.equals(confirm_password)) {
            confirm_pass.setError("MPIN doesn't matched");
            check_flag = false;
        } else if (old_password.equals(new_password)) {
            new_pass.setError("Don't use old MPIN");
            check_flag = false;
        }
        return check_flag;
    }

    public void change_password(String URL){
        /*final ProgressDialog progressDialog=  new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();*/
        change_password_url = URL;

        final StringRequest stringRequest=new StringRequest(Request.Method.GET,URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
//                progressDialog.dismiss();
                try{
                    JSONObject jsonObject= new JSONObject(response);
                    JSONArray array=jsonObject.getJSONArray("Table");
                    JSONObject contain=array.getJSONObject(0);
                    String code=contain.getString("Response_Code");
                    String message=contain.getString("Message");
                    if (code.equals("100")){
                        Toast.makeText(getContext(), "Password changed successfully", Toast.LENGTH_LONG).show();
                        Intent intent= new Intent(getContext(),MainActivity.class);
                        progressDialog.dismiss();
                        startActivity(intent);

                    }else if (code.equals("101")){
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Invalid Old Pin", Toast.LENGTH_LONG).show();
                    }

                    // JSONArray array1=jsonObject.getJSONArray("Table1");

                  /*  for (int i=0;i<array.length();i++){
                        JSONObject contain=array.getJSONObject(i);
                        JSONObject contain1 = array.getJSONObject(i);

                    }*/
                }catch (JSONException e){
                    Log.e("ERROR:",e.getMessage());
//                    progressDialog.dismiss();


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {

        };
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
}

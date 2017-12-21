package com.infobrain.sidiganesh.fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.infobrain.sidiganesh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bikas on 12/3/2017.
 */

public class Feedback_frag extends Fragment {
    String sub, msg, post_feedback_url;
    EditText subject, message;
    SharedPreferences preferences;
    Button feedback_send_btn;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;
    String userPhoneNo, userMpin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View some = inflater.inflate(R.layout.feedback_frag, container, false);
        getActivity().setTitle("Feedback");
        return some;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subject = (EditText) view.findViewById(R.id.feedback_subject);
        message = view.findViewById(R.id.feedback_message);
        feedback_send_btn = view.findViewById(R.id.send_feedback_btn);
        preferences = getActivity().getSharedPreferences("LOGIN", 0);
        userPhoneNo = preferences.getString("user_number", "");
        userMpin = preferences.getString("user_mpin", "");

        feedback_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkFields()) {

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                    } else {
                        builder = new AlertDialog.Builder(getContext());
                        builder.setIcon(android.R.drawable.ic_dialog_alert);
                    }
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    post_feedback_url = "http://inet.siddiganesh.com.np/services/webservice.asmx/Post_Feedback_Message?Mobile_No=" + userPhoneNo + "&MPin=" + userMpin + "&Subject=" + sub + "&Message=" + msg;
//                    post_feedback_url = "http://inet.siddiganesh.com.np/services/webservice.asmx/Post_Feedback_Message?Mobile_No="+phone+"&MPin="+pin+"&Subject="+sub+"&Message="+msg;
                    Log.e("URL", post_feedback_url);
                    post_feedback(post_feedback_url);
                    //Log.e("Inside call",phone);
                }
            }
        });
    }

    public boolean checkFields() {

        boolean check_flag = true;

        sub = String.valueOf(subject.getText());
        msg = String.valueOf(message.getText());

        if (sub.isEmpty() && msg.isEmpty()) {
            subject.setError("Subject and Message Both Required");
            check_flag = false;

        } else if (sub.isEmpty()) {
            subject.setError("Subject Required");
            check_flag = false;

        } else if (msg.isEmpty()) {
            message.setError("Message Required");
            check_flag = false;
        }
        return check_flag;
    }

    public void post_feedback(String URL) {
        post_feedback_url = URL;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("Table");
                    JSONObject contain = array.getJSONObject(0);
                    String code = contain.getString("Response_Code");
                    if (code.equals("100")) {
                        progressDialog.dismiss();
                        builder.setMessage("Thanks for your feedback").show();
                        message.setText("");
                        subject.setText("");
                        // Toast.makeText(getContext(), "Feedback Sent", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }
}

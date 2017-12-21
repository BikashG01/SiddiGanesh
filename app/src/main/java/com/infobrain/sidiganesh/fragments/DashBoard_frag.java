package com.infobrain.sidiganesh.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.infobrain.sidiganesh.R;
import com.infobrain.sidiganesh.adapters.UserDepositAdapter;
import com.infobrain.sidiganesh.data_model.UserDepositDataModel;
import com.infobrain.sidiganesh.adapters.UserLoanAdapter;
import com.infobrain.sidiganesh.data_model.UserLoanDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class DashBoard_frag extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    ListView deposit;
    ListView loan;
    ImageView user_profile_pic;
    private ProgressDialog progressDialog;
    Bitmap bitmap;
    SharedPreferences sharedPreferences, preferences, userpref;
    String encodedImage;
    static final int REQUEST_CODE_TO_BROWSE_IMAGE = 1;
    public String dashboard_getUserInfo_URL;
    public String dashboard_getDepositList_URL;
    public String dashboard_getLoanList_URL;
    List<UserDepositDataModel> dashboard_deposit_array_list = new ArrayList<>();
    List<UserLoanDataModel> dashboard_loan_array_list = new ArrayList<>();
    TextView user_name, user_phone, user_address;
    String username, userphone, useraddress;
    String userPhoneNo, userMpin;
    SwipeRefreshLayout swiper;
    private LinearLayout linearLayout;

    public UserDepositAdapter depositAdapter;
    public UserLoanAdapter loanAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Please Wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        linearLayout=(LinearLayout)view.findViewById(R.id.main_layout);
        linearLayout.setVisibility(View.INVISIBLE);

        user_name = view.findViewById(R.id.user_name);
        user_phone = view.findViewById(R.id.user_phone);
        user_address = view.findViewById(R.id.user_location);
        preferences = getActivity().getSharedPreferences("LOGIN", 0);
        userpref = getActivity().getSharedPreferences("INFO", 0);
        userPhoneNo = preferences.getString("user_number", "");
        userMpin = preferences.getString("user_mpin", "");
        getUserInfo(userPhoneNo, userMpin);
        getDepositList(userPhoneNo, userMpin);
        getLoanList(userPhoneNo, userMpin);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        deposit = (ListView) view.findViewById(R.id.list_item_deposit);
        depositAdapter = new UserDepositAdapter(dashboard_deposit_array_list, getContext());
        deposit.setAdapter(depositAdapter);
        user_profile_pic = (ImageView) view.findViewById(R.id.ProfileImage);
        loan = view.findViewById(R.id.list_item_loan);
        loanAdapter = new UserLoanAdapter(dashboard_loan_array_list, getContext());
        loan.setAdapter(loanAdapter);
        decodeImage();
        user_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_Image();


            }
        });
        //sharedPreferences();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View some = inflater.inflate(R.layout.fragment_dash_board_frag, container, false);
        getActivity().setTitle("Mobile Passbook");
        return some;
    }


    private void getDepositList(String u_num, String u_pin) {
        dashboard_getDepositList_URL = "http://inet.siddiganesh.com.np/services/webservice.asmx/Validate_UserLogin?Mobile_No=" + u_num + "&MPin=" + u_pin;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, dashboard_getDepositList_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("Table2");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject contain = array.getJSONObject(i);
                        String code = contain.getString("AC_TYPE");
                        if (code.equals("DEPOSIT")) {
                            String p_name = contain.getString("PRODUCT_NAME");
                            String ac_no = contain.getString("AC_NO");
                            String p_bal = contain.getString("Prn_Bal");
                            String prin_bal = p_bal.replaceAll("[^\\d.]", "");

//                            Toast.makeText(getContext(), p_name + " " + ac_no + " " + prin_bal, Toast.LENGTH_SHORT).show();
                            UserDepositDataModel depo_list = new UserDepositDataModel(p_name, ac_no, prin_bal);
                            dashboard_deposit_array_list.add(depo_list);
                            ListUnion.setDynamicHeight(deposit);
                        }
//                        else {
//                            Toast.makeText(getContext(), "Bahira", Toast.LENGTH_SHORT).show();
//                        }

                    }

                } catch (JSONException e) {
                    Log.e("ERROR:", e.getMessage());


                }
                depositAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


    }

    private void getUserInfo(String u_num, String u_pin) {

        dashboard_getUserInfo_URL = "http://inet.siddiganesh.com.np/services/webservice.asmx/Validate_UserLogin?Mobile_No=" + u_num + "&MPin=" + u_pin;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, dashboard_getUserInfo_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("Table1");
                    JSONObject contain = array.getJSONObject(0);

                    username = contain.getString("FULL_NAME");
                    userphone = contain.getString("MOBILE_NO");
                    useraddress = contain.getString("ADDRESS");

                    SharedPreferences.Editor editor = userpref.edit();
                    editor.putString("user_name", username);
                    editor.putString("user_phone", userphone);
                    editor.putString("user_address", useraddress);
                    editor.commit();

//                    Toast.makeText(getContext(), user_name + " " + user_phone + " " + user_address, Toast.LENGTH_SHORT).show();

                    user_name.setText(username);
                    user_phone.setText(userphone);
                    user_address.setText(useraddress);


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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void getLoanList(String u_num, String u_pin) {
        dashboard_getLoanList_URL = "http://inet.siddiganesh.com.np/services/webservice.asmx/Validate_UserLogin?Mobile_No=" + u_num + "&MPin=" + u_pin;

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, dashboard_getLoanList_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("Table2");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject contain = array.getJSONObject(i);
                        String code = contain.getString("AC_TYPE");
                        if (code.equals("LOAN")) {
//                            loan_account_type, loan_account_number, loan_account_total_amount_principal,loan_interest_amount,loan_fine_amount
                            String ln_ac_type = contain.getString("PRODUCT_NAME");
                            String ln_ac_number = contain.getString("AC_NO");
                            String ln_ac_total_amt_principal = contain.getString("Prn_Bal");
                            String ln_ac_total_principal = ln_ac_total_amt_principal.replaceAll("[^\\d.]", "");
                            String ln_int_amount = contain.getString("Int_Bal");
                            String ln_int = ln_int_amount.replaceAll("[^\\d.]", "");
                            String ln_fine_amt = contain.getString("Fine_Amt");
                            String ln_fine = ln_fine_amt.replaceAll("[^\\d.]", "");
//                            Toast.makeText(getContext(), ln_ac_number + " " + ln_ac_total_principal + " " + ln_ac_type
//                                    + " " + ln_int_amount + " " + ln_fine_amt, Toast.LENGTH_SHORT).show();
                            UserLoanDataModel loan_list = new UserLoanDataModel(ln_ac_type, ln_ac_number, ln_ac_total_principal, ln_int, ln_fine);
                            dashboard_loan_array_list.add(loan_list);
                            ListUnion.setDynamicHeight(loan);
                        }
//                        else {
//                            Toast.makeText(getContext(), "Bahira", Toast.LENGTH_SHORT).show();
//                        }
                    }
                    if (dashboard_loan_array_list != null) {
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    Log.e("ERROR:", e.getMessage());
                    progressDialog.dismiss();
                }
                loanAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                linearLayout.setVisibility(View.VISIBLE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

            }
        }) {

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

    }

    @Override
    public void onRefresh() {
        getUserInfo(userPhoneNo, userMpin);
        getDepositList(userPhoneNo, userMpin);
        getLoanList(userPhoneNo, userMpin);

        swiper.setRefreshing(false);
        swiper.destroyDrawingCache();
        swiper.clearAnimation();
    }


    public static class ListUnion {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount()));
//          params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount()-1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();

        }
    }

    public void select_Image() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,
                REQUEST_CODE_TO_BROWSE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            InputStream stream;
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), path);
                user_profile_pic.setImageBitmap(bitmap);
                stream = getActivity().getContentResolver().openInputStream(data.getData());
                // Encoding Image into Base64
                Bitmap realImage = BitmapFactory.decodeStream(stream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] b = baos.toByteArray();
                //Converting Base64 into String to Store in SharedPreferences
                encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                //NOw storing String to SharedPreferences
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("my_image", encodedImage);
                editor.commit();
                Toast.makeText(getContext(), "Profile image changed.", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void decodeImage() {
        encodedImage = sharedPreferences.getString("my_image", "");
        if (!encodedImage.equalsIgnoreCase("")) {
            //Decoding the Image and display in ImageView
            byte[] b = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            user_profile_pic.setImageBitmap(bitmap);
        } else {

        }
    }
}

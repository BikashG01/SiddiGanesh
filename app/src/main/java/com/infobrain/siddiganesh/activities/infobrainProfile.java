package com.infobrain.siddiganesh.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.infobrain.siddiganesh.R;

public class infobrainProfile extends AppCompatActivity implements View.OnClickListener {
    ImageButton facebook;
    ImageButton email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infobrain_profile);
        facebook=(ImageButton)findViewById(R.id.facebook);
        email=(ImageButton)findViewById(R.id.mail);
        facebook.setOnClickListener(this);
        email.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.facebook:
                Intent facebookintent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/apple.fruit"));
                startActivity(facebookintent);
                break;
            case R.id.mail:
                Intent intent = new Intent(Intent.ACTION_SEND, Uri.fromParts(
                        "mailto","abc@gmail.com", null));
                intent.setType("text/html");
               /* intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");*/
                startActivity(Intent.createChooser(intent, "Send Email"));


        }

    }
}

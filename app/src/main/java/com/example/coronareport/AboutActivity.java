package com.example.coronareport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        ImageView closeBtn = findViewById(R.id.about_close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView google = findViewById(R.id.agoogle);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SENDTO);
//                intent.setData(Uri.parse("mail to:"));
//                intent.putExtra(Intent.EXTRA_SUBJECT, "COVID REPORT: <Add Subject Here>");
//                intent.putExtra(Intent.EXTRA_TEXT, "Hey there wtfarooq!\n\n\n\n\nRegards,\n<Insert your name here>");
//                if(intent.resolveActivity(getActivity().getPackageManager()) != null)
//                    startActivity(intent);
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"developer@covidreport.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "COVID REPORT: <Add Subject Here>");
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        ImageView github = findViewById(R.id.agithub);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/wtfarooq/")));
            }
        });

        ImageView instagram = findViewById(R.id.ainstagram);
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/wtfarooq/")));
            }
        });

        ImageView facebook = findViewById(R.id.afacebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/mirzamohdfarooq/")));
            }
        });

    }
}
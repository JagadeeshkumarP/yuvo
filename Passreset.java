package com.example.yuvo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Passreset extends AppCompatActivity {
    public int sp=0;
    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    ImageButton imageButton1;
    ImageButton imageButton2;
    ImageButton imageButton3;
    ImageButton imageButton4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref1 =getSharedPreferences("lang", MODE_PRIVATE);
        String s=pref1.getString("lang", "en");
        if(s.equals("tm"))
        sp=1;
        if(sp==0)
        setContentView(R.layout.activity_passreset);
        else
        setContentView(R.layout.passresettm);


        imageButton1=findViewById(R.id.mail);
        imageButton2=findViewById(R.id.fb);
        imageButton3=findViewById(R.id.call);
        imageButton4=findViewById(R.id.whatsapp);

        inputEmail = (EditText) findViewById(R.id.editTextTextEmailAddress4);

        btnReset = (Button) findViewById(R.id.reset);
        btnBack = (Button) findViewById(R.id.back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.SRC_ATOP);

        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Passreset.this, LoginActivity.class));
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    if(sp==0)
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplication(), "உங்கள் பதிவு செய்யப்பட்ட மின்னஞ்சல் ஐடியை உள்ளிடவும்", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if(sp==0)
                                    Toast.makeText(Passreset.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(Passreset.this, "உங்கள் கடவுச்சொல்லை மீட்டமைக்க நாங்கள் உங்களுக்கு அறிவுறுத்தல்களை அனுப்பியுள்ளோம்!", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    startActivity(new Intent(Passreset.this, LoginActivity.class));
                                } else {
                                    if(sp==0)
                                    Toast.makeText(Passreset.this, "Password Reset failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(Passreset.this, "கடவுச்சொல் மீட்டமைப்பு தோல்வியடைந்தது" + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });

        //Contact Us
        imageButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String TO="jagadeeshkumar.p2019cse@sece.ac.in";
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL,  new String[]{TO});
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                finish();
            }
        });
        imageButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Uri webpage = Uri.parse("https://www.facebook.com/groups/524447212217491/");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);}
        });
        imageButton3.setOnClickListener(new View.OnClickListener(){
            private static final int REQUEST_PHONE_CALL =100 ;

            @Override
            public void onClick(View v){
                if (ContextCompat.checkSelfPermission(Passreset.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Passreset.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    if (ContextCompat.checkSelfPermission(Passreset.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:6383427518"));
                        startActivity(intent);
                    }
                }
                else
                {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:6383427518"));
                    startActivity(intent);
                } }
        });
        imageButton4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Uri webpage = Uri.parse("https://chat.whatsapp.com/FXezQN9sBAv3IBspDUg6Cm");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);}
        });



    }
}
package com.example.yuvo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    public int sp=0;
    private EditText email;
    private EditText password;
    private EditText username;
    private EditText mobile;
    private Button signup,back;
    FirebaseAuth auth;
    private String userID;
    public ProgressBar progressBar;
    FirebaseFirestore fstore;
    //FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        SharedPreferences pref = getSharedPreferences("lang", MODE_PRIVATE);
        String s=pref.getString("lang", "en");
        if(s.equals("tm"))
        {sp=1;
            setContentView(R.layout.activity_signuptm);}

        fstore=FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        signup=findViewById(R.id.signup);
        email = findViewById(R.id.editTextTextEmailAddress4);
        password = findViewById(R.id.editTextTextPassword);
        username = findViewById(R.id.username);
        mobile = findViewById(R.id.mobile);
        back=findViewById(R.id.back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.SRC_ATOP);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this, LoginActivity.class));
            }
        });


                signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String Email =email.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String name= username.getText().toString();
                String Mobile=mobile.getText().toString();

                if (TextUtils.isEmpty(Email)) {
                    progressBar.setVisibility(View.GONE);
                    if(sp==0)
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "மின்னஞ்சல் முகவரியை உள்ளிடவும்!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(Password)) {
                    if(sp==0)
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "கடவுச்சொல்லை உள்ளிடவும்!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(Mobile)) {
                    if(sp==0)
                    Toast.makeText(getApplicationContext(), "Enter mobile number!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "மொபைல் எண்ணை உள்ளிடவும்!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                if (Password.length() < 6) {
                    if(sp==0)
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), "கடவுச்சொல் மிகவும் குறுகியது, குறைந்தபட்சம் 6 எழுத்துகளை உள்ளிடவும்!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                //progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (!task.isSuccessful()) {
                                    if(sp==0)
                                    Toast.makeText(Signup.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(Signup.this, "அங்கீகரிப்பு தோல்வியுற்றது.",
                                                Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    if(sp==0)
                                    Toast.makeText(Signup.this, "REGITERATION SUCCESSFUL" , Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(Signup.this, "பதிவு வெற்றிகரமாக முடிந்தது" , Toast.LENGTH_SHORT).show();


                                    userID = auth.getCurrentUser().getUid();
                                        DocumentReference documentReference = fstore.collection("USERS").document(userID);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("username", name);
                                        user.put("Email", Email);
                                        user.put("Mobile", Mobile);


                                       documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                if(!task.isSuccessful())
                                                {  if(sp==0)
                                                    Toast.makeText(Signup.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                else
                                                    Toast.makeText(Signup.this, "அங்கீகரிப்பு தோல்வியுற்றது.",
                                                            Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });

                                    progressBar.setVisibility(View.GONE);
                                    startActivity(new Intent(Signup.this, Homepage.class));
                                    finish();
                                }
                            }
                        });

            }
        });










    }
}
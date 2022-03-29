package com.example.yuvo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.widget.CheckBox;
//import com.google.android.gms.auth.api.signin.GoogleSignIn;
//import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;


public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private static final int REQUEST_PHONE_CALL = 100;
    ImageButton imageButton1;
    ImageButton imageButton2;
    ImageButton imageButton3;
    ImageButton imageButton4;
    SignInButton signIn;
    public  String sp="0";
    CheckBox rememberusername;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    private EditText emailEditText;
    public boolean flag=false;
    private EditText passwordEditText;
    private Button loginButton,privacy,terms,lang;
    private Button signupButton,btnfgtpass;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences pref = getSharedPreferences("lang", MODE_PRIVATE);
        sp=pref.getString("lang", "en");
        if(sp.equals("tm"))
            setContentView(R.layout.activty_maintm);

        getSupportActionBar().setTitle("      YUVO");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_foreground);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


//variable initialisation

        signIn = findViewById(R.id.sign_in_button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.SRC_ATOP);

        btnfgtpass = findViewById(R.id.fogtpass);
        emailEditText = findViewById(R.id.editTextTextEmailAddress4);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.button5);
        signupButton = findViewById(R.id.signup);
        imageButton1 = findViewById(R.id.mail);
        imageButton2 = findViewById(R.id.fb);
        imageButton3 = findViewById(R.id.call);
        imageButton4 = findViewById(R.id.whatsapp);
        privacy = findViewById(R.id.privacy);
        terms = findViewById(R.id.termsofuse);
        lang = findViewById(R.id.lang);

//google signin
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }

        });

//remuser

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            //Toast.makeText(LoginActivity.this,user.getUid(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, Homepage.class));
        }


        //login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (emailEditText.getText().length() > 0 && passwordEditText.getText().length() >= 6) {

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailEditText.getText().toString(),passwordEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressBar.setVisibility(View.GONE);
                                if(sp.equals("tm"))
                                    Toast.makeText(LoginActivity.this, "உள்நுழைவு வெற்றிகரமாக உள்ளது", Toast.LENGTH_SHORT).show();
                                else
                                Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,Homepage.class);
                                startActivity(intent);

                            }else {progressBar.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this,task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    String toastMessage = "Username or Password are not correct";
                    if(sp.equals("tm"))

                            toastMessage = "பயனர்பெயர் அல்லது கடவுச்சொல் சரியாக இல்லை";
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });



//signup
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,Signup.class);
                startActivity(intent);
            }
        });




//forgot pass
        btnfgtpass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this,Passreset.class);
                startActivity(intent);
            }
        });


//Contact Us
        imageButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String TO="yuvoyuvoyuvo@gmail.com";
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
            @Override
            public void onClick(View v){
                if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
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


//Privacy policy
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,PrivacyActivity.class);
                startActivity(intent);
            }
        });


//TERMS AND CONDITIONS
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, TermsActivity.class);
                startActivity(intent);
            }
        });
//language

        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this,"we are working on it. The feature will be updated soon", Toast.LENGTH_SHORT).show();


                SharedPreferences pref = getSharedPreferences("lang", MODE_PRIVATE);
                String s=pref.getString("lang", "en");
                if(s.equals("en")){
                SharedPreferences sharedpreferences = getSharedPreferences("lang", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("lang","tm");
                editor.commit();}
                else{
                    SharedPreferences sharedpreferences = getSharedPreferences("lang", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("lang","en");
                    editor.commit();
                }
                finish();
                startActivity(getIntent());




            }
        });


    }

    private void setAppLocale(String localeCode){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }
    //method google sign in
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);


            Intent intent = new Intent(LoginActivity.this,Homepage.class);
            startActivity(intent);
            if(sp.equals("tm"))
                Toast.makeText(LoginActivity.this," Guest login Successful", Toast.LENGTH_SHORT).show();
            else
            Toast.makeText(LoginActivity.this,"விருந்தினர் உள்நுழைவு வெற்றிகரமாக உள்ளது", Toast.LENGTH_SHORT).show();

        } catch (ApiException e) {
            Toast.makeText(LoginActivity.this,Integer.toString(e.getStatusCode()), Toast.LENGTH_SHORT).show();

        }
    }
}
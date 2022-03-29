package com.example.yuvo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    public int sp=0;
    public static final String TAG = "TAG";
    EditText profileFullName,profileEmail,profilePhone;
    ImageView profileImageView;
    Button saveBtn,logout,logout1,changepass;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    GoogleSignInOptions gso;

    GoogleSignInClient mGoogleSignInClient;
    StorageReference storageReference;
    ProgressBar progressBar1;
    ProgressBar progressBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref1 =getSharedPreferences("lang", MODE_PRIVATE);
        String s=pref1.getString("lang", "en");
        if(s.equals("tm"))
            sp=1;
        if(sp==0)
        setContentView(R.layout.activity_profile);
        else
            setContentView(R.layout.activity_profiletm);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent data = getIntent();
        final String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("phone");

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();
        TextView text=findViewById(R.id.text);
        TextView text1=findViewById(R.id.text1);
        Button register=findViewById(R.id.register);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar1.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.SRC_ATOP);

        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar2.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.SRC_ATOP);

        profileFullName =(EditText) findViewById(R.id.profileFullName);
        profileEmail = (EditText) findViewById(R.id.profileEmailAddress);
        profilePhone = (EditText) findViewById(R.id.profilePhoneNo);
        profileImageView = findViewById(R.id.profileImageView);
        saveBtn = findViewById(R.id.saveProfileInfo);
        logout=findViewById(R.id.logout);
        logout1=findViewById(R.id.logout1);
        changepass=findViewById(R.id.changepass);

        changepass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Profile.this,Passreset.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();
                GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(Profile.this,gso);
                googleSignInClient.signOut();
                if(sp==0)
                Toast.makeText(Profile.this, "Logout Succesful", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Profile.this, "வெற்றிகரமாக வெளியேறியது", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Profile.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        logout1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FirebaseAuth.getInstance().signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();
                GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(Profile.this,gso);
                googleSignInClient.signOut();
                if(sp==0)
                    Toast.makeText(Profile.this, "Logout Succesful", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Profile.this, "வெற்றிகரமாக வெளியேறியது", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Profile.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Profile.this,Signup.class);
                startActivity(intent);
            }
        });

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);



            try {
                DocumentReference doc;
                doc = fStore.collection("USERS").document(user.getUid());
                doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        String m = task.getResult().getString("Mobile");
                        profilePhone.setText(m);
                        String e = task.getResult().getString("Email");
                        profileEmail.setText(e);
                        String u = task.getResult().getString("username");
                        profileFullName.setText(u);
                    }
                });


                StorageReference profileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");

                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressBar1.setVisibility(View.VISIBLE);
                        //Picasso.get().load(uri).transform(new RoundedCornersTransform()).into(profileImageView);
                        Picasso.get()
                                .load(uri)
                                .transform(new RoundedTransformation(100, 0))
                                .fit()
                                .into(profileImageView);

                        progressBar1.setVisibility(View.GONE);
                    }
                });
            }
            catch (Exception e){
                TextInputLayout textView1=findViewById(R.id.outlinedTextField1);
                textView1.setVisibility(View.INVISIBLE);
                TextInputLayout textView2=findViewById(R.id.outlinedTextField2);
                textView2.setVisibility(View.INVISIBLE);
                TextInputLayout textView3=findViewById(R.id.outlinedTextField3);
                textView3.setVisibility(View.INVISIBLE);
                profileEmail.setVisibility(View.INVISIBLE);
                profilePhone.setVisibility(View.INVISIBLE);
                profileFullName.setVisibility(View.INVISIBLE);
                profileImageView.setVisibility(View.INVISIBLE);
                saveBtn.setVisibility(View.INVISIBLE);
                changepass.setVisibility(View.INVISIBLE);
                text.setVisibility(View.VISIBLE);
                text1.setVisibility(View.VISIBLE);
                register.setVisibility(View.INVISIBLE);
                logout1.setVisibility(View.VISIBLE);

            }


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {progressBar2.setVisibility(View.VISIBLE);
                if(profileFullName.getText().toString().isEmpty() || profilePhone.getText().toString().isEmpty()||profileEmail.getText().toString().isEmpty()  ){
                    if(sp==0)
                    Toast.makeText(Profile.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(Profile.this, "ஒன்று அல்லது பல துறைகள் காலியாக உள்ளன.", Toast.LENGTH_SHORT).show();
                    progressBar2.setVisibility(View.GONE); return;
                }

                final String email = profileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("USERS").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("Email",email);
                        edited.put("username",profileFullName.getText().toString());
                        edited.put("Mobile",profilePhone.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar2.setVisibility(View.GONE);
                                if(sp==0)
                                Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                else
                                Toast.makeText(Profile.this, "சுயவிவரம் புதுப்பிக்கப்பட்டது", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),Homepage.class));
                                finish();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Profile.this,   e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar2.setVisibility(View.GONE);
                    }
                });


            }
        });

        profileEmail.setText(email);
        profileFullName.setText(fullName);
        profilePhone.setText(phone);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        progressBar1.setVisibility(View.VISIBLE);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();

                //profileImage.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);


            }
        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        // uplaod image to firebase storage

        final StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        progressBar1.setVisibility(View.VISIBLE);
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override

            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) { progressBar1.setVisibility(View.VISIBLE);
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override

                    public void onSuccess(Uri uri) {

                        Picasso.get()
                                .load(uri)
                                .transform(new RoundedTransformation(100, 0))
                                .fit()
                                .into(profileImageView);
                        progressBar1.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });progressBar1.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
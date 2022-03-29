package com.example.yuvo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.yuvo.FertilizerActivity.isConnected;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
public int sp=0;
    private com.example.yuvo.CustomAdapter mAdapter;
    FirebaseUser user;
    FirebaseUser user1;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    StorageReference storageReference;
    StorageReference profileRef;
    SharedPreferences pref;
    private String fromUseridentify;
    private static ArrayList<FriendlyMessage> mFMessages;
    private String currentUser;
    FirebaseAuth fAuth;
    LinearLayoutManager mLinearLayoutManager;
    private RecyclerView mRecyclerView;
    private ImageButton msgBtn,dwn;
    ImageView avatar;
    public TextView offline;
    private EditText msgText;
    private TextInputLayout textInputLayout;
    public ProgressBar progressBar;
    DocumentReference doc;
    FirebaseFirestore fStore;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(sp==0)
        getMenuInflater().inflate(R.menu.my_menu1, menu);
        else
            getMenuInflater().inflate(R.menu.my_menutm1, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        SharedPreferences pref1 = getSharedPreferences("lang", MODE_PRIVATE);
        String s = pref1.getString("lang", "en");
        if (s.equals("tm"))
        {       sp = 1;

        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        textInputLayout=findViewById(R.id.outlinedTextField1);
        if(sp==1)
            textInputLayout.setHint("செய்தி...");
        msgBtn = findViewById(R.id.msgsendbtn);
        dwn = findViewById(R.id.dwn);
        dwn.setVisibility(View.GONE);
        dwn.setOnClickListener(this);
        msgBtn.setOnClickListener(this);
        msgText = findViewById(R.id.msgedittext);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        user = com.example.yuvo.FireHelper.getInstance().AuthInit().getCurrentUser();
        user.getDisplayName();
        user.getEmail();
        fStore = FirebaseFirestore.getInstance();
        progressBar=findViewById(R.id.progress1);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.SRC_ATOP);
        offline=findViewById(R.id.offline);
         fAuth = FirebaseAuth.getInstance();
      user1 = fAuth.getCurrentUser();
        doc = fStore.collection("USERS").document(user1.getUid());
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String subString = task.getResult().getString("username");
                SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor edt = pref.edit();
                edt.putString("username1",subString);
                edt.commit();
            }
        });
        if (!isConnected(ChatActivity.this)) {
            progressBar.setVisibility(View.INVISIBLE);
            offline.setVisibility(View.VISIBLE);
            try{
                if(sp==0)
                Toast.makeText(ChatActivity.this, "You are offline ", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(ChatActivity.this, "இணையம் இல்லை ", Toast.LENGTH_SHORT).show();
            }
            catch(NullPointerException nullPointerException){}

        }
         pref = getPreferences(Context.MODE_PRIVATE);
        currentUser =  pref.getString("username1", "");
        fromUseridentify = user.getUid();
        mFMessages = new ArrayList<FriendlyMessage>();
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");
        mHandler = new Handler();
        startRepeatingTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    private String getTimeStamp() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        return (dateFormat.format(cal.getTime()).toString());

    }

    public void updateFetchMessage() {
        fetchMessage();
    }
    public int temp=0;
    private int mInterval = 3000; // 3 seconds by default, can be changed later
    private Handler mHandler;




    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            dwn.setVisibility(View.GONE);
                updateFetchMessage();
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if(dy<0)
                        {temp=1;dwn.setVisibility(View.VISIBLE);}
                        if (dy > 0) {
                            if ((mLinearLayoutManager.getChildCount() + mLinearLayoutManager.findFirstVisibleItemPosition()) >= mLinearLayoutManager.getItemCount()) {
                                Log.d("TAG", "End of list");
                                dwn.setVisibility(View.GONE);
                                temp=0;
                                                    }
                        }
                    }
                });
              if(temp==0)
                {
                    freeMemory();
                    mHandler.postDelayed(mStatusChecker, mInterval);}
            }

    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }


    public void fetchMessage() {

        database.getReference().child("message").addListenerForSingleValueEvent(
                new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        mFMessages = new ArrayList<FriendlyMessage>();

                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String fromUserId = ds.child("fromUserId").getValue(String.class);
                            String name = ds.child("name").getValue(String.class);
                            String text = ds.child("text").getValue(String.class);
                            String timestamp = ds.child("timeStamp").getValue(String.class);
                            mFMessages.add(new FriendlyMessage(text, name, fromUserId, timestamp));
                        }

                        if (mFMessages.size() > 0) {
                            mAdapter = new com.example.yuvo.CustomAdapter(mFMessages, fromUseridentify);
                            mRecyclerView.setAdapter(mAdapter);
                            dwn.setVisibility(View.GONE);
                            mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount()-1);
                            progressBar.setVisibility(View.GONE);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        dwn.setVisibility(View.GONE);
                        Log.w("", "getUser:onCancelled", databaseError.toException());
                    }


                });


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.msgsendbtn:
                if (isConnected(ChatActivity.this)){
                    if (msgText.getText() == null || msgText.getText().length() <= 0) return;
                FriendlyMessage friendlyMessage = new FriendlyMessage(msgText.getText().toString().trim(), currentUser, fromUseridentify, getTimeStamp());
                myRef.push().setValue(friendlyMessage).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Log.w("", " Write was successful!");

                        mFMessages.add(new FriendlyMessage(msgText.getText().toString().trim(), currentUser, fromUseridentify, getTimeStamp()));
                        mAdapter.notifyItemRangeChanged(mFMessages.size(), 1);
                        mRecyclerView.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                        msgText.setText("");
                        dwn.setVisibility(View.GONE);
                        updateFetchMessage();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Write failed
                        // ...
                        dwn.setVisibility(View.GONE);
                        Log.w("", " Write was failed!");
                    }
                });

        }
                break;
            case R.id.dwn:
                updateFetchMessage();
                if(isConnected(ChatActivity.this))
                dwn.setVisibility(View.GONE);

            default:

                break;
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.action_one) {
            final Dialog dialog = new Dialog(ChatActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            if(sp==0)
            dialog.setContentView(R.layout.help);
            else
                dialog.setContentView(R.layout.helptm);
            ImageButton dialogButton = (ImageButton) dialog.findViewById(R.id.close);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

        }
        else if (id == R.id.action_two) {
            finish();
            startActivity(getIntent());
        }
        else{
            startActivity(new Intent(ChatActivity.this, Homepage.class));
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(ChatActivity.this, Homepage.class));

    }


    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
}

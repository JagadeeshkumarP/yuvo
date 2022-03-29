package com.example.yuvo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.MODE_PRIVATE;
import static com.example.yuvo.FertilizerActivity.isConnected;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

public int sp=0;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView txtMarquee;TextView offline;WebView webview;
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        SharedPreferences pref1 = view.getContext().getSharedPreferences("lang", MODE_PRIVATE);
        String s=pref1.getString("lang", "en");
        if(s.equals("tm"))
           sp=1;
        else
        {}
        ViewPager mViewPager;
        FirebaseFirestore fStore;
        TextInputLayout feedhint=view.findViewById(R.id.outline);
        if(sp==1)
            feedhint.setHint("பின்னூட்டம்");
        fStore = FirebaseFirestore.getInstance();
        DocumentReference doc;
        doc = fStore.collection("imageslider").document("XfH8woWK4YUftuUVVuvW");
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               String img1 = task.getResult().getString("img1");
                String img2 =task.getResult().getString("img2");
                String img3= task.getResult().getString("img3");
                String img4= task.getResult().getString("img4");
                String img5= task.getResult().getString("img5");
                String img6= task.getResult().getString("img6");
                String yt=task.getResult().getString("yturl");
                String ythint=task.getResult().getString("ythint");
                String marqueetext=task.getResult().getString("marqueetext");
                String marqueetexttm=task.getResult().getString("marqueetexttm");
                try{SharedPreferences pref = getActivity().getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor edt = pref.edit();
                edt.putString("url1",img1);
                edt.putString("url2",img2);
                edt.putString("url3",img3);
                edt.putString("url4",img4);
                edt.putString("url5",img5);
                edt.putString("url6",img6);
                edt.putString("yturl",yt);
                edt.putString("ythint",ythint);
                edt.putString("marqueetext",marqueetext);
                edt.putString("marqueetexttm",marqueetexttm);
                edt.commit();}
                catch (NullPointerException nullPointerException){}
            }
        });

        SharedPreferences pref = getActivity().getPreferences(MODE_PRIVATE);
        String[] imageUrls = new String[]{pref.getString("url1", ""),pref.getString("url2", ""),pref.getString("url3", ""),pref.getString("url4", ""),pref.getString("url5", ""),pref.getString("url6", "")};

     ViewPagerAdapter mViewPagerAdapter;
        mViewPager =view.findViewById(R.id.viewPagerMain);
             mViewPagerAdapter = new ViewPagerAdapter(getContext(), imageUrls);
           mViewPager.setAdapter(mViewPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager, true);

        RecyclerView recyclerView;
        Adapter adapter;
        if(sp==1){
        ArrayList<String> items;
        items = new ArrayList<>();
        items.add("வானிலை அறிக்கை");
        items.add("உரம் விவரங்கள்");
        items.add("விதை விவரங்கள்");
        items.add("அணை நீர்மட்டம்");
        items.add("திட்டங்கள்");
        items.add("நிலப்பரப்பு");
        items.add("வேலை அலகுகள்");
        items.add("அதிகாரி சந்திப்பு");
        items.add("குடோன் அறிக்கை");
        items.add("இயற்கை விவசாயம்");
        items.add("குருவய் சிறப்பு");
        items.add("இயந்திர தேடல்");
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter(getContext(),items);
            GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);}
        else{
            ArrayList<String> items;
            items = new ArrayList<>();
            items.add("WEATHER REPORT");
            items.add("FERTILIZER DETAILS");
            items.add("SEED AVAILABILITY");
            items.add("RESERVOIRS");
            items.add("SCHEMES");
            items.add("LAND COVERAGE");
            items.add("WORKING UNITS");
            items.add("AGRI MEET");
            items.add("GODOWN REPORT");
            items.add("ORGANIC FARM");
            items.add("KURUVAI SPECIAL");
            items.add("CUSTOM HIRING");
            recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new Adapter(getContext(),items);
            GridLayoutManager layoutManager=new GridLayoutManager(getContext(),2);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        TextView ythint=view.findViewById(R.id.ythint);
        txtMarquee =view.findViewById(R.id.marqueeText);
        if(sp==1)
        txtMarquee.setText(pref.getString("marqueetexttm", "வணக்கம் !! யுவோவுக்கு வரவேற்கிறோம் !! உங்களுக்கு சிறந்த தகவல்களை வழங்குதல் :) - பொது அரட்டை மூலம் பயனடையுங்கள் - பாதுகாப்பாக இருங்கள். நன்றி :)"));
        else
            txtMarquee.setText(pref.getString("marqueetext", "Hello there !! Welcome to yuvo !!  Providing you the Best information :) -- Get Benifited via Personal and Public chat --   Stay Safe. Thank You :)"));

        txtMarquee.setSelected(true);
        webview=view.findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(false);
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(webview.getUrl().equals("http://www.youtube.com/embed/" + pref.getString("yturl", "") + "?autoplay=1&vq=small"))
                    return false;
                return true;
            }
        });

        offline =view.findViewById(R.id.offline);
        if(sp==1)
            offline.setText("இணையம் இல்லை. தயவுசெய்து உங்கள் இணைய இணைப்பைச் சரிபார்க்கவும்");
        else
            offline.setText("No internet. kindly check your internet connection");

        webview.loadUrl("http://www.youtube.com/embed/" + pref.getString("yturl", "") + "?autoplay=1&vq=small");

        if (!isConnected(getActivity())) {
            webview.setVisibility(View.INVISIBLE);
            ythint.setVisibility(View.GONE);
            offline.setVisibility(View.VISIBLE);
            try{
                if(sp==0)
                Toast.makeText(getActivity(), "You are offline ", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "இணையம் இல்லை", Toast.LENGTH_SHORT).show();
            }
            catch(NullPointerException nullPointerException){}

        }

        else
            webview.setVisibility(View.VISIBLE);

        ythint.setText( pref.getString("ythint", "") );
        TextView sent=view.findViewById(R.id.sent);
        if(sp==1)
            sent.setText("கருத்து அனுப்பப்பட்டது");
        ImageButton feedback=view.findViewById(R.id.feedbacksend);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText feedback = view.findViewById(R.id.feedback);
                FirebaseAuth auth;
               String userID;
                FirebaseFirestore fstore;
                fstore=FirebaseFirestore.getInstance();
                auth = FirebaseAuth.getInstance();
                Map<String, Object> user = new HashMap<>();
                user.put("feedback", feedback.getText().toString());

               try {
                    userID = auth.getCurrentUser().getUid();
                    DocumentReference documentReference = fstore.collection("USERS").document(userID);
                    documentReference.set(user, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            sent.setVisibility(View.VISIBLE);
                            if(sp==0)
                            Toast.makeText(getContext(), "Feed back sent", Toast.LENGTH_SHORT).show();

                                    else
                            Toast.makeText(getContext(), "கருத்து அனுப்பப்பட்டது", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                catch(Exception e){
                   if(sp==0)
                    Toast.makeText(getContext(), "Kindly signup to send feed back", Toast.LENGTH_SHORT).show();
                   else
                       Toast.makeText(getContext(), "கருத்துக்களை அனுப்ப தயவுசெய்து பதிவு செய்யவும்", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button lang=view.findViewById(R.id.lang);
       // TextView feedbacks=view.findViewById(R.id.feedbacksend);
        TextView feed=view.findViewById(R.id.feed);
        TextView text1=view.findViewById(R.id.text1);
        TextView text=view.findViewById(R.id.text);
        TextView text2=view.findViewById(R.id.kiwis);
        Button ad = view.findViewById(R.id.advertisment);
        if(sp==1)
        { ad.setText("விளம்பரங்கள்");
            feed.setText("பின்னூட்டம்");
            text.setText("வீடியோக்கள் மற்றும் தயாரிப்புகள்");
            //feedbacks.setText("பின்னூட்டம்");
            lang.setText("ENG");
            text1.setText("எங்களை பற்றி");
            text2.setText("YUVO || விவசாயத்தில் புதுமை மற்றும் நுண்ணறிவு ....\n\nYUVO மிகவும் திறமையான நிபுணர்களால் உருவாக்கப்பட்டது \n\n நாங்கள் விவசாயம் பற்றிய சிறந்த தகவல்களை வழங்குகிறோம்");
        }

        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sp==1)
                {
                    new AlertDialog.Builder(getContext()).setMessage("வணக்கம், உங்கள் தயாரிப்பை விளம்பரப்படுத்த தயவுசெய்து ஒரு மின்னஞ்சலை அனுப்பவும்: \n yuvoyuvoyuvo@gmail.com\n\nஅல்லது  \n\nஅழைக்கவும்:\n+91 6383427518")
                            .setTitle("விளம்பரங்கள்")
                            .setCancelable(true)
                            .show();
                }
                else{
                new AlertDialog.Builder(getContext()).setMessage("Hi , To Advertise Your Product kindly send a mail to:\n yuvoyuvoyuvo@gmail.com\n\nOR  \n\nmake call To:\n+91 6383427518")
                        .setTitle("ADVERTISMENT")
                        .setCancelable(true)
                        .show();}
            }
        });
ImageButton imageButton1=view.findViewById(R.id.mail);
        imageButton1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String TO="yuvoyuvoyuvo@gmail.com";
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL,  new String[]{TO});
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
        ImageButton imageButton2=view.findViewById(R.id.fb);
        imageButton2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Uri webpage = Uri.parse("https://www.facebook.com/groups/524447212217491/");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);}
        });
        ImageButton imageButton3=view.findViewById(R.id.call);
        imageButton3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},1000);
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
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
        ImageButton imageButton4=view.findViewById(R.id.whatsapp);
        imageButton4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Uri webpage = Uri.parse("https://chat.whatsapp.com/FXezQN9sBAv3IBspDUg6Cm");
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);}
        });

        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref2 = view.getContext().getSharedPreferences("lang", MODE_PRIVATE);
                String s=pref2.getString("lang", "en");
                if(s.equals("en")){
                    SharedPreferences sharedpreferences =view.getContext(). getSharedPreferences("lang", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("lang","tm");
                    editor.commit();}
                else{
                    SharedPreferences sharedpreferences = view.getContext().getSharedPreferences("lang", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("lang","en");
                    editor.commit();
                }

                startActivity(new Intent(getContext(), Homepage.class));


                 }
        });
        Button hireus=view.findViewById(R.id.hireus);
        hireus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), HireusActivity.class));
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        return true;
                    }
                }
                return false;
            }
        });

        return view;

    }

}
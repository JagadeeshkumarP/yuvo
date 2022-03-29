package com.example.yuvo;

import android.app.MediaRouteButton;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.Context.MODE_PRIVATE;
import static com.example.yuvo.FertilizerActivity.isConnected;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public int sp=0;
    public WeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
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
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_weather, container, false);
        SharedPreferences pref1 = view.getContext().getSharedPreferences("lang", MODE_PRIVATE);
        String s=pref1.getString("lang", "en");
        if(s.equals("tm"))
            sp=1;
        TextView text1=view.findViewById(R.id.textview1);
        TextView text=view.findViewById(R.id.textView);
        ProgressBar progressBar=view.findViewById(R.id.progress1);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.SRC_ATOP);
        progressBar.setVisibility(View.VISIBLE);
        TextView textView=view.findViewById(R.id.textview);
        textView.setVisibility(View.VISIBLE);
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        WebView webView=view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        if(sp==1){
            textView.setText("காத்திருங்கள்");
            text.setText("வானிலை அறிக்கை");
            text1.setText("இணையம் இல்லை. தயவுசெய்து உங்கள் இணைய இணைப்பைச் சரிபார்க்கவும்");
        }
        DocumentReference doc;
        doc = fStore.collection("webview").document("pzc4V7eHlNLSHnBcQlWO");
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                String temp = task.getResult().getString("rainfalllink");
                String temp1 = task.getResult().getString("rainfalllinktm");

                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                if(sp==0)
                    webView.loadUrl(temp);
                else
                    webView.loadUrl(temp1);
                webView.setVisibility(View.INVISIBLE);
                webView.setWebViewClient(new WebViewClient()
                {
                    @Override
                    public void onPageFinished(WebView view, String url){
                        DocumentReference doc;
                        doc = fStore.collection("webview").document("pzc4V7eHlNLSHnBcQlWO");
                        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                String temp1 = task.getResult().getString("rainfalljs");
                                try {
                                    int color = getActivity().getTitleColor();
                                    webView.loadUrl("javascript:(function() { document.body.style.background =" + color + " ;" + temp1);
                                }catch (NullPointerException nullPointerException){}

                                if (!isConnected(getActivity())) {
                                    webView.setVisibility(View.INVISIBLE);
                                    text1.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    textView.setVisibility(View.INVISIBLE);
                                    try{
                                        if(sp==0)Toast.makeText(getActivity(), "You are offline ", Toast.LENGTH_SHORT).show();
                                    else
                                            Toast.makeText(getActivity(),
                                                    "இணையம் இல்லை", Toast.LENGTH_SHORT).show();}
                                    catch(NullPointerException nullPointerException){}

                                }

                                else
                                    webView.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }

                });
            }
        });
        webView.canGoBack();
        webView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webView.canGoBack()) {
                    webView.goBack();
                    return true;
                }
                return true;
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
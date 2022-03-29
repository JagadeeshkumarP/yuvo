package com.example.yuvo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public int sp=0;
    public FeedsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedsFragment newInstance(String param1, String param2) {
        FeedsFragment fragment = new FeedsFragment();
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
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_feeds, container, false);
        SharedPreferences pref1 = view.getContext().getSharedPreferences("lang", MODE_PRIVATE);
        String s=pref1.getString("lang", "en");
        if(s.equals("tm"))
            sp=1;
        ProgressBar progressBar=view.findViewById(R.id.progress1);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.SRC_ATOP);

        progressBar.setVisibility(View.VISIBLE);
        TextView textView=view.findViewById(R.id.textview);
        TextView tamil=view.findViewById(R.id.tamil);
        tamil.setVisibility(View.GONE);
        TextView no=view.findViewById(R.id.textview1);
        TextView title=view.findViewById(R.id.textView);
        if(sp==1){
            textView.setText("காத்திருங்கள்");
            title.setVisibility(View.GONE);
            tamil.setVisibility(View.VISIBLE);
            no.setText("இணையம் இல்லை. தயவுசெய்து உங்கள் இணைய இணைப்பைச் சரிபார்க்கவும்");
        }
        textView.setVisibility(View.VISIBLE);
        FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        WebView webView=view.findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        DocumentReference doc;
        doc = fStore.collection("webview").document("pzc4V7eHlNLSHnBcQlWO");
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                String temp1 = task.getResult().getString("feedlink");
                String temp2 = task.getResult().getString("feedlinken");
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                if(sp==1)
                webView.loadUrl(temp1);
                else
                webView.loadUrl(temp2);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setLoadsImagesAutomatically(true);
                if (!isConnected(getActivity())) {
                    webView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    no.setVisibility(View.VISIBLE);
                    try{
                    if(sp==1)
                        Toast.makeText(getContext(), "இணையம் இல்லை ", Toast.LENGTH_SHORT).show();


                    else
                        Toast.makeText(getContext(), "You are offline ", Toast.LENGTH_SHORT).show();}
                    catch (NullPointerException nullPointerException){}

                }
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
                                String temp1 = task.getResult().getString("feedjs");
                                String temp2 = task.getResult().getString("feedjsen");
                                if(sp==1)
                                webView.loadUrl("javascript:(function() { "+temp1);
                                else
                                    webView.loadUrl("javascript:(function() { "+temp2);
                                webView.setVisibility(View.VISIBLE);
                                if (!isConnected(getActivity())) {
                                    webView.setVisibility(View.INVISIBLE);
                                    no.setVisibility(View.VISIBLE);
                                    try{
                                    if(sp==1)
                                        Toast.makeText(getContext(), "இணையம் இல்லை ", Toast.LENGTH_SHORT).show();

                                    else
                                        Toast.makeText(getContext(), "You are offline ", Toast.LENGTH_SHORT).show();
                                    webView.setVisibility(View.INVISIBLE);}
                                    catch(NullPointerException nullPointerException){}
                                }

                            }
                        });
                    }
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        if ((url.contains("https://agrinews.in/about-us/")) ||(url.contains("https://agrinews.in/submit-article/"))||(url.contains("https://agrinews.in/privacy-policy/"))||(url.contains("https://agrinews.in/advertise/"))||(url.contains("https://agrinews.in/contact-us/"))||(url.contains("https://agrinews.in/terms-and-conditions/"))||(url.contains("https://agrinews.in/disclaimer/")) ) {

                            return true;
                        }
                        return false;
                    }

                });
            }
        });
        webView.canGoBack();
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

       return view;

    }
    public static boolean isConnected(Context context) {

        try{ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm!=null) {
            NetworkInfo info = cm.getActiveNetworkInfo();

            return (info != null && info.isConnected());
        }}
        catch(NullPointerException nullPointerException){}
        return false;
    }
}
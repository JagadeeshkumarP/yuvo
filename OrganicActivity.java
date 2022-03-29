package com.example.yuvo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

public class OrganicActivity extends AppCompatActivity {
    WebView webView;
    TextView text,text1,text2;
    FirebaseFirestore fStore;
    public int sp=0;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer);
        SharedPreferences pref = getSharedPreferences("lang", MODE_PRIVATE);
        String s=pref.getString("lang", "en");
        if(s.equals("tm"))
            sp=1;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        progressBar=findViewById(R.id.progress1);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFcc0000, android.graphics.PorterDuff.Mode.SRC_ATOP);

        text1=findViewById(R.id.textview1);
        text=findViewById(R.id.textview);

        fStore = FirebaseFirestore.getInstance();
        webView=(WebView)findViewById(R.id.webView);
        webView.setVisibility(View.INVISIBLE);
        webView.setWebViewClient(new OrganicActivity.MyBrowser(getTitleColor()));
        text2=findViewById(R.id.textView);
        if(sp==1){
            text.setText("காத்திருங்கள்");
            text2.setText("இயற்கை விவசாயம்");
            text1.setText("இணையம் இல்லை. தயவுசெய்து உங்கள் இணைய இணைப்பைச் சரிபார்க்கவும்");

        }
        else{
            text2.setText("ORGANIC FARM");

        }
        DocumentReference doc;
        doc = fStore.collection("webview").document("pzc4V7eHlNLSHnBcQlWO");
        doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String temp = task.getResult().getString("organiclink");
                if(sp==1)
                    temp = task.getResult().getString("organiclinktm");
                webView.setVisibility(View.INVISIBLE);
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                if (!isConnected(getApplication())) {
                    webView.setVisibility(View.INVISIBLE);
                    if(sp==1)
                        Toast.makeText(OrganicActivity.this, "இணையம் இல்லை ", Toast.LENGTH_SHORT).show();

                    else
                        Toast.makeText(OrganicActivity.this, "You are offline ", Toast.LENGTH_SHORT).show();

                }
                webView.loadUrl(temp);


            }
        });



    }
    @Override
    public void onBackPressed()
    {
        if(webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
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
    public static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != cm) {
            NetworkInfo info = cm.getActiveNetworkInfo();

            return (info != null && info.isConnected());
        }
        return false;
    }
    private class MyBrowser extends WebViewClient {

        int color;

        public MyBrowser(int color) {
            this.color = color;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
            text.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.startsWith("tel:") || url.startsWith("whatsapp:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                return true;
            }
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Toast.makeText(OrganicActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            handler.proceed();
        }

        @Override
        public void onPageFinished(WebView view, String url){
            DocumentReference doc;
            doc = fStore.collection("webview").document("pzc4V7eHlNLSHnBcQlWO");
            doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String temp1 = task.getResult().getString("organicjs");
                    //Toast.makeText(OrganicActivity.this, m, Toast.LENGTH_SHORT).show();
                    webView.loadUrl("javascript:(function() { document.body.style.background = " + color + ";"+temp1);
                    if (!isConnected(getApplication())) {
                        webView.setVisibility(View.INVISIBLE);
                        text1.setVisibility(View.VISIBLE);
                        if(sp==1)
                            Toast.makeText(OrganicActivity.this, "இணையம் இல்லை ", Toast.LENGTH_SHORT).show();

                        else
                            Toast.makeText(OrganicActivity.this, "You are offline ", Toast.LENGTH_SHORT).show();

                    }

                    else
                        webView.setVisibility(View.VISIBLE);
                    text.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);

                }
            });


        }

    }
}
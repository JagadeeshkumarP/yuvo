package com.example.yuvo;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Homepage extends AppCompatActivity {
    public  int temp=0;
    public int sp=0;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences pref1 =getSharedPreferences("lang", MODE_PRIVATE);
        String s=pref1.getString("lang", "en");
        if(s.equals("tm"))
            sp=1;
        if(sp==0)
        setContentView(R.layout.activity_homepage);
        else
            setContentView(R.layout.activity_homepagetm);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation1);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        if(temp==1 ){

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent notificationIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent broadcast = PendingIntent.getBroadcast(this, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);            Calendar cal = Calendar.getInstance();

            cal.setTimeInMillis(System.currentTimeMillis());
            cal.set(Calendar.HOUR_OF_DAY,8);
            cal.set(Calendar.MINUTE,00);
            cal.set(Calendar.SECOND, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,broadcast);//alarm manager will repeat the notification each day at the set time

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {
            // do something here
            Intent intent = new Intent(Homepage.this,Profile.class);
            startActivity(intent);
        }
        else if(id==R.id.notification){
            if(temp==1) {
                temp = 0;
                if(sp==0)
                Toast.makeText(Homepage.this, "Notifications turned OFF ", Toast.LENGTH_SHORT).show();
                else
                Toast.makeText(Homepage.this, "அறிவிப்புகள் முடக்கப்பட்டன", Toast.LENGTH_SHORT).show();
            }else
            {temp=1;
            if(sp==0)
                Toast.makeText(Homepage.this,"Notifications turned ON", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(Homepage.this, "அறிவிப்புகள் இயக்கப்பட்டன ", Toast.LENGTH_SHORT).show();
            }
           }
        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_feeds:
                    selectedFragment = new FeedsFragment();
                    break;
                case R.id.nav_chat:
                    selectedFragment = new ChatFragment();
                    break;
                case R.id.nav_weather:
                    selectedFragment = new WeatherFragment();
                    break;
                case R.id.nav_cost:
                    selectedFragment = new CostFragment();
                    break;
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
            return true;
        }
    };
}
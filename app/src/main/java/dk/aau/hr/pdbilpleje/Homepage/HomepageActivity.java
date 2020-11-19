package dk.aau.hr.pdbilpleje.Homepage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import dk.aau.hr.pdbilpleje.R;



public class HomepageActivity extends AppCompatActivity {


    public BottomNavigationView mBotNavView;
    public ImageButton mNewsView, mSettingsView, mBookingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.mainPageLayout, new HomepageFragment())
                .commit();


        mBotNavView = findViewById(R.id.botNavView);
        mNewsView = findViewById(R.id.newsView);
        mSettingsView = findViewById(R.id.settingsView);
        mBookingView = findViewById(R.id.bookingView);


        Toast.makeText(this, "It worked mothersucka!", Toast.LENGTH_SHORT).show();


        mBookingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .add(R.id.mainPageLayout, new BookingFragment())
                        .commit();

            }
        });


        mSettingsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .add(R.id.mainPageLayout, new SettingsFragment())
                        .commit();

            }
        });



        mNewsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction()
                        .add(R.id.mainPageLayout, new HomepageFragment())
                        .commit();

            }
        });




    } //OnCreate method end



}

package dk.aau.hr.pdbilpleje.Homepage;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import dk.aau.hr.pdbilpleje.R;

public class HomepageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .add(R.id.mainPageLayout, new HomepageFragment())
                .commit();




        Toast.makeText(this, "It worked mothersucka!", Toast.LENGTH_SHORT).show();





    }




}

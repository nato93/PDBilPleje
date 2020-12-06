package dk.aau.hr.pdbilpleje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dk.aau.hr.pdbilpleje.Homepage.HomepageActivity;
import dk.aau.hr.pdbilpleje.R;

public class FrontPageActivity extends AppCompatActivity {



    public Button mLoginButton;
    public TextView mTextViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);


        mLoginButton = findViewById(R.id.loginButton);
        mTextViewSignUp = findViewById(R.id.textViewSignUp);


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(FrontPageActivity.this, SignUpActivity.class);
                startActivity(intent);


            }
        });



    }












}

package dk.aau.hr.pdbilpleje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import dk.aau.hr.pdbilpleje.Homepage.HomepageActivity;

public class LoginActivity extends AppCompatActivity {


    private final static String TAG = "LoginActivity";

    public TextView mEmailTextView, mPasswordTextview;
    public Button mLoginButton, mFBloginButton;
    public ImageView mLogoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmailTextView = findViewById(R.id.textFieldEmail);
        mPasswordTextview = findViewById(R.id.textFieldPassword);
        mLoginButton = findViewById(R.id.loginButton);
        //mFBloginButton = findViewById(R.id.fb_login_button);
        mLogoImageView = findViewById(R.id.imageViewLogo2);
        mLogoImageView.setImageResource(R.drawable.pdbilplejelogo);




        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "You clicked the button");
                Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                startActivity(intent);


            }
        });









    }
}
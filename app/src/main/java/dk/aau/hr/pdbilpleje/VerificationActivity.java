package dk.aau.hr.pdbilpleje;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    public EditText mVerificationEt;
    public Button mVerificationButton, mLoginButton;
    public String userTypedCode;
    private TextView mProcessText;
    private String phoneNumber = "71430433";
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        //Assigning the textfields
        mVerificationEt            = findViewById(R.id.textInputEmail2);
        mVerificationButton        = findViewById(R.id.verifyButton);
        mLoginButton               = findViewById(R.id.loginButton2);
        mProcessText               = findViewById(R.id.textProcess);

        //Instantiating a new object of Loginactivity to use it's methods.
        final LoginActivity loginActivity = new LoginActivity();
        auth = FirebaseAuth.getInstance();


        mVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Send code to the currrent user's phone number.

            loginActivity.sendVerificationCodeToUser(phoneNumber);

                mProcessText.setText("Code has been sent to phone number: " + "get the phone number from the db");
                mProcessText.setTextColor(Color.RED);
                mProcessText.setVisibility(View.VISIBLE);

            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if it's the correct code.
                userTypedCode = mVerificationEt.toString();
                //Check if the verification code send to the phone is equal to the one in mVerificationEt
                loginActivity.verifyCode(userTypedCode);

            }
        });


        }
    }



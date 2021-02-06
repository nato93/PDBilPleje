package dk.aau.hr.pdbilpleje;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

public class VerificationActivity extends AppCompatActivity {

    public EditText mVerificationEt;
    public Button mGetVerificationCode, mLoginButton;
    public String userTypedCode;
    private TextView processText;
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

        auth = FirebaseAuth.getInstance();

        //Assigning the textfields
        mVerificationEt            = findViewById(R.id.textInputEmail2);
        mGetVerificationCode = findViewById(R.id.verifyButton);
        mLoginButton             = findViewById(R.id.loginButton2);
        //Instantiating a new object of Loginactivity to use it's methods.
        final LoginActivity loginActivity = new LoginActivity();

        mGetVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //Send a verification code to the current user's phone number



            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if the verification code send to the phone is equal to the one the user typed.
                userTypedCode = mVerificationEt.toString();
                loginActivity.verifyCode(userTypedCode);
            }
        });






        }
    }



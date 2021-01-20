package dk.aau.hr.pdbilpleje;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {


    public EditText mVerificationEt;
    public Button mVerificationButton;
    public String userTypedCode;

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
        mVerificationButton             = findViewById(R.id.verifyButton);
        //Instantiating a new object of Loginactivity to use it's methods.
        final LoginActivity loginActivity = new LoginActivity();

        mVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userTypedCode = mVerificationEt.toString();
                //Check if the verification code send to the phone is equal to the one in mVerificationEt
                loginActivity.verifyCode(userTypedCode);
            }
        });
        }
    }



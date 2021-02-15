package dk.aau.hr.pdbilpleje;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.concurrent.TimeUnit;

import dk.aau.hr.pdbilpleje.Homepage.HomepageActivity;

public class VerificationActivity extends AppCompatActivity {
    public EditText mVerificationEt;
    public Button mVerificationButton, mLoginButton;
    public String userTypedCode;
    private TextView mProcessText;
    public String phoneNumber = "71430433";
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    FirebaseFirestore fStore;
    private String name = "unknown";
    private final static String TAG = "VerificationActivity";
    private DocumentReference docRef;
    public String verificationCodeBySystsem;
    // THIS IS THE NEW CODE
    @Override
    public void onStart() {
        super.onStart();
        fStore = FirebaseFirestore.getInstance();
        docRef = fStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
    //fs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        //auth = FirebaseAuth.getInstance();
        //Assigning the textfields
        mVerificationEt            = findViewById(R.id.textInputEmail2);
        mVerificationButton        = findViewById(R.id.verifyButton);
        mLoginButton               = findViewById(R.id.loginButton2);
        mProcessText               = findViewById(R.id.textProcess);
        //Instantiating a new object of Loginactivity to use it's methods.
        //final LoginActivity loginActivity = new LoginActivity();

        mVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //loginActivity.sendVerificationCodeToUser(phoneNumber);
                //mProcessText.setText("Code has been sent to phone number: " + "get the phone number from the db");
                //mProcessText.setTextColor(Color.RED);
                //mProcessText.setVisibility(View.VISIBLE);
            }
        });

        //Send code to the currrent user's phone number.
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            try {
                                //boolean phoneNumber = documentSnapshot.getBoolean("twofactor");
                                String phonenumber = documentSnapshot.getString("phonenumber");
                                mProcessText.setText(phonenumber);
                            } catch (NullPointerException e){
                                Log.d(TAG, "Nullpointerexception the value was null DET DEN HER!");
                            }
                        }else{
                            Toast.makeText(VerificationActivity.this, "This Field doesn't exist.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VerificationActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check if it's the correct code.
                userTypedCode = mVerificationEt.toString();
                //Check if the verification code send to the phone is equal to the one in mVerificationEt
                //loginActivity.verifyCode(userTypedCode);
            }
        });

        sendVerificationCodeToUser(phoneNumber);

    }

    //This method sends the code to the users phone number
    public void sendVerificationCodeToUser (String phoneno){
        Toast.makeText(VerificationActivity.this, "THE CODE SHOULD BE SENT KEK", Toast.LENGTH_SHORT).show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+ 45" + phoneno,         // Phone number to verify
                60,
                TimeUnit.SECONDS, // Timeout and unit
                TaskExecutors.MAIN_THREAD,                    // Activity (for callback binding)
                mCallbacks);          // OnVerificationStateChangedCallbacks
    }

    //This method checks if the sms sent was successful
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystsem = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerificationActivity.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
        }
    };

    //This method checks if the Sms code matches the one the user typed.
    public void verifyCode(String codeByUser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystsem, codeByUser);
        FirebaseUser user = firebaseAuth.getCurrentUser();
        Intent intent = new Intent(VerificationActivity.this, HomepageActivity.class);
        startActivity(intent);
    }
}



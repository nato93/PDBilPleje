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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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
    public String userTypedOTP;
    private TextView mProcessText;
    //public String phoneNumber;
    private FirebaseAuth auth;
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    FirebaseFirestore fStore;
    private String name = "unknown";
    private final static String TAG = "VerificationActivity";
    private DocumentReference docRef;
    public String verificationCode;
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
        mVerificationEt            = findViewById(R.id.textInputVerification);
        mVerificationButton        = findViewById(R.id.verifyButton);
        mLoginButton               = findViewById(R.id.loginButton2);
        mProcessText               = findViewById(R.id.textProcess);


        // Method that handles the callbacks a.k.a checking if the OTP was sent

        mVerificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Send code to the currrent user's phone number.
                docRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){

                                    try {
                                        //boolean phoneNumber = documentSnapshot.getBoolean("twofactor");
                                        String phoneNumber = documentSnapshot.getString("phonenumber");
                                        mProcessText.setText(phoneNumber);
                                        //sendVerificationCodeToUser(phoneNumber);
                                        StartFirebaseLogin();

                                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                                 "+45" + phoneNumber,                     // Phone number to verify
                                                60,                           // Timeout duration
                                                TimeUnit.SECONDS,                // Unit of timeout
                                                VerificationActivity.this,        // Activity (for callback binding)
                                                mCallbacks);                      // OnVerificationStateChangedCallbacks


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

                //loginActivity.sendVerificationCodeToUser(phoneNumber);
                //mProcessText.setText("Code has been sent to phone number: " + "get the phone number from the db");
                //mProcessText.setTextColor(Color.RED);
                //mProcessText.setVisibility(View.VISIBLE);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userTypedOTP = mVerificationEt.toString();

                if (userTypedOTP.isEmpty() || userTypedOTP.length() < 6) {
                    mProcessText.setText("the typed OTP length is too short!");
                    mProcessText.setTextColor(Color.RED);
                    mProcessText.setVisibility(View.VISIBLE);
                    mVerificationEt.requestFocus();
                }
                verifyCode(userTypedOTP);
            }
        });

    }

    private void verifyCode(String codeByUser) {
        //PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, codeByUser);
        //signInTheUserByCredentials(credential);
        Toast.makeText(VerificationActivity.this,"The code match!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(VerificationActivity.this, HomepageActivity.class));

        if (codeByUser.equals(verificationCode)){
            Toast.makeText(VerificationActivity.this,"The codes matches!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(VerificationActivity.this, HomepageActivity.class));
        }



    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(VerificationActivity.this,"verification completed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(VerificationActivity.this,"didn't sucessfully send code!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                //Toast.makeText(VerificationActivity.this,"Code sent",Toast.LENGTH_SHORT).show();
                mProcessText.setText("Code has been sent");
                mProcessText.setTextColor(Color.GREEN);
                mProcessText.setVisibility(View.VISIBLE);
            }
        };
    }


    private void signInTheUserByCredentials(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerificationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(VerificationActivity.this, "You logged in successfully!", Toast.LENGTH_SHORT).show();
                            //Perform Your required action here to either let the user sign In or do something required
                            Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            mProcessText.setText("Wrong code or System error!");
                            mProcessText.setTextColor(Color.RED);
                            mProcessText.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(VerificationActivity.this, HomepageActivity.class));
                            finish();
                        } else {
                            Toast.makeText(VerificationActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                            mProcessText.setText("Wrong code!");
                            mProcessText.setTextColor(Color.RED);
                            mProcessText.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

   /*

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
    public PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCode = s;
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
            Toast.makeText(VerificationActivity.this, "SMS DIDN'T SEND SUCESSFULLY!", Toast.LENGTH_SHORT).show();
        }
    };

    //This method checks if the Sms code matches the one the user typed.
    public void verifyCode(String codeByUser){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, codeByUser);
        FirebaseUser user = auth.getCurrentUser();
        Intent intent = new Intent(VerificationActivity.this, HomepageActivity.class);
        startActivity(intent);
    }

*/




}



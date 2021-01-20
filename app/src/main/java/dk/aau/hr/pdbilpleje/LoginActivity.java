package dk.aau.hr.pdbilpleje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

import dk.aau.hr.pdbilpleje.Homepage.HomepageActivity;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = "LoginActivity";
    private FirebaseAuth firebaseAuth;
    public EditText mEmailEt, mPasswordEt;
    public Button mLoginButton, mFBloginButton;
    public ImageView mLogoImageView;
    private ProgressDialog progressDialog;
    FirebaseFirestore fStore;

    public String verificationCodeBySystsem;
    public boolean UserHasTwoFactor;
    public String phoneNo;

    @Override
    public void onStart() {
        super.onStart();
        fStore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        mEmailEt = findViewById(R.id.textInputEmail);
        mPasswordEt = findViewById(R.id.textInputPassword);
        mLoginButton = findViewById(R.id.loginButton);
        mLogoImageView = findViewById(R.id.imageViewLogo2);




        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //uncomment login()  if you want login to work again
                Login();

            }
        });

    }

    public void Login(){

        firebaseAuth.signInWithEmailAndPassword(mEmailEt.getText().toString(), mPasswordEt.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //check to see if the user has turned on 2fa
                            DocumentReference docRef = fStore.collection("users").document(FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid());
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        //If the document exists
                                        DocumentSnapshot document = task.getResult();
                                        if (document != null){
                                            // assigning the field values from Firestore to our global variables
                                            UserHasTwoFactor = document.getBoolean("2fa");
                                            phoneNo = document.getString("phoneNo");
                                        } else {
                                            //If the document doesn't exist
                                            Log.d("LOGGER", "No such document");
                                        }
                                    }
                                }
                            });

                            //if the user's 2FA is true send a verification code and go to VerificationActivity.

                            if (UserHasTwoFactor == true){
                                //send a verification code to the current user
                                sendVerificationCodeToUser(phoneNo);
                                //start verification activity
                                Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                                startActivity(intent);
                            } else {
                                // Sign in success
                                //was only line 122 - 126 inside the task.isSucessful before.
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "Velkommen! " + user.toString(),
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            System.out.println("Sign-in Failed: " + task.getException().getMessage());

                            Toast.makeText(LoginActivity.this, "couldn't login", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }


    //This method sends the code to the users phone number
    private void sendVerificationCodeToUser (String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+ 45" + phoneNumber,         // Phone number to verify
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
            Toast.makeText(LoginActivity.this, "Verification Failed!", Toast.LENGTH_SHORT).show();
        }
    };

    //This method checks if the Sms code matches the one the user typed.
    public void verifyCode(String codeByUser){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystsem, codeByUser);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
        startActivity(intent);
    }




}
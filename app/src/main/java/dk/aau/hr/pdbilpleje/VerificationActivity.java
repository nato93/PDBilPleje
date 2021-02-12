package dk.aau.hr.pdbilpleje;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

    public EditText mVerificationEt;
    public Button mVerificationButton, mLoginButton;
    public String userTypedCode;
    private TextView mProcessText;
    public String phoneNumber = "71430433";
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    FirebaseFirestore fStore;
    private String name = "unknown";
    private final static String TAG = "VerificationActivity";
    private DocumentReference docRef = fStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        fStore = FirebaseFirestore.getInstance();

        //Assigning the textfields
        mVerificationEt            = findViewById(R.id.textInputEmail2);
        mVerificationButton        = findViewById(R.id.verifyButton);
        mLoginButton               = findViewById(R.id.loginButton2);
        mProcessText               = findViewById(R.id.textProcess);

        //Instantiating a new object of Loginactivity to use it's methods.
        //final LoginActivity loginActivity = new LoginActivity();
        auth = FirebaseAuth.getInstance();


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
                                        String name = documentSnapshot.getString("name");
                                        mProcessText.setText(name);

                                    } catch (NullPointerException e){
                                        Log.d(TAG, "Nullpointerexception the value was null!");
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
                //Check if it's the correct code.
                userTypedCode = mVerificationEt.toString();
                //Check if the verification code send to the phone is equal to the one in mVerificationEt
                //loginActivity.verifyCode(userTypedCode);

            }
        });

        }

        public void loadRef (View v){

        }


    }



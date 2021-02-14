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
    FirebaseFirestore db;

    public boolean userHasTwoFactor;
    public String phoneNo;

    @Override
    public void onStart() {
        super.onStart();
        db = FirebaseFirestore.getInstance();
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
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                                //If 2fa is turned on the user account, start verificationActivity

                           // if (userHasTwoFactor == true){fd
                           // }
                                //changed it from HomepageActivity to VerificationActivity.

                                Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "Velkommen! " + user.toString(),
                                        Toast.LENGTH_SHORT).show();


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
}
package dk.aau.hr.pdbilpleje;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import dk.aau.hr.pdbilpleje.Homepage.HomepageActivity;

public class LoginActivity extends AppCompatActivity {


    private final static String TAG = "LoginActivity";
    private FirebaseAuth firebaseAuth;
    public EditText mEmailEt, mPasswordEt, mEmailreset;
    public Button mLoginButton, mFBloginButton;
    public TextView mResetpassTextview;
    public ImageView mLogoImageView;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    FirebaseFirestore db;
    //private DatabaseHandler db = new DatabaseHandler();
    public DocumentReference docRef;



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
        mResetpassTextview = findViewById(R.id.textViewForgotPassword);


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEmailEt.getText().toString().isEmpty()){
                    mEmailEt.setError("Indtast venligst din email!");
                } else if (mPasswordEt.getText().toString().isEmpty()){
                    mPasswordEt.setError("Venligst indtast din adgangskode!");
                } else {
                    Login();
                }
                //uncomment login()  if you want login to work again
            }
        });

        mResetpassTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createResetPassDialog();
            }
        });

    }

    public void createResetPassDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View newCarPopup = getLayoutInflater().inflate(R.layout.popup_resetpass, null);
        dialogBuilder.setView(newCarPopup);
        dialogBuilder.setPositiveButton("NULSTIL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isValid(mEmailreset.getText().toString())) {
                    firebaseAuth.sendPasswordResetEmail(mEmailreset.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Nulstillings email sendt! Venligst tjek din email", Toast.LENGTH_LONG).show();
                                        Log.d(TAG, "Email sent.");
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Indtast en korrekt email!", Toast.LENGTH_SHORT).show();
                    mEmailreset.setError("Indtast en korrekt email!");
                }
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();
        mEmailreset = dialog.findViewById(R.id.editTextEmailReset);
    }

    static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public void Login(){
        firebaseAuth.signInWithEmailAndPassword(mEmailEt.getText().toString(), mPasswordEt.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {




                            //put this 1
                            docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            //put this 1
                            //Send code to the currrent user's phone number.
                            docRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.exists()){

                                                try {
                                                    //boolean phoneNumber = documentSnapshot.getBoolean("twofactor");
                                                    boolean userHasTwoFactor = documentSnapshot.getBoolean("twofactor");
                                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                                    //If 2fa is turned on the user account, start verificationActivity
                                                    // if (userHasTwoFactor == true){fd
                                                    // }

                                                    if(userHasTwoFactor == true){
                                                        Intent intent = new Intent(LoginActivity.this, VerificationActivity.class);
                                                        startActivity(intent);

                                                    }else{
                                                        Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                                                        startActivity(intent);
                                                        Toast.makeText(LoginActivity.this, "Velkommen! ",
                                                                Toast.LENGTH_SHORT).show();
                                                    }

                                                } catch (NullPointerException e){
                                                    Toast.makeText(LoginActivity.this, "The twofactor was null", Toast.LENGTH_SHORT).show();

                                                }
                                            }else{
                                                Toast.makeText(LoginActivity.this, "This Field doesn't exist.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(LoginActivity.this, HomepageActivity.class);
                                                startActivity(intent);
                                                Toast.makeText(LoginActivity.this, "Velkommen! ",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }


                                    })

                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                            Log.d(TAG, e.toString());
                                        }
                                    });

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
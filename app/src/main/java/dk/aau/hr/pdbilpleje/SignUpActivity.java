package dk.aau.hr.pdbilpleje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dk.aau.hr.pdbilpleje.Homepage.HomepageActivity;

public class SignUpActivity extends AppCompatActivity {

    public EditText mEmailEt,mRepeatPasswordEt, mPasswordEt, mPhoneNumberEt;
    public Button mSignUpButton;
    private final static String TAG = "SignUpActivity";
    FirebaseFirestore fStore;
    private FirebaseAuth mAuth;
    public FirebaseUser fUser;
    private String userID;
    private TextView mExistingUser, mError;
    public String email, password, phoneNumber, passwordRepeat;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
       // FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //instantiating the firebase
        //userID = fUser.getUid();
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //Assigning the textfields
        mEmailEt            = findViewById(R.id.textInputEmail2);
        mPasswordEt         = findViewById(R.id.textInputPassword2);
        mRepeatPasswordEt   = findViewById(R.id.textInputRepeatPassword2);
        mSignUpButton       = findViewById(R.id.signupButton);
        mPhoneNumberEt      = findViewById(R.id.textInputPhoneNumber);


        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //we assign the values from the user into Strings
                email = mEmailEt.getText().toString();
                password = mPasswordEt.getText().toString();
                passwordRepeat = mRepeatPasswordEt.getText().toString();
                //phoneNumber = mPhoneNumberEt.getText().toString();

                createAccount();

                //Create a HashMap where you store the user data
/*                Map<String, String> user = new HashMap<>();
                user.put("name", "email");

                // Add a new document with a generated ID
                fStore.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), HomepageActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "registration didnt work!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });*/

/*                if (!password.equals(passwordRepeat)) {
                    Toast.makeText(SignUpActivity.this,"Passwords do not match.", Toast.LENGTH_SHORT).show();
                } else {
                    createAccount();
                }*/
            } //onClick View end
        }); //onClicklistener end


    }//on create end




    public void createAccount(){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        try {
                                            //check if successful
                                            if (task.isSuccessful()) {
                                                //User is successfully registered and logged in
                                                //start Profile Activity here
                                                Toast.makeText(SignUpActivity.this, "registration successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
                                                //Now save the information in the firebase firestore




                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Couldn't register, try again", Toast.LENGTH_SHORT).show();
                                                FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                                Log.e("SignupActivity", "Failed Registration", e);
                                                //TODO: Add text on screen with error

                                                try {
                                                    throw task.getException();
                                                } catch(FirebaseAuthWeakPasswordException err) {

                                                    mError.setText(R.string.weak_password_exception);
                                                    mError.setTextColor(Color.RED);
                                                    mPasswordEt.requestFocus();

                                                } catch(FirebaseAuthInvalidCredentialsException err) {

                                                    mError.setText(R.string.wrong_email_format);
                                                    mError.setTextColor(Color.RED);
                                                    mEmailEt.requestFocus();

                                                } catch(FirebaseAuthUserCollisionException err) {

                                                    mError.setText(R.string.email_is_in_use);
                                                    mError.setTextColor(Color.RED);
                                                    mEmailEt.requestFocus();

                                                } catch(Exception err) {
                                                    Log.e(TAG, e.getMessage());
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
    }




} // class end
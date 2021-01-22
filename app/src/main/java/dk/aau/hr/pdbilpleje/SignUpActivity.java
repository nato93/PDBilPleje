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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import dk.aau.hr.pdbilpleje.Homepage.HomepageActivity;

public class SignUpActivity extends AppCompatActivity {

    public EditText mEmailEt,mRepeatPasswordEt, mPasswordEt, mPhoneNumberEt,mNameEt,mLastNameEt,mPostcodeEt;
    public Button mSignUpButton;
    private final static String TAG = "SignUpActivity";

    // Database stuff
    public String userId;
    public FirebaseUser fUser;
    FirebaseFirestore fStore;
    public FirebaseAuth mAuth;
    public String email, password, phoneNumber, passwordRepeat, name, lastName, postCode;

    @Override
    public void onStart() {
        super.onStart();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Assigning the textfields
        mEmailEt            = findViewById(R.id.textInputEmail2);
        mNameEt             = findViewById(R.id.textInputName);
        mLastNameEt             = findViewById(R.id.textInputLastName);
        mPostcodeEt            = findViewById(R.id.textInputPostcode);
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
                phoneNumber = mPhoneNumberEt.getText().toString();
                name = mNameEt.getText().toString();
                lastName = mLastNameEt.getText().toString();
                postCode = mPostcodeEt.getText().toString();

                createAccount();

            }
        });
    }


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
                                                Map<String, Object> user = new HashMap<>();
                                                user.put("email", email);
                                                user.put("name", name);
                                                user.put("lastname", lastName);
                                                user.put("phonenumber", phoneNumber);
                                                user.put("postalcode", postCode);
                                                user.put("twofactor", false);

                                                fStore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .set(user)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                                                Toast.makeText(getApplicationContext(), "registration successful", Toast.LENGTH_SHORT).show();
                                                                startActivity(new Intent(getApplicationContext(), HomepageActivity.class));
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error writing document", e);
                                                                Toast.makeText(SignUpActivity.this, "registration didnt work!", Toast.LENGTH_SHORT).show();
                                                                return;
                                                            }
                                                        });

                                                Toast.makeText(SignUpActivity.this, "registration successful", Toast.LENGTH_SHORT).show();

                                                startActivity(new Intent(getApplicationContext(), HomepageActivity.class));

                                            } else {
                                                Toast.makeText(SignUpActivity.this, "Couldn't register, try again", Toast.LENGTH_SHORT).show();
                                                FirebaseAuthException e = (FirebaseAuthException)task.getException();
                                                Log.e("SignupActivity", "Failed Registration", e);
                                                //TODO: Add text on screen with error

                                                try {
                                                    throw task.getException();
                                                } catch(FirebaseAuthWeakPasswordException err) {

                                                    Toast.makeText(SignUpActivity.this,"Password is too short, min. 6 characters", Toast.LENGTH_SHORT).show();
                                                    //mError.setTextColor(Color.RED);
                                                    mPasswordEt.requestFocus();


                                                } catch(FirebaseAuthInvalidCredentialsException err) {

                                                    Toast.makeText(SignUpActivity.this,"Please enter a valid email.",
                                                            Toast.LENGTH_SHORT).show();
                                                    mEmailEt.requestFocus();


                                                } catch(FirebaseAuthUserCollisionException err) {

                                                    Toast.makeText(SignUpActivity.this,"The entered email is already in use.", Toast.LENGTH_SHORT).show();
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



    //Method to check if all the fields have been filled out
    private boolean validateForm() {

        boolean valid;

        if (mEmailEt.getText().toString().isEmpty() || mPasswordEt.getText().toString().isEmpty() ||
                mRepeatPasswordEt.getText().toString().isEmpty() || mNameEt.getText().toString().isEmpty() || mPhoneNumberEt.getText().toString().isEmpty()) {

            valid = false;

            if (mEmailEt.length() == 0) {
                mEmailEt.requestFocus();
                Toast.makeText(SignUpActivity.this,"Please enter a name.",
                        Toast.LENGTH_SHORT).show();
            }

            if (mPasswordEt.length() == 0) {
                mPasswordEt.requestFocus();
                Toast.makeText(SignUpActivity.this,"Please enter a password.",
                        Toast.LENGTH_SHORT).show();
            }

            if (mRepeatPasswordEt.length() == 0) {
                mRepeatPasswordEt.requestFocus();
                mRepeatPasswordEt.setError("Please enter an address.");
            }

        } else { valid = true; }

        if (!password.equals(passwordRepeat)) {
            Toast.makeText(SignUpActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
} // class end
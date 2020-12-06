package dk.aau.hr.pdbilpleje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dk.aau.hr.pdbilpleje.Homepage.HomepageActivity;

public class SignUpActivity extends AppCompatActivity {


    public TextView mtextFieldEmail2, getMtextFieldPassword2;
    public Button msignupButton;
    private FirebaseAuth mAuth;
    private final static String TAG = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        //Initializing database
        final DatabaseHandler db = new DatabaseHandler();
        mAuth = FirebaseAuth.getInstance();


        mtextFieldEmail2 = findViewById(R.id.textFieldEmail2);
        msignupButton = findViewById(R.id.signupButton);
        getMtextFieldPassword2 = findViewById(R.id.textFieldPassword2);


        msignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //if the required fields has been filled, check if the user already exists

                //if not create user
/*                mAuth.createUserWithEmailAndPassword(mtextFieldEmail2.toString(), getMtextFieldPassword2.toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    db.updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    d.updateUI(null);
                                }

                                // ...
                            }
                        });*/


                Intent intent = new Intent(SignUpActivity.this, HomepageActivity.class);
                startActivity(intent);


            }
        });


    }


    public FirebaseUser updateUI(FirebaseUser user) {
        return user;

    }
}
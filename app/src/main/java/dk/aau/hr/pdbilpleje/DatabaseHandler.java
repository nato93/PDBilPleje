package dk.aau.hr.pdbilpleje;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHandler  {

    public static String emailCredentials, passwordCredentials;
    public static String UID;


    private FirebaseAuth mAuth;
    //private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();






}

package dk.aau.hr.pdbilpleje.Homepage.Profile;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import dk.aau.hr.pdbilpleje.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView  mNameTextview, mEmailtextview;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    public Button mChangeNameButton, mChangePasswordButton, mChangeEmailButton;
    private AlertDialog.Builder dialogBuilder, dialogBuilder2, dialogBuilderEmail;
    private AlertDialog dialog, dialog2, dialogEmail;
    private EditText mEditTextEmail, mEditTextEmail2, mEditTextName, mEditTextLastname, mEditTextNewPass, mEditTextNewPass2, mEditTextOldPass;
    public String firstName, lastName, newPass, newPass2, oldPass;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //updateUI(currentUser);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();



        //mProfileName = view.findViewById(R.id.textViewProfileName);
        mChangeNameButton = view.findViewById(R.id.changeNameButton);
        mChangePasswordButton= view.findViewById(R.id.changePasswordButton);
        mChangeEmailButton = view.findViewById(R.id.changeEmailButton);
        mNameTextview = view.findViewById(R.id.textViewName);
        mEmailtextview = view.findViewById(R.id.textViewEmail);

        mEmailtextview.setText(user.getEmail());
        mNameTextview.setText(user.getDisplayName());



        mChangeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeNameDialog();
            }
        });


        mChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordDialog();
            }
        });

        mChangeEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEmailDialog();
            }
        });

        return view;

    }


    public void changeNameDialog(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View newNamePopup = getLayoutInflater().inflate(R.layout.popup_profile_name, null);
        dialogBuilder.setView(newNamePopup);
        dialogBuilder.setPositiveButton("BEKRÆFT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firstName = mEditTextName.getText().toString();
                lastName = mEditTextLastname.getText().toString();
                if (!firstName.matches("") && !lastName.matches("")){

                    updateUserName(firstName,lastName);
                }
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();
        mEditTextName = dialog.findViewById(R.id.editTextFirstName);
        mEditTextLastname = dialog.findViewById(R.id.editTextLastName);

    }

    public void changeEmailDialog(){
        dialogBuilderEmail = new AlertDialog.Builder(getContext());
        final View newNamePopup = getLayoutInflater().inflate(R.layout.popup_profile_email, null);
        dialogBuilderEmail.setView(newNamePopup);
        dialogBuilderEmail.setPositiveButton("BEKRÆFT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mEditTextEmail.getText().toString().equals(mEditTextEmail2.getText().toString())){
                    updateUserEmail(mEditTextEmail.getText().toString());
                }
            }
        });
        dialogEmail = dialogBuilderEmail.create();
        dialogEmail.show();
        mEditTextEmail = dialogEmail.findViewById(R.id.editTextEmail);
        mEditTextEmail2 = dialogEmail.findViewById(R.id.editTextEmail2);
    }

    public void changePasswordDialog(){
        dialogBuilder2 = new AlertDialog.Builder(getContext());
        final View newPasswordPopup = getLayoutInflater().inflate(R.layout.popup_profile_password,null);
        dialogBuilder2.setView(newPasswordPopup);
        dialogBuilder2.setPositiveButton("BEKRÆFT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newPass = mEditTextNewPass.getText().toString();
                newPass2 = mEditTextNewPass2.getText().toString();
                oldPass = mEditTextOldPass.getText().toString();

                if (!newPass.isEmpty() && !newPass2.isEmpty() && !oldPass.isEmpty()){
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), oldPass);

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d(TAG, "User re-authenticated.");
                                    Toast.makeText(getContext(), "Password updated!", Toast.LENGTH_SHORT).show();
                                    updatePassword(newPass2);
                                }
                            });;
                }

            }
        });

        dialog2 = dialogBuilder2.create();
        dialog2.show();
        mEditTextOldPass = dialog2.findViewById(R.id.editTextOldPassword);
        mEditTextNewPass = dialog2.findViewById(R.id.editTextNewPassword);
        mEditTextNewPass2 = dialog2.findViewById(R.id.editTextNewPassword2);


    }

    public void updateUserEmail(final String email){
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                            Toast.makeText(getContext(), "Email has been updated successfully", Toast.LENGTH_SHORT).show();
                            mEmailtextview.setText(email);
                        }
                    }
                });
    }

    public void updateUserName(final String name, final String lastname){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name + " " + lastname)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "name updated.");
                            Toast.makeText(getContext(), "Your name has been updated", Toast.LENGTH_SHORT).show();
                            mNameTextview.setText(name + " " + lastname);
                        }
                    }
                });
    }

    public void updatePassword(final String pass2){
        user.updatePassword(pass2)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                            Toast.makeText(getContext(), "Password has been updated", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

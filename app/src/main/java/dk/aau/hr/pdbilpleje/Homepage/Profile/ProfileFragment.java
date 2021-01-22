package dk.aau.hr.pdbilpleje.Homepage.Profile;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dk.aau.hr.pdbilpleje.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private TextView mProfileName;
    private FirebaseAuth firebaseAuth;
    public Button mChangeNameButton, mChangePasswordButton;
    private AlertDialog.Builder dialogBuilder, dialogBuilder2;
    private AlertDialog dialog, dialog2;

   /* public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);
    }*/




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //FirebaseUser user = firebaseAuth.getCurrentUser();


        //mProfileName = view.findViewById(R.id.textViewProfileName);
        mChangeNameButton = view.findViewById(R.id.changeNameButton);
        mChangePasswordButton= view.findViewById(R.id.changePasswordButton);

        mChangeNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCarDialog();
            }
        });


        mChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCarDialog();
            }
        });

        return view;

    }

    public void createNewCarDialog(){
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View newCarPopup = getLayoutInflater().inflate(R.layout.popup_profile_name, null);
        dialogBuilder.setView(newCarPopup);
        dialogBuilder.setPositiveButton("BEKRÆFT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createNewCarDialog2();
            }
        });
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void createNewCarDialog2(){
        dialogBuilder2 = new AlertDialog.Builder(getContext());
        final View newCarPopup2 = getLayoutInflater().inflate(R.layout.popup_profile_password,null);
        dialogBuilder2.setView(newCarPopup2);
        dialogBuilder2.setPositiveButton("BEKRÆFT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

             //   cars.add(mEditTextModel.getText().toString());
            }
        });

        dialog2 = dialogBuilder2.create();
        dialog2.show();
       // mEditTextModel = dialog2.findViewById(R.id.editTextCarmodel);

    }

}

package dk.aau.hr.pdbilpleje.Homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import dk.aau.hr.pdbilpleje.FrontPageActivity;
import dk.aau.hr.pdbilpleje.R;

public class SettingsFragment extends Fragment {


    public Button mLogUdButton;
    public Switch mTwoFactorSwitch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
//aye

        mLogUdButton = view.findViewById(R.id.signOutButton);
        mTwoFactorSwitch = view.findViewById(R.id.twoFactorSwitch);




        final FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

        mTwoFactorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Boolean switchState = mTwoFactorSwitch.isChecked();


                if (switchState == true){
                    // set the 2fa to true in the database

                    Toast.makeText(getActivity(), "2-Faktor godkendelse er slået til!",
                            Toast.LENGTH_SHORT).show();


                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).update("twofactor", true);


                } else {
                    // set the 2fa to false in the database
                    Toast.makeText(getActivity(), "2-Faktor godkendelse er slået fra!",
                            Toast.LENGTH_SHORT).show();
                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance()
                            .getCurrentUser().getUid()).update("twofactor", false);
                }
            }
        });




        mLogUdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sign-out of the currently logged in user.

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), FrontPageActivity.class);
                startActivity(intent);
            }
        });








        return view;
    }
}

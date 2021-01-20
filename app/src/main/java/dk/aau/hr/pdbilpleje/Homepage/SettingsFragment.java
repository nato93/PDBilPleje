package dk.aau.hr.pdbilpleje.Homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import dk.aau.hr.pdbilpleje.FrontPageActivity;
import dk.aau.hr.pdbilpleje.LoginActivity;
import dk.aau.hr.pdbilpleje.R;

public class SettingsFragment extends Fragment {


    public TextView mLogUdTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
//aye

        mLogUdTextView = view.findViewById(R.id.logUdTextView);



        mLogUdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sign-out of the currently logged in user.



                Intent intent = new Intent(getActivity(), FrontPageActivity.class);
                startActivity(intent);
            }
        });








        return view;
    }
}

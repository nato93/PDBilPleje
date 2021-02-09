package dk.aau.hr.pdbilpleje.Homepage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseUser;

import dk.aau.hr.pdbilpleje.R;

public class HomepageFragment extends Fragment {

    public ImageView mNewsImageView, mNewsImageView2, mNewsImageView3;
    public ImageView mSettingsImageView;
    private final static String TAG = "HomePageFragment";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);


        //mSettingsImageView = view.findViewById(R.id.settingsView);

        Log.d(TAG, "Velkommen");


        return view;
    }

}
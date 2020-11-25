package dk.aau.hr.pdbilpleje.Homepage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import dk.aau.hr.pdbilpleje.R;

public class HomepageFragment extends Fragment {

    public ImageView mNewsImageView, mNewsImageView2;
    public ImageView mSettingsImageView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);

        mNewsImageView = view.findViewById(R.id.newsImageview);
        mNewsImageView2 = view.findViewById(R.id.newsImageview2);

        mSettingsImageView = view.findViewById(R.id.settingsView);


        //gaugeInfoText = view.findViewById(R.id.mInfoText);

        //gaugeInfoText = MainPageFragment.infoText;

        //CharSequence input = gaugeText.toString();
        //updateTextView(input);

        //gaugeText.setText(R.string.mCoInfo);
        //CharSequence input = gaugeText.getText();
        //updateTextView(gaugeText.toString());
        return view;
    }

}
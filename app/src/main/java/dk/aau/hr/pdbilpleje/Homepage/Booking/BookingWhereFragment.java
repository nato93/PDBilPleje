package dk.aau.hr.pdbilpleje.Homepage.Booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.MapView;

import dk.aau.hr.pdbilpleje.R;


public class BookingWhereFragment extends Fragment {

    private MapView mMapView;
    private Button mNextButton, mPreviousButton;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookingwhere, container, false);


        mMapView = view.findViewById(R.id.bookingMapView);
        mNextButton = view.findViewById(R.id.bookingNextButton);
        mPreviousButton = view.findViewById(R.id.bookingPreviousButton);





        return view;

    }
}

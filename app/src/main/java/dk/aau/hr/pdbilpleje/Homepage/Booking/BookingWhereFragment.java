package dk.aau.hr.pdbilpleje.Homepage.Booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.MapView;

import dk.aau.hr.pdbilpleje.R;


public class BookingWhereFragment extends Fragment {

    public MapView mMapView;
    private Button mBookingNextButton, mBookingPreviousButton;
    private Spinner mSpinnerWhereView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookingwhere, container, false);


        mMapView = view.findViewById(R.id.bookingMapView);
        mSpinnerWhereView = view.findViewById(R.id.spinnerWhereView);
        mBookingNextButton = view.findViewById(R.id.bookingNextButton);
        mBookingPreviousButton = view.findViewById(R.id.bookingPreviousButton);


        mBookingPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingFragment bookingFragment= new BookingFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.mainPageLayout, bookingFragment, bookingFragment.getTag())
                        .commit();
            }
        });

        mBookingNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingCalendarFragment bookingCalendarFragment= new BookingCalendarFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.mainPageLayout, bookingCalendarFragment, bookingCalendarFragment.getTag())
                        .commit();
            }
        });
        return view;
    }
}

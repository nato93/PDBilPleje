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

public class BookingCalendarFragment extends Fragment {


    public MapView mMapView;
    private Button mBookingNextButton, mBookingPreviousButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookingwhere, container, false);


        mMapView = view.findViewById(R.id.bookingMapView);
        mBookingNextButton = view.findViewById(R.id.bookingNextButton);
        mBookingPreviousButton = view.findViewById(R.id.bookingPreviousButton);


        mBookingPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingWhereFragment bookingWhereFragment= new BookingWhereFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.mainPageLayout, bookingWhereFragment, bookingWhereFragment.getTag())
                        .commit();
            }
        });

        mBookingNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingConfirmationFragment bookingConfirmationFragment = new BookingConfirmationFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.mainPageLayout, bookingConfirmationFragment, bookingConfirmationFragment.getTag())
                        .commit();
            }
        });
        return view;
    }
}

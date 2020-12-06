package dk.aau.hr.pdbilpleje.Homepage.Booking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import dk.aau.hr.pdbilpleje.R;


public class BookingFragment extends Fragment {

    public Spinner mServiceSpinner, mCarSpinner;
    public Button mBookingNextButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        mServiceSpinner = view.findViewById(R.id.spinnerServiceView);
        mCarSpinner = view.findViewById(R.id.spinnerCarView);
        mBookingNextButton = view.findViewById(R.id.bookingNextButton);



        mBookingNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingWhereFragment bookingWhereFragment= new BookingWhereFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.mainPageLayout, bookingWhereFragment, bookingWhereFragment.getTag())
                        .commit();
            }
        });

        return view;

    } // OnCreateView End
} //Class End

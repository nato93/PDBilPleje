package dk.aau.hr.pdbilpleje.Homepage.Booking;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import dk.aau.hr.pdbilpleje.R;





public class BookingConfirmationFragment extends Fragment {


    private Button mBookingPreviousButton;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookingconfirmation, container, false);



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

        return view;
    }

}

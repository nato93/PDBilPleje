package dk.aau.hr.pdbilpleje.Homepage.Booking;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.MapView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import dk.aau.hr.pdbilpleje.R;

public class BookingCalendarFragment extends Fragment {


    private Button mBookingNextButton, mBookingPreviousButton, mChooseDateButton;
    private TextView mTextViewDate;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookingcalendar, container, false);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("VÆLG EN DATO");
        CalendarConstraints.Builder calendarConstraintBuilder = new CalendarConstraints.Builder();
        calendarConstraintBuilder.setValidator(DateValidatorPointForward.now());
        materialDateBuilder.setCalendarConstraints(calendarConstraintBuilder.build());
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        mTextViewDate = view.findViewById(R.id.textViewDate);
        mChooseDateButton = view.findViewById(R.id.addDateButton);
        mBookingNextButton = view.findViewById(R.id.bookingNextButton2);
        mBookingPreviousButton = view.findViewById(R.id.bookingPreviousButton2);


        mChooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {

                        mTextViewDate.setText(materialDatePicker.getHeaderText());
                    }
                });

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

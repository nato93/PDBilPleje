package dk.aau.hr.pdbilpleje.Homepage.Booking;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import dk.aau.hr.pdbilpleje.MainActivity;
import dk.aau.hr.pdbilpleje.R;

public class BookingConfirmationFragment extends Fragment {

    private static final String TAG = "BookingConfirmationFrag";
    private Button mBookingPreviousButton, btnNext;
    private EditText edtName, edtPhone, edtWhere, edtPostalAddress, edtBil, edtEmail, edtTime;
    private String selService, selCar, selAddress, selDate;

    public BookingConfirmationFragment(String selService, String selCar, String selAddress, String selDate){
        this.selService = selService;
        this.selCar = selCar;
        this.selAddress = selAddress;
        this.selDate = selDate;
        Log.e(TAG, "BookingConfirmationFrag: " + selService + " - " +  selCar + " - " +  selAddress+ " - " +  selDate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookingconfirmation, container, false);
        initComponents(view);
        mBookingPreviousButton = view.findViewById(R.id.bookingPreviousButton);

        btnNext = view.findViewById(R.id.next_button);
        btnNext.setOnClickListener(view1 -> startActivity(new Intent(getActivity(), MainActivity.class)));

        mBookingPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingWhereFragment bookingWhereFragment = new BookingWhereFragment("", "");
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.mainPageLayout, bookingWhereFragment, bookingWhereFragment.getTag()).commit();
            }
        });
        return view;
    }

    private void initComponents(View view) {
        initWidgets(view);
        fillFields();
    }

    private void initWidgets(View view) {
        edtName = view.findViewById(R.id.edt_name);
        edtTime = view.findViewById(R.id.edt_time);
        edtEmail = view.findViewById(R.id.edt_email);
        edtPhone = view.findViewById(R.id.edt_phone);
        edtPostalAddress = view.findViewById(R.id.edt_postal_address);
        edtBil = view.findViewById(R.id.edt_bill);
        edtWhere = view.findViewById(R.id.edt_where);
    }

    private void fillFields(){
        edtWhere.setText(selAddress);
        edtTime.setText(selDate);
    }

}

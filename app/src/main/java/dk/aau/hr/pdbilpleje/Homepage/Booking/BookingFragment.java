package dk.aau.hr.pdbilpleje.Homepage.Booking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import dk.aau.hr.pdbilpleje.R;


public class BookingFragment extends Fragment {

    // public Spinner mServiceSpinner, mCarSpinner;
    public Button mBookingNextButton, mAddCarButton;
    private AlertDialog.Builder dialogBuilder, dialogBuilder2;
    private AlertDialog dialog, dialog2;
    private ArrayList<String> cars;
    private EditText mEditTextModel;
    protected TextView mTextViewType;
    private String selService, selCar;
    protected ImageButton mImageButton1, mImageButton2, mImageButton3, mImageButton4, mImageButton5, mImageButton6;
    private static final String TAG = "BookingFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        String[] services = new String[]{"CAP WASH", "NANO WASH", "BIG CAP WASH", "NORMAL WASH"};
        cars = new ArrayList<>();
        cars.add("Lambocap");
        cars.add("Toyota Corolla");
        mBookingNextButton = view.findViewById(R.id.NæsteButton);
        mAddCarButton = view.findViewById(R.id.addCarButton);

        mAddCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCarDialog();
            }
        });

        //  mNextButtonPopup = view.findViewById(R.id.nextButtonPopup);

        ArrayAdapter<String> service = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, services);

        AutoCompleteTextView editTextFilledExposedDropdownServices = view.findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdownServices.setAdapter(service);

        editTextFilledExposedDropdownServices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: position: " + i);
                Log.e(TAG, "onItemSelected: " + services[i]);
                selService = services[i];
            }
        });

//        editTextFilledExposedDropdownServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e(TAG, "onItemSelected: position: " + i);
//                Log.e(TAG, "onItemSelected: " + services[i]);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });

        ArrayAdapter<String> car = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, cars);
        AutoCompleteTextView editTextFilledExposedDropdownCars = view.findViewById(R.id.filled_exposed_dropdown2);
        editTextFilledExposedDropdownCars.setAdapter(car);
        editTextFilledExposedDropdownCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: position: " + i);
                Log.e(TAG, "onItemSelected: " + cars.get(i));
                selCar = cars.get(i);
            }
        });

//        editTextFilledExposedDropdownCars.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e(TAG, "onItemSelected: position: " + i);
//                Log.e(TAG, "onItemSelected: " + cars.get(i));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//            }
//        });

        mBookingNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // I need data here
                BookingWhereFragment bookingWhereFragment = new BookingWhereFragment(selService, selCar);
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.mainPageLayout, bookingWhereFragment, bookingWhereFragment.getTag()).commit();
            }
        });
        return view;
    } // OnCreateView End

    public void createNewCarDialog() {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View newCarPopup = getLayoutInflater().inflate(R.layout.popup_booking, null);
        dialogBuilder.setView(newCarPopup);
        dialogBuilder.setPositiveButton("NÆSTE >", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mTextViewType.toString().isEmpty()){
                    Toast.makeText(getContext(), "Vælg venligst din bil type!", Toast.LENGTH_LONG).show();
                } else {
                    createNewCarDialog2();
                }

            }
        });
        dialog = dialogBuilder.create();
        dialog.show();
        mTextViewType = dialog.findViewById(R.id.carType);
        mImageButton1 = dialog.findViewById(R.id.imageButton);
        mImageButton2 = dialog.findViewById(R.id.imageButton2);
        mImageButton3 = dialog.findViewById(R.id.imageButton3);
        mImageButton4 = dialog.findViewById(R.id.imageButton4);
        mImageButton5 = dialog.findViewById(R.id.imageButton5);
        mImageButton6 = dialog.findViewById(R.id.imageButton6);

        mImageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewType.setText("SEDAN");
            }
        });

        mImageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewType.setText("Coupe");
            }
        });

        mImageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewType.setText("Van");
            }
        });

        mImageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewType.setText("SUV");
            }
        });

        mImageButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewType.setText("BUS");
            }
        });

        mImageButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewType.setText("CABRIOLET");
            }
        });


    }

    public void createNewCarDialog2() {
        dialogBuilder2 = new AlertDialog.Builder(getContext());
        final View newCarPopup2 = getLayoutInflater().inflate(R.layout.popup2_booking, null);
        dialogBuilder2.setView(newCarPopup2);
        dialogBuilder2.setPositiveButton("TILFØJ BIL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cars.add(mEditTextModel.getText().toString());
            }
        });

        dialogBuilder2.setNegativeButton("TILBAGE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createNewCarDialog();
            }
        });
        dialog2 = dialogBuilder2.create();
        dialog2.show();
        mEditTextModel = dialog2.findViewById(R.id.editTextCarmodel);


    }
} //Class End

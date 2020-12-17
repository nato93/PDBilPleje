package dk.aau.hr.pdbilpleje.Homepage.Booking;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;

import dk.aau.hr.pdbilpleje.R;


public class BookingWhereFragment extends Fragment  {

    //implements OnMapReadyCallback

    public MapView mMapView;
    GoogleMap map;
    private Button mBookingNextButton, mBookingPreviousButton, mAddAddressButton;
    private Spinner mSpinnerWhereView;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ArrayList<String> addresses;
    private EditText mEditTextAddress, mEditTextPostalCode, mEditTextCity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookingwhere, container, false);

        addresses = new ArrayList<>();
        addresses.add("SANKT ANNÆ PLADS 32, -3");
        addresses.add("KBH Rådhuspladsen cap");
        mBookingNextButton = view.findViewById(R.id.NæsteButton);
        mAddAddressButton = view.findViewById(R.id.addAddressButton);
        //mMapView = view.findViewById(R.id.bookingMapView);
        //initGoogleMap(savedInstanceState);




        ArrayAdapter<String> place =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.dropdown_menu_popup_item,
                        addresses);

        AutoCompleteTextView editTextFilledExposedDropdownServices =
                view.findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdownServices.setAdapter(place);


        mBookingNextButton = view.findViewById(R.id.bookingNextButton);
        mBookingPreviousButton = view.findViewById(R.id.bookingPreviousButton);


        mBookingPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingFragment bookingFragment = new BookingFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.mainPageLayout, bookingFragment, bookingFragment.getTag())
                        .commit();
            }
        });

        mBookingNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookingCalendarFragment bookingCalendarFragment = new BookingCalendarFragment();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.mainPageLayout, bookingCalendarFragment, bookingCalendarFragment.getTag())
                        .commit();
            }
        });

        mAddAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCarDialog2();
            }
        });

        return view;
    }

    public void createNewCarDialog2() {
        dialogBuilder = new AlertDialog.Builder(getContext());
        final View newCarPopup2 = getLayoutInflater().inflate(R.layout.popup3_booking, null);
        dialogBuilder.setView(newCarPopup2);
        dialogBuilder.setPositiveButton("TILFØJ ADDR.", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dialog = dialogBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mEditTextAddress.getText().toString().equals("")) {
                            mEditTextAddress.setError("Venligst udfyld dette felt!");
                        } else if (mEditTextPostalCode.getText().toString().equals("")) {
                            mEditTextPostalCode.setError("Venligst udfyld dette felt!");
                        } else if (mEditTextCity.getText().toString().equals("")) {
                            mEditTextCity.setError("Venligst udfyld dette felt!");
                        } else {
                            addresses.add(mEditTextAddress.getText().toString() + ", "
                                    + mEditTextPostalCode.getText().toString() + " "
                                    + mEditTextCity.getText().toString());
                            dialog.dismiss();
                        }
                    }
                });

            }
        });
        dialog.show();
        mEditTextAddress = dialog.findViewById(R.id.editTextAddress);
        mEditTextPostalCode = dialog.findViewById(R.id.editTextPostalCode);
        mEditTextCity = dialog.findViewById(R.id.editTextCity);
    }

/*    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey");
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }*/


/*
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle("MapViewBundleKey");
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle("MapViewBundleKey", mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }*/
}





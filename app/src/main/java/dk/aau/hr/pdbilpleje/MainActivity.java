package dk.aau.hr.pdbilpleje;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import dk.aau.hr.pdbilpleje.Homepage.BookingEventCallback;
import dk.aau.hr.pdbilpleje.Homepage.Model.BookingEventModel;
import dk.aau.hr.pdbilpleje.Payment.Model.PaymentModel;
import dk.aau.hr.pdbilpleje.Payment.PaymentActivity;

public class MainActivity extends Activity implements BookingEventCallback {

    private static final String TAG = "MainActivity";
    com.google.api.services.calendar.Calendar mService;
    Button btnLaunch;
    private final int LAUNCH_PAYMENT_ACTIVITY = 12323;

    BookingEventModel bem;
    PaymentModel pm;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference eventsRef;
    DatabaseReference paymentRef;

    GoogleAccountCredential credential;

    private TextView mStatusText;
    private TextView mResultsText;

    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initLayout());

        initComponents();
        initListeners();

        Intent intent = new Intent(this, PaymentActivity.class);
        startActivityForResult(intent, LAUNCH_PAYMENT_ACTIVITY);

        // Initialize credentials and service object.
        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

        mService = new com.google.api.services.calendar.Calendar.Builder(transport, jsonFactory, credential)
                .setApplicationName("Pdbilpleje")
                .build();

    }

    private void initComponents() {

        initFbDbReferences();
        initGoogleSignIn();
    }

    private void initGoogleSignIn() {
    }

    private void initFbDbReferences() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        eventsRef = firebaseDatabase.getReference(Constants.EVENTS_FBDB_REF);
        paymentRef = firebaseDatabase.getReference(Constants.PAYMENT_FBDB_REF);
    }

    public LinearLayout initLayout() {

        LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mStatusText = new TextView(this);
        mStatusText.setLayoutParams(tlp);
        mStatusText.setTypeface(null, Typeface.BOLD);
        mStatusText.setText("Retrieving data...");
        activityLayout.addView(mStatusText);

        mResultsText = new TextView(this);
        mResultsText.setLayoutParams(tlp);
        mResultsText.setPadding(16, 16, 16, 16);
        mResultsText.setVerticalScrollBarEnabled(true);
        mResultsText.setMovementMethod(new ScrollingMovementMethod());
        activityLayout.addView(mResultsText);

        //Payment Button //
        btnLaunch = new Button(this);
        btnLaunch.setText("Launch Payment");
        btnLaunch.setLayoutParams(tlp);
        btnLaunch.setPadding(16, 16, 16, 16);
        activityLayout.addView(btnLaunch);

        btnLaunch.setVisibility(View.GONE);
        mResultsText.setVisibility(View.GONE);
        mStatusText.setVisibility(View.GONE);

        return activityLayout;

    }

    private void initListeners() {

//        btnLaunch.setOnClickListener(view -> {
//            Toast.makeText(this, "Clicked ${videoModel.name}", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(this, PaymentActivity.class);
//            startActivityForResult(intent, LAUNCH_PAYMENT_ACTIVITY);
//        });

    }

    private void log(String m) {
        Log.e(TAG, m);
    }

    private void savePaymentOnline(PaymentModel p, BookingEventModel be) {

        final String randomKey = paymentRef.push().getKey();

        assert randomKey != null;
        paymentRef.child(randomKey).setValue(p)
                .addOnCompleteListener(task -> {
                    log("savePaymentOnline() onComplete");
                    if (task.isSuccessful()) {
                        log("savePaymentOnline() onComplete: task success");
                        // Payment saved online
                        Log.e(TAG, "savePaymentOnline: " + be.getDate() + be.getTitle() + " - " + randomKey);
                        saveBookingEventOnline(be, randomKey);
                    } else {
                        log("savePaymentOnline() onComplete: task failure");
                    }
                }).addOnFailureListener(e -> log("savePaymentOnline() onFailure"));
//                .addOnCanceledListener(() -> log("savePaymentOnline() onCanceled"));
    }

    private void saveBookingEventOnline(BookingEventModel be, String randomKey) {

        this.bem.setBookinEventID(randomKey);

        assert randomKey != null;
        eventsRef.child(randomKey).setValue(be)
                .addOnCompleteListener(task -> {
                    log("saveBookingEventOnline() onComplete");
                    if (task.isSuccessful()) {
                        log("saveBookingEventOnline() onComplete: task success");
                        // Booking event saved online

                    } else {
                        log("saveBookingEventOnline() onComplete: task failure");
                    }
                }).addOnFailureListener(e -> log("saveBookingEventOnline() onFailure"));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        if (isGooglePlayServicesAvailable()) {
            refreshResults();
        } else {
            mStatusText.setText("Google Play Services required: " + "after installing, close and relaunch this app.");
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == RESULT_OK) {
                    refreshResults();
                } else {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                        refreshResults();
                        if (pm != null) {
                            Log.e(TAG, "onActivityResult After selecting account: Payment not null");
                            new ApiAsyncTask(this, this).execute();
                        } else {
                            Log.e(TAG, "onActivityResult After selecting account: Payment is null");
                        }

                    }
                } else if (resultCode == RESULT_CANCELED) {
                    mStatusText.setText("Account unspecified.");
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    refreshResults();
                } else {
//                    chooseAccount();
                }
                break;
            case LAUNCH_PAYMENT_ACTIVITY:
                if (resultCode == Activity.RESULT_OK) {
                    int result = data.getIntExtra("result", -1);
                    if (result == 1) {
                        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
                        PaymentModel paymentModel = (PaymentModel) data.getSerializableExtra("paymentModel");
                        Log.e(TAG, "onActivityResult: PAYMENT :: "
                                + paymentModel.getDateAdded()
                                + " - " + paymentModel.getAmount()
                                + " - " + paymentModel.getCard()
                                + " - " + paymentModel.getPaymentReference()
                        );
                        if (isDeviceOnline()) {
                            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                            if (accountName != null) {
                                Log.e(TAG, "onActivityResult: device was online");
                                Log.e(TAG, "onActivityResult: accountName is null");
                                chooseAccount();
                            } else {
                                if (credential != null) {
                                    paymentModel.setUserId(credential.getSelectedAccountName());
                                    this.pm = paymentModel;
                                    Log.e(TAG, "onActivityResult: getSelectedAccountName = " + credential.getSelectedAccountName());
                                    Log.e(TAG, "onActivityResult: credentials was not null");
                                    Log.e(TAG, "onActivityResult: device was online");
                                    Log.e(TAG, "onActivityResult: accountName is not null");
                                    new ApiAsyncTask(this, this).execute();
                                } else {
                                    Log.e(TAG, "onActivityResult: credentials is null");
                                    Log.e(TAG, "onActivityResult: device is online");
                                    Log.e(TAG, "onActivityResult: accountName is not null");
                                    chooseAccount();
                                }
                            }
                        }
                    } else {
                        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refreshResults() {
        if (credential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
//                new ApiAsyncTask(this,this).execute();
            } else {
                mStatusText.setText("No network connection available.");
            }
        }
    }


    public void clearResultsText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusText.setText("Retrieving data…");
                mResultsText.setText("");
            }
        });
    }

    public void updateResultsText(final List<String> dataStrings) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataStrings == null) {
                    mStatusText.setText("Error retrieving data!");
                } else if (dataStrings.size() == 0) {
                    mStatusText.setText("No data found.");
                } else {
                    mStatusText.setText("Data retrieved using" + " the Google Calendar API:");
                    mResultsText.setText(TextUtils.join("\n\n", dataStrings));
                }
            }
        });
    }

    public void updateStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStatusText.setText(message);
            }
        });
    }

    private void chooseAccount() {
        startActivityForResult(
                credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    void showGooglePlayServicesAvailabilityErrorDialog(final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionStatusCode,
                        MainActivity.this,
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

    @Override
    public void onBookingEventReceive(BookingEventModel bem) {
        final Dialog[] dialog = new Dialog[1];
        runOnUiThread(() -> {
            dialog[0] = new Dialog(this);
            dialog[0].requestWindowFeature(Window.FEATURE_NO_TITLE); // before
            dialog[0].setContentView(R.layout.layout_main);
            dialog[0].getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog[0].setCancelable(true);
            dialog[0].show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog[0].dismiss();
                    Log.e(TAG, "run: before finish()");
                    finish();
                    Log.e(TAG, "run: after finish()");
                }
            }, 5000);
        });

        Log.e(TAG, "onBookingEventReceive: AFTER ASYNC CALLBACK IS CALLED");
        Log.e(TAG, "onBookingEventReceive() ");

        this.bem = bem;
        if (pm != null) {

            bem.setUserId(pm.getUserId());
            savePaymentOnline(pm, bem);
        } else {
            Log.e(TAG, "onBookingEventReceive: pm is null");
        }

    }

}
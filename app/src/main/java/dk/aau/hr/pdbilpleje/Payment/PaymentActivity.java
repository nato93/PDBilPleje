package dk.aau.hr.pdbilpleje.Payment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.model.StripeIntent;
import com.stripe.android.view.CardMultilineWidget;

import org.jetbrains.annotations.NotNull;

import dk.aau.hr.pdbilpleje.Payment.Model.PaymentModel;
import dk.aau.hr.pdbilpleje.Payment.Network.MyApiClient;
import dk.aau.hr.pdbilpleje.Payment.Network.MyApiService;
import dk.aau.hr.pdbilpleje.Payment.Utils.UtilsFunc;
import dk.aau.hr.pdbilpleje.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";
    TextView tvSubtotal, tvTotal;
    String paymentIntentClientSecret;
    CardMultilineWidget cardInputWidget;
    Stripe stripe;
    int AMOUNT = 60;
    MyApiService paymentApi;
    Toolbar toolbar;
    ProgressDialog progress;
    boolean isSuccessful = false;
    PaymentModel paymentModel;
    MyApiClient retrofit;
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        init();
        getToken();
    }

    private void init() {
        tvSubtotal = findViewById(R.id.sub_total);
        tvTotal = findViewById(R.id.total);
        cardInputWidget = findViewById(R.id.cardInputWidget);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> goBack(view));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tvSubtotal.setText(AMOUNT + "$");
        tvTotal.setText(AMOUNT + "$");
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait ..");
        progress.setCancelable(false);
        PaymentConfiguration.init(
                this,
                PaymentConstants.PUBLISH_KEY
        );
    }

    private void getToken() {

        progress.show();

        paymentApi = MyApiClient.getInstance().create(MyApiService.class);
        disposable = paymentApi.getClientKey(PaymentConstants.API_KEY, AMOUNT).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((s -> runOnUiThread(() -> {
                    Log.e("CODE", s);
                    progress.dismiss();
                    paymentIntentClientSecret = s;
                })), t ->
                {
                    paymentIntentClientSecret = null;
                    t.printStackTrace();
                    Toast.makeText(PaymentActivity.this, "Error Occurred : " + t.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    public void payNow(View view) {
        if (UtilsFunc.isNetworkConnected(this)) {
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (paymentIntentClientSecret != null) {
                if (params != null) {
                    progress.show();
                    ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                            .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                    stripe = new Stripe(this, PaymentConfiguration.getInstance(this).getPublishableKey());
                    stripe.confirmPayment(this, confirmParams);
                }
            } else {
                Toast.makeText(this, "An Error Occurred!", Toast.LENGTH_LONG);
            }
        } else {
            Toast.makeText(this, "Network Required!", Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        stripe.onPaymentResult(requestCode, data, new ApiResultCallback<PaymentIntentResult>() {
            @Override
            public void onSuccess(PaymentIntentResult result) {
                PaymentIntent paymentIntent = result.getIntent();
                StripeIntent.Status status = paymentIntent.getStatus();
                progress.dismiss();
                if (status == StripeIntent.Status.Succeeded) {
                    Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onSuccess: PAYMENT SUCCESSFUL");
                    paymentModel = new PaymentModel();
                    paymentModel.setAmount(AMOUNT);
                    paymentModel.setPaymentReference(paymentIntent.getId());
                    paymentModel.setDateAdded(UtilsFunc.getDateTime());
                    paymentModel.setUserId("userId");
                    paymentModel.setCard("**** **** **** " + paymentIntent.getPaymentMethod().card.last4);
                    isSuccessful = true;


                    goBack(null);
                } else {
                    Log.e(TAG, "Payment failed: ");
                    Toast.makeText(PaymentActivity.this, "Payment failed", Toast.LENGTH_LONG).show();
                    isSuccessful = false;
                }
            }

            @Override
            public void onError(@NotNull Exception e) {
                Toast.makeText(PaymentActivity.this, "Payment Failed : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("PAYMENT", e.toString());
                progress.dismiss();
                isSuccessful = false;
            }
        });
    }


    void goBack(View view) {
        if (isSuccessful) {
            resultOk();
        } else
            resultCancel();
    }

    void resultOk() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", 1);
        returnIntent.putExtra("paymentModel", paymentModel);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    void resultCancel() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goBack(null);
    }

    @Override
    public boolean onNavigateUp() {
        goBack(null);
        return true;
    }
}
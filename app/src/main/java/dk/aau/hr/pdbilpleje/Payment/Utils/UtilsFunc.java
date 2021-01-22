package dk.aau.hr.pdbilpleje.Payment.Utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilsFunc {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}

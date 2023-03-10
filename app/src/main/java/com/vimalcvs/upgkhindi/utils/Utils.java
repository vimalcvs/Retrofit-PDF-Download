package com.vimalcvs.upgkhindi.utils;

import static com.vimalcvs.upgkhindi.Config.IS_TRIAL;
import static com.vimalcvs.upgkhindi.utils.Constant.FALSE;
import static com.vimalcvs.upgkhindi.utils.Constant.ZERO;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import java.util.List;
import java.util.Map;

public class Utils {

    public static boolean isEmpty(final Object s) {
        if (s == null) {
            return true;
        }
        if ((s instanceof String) && (((String) s).trim().length() == ZERO)) {
            return true;
        }
        if (s instanceof Map) {
            return ((Map<?, ?>) s).isEmpty();
        }
        if (s instanceof List) {
            return ((List<?>) s).isEmpty();
        }
        if (s instanceof Object[]) {
            return (((Object[]) s).length == ZERO);
        }
        return FALSE;
    }

    public static void getErrors(final Exception e) {
        final String stackTrace = "Prashant ::" + Log.getStackTraceString(e);
        Utils.sout("" + stackTrace);
    }

    public static void sout(final String msg) {
        if (IS_TRIAL) {
            System.out.println("Prashant :: " + msg);
        }
    }


    public static boolean isOnline(final Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
                }
            }
        } else {
            final NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        return FALSE;
    }


}

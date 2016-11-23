package com.appunite.mialarm.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


public class PermissionHelper {

    public static boolean hasPermissions(final Context context, @NonNull String[] permissions) {
        boolean hasRequiredPermissions = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                hasRequiredPermissions = false;
                break;
            }
        }
        return hasRequiredPermissions;
    }

    public static void checkPermissionsAndAsk(final Activity activity, final int requestCode,
                                              @NonNull String[] permissions) {
        if (!hasPermissions(activity, permissions)) {
            ActivityCompat.requestPermissions(activity,
                    permissions,
                    requestCode);
        }

    }
}

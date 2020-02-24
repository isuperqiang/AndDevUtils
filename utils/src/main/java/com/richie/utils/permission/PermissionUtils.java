package com.richie.utils.permission;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * EasyPermissons 封装
 *
 * @author Richie on 2019.01.09
 */
public final class PermissionUtils {
    private static final int REQUEST_CODE = 1024;
    private static final String TAG = "PermissionUtils";

    private PermissionUtils() {
    }

    public static void requestPermissions(@NonNull Activity host, @Size(min = 1) @NonNull String... perms) {
        EasyPermissions.requestPermissions(host, "亲，请授予必要的权限哦～", REQUEST_CODE, perms);
    }

    public static void requestPermissions(@NonNull Activity host, @NonNull String rationale, int requestCode,
                                          @Size(min = 1) @NonNull String... perms) {
        EasyPermissions.requestPermissions(host, rationale, requestCode, perms);
    }

    public static boolean hasPermissions(@NonNull Context context, @Size(min = 1) @NonNull String... perms) {
        return EasyPermissions.hasPermissions(context, perms);
    }

    public static void checkPermissions(@NonNull Activity host, @NonNull String rationale,
                                        int requestCode, @Size(min = 1) @NonNull String... perms) {
        if (!hasPermissions(host, perms)) {
            requestPermissions(host, rationale, requestCode, perms);
        }
    }

    public static void checkPermissions(@NonNull Activity host, @Size(min = 1) @NonNull String... perms) {
        if (!hasPermissions(host, perms)) {
            requestPermissions(host, perms);
        }
    }

    public static void onRequestPermissionsResult(@NonNull Activity host, int requestCode, @NonNull String[] permissions,
                                                  @NonNull int[] grantResults, @Nullable PermissionCallback permissionCallback) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,
                new InnerPermissionCallback(permissionCallback, host));
    }

    private static class InnerPermissionCallback implements EasyPermissions.PermissionCallbacks,
            EasyPermissions.RationaleCallbacks {
        PermissionCallback mPermissionCallback;
        Activity mHost;

        InnerPermissionCallback(PermissionCallback permissionCallback, Activity host) {
            mPermissionCallback = permissionCallback;
            mHost = host;
        }

        @Override
        public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
            Log.d(TAG, "onPermissionsGranted() called with: requestCode = [" + requestCode + "], perms = [" + perms + "]");
            if (mPermissionCallback != null) {
                mPermissionCallback.onPermissionsGranted(perms, requestCode);
            }
        }

        @Override
        public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
            Log.d(TAG, "onPermissionsDenied() called with: requestCode = [" + requestCode + "], perms = [" + perms + "]");
            if (mPermissionCallback != null) {
                mPermissionCallback.onPermissionsDenied(perms, requestCode);
            }

            boolean permanentDenied = false;
            for (String perm : perms) {
                if (EasyPermissions.permissionPermanentlyDenied(mHost, perm)) {
                    permanentDenied = true;
                    break;
                }
            }
            if (permanentDenied) {
                new AlertDialog.Builder(mHost)
                        .setMessage("亲，请手动打开权限哦～")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                PermissionSettingPage.start(mHost, true);
                            }
                        })
                        .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mHost.finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            } else {
                String[] denied = new String[]{};
                requestPermissions(mHost, perms.toArray(denied));
            }
        }

        @Override
        public void onRequestPermissionsResult(int i, @NonNull String[] strings, @NonNull int[] ints) {
            Log.d(TAG, "onRequestPermissionsResult() called with: i = [" + i + "], strings = [" + strings + "], ints = [" + ints + "]");
        }

        @Override
        public void onRationaleAccepted(int requestCode) {
            Log.d(TAG, "onRationaleAccepted() called with: requestCode = [" + requestCode + "]");
        }

        @Override
        public void onRationaleDenied(int requestCode) {
            Log.d(TAG, "onRationaleDenied() called with: requestCode = [" + requestCode + "]");
        }
    }

    public abstract class PermissionCallback {
        /**
         * 授予权限
         *
         * @param permissions
         * @param requestCode
         */
        protected void onPermissionsGranted(List<String> permissions, int requestCode) {
        }

        /**
         * 拒绝权限
         *
         * @param permissions
         * @param requestCode
         */
        protected void onPermissionsDenied(List<String> permissions, int requestCode) {
        }
    }
}

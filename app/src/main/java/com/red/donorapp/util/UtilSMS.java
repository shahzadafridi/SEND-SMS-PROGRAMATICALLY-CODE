package com.red.donorapp.util;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.widget.Toast;

import java.util.List;

public class UtilSMS {

    public static int SMS_PERMISSION_CODE = 1111;
    static AlertDialog alert = null;

    static boolean isSIM1 = false;

    public static boolean checkPermission(Context context){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }else {
            return true;
        }
    }

    public static void requestPermission(Context context){
        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, SMS_PERMISSION_CODE);
    }

    public static void showDialog(List localList, Context context, String phone, String message, PendingIntent sentPI, PendingIntent deliveredPI){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Select sim")
                .setCancelable(true)
                .setPositiveButton("SIM 2", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                            setIsSIM1(false);
                            //SendSMS From SIM Two
                            SubscriptionInfo simInfo2 = (SubscriptionInfo) localList.get(1);
                            SmsManager.getSmsManagerForSubscriptionId(simInfo2.getSubscriptionId()).sendTextMessage(phone, null, message, sentPI, deliveredPI);
                            if (alert != null)
                                alert.dismiss();
                        }
                    }
                })
                .setNegativeButton("SIM 1", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                            setIsSIM1(true);
                            //SendSMS From SIM One
                            SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(0);
                            SmsManager.getSmsManagerForSubscriptionId(simInfo1.getSubscriptionId()).sendTextMessage(phone, null, message, sentPI, deliveredPI);
                            if (alert != null)
                                alert.dismiss();
                        }
                    }
                });
        alert = builder.create();
        alert.show();
    }

    public static void sendSMS(Context context, String phone, String message, PendingIntent sentPI,  PendingIntent deliveredPI){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (UtilSMS.checkPermission(context)) {
                SubscriptionManager localSubscriptionManager = SubscriptionManager.from(context);
                if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                    List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                    showDialog(localList,context,phone,message, sentPI,deliveredPI);

                }
            }else {
                UtilSMS.requestPermission(context);
            }
        } else {
            SmsManager.getDefault().sendTextMessage(phone, null, message, sentPI, deliveredPI);
            Toast.makeText(context, "sending sms", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isIsSIM1() {
        return isSIM1;
    }

    public static void setIsSIM1(boolean isSIM1) {
        UtilSMS.isSIM1 = isSIM1;
    }


}

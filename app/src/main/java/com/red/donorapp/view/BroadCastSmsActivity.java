package com.red.donorapp.view;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;
import com.red.donorapp.R;
import com.red.donorapp.util.UtilSMS;

public class BroadCastSmsActivity extends AppCompatActivity {

    String SENT = "SMS_SENT", DELIVERED = "SMS_DELIVERED";
    PendingIntent sentPI;
    PendingIntent deliveredPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_cast_sms);
        sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
        UtilSMS.sendSMS(this,"+923339218035","This is test message from Shahzad Afridi",sentPI,deliveredPI);
    }

    // SEND BroadcastReceiver
    BroadcastReceiver sendSMS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "SMS sent",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(getBaseContext(), "Generic failure",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(getBaseContext(), "No service",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(getBaseContext(), "Null PDU",
                            Toast.LENGTH_SHORT).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(getBaseContext(), "Radio off",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    // DELIVERY BroadcastReceiver
    BroadcastReceiver deliverSMS = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(getBaseContext(), "SMS delivered",
                            Toast.LENGTH_SHORT).show();
                    break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getBaseContext(), "SMS not delivered",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UtilSMS.SMS_PERMISSION_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            UtilSMS.sendSMS(this,"+923339218035","This is test message from Shahzad Afridi",sentPI,deliveredPI);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(sendSMS, new IntentFilter(SENT));
        registerReceiver(deliverSMS, new IntentFilter(DELIVERED));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(sendSMS);
        unregisterReceiver(deliverSMS);
    }
}

package com.example.check_phone_state;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class outgoingnumber extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        SharedPreferences    sharedPreferences = context.getSharedPreferences("State1", Context.MODE_PRIVATE);
        SharedPreferences.Editor   editor = sharedPreferences.edit();
        editor.putString("outgoingno", number);
        editor.commit();



    }
}

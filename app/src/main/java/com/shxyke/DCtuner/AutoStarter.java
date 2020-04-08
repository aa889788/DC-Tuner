package com.shxyke.DCtuner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class AutoStarter extends BroadcastReceiver {

    private SharedPreferences sharedPreferences;
    public AutoStarter()
    {
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            sharedPreferences = context.getSharedPreferences("share", context.MODE_PRIVATE);
            if(sharedPreferences.getBoolean(context.getString(R.string.auto_run_status),false)){
                Utilties.set_dc_status(true);
                Utilties.update_status(context);
            }
        }
    }
}

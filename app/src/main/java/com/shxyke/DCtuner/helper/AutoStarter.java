package com.shxyke.DCtuner.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.shxyke.DCtuner.Contents;
import com.shxyke.DCtuner.utils.SPUtils;

import java.util.Objects;

public class AutoStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.requireNonNull(intent.getAction()).equals("android.intent.action.BOOT_COMPLETED")) {
            if(!Utilties.haveRoot() || !Utilties.isDCKernel()){
                return;
            }
            if((boolean) SPUtils.getData(Contents.SP_ON_BOOT_KEY,false)) {
                Utilties.set_elvss_num((Integer)SPUtils.getData(Contents.SP_ELVSS_MIN_KEY,66));
                Utilties.set_dc_status(true);
                if(Utilties.isColorModKernel()){
                    if((boolean)SPUtils.getData(Contents.SP_SRGB_MODE_STATUS_KEY, false)){
                        Utilties.set_srgb_status(true);
                    }
                    if((boolean)SPUtils.getData(Contents.SP_DCIP3_MODE_STATUS_KEY, false)){
                        Utilties.set_dci_p3_status(true);
                    }
                }
                Utilties.update_status();
            }
        }
    }
}

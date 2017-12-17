package com.mobiwarez.laki.seapp.messaging;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");

        Intent push = new Intent(context, PushIntentService.class);
        push.putExtras(getResultExtras(true));
        context.startService(push);
        setResultCode(Activity.RESULT_OK);
    }
}

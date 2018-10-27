package com.clipsync.bit_by_bit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "Hello", Toast.LENGTH_SHORT).show();
        //context.startService(new Intent(context, Copyservice.class));
        //Dialog dialog = new Dialog();
        //dialog.show();
        String data = intent.getStringExtra("copied_text");
        Log.d("ABHI", ""+data);
        //context.startActivity(new Intent(context, Fragment_adaptor.class));
        Intent intent1 = new Intent(context, Fragment_adaptor.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("copied_text", data);
        context.startActivity(intent1);
    }
}

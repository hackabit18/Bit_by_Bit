package com.clipsync.bit_by_bit;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Copyservice extends Service {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private boolean in_or_out = true;

    private OnPrimaryClipChangedListener listener = new OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            Log.d("ABHI", "Triggered");
            performClipboardCheck();
        }
    };

    private void performClipboardCheck() {
        ClipboardManager cb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (cb.hasPrimaryClip()) {
            ClipData cd = cb.getPrimaryClip();
            //Log.d("ABHI", cd.getItemAt(0).getText().toString());
            //Toast.makeText(this, ""+(cd.getItemAt(0).getText()).toString(), Toast.LENGTH_SHORT).show();
//            if (cd.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
//                Log.d("ABHI", cd.getItemAt(0).getText().toString());
//                Toast.makeText(this, ""+(cd.getItemAt(0).getText()).toString(), Toast.LENGTH_SHORT).show();
//            }
//            Intent intent = new Intent(this, Swipe_view.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
            if (cd.toString() != null && !TextUtils.isEmpty(cd.toString())) {

                if (in_or_out) {
                    Intent intent = new Intent("com.prototype.prototype");
                    intent.putExtra("copied_text", cd.getItemAt(0).getText().toString());
                    sendBroadcast(intent);
                    Log.d("ABHI", ""+(cd.getItemAt(0).getText()).toString());
                } else {
                    in_or_out = true;
                }

            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).addPrimaryClipChangedListener(listener);
        clip_service();
    }

    private void clip_service() {
        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("post").limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    String msg = child.child("data").getValue(String.class);
                    String id = child.child("device_id").getValue(String.class);
                    //Log.d("ABHI", "data added");

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", msg);
                    clipboard.setPrimaryClip(clip);
                    in_or_out = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        return START_STICKY;
    }

    
}

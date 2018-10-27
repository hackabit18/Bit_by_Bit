package com.clipsync.bit_by_bit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class Fragment_adaptor extends AppCompatActivity implements Dialog.inter_face {

    Dialog dialog;
    String text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_sheet);
        if (getIntent().getStringExtra("copied_text") == null){
            //dialog.dismiss();
            //finish();
        }else {
            text = getIntent().getStringExtra("copied_text");
        }
        if (savedInstanceState != null){
            finish();
        }

        dialog = new Dialog();
        Bundle bundle = new Bundle();
        bundle.putString("params", text);
        // set MyFragment Arguments
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "Dialog");

//        Intent intent = new Intent("com.copied_text");
//        intent.putExtra("copied_text", text);
//        sendBroadcast(intent);
//        Log.d("ABHI", "fdgd"+text);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                dialog.dismiss();
//                finish();
//            }
//        },3000);
    }

    @Override
    public void call() {
        //Toast.makeText(this, "des", Toast.LENGTH_SHORT).show();
        if (dialog != null){
            dialog.dismiss();
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (dialog.isVisible()){
            dialog.dismiss();
            finish();
        }else {
            super.onBackPressed();
        }
    }
}

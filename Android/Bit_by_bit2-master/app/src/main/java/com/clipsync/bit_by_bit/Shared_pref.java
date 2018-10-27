package com.clipsync.bit_by_bit;

import android.content.Context;
import android.content.SharedPreferences;

public class Shared_pref {

    private static volatile Shared_pref shared_prefs;
    Context context;
    SharedPreferences sharedPreferences, login_attempts;
    SharedPreferences.Editor editor, attempts;

    public Shared_pref(Context context){
        this.context = context;
        prepare_sharedprefs();
    }

//    public static Shared_prefs getInstance(){
//        if (shared_prefs == null){
//            synchronized (Shared_prefs.class){
//                if (shared_prefs == null){
//                    shared_prefs = new Shared_prefs();
//                }
//            }
//        }
//        return shared_prefs;
//    }

    public void prepare_sharedprefs(){
        sharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();

//        editor.putInt(Utils.username_file, 12345);
//        editor.putInt(Utils.password_file, 12345);
//        editor.putInt(Utils.email_file, 12345);
//        editor.putInt(Utils.displayname_file, 12345);
//        editor.commit();
    }

    public void put_email(String email, String name, String photourl){
        editor.putString("email", email);
        editor.putString("name", name);
        editor.putString("photourl", photourl);
        editor.commit();
    }

    public String get_email(){
        return sharedPreferences.getString("email","null");
    }

    public String get_name(){
        return sharedPreferences.getString("name","null");
    }

    public String get_photo(){
        return sharedPreferences.getString("photourl","null");
    }

    public void put_deviceid(String id){
        editor.putString("device_id", id);
        editor.commit();
    }

    public String get_deviceid(){
        return sharedPreferences.getString("device_id", "Android");
    }

    public void set_session(){
        editor.putBoolean("session", true);
        editor.commit();
    }

    public boolean get_session(){
        return sharedPreferences.getBoolean("session", false);
    }

    public void reset_session(){
        editor.putBoolean("session", false);
        editor.remove("email");
        editor.remove("name");
        editor.remove("photourl");
        editor.commit();
    }

}

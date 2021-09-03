package dresta.putra.wargakita;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import dresta.putra.wargakita.kontak.KontakPojo;
import dresta.putra.wargakita.user.UserPojo;


public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
     static Context _context;
     private static final String USERNAME = "username";
     private static final String DEVICE_ID = "device_id";
     private static final String PASSWORD = "password";
     private static final String LAST_LOGIN = "LAST_LOGIN";
     private static final String NO_PEGAWAI = "no_pegawai";
     private static final String ID_USER = "id_user";
     private static final String NAMA_LENGKAP = "nama_lengkap";
     private static final String ID_KOPERASI = "id_koperasi";
     private static final String NO_HP = "no_hp";
     private static final String EMAIL = "email";
     private static final String ALAMAT = "alamat";
     private static final String KECAMATAN = "kecamatan";
     private static final String KABUPATEN = "id_user";
     private static final String PROVINSI = "provinsi";
     private static final String WARGA_NEGARA = "warga_negara";
     private static final String STATUS = "status";
     private static final String TGL_BERGABUNG = "tgl_bergabung";
     private static PrefManager mInstance;
     private UserPojo userPojo;
     private KontakPojo kontakPojo;
//     offline mode TAG
    private static final String USERPOJO = "userpojo";
    private static final String KONTAKPOJO = "kontakpojo";

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "artakita";

    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void storeUserName(String names) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, names);
        editor.commit();
    }
    public void storeDataUser(String id_user,String username, String password) {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ID_USER, id_user);
        editor.putString(USERNAME, username);
        editor.putLong(LAST_LOGIN,System.currentTimeMillis()/ 1000L);
        editor.putString(PASSWORD,password);
        editor.commit();
    }
    //find logged in user
    public Long getLastLogin() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(LAST_LOGIN, 0);
    }

    public Long getTokenExpiredTime(){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        Long exp = sharedPreferences.getLong(LAST_LOGIN, 0);
        return (exp+86400);
    }
    //find logged in user
    public String getPassword() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(PASSWORD, null);
    }



    public boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        //tambah 24 jam
        long expiredToken = getLastLogin() + 86400;
        if ((System.currentTimeMillis()/ 1000L)>expiredToken && LoggedInIdUser()!=null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            setFirstTimeLaunch(false);
            return false;
        }else{
            return sharedPreferences.getString(ID_USER, null) != null;
        }
    }



    //find logged in user
    public String LoggedInIdUser() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ID_USER, null);
    }
    public String LoggedInUserUsername() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USERNAME, null);
    }
    public String getDeviceId(){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(DEVICE_ID, null);

    }
    public  void setDeviceId(String deviceId){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEVICE_ID, deviceId);
        editor.commit();
    }


    //Logout user
    public void logout() {
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        setFirstTimeLaunch(false);
        _context.startActivity(new Intent(_context, WelcomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
    public void setUserPojo(UserPojo userPojoP){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<UserPojo>(){}.getType();
        editor.putString(USERPOJO, gson.toJson(userPojoP,type));
        editor.commit();

    }
    
    public UserPojo getUserPojo(){
        userPojo = new UserPojo();
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String serializedObject = sharedPreferences.getString(USERPOJO, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<UserPojo>(){}.getType();
            userPojo = gson.fromJson(serializedObject, type);
        }
        return  userPojo;
    }
    public void setKontakpojo(KontakPojo kontakPojoP){
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        Type type = new TypeToken<KontakPojo>(){}.getType();
        editor.putString(KONTAKPOJO, gson.toJson(kontakPojoP,type));
        editor.commit();

    }

    public KontakPojo getKontakPojo(){
        kontakPojo = new KontakPojo();
        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String serializedObject = sharedPreferences.getString(KONTAKPOJO, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<KontakPojo>(){}.getType();
            kontakPojo = gson.fromJson(serializedObject, type);
        }
        return  kontakPojo;
    }

}

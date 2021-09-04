package dresta.putra.wargakita.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import dresta.putra.wargakita.AboutActivity;
import dresta.putra.wargakita.ResponsePojo;
import dresta.putra.wargakita.chat.java.ui.activity.DialogsActivity;
import dresta.putra.wargakita.chat.java.utils.ErrorUtils;
import dresta.putra.wargakita.chat.java.utils.SharedPrefsHelper;
import dresta.putra.wargakita.chat.java.utils.chat.ChatHelper;
import dresta.putra.wargakita.chat.java.utils.qb.QbUsersHolder;
import dresta.putra.wargakita.daftar.DaftarActivity;
import dresta.putra.wargakita.user.UserPojo;
import dresta.putra.wargakita.MainActivity;
import dresta.putra.wargakita.PrefManager;
import dresta.putra.wargakita.R;
import dresta.putra.wargakita.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class LoginActivity extends AppCompatActivity {
    private static final int UNAUTHORIZED = 401;
    EditText username_input,password_input,EtEmail;
    Button BtnLogin,BtnClose,BtnKirim;
    Vibrator v;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private PrefManager prefManager;
    private TextView TxvForgetPass;
    private ProgressDialog progressDialog = null;

    interface MyAPIService {
        @FormUrlEncoded
        @POST("api/login/user")
        Call<LoginResponsePojo> getDatapengguna(@Field("username") String username, @Field("password") String password);
        @FormUrlEncoded
        @POST("api/login/forget_password")
        Call<ResponsePojo> forgetPassword(@Field("email") String email);
    }
    private MyAPIService myAPIService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView IvInfo = findViewById(R.id.IvInfo);
        myAPIService = RetrofitClientInstance.getRetrofitInstance(LoginActivity.this).create(MyAPIService.class);
        IvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Iinfo = new Intent(LoginActivity.this, AboutActivity.class);
                startActivity(Iinfo);
            }
        });

        bottom_sheet = findViewById(R.id.bottom_sheet);
        EtEmail = findViewById(R.id.EtEmail);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        TxvForgetPass = findViewById(R.id.TxvForgetPass);
        BtnClose = findViewById(R.id.BtnClose);
        BtnKirim = findViewById(R.id.BtnKirim);

        username_input = findViewById(R.id.userName);
        password_input = findViewById(R.id.loginPassword);
        BtnLogin = findViewById(R.id.BtnLogin);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        prefManager = new PrefManager(this);
        boolean loggedInIdMahasiswa= prefManager.isUserLoggedIn();
        if (loggedInIdMahasiswa) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        BtnLogin.setOnClickListener(view -> validateUserData());
        TxvForgetPass.setOnClickListener(v -> sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
        BtnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirim(BtnKirim);
            }
        });
        BtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        TextView TxvRegister = findViewById(R.id.TxvRegister);
        TxvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, DaftarActivity.class);
            startActivity(intent);
        });




    }

    private void kirim(final Button button){

        String EtEmails = EtEmail.getText().toString();
        button.setEnabled(false);
        if (TextUtils.isEmpty(EtEmails)) {
            EtEmail.setError("Email masih kosong");
            EtEmail.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "KIRIM");
            return;
        }
        if (!TextUtils.isEmpty(EtEmails) && !Patterns.EMAIL_ADDRESS.matcher(EtEmails).matches()) {
            EtEmail.setError("Format email tidak sesuai");
            EtEmail.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "KIRIM");
            return;
        }
        Call<ResponsePojo> responsePojoCall = myAPIService.forgetPassword(EtEmails);
        responsePojoCall.enqueue(new Callback<ResponsePojo>() {
            @Override
            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
                if (response.body()!=null){
                    if (response.body().getStatus()==200){
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "KIRIM");
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }else{
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "KIRIM");
                        Toast.makeText(LoginActivity.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePojo> call, Throwable t) {
                button.setEnabled(true);
                DrawableButtonExtensionsKt.hideProgress(button, "KIRIM");
                Toast.makeText(LoginActivity.this, "Terjadi gangguan jaringan, coba lagi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void validateUserData() {
        //fflagirst getting the values
        final String username= username_input.getText().toString();
        final String password = password_input.getText().toString();

        //checking if username is empty
        if (TextUtils.isEmpty(username)) {
            username_input.setError("Username masih kosong");
            username_input.requestFocus();
            // Vibrate for 100 milliseconds
            v.vibrate(100);
            BtnLogin.setEnabled(true);
            return;
        }
        //checking if password is empty
        if (TextUtils.isEmpty(password)) {
            password_input.setError("Password masih kosong");
            password_input.requestFocus();
            //Vibrate for 100 milliseconds
            v.vibrate(100);
            BtnLogin.setEnabled(true);
            return;
        }



        //Login User if everything is fine
        loginUser(username,password);


    }


    private void loginUser(String username, final String password) {
        //making api call


        ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(LoginActivity.this);
        progressDoalog.setMax(100);
        progressDoalog.setTitle("Login");
        progressDoalog.setMessage("Login....");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDoalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
        progressDoalog.show();

        Call<LoginResponsePojo> call = myAPIService.getDatapengguna(username,password);

        call.enqueue(new Callback<LoginResponsePojo>() {

            @Override
            public void onResponse(Call<LoginResponsePojo> call, Response<LoginResponsePojo> response) {
                if (response.body() != null) {
                    if(response.body().getStatus()==200){
                        progressDoalog.dismiss();
                        set_sess(response.body().getData().getId_user(),response.body().getData().getUsername(),password, response.body().getData());
                        showProgressDialog("Login fitur chat..");
                        QBUser qbUser = new QBUser();

                        qbUser.setLogin(String.valueOf(prefManager.getUserPojo().getUsername()).trim());
                        qbUser.setFullName(String.valueOf(prefManager.getUserPojo().getNamaLengkap()).trim());
                        String password = (String.valueOf(prefManager.getUserPojo().getPassword()).length() > 40) ? String.valueOf(prefManager.getUserPojo().getPassword()).substring(0,39) : String.valueOf(prefManager.getUserPojo().getPassword());
                        qbUser.setPassword(password);
                        signIn(qbUser);

                    }else{
                        progressDoalog.dismiss();
                        Toast.makeText(LoginActivity.this,response.body().getMsg(),Toast.LENGTH_LONG).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<LoginResponsePojo> call, Throwable throwable) {
                progressDoalog.dismiss();
                Toast.makeText(LoginActivity.this, "Gagal Login, Coba Lagi", Toast.LENGTH_LONG).show();
            }
        });

    }
    public void set_sess(String id_kolektor,String username,String password, UserPojo userPojo){
        prefManager.storeDataUser(id_kolektor,username,password);
        prefManager.setUserPojo(userPojo);
    }

//    chat

    private void updateUser(final QBUser user) {

    ChatHelper.getInstance().updateUser(user, new QBEntityCallback<QBUser>() {
        @Override
        public void onSuccess(QBUser user, Bundle bundle) {
            SharedPrefsHelper.getInstance().saveQbUser(user);
            QbUsersHolder.getInstance().putUser(user);
        }
        @Override
        public void onError(QBResponseException e) {
            showErrorSnackbar(R.string.login_chat_login_error, e, null);
        }
    });
}
    private void signIn(final QBUser user) {
        QBSettings.getInstance();
        ChatHelper.getInstance().login(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser userFromRest, Bundle bundle) {
                if (userFromRest.getFullName() != null && userFromRest.getFullName().equals(user.getFullName())) {
                    hideProgressDialog();
                    SharedPrefsHelper.getInstance().saveQbUser(user);
                    QbUsersHolder.getInstance().putUser(user);
                    Intent IHome = new Intent(LoginActivity.this,MainActivity.class);
                    finish();
                    startActivity(IHome);
                } else {
                    hideProgressDialog();
                    //Need to set password NULL, because server will update user only with NULL password
                    updateUser(user);
                    Intent IHome = new Intent(LoginActivity.this,MainActivity.class);
                    finish();
                    startActivity(IHome);
                }
            }

            @Override
            public void onError(QBResponseException e) {
                if (e.getHttpStatusCode() == UNAUTHORIZED) {
                    signUp(user);
                } else {
                    progressDialog.dismiss();
                    String username= username_input.getText().toString();
                    String password = password_input.getText().toString();
                    showErrorSnackbar(R.string.login_chat_login_error, e, v -> loginUser(username,password));
                }
            }
        });
    }
    protected void showErrorSnackbar(@StringRes int resId, Exception e, View.OnClickListener clickListener) {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        if (rootView != null) {
            ErrorUtils.showSnackbar(rootView, resId, e,
                    R.string.dlg_retry, clickListener);
        }
    }

    private void signUp(final QBUser newUser) {
        SharedPrefsHelper.getInstance().removeQbUser();
        QBUsers.signUp(newUser).performAsync(new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle bundle) {
                signIn(newUser);
            }

            @Override
            public void onError(QBResponseException e) {
                showErrorSnackbar(R.string.login_sign_up_error, e, v1 -> {
                    String username= username_input.getText().toString();
                    String password = password_input.getText().toString();
                    loginUser(username,password);
                });
            }
        });
    }
    protected void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            // Disable the back button
            DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            };
            progressDialog.setOnKeyListener(keyListener);
        }
        progressDialog.setMessage(message);
        try {
            progressDialog.show();
        } catch (Exception e) {
            if (e.getMessage() != null) {
//                Log.d(, e.getMessage());
            }
        }
    }
    protected void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }




}


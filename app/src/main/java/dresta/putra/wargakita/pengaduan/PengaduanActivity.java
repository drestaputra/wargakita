package dresta.putra.wargakita.pengaduan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressParams;

import java.util.Objects;

import dresta.putra.wargakita.R;
import dresta.putra.wargakita.ResponsePojo;
import dresta.putra.wargakita.RetrofitClientInstance;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class PengaduanActivity extends AppCompatActivity {
    private EditText EtNamaLengkap,EtEmail,EtIsiAduan;
    private Button BtnKirim;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaduan);
        BtnKirim = findViewById(R.id.BtnKirim);
        EtNamaLengkap = findViewById(R.id.EtNamaLengkap);
        EtEmail = findViewById(R.id.EtEmail);
        EtIsiAduan = findViewById(R.id.EtIsiAduan);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        BtnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirim(BtnKirim);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    private void kirim(final Button button) {
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setButtonTextRes(R.string.loading);
                progressParams.setProgressColor(Color.WHITE);
                return Unit.INSTANCE;
            }
        });
//        String EtNamaLengkaps,EtEmail,EtIsiAduan;
        button.setEnabled(false);
        final String EtNamaLengkaps = EtNamaLengkap.getText().toString();
        final String EtIsiAduans = EtIsiAduan.getText().toString();
        if (TextUtils.isEmpty(EtNamaLengkaps)) {
            EtNamaLengkap.setError("Nama masih kosong");
            EtNamaLengkap.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }

        if (TextUtils.isEmpty(EtIsiAduans)) {
            EtIsiAduan.setError("Isi aduan masih kosong");
            EtIsiAduan.requestFocus();
            button.setEnabled(true);
            DrawableButtonExtensionsKt.hideProgress(button, "Kirim");
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PengaduanActivity.this, "Pengaduan berhasil dikirim", Toast.LENGTH_SHORT).show();
                finish();

            }
        }, 3000);



    }
}

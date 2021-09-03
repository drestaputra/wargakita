package dresta.putra.wargakita.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import dresta.putra.wargakita.R;
import dresta.putra.wargakita.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class ProfilUserActivity extends AppCompatActivity {
    private String id_nasabah;
    private TextView TxvUsername, TxvEmail, TxvNamaUser, TxvNoHp, TxvAlamat, TxvProvinsi, TxvKabupaten, TxvKecamatan, Txvwarga_negara,TxvNoKtp, TxvNasabah,TxvPinjaman, TxvSimpanan;
    private ImageView IvFotoNasabah,IvBack;
    private UserPojo userPojo;
    private LinearLayout NoConn;
    private Button btRefresh;
    SwipeRefreshLayout swipe;
    interface APIProfilUser{
        @GET("api/user/profil")
        Call<ProfilUserPojoResponse> getProfil();
    }
    private APIProfilUser servicePojo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_user);
        ImageView IvBack = findViewById(R.id.IvBack);
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        swipe = findViewById(R.id.swipe);
        TxvUsername = findViewById(R.id.TxvUsername);
        TxvEmail = findViewById(R.id.TxvEmail);
        TxvNamaUser = findViewById(R.id.TxvNamaUser);
        TxvNoHp = findViewById(R.id.TxvNoHp);
        TxvAlamat = findViewById(R.id.TxvAlamat);
        TxvProvinsi = findViewById(R.id.TxvProvinsi);
        TxvKabupaten = findViewById(R.id.TxvKabupaten);
        TxvKecamatan = findViewById(R.id.TxvKecamatan);
        Txvwarga_negara = findViewById(R.id.Txvwarga_negara);
        TxvNoKtp = findViewById(R.id.TxvNoKtp);
        NoConn = findViewById(R.id.NoConn);
        btRefresh = findViewById(R.id.btRefresh);
        servicePojo = RetrofitClientInstance.getRetrofitInstance(ProfilUserActivity.this).create(APIProfilUser.class);
        initDataProfil();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initDataProfil();
                swipe.setRefreshing(false);
            }
        });
        ImageView IvEdit = findViewById(R.id.IvEdit);
        IvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Iedit = new Intent(ProfilUserActivity.this,EditProfilUserActivity.class);
                startActivity(Iedit);
                overridePendingTransition(0,0);
            }
        });
        ImageView IvEditPassword = findViewById(R.id.IvEditPassword);
        IvEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Iedit = new Intent(ProfilUserActivity.this,EditPasswordUserActivity.class);
                startActivity(Iedit);
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDataProfil();
    }

    private void initDataProfil() {
        Call<ProfilUserPojoResponse> userPojoCall = servicePojo.getProfil();
        userPojoCall.enqueue(new Callback<ProfilUserPojoResponse>() {
            @Override
            public void onResponse(Call<ProfilUserPojoResponse> call, Response<ProfilUserPojoResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        NoConn.setVisibility(View.GONE);
                        userPojo = response.body().getData();
                        TxvUsername.setText(userPojo.getUsername());
                        TxvEmail.setText(userPojo.getEmail());
                        TxvNamaUser.setText(userPojo.getNamaLengkap());
                        TxvNoHp.setText(userPojo.getNo_hp());
                        TxvAlamat.setText(userPojo.getAlamat());
                        TxvProvinsi.setText(userPojo.getLabel_provinsi());
                        TxvKabupaten.setText(userPojo.getLabel_kabupaten());
                        TxvKecamatan.setText(userPojo.getLabel_kecamatan());
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfilUserPojoResponse> call, Throwable t) {
//                Toast.makeText(ProfilUserActivity.this, "Terjadi gangguan jaringan", Toast.LENGTH_SHORT).show();
                NoConn.setVisibility(View.VISIBLE);
                btRefresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initDataProfil();
                    }
                });
            }
        });
    }

    class ProfilUserPojoResponse{
        @SerializedName("status")
        Integer status;
        @SerializedName("msg")
        String msg;
        @SerializedName("data")
        UserPojo data;

        public ProfilUserPojoResponse(Integer status, String msg, UserPojo data) {
            this.status = status;
            this.msg = msg;
            this.data = data;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public UserPojo getData() {
            return data;
        }

        public void setData(UserPojo data) {
            this.data = data;
        }
    }
}

package dresta.putra.wargakita.agenda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import dresta.putra.wargakita.PrefManager;
import dresta.putra.wargakita.R;

public class AgendaActivity extends AppCompatActivity {
    private TextView TxvTanggal;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int PER_PAGE=20;
    private int currentPage = PAGE_START;
    private String query_pencarian="";
    private GridLayoutManager linearLayoutManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    private String tgl;
    private PrefManager prefManager;
    private CalendarView CalvJadwal;
    private Locale localeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);
        mShimmerViewContainer = findViewById(R.id.shimmer_card_view);
        localeID = new Locale("in", "ID");
        CalvJadwal = findViewById(R.id.CalvJadwal);
        TxvTanggal = findViewById(R.id.TxvTanggal);
        Configuration config = new Configuration();
        config.locale = localeID;
        getApplicationContext().getResources().updateConfiguration(config, null);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy",localeID);
        SimpleDateFormat tgl_sekarang = new SimpleDateFormat("dd MMMM yyyy",localeID);
        SimpleDateFormat tgl_sekarang_ymd = new SimpleDateFormat("yyyy-MM-dd",localeID);
        final String selectedDate = tgl_sekarang.format(new Date(CalvJadwal.getDate()));
        final String selectedDate_ymd = tgl_sekarang_ymd.format(new Date(CalvJadwal.getDate()));
        tgl = selectedDate_ymd;
        TxvTanggal.setText("Agenda Kegiatan : "+selectedDate);
        CalvJadwal.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            TxvTanggal.setText("Agenda Kegiatan : "+dayOfMonth+" "+getMonth(month)+" "+ year);
            tgl = year+"-"+(month+1)+"-"+dayOfMonth;
        });

        RecyclerView rv = findViewById(R.id.RvData);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(v -> finish());
        prefManager = new PrefManager(this);
        Button BtnBuatAgenda = findViewById(R.id.BtnBuatAgenda);
        BtnBuatAgenda.setOnClickListener(v -> {
            Intent intent = new Intent(AgendaActivity.this, BuatAgendaActivity.class);
            startActivity(intent);
        });
    }
    public String getMonth(int month) { return DateFormatSymbols.getInstance(localeID).getMonths()[month]; }

}
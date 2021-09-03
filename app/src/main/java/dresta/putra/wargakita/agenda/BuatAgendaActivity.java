package dresta.putra.wargakita.agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import dresta.putra.wargakita.R;
public class BuatAgendaActivity extends AppCompatActivity {
    private EditText EtTglAgenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_agenda);
        EtTglAgenda = findViewById(R.id.EtTglAgenda);
        EtTglAgenda.setOnClickListener(v -> showDateStartDialog());
        Button BtnAjukan = findViewById(R.id.BtnAjukan);
        BtnAjukan.setOnClickListener(v -> {
            Toast.makeText(this, "Pengajuan Agenda kegiatan berhasil", Toast.LENGTH_SHORT).show();
            finish();
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }
    private void showDateStartDialog(){
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(BuatAgendaActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            Locale localeID = new Locale("in", "ID");
            SimpleDateFormat format = new SimpleDateFormat("EEEE,  d MMMM yyyy ", localeID);
            EtTglAgenda.setText(format.format(newDate.getTime()));
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
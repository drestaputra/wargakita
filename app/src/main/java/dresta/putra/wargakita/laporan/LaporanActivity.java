package dresta.putra.wargakita.laporan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import dresta.putra.wargakita.R;
import pub.devrel.easypermissions.EasyPermissions;

public class LaporanActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{
    Button BtnKirim, BtnImage;
    Spinner SpJenisLaporan;
    public static final int REQUEST_IMAGE = 100;
    private static final int RC_EXTERNAL_STORAGE= 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        BtnKirim = findViewById(R.id.BtnKirim);
        BtnKirim.setOnClickListener(v -> {
            Toast.makeText(this, "Laporan berhasil dikirim", Toast.LENGTH_SHORT).show();
            finish();
        });
        BtnImage = findViewById(R.id.BtnImage);
        BtnImage.setOnClickListener(v -> {
            execute(REQUEST_IMAGE);
        });

        SpJenisLaporan = findViewById(R.id.SpJenisLaporan);
        List<String> listSpinner = new ArrayList<>();
        listSpinner.add("Pilih");
        listSpinner.add("Laporan 1");
        listSpinner.add("Laporan 2");
        listSpinner.add("Laporan 3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(LaporanActivity.this,
                android.R.layout.simple_spinner_item, listSpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpJenisLaporan.setAdapter(adapter);
    }
    void execute(int requestCode){
        switch (requestCode){
            case REQUEST_IMAGE:
                if(EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent openGalleryIntent = new Intent(Intent.ACTION_PICK);
                    openGalleryIntent.setType("image/*");
                    startActivityForResult(openGalleryIntent, REQUEST_IMAGE);
                    break;
                }else{
                    EasyPermissions.requestPermissions(this,"Izinkan Aplikasi Mengakses Storage?",REQUEST_IMAGE,Manifest.permission.READ_EXTERNAL_STORAGE);
                }
        }
    }
    private void reloadImage(){
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    void uploadFile(Uri contentURI){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE && resultCode == RESULT_OK){

        }
    }


    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RC_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
            } else {
//                finish();
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == REQUEST_IMAGE){
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if(requestCode == REQUEST_IMAGE){
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
    private void edit(final Button button){

    }
    void showLoading(){

    }
    void dismissLoading(){

    }
}
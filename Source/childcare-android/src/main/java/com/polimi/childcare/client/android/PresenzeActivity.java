package com.polimi.childcare.client.android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.Result;
import com.polimi.childcare.client.shared.qrcode.BambinoQRUnit;
import com.polimi.childcare.shared.serialization.SerializationUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class PresenzeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    private FloatingActionButton fabScanQRCode;

    //LayourQRCode
    private ZXingScannerView mScannerView;
    private LinearLayout layoutQrCode;
    private FloatingActionButton fabEnter;
    private FloatingActionButton fabExit;
    private TextView txtName;
    private TextView txtSurname;
    private TextView txtFiscalCode;
    private EditText txtDate;

    private Timer qrTimer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presenze_screen);

        this.qrTimer = new Timer();

        this.fabScanQRCode = findViewById(R.id.fabScanQr);

        this.layoutQrCode = findViewById(R.id.layoutQrScanner);
        this.mScannerView = findViewById(R.id.qrCodeScannerView);
        this.fabEnter = findViewById(R.id.fabEnter);
        this.fabExit = findViewById(R.id.fabExit);
        this.txtName = findViewById(R.id.txtName);
        this.txtSurname = findViewById(R.id.txtSurname);
        this.txtFiscalCode = findViewById(R.id.txtFiscalCode);
        this.txtDate = findViewById(R.id.txtDate);

        this.fabScanQRCode.setOnClickListener((view)->{


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {

                    // Permission is not granted
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            0);
                }
                else
                    // Permission is not granted
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            0);

                Toast.makeText(this, "E' necessario acconsetire all'utilizzo della fotocamera!", Toast.LENGTH_LONG).show();
                return;
            }

            layoutQrCode.setVisibility(View.VISIBLE);
            clearQrScanner();
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        });

        this.fabExit.setOnClickListener((view -> closeQrScanner()));
    }

    @Override
    public void handleResult(Result result)
    {
        if(result != null && result.getText().length() > 0)
        {
            String base64Data = result.getText();
            byte[] data = Base64.decode(base64Data, Base64.DEFAULT);
            BambinoQRUnit resultBambino = SerializationUtils.deserializeByteArray(data, BambinoQRUnit.class);
            if(resultBambino != null)
            {
                txtName.setText(resultBambino.getNome());
                txtSurname.setText(resultBambino.getCognome());
                txtFiscalCode.setText(resultBambino.getCodiceFiscale());
            }
        }
        //Riabilito la telecamera dopo 10 secondi
        qrTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mScannerView.startCamera();
                mScannerView.setResultHandler(PresenzeActivity.this);
                clearQrScanner();
            }
        }, 10000);

        mScannerView.stopCamera();
        //layoutQrCode.setVisibility(View.GONE);
    }

    private void clearQrScanner()
    {
        txtName.setText("");
        txtSurname.setText("");
        txtFiscalCode.setText("");
        txtDate.setText(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm").format(LocalDateTime.now()));
    }

    private void closeQrScanner()
    {
        qrTimer.cancel();
        layoutQrCode.setVisibility(View.GONE);
        mScannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 0)
        {
            if(grantResults.length > 0 && grantResults[0] > 0)
            {
                //Ripremo il bottone
                fabScanQRCode.callOnClick();
            }
        }
    }
}

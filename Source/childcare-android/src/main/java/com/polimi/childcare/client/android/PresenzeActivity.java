package com.polimi.childcare.client.android;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.Result;
import com.polimi.childcare.client.android.adapters.GenericViewHolderAdapter;
import com.polimi.childcare.client.android.viewholders.BambinoViewHolder;
import com.polimi.childcare.client.android.viewholders.GenericViewHolder;
import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.shared.qrcode.BambinoQRUnit;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.networking.requests.BaseRequest;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;
import com.polimi.childcare.shared.serialization.SerializationUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PresenzeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    private FloatingActionButton fabScanQRCode;
    private RecyclerView listPresenze;

    //LayourQRCode
    private ZXingScannerView mScannerView;
    private AppCompatImageView imgScannedBambinoPreview;
    private LinearLayout layoutQrCode;
    private FloatingActionButton fabEnter;
    private FloatingActionButton fabExit;
    private TextView txtName;
    private TextView txtSurname;
    private TextView txtFiscalCode;
    private EditText txtDate;

    private Timer qrTimer;

    //Gestione Presenze
    private GenericViewHolderAdapter<Bambino,BambinoViewHolder> presenzeAdapter;
    private LinearLayoutManager presenzeLayoutManager;
    private NetworkOperation requestPresenzeUpdate;

    //Take Picture
    private UUID currentTakePictureID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presenze_screen);

        this.fabScanQRCode = findViewById(R.id.fabScanQr);
        this.listPresenze = findViewById(R.id.listPresenze);

        this.layoutQrCode = findViewById(R.id.layoutQrScanner);
        this.mScannerView = findViewById(R.id.qrCodeScannerView);
        this.imgScannedBambinoPreview = findViewById(R.id.imgScanBambinoPreview);
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

        this.listPresenze.setLayoutManager((presenzeLayoutManager = new LinearLayoutManager(this)));
        this.listPresenze.setAdapter((presenzeAdapter = new GenericViewHolderAdapter<>(BambinoViewHolder.class, null)));

        //Se non sto cercando di aggiornare la lista presenze
        if(requestPresenzeUpdate == null)
        {
            requestPresenzeUpdate = new NetworkOperation(new FilteredBambiniRequest(0, 0, false, null, null), this::OnPresenzeUpdate, false);
            ClientNetworkManager.getInstance().submitOperation(requestPresenzeUpdate);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(takePictureReciver, new IntentFilter(BambinoViewHolder.ACTION_TAKE_PICTURE));
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
                showQrScannerPartialResult(resultBambino);

                //Riabilito la telecamera dopo 10 secondi
                if(qrTimer != null)
                    qrTimer.cancel();
                qrTimer = new Timer();
                qrTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(() -> {
                            mScannerView.startCamera();
                            mScannerView.setResultHandler(PresenzeActivity.this);
                            clearQrScanner();
                        });
                    }
                }, 10000);

                mScannerView.stopCamera();
                return;
            }
        }

        mScannerView.startCamera();
        mScannerView.setResultHandler(this);

        //layoutQrCode.setVisibility(View.GONE);
    }

    private void showQrScannerPartialResult(BambinoQRUnit resultBambino)
    {
        txtName.setText(resultBambino.getNome());
        txtSurname.setText(resultBambino.getCognome());
        txtFiscalCode.setText(resultBambino.getCodiceFiscale());

        Bitmap previewImage = ImageStore.getInstance().GetImage(UUID.nameUUIDFromBytes(String.valueOf(resultBambino.getID()).getBytes()));
        if(previewImage != null)
        {
            this.imgScannedBambinoPreview.setVisibility(View.VISIBLE);
            this.imgScannedBambinoPreview.setImageBitmap(previewImage);
        }
    }

    private void clearQrScanner()
    {
        this.imgScannedBambinoPreview.setVisibility(View.GONE);
        txtName.setText("");
        txtSurname.setText("");
        txtFiscalCode.setText("");
        txtDate.setText(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm").format(LocalDateTime.now()));
    }

    private void closeQrScanner()
    {
        clearQrScanner();
        if(qrTimer != null)
        {
            qrTimer.cancel();
            qrTimer.purge();
        }
        layoutQrCode.setVisibility(View.GONE);
        mScannerView.stopCamera();
    }

    @Override
    protected void onPause() {
        closeQrScanner();
        super.onPause();
    }

    private void OnPresenzeUpdate(BaseResponse response)
    {
        if(response instanceof BadRequestResponse)
            Toast.makeText(this, "Errore durante la connessione al server per aggiornare le presenze", Toast.LENGTH_LONG).show();

        if(response instanceof ListBambiniResponse)
        {
            runOnUiThread(() -> {
                this.presenzeAdapter.clear();
                this.presenzeAdapter.addRange(((ListBambiniResponse) response).getPayload());
            });
        }

        this.requestPresenzeUpdate = null;
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
        if(requestCode == 1)
        {
            if(grantResults.length > 0 && grantResults[0] > 0)
            {
                openTakeProfilePictureDialog();
            }
        }
    }

    private void openTakeProfilePictureDialog()
    {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(pictureIntent, 100);
    }

    private BroadcastReceiver takePictureReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            currentTakePictureID = UUID.fromString(intent.getStringExtra(BambinoViewHolder.EXTRA_UUID));

            if (ContextCompat.checkSelfPermission(PresenzeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(PresenzeActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    // Permission is not granted
                    ActivityCompat.requestPermissions(PresenzeActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
                } else
                    // Permission is not granted
                    ActivityCompat.requestPermissions(PresenzeActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            1);
            }
            else
                openTakeProfilePictureDialog();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100)
        {
            if (resultCode == RESULT_OK && currentTakePictureID != null && data.hasExtra("data"))
            {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ImageStore.getInstance().SaveImage(currentTakePictureID, bitmap);
                if(this.presenzeAdapter != null)
                    this.presenzeAdapter.updateAll();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

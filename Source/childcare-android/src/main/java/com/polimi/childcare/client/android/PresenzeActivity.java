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
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.zxing.Result;
import com.polimi.childcare.client.android.adapters.GenericViewHolderAdapter;
import com.polimi.childcare.client.android.viewholders.BambinoViewHolder;
import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.shared.qrcode.BambinoQRUnit;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;
import com.polimi.childcare.shared.serialization.SerializationUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.ToIntFunction;

public class PresenzeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler, SearchView.OnQueryTextListener
{
    private FloatingActionButton fabScanQRCode;
    private RecyclerView listPresenze;

    //LayourQRCode
    private ZXingScannerView mScannerView;
    private AppCompatImageView imgScannedBambinoPreview;
    private ConstraintLayout layoutQrCode;
    private LinearLayout layoutQrDetails;
    private FloatingActionButton fabEnter;
    private FloatingActionButton fabExit;
    private TextView txtName;
    private TextView txtSurname;
    private TextView txtFiscalCode;
    private EditText txtDate;

    private Timer qrTimer;
    private Handler animationHandler;

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
        this.layoutQrDetails = findViewById(R.id.layoutDetails);
        this.mScannerView = findViewById(R.id.qrCodeScannerView);
        this.imgScannedBambinoPreview = findViewById(R.id.imgScanBambinoPreview);
        this.fabEnter = findViewById(R.id.fabEnter);
        this.fabExit = findViewById(R.id.fabExit);
        this.txtName = findViewById(R.id.txtName);
        this.txtSurname = findViewById(R.id.txtSurname);
        this.txtFiscalCode = findViewById(R.id.txtFiscalCode);
        this.txtDate = findViewById(R.id.txtDate);

        //Animazioni
        this.layoutQrDetails.setAlpha(0f);
        this.layoutQrDetails.setTranslationY(-this.layoutQrDetails.getHeight());
        this.fabEnter.setScaleX(0);
        this.fabEnter.setScaleY(0);
        this.animationHandler = new Handler(Looper.myLooper());

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

            layoutQrCode.setAlpha(0f);
            layoutQrCode.setVisibility(View.VISIBLE);
            layoutQrCode.animate().alpha(1f).setDuration(300).start();
            clearQrScanner();
        });

        this.listPresenze.setLayoutManager((presenzeLayoutManager = new LinearLayoutManager(this)));
        this.listPresenze.setAdapter((presenzeAdapter = new GenericViewHolderAdapter<>(BambinoViewHolder.class, Bambino.class, CacheManager.getInstance(this).getBambini(),
                Comparator.comparingInt((ToIntFunction<Bambino>) Persona::getID))));

        //Se non sto cercando di aggiornare la lista presenze
        if(requestPresenzeUpdate == null)
        {
            requestPresenzeUpdate = new NetworkOperation(new FilteredBambiniRequest(0, 0, false, null, null),
                                                        this::OnPresenzeUpdate, false);
            ClientNetworkManager.getInstance().submitOperation(requestPresenzeUpdate);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(takePictureReciver, new IntentFilter(BambinoViewHolder.ACTION_TAKE_PICTURE));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
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
                            clearQrScanner();
                        });
                    }
                }, 10000);

                mScannerView.stopCamera();
                return;
            }
        }
        clearQrScanner();

        //layoutQrCode.setVisibility(View.GONE);
    }

    private void showQrScannerPartialResult(BambinoQRUnit resultBambino)
    {
        txtName.setText(resultBambino.getNome());
        txtSurname.setText(resultBambino.getCognome());
        txtFiscalCode.setText(resultBambino.getCodiceFiscale());
        this.layoutQrDetails.animate().translationY(0).alpha(1f).setDuration(350).start();
        Bitmap previewImage = ImageStore.getInstance().GetImage(this, UUID.nameUUIDFromBytes(String.valueOf(resultBambino.getID()).getBytes()));
        if(previewImage != null)
        {
            this.imgScannedBambinoPreview.setAlpha(0.0f);
            this.imgScannedBambinoPreview.animate().alpha(1f).setDuration(350).start();
            this.imgScannedBambinoPreview.setVisibility(View.VISIBLE);
            this.imgScannedBambinoPreview.setImageBitmap(previewImage);
        }

        this.fabExit.setImageDrawable(getDrawable(R.drawable.ic_arrow_downward_black_24dp));
        this.fabExit.setOnClickListener(null); //TODO: Exit
        this.fabEnter.setOnClickListener(null); //TODO: Enter
        this.fabEnter.animate().scaleX(1).scaleY(1).setDuration(350).start();
    }

    private void clearQrScanner()
    {
        this.imgScannedBambinoPreview.animate().alpha(0f).setDuration(350).withEndAction(() -> this.imgScannedBambinoPreview.setVisibility(View.GONE)).start();
        this.fabExit.setOnClickListener((view -> closeQrScanner()));
        txtName.setText("");
        txtSurname.setText("");
        txtFiscalCode.setText("");

        this.fabExit.setImageDrawable(getDrawable(R.drawable.ic_close_black_24dp));
        this.fabEnter.setOnClickListener(null);
        this.fabEnter.animate().scaleX(0).scaleY(0).setDuration(350).start();

        txtDate.setText(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm").format(LocalDateTime.now()));

        this.layoutQrDetails.animate().translationY(-this.layoutQrDetails.getHeight()).alpha(0f).setDuration(350).withEndAction(() -> {
            mScannerView.startCamera();
            mScannerView.setResultHandler(PresenzeActivity.this);
        }).start();
    }

    private void closeQrScanner()
    {
        clearQrScanner();
        if(qrTimer != null)
        {
            qrTimer.cancel();
            qrTimer.purge();
        }
        mScannerView.stopCamera();
        layoutQrCode.animate().alpha(0.0f).setDuration(300).withEndAction(() -> layoutQrCode.setVisibility(View.GONE)).start();
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
            CacheManager.getInstance(this).replaceBambini(this, ((ListBambiniResponse) response).getPayload());
            runOnUiThread(() -> {
                this.presenzeAdapter.replaceAll(CacheManager.getInstance(this).getBambini());
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
                ImageStore.getInstance().SaveImage(this, currentTakePictureID, bitmap);
                if(this.presenzeAdapter != null)
                    this.presenzeAdapter.updateAll();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private List<Bambino> filter(List<Bambino> bambini, String query)
    {
        if(query == null || query.isEmpty())
            return bambini;

        final String lowerCaseQuery = query.trim().toLowerCase();
        final boolean spaced = lowerCaseQuery.trim().contains(" ");
        final List<Bambino> filteredModelList = new ArrayList<>();
        for (Bambino bambino : bambini)
        {
            try
            {
                //Se è un numero filtro per matricola
                Integer.parseInt(query);
                if(String.valueOf(bambino.getID()).contains(query))
                    filteredModelList.add(bambino);
            }
            catch(NumberFormatException ex)
            {
                //Se non è un numero filtro per Nome, Cognome e Codice Fiscale

                if (bambino.getNome().toLowerCase().contains(lowerCaseQuery)
                        || bambino.getCognome().toLowerCase().contains(lowerCaseQuery)
                        || bambino.getCodiceFiscale().toLowerCase().contains(lowerCaseQuery))
                    filteredModelList.add(bambino);

                if(!filteredModelList.contains(bambino) &&
                        spaced  &&
                        ((bambino.getNome().toLowerCase() + " " + bambino.getCognome().toLowerCase()).contains(lowerCaseQuery)
                           || (bambino.getCognome().toLowerCase() + " " + bambino.getNome().toLowerCase()).contains(lowerCaseQuery)))
                    filteredModelList.add(bambino);
            }

        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextChange(String query)
    {
        final List<Bambino> filteredModelList = filter(CacheManager.getInstance(this).getBambini(), query);
        presenzeAdapter.replaceAll(filteredModelList);
        listPresenze.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }
}

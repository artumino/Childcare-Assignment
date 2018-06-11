package com.polimi.childcare.client.android;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.polimi.childcare.client.shared.networking.NetworkOperationVault;
import com.polimi.childcare.client.shared.qrcode.BambinoQRUnit;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.entities.Persona;
import com.polimi.childcare.shared.entities.RegistroPresenze;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;
import com.polimi.childcare.shared.networking.requests.special.FilteredLastPresenzaRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListRegistroPresenzeResponse;
import com.polimi.childcare.shared.serialization.SerializationUtils;
import com.polimi.childcare.shared.utils.EntitiesHelper;
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
    private GenericViewHolderAdapter<RegistroPresenze,BambinoViewHolder> presenzeAdapter;
    private LinearLayoutManager presenzeLayoutManager;
    private NetworkOperationVault networkOperationVault;

    //Take Picture
    private UUID currentTakePictureID;

    //Refresh Layout
    private SwipeRefreshLayout refreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.presenze_screen);

        networkOperationVault = new NetworkOperationVault();

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

        this.refreshLayout = findViewById(R.id.swiperefresh);

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
        this.listPresenze.setAdapter((presenzeAdapter = new GenericViewHolderAdapter<>(BambinoViewHolder.class, RegistroPresenze.class, CacheManager.getInstance(this).getPresenze(),
                (o1, o2) -> Integer.compare(o1.getBambino().getID(), o2.getBambino().getID()))));

        RefreshData();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshData();
                refreshLayout.setRefreshing(false);
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(takePictureReciver, new IntentFilter(BambinoViewHolder.ACTION_TAKE_PICTURE));
    }

    private void RefreshData()
    {
        //Se non sto cercando di aggiornare la lista presenze
        if(!networkOperationVault.operationRunning(FilteredBambiniRequest.class))
        {
            networkOperationVault.submitOperation(new NetworkOperation(new FilteredBambiniRequest(0, 0, false),
                    this::OnPresenzeUpdate, false));
        }

        if(!networkOperationVault.operationRunning(FilteredLastPresenzaRequest.class))
        {
            networkOperationVault.submitOperation(new NetworkOperation(new FilteredLastPresenzaRequest(0, 0, false),
                    this::OnLastPresenzeUpdate, false));
        }
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
        networkOperationVault.operationDone(FilteredBambiniRequest.class);

        if(response instanceof BadRequestResponse)
            Toast.makeText(this, "Errore durante la connessione al server per aggiornare le presenze", Toast.LENGTH_LONG).show();

        if(response instanceof ListBambiniResponse)
        {
            CacheManager.getInstance(this).replaceBambini(this, ((ListBambiniResponse) response).getPayload());
            runOnUiThread(() -> {
                this.presenzeAdapter.replaceAll(CacheManager.getInstance(this).getPresenze());
            });
        }
    }

    private void OnLastPresenzeUpdate(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredLastPresenzaRequest.class);

        if(response instanceof BadRequestResponse)
            Toast.makeText(this, "Errore durante la connessione al server per aggiornare le presenze", Toast.LENGTH_LONG).show();

        if(response instanceof ListRegistroPresenzeResponse)
        {
            CacheManager.getInstance(this).replaceStatoPresenzeMap(this, EntitiesHelper.presenzeToSearchMap(((ListRegistroPresenzeResponse) response).getPayload()));
            runOnUiThread(() -> {
                this.presenzeAdapter.replaceAll(CacheManager.getInstance(this).getPresenze());
            });
        }
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

    private List<RegistroPresenze> filter(List<RegistroPresenze> presenze, String query)
    {
        if(query == null || query.isEmpty())
            return presenze;

        final String lowerCaseQuery = query.trim().toLowerCase();
        final boolean spaced = lowerCaseQuery.trim().contains(" ");
        final List<RegistroPresenze> filteredModelList = new ArrayList<>();
        for (RegistroPresenze presenza : presenze)
        {
            try
            {
                //Se è un numero filtro per matricola
                Integer.parseInt(query);
                if(String.valueOf(presenza.getBambino().getID()).contains(query))
                    filteredModelList.add(presenza);
            }
            catch(NumberFormatException ex)
            {
                //Se non è un numero filtro per Nome, Cognome e Codice Fiscale

                if (presenza.getBambino().getNome().toLowerCase().contains(lowerCaseQuery)
                        || presenza.getBambino().getCognome().toLowerCase().contains(lowerCaseQuery)
                        || presenza.getBambino().getCodiceFiscale().toLowerCase().contains(lowerCaseQuery))
                    filteredModelList.add(presenza);

                if(!filteredModelList.contains(presenza) &&
                        spaced  &&
                        ((presenza.getBambino().getNome().toLowerCase() + " " + presenza.getBambino().getCognome().toLowerCase()).contains(lowerCaseQuery)
                           || (presenza.getBambino().getCognome().toLowerCase() + " " + presenza.getBambino().getNome().toLowerCase()).contains(lowerCaseQuery)))
                    filteredModelList.add(presenza);
            }

        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextChange(String query)
    {
        final List<RegistroPresenze> filteredModelList = filter(CacheManager.getInstance(this).getPresenze(), query);
        presenzeAdapter.replaceAll(filteredModelList);
        listPresenze.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query)
    {
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        networkOperationVault.abortAllOperations();
    }
}

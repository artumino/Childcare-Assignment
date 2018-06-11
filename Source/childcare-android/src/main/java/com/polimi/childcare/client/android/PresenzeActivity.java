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
import com.polimi.childcare.client.android.menu.SelectMezzoActionProvider;
import com.polimi.childcare.client.android.tuples.BambinoGruppoTuple;
import com.polimi.childcare.client.android.viewholders.PresenzaViewHolder;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.client.shared.networking.NetworkOperationVault;
import com.polimi.childcare.client.shared.qrcode.BambinoQRUnit;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.requests.filtered.FilteredBambiniRequest;
import com.polimi.childcare.shared.networking.requests.setters.SetRegistroPresenzeRequest;
import com.polimi.childcare.shared.networking.requests.special.FilteredLastPresenzaRequest;
import com.polimi.childcare.shared.networking.requests.special.GetCurrentGitaRequest;
import com.polimi.childcare.shared.networking.requests.special.StartPresenzaCheckRequest;
import com.polimi.childcare.shared.networking.responses.BadRequestResponse;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListBambiniResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListGitaResponse;
import com.polimi.childcare.shared.networking.responses.lists.ListRegistroPresenzeResponse;
import com.polimi.childcare.shared.serialization.SerializationUtils;
import com.polimi.childcare.shared.utils.EntitiesHelper;
import com.polimi.childcare.shared.utils.StatoPresenzaUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private GenericViewHolderAdapter<BambinoGruppoTuple,PresenzaViewHolder> presenzeAdapter;
    private LinearLayoutManager presenzeLayoutManager;
    private NetworkOperationVault networkOperationVault;

    //Take Picture
    private UUID currentTakePictureID;

    //Refresh Layout
    private SwipeRefreshLayout refreshLayout;

    //Toolbar
    private MenuItem selectMezzoItem;
    private MenuItem startPresenzeCheck;
    private SelectMezzoActionProvider selectMezzoActionProvider;
    private MezzoDiTrasporto selectedMezzoDiTrasporto;

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
            clearQrScanner(false);
        });

        this.listPresenze.setLayoutManager((presenzeLayoutManager = new LinearLayoutManager(this)));
        this.listPresenze.setAdapter((presenzeAdapter = new GenericViewHolderAdapter<>(PresenzaViewHolder.class, BambinoGruppoTuple.class, CacheManager.getInstance(this).getPresenze(),
                (o1, o2) -> Integer.compare(o1.getLinkedBambino().getID(), o2.getLinkedBambino().getID()))));

        RefreshData();

        refreshLayout.setOnRefreshListener(() -> {
            RefreshData();
            refreshLayout.setRefreshing(false);
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(takePictureReciver, new IntentFilter(PresenzaViewHolder.ACTION_TAKE_PICTURE));
        LocalBroadcastManager.getInstance(this).registerReceiver(changePresenzaReceiver, new IntentFilter(PresenzaViewHolder.ACTION_CHANGE_PRESENZA));
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

        if(!networkOperationVault.operationRunning(GetCurrentGitaRequest.class))
        {
            networkOperationVault.submitOperation(new NetworkOperation(new GetCurrentGitaRequest(LocalDateTime.now().toEpochSecond(ZoneOffset.systemDefault().getRules().getOffset(Instant.now()))),
                    this::OnCurrentGitaUpdate, false));
        }

        checkForGita();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        selectMezzoItem = menu.findItem(R.id.action_select_autobus);
        startPresenzeCheck = menu.findItem(R.id.action_start_check_presenze);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        if(selectMezzoItem != null)
        {
            selectMezzoItem.setVisible(CacheManager.getInstance(this).getCurrentGita() != null);
            MenuItemCompat.setActionProvider(selectMezzoItem, (selectMezzoActionProvider = new SelectMezzoActionProvider(this)));
        }

        if(startPresenzeCheck != null)
            startPresenzeCheck.setVisible(CacheManager.getInstance(this).getCurrentGita() != null);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(selectMezzoActionProvider != null)
        {
            MezzoDiTrasporto mezzoDiTrasporto = selectMezzoActionProvider.getMezzoFromItem(item);
            if(mezzoDiTrasporto != null)
            {
                this.selectedMezzoDiTrasporto = mezzoDiTrasporto;
                if(this.selectedMezzoDiTrasporto.getID() == 0 && this.selectedMezzoDiTrasporto.getTarga() == null)
                {
                    this.selectedMezzoDiTrasporto = null;
                    if(getSupportActionBar() != null)
                        getSupportActionBar().setTitle(getString(R.string.app_name));
                }
                else
                    if(getSupportActionBar() != null)
                        getSupportActionBar().setTitle(getString(R.string.app_name) + " - " + selectedMezzoDiTrasporto.getTarga());
            }
        }

        if(item == startPresenzeCheck)
            checkPresenze();

        return super.onOptionsItemSelected(item);
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
                            clearQrScanner(false);
                        });
                    }
                }, 10000);

                mScannerView.stopCamera();
                return;
            }
        }
        clearQrScanner(false);

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

        fabEnter.setOnClickListener(v -> {
            SetBambinoPresenza(resultBambino, false);
            closeQrScanner();
        });

        fabExit.setOnClickListener(v -> {
            SetBambinoPresenza(resultBambino, true);
            closeQrScanner();
        });

        this.fabEnter.animate().scaleX(1).scaleY(1).setDuration(350).start();
    }

    private void clearQrScanner(boolean closing)
    {
        mScannerView.setVisibility(View.VISIBLE);
        this.imgScannedBambinoPreview.animate().alpha(0f).setDuration(350).withEndAction(() -> this.imgScannedBambinoPreview.setVisibility(View.GONE)).start();
        this.fabExit.setOnClickListener((view -> closeQrScanner()));
        txtName.setText("");
        txtSurname.setText("");
        txtFiscalCode.setText("");

        this.fabExit.setImageDrawable(getDrawable(R.drawable.ic_close_black_24dp));
        this.fabEnter.setOnClickListener(null);
        this.fabEnter.animate().scaleX(0).scaleY(0).setDuration(350).start();

        txtDate.setText(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm").format(LocalDateTime.now()));

        if(!closing)
            this.layoutQrDetails.animate().translationY(-this.layoutQrDetails.getHeight()).alpha(0f).setDuration(350).withEndAction(() -> {
                mScannerView.startCamera();
                mScannerView.setResultHandler(PresenzeActivity.this);
            }).start();
    }

    private void closeQrScanner()
    {
        clearQrScanner(true);
        if(qrTimer != null)
        {
            qrTimer.cancel();
            qrTimer.purge();
        }
        mScannerView.setVisibility(View.GONE);
        mScannerView.setResultHandler(null);
        mScannerView.stopCamera();
        layoutQrCode.animate().alpha(0.0f).setDuration(300).withEndAction(() -> layoutQrCode.setVisibility(View.GONE)).start();
    }

    private void checkForGita()
    {
        if(CacheManager.getInstance(this).getCurrentGita() != null &&
                (CacheManager.getInstance(this).getCurrentGita().getDataInizio().isEqual(LocalDate.now()) || CacheManager.getInstance(this).getCurrentGita().getDataInizio().isBefore(LocalDate.now())) &&
                (CacheManager.getInstance(this).getCurrentGita().getDataFine().isEqual(LocalDate.now()) || CacheManager.getInstance(this).getCurrentGita().getDataFine().isAfter(LocalDate.now())))
        {
            if(selectMezzoItem != null)
                selectMezzoItem.setVisible(true);
            if(startPresenzeCheck != null)
                startPresenzeCheck.setVisible(true);
        }
        else
        {
            CacheManager.getInstance(this).replaceCurrentGita(null);

            if(selectMezzoItem != null)
                selectMezzoItem.setVisible(false);
            if(startPresenzeCheck != null)
                startPresenzeCheck.setVisible(false);

            this.selectedMezzoDiTrasporto = null;
            if(getSupportActionBar() != null)
                getSupportActionBar().setTitle(getString(R.string.app_name));
        }
    }

    private void checkPresenze()
    {
        Gita currentGita = CacheManager.getInstance(this).getCurrentGita();
        if(currentGita != null &&
                (currentGita.getDataInizio().isEqual(LocalDate.now()) || currentGita.getDataInizio().isBefore(LocalDate.now())) &&
                (currentGita.getDataFine().isEqual(LocalDate.now()) || currentGita.getDataFine().isAfter(LocalDate.now())))
        {
            //Aggiorna le presenze in locale
            List<BambinoGruppoTuple> presenze = CacheManager.getInstance(this).getPresenze();
            HashMap<Bambino,RegistroPresenze> registroPresenzeHashMap = CacheManager.getInstance(this).getStatoPresenzaHashMap();
            for(BambinoGruppoTuple presenza : presenze)
            {
                if(presenza.getLinkedPresenza().getStato() != RegistroPresenze.StatoPresenza.Assente &&
                        presenza.getLinkedPresenza().getStato() != RegistroPresenze.StatoPresenza.Uscito &&
                        presenza.getLinkedPresenza().getStato() != RegistroPresenze.StatoPresenza.UscitoInAnticipo)
                {
                    RegistroPresenze newPresenza = new RegistroPresenze(RegistroPresenze.StatoPresenza.Disperso,
                            LocalDate.now(),
                            LocalDateTime.now(),
                            (short)LocalTime.now().getHour(),
                            presenza.getLinkedBambino(),
                            currentGita);
                    presenza.setLinkedPresenza(newPresenza);
                    registroPresenzeHashMap.put(presenza.getLinkedBambino(), newPresenza);
                }
            }
            CacheManager.getInstance(this).replaceStatoPresenzeMap(registroPresenzeHashMap);
            presenzeAdapter.replaceAll(CacheManager.getInstance(this).getPresenze());

            //Invia la richiesta di reset al server
            networkOperationVault.submitOperation(new NetworkOperation(new StartPresenzaCheckRequest(currentGita), response -> {
                networkOperationVault.operationDone(StartPresenzaCheckRequest.class);
                RefreshData();
            }, false));
        }
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
            runOnUiThread(() -> Toast.makeText(this, "Errore durante la connessione al server per aggiornare le presenze", Toast.LENGTH_LONG).show());


        if(response instanceof ListBambiniResponse)
        {
            CacheManager.getInstance(this).replaceBambini(((ListBambiniResponse) response).getPayload());
            runOnUiThread(() -> {
                this.presenzeAdapter.replaceAll(CacheManager.getInstance(this).getPresenze());
            });
        }
    }

    private void OnLastPresenzeUpdate(BaseResponse response)
    {
        networkOperationVault.operationDone(FilteredLastPresenzaRequest.class);

        if(!(response instanceof ListRegistroPresenzeResponse))
            runOnUiThread(() -> Toast.makeText(this, "Errore durante la connessione al server per aggiornare le presenze", Toast.LENGTH_LONG).show());
        else
        {
            CacheManager.getInstance(this).replaceStatoPresenzeMap(EntitiesHelper.presenzeToSearchMap(((ListRegistroPresenzeResponse) response).getPayload()));
            runOnUiThread(() -> {
                this.presenzeAdapter.replaceAll(CacheManager.getInstance(this).getPresenze());
            });
        }
    }

    private void OnCurrentGitaUpdate(BaseResponse response)
    {
        networkOperationVault.operationDone(GetCurrentGitaRequest.class);

        if(!(response instanceof ListGitaResponse))
            runOnUiThread(() -> Toast.makeText(this, "Errore durante la connessione al server per aggiornare le presenze", Toast.LENGTH_LONG).show());
        else
        {
            if(((ListGitaResponse) response).getPayload().size() > 0)
                CacheManager.getInstance(this).replaceCurrentGita(((ListGitaResponse) response).getPayload().get(0));
            else
                CacheManager.getInstance(this).replaceCurrentGita(null);
            runOnUiThread(this::checkForGita);
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
            currentTakePictureID = UUID.fromString(intent.getStringExtra(PresenzaViewHolder.EXTRA_UUID));

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

    private BroadcastReceiver changePresenzaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            int hashCode = intent.getIntExtra(PresenzaViewHolder.EXTRA_TUPLE_HASHCODE, 0);

            BambinoGruppoTuple refTuple = null;
            for (BambinoGruppoTuple tuple : CacheManager.getInstance(context).getPresenze())
                if(tuple.hashCode() == hashCode)
                {
                    refTuple = tuple;
                    break;
                }

            if(refTuple == null)
                return;

            final BambinoGruppoTuple bambinoGruppoTuple = refTuple;
            layoutQrCode.setAlpha(0f);
            layoutQrCode.setVisibility(View.VISIBLE);
            layoutQrCode.animate().alpha(1f).setDuration(300).withEndAction(() -> showQrScannerPartialResult(new BambinoQRUnit(bambinoGruppoTuple.getLinkedBambino()))).start();

            txtDate.setText(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm").format(LocalDateTime.now()));
        }
    };

    private void SetBambinoPresenza(BambinoQRUnit bambinoQRUnit, boolean isUscita)
    {
        int hashCode = Objects.hash(bambinoQRUnit.getID(), Persona.class);

        BambinoGruppoTuple refTuple = null;
        for (BambinoGruppoTuple tuple : CacheManager.getInstance(this).getPresenze())
            if(tuple.hashCode() == hashCode)
            {
                refTuple = tuple;
                break;
            }

        if(refTuple == null)
            return;

        Gita currentGita = CacheManager.getInstance(this).getCurrentGita();
        RegistroPresenze.StatoPresenza newStatoPresenza = StatoPresenzaUtils.getSuggestedStatoPresenzaFromPresenza(refTuple.getLinkedPresenza(), isUscita);

        if(newStatoPresenza == null) {
            presenzeAdapter.replaceAll(CacheManager.getInstance(this).getPresenze());
            return;
        }

        if(currentGita != null && refTuple.getLinkedPresenza().getStato() == RegistroPresenze.StatoPresenza.Disperso)
        {
            HashMap<Integer, MezzoDiTrasporto> gruppiToMezzi = CacheManager.getInstance(this).getGruppiToMezzoDiTraspostoMap();

            if(!isUscita)
                newStatoPresenza = selectedMezzoDiTrasporto == null ? RegistroPresenze.StatoPresenza.Presente :
                                gruppiToMezzi.get(refTuple.getLinkedBambino().getGruppoForeignKey()) == selectedMezzoDiTrasporto ? RegistroPresenze.StatoPresenza.Presente : RegistroPresenze.StatoPresenza.PresenteMezzoErrato;
            else
                newStatoPresenza = RegistroPresenze.StatoPresenza.UscitoInAnticipo;
        }

        RegistroPresenze toSend = null;
        HashMap<Bambino, RegistroPresenze> registroPresenzeHashMap = CacheManager.getInstance(this).getStatoPresenzaHashMap();
        if(refTuple.getLinkedPresenza().getStato() == RegistroPresenze.StatoPresenza.Disperso)
        {
            refTuple.getLinkedPresenza().setStato(newStatoPresenza);
            toSend = refTuple.getLinkedPresenza();
        }
        else
            toSend = new RegistroPresenze(newStatoPresenza, LocalDate.now(), LocalDateTime.now(), (short)LocalTime.now().getHour(), refTuple.getLinkedBambino(), currentGita);

        //Aggiorno con i dati che ho modificato
        registroPresenzeHashMap.put(refTuple.getLinkedBambino(), toSend);
        CacheManager.getInstance(this).replaceStatoPresenzeMap(registroPresenzeHashMap);

        CacheManager.getInstance(this).submitPersistentNetworkOperation(new NetworkOperation(new SetRegistroPresenzeRequest(toSend, false, toSend.consistecyHashCode()), response -> {
            RefreshData();
        }, false));

        this.presenzeAdapter.replaceAll(CacheManager.getInstance(this).getPresenze());
        this.presenzeAdapter.updateAll();
    }

    @Override
    public void onBackPressed()
    {
        if(layoutQrCode.getVisibility() == View.VISIBLE)
            closeQrScanner();
        else
            super.onBackPressed();
    }

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

    private List<BambinoGruppoTuple> filter(List<BambinoGruppoTuple> presenze, String query)
    {
        if(query == null || query.isEmpty())
            return presenze;

        final String lowerCaseQuery = query.trim().toLowerCase();
        final boolean spaced = lowerCaseQuery.trim().contains(" ");
        final List<BambinoGruppoTuple> filteredModelList = new ArrayList<>();
        for (BambinoGruppoTuple presenza : presenze)
        {
            try
            {
                //Se è un numero filtro per matricola
                Integer.parseInt(query);
                if(String.valueOf(presenza.getLinkedBambino().getID()).contains(query))
                    filteredModelList.add(presenza);
            }
            catch(NumberFormatException ex)
            {
                //Se non è un numero filtro per Nome, Cognome e Codice Fiscale

                if (presenza.getLinkedBambino().getNome().toLowerCase().contains(lowerCaseQuery)
                        || presenza.getLinkedBambino().getCognome().toLowerCase().contains(lowerCaseQuery)
                        || presenza.getLinkedBambino().getCodiceFiscale().toLowerCase().contains(lowerCaseQuery))
                    filteredModelList.add(presenza);

                if(!filteredModelList.contains(presenza) &&
                        spaced  &&
                        ((presenza.getLinkedBambino().getNome().toLowerCase() + " " + presenza.getLinkedBambino().getCognome().toLowerCase()).contains(lowerCaseQuery)
                           || (presenza.getLinkedBambino().getCognome().toLowerCase() + " " + presenza.getLinkedBambino().getNome().toLowerCase()).contains(lowerCaseQuery)))
                    filteredModelList.add(presenza);
            }

        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextChange(String query)
    {
        final List<BambinoGruppoTuple> filteredModelList = filter(CacheManager.getInstance(this).getPresenze(), query);
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

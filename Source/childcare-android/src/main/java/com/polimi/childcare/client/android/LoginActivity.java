package com.polimi.childcare.client.android;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.polimi.childcare.client.shared.networking.ClientNetworkManager;

import java.security.Permission;
import java.security.Permissions;
import java.time.LocalDateTime;

//import com.polimi.childcare.client.shared.networking.ClientNetworkManager;

public class LoginActivity extends AppCompatActivity {

    private LinearLayout layoutLogo;
    private LinearLayout layoutConnection;

    private EditText txtServerAddress;
    private TextInputLayout txtLayoutServerAddress;
    private Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(ClientNetworkManager.getInstance().isConnected())
        {
            unlockApplication();
            return;
        }

        setContentView(R.layout.login_screen);

        layoutLogo = this.findViewById(R.id.layoutTitle);
        layoutConnection = this.findViewById(R.id.layoutConnection);
        txtServerAddress = this.findViewById(R.id.txtIpAddress);
        txtLayoutServerAddress = this.findViewById(R.id.txtLayoutServerAddress);
        btnConnect = this.findViewById(R.id.btnConnect);

        //Animazione ingresso
        if(layoutConnection != null && layoutLogo != null)
        {
            layoutLogo.animate().setStartDelay(1500).translationYBy(-TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, Resources.getSystem().getDisplayMetrics())).setDuration(500).start();
            layoutConnection.setY(Resources.getSystem().getDisplayMetrics().heightPixels + layoutConnection.getHeight());
            layoutConnection.animate().translationY(0).setStartDelay(1500).setDuration(500).start();
        }

        if(txtServerAddress != null && CacheManager.getInstance(this).getLastConnectionAddress() != null)
            txtServerAddress.setText(CacheManager.getInstance(this).getLastConnectionAddress());

        if(canRunOffline())
            btnConnect.setText("Avvia (Offline)");

        btnConnect.setOnClickListener((click) ->
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.INTERNET)) {

                    // Permission is not granted
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.INTERNET},
                            0);
                }
                else
                    // Permission is not granted
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.INTERNET},
                            0);

                txtLayoutServerAddress.setError("Permessi di connessione necessari");
                return;
            }

            //Le operazioni di rete su Android devono essere sempre su altri thread
            AsyncTask.execute(() ->
            {
                if (ClientNetworkManager.getInstance().tryConnect(txtServerAddress.getText().toString(), 55403))
                {
                    CacheManager.getInstance(this).replaceLastConnectionAddress(txtServerAddress.getText().toString());
                    unlockApplication();
                }
                else
                {
                    if(canRunOffline())
                        unlockApplication();
                    else
                        runOnUiThread(() -> txtLayoutServerAddress.setError("Impossibile connettersi al server"));
                }
            });

        });
    }

    //Se sono già connesso salto la schermata di login o se ho una cache ancora valida (massimo 2h)
    private boolean canRunOffline()
    {
        return CacheManager.getInstance(this).getLastUpdate().plusHours(2).isAfter(LocalDateTime.now()) && CacheManager.getInstance(this).getBambini().size() > 0;
    }

    private void unlockApplication()
    {
        startActivity(new Intent(this, PresenzeActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 0)
        {
            if(grantResults.length > 0 && grantResults[0] > 0)
            {
                //Ripremo il bottone
                btnConnect.callOnClick();
            }
        }
    }
}

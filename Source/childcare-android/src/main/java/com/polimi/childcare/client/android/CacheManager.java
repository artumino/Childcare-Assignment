package com.polimi.childcare.client.android;

import android.content.Context;
import com.polimi.childcare.client.android.tuples.BambinoGruppoTuple;
import com.polimi.childcare.client.shared.networking.ClientNetworkManager;
import com.polimi.childcare.client.shared.networking.NetworkOperation;
import com.polimi.childcare.shared.entities.*;
import com.polimi.childcare.shared.networking.responses.BaseResponse;
import com.polimi.childcare.shared.serialization.SerializationUtils;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Cache di tutte le liste che vengono utilizzate dall'applicazione
 */
public class CacheManager implements Serializable
{
    private transient static CacheManager _instance;

    /**
     * Ottiene il singleton dalla memoria, dal disco rigido o ne crea una nuova istanza
     * @param context Contesto Android dell'applicazione
     * @return Istanza del singleton
     */
    public static CacheManager getInstance(Context context)
    {
        return (_instance != null) ? _instance :
                ((_instance = loadFromDisk(context)) != null ?
                                _instance : (_instance = new CacheManager(context)));
    }

    //Persistent Networking
    private ArrayList<NetworkOperation> persistentOperations = new ArrayList<>();

    private ArrayList<Bambino> bambini = new ArrayList<>();
    private HashMap<Bambino, RegistroPresenze> statoPresenzaHashMap = new HashMap<>();
    private ArrayList<Gruppo> gruppi = new ArrayList<>();
    private Gita currentGita;

    private long utcBambiniUpdateInstant = 0;
    private long utcPresenzaHashmapUpdateInstant = 0;
    private long utcGuppiUpdateInstant = 0;
    private long utcCurrentGitaUpdateInstant = 0;
    private long utcLastUpdate = 0;

    private transient List<BambinoGruppoTuple> tupleGenerate;
    private transient long lastPresenzaConstructionInstant = 0;

    private transient HashMap<NetworkOperation, NetworkOperation.INetworkOperationCallback> callbackHashMap = new HashMap<>();

    private transient Context context;

    CacheManager(Context context)
    {
        this.tupleGenerate = new ArrayList<>();
        this.context = context;
    }

    public LocalDateTime getLastUpdate()
    {
        return LocalDateTime.ofEpochSecond(this.utcLastUpdate, 0, ZoneOffset.UTC);
    }

    public LocalDateTime getLastBambiniUpdate()
    {
        return LocalDateTime.ofEpochSecond(this.utcBambiniUpdateInstant, 0, ZoneOffset.UTC);
    }

    public List<Bambino> getBambini()
    {
        return new ArrayList<>(bambini);
    }

    public List<BambinoGruppoTuple> getPresenze()
    {
        if(lastPresenzaConstructionInstant > utcLastUpdate)
            return new ArrayList<>(tupleGenerate);

        List<BambinoGruppoTuple> presenzeList = new ArrayList<>(bambini.size());

        for(Bambino bambino : bambini)
        {
            BambinoGruppoTuple presenza = null;
            if (statoPresenzaHashMap != null && statoPresenzaHashMap.containsKey(bambino))
                presenza = new BambinoGruppoTuple(bambino, statoPresenzaHashMap.get(bambino));
            else
                presenza = new BambinoGruppoTuple(bambino);

            presenzeList.add(presenza);
        }

        tupleGenerate = new ArrayList<>(presenzeList);
        lastPresenzaConstructionInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();

        return presenzeList;
    }

    public List<Gruppo> geGruppi()
    {
        return new ArrayList<>(gruppi);
    }

    public Gita getCurrentGita() {
        return this.currentGita;
    }

    public HashMap<Bambino,RegistroPresenze> getStatoPresenzaHashMap() { return new HashMap<>(statoPresenzaHashMap); }

    public void replaceBambini(List<Bambino> bambini)
    {
        this.bambini = new ArrayList<>(bambini);
        utcBambiniUpdateInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
        utcLastUpdate = utcBambiniUpdateInstant;
        updateState();
    }

    public void replaceStatoPresenzeMap(HashMap<Bambino, RegistroPresenze> statoPresenzaHashMap)
    {
        this.statoPresenzaHashMap = new HashMap<>(statoPresenzaHashMap);
        utcPresenzaHashmapUpdateInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
        utcLastUpdate = utcPresenzaHashmapUpdateInstant;
        updateState();
    }

    public void replaceGruppi(List<Gruppo> gruppi)
    {
        this.gruppi = new ArrayList<>(gruppi);
        utcGuppiUpdateInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
        utcLastUpdate = utcGuppiUpdateInstant;
        updateState();
    }

    public void replaceCurrentGita(Gita gita)
    {
        this.currentGita = gita;
        utcCurrentGitaUpdateInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
        utcLastUpdate = utcCurrentGitaUpdateInstant;
        updateState();
    }

    public HashMap<Integer, MezzoDiTrasporto> getGruppiToMezzoDiTraspostoMap()
    {
        HashMap<Integer, MezzoDiTrasporto> integerMezzoDiTrasportoHashMap = new HashMap<>();
        if(this.currentGita != null && this.currentGita.getPianiViaggi() != null)
        {
            for(PianoViaggi pianoViaggi : this.currentGita.getPianiViaggi())
                integerMezzoDiTrasportoHashMap.put(pianoViaggi.getGruppoForeignKey(), pianoViaggi.getMezzo());
        }
        return integerMezzoDiTrasportoHashMap;
    }

    public void submitPersistentNetworkOperation(NetworkOperation operation)
    {
        persistentOperations.add(operation);
        operation.setCallback(response -> PersistedNetworkOperationCompleted(operation, response));
        ClientNetworkManager.getInstance().submitOperation(operation);
        updateState();
    }

    /**
     * Salvo lo stato della classe su disco
     */
    private void updateState()
    {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(context.getFilesDir(), "cache.cache"));

            byte[] classData = SerializationUtils.serializeToByteArray(this);
            if(classData != null)
                fileOutputStream.write(classData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerAllPersistedNetworkOperations()
    {
        for(NetworkOperation operation : persistentOperations)
        {
            operation.setCallback(response -> PersistedNetworkOperationCompleted(operation, response));
            ClientNetworkManager.getInstance().submitOperation(operation);
        }
    }

    private void PersistedNetworkOperationCompleted(NetworkOperation operation, BaseResponse response)
    {
        persistentOperations.remove(operation);
        if(callbackHashMap.containsKey(operation))
            callbackHashMap.get(operation).OnResult(response);
        callbackHashMap.remove(operation);
        updateState();
    }

    private static CacheManager loadFromDisk(Context context)
    {
        BufferedInputStream buf = null;
        CacheManager storedCacheManager = null;

        File file = new File(context.getFilesDir(), "cache.cache");
        int size = (int) file.length();
        byte[] bytes = new byte[size];

        try {
            buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);

            storedCacheManager = SerializationUtils.deserializeByteArray(bytes, CacheManager.class);

            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            try {
                if (buf != null) {
                    buf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(storedCacheManager != null)
        {
            storedCacheManager.context = context;
            storedCacheManager.registerAllPersistedNetworkOperations();
        }

        return storedCacheManager;
    }
}

package com.polimi.childcare.client.android;

import android.content.Context;
import com.polimi.childcare.shared.entities.*;
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
                                _instance : (_instance = new CacheManager()));
    }

    CacheManager()
    {

    }

    private ArrayList<Bambino> bambini = new ArrayList<>();
    private HashMap<Bambino, RegistroPresenze> statoPresenzaHashMap = new HashMap<>();
    private ArrayList<Gruppo> gruppi = new ArrayList<>();
    private Gita currentGita;

    private long utcBambiniUpdateInstant = 0;
    private long utcPresenzaHashmapUpdateInstant = 0;
    private long utcGuppiUpdateInstant = 0;
    private long utcCurrentGitaUpdateInstant = 0;
    private long utcLastUpdate = 0;

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

    public List<RegistroPresenze> getPresenze()
    {
        List<RegistroPresenze> presenzeList = new ArrayList<>(bambini.size());

        for(Bambino bambino : bambini)
        {
            RegistroPresenze presenza = null;
            if (statoPresenzaHashMap != null && statoPresenzaHashMap.containsKey(bambino))
                presenza = statoPresenzaHashMap.get(bambino);
            else
                presenza = new RegistroPresenze(RegistroPresenze.StatoPresenza.Assente, LocalDate.now(), LocalDate.now().atStartOfDay(), (short)0, bambino, currentGita);

            presenza.setBambino(bambino);
            presenzeList.add(presenza);
        }

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

    public void replaceBambini(Context context, List<Bambino> bambini)
    {
        this.bambini = new ArrayList<>(bambini);
        utcBambiniUpdateInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
        utcLastUpdate = utcBambiniUpdateInstant;
        updateState(context);
    }

    public void replaceStatoPresenzeMap(Context context, HashMap<Bambino, RegistroPresenze> statoPresenzaHashMap)
    {
        this.statoPresenzaHashMap = new HashMap<>(statoPresenzaHashMap);
        utcPresenzaHashmapUpdateInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
        utcLastUpdate = utcPresenzaHashmapUpdateInstant;
        updateState(context);
    }

    public void replaceGruppi(Context context, List<Gruppo> gruppi)
    {
        this.gruppi = new ArrayList<>(gruppi);
        utcGuppiUpdateInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
        utcLastUpdate = utcGuppiUpdateInstant;
        updateState(context);
    }

    public void replaceCurrentGita(Context context, Gita gita)
    {
        this.currentGita = gita;
        utcCurrentGitaUpdateInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
        utcLastUpdate = utcCurrentGitaUpdateInstant;
        updateState(context);
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

    /**
     * Salvo lo stato della classe su disco
     * @param context Contesto Android dell'applicazione
     */
    private void updateState(Context context)
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

        return storedCacheManager;
    }
}

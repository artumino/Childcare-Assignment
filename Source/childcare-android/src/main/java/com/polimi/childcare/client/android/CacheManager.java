package com.polimi.childcare.client.android;

import android.content.Context;
import com.polimi.childcare.shared.entities.Bambino;
import com.polimi.childcare.shared.serialization.SerializationUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Cache di tutte le liste che vengono utilizzate dall'applicazione
 */
public class CacheManager implements Serializable
{
    private transient static CacheManager _instance;

    /**
     * Ottiene il singleton dalla memoria, dal disco rigido o ne crea una nuova istanza
     * @param context
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
    private long utcBambiniUpdateInstant = 0;
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

    public void replaceBambini(Context context, List<Bambino> bambini)
    {
        this.bambini = new ArrayList<>(bambini);
        utcBambiniUpdateInstant = LocalDateTime.now().toInstant(ZoneOffset.UTC).getEpochSecond();
        utcLastUpdate = utcBambiniUpdateInstant;
        updateState(context);
    }

    /**
     * Salvo lo stato della classe su disco
     * @param context
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

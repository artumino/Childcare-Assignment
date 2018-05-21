package com.polimi.childcare.client.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Classe usata per ottenere/salvare immagini in locale legate ad un ID generato
 */
public class ImageStore
{
    private static ImageStore _instance;
    public static ImageStore getInstance() { return (_instance != null) ? _instance : (_instance = new ImageStore()); }

    ImageStore()
    {

    }

    /**
     * Salva un immagine per un ID
     * @param uuid ID con cui salvare l'immagine
     * @param image Immagine da salvare
     */
    public void SaveImage(UUID uuid, Bitmap image)
    {
        FileOutputStream file = null;
        try {
            file = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), uuid.toString() + ".thumb").getAbsolutePath());
            image.compress(Bitmap.CompressFormat.PNG, 100, file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Carica l'immagine legata ad un ID
     * @param uuid ID associato all'immagine
     * @return Bitmap se immagine presente, null in caso contrario
     */
    public Bitmap GetImage(UUID uuid)
    {
        return BitmapFactory.decodeFile(new File(Environment.getExternalStorageDirectory(), uuid.toString() + ".thumb").getAbsolutePath());
    }
}

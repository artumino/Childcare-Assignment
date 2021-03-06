package com.polimi.childcare.shared.serialization;

import java.io.*;

public class SerializationUtils
{
    public static <T extends Serializable> byte[] serializeToByteArray(T object)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            return bos.toByteArray();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                bos.close();
            } catch (IOException ignored) {}
        }
        return null;
    }

    public static <T extends Serializable> T deserializeByteArray(byte[] arr, Class<T> tClass)
    {
        ByteArrayInputStream bis = new ByteArrayInputStream(arr);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            Object obj = in.readObject();

            //Eseguo il cast alla classe richiesta solo se posso effettivamente farlo
            if(obj != null && tClass.isAssignableFrom(obj.getClass()))
                return (T)obj;
        }
        catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ignored) {}
        }
        return null;
    }
}

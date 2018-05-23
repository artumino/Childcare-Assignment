package com.polimi.childcare.client.shared.qrcode;

import com.polimi.childcare.shared.entities.Bambino;

import java.io.Serializable;

/**
 * Classe usata per lo scambio dati tramite QRCode
 */
public class BambinoQRUnit implements Serializable
{
    private int ID;
    private String Nome;
    private String Cognome;
    private String CodiceFiscale;

    public BambinoQRUnit(Bambino from)
    {
        this.ID = from.getID();
        this.Nome = from.getNome();
        this.Cognome = from.getCognome();
        this.CodiceFiscale = from.getCodiceFiscale();
    }

    public int getID() {
        return ID;
    }

    public String getNome() {
        return Nome;
    }

    public String getCognome() {
        return Cognome;
    }

    public String getCodiceFiscale() {
        return CodiceFiscale;
    }
}

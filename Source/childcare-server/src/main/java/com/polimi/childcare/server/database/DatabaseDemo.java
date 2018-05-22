package com.polimi.childcare.server.database;

import com.polimi.childcare.shared.entities.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Classe di Debug utilizzata per generare dati demo all'interno del DB
 */
public class DatabaseDemo
{
    public static void runDemoGeneration(int approximateBambiniCount)
    {
        List<NumeroTelefono> telefoni = generateTelefoni(approximateBambiniCount * 3);

        //Inserimenti in transazione
        DatabaseSession.getInstance().insertAll(telefoni);
    }

    private static List<NumeroTelefono> generateTelefoni(int count)
    {
        ArrayList<NumeroTelefono> telefoni = new ArrayList<>(count);
        ArrayList<String> telefoniStr = new ArrayList<>(count);
        Random rnd = new Random();
        StringBuilder telephoneNumber;
        for(int i = 0; i < count; i++)
        {
            String newPhoneNumber;
            do
            {
                telephoneNumber = new StringBuilder(13);
                telephoneNumber.append("+39");
                for(int j = 0; j < 10; j++)
                    telephoneNumber.append((char)('0' + rnd.nextInt(10)));
                newPhoneNumber = telephoneNumber.toString();
            } while (telefoniStr.contains(newPhoneNumber));
            telefoniStr.add(newPhoneNumber);
        }

        for(String telefono : telefoniStr)
            telefoni.add(new NumeroTelefono(telefono));

        return telefoni;
    }

    private static List<ReazioneAvversa> generateReazioniAvverse()
    {
        LinkedList<ReazioneAvversa> reazioniAvverse = new LinkedList<>();
        reazioniAvverse.add(new ReazioneAvversa("Mela", "CROSS-REATTIVITÀ DOCUMENTATA: Patata, carota, polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Carota", "CROSS-REATTIVITÀ DOCUMENTATA: Sedano, anice, mela, patata, segale, frumento, ananas, avocado, polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Cereali", "CROSS-REATTIVITÀ DOCUMENTATA: Frumento, segale, orzo, avena, granoturco, riso, polline di graminacee, corrispondenti pollini"));
        reazioniAvverse.add(new ReazioneAvversa("Merluzzo", "CROSS-REATTIVITÀ DOCUMENTATA: Anguilla, sgombro, salmone, trota, tonno"));
        reazioniAvverse.add(new ReazioneAvversa("Latte di mucca", "CROSS-REATTIVITÀ DOCUMENTATA: Latte d’asina, capra, di altri animali simili"));
        reazioniAvverse.add(new ReazioneAvversa("Uova", "CROSS-REATTIVITÀ DOCUMENTATA: Albume, lisozima, tuorlo, ovoalbumina, ovomucoide"));
        reazioniAvverse.add(new ReazioneAvversa("Aglio", "CROSS-REATTIVITÀ DOCUMENTATA: Cipolla, asparago"));
        reazioniAvverse.add(new ReazioneAvversa("Miele", "CROSS-REATTIVITÀ DOCUMENTATA: Contaminazione da polline di composite"));
        reazioniAvverse.add(new ReazioneAvversa("Piselli", "CROSS-REATTIVITÀ DOCUMENTATA: Lenticchie, liquirizia, semi di soia, fagioli bianchi, noccioline americane, finocchio"));
        reazioniAvverse.add(new ReazioneAvversa("Pesca", "CROSS-REATTIVITÀ DOCUMENTATA: Albicocca, prugna, banana, guava"));
        reazioniAvverse.add(new ReazioneAvversa("Noce americana", "CROSS-REATTIVITÀ DOCUMENTATA: Noccioline, noce, noce brasiliana"));
        reazioniAvverse.add(new ReazioneAvversa("Riso", "CROSS-REATTIVITÀ DOCUMENTATA: Cereali, granoturco, polline di segale"));
        reazioniAvverse.add(new ReazioneAvversa("Gamberetto", "CROSS-REATTIVITÀ DOCUMENTATA: Granchio comune, aragosta, calamaro, gambero, acari"));

        reazioniAvverse.add(new ReazioneAvversa("Brassicacea", "All’interno della famiglia: cavolo (verza, capuccio, rapa, cruciferae fiore, broccolo di bruxelles), rapa, colza, ravizzone e loro olii"));
        reazioniAvverse.add(new ReazioneAvversa("Compositae", "asteraceae – All’interno della famiglia: camomilla, carciofo, cicoria, lattuga, girasole (semi ed olio) dragoncello e con i corrispondenti pollini"));
        reazioniAvverse.add(new ReazioneAvversa("Cucurbitace", "All’interno della famiglia: zucchino, zucca, melone, anguria, cetriolo, e con il polline di Gramineae e con il pomodoro (fam. solanaceae)"));
        reazioniAvverse.add(new ReazioneAvversa("Gramineae", "poaceae (fam. solanaceae) – All’interno della famiglia: frumento, mais, segale, orzo, riso, avena, con il polline di Gramineae e con il pomodoro"));
        reazioniAvverse.add(new ReazioneAvversa("Leguminosea", "papilionaceae – All’interno della famiglia: fagioli, soia, arachidi, piselli, lenticchie, liquerizia, gomme"));
        reazioniAvverse.add(new ReazioneAvversa("Liliaceae", "All’interno della famiglia: asparago, porro, cipolla, aglio, ecc."));
        reazioniAvverse.add(new ReazioneAvversa("Solanaceae", "All’interno della famiglia: patata, melanzana, peperone, pomodoro e con le graminaceae"));
        reazioniAvverse.add(new ReazioneAvversa("Rutaceae", "All’interno della famiglia: limone, mandarino, pompelmo, arancia, cedro e con il vischio (fam. Loranthaceae)"));
        reazioniAvverse.add(new ReazioneAvversa("Rosaceae", "All’interno della famiglia: mandorle, mela, albicocca, pesca, susina, ciliegia, prugna, fragola e con il polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Umbellifera", "apiaceae – All’interno della famiglia: anice, carota, finocchio, sedano, prezzemolo e con il polline di artemisia"));
        reazioniAvverse.add(new ReazioneAvversa("Grano", "segale – Papaina, bromelina, e polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Banana", "castagna, kiwi, avocado – Tra di loro,con il lattice e il ficus beniamina"));
        reazioniAvverse.add(new ReazioneAvversa("Banana", "Melone e polline di Compositeae"));
        reazioniAvverse.add(new ReazioneAvversa("Carota", "Lattuga, sedano, anice, mela, patata, segale, frumento, ananas, avocado, e polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Mela", "Patata, carota, sedano, e con il polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Semi e noci", "Fra di loro (noce, noce americana, nocciola, mandorla) e con l’arachide (fam. leguminoseae)"));
        reazioniAvverse.add(new ReazioneAvversa("Sedano", "Carota, cumino, anice, finocchio, coriandolo, pepe, noce moscata, zenzero, cannella"));
        reazioniAvverse.add(new ReazioneAvversa("Nocciole", "Segale, semi di sesamo, kiwi, semi di papavero"));
        reazioniAvverse.add(new ReazioneAvversa("Latte", "Fra di loro (latte di mucca, capra, ecc.)"));
        reazioniAvverse.add(new ReazioneAvversa("Uova", "Singole proteine, ovoalbumina, ovomucoide, e con le piume ed il siero di volatili"));
        reazioniAvverse.add(new ReazioneAvversa("Carni", "Fra di loro (carne di maiale, di bue, di coniglio, ecc.) e fra carne di bovino e latte"));
        reazioniAvverse.add(new ReazioneAvversa("Crustacea", "All’interno della famiglia: gambero, aragosta, granchio, calamaro ecc."));
        reazioniAvverse.add(new ReazioneAvversa("Gasteropodi", "Acari"));
        reazioniAvverse.add(new ReazioneAvversa("Molluschi", "Tra di loro ( mitili, vongole, ostriche, ecc.)"));
        reazioniAvverse.add(new ReazioneAvversa("Pesci", "Tra di loro (merluzzo, sgombro, salmone, trota, tonno, ecc.)"));
        reazioniAvverse.add(new ReazioneAvversa("Surimi", "Merluzzo"));
        return reazioniAvverse;
    }

    private static List<Pediatra> generatePediatri(int count)
    {
        ArrayList<Pediatra> pediatri = new ArrayList<>(count);

        return pediatri;
    }

    private static List<Contatto> generateContatti(int count)
    {
        ArrayList<Contatto> contatti = new ArrayList<>(count);

        return contatti;
    }

    private static List<Addetto> generateAddetti(int count, List<ReazioneAvversa> reazioniAvverse)
    {
        ArrayList<Addetto> addetti = new ArrayList<>(count);

        return addetti;
    }

    private static List<Genitore> generateGenitori(int count, List<ReazioneAvversa> reazioniAvverse)
    {
        ArrayList<Genitore> genitori = new ArrayList<>(count);

        return genitori;
    }

    private static List<Bambino> generateBambini(int count, List<Genitore> generatedGenitori, List<Contatto> generatedContatti, List<ReazioneAvversa> reazioniAvverse)
    {
        ArrayList<Bambino> bambini = new ArrayList<>(count);

        return bambini;
    }
}

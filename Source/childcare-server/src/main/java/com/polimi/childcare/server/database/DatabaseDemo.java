package com.polimi.childcare.server.database;

import com.polimi.childcare.shared.entities.*;
import org.hibernate.Session;
import org.hibernate.sql.Delete;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.xml.crypto.Data;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Classe di Debug utilizzata per generare dati demo all'interno del DB
 */
public class DatabaseDemo
{
    //region Array Costanti
    private static String[] nomiComuni = new String[] { "Pino", "Maria", "Federico", "Jacopo", "Anna", "Marta", "Marco",
                                                        "Davide", "Pippo", "Luigi", "Simone", "Antonio", "Marzia",
                                                        "Marcello", "Giacobbe", "Edoardo", "Kevin", "Mirco", "Mirko",
                                                        "Alberto", "Elia", "Katia", "Lucia", "Sara", "Pinco"};

    private static String[] cognomiComuni = new String[] { "Pinto", "Sabbatini", "Bucco", "Lettiere", "Monaldo", "Rossi",
                                                            "Loggia", "Trevisani", "Pallino", "Castiglione", "Mazzi", "Toscani",
                                                            "Lucciano", "Lorenzo", "Gaetano", "Fiorentino", "Pancrazio",
                                                            "Ladislao", "Bellucci", "Belloni", "Enrico"};

    private static String[][] cittaProvincia = new String[][]
    {
            new String[] {"Agrigento","AG"}, new String[] {"Alessandria","AL"}, new String[] {"Ancona","AN"},
            new String[] {"Aosta","AO"}, new String[] {"Arezzo","AR"}, new String[] {"Ascoli Piceno","AP"},
            new String[] {"Asti","AT"}, new String[] {"Avellino","AV"}, new String[] {"Bari","BA"},
            new String[] {"Barletta-Andria-Trani","BT"}, new String[] {"Belluno","BL"}, new String[] {"Benevento","BN"},
            new String[] {"Bergamo","BG"}, new String[] {"Biella","BI"}, new String[] {"Bologna","BO"},
            new String[] {"Bolzano","BZ"}, new String[] {"Brescia","BS"}, new String[] {"Brindisi","BR"},
            new String[] {"Cagliari","CA"}, new String[] {"Caltanissetta","CL"}, new String[] {"Campobasso","CB"},
            new String[] {"Caserta","CE"}, new String[] {"Catania","CT"}, new String[] {"Catanzaro","CZ"},
            new String[] {"Chieti","CH"}, new String[] {"Como","CO"}, new String[] {"Cosenza","CS"},
            new String[] {"Cremona","CR"}, new String[] {"Crotone","KR"}, new String[] {"Cuneo","CN"}, new String[] {"Enna","EN"},
            new String[] {"Fermo","FM"}, new String[] {"Ferrara","FE"}, new String[] {"Firenze","FI"}, new String[] {"Foggia","FG"},
            new String[] {"Forli Cesena","FC"}, new String[] {"Frosinone","FR"}, new String[] {"Genova","GE"}, new String[] {"Gorizia","GO"},
            new String[] {"Grosseto","GR"}, new String[] {"Imperia","IM"}, new String[] {"Isernia","IS"}, new String[] {"L'Aquila","AQ"},
            new String[] {"La Spezia","SP"}, new String[] {"Latina","LT"}, new String[] {"Lecce","LE"}, new String[] {"Lecco","LC"},
            new String[] {"Livorno","LI"}, new String[] {"Lodi","LO"}, new String[] {"Lucca","LU"}, new String[] {"Macerata","MC"},
            new String[] {"Mantova","MN"}, new String[] {"Massa Carrara","MS"}, new String[] {"Matera","MT"}, new String[] {"Messina","ME"},
            new String[] {"Milano","MI"}, new String[] {"Modena","MO"}, new String[] {"Monza-Brianza","MB"}, new String[] {"Napoli","NA"},
            new String[] {"Novara","NO"}, new String[] {"Nuoro","NU"}, new String[] {"Oristano","OR"}, new String[] {"Padova","PD"},
            new String[] {"Palermo","PA"}, new String[] {"Parma","PR"}, new String[] {"Pavia","PV"}, new String[] {"Perugia","PG"},
            new String[] {"Pesaro Urbino","PU"}, new String[] {"Pescara","PE"}, new String[] {"Piacenza","PC"}, new String[] {"Pisa","PI"},
            new String[] {"Pistoia","PT"}, new String[] {"Pordenone","PN"}, new String[] {"Potenza","PZ"}, new String[] {"Prato","PO"},
            new String[] {"Ragusa","RG"}, new String[] {"Ravenna","RA"}, new String[] {"Reggio Calabria","RC"}, new String[] {"Reggio Emilia","RE"},
            new String[] {"Rieti","RI"}, new String[] {"Rimini","RN"}, new String[] {"Roma","RM"}, new String[] {"Rovigo","RO"},
            new String[] {"Salerno","SA"}, new String[] {"Sassari","SS"}, new String[] {"Savona","SV"}, new String[] {"Siena","SI"},
            new String[] {"Siracusa","SR"}, new String[] {"Sondrio","SO"}, new String[] {"Taranto","TA"}, new String[] {"Teramo","TE"},
            new String[] {"Terni","TR"}, new String[] {"Torino","TO"}, new String[] {"Trapani","TP"}, new String[] {"Trento","TN"},
            new String[] {"Treviso","TV"}, new String[] {"Trieste","TS"}, new String[] {"Udine","UD"}, new String[] {"Varese","VA"}

    };
    //endregion

    public static void runDemoGeneration(int approximateBambiniCount)
    {
        Locale.setDefault(new Locale("it_IT"));

        //Genero reazioni avverse
        List<ReazioneAvversa> reazioniAvverse = null;
        long ReazioniCount = 0;
        Session session = DatabaseSession.getInstance().openSession();
        ReazioniCount = DatabaseSession.getInstance().query(ReazioneAvversa.class, session).count();

        if(ReazioniCount > 0)
        {
            reazioniAvverse = new ArrayList<>((int)ReazioniCount);
            reazioniAvverse = DatabaseSession.getInstance().query(ReazioneAvversa.class, session).toList();
        }
        session.close();

        if(reazioniAvverse == null)
        {
            reazioniAvverse = generateReazioniAvverse();
            DatabaseSession.getInstance().insertAll(reazioniAvverse);
        }

        //Genero Addetti
        List<Addetto> addetti = generateAddetti(approximateBambiniCount / 5, reazioniAvverse, generateTelefoni(approximateBambiniCount));
        DatabaseSession.getInstance().insertAll(addetti);

        //Genero Contatti e Pediatri
        List<Pediatra> pediatri = generatePediatri(1 + (approximateBambiniCount / 25), generateTelefoni(approximateBambiniCount));
        DatabaseSession.getInstance().insertAll(pediatri);

        //Genero bambini dai genitori(evitando genitori orfani)
        List<Bambino> bambini = generateBambini(approximateBambiniCount, generateGenitori((approximateBambiniCount / 3), reazioniAvverse, generateTelefoni(approximateBambiniCount)), generateContatti((int)(approximateBambiniCount * 1.5), generateTelefoni(approximateBambiniCount)), pediatri, reazioniAvverse, generateTelefoni(approximateBambiniCount));
        DatabaseSession.getInstance().insertAll(bambini);

        //Inserisco tutti i contatti
        ArrayList<Contatto> contatti = new ArrayList<>(approximateBambiniCount);
        for(Bambino bambino : bambini)
            for (Contatto contatto : bambino.getContatti())
                if(!contatti.contains(contatto))
                    contatti.add(contatto);
        DatabaseSession.getInstance().insertAll(contatti);
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

    //region Reazioni Avverse
    private static List<ReazioneAvversa> generateReazioniAvverse()
    {
        ArrayList<ReazioneAvversa> reazioniAvverse = new ArrayList<>();

        reazioniAvverse.add(new ReazioneAvversa("Mela", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Patata, carota, polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Carota", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Sedano, anice, mela, patata, segale, frumento, ananas, avocado, polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Cereali", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Frumento, segale, orzo, avena, granoturco, riso, polline di graminacee, corrispondenti pollini"));
        reazioniAvverse.add(new ReazioneAvversa("Merluzzo", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Anguilla, sgombro, salmone, trota, tonno"));
        reazioniAvverse.add(new ReazioneAvversa("Latte di mucca", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Latte d'asina, capra, di altri animali simili"));
        reazioniAvverse.add(new ReazioneAvversa("Uova", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Albume, lisozima, tuorlo, ovoalbumina, ovomucoide"));
        reazioniAvverse.add(new ReazioneAvversa("Aglio", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Cipolla, asparago"));
        reazioniAvverse.add(new ReazioneAvversa("Miele", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Contaminazione da polline di composite"));
        reazioniAvverse.add(new ReazioneAvversa("Piselli", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Lenticchie, liquirizia, semi di soia, fagioli bianchi, noccioline americane, finocchio"));
        reazioniAvverse.add(new ReazioneAvversa("Pesca", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Albicocca, prugna, banana, guava"));
        reazioniAvverse.add(new ReazioneAvversa("Noce americana", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Noccioline, noce, noce brasiliana"));
        reazioniAvverse.add(new ReazioneAvversa("Riso", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Cereali, granoturco, polline di segale"));
        reazioniAvverse.add(new ReazioneAvversa("Gamberetto", "CROSS-REATTIVIT\u00c0 DOCUMENTATA: Granchio comune, aragosta, calamaro, gambero, acari"));

        reazioniAvverse.add(new ReazioneAvversa("Brassicacea", "All'interno della famiglia: cavolo (verza, capuccio, rapa, cruciferae fiore, broccolo di bruxelles), rapa, colza, ravizzone e loro olii"));
        reazioniAvverse.add(new ReazioneAvversa("Compositae", "asteraceae - All'interno della famiglia: camomilla, carciofo, cicoria, lattuga, girasole (semi ed olio) dragoncello e con i corrispondenti pollini"));
        reazioniAvverse.add(new ReazioneAvversa("Cucurbitace", "All'interno della famiglia: zucchino, zucca, melone, anguria, cetriolo, e con il polline di Gramineae e con il pomodoro (fam. solanaceae)"));
        reazioniAvverse.add(new ReazioneAvversa("Gramineae", "poaceae (fam. solanaceae) - All'interno della famiglia: frumento, mais, segale, orzo, riso, avena, con il polline di Gramineae e con il pomodoro"));
        reazioniAvverse.add(new ReazioneAvversa("Leguminosea", "papilionaceae - All'interno della famiglia: fagioli, soia, arachidi, piselli, lenticchie, liquerizia, gomme"));
        reazioniAvverse.add(new ReazioneAvversa("Liliaceae", "All'interno della famiglia: asparago, porro, cipolla, aglio, ecc."));
        reazioniAvverse.add(new ReazioneAvversa("Solanaceae", "All'interno della famiglia: patata, melanzana, peperone, pomodoro e con le graminaceae"));
        reazioniAvverse.add(new ReazioneAvversa("Rutaceae", "All'interno della famiglia: limone, mandarino, pompelmo, arancia, cedro e con il vischio (fam. Loranthaceae)"));
        reazioniAvverse.add(new ReazioneAvversa("Rosaceae", "All'interno della famiglia: mandorle, mela, albicocca, pesca, susina, ciliegia, prugna, fragola e con il polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Umbellifera", "apiaceae - All'interno della famiglia: anice, carota, finocchio, sedano, prezzemolo e con il polline di artemisia"));
        reazioniAvverse.add(new ReazioneAvversa("Grano", "segale - Papaina, bromelina, e polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Banana", "castagna, kiwi, avocado - Tra di loro,con il lattice e il ficus beniamina"));
        reazioniAvverse.add(new ReazioneAvversa("Banana", "Melone e polline di Compositeae"));
        reazioniAvverse.add(new ReazioneAvversa("Carota", "Lattuga, sedano, anice, mela, patata, segale, frumento, ananas, avocado, e polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Mela", "Patata, carota, sedano, e con il polline di betulla"));
        reazioniAvverse.add(new ReazioneAvversa("Semi e noci", "Fra di loro (noce, noce americana, nocciola, mandorla) e con l'arachide (fam. leguminoseae)"));
        reazioniAvverse.add(new ReazioneAvversa("Sedano", "Carota, cumino, anice, finocchio, coriandolo, pepe, noce moscata, zenzero, cannella"));
        reazioniAvverse.add(new ReazioneAvversa("Nocciole", "Segale, semi di sesamo, kiwi, semi di papavero"));
        reazioniAvverse.add(new ReazioneAvversa("Latte", "Fra di loro (latte di mucca, capra, ecc.)"));
        reazioniAvverse.add(new ReazioneAvversa("Uova", "Singole proteine, ovoalbumina, ovomucoide, e con le piume ed il siero di volatili"));
        reazioniAvverse.add(new ReazioneAvversa("Carni", "Fra di loro (carne di maiale, di bue, di coniglio, ecc.) e fra carne di bovino e latte"));
        reazioniAvverse.add(new ReazioneAvversa("Crustacea", "All'interno della famiglia: gambero, aragosta, granchio, calamaro ecc."));
        reazioniAvverse.add(new ReazioneAvversa("Gasteropodi", "Acari"));
        reazioniAvverse.add(new ReazioneAvversa("Molluschi", "Tra di loro ( mitili, vongole, ostriche, ecc.)"));
        reazioniAvverse.add(new ReazioneAvversa("Pesci", "Tra di loro (merluzzo, sgombro, salmone, trota, tonno, ecc.)"));
        reazioniAvverse.add(new ReazioneAvversa("Surimi", "Merluzzo"));
        return reazioniAvverse;
    }
    //endregion

    //region Contatti e Pediatri
    private static void populateContatto(Contatto contatto, List<NumeroTelefono> numeriDiTelefonoDisponibili)
    {
        Random rnd = new Random();
        contatto.setNome(nomiComuni[rnd.nextInt(nomiComuni.length)]);
        contatto.setCognome(cognomiComuni[rnd.nextInt(cognomiComuni.length)]);
        contatto.setDescrizione("TEST" + (1000000 + rnd.nextInt(9000000)));
        int ComuneProvincia = rnd.nextInt(cittaProvincia.length);
        contatto.setIndirizzo("via " + nomiComuni[rnd.nextInt(nomiComuni.length)] + " " + cognomiComuni[rnd.nextInt(cognomiComuni.length)]
                                + " " + (1 + rnd.nextInt(200)) + ", " + cittaProvincia[ComuneProvincia][0] + ", Italia");

        //Calcola quanti numeri di telefono sono opportuni
        int numeroNumeriDiTelefono = 0;
        if(rnd.nextInt(1000) < 100)
            numeroNumeriDiTelefono = 2;
        else
            numeroNumeriDiTelefono = 1;

        for(int i = 0; i < numeroNumeriDiTelefono; i++)
        {
            NumeroTelefono telefono;
            do {
                telefono  = numeriDiTelefonoDisponibili.get(rnd.nextInt(numeriDiTelefonoDisponibili.size()));
            } while (contatto.getTelefoni().contains(telefono));
            contatto.addNumero(telefono);
        }
    }

    private static List<Contatto> generateContatti(int count, List<NumeroTelefono> telefoni)
    {
        ArrayList<Contatto> contatti = new ArrayList<>(count);
        for(int i = 0; i < count; i++)
        {
            Contatto contatto = new Contatto();
            populateContatto(contatto, telefoni);
            contatti.add(contatto);
        }
        return contatti;
    }

    private static List<Pediatra> generatePediatri(int count, List<NumeroTelefono> telefoni)
    {
        ArrayList<Pediatra> pediatri = new ArrayList<>(count);
        for(int i = 0; i < count; i++)
        {
            Pediatra pediatra = new Pediatra();
            populateContatto(pediatra, telefoni);
            pediatri.add(pediatra);
        }
        return pediatri;
    }

    //endregion

    //region Persone (Bambini, Genitori e Addetti)

    private static void populatePersona(Persona persona, List<ReazioneAvversa> reazioniAvverse, List<NumeroTelefono> numeriDiTelefonoDisponibili)
    {
        Random rnd = new Random();
        persona.setNome(nomiComuni[rnd.nextInt(nomiComuni.length)]);
        persona.setCognome(cognomiComuni[rnd.nextInt(cognomiComuni.length)]);
        persona.setCodiceFiscale("TEST" + (1000000 + rnd.nextInt(9000000)));
        persona.setCittadinanza("Italiana");
        persona.setDataNascita(LocalDateTime.now().minusDays(rnd.nextInt(18250)).withMinute(rnd.nextInt(59)).withHour(rnd.nextInt(23)).toLocalDate());
        int ComuneProvincia = rnd.nextInt(cittaProvincia.length);
        persona.setComune(cittaProvincia[ComuneProvincia][0]);
        persona.setProvincia(cittaProvincia[ComuneProvincia][1]);
        persona.setStato("Italia");
        persona.setSesso((byte)rnd.nextInt(3));
        persona.setResidenza("via " + nomiComuni[rnd.nextInt(nomiComuni.length)] + " " + cognomiComuni[rnd.nextInt(cognomiComuni.length)] + " " + (1 + rnd.nextInt(200)));

        //Calcola quanti numeri di telefono sono opportuni
        int numeroNumeriDiTelefono = 0;
        if(!(persona instanceof Bambino))
            if(rnd.nextInt(1000) < 100)
                numeroNumeriDiTelefono = 2;
            else
                numeroNumeriDiTelefono = 1;
        if(persona instanceof Bambino && rnd.nextInt(1000) < 25)
            numeroNumeriDiTelefono = 1;


        for(int i = 0; i < numeroNumeriDiTelefono; i++)
        {
            NumeroTelefono telefono;
            do {
                telefono  = numeriDiTelefonoDisponibili.get(rnd.nextInt(numeriDiTelefonoDisponibili.size()));
            } while (persona.getTelefoni().contains(telefono));
            persona.addTelefono(telefono);
        }

        //Calcola numero di reazioni avverse
        int numeroReazioniAvverse = 0;

        if(rnd.nextInt(10000) < 100)
            numeroReazioniAvverse = 2;
        else if(rnd.nextInt(1000) < 100)
            numeroReazioniAvverse = 1;

        ArrayList<ReazioneAvversa> reazioneAvverseAssegnate = new ArrayList<>(numeroReazioniAvverse);
        for(int i = 0; i < numeroReazioniAvverse; i++)
        {
            ReazioneAvversa reazioneAvversa;
            do {
                reazioneAvversa  = reazioniAvverse.get(rnd.nextInt(reazioniAvverse.size()));
            } while (reazioneAvverseAssegnate.contains(reazioneAvversa));
            reazioneAvverseAssegnate.add(reazioneAvversa);
            persona.unsafeAddDiagnosi(new Diagnosi(rnd.nextBoolean(), persona, reazioneAvversa));
        }
    }

    private static List<Addetto> generateAddetti(int count, List<ReazioneAvversa> reazioniAvverse, List<NumeroTelefono> telefoni)
    {
        ArrayList<Addetto> addetti = new ArrayList<>(count);
        for(int i = 0; i < count; i++)
        {
            Addetto addetto = new Addetto();
            populatePersona(addetto, reazioniAvverse, telefoni);
            addetti.add(addetto);
        }
        return addetti;
    }

    private static List<Genitore> generateGenitori(int count, List<ReazioneAvversa> reazioniAvverse, List<NumeroTelefono> telefoni)
    {
        ArrayList<Genitore> genitori = new ArrayList<>(count);
        for(int i = 0; i < count; i++)
        {
            Genitore genitore = new Genitore();
            populatePersona(genitore, reazioniAvverse, telefoni);
            genitori.add(genitore);
        }
        return genitori;
    }

    private static List<Bambino> generateBambini(int count, List<Genitore> generatedGenitori, List<Contatto> generatedContatti, List<Pediatra> generatedPediatri, List<ReazioneAvversa> reazioniAvverse, List<NumeroTelefono> telefoni)
    {
        Random rnd = new Random();
        ArrayList<Bambino> bambini = new ArrayList<>(count);
        for(int i = 0; i < count; i++)
        {
            Bambino bambino = new Bambino();
            populatePersona(bambino, reazioniAvverse, telefoni);

            Pediatra pediatra = generatedPediatri.get(rnd.nextInt(generatedPediatri.size()));
            bambino.setPediatra(pediatra);

            //Aggiunge i genitori
            int numeroGenitori = 1;
            if(rnd.nextInt(100) < 75)
                numeroGenitori = 2;

            for(int j = 0; j < numeroGenitori; j++)
            {
                Genitore genitore;
                do {
                    genitore = generatedGenitori.get(rnd.nextInt(generatedGenitori.size()));
                } while (bambino.getGenitori().contains(genitore));
                bambino.addGenitore(genitore);
            }

            //Aggiunge i contatti
            int numeroContatti = 4;
            if(rnd.nextInt(100) < 75)
                numeroContatti = 2;

            for(int j = 0; j < numeroContatti; j++)
            {
                Contatto contatto;
                do {
                    contatto = generatedContatti.get(rnd.nextInt(generatedContatti.size()));
                } while (bambino.getContatti().contains(contatto));
                contatto.addBambino(bambino);
                bambino.unsafeAddContatto(contatto);
            }
            bambini.add(bambino);
        }
        return bambini;
    }

    //endregion
}

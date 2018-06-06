import com.polimi.childcare.shared.entities.Menu;
import org.junit.Assert;
import org.junit.Test;


public class MenuTest
{
    /**
     * Prova che il sistema di ricorrenze dei menu sia funzionante
     */
    @Test
    public void RecurrenceTest()
    {
        Menu testMenu = new Menu();

        //Di default non devono esservi ricorrenze
        Assert.assertEquals(testMenu.getRicorrenza(), 0x0000);

        //Provo a rimuovere flag che non sono presenti
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Lun);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Mar);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Mer);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Gio);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Ven);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Sab);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Dom);
        Assert.assertEquals(testMenu.getRicorrenza(), 0x0000);

        //Provo ad aggiungere man mano delle ricorrenze e controllo che non si siano modificati i valori di altri flag
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Lun);
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Lun));
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Mar);
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Lun));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mar));
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Mer);
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Lun));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mar));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mer));
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Gio);
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Lun));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mar));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mer));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Gio));
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Dom);
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Lun));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mar));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mer));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Gio));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Dom));


        //Rimuovo alcune ricorrenze aggiunte a controllo che i flag siano del valore atteso
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Lun);
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mar));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mer));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Gio));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Dom));
        Assert.assertFalse(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Lun));
        Assert.assertFalse(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Sab));
        Assert.assertFalse(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Ven));

        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Gio);
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mar));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Mer));
        Assert.assertTrue(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Dom));
        Assert.assertFalse(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Gio));
        Assert.assertFalse(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Lun));
        Assert.assertFalse(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Sab));
        Assert.assertFalse(testMenu.isRecurringDuringDayOfWeek(Menu.DayOfWeekFlag.Ven));

        //Rimuovo tutte le ricorrenze e controllo ancora che siano 0
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Lun);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Mar);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Mer);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Gio);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Ven);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Sab);
        testMenu.removeRicorrenza(Menu.DayOfWeekFlag.Dom);
        Assert.assertEquals(testMenu.getRicorrenza(), 0x0000);

        //Controllo che aggiungendo tutte le ricorrenze il risultato sia l'OR logico tra tutti i flag
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Lun);
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Mar);
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Mer);
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Gio);
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Ven);
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Sab);
        testMenu.addRicorrenza(Menu.DayOfWeekFlag.Dom);
        Assert.assertEquals(testMenu.getRicorrenza(), Menu.DayOfWeekFlag.Lun.getFlag() |
                                                      Menu.DayOfWeekFlag.Mar.getFlag() |
                                                      Menu.DayOfWeekFlag.Mer.getFlag() |
                                                      Menu.DayOfWeekFlag.Gio.getFlag() |
                                                      Menu.DayOfWeekFlag.Ven.getFlag() |
                                                      Menu.DayOfWeekFlag.Sab.getFlag() |
                                                      Menu.DayOfWeekFlag.Dom.getFlag());
    }
}

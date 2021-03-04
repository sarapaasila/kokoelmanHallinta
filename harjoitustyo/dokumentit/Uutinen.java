package harjoitustyo.dokumentit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Konkreettinen Uutinen-luokka, joka sisältää uutisille yhteiset piirteet.
 * Uutinen-luokka on Dokumentti-luokan aliluokka.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020
 * <p>
 * @author Sara Paasila (sara.paasila@tuni.fi),
 * Informaatioteknologian ja viestinnän tiedekunta,
 * Tampereen yliopisto.
 */
public class Uutinen extends Dokumentti {
    /** Uutisen julkaisupäivämäärä. */
    private LocalDate päivämäärä;

    /* Luokan aksessori. */
    public LocalDate päivämäärä() {
        return päivämäärä;
    }

    /** 
     * Päivämäärän asettava aksessori Uutinen-oliolle.
     * @param pm on uutiselle asetettava päivämäärä, joka on LocalDate-tyyppiä.
     * @throws IllegalArgumentException, jos parametri on null-arvoinen.
     */
    public void päivämäärä(LocalDate pm) throws IllegalArgumentException {
        if (pm == null) {
            throw new IllegalArgumentException("Uutisen päivämäärää ei ole määritetty.");
        }
        else {
            päivämäärä = pm;
        }
    }

    /** 
     * Uutinen-olion parametrillinen rakentaja.
     * Rakentaja ohjaa saamansa parametrit oman luokkansa ja yliluokan metodeihin.
     * @param t on uutiselle annettu tunnisteluku.
     * @param pm on uutiselle annettu päivämäärä.
     * @param x on uutiselle annettu sisältöteksti.
     */
    public Uutinen(int t, LocalDate pm, String x) {
        super(t, x);
        päivämäärä(pm);
    }

    // Korvaa toString-metodin.
    public String toString() {
        // Määritetään päivämäärän muoto.
        DateTimeFormatter muoto = DateTimeFormatter.ofPattern("d.M.yyyy");
        // Kutsutaan olion päivämäärää ja muotoillaan se
        // muoto-DateTimeFOrmatterin mukaan.
        String pm = päivämäärä().format(muoto);
        return tunniste() + "///" + pm + "///" + teksti();
    }
}
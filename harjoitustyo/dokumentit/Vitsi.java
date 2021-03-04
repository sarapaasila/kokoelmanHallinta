package harjoitustyo.dokumentit;

/**
 * Konkreettinen Vitsi-luokka, joka sisältää vitseille yhteiset piirteet.
 * Vitsi-luokka on Dokumentti-luokan aliluokka.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020
 * <p>
 * @author Sara Paasila (sara.paasila@tuni.fi),
 * Informaatioteknologian ja viestinnän tiedekunta,
 * Tampereen yliopisto.
 */
public class Vitsi extends Dokumentti {
    /** Vitsin laji. */
    private String laji;

    /* Luokan aksessori. */
    public String laji() {
        return laji;
    }
    
    /** 
     * Vitsin lajin asettava aksessori Vitsi-oliolle.
     * @param l on vitsille asetettava laji, joka on merkkijono.
     * @throws IllegalArgumentException, jos parametri on null-arvoinen tai tyhjä merkkijono.
     */
    public void laji(String l) throws IllegalArgumentException {
        if (l == null || l == "") {
            throw new IllegalArgumentException("Vitsin lajia ei ole määritetty.");
        }
        else {
            laji = l;
        }
    }

    /** 
     * Vitsi-luokan parametrillinen rakentaja.
     * Rakentaja ohjaa saamansa parametrit oman luokkansa ja yliluokan metodeihin.
     * @param t on vitsille annettu tunnisteluku.
     * @param l on vitsille annettu laji.
     * @param x on vitsille annettu sisältöteksti.
     */
    public Vitsi(int t, String l, String x) {
        super(t, x);
        laji(l);
    }

    // Korvaa toString-metodin.
    public String toString() {
        return tunniste() + "///" + laji() + "///" + teksti();
    }
}
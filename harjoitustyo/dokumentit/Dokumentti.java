package harjoitustyo.dokumentit;

import harjoitustyo.apulaiset.Tietoinen;
import java.lang.Comparable;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Abstrakti Dokumentti-luokka, joka sisältää dokumenteille yhteiset piirteet.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020
 * <p>
 * @author Sara Paasila (sara.paasila@tuni.fi),
 * Informaatioteknologian ja viestinnän tiedekunta,
 * Tampereen yliopisto.
 */
public abstract class Dokumentti implements Comparable<Dokumentti>, Tietoinen<Dokumentti> {
    /** Dokumentin yksilöivä tunnisteluku. */
    private int tunniste;

    /** Dokumentin tekstisisältö. */
    private String teksti;

    /* Luokan aksessorit. */
    public int tunniste() {
        return tunniste;
    }

    public String teksti() {
        return teksti;
    }

    /**
     * Tunnisteluvun asettava aksessori Dokumentti-oliolle.
     * @param t on dokumentille asetettava tunnisteluku, joka on kokonaislukutyyppiä.
     * @throws IllegalArgumentException, jos luku on 0 tai pienempi.
     */
    public void tunniste(int t) throws IllegalArgumentException {
        // Tarkistetaan, että parametri ei ole arvoltaan ykköstä pienempi.
        if (t < 1) {
            throw new IllegalArgumentException("Dokumentin tunniste ei ole oikeellinen.");
        }
        else {
            tunniste = t;
        }
    }

    /**
     * Sisältötekstin asettava aksessori Dokumentti-oliolle.
     * @param x on dokumentille asetettva sisältöteksti, joka on merkkijonotyyppiä.
     * @throws IllegalArgumentException, jos alkio on null-arvoinen tai tyhjä merkkijono.
     */
    public void teksti(String x) throws IllegalArgumentException {
        // Tarkistetaan, että parametri ei ole arvoltaan null tai tyhjä merkkijono.
        if (x == null || x == "") {
            throw new IllegalArgumentException("Dokumentin sisältöä ei ole määritetty.");
        }
        else {
            teksti = x;
        }
    }

    /**
     * Dokumentti-olion parametrillinen rakentaja.
     * Rakentaja ohjaa saamansa parametrit niille tarkoitettuihin metodeihin.
     * @param t on dokumentille annettu tunnisteluku.
     * @param x on dokumentille annettu sisältöteksti.
     */
    public Dokumentti(int t, String x) {
        tunniste(t);
        teksti(x);
    }

    // Korvaa toString-metodin.
    public String toString() {
        return tunniste() + "///" + teksti();
    }

    /**
     * Metodi tarkistaa, ovatko alkiot samoja.
     * <p>
     * Korvaa equals-metodin.
     * @return true, jos annettu parametri ei ole null, kokoniasluku, merkkijono 
     * ja metodilla testattavat alkiot ovat samat.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null 
        || o instanceof Integer 
        || o instanceof String 
        || this.tunniste() != ((Dokumentti) o).tunniste()) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Metodi vertailee alkioita keskenään. 
     * <p>
     * Ilmentää rajapinnan Comparable<Dokumentti> compareTo-metodia.
     * @return 1, 0 tai -1 riippuen tunnisteiden suhteesta.
     * @throws NullPointerException, jos yritetään verrata null-arvoista alkio(i)ta.
     */
    @Override
    public int compareTo(Dokumentti x) throws NullPointerException {
        if (this == null || x == null) {
            throw new NullPointerException("Verrattavat eivät voi olla arvoltaan null");
        }
        else {
            int luku = 0;
            if (tunniste() < x.tunniste()) {
                luku = -1;
            }
            if (tunniste() > x.tunniste()) {
                luku = 1;
            }
            return luku;
        }
    }

    /**
     * Metodi etsii Dokumentti-olion sisältötekstistä, löytyykö parametrina annetun 
     * linkitetyn listan sanat siitä.
     * <p>
     * Ilmentää rajapinnan Tietoinen<Dokumentti> sanatTäsmäävät-metodia.
     * @return true, jos sisältötekstistä löytyy linkitetyn listan kaikki sanat.
     * @throws IllegalArgumentAxception, jos hakusanat-lista on null-arvoinen tai tyhjä.
     */
    @Override
    public boolean sanatTäsmäävät(LinkedList<String> hakusanat) throws IllegalArgumentException {
        if (hakusanat == null || hakusanat.isEmpty()) {
            throw new IllegalArgumentException("Parametri ei ole oikeellinen");
        }
        else {
            // Muutetaan merkkijono helpommin käsiteltävään muotoon.
            final String[] tekstilista = teksti().split(" ");
            boolean lippu = false;
            
            // Luodaan muuttuja, johon lisätään aina yksi, jos hakusanat-listan sanaa ei löydy
            // tekstilista-muuttujasta.
            int vahti = 0;
            for (int i = 0; i < hakusanat.size(); i++) {
                boolean etsi = Arrays.asList(tekstilista).contains(hakusanat.get(i));
                if (!etsi) {
                    vahti++;
                }
            }

            // Jos vahti ei ole suurempi kuin nolla, muutetaan lipun arvoksi true.
            if (vahti == 0) {
                lippu = true;
            }
            return lippu;
        }
    }

    /**
     * Metodi siivoaa sisältötekstiä hakukelpoiseksi. 
     * <p>
     * Se poistaa ensin säiliö-muuttujasta välimerkit-merkkkijonosta löytyvät merkit,
     * seuraavaksi se muuttaa säiliön kaikki kirjaimet pieniksi, ja viimeiseksi se poistaa
     * säiliöstä sulkusanat-linkitetyn listan sanat.
     * Metodin lopussa säiliö-muuttuja viedään takaisin Dokumentti-olion sisältötekstiksi.
     * <p>
     * Ilmentää rajapinnan Tietoinen<Dokumentti> siivoa-metodia.
     * @throws IllegalArgumentAxception, jos sulkusanat-lista on null-arvoinen tai tyhjä, 
     * tai välimerkit-merkkijono on null-arvoinen tai tyhjä merkkijono.
     */
    @Override
    public void siivoa(LinkedList<String> sulkusanat, String välimerkit) 
    throws IllegalArgumentException {
        if (sulkusanat == null || sulkusanat.isEmpty() || välimerkit == null || välimerkit == "") {
            throw new IllegalArgumentException("Parametrit eivät ole oikeellisia.");
        }
        else {
            // Noudetaan siivottava merkkijono.
            String säiliö = this.teksti();

            // Poistetaan merkkijonona annetut välimerkit teksti-merkkijonosta.
            for (int i = 0; i < välimerkit.length(); i++) {
                String välimerkki = Character.toString(välimerkit.charAt(i));
                säiliö = säiliö.replace(välimerkki, "");
            }
            
            // Muutetaan teksti-merkkijonon kaikki kirjaimet pieniksi kirjaimiksi.
            säiliö = säiliö.toLowerCase();
            
            // Luodaan jako-lista säiliömerkkijonosta.
            LinkedList<String> jako = new LinkedList<String>();
            for (String s: säiliö.split(" ")) {
                jako.add(s);
            }

            // Poistetaan jako-listasta löytyvät sulkusanat-listan alkiot.
            for (int i = 0; i < sulkusanat.size(); i++) {
                for (int j = 0; j < jako.size(); j++) {
                    if (sulkusanat.get(i).equals(jako.get(j))) {
                        jako.remove(j);
                    }
                }
            }

            // Tarkistetaan, että kaikki sulkusanat ovat poistettu dokumenteista.
            for (int i = 0; i < sulkusanat.size(); i++) {
                for (int j = 0; j < jako.size(); j++) {
                    if (sulkusanat.get(i).equals(jako.get(j))) {
                        jako.remove(j);
                    }
                }
            }
            
            // Muutetaan ensiksi linkitetty lista taulukoksi ja sitten taulukko merkkijonoksi.
            String merkkijono = String.join(" ", jako.toArray(new String[jako.size()]));

            // Tarkistetaan, ettei merkkijonon alussa ole välilyöntiä.
            if (merkkijono.charAt(0) == ' ') {
                merkkijono = merkkijono.substring(1);
            }
            // Tarkistetaan, ettei merkkijonon perässä ole välilyöntiä.
            if (merkkijono.charAt(merkkijono.length()-1) == ' ') {
                merkkijono = merkkijono.substring(0, merkkijono.length()-2);
            }
            // Tarkistetaan, ettei merkkijonossa ole kahta välilyöntiä peräkana.
            for (int i = 0; i < merkkijono.length(); i++) {
                if (merkkijono.charAt(i) == ' ' && merkkijono.charAt(i+1) == ' ') {
                    merkkijono = merkkijono.substring(0, i) + merkkijono.substring(i+1);
                }
            }

            teksti(merkkijono);
        }
    }
}
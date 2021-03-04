package harjoitustyo.kokoelma;

import harjoitustyo.omalista.OmaLista;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Scanner;

import harjoitustyo.apulaiset.Kokoava;
import harjoitustyo.dokumentit.Dokumentti;
import harjoitustyo.dokumentit.Uutinen;
import harjoitustyo.dokumentit.Vitsi;

/**
 * Konkreettinen Kokoelma-luokka, joka on vastuussa kokoelman logiikasta. Luokka
 * hallinnoi kokoelmaa ja sen dokumenttitiedostoja.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020.
 * <p>
 * @author Sara Paasila (sara.paasila@tuni.fi),
 * Informaatioteknologian ja viestinnän tiedekunta,
 * Tampereen yliopisto.
 */
public class Kokoelma implements Kokoava<Dokumentti> {
    /** Kokoelmaan kuuluvien dokumenttiolioiden viitteet. */
    private OmaLista<Dokumentti> dokumentit;

    /* Attribuutin lukeva aksessori */
    public OmaLista<Dokumentti> dokumentit()  {
        return dokumentit;
    }

    /** Kokoelma-olion oletusrakentaja, jota kutsuttaessa luodaan tyhjä listaolio. */
    public Kokoelma() {
        dokumentit = new OmaLista<Dokumentti>();
    }

    /**
     * Metodilla lisätään parametrina saatu alkio tiettyyn kohtaan dokumentit-oliossa.
     * Alkio lisätään listassa siihen paikkaan, missä kaikki sitä edeltävät alkiot ovat
     * pienempiä tai yhtä suuria kuin alkio ja kaikki sen jälkeen tulevat alkiot ovat
     * suurempia kuin alkio.
     * @param uusi on uusi-olioon lisättävä alkio, joka on Dokumentti-tyyppiä.
     * @throws IllegalArgumentException, jos parametri on null-arvoinen tai dokumentit-oliosta löytyy
     * samanlainen olio kuin uusi-olio jo valmiiksi.
     */
    @Override
    public void lisää(Dokumentti uusi) throws IllegalArgumentException {
        if (uusi == null) {
            throw new IllegalArgumentException("Kokoelmaan ei saa lisätä null-arvoista alkiota.");
        }
        else if (dokumentit.contains(uusi)) {
            throw new IllegalArgumentException("Kokoelmassa on jo samanlainen tiedosto.");
        }
        else {
            if (dokumentit.isEmpty()) {
                dokumentit.add(0, uusi);
            }
            else {
                boolean lippu = false;
                int i = 0;
                while (!lippu) {
                    // Vertailtava alkio on myös listan viimeinen.
                    if (i == dokumentit.size()) {
                        dokumentit.addLast(uusi);
                        lippu = true;
                    }
                    else if (uusi.tunniste() < dokumentit.get(i).tunniste()) {
                        dokumentit.add(i, uusi);
                        lippu = true;
                    }
                    else {
                        i++;
                    }
                }
            }
        }
    }

    /**
     * Metodilla haetaan dokumentit-oliosta osaolio, jolla on sama tunnisteluku 
     * kuin parametrinä annettu kokonaisluku.
     * @param tunniste on kokonaisluku, jolla tehdään metodissa tehtävä vertailu.
     * @return Dokumentti-olio.
     */
    @Override
    public Dokumentti hae(int tunniste) {
        Dokumentti säilö = null;
        for (int i = 0; i < this.dokumentit().size(); i++) {
            if (tunniste == this.dokumentit().get(i).tunniste()) {
                säilö = this.dokumentit().get(i);
            }
        }
        return säilö;
    }

    /**
     * Metodilla luetaan annetusta tiedostosta dokumentit ja lisätään ne kokoelmaan.
     * @param rivit on tiedostoa lukeva skanneri.
     * @param parametriOG on tiedoston nimestä poimittu tunnistin ("jokes" tai "news").
     */
    public void lataaDokumentit(Scanner rivit, String parametriOG) {
        // Parametriksi annetussa tiedostossa on vitsejä, eli alkaa "jokes"-sanalla.
        if (parametriOG.equals("jokes")) {
            while (rivit.hasNextLine()) {
                // Jaetaan vitsi sen välimerkkien perusteella kolmeen alkioon ja luodaan
                // niistä uusi Vitsi-olio, joka lisätään kokoelmaan.
                String[] taulu = rivit.nextLine().split("///");
                Vitsi x = new Vitsi(Integer.parseInt(taulu[0]), taulu[1], taulu[2]);
                this.lisää(x);
            }
        }
        // Parametriksi annetussa tiedostossa on uutisia, eli alkaa "news"-sanalla.
        // Uutinen-olioissa on ongelma LocalDate-muuttujan luomisessa.
        if (parametriOG.equals("news")) {
            while (rivit.hasNextLine()) {
                // Jaetaan uutinen sen välimerkkien perusteella kolmeen alkioon.
                String[] taulu1 = rivit.nextLine().split("///");
                String[] taulu2 = taulu1[1].split("\\.");
                int[] tauluNro = new int[taulu2.length];

                for (int i = 0; i < taulu2.length; i++) {
                    tauluNro[i] = Integer.parseInt(taulu2[i]);
                }
                
                // Luodaan kokonaisluvuista LocalDate-muuttuja.
                LocalDate pvm = LocalDate.of(tauluNro[2], tauluNro[1], tauluNro[0]);
                // Luodaan aikaisemmin jaetuista osista ja luodusta LocalDate-muuttujasta 
                // uusi Uutinen-olio, joka lisätään kokoelmaan.
                Uutinen x = new Uutinen(Integer.parseInt(taulu1[0]), pvm, taulu1[2]);
                this.lisää(x);
            }
        }
    }

    /**
     * Metodilla lisätään skannerista luetut rivit linkitetylle listalle.
     * @param sanat on skanneri, joka lukee sulkusanatiedoston.
     * @return linkitetty lista, jonka alkioina on sulkusanatiedoston rivit.
     */
    public LinkedList<String> lataaSulkusanat(Scanner sanat) {
        LinkedList<String> lisätyt = new LinkedList<String>();
        // Lisätään kaikki tiedoston rivit tyhjään listaan yksitellen.
        while (sanat.hasNext()) {
            lisätyt.add(sanat.next());
        }
        return lisätyt;
    }

    /**
     * Metodilla palautetaan Dokumentti-olion dokumentit merkkijonomuodossa.
     * @param t on indeksi, jolla haetaan joko tietty olio indeksinsä perusteella tai kaikki oliot.
     * @return Dokumentti-olio merkkijonomuodossa.
     */
    public String tulosta(int t) {
        String palautus = "";
        // Sijoitetaan kaikki dokumentit merkkijonoon.
        if (t == -1) {
            for (int i = 0; i < this.dokumentit().size(); i++) {
                palautus = palautus + this.dokumentit().get(i).toString() + System.lineSeparator();
            }
        }
        // Sijoitetaan tunnuslukua vastaavaa dokumentti merkkijonoon.
        else {
            boolean löytyikö = false;
            for (int i = 0; i < this.dokumentit().size(); i++) {
                if (t == this.dokumentit().get(i).tunniste()) {
                    palautus = palautus + this.dokumentit().get(i).toString();
                    löytyikö = true;
                }
            }
            if (!löytyikö) {
                palautus = palautus + "Error!";
            }
        }
        return palautus;
    }

    /**
     * Metodilla käsitellään, onko käyttäjän antaman merkkijonon lisääminen kokoelmaan
     * laillista. Esimerkiksi onko käyttäjä lisäämässä vitsiä uutisiin tai toisinpäin.
     * @param parametriOG on ohjelman käynnistyksessä käyttäjän antaman tiedoston
     * tyyppi (joko uutinen tai vitsi).
     * @param taulu on käyttäjän antama dokumentti merkkijonotaulussa.
     * @return false, jos käyttäjä on lisäämässä vitsiä uutisiin tai toisinpäin.
     */
    public boolean käsitteleLisäystä(String parametriOG, String[] taulu) {
        boolean onnistuikoLisäys = true;
        int luku = taulu[1].indexOf('.');

        // Parametriksi annetussa tiedostossa on vitsejä, eli alkaa "jokes"-sanalla.
        if (luku == -1 && parametriOG.equals("jokes")) {
            Vitsi x = new Vitsi(Integer.parseInt(taulu[0]), taulu[1], taulu[2]);
            this.lisää(x);
        }
        // Parametriksi annetussa tiedostossa on uutisia, eli alkaa "news"-sanalla.
        // Uutinen-olioissa on ongelma LocalDate-muuttujan luomisessa.
        else if (luku != -1 && parametriOG.equals("news")) {
            String[] taul = taulu[1].split("\\.");
            int[] taulNro = new int[taul.length];

            for (int i = 0; i < taul.length; i++) {
                taulNro[i] = Integer.parseInt(taul[i]);
            }
    
            // Luodaan kokonaisluvuista LocalDate-muuttuja.
            LocalDate pvm = LocalDate.of(taulNro[2], taulNro[1], taulNro[0]);
            // Luodaan aikaisemmin jaetuista osista ja luodusta LocalDate-muuttujasta 
            // uusi Uutinen-olio, joka lisätään kokoelmaan.
            Uutinen x = new Uutinen(Integer.parseInt(taulu[0]), pvm, taulu[2]);
            this.lisää(x);
        }
        else {
            onnistuikoLisäys = false;
        }
        return onnistuikoLisäys;
    }

    /**
     * Metodi etsii annettuja hakusanoja kokoelman dokumenteista ja palauttaa
     * niiden  dokumenttien tunnisteluvut, joissa on kaikki hakusanat.
     * @param hakusanat on käyttäjän antamat hakusanat linkitetyssä listassa.
     * @return merkkijonolista, jossa on kaikki hakusanoja sisältävien
     * dokumenttien tunnisteluvut.
     */
    public String etsiSanoja(LinkedList<String> hakusanat) {
        String palautus = "";
        for (int i = 0; i < this.dokumentit().size(); i++) {
            // Jos sanatTäsmäävät-metodi palauttaa arvon true, eli kaikki hakusanat
            // löytyvät yksittäisestä dokumentista, lisätään tunnisteluku merkkijonoon.
            if (this.dokumentit().get(i).sanatTäsmäävät(hakusanat)) {
                palautus = palautus + this.dokumentit().get(i).tunniste() 
                + System.lineSeparator();
            }
        }
        return palautus;
    }

    /**
     * Metodilla haetaan parametristä saatua tunnistelukua vastaavaa dokumenttia
     * ja sen löydettyä se poistetaan kokoelmasta.
     * @param t on poistettavan dokumentin tunnisteluku.
     * @return true, jos dokumentti pystyttiin poistamaan.
     */
    public boolean poista(int t) {
        boolean löytyi = false;
        for (int i = 0; i < this.dokumentit().size(); i++) {
            if (this.dokumentit().get(i).tunniste() == t){
                this.dokumentit().remove(i);
                löytyi = true;
            }
        }
        return löytyi;
    }

    /**
     * Metodilla esikäsitellään Kokoelma-olion osaoliot kutsumalla Dokumentti-luokan
     * siivoa-metodia.
     * @param sulku on linkitettylista, johon on siivoa-metodissa tarvittavat 
     * sulkusanat.
     * @param välimerkit on merkkijono, jossa on siivoa-metodissa tarvittavat
     * välimerkit.
     */
    public void esikäsittelynEsikäsittely(LinkedList<String> sulku, String välimerkit) {
        for (int i = 0; i < this.dokumentit().size(); i++) {
            this.dokumentit().get(i).siivoa(sulku, välimerkit);
        }
    }
}
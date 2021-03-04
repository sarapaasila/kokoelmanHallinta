package harjoitustyo.kokoelma;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Konkreettinen Käyttöliittymä-luokka, joka on vastuussa käyttäjän kanssa vuorovaiktuksesta.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020.
 * <p>
 * @author Sara Paasila (sara.paasila@tuni.fi),
 * Informaatioteknologian ja viestinnän tiedekunta,
 * Tampereen yliopisto.
 */
public class Kayttoliittyma {
    /** Taulukko käytettävien tiedostojen nimistä. */
    private Kokoelma kokoelma;

    public Kokoelma kokoelma(boolean lippu) {
        // Jos lippu-muuttuja on tosi, luodaan uusi kokoelma.
        if (lippu) {
            kokoelma = new Kokoelma();
        }
        return kokoelma;
    }

    /**
     * Metodilla käynnistetään ohjelman toiminta; lisätään annettujen tiedostojen rivit
     * linkitetyihin listoihin ohjelman käsiteltävksi ja kutsutaan pääsilmukkaa.
     * @param args on ohjelman käynnistyksessä annetut tiedostonimet taulumuuttujana.
     */
    public Kayttoliittyma(String[] args) {
        System.out.println("Welcome to L.O.T.");
        // Tarkistetaan komentoparametrien määrä.
        if (args.length != 2) {
            System.out.println("Wrong number of command-line arguments!");
            System.out.println("Program terminated.");
        } 
        else {
            // Yritetään lukea tiedostoja. Heittää virheen, jos ei pysty tai löydä niitä.
            try {
                // Luodaan tyhjä lista.
                LinkedList<String> sulkusanat = new LinkedList<String>();
                // Sijoitetaan dokumenttitiedoston nimi muuttujaan.
                final String parametriOG = args[0].split("_")[0];

                // Luetaan sulkusanatiedosto ja lisätään sen sisältö sulku-listalle.
                sulkusanat = kokoelma(true).lataaSulkusanat(new Scanner(new File(args[1])));
                // Luetaan dokumenttitiedosto ja Ladataan sen sisältö kokoelmaan.
                kokoelma(true).lataaDokumentit(new Scanner(new File(args[0])), parametriOG);

                tulkitseSyötteet(args, parametriOG, sulkusanat);
            }
            catch (FileNotFoundException e) {
                System.out.println("Missing file!");
                System.out.println("Program terminated.");
            }
        }
    }

    /**
     * Metodi toimii ohjelman pääsilmukkana; luetaan käyttäjän antamat komennot ja
     * ohjataan ohjelman toimintaa sen mukaan.
     * @param args on ohjelman käynnistyksessä annetut tiedostonimet taulumuuttujana.
     * @param parametriOG on args-muuttujasta erotetultu muuttuja, jolla tunnistetaan tiedosto
     * joko vitseiksi ("jokes") tai uutisiksi ("news").
     * @param sulkusanat on dokumenttitiedostojen esikäsittelyssä käytettävä lista.
     */
    public void tulkitseSyötteet(String[] args, String parametriOG, 
    LinkedList<String> sulkusanat) {
        boolean lopeta = false;
        boolean kaiuta = false;
        Scanner scr = new Scanner(System.in);

        do {
            System.out.println("Please, enter a command:");
            String[] syöte = scr.nextLine().split(" ");

            if (kaiuta) {
                for (int i = 0; i < syöte.length; i++) {
                    if (i == syöte.length-1) {
                        System.out.println(syöte[i]);
                    }
                    else {
                        System.out.print(syöte[i] + " ");
                    }
                }
            }

            // Lopetetaan ohjelman käyttö.
            if (syöte[0].equals("quit")) {
                lopeta = true;
            }
            // Käyttäjän antaman komennon kaiuttaminen.
            else if (syöte[0].equals("echo")) {
                if (kaiuta) {
                    kaiuta = false;
                }
                else {
                    kaiuta = true;
                    System.out.println("echo");
                }
            }
            // Tulostetaan dokumenttitiedosto(-ja).
            else if (syöte[0].equals("print")) {
                tulostusMetodi(syöte);
            }
            // Lisätään dokumenttitiedosto kokoelmaan.
            else if (syöte[0].equals("add")) {
                lisäysMetodi(syöte, parametriOG);
            }
            // Etsitään sana(t) dokumenttitiedostoista.
            else if (syöte[0].equals("find")) {
                etsintäMetodi(syöte);
            }
            // Dokumenttitiedoston poistetaan tunnisteluvun perusteella.
            else if (syöte[0].equals("remove")) {
                poistoMetodi(syöte);
            }
            // Dokumenttitiedostojen esikäsitelly.
            else if (syöte[0].equals("polish")) {
                esikäsittelyMetodi(syöte, sulkusanat);
            }
            // Dokumenttitiedostojen uudelleenlataaminen.
            else if (syöte[0].equals("reset")) {
                nollausMetodi(args, parametriOG);
            }
            // Virheviesti, kun käyttäjän syöte ei vastaa haluttua.
            else {
                System.out.println("Error!");
            }
        }
        while (!lopeta);
        scr.close();

        System.out.println("Program terminated.");
    }

    /**
     * Metodilla hallinnoidaan käyttäjän komentojen kaiuttamista.
     * @param kaiuta toimii kaiutuksen alkamisen ja loppumisen lippumuuttujana.
     * @return true, jos kaiuta on false ja toisinpäin.
     */
    public boolean kaiutusMetodi(boolean kaiuta) {
        if (kaiuta) {
            kaiuta = false;
        }
        else {
            kaiuta = true;
            System.out.println("echo");
        }
        return kaiuta;
    }

    /**
     * Metodilla tulostetaan joko kaikki kokoelman dokumenttitiedostot parametreja
     * ollessa yksi tai tietty dokumenttitiedosto käyttäjän antaman tunnisteluvun
     * perusteella parametreja ollessa kaksi.
     * @param syöte on käyttäjän antama komento taulumuuttujana.
     */
    public void tulostusMetodi(String[] syöte) {
        int t = 0;
        // Käyttäjän syöte on pelkästään "print".
        if (syöte.length == 1) {
            t = -1;
            System.out.print(kokoelma(false).tulosta(t));
        }
        // Käyttäjän syöteessä on kokonaislukutyyppinen tunnisteluku.
        else if (syöte.length == 2 && (syöte[1].matches("[0-9]+"))) {
            t = Integer.parseInt(syöte[1]);
            if (t > 0) {
                System.out.println(kokoelma(false).tulosta(t));
            }
            else {
                System.out.println("Error!");
            }
        }
        else {
            System.out.println("Error!");
        }
    }

    /**
     * Metodilla lisätään kokoelmaan dokumenttitiedosto, joka saadaan käyttäjän antamasta
     * syötteestä merkkijonona.
     * @param syöte on käyttäjän antama komento taulumuuttujana.
     * @param parametriOG on args-muuttujasta erotetultu muuttuja, jolla tunnistetaan tiedosto
     * joko vitseiksi ("jokes") tai uutisiksi ("news").
     */
    public void lisäysMetodi(String[] syöte, String parametriOG) {
        if (syöte.length != 1) {
            // Siirretään taulun sisältö merkkijonoon ensimmäistä elementtiä 
            // ("add") lukuunottamatta.
            String lisäys = "";
            for (int i = 1; i < syöte.length; i++) {
                if (i == (syöte.length - 1)) {
                    lisäys = lisäys + syöte[i];
                }
                else {
                    lisäys = lisäys + syöte[i] + " ";
                }
            }
            // Jaetaan dokumentti sen välimerkkien perusteella kolmeen alkioon.
            String[] taulu = lisäys.split("///");

            try {
                // Tarkistetaan, että tunnisteluku ei ole varattu.
                if (kokoelma(false).hae(Integer.parseInt(taulu[0])) != null) {
                    System.out.println("Error!");
                }
                else {
                    // Katsotaan, saatiinko lisättyä olio kokoelmaan.
                    boolean onnistuikoLisäys = kokoelma(false).käsitteleLisäystä(parametriOG, taulu);
                    if (!onnistuikoLisäys) {
                        System.out.println("Error!");
                    }
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Error!");
            }
        }
        else {
            System.out.println("Error!");
        }
    }

    /**
     * Metodilla etsitään ja tulostetaan dokumenttitiedostojen tunnistusluvut, josisa
     * ovat kaikki käyttäjän komentoparametreina syöttämät sanat.
     * @param syöte on käyttäjän antama komento taulumuuttujana.
     */
    public void etsintäMetodi(String[] syöte) {
        if (syöte.length == 1) {
            System.out.println("Error!");
        }
        else {
            // Luodaan tyhjä lista, johon sijoitetaan hakusanat.
            LinkedList<String> hakusanat = new LinkedList<String>();
            // Sijoitetaan kaikki alkiot linkitetylle listalle ensimmäistä 
            // lukuunottomatta ("find").
            for (int i = 1; i < syöte.length; i++) {
                hakusanat.add(syöte[i]);
            }

            String löydetyt = kokoelma(false).etsiSanoja(hakusanat);
            if (löydetyt != "") {
                System.out.print(löydetyt);
            }
        }
    }

    /**
     * Metodilla poistetaan dokumenttitiedosto käyttäjän antaman tunnistusluvun perusteella.
     * @param syöte on käyttäjän antama komento taulumuuttujana.
     */
    public void poistoMetodi(String[] syöte) {
        if (syöte.length == 2 && (syöte[1].matches("[0-9]+"))) {
            boolean löytyikö = kokoelma(false).poista(Integer.valueOf(syöte[1]));

            if (!löytyikö) {
                System.out.println("Error!");
            }
        }
        else {
            System.out.println("Error!");
        }
    }

    /**
     * Metodilla esikäsitellään dokumenttitiedostot vaiheittain; poistetaan välimerkit,
     * muutetaan kirjaimet pieniksi ja poistetaan sulkusanat-listan sanat dokumenteista.
     * @param syöte on käyttäjän antama komento taulumuuttujana.
     * @param sulkusanat on dokumenttitiedostojen esikäsittelyssä käytettävä lista.
     */
    public void esikäsittelyMetodi(String[] syöte, LinkedList<String> sulkusanat) {
        if (syöte.length == 2) {
            kokoelma(false).esikäsittelynEsikäsittely(sulkusanat, syöte[1]);
        }
        else {
            System.out.println("Error!");
        }
    }

    /**
     * Metodilla nollataan kokoelmaan tehdyt muutokset eli ladataan vanhan kokoelman päälle
     * uusi tyhjä kokoelma, johon lisätään tiedoston dokumentit.
     * @param korpus on kokoelmaolio, jonka osaoliot ovat dokumenttitiedostoja.
     * @param args on ohjelman käynnistyksessä annetut tiedostonimet taulumuuttujana.
     * @param parametriOG on args-muuttujasta erotetultu muuttuja, jolla tunnistetaan tiedosto
     * joko vitseiksi ("jokes") tai uutisiksi ("news").
     */
    public void nollausMetodi(String[] args, String parametriOG) {
        // Yritetään lukea tiedostoja. Heittää virheen, jos ei pysty tai löydä niitä.
        try {
            // Nollataan kokoelman sisältö uudella tyhjällä kokoelmalla.
            // Luetaan dokumenttitiedosto ja ladataan sen sisältö kokoelmaan.
            kokoelma(true).lataaDokumentit(new Scanner(new File(args[0])), parametriOG);
        }
        catch (FileNotFoundException e) {
            System.out.println("Missing file!");
        }
    }
}
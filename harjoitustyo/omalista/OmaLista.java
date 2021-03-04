package harjoitustyo.omalista;

import java.util.LinkedList;

import harjoitustyo.apulaiset.Ooperoiva;

/**
 * Konkreettinen OmaLista<E>-luokka, joka on LinkedList<E>-luokan aliluokka.
 * <p>
 * Harjoitustyö, Olio-ohjelmoinnin perusteet II, kevät 2020
 * <p>
 * @author Sara Paasila (sara.paasila@tuni.fi),
 * Informaatioteknologian ja viestinnän tiedekunta,
 * Tampereen yliopisto.
 */
public class OmaLista<E> extends LinkedList<E> implements Ooperoiva<E> {

    /**
     * Metodilla lisätään parametrina saatu alkio tiettyyn kohtaan OmaLista<E>-oliossa.
     * Alkio lisätään listassa siihen paikkaan, missä kaikki sitä edeltävät alkiot ovat
     * pienempiä tai yhtä suuria kuin alkio ja kaikki sen jälkeen tulevat alkiot ovat
     * suurempia kuin alkio.
     * @param uusi on OmaLista<E>-olioon lisättävä alkio, joka on tyypiltään geneerinen.
     * @throws IllegalArgumentException, jos parametri on null-arvoinen tai ei ole Comparable-tyyppiä.
     */
    @SuppressWarnings("unchecked")
    public void lisää(E uusi) throws IllegalArgumentException {
        if (uusi != null) {
            // Annettu alkio on merkkijono.
            if (uusi instanceof Comparable 
            ) {
                // Jos lista on tyhjä, lisätään alkio suoraan listalle.
                if (this.isEmpty()) {
                    this.addFirst(uusi);
                }

                // Vertaillaan lisättävää alkiota listan alkioihin ja sijoitetaan
                // uusi-muuttuja oikeaan paikkaan.
                else {
                    boolean lippu = false;
                    int i = 0;

                    while (!lippu) {
                        // Vertailtava alkio on myös listan viimeinen.
                        if (i == this.size()) {
                            this.addLast(uusi);
                            lippu = true;
                        }
                        else {
                            int vertaus = ((Comparable) uusi).compareTo(((Comparable) this.get(i)));
                            if (vertaus < 0) {
                                this.add(i, uusi);
                                lippu = true;
                            }
                            else {
                                i++;
                            }
                        }
                    }
                }
            }
            else {
                throw new IllegalArgumentException("Alkion tulee olla joko kokonaisluku tai merkkijono.");
            }
        }
        else {
            throw new IllegalArgumentException("Alkio ei saa olla arvoltaan null.");
        }
    }
}
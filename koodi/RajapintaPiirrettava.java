
/**
 *
 * Rajapinta <CODE>RajapintaPiirrettava</CODE> määrittä rajapinnan käyttöliittymän
 * sellaisille osille, joita esim. näytönpäivityssäie voi kutsua päivittämään
 * näkymänsä. Näin ollen säie voi olla yksi erillinen luokka, jolle on määritetty
 * keitä se kutsuu päivittämään piirtopintansa.
 *  
 * @author Jaakko Luttinen
 *
 */
public interface RajapintaPiirrettava {
    
    /**
     * Päivittää piirtopinnan.
     */
    public abstract void piirra();
    
    /**
     * Piirtokutsuja välittävä taho välittää tällaisen viestin, kun kutsujan tila
     * muuttuu. Esim. jos näytönpäivityssäie pysäytetään, välitetään kaikille
     * sille määritetyille piirrettäville olioille tämä metodikutsu parametriarvolla
     * <CODE>false</CODE>. Näin ollen luokalla on mahdollisuus tehdä toimenpiteitä sen
     * mukaan, että kukaan ulkopuolinen ei tällä hetkellä enää välitäkään piirrä-komentoja.
     * Luokka esim. huolehtii piirtämisestä itse. Tätä metodia kutsutaan vain
     * tilan muuttuessa, joten jos luokka haluaa ottaa huomioon sen, että sille
     * välittääkin useampi taho piirra-komentoja, voi se pitää yllä laskuria. Kun
     * laskuri on nolla, vasta silloin täytyy itse huolehtia piirtämisestä. Käytännössä
     * näin tuskin tarvitsee toimia.
     * @param paivitetaan Totuusarvo kertoo, välittääkö ulkopuolinen kontrolloija vielä
     * piirra-komentoja.
     */
    public abstract void paivitetaan( boolean paivitetaan );
    
}
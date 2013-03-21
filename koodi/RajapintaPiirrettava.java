
/**
 *
 * Rajapinta <CODE>RajapintaPiirrettava</CODE> m��ritt� rajapinnan k�ytt�liittym�n
 * sellaisille osille, joita esim. n�yt�np�ivityss�ie voi kutsua p�ivitt�m��n
 * n�kym�ns�. N�in ollen s�ie voi olla yksi erillinen luokka, jolle on m��ritetty
 * keit� se kutsuu p�ivitt�m��n piirtopintansa.
 *  
 * @author Jaakko Luttinen
 *
 */
public interface RajapintaPiirrettava {
    
    /**
     * P�ivitt�� piirtopinnan.
     */
    public abstract void piirra();
    
    /**
     * Piirtokutsuja v�litt�v� taho v�litt�� t�llaisen viestin, kun kutsujan tila
     * muuttuu. Esim. jos n�yt�np�ivityss�ie pys�ytet��n, v�litet��n kaikille
     * sille m��ritetyille piirrett�ville olioille t�m� metodikutsu parametriarvolla
     * <CODE>false</CODE>. N�in ollen luokalla on mahdollisuus tehd� toimenpiteit� sen
     * mukaan, ett� kukaan ulkopuolinen ei t�ll� hetkell� en�� v�lit�k��n piirr�-komentoja.
     * Luokka esim. huolehtii piirt�misest� itse. T�t� metodia kutsutaan vain
     * tilan muuttuessa, joten jos luokka haluaa ottaa huomioon sen, ett� sille
     * v�litt��kin useampi taho piirra-komentoja, voi se pit�� yll� laskuria. Kun
     * laskuri on nolla, vasta silloin t�ytyy itse huolehtia piirt�misest�. K�yt�nn�ss�
     * n�in tuskin tarvitsee toimia.
     * @param paivitetaan Totuusarvo kertoo, v�litt��k� ulkopuolinen kontrolloija viel�
     * piirra-komentoja.
     */
    public abstract void paivitetaan( boolean paivitetaan );
    
}
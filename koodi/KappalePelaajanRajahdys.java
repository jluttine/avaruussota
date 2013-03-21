
/**
 * 
 * Luokka <CODE>KappalePelaajanRajahdys</CODE> kuvaa räjähdystä, joka syntyy, kun pelaaja
 * kuolee. Kun räjähdys tuhoutuu, lähettää se viestin arpoa pelaajalle uusi sijainti.
 * Räjähdys on kertakäyttöinen: Pelaaja voidaan asettaa vain konstruktorissa ja se asetetaan
 * null:ksi, kun räjähdys tuhoutuu.
 *
 * @author Jaakko Luttinen
 *
 */
public class KappalePelaajanRajahdys extends KappaleRajahdys {

    /**
     * Pelaaja, joka on kuollut.
     */
    private Pelaaja pelaaja;
    
    /**
     * Luo uuden pelaajan räjähdyksen annetun mallin perusteella.
     * @param malli Räjähdys, jonka perusteella räjähdys luodaan.
     * @param pelaaja Pelaaja, joka on kuollut.
     */
    public KappalePelaajanRajahdys( KappaleRajahdys malli, Pelaaja pelaaja ) {
        
        super( malli );
        this.pelaaja = pelaaja;
        
    }
    
    /**
     * Arpoo pelaajalle uuden sijainnin. Kutsuu yläluokan tuhodu-metodia. Asettaa
     * pelaajaksi <CODE>null</CODE>.
     */
    public void tuhoudu() {
        
        super.tuhoudu();
        
        //Arvotaan tuhoutuneelle alukselle uusi sijainti.
        if ( this.pelaaja != null && this.pelaaja.kerroPeli() != null )
            this.pelaaja.kerroPeli().arvoPelaajanSijainti( this.pelaaja );
            
        this.pelaaja = null;
        
    }
    
}
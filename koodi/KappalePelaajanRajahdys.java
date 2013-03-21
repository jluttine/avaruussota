
/**
 * 
 * Luokka <CODE>KappalePelaajanRajahdys</CODE> kuvaa r�j�hdyst�, joka syntyy, kun pelaaja
 * kuolee. Kun r�j�hdys tuhoutuu, l�hett�� se viestin arpoa pelaajalle uusi sijainti.
 * R�j�hdys on kertak�ytt�inen: Pelaaja voidaan asettaa vain konstruktorissa ja se asetetaan
 * null:ksi, kun r�j�hdys tuhoutuu.
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
     * Luo uuden pelaajan r�j�hdyksen annetun mallin perusteella.
     * @param malli R�j�hdys, jonka perusteella r�j�hdys luodaan.
     * @param pelaaja Pelaaja, joka on kuollut.
     */
    public KappalePelaajanRajahdys( KappaleRajahdys malli, Pelaaja pelaaja ) {
        
        super( malli );
        this.pelaaja = pelaaja;
        
    }
    
    /**
     * Arpoo pelaajalle uuden sijainnin. Kutsuu yl�luokan tuhodu-metodia. Asettaa
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
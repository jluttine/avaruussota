
/**
 * 
 * Luokka <CODE>Tekoaly</CODE> mallintaa jonkun pelaajan teko�ly�. Teko�lyll�
 * voi olla jokin nimi. Teko�lylle pit�� kertoa sen nimi heti luonnin yhteydess�
 * eik� sit� voi muuttaa. Teko�lyll� on jokin pelaaja, jonka alusta se ohjaa. Kun
 * teko�lylle v�litet��n toimi-viesti, teko�ly miettii ja ohjaamansa pelaajan aluksen
 * ohjauksen ja v�litt�� ne alukselle.
 *
 * @author Jaakko Luttinen
 *
 */

public abstract class Tekoaly {

    /**
     * Pelaaja, jonka alusta teko�ly ohjaa.
     */
    private Pelaaja pelaaja;
    
    /**
     * Teko�ly nimi. Voi olla null.
     */
    private String nimi; 
 
    /**
     * Luo uuden teko�lyn annetulla nimell�.
     * @param nimi Teko�lyn nimi.
     */
    protected Tekoaly( String nimi ) {
        
        this.pelaaja = null;
        this.nimi = nimi;
        
    }
    
    /**
     * Luo uuden teko�lyn annetulla nimell� ja kiinnitt�� siihen annetun pelaajan.
     * @param pelaaja Pelaaja, jonka alusta teko�ly ohjaa.
     * @param nimi Teko�lyn nimi.
     */
    protected Tekoaly( Pelaaja pelaaja, String nimi ) {
        
        this.pelaaja = pelaaja;
        this.nimi = nimi;
        
    }
    
    /**
     * Kertoo pelaajan nimen.
     * @return Pelaajan nimi.
     */
    public String kerroNimi() {
        
        return this.nimi;
        
    }
    
    /**
     * Asettaa ohjattavan aluksen ohjauksen. Luokkahierarkian sis�iseen k�ytt��n.
     * @param suunta Ohjauksen suunta.
     */
    protected void asetaOhjaus( int suunta ) {
        
        this.pelaaja.kerroAlus().asetaOhjaus( suunta );
        
    }
    
    /**
     * Asettaa ohjattavan aluksen kaasun. Luokkahierarkian sis�iseen k�ytt��n.
     * @param kaasu Totuusarvo, joka kertoo, onko kaasu pohjassa.
     */
    protected void asetaKaasu( boolean kaasu ) {
        
        this.pelaaja.kerroAlus().asetaKaasu( kaasu );
               
    }
    
    /**
     * Asettaa ohjattavan aluksen liipaisimen. Luokkahierarkian sis�iseen k�ytt��n.
     * @param ammutaan Totuusarvo, joka kertoo, onko liipaisin pohjassa.
     */
    protected void asetaLiipaisin( boolean ammutaan ) {
        
        this.pelaaja.kerroAlus().asetaLiipaisin( ammutaan );
        
    }
    
    /**
     * Kertoo pelaajan, jonka alusta teko�ly ohjaa.
     * @return Pelaaja, jonka alusta teko�ly ohjaa.
     */
    public Pelaaja kerroPelaaja() {
        
        return this.pelaaja;
        
    }
    
    /**
     * Asettaa pelaajan, jonka alusta teko�ly ohjaa. Asettaa my�s pelaajan teko�lyksi
     * itsens�. Jos <CODE>pelaaja</CODE> on null tai sama kuin teko�lyn jo ohjaama
     * pelaaja, ei asetusta suoriteta.
     * @param pelaaja Pelaaja, jonka alusta teko�ly ohjaa.
     */
    public void asetaPelaaja( Pelaaja pelaaja ) {
        
        if ( pelaaja == null || this.pelaaja == pelaaja )
            return;
        
        this.poistaPelaaja();
        
        this.pelaaja = pelaaja;
        this.pelaaja.asetaTekoaly( this );
        
    }
    
    /**
     * Poistaa teko�lylt� pelaajan. Poistaa my�s pelaajalta teko�lyn.
     */
    public void poistaPelaaja() {
        
        if ( this.pelaaja != null ) {
            
            Pelaaja vanhaPelaaja = this.pelaaja;
            this.pelaaja = null;
            vanhaPelaaja.poistaTekoaly();
            
        }
        
    }

    /**
     * Kertoo teko�lyn ohjaaman pelaajan aluksen tai null, jos pelaajaa tai alusta ei ole.
     * @return Teko�lyn ohjaama alus tai null.
     */
    public KappalePelaajanAlus kerroAlus() {
        
        if ( this.pelaaja == null )
            return null;
        
        return this.pelaaja.kerroAlus();
        
    }
    
    /**
     * Kertoo teko�lyn ohjaaman aluksen sijainnin tai null, jos pelaajaa, alusta tai sijaintia
     * ei ole.
     * @return Teko�lyn ohjaaman aluksen sijainti tai null.
     */
    public Koordinaatit kerroSijainti() {
        
        KappalePelaajanAlus omaAlus = this.kerroAlus();
        
        if ( omaAlus == null )
            return null;
        
        return omaAlus.kerroSijainti();
        
    }
    
    /**
     * Palauttaa uuden kopion t�st� teko�lyst�. Jokaisen aliluokan tulisi kirjoittaa
     * t�lle metodille oma versionsa!
     * @return Kopio teko�lyst�.
     */
    public abstract Tekoaly kerroKopio();
   
    /**
     * Teko�ly tutkii tilanteen ja tekee haluamansa ohjaustoimenpiteet.
     */
    public abstract void toimi();
       
}
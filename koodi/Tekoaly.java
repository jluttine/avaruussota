
/**
 * 
 * Luokka <CODE>Tekoaly</CODE> mallintaa jonkun pelaajan tekoälyä. Tekoälyllä
 * voi olla jokin nimi. Tekoälylle pitää kertoa sen nimi heti luonnin yhteydessä
 * eikä sitä voi muuttaa. Tekoälyllä on jokin pelaaja, jonka alusta se ohjaa. Kun
 * tekoälylle välitetään toimi-viesti, tekoäly miettii ja ohjaamansa pelaajan aluksen
 * ohjauksen ja välittää ne alukselle.
 *
 * @author Jaakko Luttinen
 *
 */

public abstract class Tekoaly {

    /**
     * Pelaaja, jonka alusta tekoäly ohjaa.
     */
    private Pelaaja pelaaja;
    
    /**
     * Tekoäly nimi. Voi olla null.
     */
    private String nimi; 
 
    /**
     * Luo uuden tekoälyn annetulla nimellä.
     * @param nimi Tekoälyn nimi.
     */
    protected Tekoaly( String nimi ) {
        
        this.pelaaja = null;
        this.nimi = nimi;
        
    }
    
    /**
     * Luo uuden tekoälyn annetulla nimellä ja kiinnittää siihen annetun pelaajan.
     * @param pelaaja Pelaaja, jonka alusta tekoäly ohjaa.
     * @param nimi Tekoälyn nimi.
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
     * Asettaa ohjattavan aluksen ohjauksen. Luokkahierarkian sisäiseen käyttöön.
     * @param suunta Ohjauksen suunta.
     */
    protected void asetaOhjaus( int suunta ) {
        
        this.pelaaja.kerroAlus().asetaOhjaus( suunta );
        
    }
    
    /**
     * Asettaa ohjattavan aluksen kaasun. Luokkahierarkian sisäiseen käyttöön.
     * @param kaasu Totuusarvo, joka kertoo, onko kaasu pohjassa.
     */
    protected void asetaKaasu( boolean kaasu ) {
        
        this.pelaaja.kerroAlus().asetaKaasu( kaasu );
               
    }
    
    /**
     * Asettaa ohjattavan aluksen liipaisimen. Luokkahierarkian sisäiseen käyttöön.
     * @param ammutaan Totuusarvo, joka kertoo, onko liipaisin pohjassa.
     */
    protected void asetaLiipaisin( boolean ammutaan ) {
        
        this.pelaaja.kerroAlus().asetaLiipaisin( ammutaan );
        
    }
    
    /**
     * Kertoo pelaajan, jonka alusta tekoäly ohjaa.
     * @return Pelaaja, jonka alusta tekoäly ohjaa.
     */
    public Pelaaja kerroPelaaja() {
        
        return this.pelaaja;
        
    }
    
    /**
     * Asettaa pelaajan, jonka alusta tekoäly ohjaa. Asettaa myös pelaajan tekoälyksi
     * itsensä. Jos <CODE>pelaaja</CODE> on null tai sama kuin tekoälyn jo ohjaama
     * pelaaja, ei asetusta suoriteta.
     * @param pelaaja Pelaaja, jonka alusta tekoäly ohjaa.
     */
    public void asetaPelaaja( Pelaaja pelaaja ) {
        
        if ( pelaaja == null || this.pelaaja == pelaaja )
            return;
        
        this.poistaPelaaja();
        
        this.pelaaja = pelaaja;
        this.pelaaja.asetaTekoaly( this );
        
    }
    
    /**
     * Poistaa tekoälyltä pelaajan. Poistaa myös pelaajalta tekoälyn.
     */
    public void poistaPelaaja() {
        
        if ( this.pelaaja != null ) {
            
            Pelaaja vanhaPelaaja = this.pelaaja;
            this.pelaaja = null;
            vanhaPelaaja.poistaTekoaly();
            
        }
        
    }

    /**
     * Kertoo tekoälyn ohjaaman pelaajan aluksen tai null, jos pelaajaa tai alusta ei ole.
     * @return Tekoälyn ohjaama alus tai null.
     */
    public KappalePelaajanAlus kerroAlus() {
        
        if ( this.pelaaja == null )
            return null;
        
        return this.pelaaja.kerroAlus();
        
    }
    
    /**
     * Kertoo tekoälyn ohjaaman aluksen sijainnin tai null, jos pelaajaa, alusta tai sijaintia
     * ei ole.
     * @return Tekoälyn ohjaaman aluksen sijainti tai null.
     */
    public Koordinaatit kerroSijainti() {
        
        KappalePelaajanAlus omaAlus = this.kerroAlus();
        
        if ( omaAlus == null )
            return null;
        
        return omaAlus.kerroSijainti();
        
    }
    
    /**
     * Palauttaa uuden kopion tästä tekoälystä. Jokaisen aliluokan tulisi kirjoittaa
     * tälle metodille oma versionsa!
     * @return Kopio tekoälystä.
     */
    public abstract Tekoaly kerroKopio();
   
    /**
     * Tekoäly tutkii tilanteen ja tekee haluamansa ohjaustoimenpiteet.
     */
    public abstract void toimi();
       
}
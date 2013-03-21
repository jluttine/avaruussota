
/**
 * 
 * Luokka <CODE>SaieTietokoneohjaus</CODE> mallintaa s‰iett‰, joka v‰litt‰‰ teko‰lyille
 * tietyin v‰liajoin viestin, jotta ne voivat mietti‰ ja tehd‰ ohjaustoimenpiteens‰.
 * S‰ikeen voi k‰ynnist‰‰, lopettaa ja k‰ynnist‰‰ aina uudestaan. S‰ikeen k‰yntitilaa
 * voi muuttaa. Oliolle t‰ytyy kertoa peli, jonka teko‰lyille s‰ie v‰lit‰‰ toimi-viestin.
 *
 * @author Jaakko Luttinen
 *
 */
public class SaieTietokoneohjaus implements Runnable {
    
    /**
     * Peli, jonka teko‰lyist‰ s‰ie huolehtii.
     */
    private AvaruussotaPeli peli;
    
    /**
     * Totuusarvo, joka kertoo, onko s‰ie lopetettava tai jo lopetettu.
     */
    private boolean lopetettava;
    
    /**
     * S‰ikeen k‰yntitila. false = pys‰hdyksiss‰, true = k‰yntitilassa.
     */
    private boolean kaynnissa;
    
    /**
     * Aika (ms), jonka v‰lein aloitetaan uusi mietint‰kierros.
     */
    private int intervalli;
    
    /**
     * Uusi s‰ie taustalle.
     */
    private Thread ohjausSaie;
    
    /**
     * Luo uuden tietokoneohjauss‰ikeen. Asettaa mietint‰intervalliksi jonkin alkuarvon.
     */
    public SaieTietokoneohjaus( AvaruussotaPeli peli ) {
  
        this.peli = peli;
        this.kaynnissa = true;
        this.intervalli = 50;
        
    }
    
    /**
     * K‰ynnist‰‰ s‰ikeen. Lopettaa mahdollisen aiemman s‰ikeen pyˆrimisen.
     */
    public void kaynnista() {
       
        this.lopeta();
        
        //Odotetaan, ett‰ edellinen s‰ie saa lopetettua.
        while ( this.ohjausSaie != null )
            Thread.yield();
        
        this.lopetettava = false;
        this.ohjausSaie = new Thread( this );
        this.ohjausSaie.setDaemon( true );
        this.ohjausSaie.setName( "Tietokoneohjaus-s‰ie" );
        this.ohjausSaie.start();
        
    }
    
    /**
     * Lopettaa s‰ikeen.
     */
    public synchronized void lopeta() {
        
        this.lopetettava = true;
        
    }

    /**
     * Asettaa s‰ikeen k‰yntitilan. false=pys‰hdyksiss‰, true=k‰yntitilassa.
     * @param kaynnissa S‰ikeen k‰yntitila.
     */
    public void asetaKaynti( boolean kaynnissa ) {
        
        this.kaynnissa = kaynnissa;
        
    }
    
    /**
     * Kertoo s‰ikeen k‰yntitilan.
     * @return S‰ikeen k‰yntitila.
     */
    public boolean kerroKaynti() {
        
        return this.kaynnissa;
        
    }
    
    /**
     * Asettaa teko‰lyjen mietint‰tauon. Jos <CODE>intervalli</CODE> on ep‰positiivinen,
     * ei intervallia aseteta.
     * @param intervalli Mietint‰tauko millisekunteina.
     */
    public void asetaIntervalli( int intervalli ) {
        
        if ( intervalli > 0 )
            this.intervalli = intervalli;
        
    }
    
    /**
     * S‰ikeen toimimisen edellytt‰m‰ metodi. Ei ulkopuoliseen kutsumiseen.
     */
    public void run() {
        
        while ( !this.lopetettava ) {
            
            synchronized ( this ) {
                
                if ( this.kaynnissa ) {
       
                    try {
                        
                        //T‰ss‰ jokainen teko‰ly sitten saa toimia.
                        for ( int ind = 0; ind < this.peli.kerroPelaajienMaara(); ind++ ) {
                            
                            try {
                                
                            Tekoaly tekoaly = this.peli.kerroPelaaja( ind ).kerroTekoaly();
                            
                            if ( tekoaly != null )
                                tekoaly.toimi();
                            
                            }
                            catch ( Exception virhe ) {  } //Pelaajan indeksi virheellinen..
                            
                        }
                    
                    }
                    catch ( RuntimeException virhe ) {
                        
                        ( new DialogiIlmoitus( null, "Vakava virhe", "Ohjelmassa on tapahtunut virhe, josta se ei kykene suoriutumaan: " +
                                									 virhe.getMessage() ) ).setVisible( true );
                        System.exit( 1 );
                        
                    }
                    
                }
                
            }
            
            try {
                
                Thread.sleep( this.intervalli );
                
            }
            catch ( InterruptedException e ) { }
            
        }
        
        //Nyt s‰ie on t‰ysin loppunut.
        this.ohjausSaie = null;
        
    }
    
}
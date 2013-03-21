
import java.util.ArrayList;

/**
 * 
 * Luokka <CODE>SaieNaytonpaivitys</CODE> kuvaa sellaista luokkaa, joka käynnistettäessan
 * luo uuden säikeen. Tämä säie välittää tiheään tahtiin sille annetuille piirrettäville
 * olioille viestin piirtää itsensä. Tämän olion voi lopettaa ja käynnistää useaan
 * kertaan (vrt. puhdas säie). Olio voidaan myös pysäyttää eli kertoa sille, ettei
 * sen tarvitse välittää piirtokomentoja. Olio välittää viestin sen piirrettäväksi
 * määrätyille olioilleen, jos tämä päivitystila muuttuu.
 * 
 * @author Jaakko Luttinen
 *
 */
public class SaieNaytonpaivitys implements Runnable {
    
    /**
     * Totuusarvo kertoo, onko säie lopetettava.
     */
    private boolean lopetettava;
    
    /**
     * Totuusarvo kertoo, piirretäänkö.
     */
    private boolean paivitetaan;
    
    /**
     * Säie.
     */
    private Thread saie;
    
    /**
     * Piirrettäväksi määrätyt oliot.
     */
    private ArrayList piirrettavat;

    /**
     * Luo uuden näytönpäivityssäikeen.
     */
    public SaieNaytonpaivitys() {

        this.piirrettavat = new ArrayList();
        this.lopetettava = true;
        this.paivitetaan = true;
        this.saie = null;
        
    }

    /**
     * Asettaa näytönpäivityksen tilan. Jos tila muuttuu, välittää viestin
     * piirrettäväksi annetuille oliolleen.
     * @param paivitetaan Totuusarvo kertoo, päivitetäänkö piirrettäviä.
     */
    public void asetaPaivitys( boolean paivitetaan ) {
        
        //Jos tila muuttuu..
        if ( this.paivitetaan != paivitetaan ) {
            
            this.paivitetaan = paivitetaan;
            for ( int ind = 0; ind < this.piirrettavat.size(); ind++ )
                ( ( RajapintaPiirrettava )this.piirrettavat.get( ind ) ).paivitetaan( this.paivitetaan );
            
        }
        
    }

    /**
     * Lisää piirrettävien joukkoon annetun piirrettävän olion, jos se ei ole <CODE>null</CODE>.
     * @param piirrettava Olio, jolle piirrä-komentoja halutaan välitettävän.
     */
    public void lisaaPiirrettava( RajapintaPiirrettava piirrettava ) {
        
        if ( piirrettava != null ) {
            
            this.piirrettavat.add( piirrettava );
            piirrettava.paivitetaan( this.paivitetaan );
            
        }
        
    }
    
    /**
     * Lopettaa mahdollisen aiemman säikeen ja käynnistää uuden säikeen.
     */
    public void kaynnista() {
        
        //Lopetetaan edellinen säie.
        this.lopeta();
        
        //Odotetaan, että se todella loppuu.
        while ( this.saie != null )
            Thread.yield();
        
        //Käynnistetään uusi säie.
        this.lopetettava = false;
        this.saie = new Thread( this );
        this.saie.setName( "Näytönpäivitys-säie" );
        this.saie.start();
        
    }
    
    /**
     * Lopettaa säikeen.
     */
    public synchronized void lopeta() {
        
        this.lopetettava = true;
        
    }

    /**
     * Säikeen vaatima metodi. Ulkopuolisten ei tulisi tätä metodia kutsua.
     */
    public void run() {
        
        //Frameraten tulostusta varten.
        long aikaNyt = System.currentTimeMillis();
        
        while ( !this.lopetettava ) {
            
            synchronized ( this ) {
                
                //Nämä ovat sitä varten, jos halutaan tuo frameratea tulostaa.
	            long aikaViimeksi = aikaNyt;
	            aikaNyt = System.currentTimeMillis();

                //Tässä näytönpäivitysrutiinit.
                if ( this.paivitetaan ) {
                    
                    /*
                    //Kommentoimalla tuon heti edellisen rivin pois, tulostaa peli näytölle
                    //frameratea. 
                    if ( aikaViimeksi != aikaNyt )
                        System.out.println( "Framerate: " + ( 1000 / ( aikaNyt - aikaViimeksi ) ) );
                    //*/    

                    for ( int ind = 0; ind < this.piirrettavat.size(); ind++ )
                        ( ( RajapintaPiirrettava )this.piirrettavat.get( ind ) ).piirra();
                
                }
                
            }
            
            try {
                
                Thread.sleep( 15 );
                
            }
            catch ( InterruptedException virhe ) {  }
            
        }
        
        //Merkkinä säikeen loppumisesta.
        this.saie = null;
        
    }
    
}
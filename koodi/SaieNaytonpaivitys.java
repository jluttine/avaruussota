
import java.util.ArrayList;

/**
 * 
 * Luokka <CODE>SaieNaytonpaivitys</CODE> kuvaa sellaista luokkaa, joka k�ynnistett�essan
 * luo uuden s�ikeen. T�m� s�ie v�litt�� tihe��n tahtiin sille annetuille piirrett�ville
 * olioille viestin piirt�� itsens�. T�m�n olion voi lopettaa ja k�ynnist�� useaan
 * kertaan (vrt. puhdas s�ie). Olio voidaan my�s pys�ytt�� eli kertoa sille, ettei
 * sen tarvitse v�litt�� piirtokomentoja. Olio v�litt�� viestin sen piirrett�v�ksi
 * m��r�tyille olioilleen, jos t�m� p�ivitystila muuttuu.
 * 
 * @author Jaakko Luttinen
 *
 */
public class SaieNaytonpaivitys implements Runnable {
    
    /**
     * Totuusarvo kertoo, onko s�ie lopetettava.
     */
    private boolean lopetettava;
    
    /**
     * Totuusarvo kertoo, piirret��nk�.
     */
    private boolean paivitetaan;
    
    /**
     * S�ie.
     */
    private Thread saie;
    
    /**
     * Piirrett�v�ksi m��r�tyt oliot.
     */
    private ArrayList piirrettavat;

    /**
     * Luo uuden n�yt�np�ivityss�ikeen.
     */
    public SaieNaytonpaivitys() {

        this.piirrettavat = new ArrayList();
        this.lopetettava = true;
        this.paivitetaan = true;
        this.saie = null;
        
    }

    /**
     * Asettaa n�yt�np�ivityksen tilan. Jos tila muuttuu, v�litt�� viestin
     * piirrett�v�ksi annetuille oliolleen.
     * @param paivitetaan Totuusarvo kertoo, p�ivitet��nk� piirrett�vi�.
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
     * Lis�� piirrett�vien joukkoon annetun piirrett�v�n olion, jos se ei ole <CODE>null</CODE>.
     * @param piirrettava Olio, jolle piirr�-komentoja halutaan v�litett�v�n.
     */
    public void lisaaPiirrettava( RajapintaPiirrettava piirrettava ) {
        
        if ( piirrettava != null ) {
            
            this.piirrettavat.add( piirrettava );
            piirrettava.paivitetaan( this.paivitetaan );
            
        }
        
    }
    
    /**
     * Lopettaa mahdollisen aiemman s�ikeen ja k�ynnist�� uuden s�ikeen.
     */
    public void kaynnista() {
        
        //Lopetetaan edellinen s�ie.
        this.lopeta();
        
        //Odotetaan, ett� se todella loppuu.
        while ( this.saie != null )
            Thread.yield();
        
        //K�ynnistet��n uusi s�ie.
        this.lopetettava = false;
        this.saie = new Thread( this );
        this.saie.setName( "N�yt�np�ivitys-s�ie" );
        this.saie.start();
        
    }
    
    /**
     * Lopettaa s�ikeen.
     */
    public synchronized void lopeta() {
        
        this.lopetettava = true;
        
    }

    /**
     * S�ikeen vaatima metodi. Ulkopuolisten ei tulisi t�t� metodia kutsua.
     */
    public void run() {
        
        //Frameraten tulostusta varten.
        long aikaNyt = System.currentTimeMillis();
        
        while ( !this.lopetettava ) {
            
            synchronized ( this ) {
                
                //N�m� ovat sit� varten, jos halutaan tuo frameratea tulostaa.
	            long aikaViimeksi = aikaNyt;
	            aikaNyt = System.currentTimeMillis();

                //T�ss� n�yt�np�ivitysrutiinit.
                if ( this.paivitetaan ) {
                    
                    /*
                    //Kommentoimalla tuon heti edellisen rivin pois, tulostaa peli n�yt�lle
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
        
        //Merkkin� s�ikeen loppumisesta.
        this.saie = null;
        
    }
    
}
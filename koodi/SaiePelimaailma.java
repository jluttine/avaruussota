
/**
 * 
 * Luokka <CODE>SaiePelimaailma</CODE> on mallintaa s�iett�, joka py�ritt��
 * sille annettua maailmaa. S�ie l�hett�� pienin v�liajoin maailmalle viestin
 * suorittaa syk�yksen ja antaa sille ajan, joka on kulunut viime syk�yksest�.
 *  S�ikeen voi k�ynnist��, lopettaa ja k�ynnist�� aina uudestaan. S�ikeen voi
 * pys�ytt�� ja laittaa taas k�yntiin. S�ie my�s laskee kaikkien syk�yksien
 * muodostamaa yhteenlaskettua ajankulumaa.
 *
 * @author Jaakko Luttinen
 *
 */
public class SaiePelimaailma implements Runnable {
    
    /**
     * Maksimisyk�ys, joka maailmaan v�litet��n. Suuremmat ajankulumat
     * v�litet��n t�m�n suuruisina. Saattaa aiheutta nykimist� ja "lagaamista",
     * mutta se on parempi kuin harppominen.
     */
    public static final int AJAN_MAKSIMISYKAYS = 50;
    
    /**
     * Maailma, jolle syk�yksi� v�litet��n.
     */
    private Avaruus maailma;
    
    /**
     * Totuusarvo, joka kertoo, onko peli pys�ytys- vai k�yntitilassa.
     */
    private boolean kaynnissa;
    
    /**
     * Totuusarvo, joka kertoo, pit��k� s�ie lopettaa tai onko se lopetettu.
     */
    private boolean lopetettava;
    
    /**
     * Kaikkien syk�yksien yhteenlaskettu aika viime k�ynnistyksest� (aloituksesta).
     */
    private long pelinKesto;
    
    /**
     * S�ie, joka py�rii taustalla.
     */
    private Thread saie;
    
    /**
     * Luo uuden pelimaailmaa py�ritt�v�n s�ikeen.
     */
    public SaiePelimaailma() {
        
        this.kaynnissa = true;
        this.lopetettava = true;
        this.pelinKesto = 0;
        this.saie = null;
        
    }
    
    /**
     * K�ynnist�� uuden s�ikeen py�ritt�m��n annettua maailmaa. Lopettaa mahdollisen
     * aiemman maailman py�ritt�misen.
     * @param maailma Avaruus, jolle syk�yksi� halutaan v�litt��.
     */
    public void kaynnista( Avaruus maailma ) {

        //Lopetetaan aiempi s�ie.
        this.lopeta();

        //Odotetaan, ett� s�ie saa lopetettua.
        while ( this.onKaynnissa() )
            Thread.yield();

        this.maailma = maailma;
        this.lopetettava = false;
        this.pelinKesto = 0;
 
        this.saie = new Thread( this );
        this.saie.setDaemon( true );
        this.saie.setName( "Pelimaailma -thread" );
        this.saie.start();
        
    }
    
    /**
     * Kertoo, onko s�ie t�ll� hetkell� luotu ja k�ynnistetty. Huomaa, ett�
     * t�m�n vastakohta on lopetettu. K�ynti- ja pys�ytystilat ovat vain
     * k�ynnistetyn s�ikeen kaksi eri tilaa. Huomaa ero! 
     * @return Totuusarvo, joka kertoo, onko s�ie k�ynnistetty.
     */
    public boolean onKaynnissa() {
        
        return this.saie != null;
        
    }
    
    /**
     * Asettaa s�ikeen k�yntitilan. false pys�ytt�� s�ikeen, true laittaa
     * s�ikeen taas toimimaan.
     * @param kaynnissa S�ikeen k�yntitila.
     */
    public void asetaKaynti( boolean kaynnissa ) {
        
        this.kaynnissa = kaynnissa;
        
    }
    
    /**
     * Kertoo s�ikeen k�yntitilan.
     * @return S�ikeen k�yntitila.
     */
    public boolean kerroKaynti() {
        
        return this.kaynnissa;
        
    }
    
    /**
     * Lopettaa s�ikeen.
     */
    public synchronized void lopeta() {
        
        this.lopetettava = true;
        
    }
    
    /**
     * S�ikeen toimimisen edellytt�m� metodi. K�ytt�j�n ei tulisi t�t�
     * metodia kutsua. Jos k�yntitila on asetettu, v�litt�� metodi
     * maailmaan syk�yksi�, joiden suuruus on edellisest� syk�yksest� kulunut
     * aika millisekunteina.
     */
    public void run() {
        
        long aikaNyt = System.currentTimeMillis();
        
        while ( !this.lopetettava ) {
            
            synchronized ( this ) {
                
                long aikaViimeksi = aikaNyt;
                aikaNyt = System.currentTimeMillis();
                int aikaaKulunut = ( int )( aikaNyt - aikaViimeksi );
                
                if ( this.kaynnissa && aikaaKulunut > 0 ) {
                    
                    if ( aikaaKulunut > SaiePelimaailma.AJAN_MAKSIMISYKAYS )
                        aikaaKulunut = SaiePelimaailma.AJAN_MAKSIMISYKAYS;
                    
                    try {
                        
                        this.maailma.suoritaSykays( aikaaKulunut );
                        
                    }
                    catch ( RuntimeException virhe ) {
                        
                        ( new DialogiIlmoitus( null, "Vakava virhe", "Ohjelmassa on tapahtunut virhe, josta se ei kykene suoriutumaan: " +
                                									 virhe.getMessage() ) ).setVisible( true );
                        System.exit( 1 );
                        
                    }
                    
                    this.pelinKesto += aikaaKulunut;
                    
                    //Kun poistaa alla olevalta rivilt� kommentoinnin, saa s�ikeen
                    //toiminnasta tulostumaan tietoa.
                    //System.out.println( "Syk�ys: " + aikaaKulunut + " -- Pelin kesto: " + this.pelinKesto );
                    
                }
                
            }
            
            try {
                
                Thread.sleep( 1 );
                
            }
            catch ( InterruptedException virhe ) {  }
            
        }

        //S�ie on nyt t�ysin lopetettu. T�m� asetus on merkkin� siit�.
        this.saie = null;

    }
    
}
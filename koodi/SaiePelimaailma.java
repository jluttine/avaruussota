
/**
 * 
 * Luokka <CODE>SaiePelimaailma</CODE> on mallintaa säiettä, joka pyörittää
 * sille annettua maailmaa. Säie lähettää pienin väliajoin maailmalle viestin
 * suorittaa sykäyksen ja antaa sille ajan, joka on kulunut viime sykäyksestä.
 *  Säikeen voi käynnistää, lopettaa ja käynnistää aina uudestaan. Säikeen voi
 * pysäyttää ja laittaa taas käyntiin. Säie myös laskee kaikkien sykäyksien
 * muodostamaa yhteenlaskettua ajankulumaa.
 *
 * @author Jaakko Luttinen
 *
 */
public class SaiePelimaailma implements Runnable {
    
    /**
     * Maksimisykäys, joka maailmaan välitetään. Suuremmat ajankulumat
     * välitetään tämän suuruisina. Saattaa aiheutta nykimistä ja "lagaamista",
     * mutta se on parempi kuin harppominen.
     */
    public static final int AJAN_MAKSIMISYKAYS = 50;
    
    /**
     * Maailma, jolle sykäyksiä välitetään.
     */
    private Avaruus maailma;
    
    /**
     * Totuusarvo, joka kertoo, onko peli pysäytys- vai käyntitilassa.
     */
    private boolean kaynnissa;
    
    /**
     * Totuusarvo, joka kertoo, pitääkö säie lopettaa tai onko se lopetettu.
     */
    private boolean lopetettava;
    
    /**
     * Kaikkien sykäyksien yhteenlaskettu aika viime käynnistyksestä (aloituksesta).
     */
    private long pelinKesto;
    
    /**
     * Säie, joka pyörii taustalla.
     */
    private Thread saie;
    
    /**
     * Luo uuden pelimaailmaa pyörittävän säikeen.
     */
    public SaiePelimaailma() {
        
        this.kaynnissa = true;
        this.lopetettava = true;
        this.pelinKesto = 0;
        this.saie = null;
        
    }
    
    /**
     * Käynnistää uuden säikeen pyörittämään annettua maailmaa. Lopettaa mahdollisen
     * aiemman maailman pyörittämisen.
     * @param maailma Avaruus, jolle sykäyksiä halutaan välittää.
     */
    public void kaynnista( Avaruus maailma ) {

        //Lopetetaan aiempi säie.
        this.lopeta();

        //Odotetaan, että säie saa lopetettua.
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
     * Kertoo, onko säie tällä hetkellä luotu ja käynnistetty. Huomaa, että
     * tämän vastakohta on lopetettu. Käynti- ja pysäytystilat ovat vain
     * käynnistetyn säikeen kaksi eri tilaa. Huomaa ero! 
     * @return Totuusarvo, joka kertoo, onko säie käynnistetty.
     */
    public boolean onKaynnissa() {
        
        return this.saie != null;
        
    }
    
    /**
     * Asettaa säikeen käyntitilan. false pysäyttää säikeen, true laittaa
     * säikeen taas toimimaan.
     * @param kaynnissa Säikeen käyntitila.
     */
    public void asetaKaynti( boolean kaynnissa ) {
        
        this.kaynnissa = kaynnissa;
        
    }
    
    /**
     * Kertoo säikeen käyntitilan.
     * @return Säikeen käyntitila.
     */
    public boolean kerroKaynti() {
        
        return this.kaynnissa;
        
    }
    
    /**
     * Lopettaa säikeen.
     */
    public synchronized void lopeta() {
        
        this.lopetettava = true;
        
    }
    
    /**
     * Säikeen toimimisen edellyttämä metodi. Käyttäjän ei tulisi tätä
     * metodia kutsua. Jos käyntitila on asetettu, välittää metodi
     * maailmaan sykäyksiä, joiden suuruus on edellisestä sykäyksestä kulunut
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
                    
                    //Kun poistaa alla olevalta riviltä kommentoinnin, saa säikeen
                    //toiminnasta tulostumaan tietoa.
                    //System.out.println( "Sykäys: " + aikaaKulunut + " -- Pelin kesto: " + this.pelinKesto );
                    
                }
                
            }
            
            try {
                
                Thread.sleep( 1 );
                
            }
            catch ( InterruptedException virhe ) {  }
            
        }

        //Säie on nyt täysin lopetettu. Tämä asetus on merkkinä siitä.
        this.saie = null;

    }
    
}
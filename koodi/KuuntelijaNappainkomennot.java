
import java.awt.event.*;

/**
 * 
 * Luokka <CODE>KuuntelijaNappainkomennot</CODE> on pelin näppäinsyötteestä huolehtiva
 * luokka. Olio täytyy asettaa peli-ikkunan näppäintenkuuntelijaksi, niin se
 * osaa sen jälkeen tulkita näppäinpainallukset ja välittää tarvittavat viestit
 * pelille. Näppäimistökomentoja välitetään kaikille pelin ihmispelaajille. Jos
 * pelissä on liikaa ihmispelaajia, konstruktori antaa virheilmoituksen. Myöhemmin
 * liiasta ihmispelaajien määrästä ei enää huomauteta, joten ihmispelaajia ei
 * tulisi enää sen jälkeen lisätä. Jos niitä lisätään, komentoja välitetään
 * vain sille määrälle ihmispelaajia, joita luokka tukee.
 * Näppäimiä ei voi muuttaa, ne ovat tietyt vakiot.
 * 
 * @author Jaakko Luttinen
 *
 */
public class KuuntelijaNappainkomennot implements KeyListener {
    
    /**
     * Näppäimistöllä ohjattavien pelaajien maksimimäärä.
     */
    public static final int NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA = 2;
 
    /**
     * Vakio, joka kertoo, kuinka monta erilaista ohjausnäppäintä yhdellä pelaajalla on.
     */
    public static final int NAPPAIN_OHJAUSMAARA = 4;
    
    /**
     * Kaasunäppäimen indeksi.
     */
    public static final int NAPPAIN_OHJAUSKAASU = 0;
    
    /**
     * Vasemmalle ohjaavan näppäimen indeksi.
     */
    public static final int NAPPAIN_OHJAUSVASEMMALLE = 1;
    
    /**
     * Oikealle ohjaavan näppäimen indeksi.
     */
    public static final int NAPPAIN_OHJAUSOIKEALLE = 2;
    
    /**
     * Liipaisimena toimivan näppäimen indeksi.
     */
    public static final int NAPPAIN_OHJAUSLIIPAISIN = 3;
    
    /**
     * Peli, johon näppäinkomentojen aikaansaamat komennot välitetään.
     */
    private Tulkki peli;
    
    /**
     * Näppäinkomennot, joilla ohjauksia suoritetaan. Indeksit lasketaan luokan
     * vakioiden avulla.
     */
    private Nappain[] nappainkomennot;
    
    /**
     * Luo uuden näppäinkomentojen kuuntelijan. Käyttäjän tulee huolehtia siitä,
     * että asettaa luodun kuuntelijan jonkun ikkunan (tms.) kuuntelijaksi.
     * @param peli Peli, johon komennot välitetään.
     * @throws Exception Poikkeus kertoo, että peliä ei ole annettu, pelissä on
     * liikaa ihmispelaajia tai luokan näppäinkomentojen rakentelussa on tapahtunut
     * virhe.
     */
    public KuuntelijaNappainkomennot( Tulkki peli ) throws Exception {
       
        if ( peli == null )
            throw new Exception( "Näppäinkomentojen kuuntelijalle ei määritetty peliä." );
        
        this.peli = peli;
        
        int ohjattavienMaara = this.peli.kerroIhmispelaajienMaara();
        
        if ( ohjattavienMaara > KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA ) {
        
            throw new Exception( "Näppäimistölllä ei voida ohjata maksimissaan kuin " + 
                    			 KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA +
                    			 " pelaajaa." );
            
        }
        
        this.muodostaNappainkomennot();
        
    }
    
    /**
     * Näppäinkomentojen kuuntelijan metodi. Kutsutaan, kun näppäintä
     * painetaan. Välittää sen mukaiset ohjauskomennot.
     */
    public void keyPressed( KeyEvent event ) {
        
        this.suoritaNappainviesti( event.getKeyCode(), true );
        
    }
    
    /**
     * Näppäinkomentojen kuuntelijan metodi. Kutsutaan, kun näppäin
     * vapautetaan. Välittää sen mukaiset ohjauskomennot.
     */
    public void keyReleased( KeyEvent event ) {

        this.suoritaNappainviesti( event.getKeyCode(), false );
        
    }
    
    /**
     * Näppäinkomentojen kuuntelijan metodi. Kutsutaan, kun merkki
     * kirjoitetaan. Ei käyttöä tässä luokassa.
     */
    public void keyTyped( KeyEvent event ) {
        
        return;
        
    }
    
    /**
     * Suorittaa sellaisen näppäintoiminnon, jossa annetun näppäimen
     * tila on <CODE>pohjassa</CODE>-muuttujan määräämä.
     * @param nappainkoodi Näppäin, jonka tila on annettu.
     * @param pohjassa Totuusarvo kertoo, onko näppäin pohjassa.
     */
    private void suoritaNappainviesti( int nappainkoodi, boolean pohjassa ) {
        
        int ohjattavienMaara = this.peli.kerroIhmispelaajienMaara();
        
        //Välittää viestin kullekin näppäimelle ja, jos viesti saa aikaan muutoksen,
        //päivitetään ohjausta sen mukaan.
        for ( int ind  = 0; ind < ohjattavienMaara && ind < KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA; ind++ ) {
 
            if ( this.viestiNappaimelle( this.kerroOhjausKaasuIndeksi( ind ), nappainkoodi, pohjassa ) )
                this.paivitaKaasu( ind );

            if ( this.viestiNappaimelle( this.kerroOhjausVasemmalleIndeksi( ind ), nappainkoodi, pohjassa ) )
                this.paivitaOhjaus( ind );
            
            if ( this.viestiNappaimelle( this.kerroOhjausOikealleIndeksi( ind ), nappainkoodi, pohjassa ) )
                this.paivitaOhjaus( ind );

            if ( this.viestiNappaimelle( this.kerroOhjausLiipaisinIndeksi( ind ), nappainkoodi, pohjassa ) )
                this.paivitaLiipaisin( ind );

        }

    }
    
    /**
     * Jos annettua indeksiä vastaavan näppäimen näppäinkoodi on sama kuin annettu
     * näppäinkoodi, asettaa sen tilaksi annetun tilan. Jos näppäimen tila nyt
     * muuttuu, palautetaan false.
     * @param nappainIndeksi Näppäimen indeksi <CODE>nappainkomennot</CODE> taulukossa.
     * @param nappainkoodi Viestin näppäinkoodi.
     * @param pohjassa Totuusarvo kertoo, onko näppäinkoodin näppäin pohjassa.
     * @return Totuusarvo kertoo, vastasiko näppäinkoodi indeksiä vastaavan
     * näppäimen näppäinkoodia, ja jos vastasi, muuttuiko tämän näppäimen tila, kun sen
     * tilaksi asetettiin annettu tila.
     */
    private boolean viestiNappaimelle( int nappainIndeksi, int nappainkoodi, boolean pohjassa ) {

        //Tutkitaan, onko kyse samasta näppäimestä.
        if ( nappainkoodi == this.nappainkomennot[nappainIndeksi].kerroNappainkoodi() ) {
            
            //Tutkitaan, muuttuuko näppäimen tila.
            if ( this.nappainkomennot[nappainIndeksi].onPohjassa() != pohjassa ) {
                
                //Muutetaan näppäimen tila ja palautetaan true.
                this.nappainkomennot[nappainIndeksi].asetaTila( pohjassa );
                return true;
                
            }
             
        }
        
        return false;

    }
    
    /**
     * Päivittää annetun ihmispelaajan ohjauksen.
     * @param ohjattava Ihmispelaajaindeksi.
     */
    private void paivitaOhjaus( int ohjattava ) {
        
        //Suunta suoraan.
        int suunta = 0;
        
        //Pyöritetään vasemmalle.
        if ( this.nappainkomennot[this.kerroOhjausVasemmalleIndeksi(ohjattava)].onPohjassa() )
            suunta -= 1;
        
        //Pyöritetään oikealle.
        if ( this.nappainkomennot[this.kerroOhjausOikealleIndeksi(ohjattava)].onPohjassa() )
            suunta += 1;
        
        //Korjataan ohjaus.
        this.peli.asetaIhmispelaajanOhjaus( ohjattava, suunta );

    }
    
    /**
     * Päivittää annetun ihmispelaajan kaasun.
     * @param ohjattava Ihmispelaajaindeksi.
     */
    private void paivitaKaasu( int ohjattava ) {
        
        boolean kaasuPohjassa = this.nappainkomennot[this.kerroOhjausKaasuIndeksi(ohjattava)].onPohjassa();
        
        this.peli.asetaIhmispelaajanKaasu( ohjattava, kaasuPohjassa );
        
    }
    
    /**
     * Päivittää annetun ihmispelaajan liipaisimen.
     * @param ohjattava Ihmispelaajaindeksi.
     */
    private void paivitaLiipaisin( int ohjattava ) {
        
        boolean liipaisinPohjassa = this.nappainkomennot[this.kerroOhjausLiipaisinIndeksi(ohjattava)].onPohjassa();
        
        this.peli.asetaIhmispelaajanLiipaisin( ohjattava, liipaisinPohjassa );
        
    }
    
    /**
     * Rakentaa näppäinkomennot.
     * @throws Exception Poikkeus kertoo, ettei näppäinkomentoa olekaan määritetty niin
     * monelle kuin luokan vakio antaa ymmärtää.
     */
    private void muodostaNappainkomennot() throws Exception {
        
        this.nappainkomennot = new Nappain[KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA*KuuntelijaNappainkomennot.NAPPAIN_OHJAUSMAARA];
        int ind = 0;
        
        //Jos luokka tukee vähintään yhtä pelaajaa:
        if ( KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA >= ind + 1 ) {
            
            nappainkomennot[this.kerroOhjausKaasuIndeksi(ind)] = new Nappain( KeyEvent.VK_UP );
            nappainkomennot[this.kerroOhjausVasemmalleIndeksi(ind)] = new Nappain( KeyEvent.VK_LEFT );
            nappainkomennot[this.kerroOhjausOikealleIndeksi(ind)] = new Nappain( KeyEvent.VK_RIGHT );
            nappainkomennot[this.kerroOhjausLiipaisinIndeksi(ind)] = new Nappain( KeyEvent.VK_CONTROL );
            ind++;
            
        }
        
        //Jos luokka tukee vähintään kahta pelaajaa:
        if ( KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA >= ind + 1 ) {
            
            nappainkomennot[this.kerroOhjausKaasuIndeksi(ind)] = new Nappain( KeyEvent.VK_E );
            nappainkomennot[this.kerroOhjausVasemmalleIndeksi(ind)] = new Nappain( KeyEvent.VK_S );
            nappainkomennot[this.kerroOhjausOikealleIndeksi(ind)] = new Nappain( KeyEvent.VK_F );
            nappainkomennot[this.kerroOhjausLiipaisinIndeksi(ind)] = new Nappain( KeyEvent.VK_Q );
            ind++;
            
        }
        
        //Jos luokka väittää tukevansa useampia pelaajia, heitetään poikkeus.
        if ( KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA >= ind + 1 )
            throw new Exception( ( ind + 1 ) + " pelaajalle ei ole määritelty näppäinkomentoja." );
       
    }

    /**
     * Kertoo annetun ihmispelaajan kaasu-näppäimen näppäinkoodin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Kaasu-näppäimen näppäinkoodi.
     */
    public int kerroOhjausKaasuNappainkoodi( int ohjattava ) {
        
        return this.nappainkomennot[this.kerroOhjausKaasuIndeksi(ohjattava)].kerroNappainkoodi();
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan vasemmalle-näppäimen näppäinkoodin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Vasemmalle-näppäimen näppäinkoodi.
     */
    public int kerroOhjausVasemmalleNappainkoodi( int ohjattava ) {
        
        return this.nappainkomennot[this.kerroOhjausVasemmalleIndeksi(ohjattava)].kerroNappainkoodi();
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan oikealle-näppäimen näppäinkoodin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Oikealle-näppäimen näppäinkoodi.
     */
    public int kerroOhjausOikealleNappainkoodi( int ohjattava ) {
        
        return this.nappainkomennot[this.kerroOhjausOikealleIndeksi(ohjattava)].kerroNappainkoodi();
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan liipaisin-näppäimen näppäinkoodin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Liipaisin-näppäimen näppäinkoodi.
     */
    public int kerroOhjausLiipaisinNappainkoodi( int ohjattava ) {
        
        return this.nappainkomennot[this.kerroOhjausLiipaisinIndeksi(ohjattava)].kerroNappainkoodi();
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan kaasu-näppäimen indeksin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Kaasu-näppäimen indeksi.
     */
    private int kerroOhjausKaasuIndeksi( int ohjattava ) {
        
        return ohjattava * KuuntelijaNappainkomennot.NAPPAIN_OHJAUSMAARA +
        	   KuuntelijaNappainkomennot.NAPPAIN_OHJAUSKAASU;
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan vasemmalle-näppäimen indeksin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Vasemmalle-näppäimen indeksi.
     */
    private int kerroOhjausVasemmalleIndeksi( int ohjattava ) {
        
        return ohjattava * KuuntelijaNappainkomennot.NAPPAIN_OHJAUSMAARA +
        	   KuuntelijaNappainkomennot.NAPPAIN_OHJAUSVASEMMALLE;
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan oikealle-näppäimen indeksin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Oikealle-näppäimen indeksi.
     */
    private int kerroOhjausOikealleIndeksi( int ohjattava ) {
        
        return ohjattava * KuuntelijaNappainkomennot.NAPPAIN_OHJAUSMAARA +
        	   KuuntelijaNappainkomennot.NAPPAIN_OHJAUSOIKEALLE;
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan liipaisin-näppäimen indeksin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Liipaisin-näppäimen indeksi.
     */
    private int kerroOhjausLiipaisinIndeksi( int ohjattava ) {
        
        return ohjattava * KuuntelijaNappainkomennot.NAPPAIN_OHJAUSMAARA +
        	   KuuntelijaNappainkomennot.NAPPAIN_OHJAUSLIIPAISIN;
        
    }

}
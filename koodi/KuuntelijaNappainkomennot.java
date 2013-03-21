
import java.awt.event.*;

/**
 * 
 * Luokka <CODE>KuuntelijaNappainkomennot</CODE> on pelin n‰pp‰insyˆtteest‰ huolehtiva
 * luokka. Olio t‰ytyy asettaa peli-ikkunan n‰pp‰intenkuuntelijaksi, niin se
 * osaa sen j‰lkeen tulkita n‰pp‰inpainallukset ja v‰litt‰‰ tarvittavat viestit
 * pelille. N‰pp‰imistˆkomentoja v‰litet‰‰n kaikille pelin ihmispelaajille. Jos
 * peliss‰ on liikaa ihmispelaajia, konstruktori antaa virheilmoituksen. Myˆhemmin
 * liiasta ihmispelaajien m‰‰r‰st‰ ei en‰‰ huomauteta, joten ihmispelaajia ei
 * tulisi en‰‰ sen j‰lkeen lis‰t‰. Jos niit‰ lis‰t‰‰n, komentoja v‰litet‰‰n
 * vain sille m‰‰r‰lle ihmispelaajia, joita luokka tukee.
 * N‰pp‰imi‰ ei voi muuttaa, ne ovat tietyt vakiot.
 * 
 * @author Jaakko Luttinen
 *
 */
public class KuuntelijaNappainkomennot implements KeyListener {
    
    /**
     * N‰pp‰imistˆll‰ ohjattavien pelaajien maksimim‰‰r‰.
     */
    public static final int NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA = 2;
 
    /**
     * Vakio, joka kertoo, kuinka monta erilaista ohjausn‰pp‰int‰ yhdell‰ pelaajalla on.
     */
    public static final int NAPPAIN_OHJAUSMAARA = 4;
    
    /**
     * Kaasun‰pp‰imen indeksi.
     */
    public static final int NAPPAIN_OHJAUSKAASU = 0;
    
    /**
     * Vasemmalle ohjaavan n‰pp‰imen indeksi.
     */
    public static final int NAPPAIN_OHJAUSVASEMMALLE = 1;
    
    /**
     * Oikealle ohjaavan n‰pp‰imen indeksi.
     */
    public static final int NAPPAIN_OHJAUSOIKEALLE = 2;
    
    /**
     * Liipaisimena toimivan n‰pp‰imen indeksi.
     */
    public static final int NAPPAIN_OHJAUSLIIPAISIN = 3;
    
    /**
     * Peli, johon n‰pp‰inkomentojen aikaansaamat komennot v‰litet‰‰n.
     */
    private Tulkki peli;
    
    /**
     * N‰pp‰inkomennot, joilla ohjauksia suoritetaan. Indeksit lasketaan luokan
     * vakioiden avulla.
     */
    private Nappain[] nappainkomennot;
    
    /**
     * Luo uuden n‰pp‰inkomentojen kuuntelijan. K‰ytt‰j‰n tulee huolehtia siit‰,
     * ett‰ asettaa luodun kuuntelijan jonkun ikkunan (tms.) kuuntelijaksi.
     * @param peli Peli, johon komennot v‰litet‰‰n.
     * @throws Exception Poikkeus kertoo, ett‰ peli‰ ei ole annettu, peliss‰ on
     * liikaa ihmispelaajia tai luokan n‰pp‰inkomentojen rakentelussa on tapahtunut
     * virhe.
     */
    public KuuntelijaNappainkomennot( Tulkki peli ) throws Exception {
       
        if ( peli == null )
            throw new Exception( "N‰pp‰inkomentojen kuuntelijalle ei m‰‰ritetty peli‰." );
        
        this.peli = peli;
        
        int ohjattavienMaara = this.peli.kerroIhmispelaajienMaara();
        
        if ( ohjattavienMaara > KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA ) {
        
            throw new Exception( "N‰pp‰imistˆlll‰ ei voida ohjata maksimissaan kuin " + 
                    			 KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA +
                    			 " pelaajaa." );
            
        }
        
        this.muodostaNappainkomennot();
        
    }
    
    /**
     * N‰pp‰inkomentojen kuuntelijan metodi. Kutsutaan, kun n‰pp‰int‰
     * painetaan. V‰litt‰‰ sen mukaiset ohjauskomennot.
     */
    public void keyPressed( KeyEvent event ) {
        
        this.suoritaNappainviesti( event.getKeyCode(), true );
        
    }
    
    /**
     * N‰pp‰inkomentojen kuuntelijan metodi. Kutsutaan, kun n‰pp‰in
     * vapautetaan. V‰litt‰‰ sen mukaiset ohjauskomennot.
     */
    public void keyReleased( KeyEvent event ) {

        this.suoritaNappainviesti( event.getKeyCode(), false );
        
    }
    
    /**
     * N‰pp‰inkomentojen kuuntelijan metodi. Kutsutaan, kun merkki
     * kirjoitetaan. Ei k‰yttˆ‰ t‰ss‰ luokassa.
     */
    public void keyTyped( KeyEvent event ) {
        
        return;
        
    }
    
    /**
     * Suorittaa sellaisen n‰pp‰intoiminnon, jossa annetun n‰pp‰imen
     * tila on <CODE>pohjassa</CODE>-muuttujan m‰‰r‰‰m‰.
     * @param nappainkoodi N‰pp‰in, jonka tila on annettu.
     * @param pohjassa Totuusarvo kertoo, onko n‰pp‰in pohjassa.
     */
    private void suoritaNappainviesti( int nappainkoodi, boolean pohjassa ) {
        
        int ohjattavienMaara = this.peli.kerroIhmispelaajienMaara();
        
        //V‰litt‰‰ viestin kullekin n‰pp‰imelle ja, jos viesti saa aikaan muutoksen,
        //p‰ivitet‰‰n ohjausta sen mukaan.
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
     * Jos annettua indeksi‰ vastaavan n‰pp‰imen n‰pp‰inkoodi on sama kuin annettu
     * n‰pp‰inkoodi, asettaa sen tilaksi annetun tilan. Jos n‰pp‰imen tila nyt
     * muuttuu, palautetaan false.
     * @param nappainIndeksi N‰pp‰imen indeksi <CODE>nappainkomennot</CODE> taulukossa.
     * @param nappainkoodi Viestin n‰pp‰inkoodi.
     * @param pohjassa Totuusarvo kertoo, onko n‰pp‰inkoodin n‰pp‰in pohjassa.
     * @return Totuusarvo kertoo, vastasiko n‰pp‰inkoodi indeksi‰ vastaavan
     * n‰pp‰imen n‰pp‰inkoodia, ja jos vastasi, muuttuiko t‰m‰n n‰pp‰imen tila, kun sen
     * tilaksi asetettiin annettu tila.
     */
    private boolean viestiNappaimelle( int nappainIndeksi, int nappainkoodi, boolean pohjassa ) {

        //Tutkitaan, onko kyse samasta n‰pp‰imest‰.
        if ( nappainkoodi == this.nappainkomennot[nappainIndeksi].kerroNappainkoodi() ) {
            
            //Tutkitaan, muuttuuko n‰pp‰imen tila.
            if ( this.nappainkomennot[nappainIndeksi].onPohjassa() != pohjassa ) {
                
                //Muutetaan n‰pp‰imen tila ja palautetaan true.
                this.nappainkomennot[nappainIndeksi].asetaTila( pohjassa );
                return true;
                
            }
             
        }
        
        return false;

    }
    
    /**
     * P‰ivitt‰‰ annetun ihmispelaajan ohjauksen.
     * @param ohjattava Ihmispelaajaindeksi.
     */
    private void paivitaOhjaus( int ohjattava ) {
        
        //Suunta suoraan.
        int suunta = 0;
        
        //Pyˆritet‰‰n vasemmalle.
        if ( this.nappainkomennot[this.kerroOhjausVasemmalleIndeksi(ohjattava)].onPohjassa() )
            suunta -= 1;
        
        //Pyˆritet‰‰n oikealle.
        if ( this.nappainkomennot[this.kerroOhjausOikealleIndeksi(ohjattava)].onPohjassa() )
            suunta += 1;
        
        //Korjataan ohjaus.
        this.peli.asetaIhmispelaajanOhjaus( ohjattava, suunta );

    }
    
    /**
     * P‰ivitt‰‰ annetun ihmispelaajan kaasun.
     * @param ohjattava Ihmispelaajaindeksi.
     */
    private void paivitaKaasu( int ohjattava ) {
        
        boolean kaasuPohjassa = this.nappainkomennot[this.kerroOhjausKaasuIndeksi(ohjattava)].onPohjassa();
        
        this.peli.asetaIhmispelaajanKaasu( ohjattava, kaasuPohjassa );
        
    }
    
    /**
     * P‰ivitt‰‰ annetun ihmispelaajan liipaisimen.
     * @param ohjattava Ihmispelaajaindeksi.
     */
    private void paivitaLiipaisin( int ohjattava ) {
        
        boolean liipaisinPohjassa = this.nappainkomennot[this.kerroOhjausLiipaisinIndeksi(ohjattava)].onPohjassa();
        
        this.peli.asetaIhmispelaajanLiipaisin( ohjattava, liipaisinPohjassa );
        
    }
    
    /**
     * Rakentaa n‰pp‰inkomennot.
     * @throws Exception Poikkeus kertoo, ettei n‰pp‰inkomentoa olekaan m‰‰ritetty niin
     * monelle kuin luokan vakio antaa ymm‰rt‰‰.
     */
    private void muodostaNappainkomennot() throws Exception {
        
        this.nappainkomennot = new Nappain[KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA*KuuntelijaNappainkomennot.NAPPAIN_OHJAUSMAARA];
        int ind = 0;
        
        //Jos luokka tukee v‰hint‰‰n yht‰ pelaajaa:
        if ( KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA >= ind + 1 ) {
            
            nappainkomennot[this.kerroOhjausKaasuIndeksi(ind)] = new Nappain( KeyEvent.VK_UP );
            nappainkomennot[this.kerroOhjausVasemmalleIndeksi(ind)] = new Nappain( KeyEvent.VK_LEFT );
            nappainkomennot[this.kerroOhjausOikealleIndeksi(ind)] = new Nappain( KeyEvent.VK_RIGHT );
            nappainkomennot[this.kerroOhjausLiipaisinIndeksi(ind)] = new Nappain( KeyEvent.VK_CONTROL );
            ind++;
            
        }
        
        //Jos luokka tukee v‰hint‰‰n kahta pelaajaa:
        if ( KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA >= ind + 1 ) {
            
            nappainkomennot[this.kerroOhjausKaasuIndeksi(ind)] = new Nappain( KeyEvent.VK_E );
            nappainkomennot[this.kerroOhjausVasemmalleIndeksi(ind)] = new Nappain( KeyEvent.VK_S );
            nappainkomennot[this.kerroOhjausOikealleIndeksi(ind)] = new Nappain( KeyEvent.VK_F );
            nappainkomennot[this.kerroOhjausLiipaisinIndeksi(ind)] = new Nappain( KeyEvent.VK_Q );
            ind++;
            
        }
        
        //Jos luokka v‰itt‰‰ tukevansa useampia pelaajia, heitet‰‰n poikkeus.
        if ( KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA >= ind + 1 )
            throw new Exception( ( ind + 1 ) + " pelaajalle ei ole m‰‰ritelty n‰pp‰inkomentoja." );
       
    }

    /**
     * Kertoo annetun ihmispelaajan kaasu-n‰pp‰imen n‰pp‰inkoodin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Kaasu-n‰pp‰imen n‰pp‰inkoodi.
     */
    public int kerroOhjausKaasuNappainkoodi( int ohjattava ) {
        
        return this.nappainkomennot[this.kerroOhjausKaasuIndeksi(ohjattava)].kerroNappainkoodi();
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan vasemmalle-n‰pp‰imen n‰pp‰inkoodin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Vasemmalle-n‰pp‰imen n‰pp‰inkoodi.
     */
    public int kerroOhjausVasemmalleNappainkoodi( int ohjattava ) {
        
        return this.nappainkomennot[this.kerroOhjausVasemmalleIndeksi(ohjattava)].kerroNappainkoodi();
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan oikealle-n‰pp‰imen n‰pp‰inkoodin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Oikealle-n‰pp‰imen n‰pp‰inkoodi.
     */
    public int kerroOhjausOikealleNappainkoodi( int ohjattava ) {
        
        return this.nappainkomennot[this.kerroOhjausOikealleIndeksi(ohjattava)].kerroNappainkoodi();
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan liipaisin-n‰pp‰imen n‰pp‰inkoodin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Liipaisin-n‰pp‰imen n‰pp‰inkoodi.
     */
    public int kerroOhjausLiipaisinNappainkoodi( int ohjattava ) {
        
        return this.nappainkomennot[this.kerroOhjausLiipaisinIndeksi(ohjattava)].kerroNappainkoodi();
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan kaasu-n‰pp‰imen indeksin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Kaasu-n‰pp‰imen indeksi.
     */
    private int kerroOhjausKaasuIndeksi( int ohjattava ) {
        
        return ohjattava * KuuntelijaNappainkomennot.NAPPAIN_OHJAUSMAARA +
        	   KuuntelijaNappainkomennot.NAPPAIN_OHJAUSKAASU;
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan vasemmalle-n‰pp‰imen indeksin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Vasemmalle-n‰pp‰imen indeksi.
     */
    private int kerroOhjausVasemmalleIndeksi( int ohjattava ) {
        
        return ohjattava * KuuntelijaNappainkomennot.NAPPAIN_OHJAUSMAARA +
        	   KuuntelijaNappainkomennot.NAPPAIN_OHJAUSVASEMMALLE;
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan oikealle-n‰pp‰imen indeksin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Oikealle-n‰pp‰imen indeksi.
     */
    private int kerroOhjausOikealleIndeksi( int ohjattava ) {
        
        return ohjattava * KuuntelijaNappainkomennot.NAPPAIN_OHJAUSMAARA +
        	   KuuntelijaNappainkomennot.NAPPAIN_OHJAUSOIKEALLE;
        
    }
    
    /**
     * Kertoo annetun ihmispelaajan liipaisin-n‰pp‰imen indeksin.
     * @param ohjattava Ihmispelaajaindeksi.
     * @return Liipaisin-n‰pp‰imen indeksi.
     */
    private int kerroOhjausLiipaisinIndeksi( int ohjattava ) {
        
        return ohjattava * KuuntelijaNappainkomennot.NAPPAIN_OHJAUSMAARA +
        	   KuuntelijaNappainkomennot.NAPPAIN_OHJAUSLIIPAISIN;
        
    }

}
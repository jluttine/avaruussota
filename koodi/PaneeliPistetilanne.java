
import javax.swing.*;
import java.awt.*;

/**
 * 
 * Luokka <CODE>PaneeliPistetilanne</CODE> kuvaa paneelia, joka n‰ytt‰‰ er‰‰n
 * <CODE>Tulkki</CODE>-pelin pelaajien pistetilannetta. Pelaajat ovat indeksiens‰
 * mukaan j‰rjestyksess‰ alakkain. Pisteet n‰kyv‰t pelaajan vierell‰. Paneeli n‰ytt‰‰
 * vain korkeintaan niin monen pelaajan pisteit‰, kuin peliss‰ oli pelaajia
 * paneelin luontihetkell‰.
 * 
 * @author Jaakko Luttinen
 *
 */
public class PaneeliPistetilanne extends JPanel implements RajapintaPiirrettava {
    
    /**
     * Peli, jonka pistetilannetta paneeli n‰ytt‰‰.
     */
    private Tulkki peli;
    
    /**
     * Pelaajien pisteet viime p‰ivityksen j‰lkeen.
     */
    private int[] pelaajienPisteet;
    
    /**
     * Tekstit, jotka n‰ytt‰v‰t pelaajien nimet.
     */
    private JLabel[] pelaajanNimiTeksti;
    
    /**
     * Tekstit, jotka n‰ytt‰v‰t pelaajien pisteet.
     */
    private JLabel[] pelaajanPisteetTeksti;
    
    /**
     * Luo uuden paneelin n‰ytt‰m‰‰n annetun pelin pistetilannetta.
     * @param peli Peli, jonka pistetilannetta n‰ytet‰‰n.
     * @throws Exception Poikkeus kertoo, ett‰ peli‰ ei ollut m‰‰ritetty.
     */
    public PaneeliPistetilanne( Tulkki peli ) throws Exception {
        
        if ( peli == null )
            throw new Exception( "Pistetilanne-paneelia ei voi luoda: Peli‰ ei ole m‰‰ritetty." );
        
        this.peli = peli;
        
        //Laitetaan pient‰ reunusta.
        this.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        
        //Pelaajien m‰‰r‰ luontihetkell‰ m‰‰r‰‰ taulukoiden koot.
        int pelaajienMaara = this.peli.kerroPelaajienMaara();
        this.pelaajienPisteet = new int[pelaajienMaara];
        this.pelaajanNimiTeksti = new JLabel[pelaajienMaara];
        this.pelaajanPisteetTeksti = new JLabel[pelaajienMaara];

        //Pistetaulukko paneeli kuntoon...
        GridLayout layout = new GridLayout( pelaajienMaara, 1 );
        layout.setHgap( 5 );
        layout.setVgap( 5 );
        JPanel nimetPaneeli = new JPanel();
        JPanel pisteetPaneeli = new JPanel();
        nimetPaneeli.setLayout( layout );
        pisteetPaneeli.setLayout( layout );

        //Laitetaan nimisarake ja pistesarake vierekk‰in paneelille.
        this.add( nimetPaneeli );
        this.add( pisteetPaneeli );
        
        for ( int ind = 0; ind < pelaajienMaara; ind++ ) {
            
            //Laitetaan nimitekstit nimisarakkeeseen.
            this.pelaajanNimiTeksti[ind] = new JLabel();
            nimetPaneeli.add( this.pelaajanNimiTeksti[ind] );

            //Laitetaan pistetekstit pistesarakkeeseen.
            this.pelaajanPisteetTeksti[ind] = new JLabel();
            pisteetPaneeli.add( this.pelaajanPisteetTeksti[ind] );
            
        }
        
        //P‰ivitet‰‰n paneeli ja pakotetaan piirto.
        this.paivitaPaneeli( true );
        
    }

    public void paivitetaan( boolean paivitetaan ) {
        
        //T‰m‰ ilmoitus ei vaikuta .
        return;
        
    }
    
    public void piirra() {
        
        //P‰ivitet‰‰n paneeli, muttei pakoteta piirtoa.
        this.paivitaPaneeli( false );
        
    }
    
    /**
     * P‰ivitt‰‰ paneelin: Vain jos pistetilanteessa on tapahtnut muutoksia tai piirto
     * pakotetaan, p‰ivitet‰‰n paneelin tekstikent‰t.
     * @param pakotaPiirto Pakotetaanko tekstikenttien p‰ivitys.
     */
    public void paivitaPaneeli( boolean pakotaPiirto ) {
        
        if ( !pakotaPiirto && !this.paivitaPelaajienPisteet() )
            return;
        
        for ( int ind = 0; ind < this.pelaajienPisteet.length; ind++ ) {
            
            this.pelaajanNimiTeksti[ind].setText( this.peli.kerroPelaajanNimi( ind ) );
            this.pelaajanPisteetTeksti[ind].setText( "" + this.pelaajienPisteet[ind] );
            
        }
        
    }
    
    /**
     * P‰ivitt‰‰ pelaajien pistetilanteen yksityiseen kentt‰‰n. Jos pelaajien
     * pistetilanteessa on tapahtunut muutoksia, palautetaan true.
     * @return Totuusarvo kertoo, onko pisteiss‰ tapahtunut muutoksia viime p‰ivityksen
     * j‰lkeen.
     */
    private boolean paivitaPelaajienPisteet() {
        
        boolean onMuutos = false;
        
        for ( int ind = 0; ind < this.peli.kerroPelaajienMaara() && ind < this.pelaajienPisteet.length; ind++ ) {
            
            //Onko pisteet muuttuneet viime kerrasta?
            if ( this.peli.kerroPelaajanPisteet( ind ) != this.pelaajienPisteet[ind] ) {
                
                this.pelaajienPisteet[ind] = this.peli.kerroPelaajanPisteet( ind );
                onMuutos = true;
                
            }
            
        }
        
        return onMuutos;
        
    }
    
}
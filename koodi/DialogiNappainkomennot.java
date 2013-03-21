
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * 
 * Luokka <CODE>DialogiNappainkomennot</CODE> kuvaa modaalista dialogia, joka
 * n‰ytt‰‰ tietyn pelaajan n‰pp‰inkomennot. Dialogille voi asettaa yl‰ikkunan.
 * Dialogi sulkeutuu Takaisin-napista.
 * 
 * 
 * @author Jaakko Luttinen
 *
 */
public class DialogiNappainkomennot extends JDialog implements ActionListener {

    /**
     * Dialogin sulkeva Takaisin-nappi.
     */
    private JButton takaisinNappi = new JButton( "Takaisin" );
    
    /**
     * Luo uuden dialogin.
     * @param ylaikkuna Dialogin yl‰ikkuna.
     * @param nappaintenLukija N‰pp‰intenlukija, jolta n‰pp‰inkomennot kysyt‰‰n.
     * @param indeksi Ihmispelaajaindeksi, jonka n‰pp‰inkomennot n‰ytet‰‰n.
     * @throws Exception Poikkeus kertoo, ett‰ pelaajalle ei ole n‰pp‰inkomentoja.
     */
    public DialogiNappainkomennot( JFrame ylaikkuna, KuuntelijaNappainkomennot nappaintenLukija, int indeksi ) throws Exception {
        
        super( ylaikkuna, "N‰pp‰inkomennot", true );
        
        //Tarkastetaan, onko pelaajalle olemassa n‰pp‰inkomentoja.
        if ( indeksi < 0 || indeksi >= KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA )
            throw new Exception( "Annetulle pelaajalle ei ole n‰pp‰imistˆohjausta." );
        
        JPanel kokoPaneeli = new JPanel();
        kokoPaneeli.setLayout( new BoxLayout( kokoPaneeli, BoxLayout.Y_AXIS ) );
        
        //Laitetaan paneeliin komentopaneeli ja napitpaneeli.
        JPanel komennotPaneeli = new JPanel();
        JPanel napitPaneeli = new JPanel();
        komennotPaneeli.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        napitPaneeli.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        kokoPaneeli.add( komennotPaneeli );
        kokoPaneeli.add( napitPaneeli );
        
        //Komennot paneelin rakentaminen.
        GridLayout layout = new GridLayout( 4, 2 );
        layout.setHgap( 5 );
        layout.setVgap( 5 );
        komennotPaneeli.setLayout( layout );
        komennotPaneeli.add( new JLabel( "Kaasu:" ) );
        komennotPaneeli.add( new JLabel( KeyEvent.getKeyText( nappaintenLukija.kerroOhjausKaasuNappainkoodi( indeksi ) ) ) );
        komennotPaneeli.add( new JLabel( "Vasemmalle:" ) );
        komennotPaneeli.add( new JLabel( KeyEvent.getKeyText( nappaintenLukija.kerroOhjausVasemmalleNappainkoodi( indeksi ) ) ) );
        komennotPaneeli.add( new JLabel( "Oikealle:" ) );
        komennotPaneeli.add( new JLabel( KeyEvent.getKeyText( nappaintenLukija.kerroOhjausOikealleNappainkoodi( indeksi ) ) ) );
        komennotPaneeli.add( new JLabel( "Liipaisin:" ) );
        komennotPaneeli.add( new JLabel( KeyEvent.getKeyText( nappaintenLukija.kerroOhjausLiipaisinNappainkoodi( indeksi ) ) ) );
        
        //Napit paneelin rakentaminen.
        this.takaisinNappi.addActionListener( this );
        napitPaneeli.add( this.takaisinNappi );
        
        //Laitetaan dialogi kuntoon.
        this.setContentPane( kokoPaneeli );
        this.setResizable( false );
        this.pack();
        
        
    }
    
    /**
     * Takaisin-napin painallusta kuunteleva metodi. Sulkee dialogin, kun
     * nappia painetaan.
     */
    public void actionPerformed( ActionEvent event ) {
        
        if ( event.getSource() == this.takaisinNappi ) {
            
            this.dispose();
            
        }
        
    }
    
}
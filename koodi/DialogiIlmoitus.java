
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * Luokka <CODE>DialogiIlmoitus</CODE> kuvaa sellaista dialogia, jolla
 * on jokin otsikko ja viesti. Dialogissa on my�s OK-nappi, jolla se
 * sulkeutuu. Dialogille on mahdollista m��r�t� yl�ikkuna. Dialogi on
 * modaalinen.
 * 
 * @author Jaakko Luttinen
 *
 */
public class DialogiIlmoitus extends JDialog implements ActionListener{
   
    /**
     * Dialogin OK-nappi.
     */
    private JButton okNappi = new JButton( "OK" );
    
    /**
     * Luo uuden modaalisen dialogin viestill� ja OK-napilla varustettuna.
     * @param ylaikkuna Dialogin yl�ikkuna.
     * @param otsikko Otsikko.
     * @param ilmoitus Viesti, joka dialogissa n�ytet��n.
     */
    public DialogiIlmoitus( JFrame ylaikkuna, String otsikko, String ilmoitus ) {
        
        super( ylaikkuna, otsikko, true );

        //Rakennetaan paneeli.
        JPanel paneeli = new JPanel();
        paneeli.setBorder( BorderFactory.createEmptyBorder( 10, 10, 10, 10 ) );
        //Teksti ja napit alakkain.
        GridLayout layout = new GridLayout( 2, 1 );
        layout.setHgap( 10 );
        layout.setVgap( 10 );
        paneeli.setLayout( layout );

        //Kuunnellaan OK-nappia.
        this.okNappi.addActionListener( this );
        
        //Laitetaan teksti ja nappi paneeliin.
        JComponent ilmoitusteksti = new JLabel( ilmoitus );
        paneeli.add( ilmoitusteksti );
        paneeli.add( this.okNappi );
        
        this.setContentPane( paneeli );
        this.setResizable( false );
        this.pack();
        
    }
    
    /**
     * T�ll� metodilla kuunnellaan OK-napin painamista. Suljetaan dialogi, kun
     * OK-nappia painetaan.
     */
    public void actionPerformed( ActionEvent event ) {
        
        if ( event.getSource() == this.okNappi ) {
            
            this.dispose();
            
        }
        
    }
    
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * Luokka <CODE>IkkunaLopputilanne</CODE> kuvaa ikkunaa, joka <CODE>Tulkki</CODE>-pelin
 * loputtua n‰ytt‰‰ pelaajien pistetilanteen. Taulukosta myˆs n‰kee, kuinka monta kertaa
 * joku pelaaja on tappanut toisen pelaajan. Pelaajat ovat pisteiden mukaisessa
 * paremmuusj‰rjestyksess‰. Ikkunan Jatka-nappia painamalla p‰‰see ikkunan yl‰ikkunaan.
 * 
 * @author Jaakko Luttinen
 *
 */
public class IkkunaLopputilanne extends JFrame implements ActionListener {

    /**
     * Nappi, jota painamalla ikkuna sulkeutuu ja yl‰ikkuna avautuu.
     */
    private JButton jatkaNappi = new JButton( "Jatka" );
    
    /**
     * Ikkunan yl‰ikkuna.
     */
    private JFrame ylaikkuna;

    /**
     * Peli, jonka pistetilannetta ikkuna n‰ytt‰‰.
     */
    private Tulkki peli;
    
    /**
     * Luo uuden ikkunan n‰ytt‰m‰‰n annetun pelin lopullisen pistetilanteen.
     * @param ylaikkuna Ikkunan yl‰ikkuna.
     * @param peli Peli, jonka pistetilanne n‰ytet‰‰n.
     * @throws Exception Poikkeus kertoo, ett‰ yl‰ikkunaa tai peli ei m‰‰ritetty.
     */
    public IkkunaLopputilanne( JFrame ylaikkuna, Tulkki peli ) throws Exception {
        
        super( "Lopputilanne" );
        
        if ( ylaikkuna == null )
            throw new Exception( "Lopputilanne-ikkunaa ei voi luoda: Yl‰ikkunaa ei m‰‰ritetty." );
        
        if ( peli == null )
            throw new Exception( "Lopputilanne-ikkunaa ei voi luoda: Peli ei m‰‰ritetty." );
        
        this.ylaikkuna = ylaikkuna;
        this.peli = peli;

        //Laitetaan paneeli kuntoon. Siihen tulee pistetilanne- ja napit-paneelit alakkain.
        JPanel paneeli = new JPanel();
        paneeli.setLayout( new BoxLayout( paneeli, BoxLayout.Y_AXIS ) );
        paneeli.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        JPanel pistetilannePaneeli = new JPanel();
        paneeli.add( pistetilannePaneeli );
        JPanel napitPaneeli = new JPanel();
        paneeli.add( napitPaneeli );

        //Pistetilanne-paneelin rakentaminen.
        //Laitetaan pelaajat j‰rjestykseen.
        int[] pelaajienJarjestys = this.laitaPelaajatJarjestykseen();
        int pelaajienMaara = pelaajienJarjestys.length;
        GridLayout layout = new GridLayout( pelaajienMaara + 1, pelaajienMaara + 3 );
        layout.setHgap( 5 );
        layout.setVgap( 5 );
        pistetilannePaneeli.setLayout( layout );
        pistetilannePaneeli.add( new JLabel( "" ) );
        pistetilannePaneeli.add( new JLabel( "" ) );
        //Yl‰rivi, josta voi katsoa, kuka on tapettu.
        for ( int ind = 0; ind < pelaajienMaara; ind++)
            pistetilannePaneeli.add( new JLabel( this.peli.kerroPelaajanNimi( pelaajienJarjestys[ind] ) ) );
        pistetilannePaneeli.add( new JLabel( "Yhteens‰" ) );
        //Itse taulukko, jonka sarakkeissa pelaajan sijoitus, nimi, tapot ja pisteet.
        for ( int ind = 0; ind < pelaajienMaara; ind++ ) {

            //Tulostetaan sijoitus, jos se ei ole jaettu sijoitus.
            if ( ind == 0 || this.peli.kerroPelaajanPisteet( pelaajienJarjestys[ind] ) != this.peli.kerroPelaajanPisteet( pelaajienJarjestys[ind-1] ) )
                pistetilannePaneeli.add( new Label( ( ind + 1 ) + "." ) );
            else
                pistetilannePaneeli.add( new Label( "" ) );
            
            pistetilannePaneeli.add( new JLabel( this.peli.kerroPelaajanNimi( pelaajienJarjestys[ind] ) ) );
            
            //T‰ss‰ kunkin pelaajan tapot kustakin pelaajasta...
            for ( int jnd = 0; jnd < pelaajienMaara; jnd++ )
                pistetilannePaneeli.add( new JLabel( "" + this.peli.kerroPelaajanTapot( pelaajienJarjestys[ind], pelaajienJarjestys[jnd] ) ) );
            
            pistetilannePaneeli.add( new JLabel( "" + this.peli.kerroPelaajanPisteet( pelaajienJarjestys[ind] ) ) );
            
        }
        
        //Laitetaan napit kuntoon.
        this.jatkaNappi.addActionListener( this );
        napitPaneeli.add( this.jatkaNappi );
        
        //Laitetaan paneeli ikkunaan.
        this.setContentPane( paneeli );
        
        //Laitetaan ikkuna kuntoon.
        this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        this.setResizable( false );
        this.pack();

    }
    
    /**
     * Yksinkertainen sorttausmetodi palauttaa pelin pelaajien indeksit pisteiden
     * mukaisessa paremmuusj‰rjestyksess‰. Sorttausalgoritmi on hyvin yksinkertainen:
     * L‰hdet‰‰n k‰ym‰‰n alkioita j‰rjestyksess‰. Siirret‰‰n alkiota niin kauan eteenp‰in
     * kuin sen arvo on edell‰olevaa suurempi. Sitten siirryt‰‰n seuraavaan alkioon.
     * @return Taulukko pelaajien indekseist‰ paremmuusj‰rjestyksess‰.
     */
    private int[] laitaPelaajatJarjestykseen() {
        
        int[] taulukko = new int[this.peli.kerroPelaajienMaara()];
        
        //Alustetaan taulukko. Laitetaan pelaajat vain indeksien mukaisessa j‰rjestyksess‰.
        for ( int ind = 0; ind < taulukko.length; ind++ )
            taulukko[ind] = ind;
        
        //Tehd‰‰n yksinkertainen sorttaus. K‰yd‰‰n alkiot l‰pi j‰rjestyksess‰.
        for ( int ind = 1; ind < taulukko.length; ind++ ) {
            
            //Siiret‰‰n alkiota niin kauan eteenp‰in kuin sen arvo on edell‰ olevaa suurempi.
            for ( int jnd = ind;
            	  jnd > 0 && this.peli.kerroPelaajanPisteet( taulukko[jnd] ) > this.peli.kerroPelaajanPisteet( taulukko[jnd-1] );
            	  jnd-- ) {
                
                //Siirrossa alkioiden arvot vaihtuvat p‰ikseen.
                int apuluku = taulukko[jnd-1];
                taulukko[jnd-1] = taulukko[jnd];
                taulukko[jnd] = apuluku;
                
            }
            
        }
        
        return taulukko;
        
    }
    
    /**
     * Metodi kuuntelee Jatka-napin painamista ja kun sit‰ painetaan, sulkee ikkunan
     * ja aukaisee yl‰ikkunan.
     * @param event Tapahtumaolio.
     */
    public void actionPerformed( ActionEvent event ) {
        
        if ( event.getSource() == this.jatkaNappi ) {

            //Suljetaan t‰m‰ ikkuna ja avataan yl‰ikkuna.
            this.dispose();
            this.ylaikkuna.setVisible( true );
            
        }

    }
   
}
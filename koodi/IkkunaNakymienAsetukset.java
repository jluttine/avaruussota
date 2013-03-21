
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * Luokka <CODE>IkkunaNakymienAsetukset</CODE> on ikkuna, jossa k‰ytt‰j‰n voi
 * asettaa pelaajille n‰kymi‰ sek‰ tutkia viel‰ ihmispelaajien n‰pp‰inkomentoja.
 * K‰ynnist‰-napista avataan peli-ikkuna ja peli k‰ynnistyy. Takaisin-napista
 * palataan yl‰ikkunaan.
 * 
 * @author Jaakko Luttinen
 *
 */
public class IkkunaNakymienAsetukset extends JFrame implements ActionListener {

    /**
     * K‰ynnist‰-nappi, jolla peli k‰ynnistyy.
     */
    private JButton kaynnistaNappi = new JButton( "K‰ynnist‰" );
    
    /**
     * Takaisin-nappi, jolla palataan yl‰ikkunaan.
     */
    private JButton takaisinNappi = new JButton( "Takaisin" );
    
    /**
     * Napit, joilla voi tutkia ihmispelaajien n‰pp‰inkomentoja.
     */
    private JButton[] nappainkomennotNappi;
    
    /**
     * Checkboxit, joilla pelaajille voi asettaa n‰kymi‰.
     */
    private JCheckBox[] nakymaCheckBoxit;
    
    /**
     * Ikkunan yl‰ikkuna.
     */
    private JFrame ylaikkuna;
    
    /**
     * Ihmispelaajien n‰pp‰inkomentoja kuunteleva olio.
     */
    private KuuntelijaNappainkomennot nappaintenlukija;
    
    /**
     * Peli, jota ikkuna k‰sittelee.
     */
    private Tulkki peli;
    
    /**
     * Luo uuden ikkunan n‰kymien asetuksia varten. 
     * @param ylaikkuna Ikkunan yl‰ikkuna.
     * @param peli Peli, jota k‰sitell‰‰n.
     * @throws Exception Poikkeus kertoo yl‰ikkunaa tai peli‰ ei ole m‰‰ritetty tai
     * pelin asetuksissa on jotain vikaa.
     */
    public IkkunaNakymienAsetukset( JFrame ylaikkuna, Tulkki peli ) throws Exception {
        
        super( "N‰kymien asetukset" );
        
        if ( ylaikkuna == null )
            throw new Exception( "N‰kymien asetukset -ikkunaa ei voi luoda. Yl‰ikkunaa ei m‰‰ritetty." );
        
        if ( peli == null )
            throw new Exception( "N‰kymien asetukset -ikkunaa ei voi luoda. Peli‰ ei m‰‰ritetty." );
        
        this.ylaikkuna = ylaikkuna;
        this.peli = peli;
 
        //Luo uuden olion kuuntelemaaan pelin ihmispelaajien n‰pp‰inkomentoja.
        //Saattaa heitt‰‰ poikkeuksen, jos asetuksissa on jotain vikaa.
        this.nappaintenlukija = new KuuntelijaNappainkomennot( this.peli );

        //Laitetaan koko ikkunan paneeli kuntoon.
        JPanel paneeli = new JPanel();
        paneeli.setLayout( new BoxLayout( paneeli, BoxLayout.Y_AXIS ) );
        paneeli.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        //Siihen tulee tiedot- ja napit-paneelit alakkain.
        JPanel tiedotPaneeli = new JPanel();
        paneeli.add( tiedotPaneeli );
        JPanel napitPaneeli = new JPanel();
        paneeli.add( napitPaneeli );
        
        //Laitetaan napit kuntoon. Kaksi K‰ynnist‰- ja Takaisin-napit vierekk‰in.
        GridLayout layout = new GridLayout( 1, 2 );
        layout.setHgap( 5 );
        layout.setVgap( 5 );
        this.kaynnistaNappi.addActionListener( this );
        this.takaisinNappi.addActionListener( this );
        napitPaneeli.setLayout( layout );
        napitPaneeli.add( this.kaynnistaNappi );
        napitPaneeli.add( this.takaisinNappi );
        
        //Tiedot paneeli kuntoon. Sarakkeissa pelaajan nimi, N‰kym‰-raksitus ja N‰pp‰imet-nappi.
        int pelaajienMaara = this.peli.kerroPelaajienMaara();
        int sarakkeita = 3;
        this.nakymaCheckBoxit = new JCheckBox[pelaajienMaara];
        this.nappainkomennotNappi = new JButton[this.peli.kerroIhmispelaajienMaara()];
        layout = new GridLayout( pelaajienMaara + 1, sarakkeita );
        layout.setHgap( 5 );
        layout.setVgap( 5 );
        tiedotPaneeli.setLayout( layout );
        //Kirjoitetaan otsikot.
        tiedotPaneeli.add( new JLabel( "Pelaaja" ) );
        tiedotPaneeli.add( new JLabel( "N‰kym‰" ) );
        tiedotPaneeli.add( new JLabel( "N‰pp‰imet" ) );
        //T‰m‰ indeksi laskee, monesko ihmispelaaja on kyseess‰ ja asettaa n‰pp‰imet-napin
        //sen mukaan. N‰pp‰inkomentonappihan tulee VAIN ihmispelaajille ja ind laskee
        //kaikkia pelaajia.
        int ihmispelaajaIndeksi = 0;
        for ( int ind = 0; ind < pelaajienMaara; ind++ ) {
            
            //Nimi.
            tiedotPaneeli.add( new JLabel( this.peli.kerroPelaajanNimi( ind ) ) );

            //N‰kym‰ valintaruutu.
            this.nakymaCheckBoxit[ind] = new JCheckBox();
            tiedotPaneeli.add( this.nakymaCheckBoxit[ind] );
            
            if ( this.peli.onIhmispelaaja( ind ) ) {
          
                //N‰pp‰imet-napin indeksi taulukossa kertoo pelaajan ihmispelaajaindeksin.
                this.nappainkomennotNappi[ihmispelaajaIndeksi] = new JButton( "N‰pp‰imet" );
                this.nappainkomennotNappi[ihmispelaajaIndeksi].addActionListener( this );
                tiedotPaneeli.add( this.nappainkomennotNappi[ihmispelaajaIndeksi] );
                ihmispelaajaIndeksi++;
                
            }
            else {
                
                //Tietokonepelaajille ei N‰pp‰imet-nappia laiteta.
                tiedotPaneeli.add( new JLabel( "" ) );
                
            }
            
        }
        
        //Valitaan ja disabloidaan ihmispelaajien n‰kym‰t. Ihmisille halutaan pakottaa
        //n‰kym‰!
        for ( int ind = 0; ind < this.peli.kerroIhmispelaajienMaara(); ind++ ) {
            
            int indeksi = this.peli.kerroIhmispelaajanIndeksi( ind );
            this.nakymaCheckBoxit[indeksi].addActionListener( this );
            this.nakymaCheckBoxit[indeksi].setSelected( true );
            this.nakymaCheckBoxit[indeksi].setEnabled( false );
            
        }
        
        //Laitetaan paneeli ikkunaan.
        this.setContentPane( paneeli );
        
        //Laitetaan ikkuna kuntoon.
        this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        this.setResizable( false );
        this.pack();
        
    }
    
    /**
     * Luo taulukon, joka on juuri oikean kokoinen pit‰m‰‰n sis‰ll‰‰n niiden pelaajien
     * indeksit, joille on valittu n‰kym‰.
     * @return Taulukko niiden pelaajien indekseist‰, joille on asetettu n‰kym‰.
     */
    private int[] kerroNakymienPelaajat() {

        //Luodaan oikean kokoinen taulukko n‰kymien pelaajista.
        int[] nakymienPelaajat = new int[this.kerroNakymienMaara()];
        //Laskuri kertoo, n‰kym‰n indeksin.
        int laskuri = 0;
        
        //K‰yd‰‰n kaikki pelaajat l‰pi.
        for ( int ind = 0; ind < this.nakymaCheckBoxit.length; ind++ )
            if ( this.nakymaCheckBoxit[ind].isSelected() ) 
                nakymienPelaajat[laskuri++] = ind;
                       
        return nakymienPelaajat;
        
    }

    /**
     * Kertoo, kuinka monessa N‰kym‰-checkboxissa on rasti.
     * @return Luku monessako N‰kym‰-checkboxissa on rasti.
     */
    private int kerroNakymienMaara() {
        
        int laskuri = 0;
        
        for ( int ind = 0; ind < this.nakymaCheckBoxit.length; ind++ )
            if ( this.nakymaCheckBoxit[ind].isSelected() )
                laskuri++;
            
        return laskuri;
        
    }
    
    /**
     * Kuuntelee K‰ynnist‰-, Takaisin- ja N‰pp‰inkomennot-nappeja. K‰ynnist‰-napista
     * avaa peli-ikkunan. Takaisin-napista avaa yl‰ikkunan. N‰pp‰inkommennot-napista
     * n‰ytt‰‰ halutun pelaajan n‰pp‰inkomennot dialogissa.
     * @param event Tapahtumaolio.
     */
    public void actionPerformed( ActionEvent event ) {

        try {
            
            if ( event.getSource() == this.kaynnistaNappi ) {

                //Luodaan peli-ikkuna ja laitetaan se n‰ytˆlle. Pidet‰‰n peli pause-tilassa
                //kunnes kaikki on todella valmista ja n‰kyviss‰ (jottei mene sekunnin osia
                //ihmispelaajalta n‰kem‰tt‰).
                this.peli.asetaPelinKaynti( false );

                //Luodaan peli-ikkuna n‰kyville.
                IkkunaPeli ikkuna = new IkkunaPeli( this, this.peli, this.kerroNakymienPelaajat(), this.nappaintenlukija );
                this.dispose();
                ikkuna.setVisible( true );

                //Laitetaan peli taas k‰yntiin ikkunan metodia k‰ytt‰en, jotta se pystyy
                //tekem‰‰n omat toimenpiteens‰ (p‰ivitt‰‰ Pys‰yt‰/Jatka-napin).
                ikkuna.asetaPelinKaynti( true );
                return;
                
            }
            
            if ( event.getSource() == this.takaisinNappi ) {
                
                //Suljetaan ikkuna ja laitetaan yl‰ikkuna n‰ytˆlle.
                this.dispose();
                this.ylaikkuna.setVisible( true );
                return;
                
            }
            
            for ( int ind = 0; ind < this.nappainkomennotNappi.length; ind++ ) {
                
                if ( event.getSource() == this.nappainkomennotNappi[ind] ) {

                    //N‰ytet‰‰n halutun ihmispelaajan n‰pp‰inkomennot.
                    DialogiNappainkomennot nappainIkkuna = new DialogiNappainkomennot( this, this.nappaintenlukija, ind );
                    nappainIkkuna.setVisible( true );
                    
                }
                
                
            }
            
        }
        catch ( Exception virhe ) {
            
            //Ker‰t‰‰n kaikki virheilmoitukset t‰nne ja n‰ytet‰‰n k‰ytt‰j‰lle.
            ( new DialogiIlmoitus( this, "Virhe", virhe.getMessage() ) ).setVisible( true );
            return;
            
        }
    }
   
}
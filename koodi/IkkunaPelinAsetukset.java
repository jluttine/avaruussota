
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * Luokka <CODE>IkkunaPelinAsetukset</CODE> kuvaa ikkunaa, jossa k‰ytt‰j‰ voi tehd‰
 * alustaa <CODE>Tulkki</CODE>-pelin. Ikkunassa k‰ytt‰j‰ voi lis‰t‰/poistaa/muokata
 * pelaajia, valita pelikent‰n sek‰ asettaa voittoon vaadittavan pistem‰‰r‰n.
 * Seuraava-napilla k‰ytt‰j‰ siirtyy pelin asetuksien toiseen vaiheeseen.
 * Lopeta-napilla k‰ytt‰j‰ voi lopettaa koko ohjelman.
 * 
 * Ikkunan paneeli rakentuu seuraavista osista: Pelaajat-paneeli, Pelikent‰n
 * asetukset -paneeli, Pistem‰‰r‰-paneeli ja Napit-paneeli. Pelaajat-paneelissa
 * n‰kyy kaikkien pelin pelaajien tiedot ja niit‰ voi muokata, poistaa tai lis‰t‰.
 * Pelikent‰n asetukset -paneelissa k‰ytt‰j‰ joko valitsee valmiin pelikent‰n tai
 * asettaa generoitavan kent‰n mitat. Pistem‰‰r‰-paneelissa k‰ytt‰j‰ asettaa voittoon
 * vaadittavan pistem‰‰r‰n. Napit-paneelissa on napit, joilla k‰ytt‰j‰ voi joko lopettaa
 * koko ohjelman tai jatkaa eteenp‰in.
 * 
 * @author Jaakko Luttinen
 *
 */
public class IkkunaPelinAsetukset extends JFrame implements ActionListener {
    
    /**
     * Nappi, jolla k‰ytt‰j‰ voi lis‰t‰ peliin uuden pelaajan.
     */
    private JButton lisaaPelaajaNappi = new JButton( "Lis‰‰ pelaaja" );
    
    /**
     * Nappi, jolla ikkuna sulkeutuu ja N‰kymienAsetukset-ikkuna avautuu.
     */
    private JButton seuraavaNappi = new JButton( "Seuraava" );
    
    /**
     * Nappi, jolla koko ohjelma loppuu.
     */
    private JButton lopetaNappi = new JButton( "Lopeta" );
    
    /**
     * Paneeli, jossa pelin pelaajien tiedot n‰ytet‰‰n.
     */
    private JPanel pelaajatiedotPaneeli = new JPanel();
    
    /**
     * Paneeli, jossa pelikent‰n asetukset n‰ytet‰‰n.
     */
    private PaneeliPelikentanAsetukset pelikentanAsetuksetPaneeli;
    
    /**
     * Tekstisyˆttˆ, jolla k‰ytt‰j‰ voi syˆtt‰‰ voittoon vaadittavan pistem‰‰r‰n.
     */
    private JTextField pistemaaraSyotto = new JTextField( "20" );
    
    /**
     * Jokaiseen pelin pelaajaan liittyv‰ Muokkaa-nappi.
     */
    private JButton[] muokkaaNapit;
    
    /**
     * Jokaiseen pelin pelaajaan liittyv‰ Poista-nappi.
     */
    private JButton[] poistaNapit;
 
    /**
     * Peli, jota ikkuna k‰sittelee.
     */
    private Tulkki peli;
    
    /**
     * Luo uuden ikkunan, joka luo uuden <CODE>Tulkki</CODE>-pelin ja tarjoaa
     * k‰ytt‰j‰lle mahdollisuudet muokata pelin asetuksia.
     * @throws Exception Poikkeus kertoo, ett‰ pelin malleja ei onnistuttu lataamaan.
     */
    public IkkunaPelinAsetukset() throws Exception {
        
        super( "Pelin asetukset" );
        
        //T‰ss‰ saattaa tulla poikkeus, jos mallien lataus ep‰onnistuu.
        this.peli = new Tulkki();
        
        //Laitetaan alussa pari pelaajaa, jottei peli ole ihan tyhj‰.
        //Ei haittaa jos ei onnistukaan...
        try {
            
            this.peli.lisaaPelaaja( "Pelaaja1", -1, 0 );
            this.peli.lisaaPelaaja( "Pelaaja2", 0, 0 );
            
        }
        catch ( Exception virhe ) { }

        //Ikkunan paneeliin tulee nelj‰ osa-paneelia alakkain: Pelaajat-paneeli,
        //Pelikent‰nAsetukset-paneeli, Pistem‰‰r‰-paneeli ja Napit-Paneeli.
        JPanel paneeli = new JPanel();
        paneeli.setLayout( new BoxLayout( paneeli, BoxLayout.Y_AXIS ) );
        paneeli.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        //Osapaneelit. Pelikent‰nAsetukset-paneeli rakentaa itse itsens‰.
        JPanel pelaajatPaneeli = new JPanel();
        this.pelikentanAsetuksetPaneeli = new PaneeliPelikentanAsetukset( this.peli );
        JPanel pistemaaraPaneeli = new JPanel();
        JPanel napitPaneeli = new JPanel();
        //Laitetaan osapaneelit koko paneeliin.
        paneeli.add( pelaajatPaneeli );
        paneeli.add( this.pelikentanAsetuksetPaneeli );
        paneeli.add( pistemaaraPaneeli );
        paneeli.add( napitPaneeli );
        
        //Pelaajat-paneeli kuntoon. N‰ytt‰‰ pelajien tiedot taulukossa.
        pelaajatPaneeli.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        pelaajatPaneeli.setLayout( new BoxLayout( pelaajatPaneeli, BoxLayout.Y_AXIS ) );
        this.paivitaPelaajatiedotPaneeli();
        pelaajatPaneeli.add( this.pelaajatiedotPaneeli );
        pelaajatPaneeli.add( this.lisaaPelaajaNappi );
        this.lisaaPelaajaNappi.addActionListener( this );

        //Pistem‰‰r‰-paneeli kuntoon.
        pistemaaraPaneeli.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        this.pistemaaraSyotto.setColumns( 2 );
        pistemaaraPaneeli.add( new JLabel( "Voiton pistem‰‰r‰" ) );
        pistemaaraPaneeli.add( this.pistemaaraSyotto );
        
        //Napit-paneeli kuntoon. Seuraava- ja Lopeta-napit vierekk‰in.
        napitPaneeli.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        this.seuraavaNappi.addActionListener( this );
        this.lopetaNappi.addActionListener( this );
        napitPaneeli.setLayout( new BoxLayout( napitPaneeli, BoxLayout.X_AXIS ) );
        napitPaneeli.add( this.seuraavaNappi );
        napitPaneeli.add( this.lopetaNappi );
        
        //Laitetaan ikkuna kuntoon.
        this.setContentPane( paneeli );
        this.setResizable( false );
        this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );//JFrame.EXIT_ON_CLOSE );
        this.pack();

    }
    
    /**
     * Tyhjent‰‰ Pelaajatiedot-paneelin aiemman sis‰llˆn ja rakentaa paneelin
     * uudelleen. Pelaajatiedot-paneelin sis‰ltˆ on otsikoista ja niit‰
     * vastaavien pelaajien tietojen muodostamasta taulukosta.
     */
    public void paivitaPelaajatiedotPaneeli() {

        //Jokaisella pelaajalla oma rivins‰.
        int riveja = this.peli.kerroPelaajienMaara();
        //Sarakkeet: Nimi, teko‰ly, alus, Muokkaa-nappi, Poista-nappi.
        int sarakkeita = 5;

        this.muokkaaNapit = new JButton[riveja];
        this.poistaNapit = new JButton[riveja];

        //Poistetaan kaikki vanhat komponentit! P‰ivityksess‰ ei haluta laittaa lis‰‰
        //vaan korvata vanha tieto uudella.
        this.pelaajatiedotPaneeli.removeAll();
        
        //Paneelin ulkon‰kˆ kuntoon.
        this.pelaajatiedotPaneeli.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        //Rivim‰‰r‰ss‰ otetaan otsikkorivi huomioon.
        GridLayout layout = new GridLayout( riveja + 1, sarakkeita );
        layout.setHgap( 5 );
        layout.setVgap( 5 );
        this.pelaajatiedotPaneeli.setLayout( layout );
    
        //Taulukon otsikkorivi.
        this.pelaajatiedotPaneeli.add( new JLabel( "Nimi" ) );
        this.pelaajatiedotPaneeli.add( new JLabel( "Ohjaus" ) );
        this.pelaajatiedotPaneeli.add( new JLabel( "Alus" ) );
        this.pelaajatiedotPaneeli.add( new JLabel( "Muuta" ) );
        this.pelaajatiedotPaneeli.add( new JLabel( "Poista" ) );
        
        //Laitetaan jokaisen pelaajan tiedot omalle rivilleen.
        for ( int ind = 0; ind < riveja; ind++ ) {

            //1. sarake: Nimi.
            this.pelaajatiedotPaneeli.add( new JLabel( this.peli.kerroPelaajanNimi( ind ) ) );

            //2. sarake: Teko‰ly.
            int tekoalyIndeksi = this.peli.kerroPelaajanTekoaly( ind );
            String tekoaly = "Ihminen";
            if ( tekoalyIndeksi != -1 )
                tekoaly = this.peli.kerroTekoaly( tekoalyIndeksi );
            this.pelaajatiedotPaneeli.add( new JLabel( tekoaly ) );
            
            //3. sarake: Alus.
            this.pelaajatiedotPaneeli.add( new JLabel( this.peli.kerroAlus( this.peli.kerroPelaajanAlus( ind ) ) ) );
 
            //4. sarake: Muokkaa-nappi.
            this.muokkaaNapit[ind] = new JButton( "Muokkaa" );
            this.muokkaaNapit[ind].addActionListener( this );
            this.pelaajatiedotPaneeli.add( this.muokkaaNapit[ind] );
            
            //5. sarake: Poista-nappi.
            this.poistaNapit[ind] = new JButton( "Poista" );
            this.poistaNapit[ind].addActionListener( this );
            this.pelaajatiedotPaneeli.add( this.poistaNapit[ind] );
            
        }
               
    }

    /**
     * Asettaa ikkunan n‰kyvyyden. Jos ikkuna asetetaan n‰kyviin, p‰ivitet‰‰n
     * samalla Pelaajatiedot-paneeli.
     * @param visible Totuusarvo kertoo, asetetaanko ikkuna n‰kyviin.
     */
    public void setVisible( boolean visible ) {
        
        if ( visible == true ) {
            
            this.paivitaPelaajatiedotPaneeli();
            this.pack();
            
        }
        
        super.setVisible( visible );
        
    }

    /**
     * Yritt‰‰ asettaa pelin voittoon vaadittavan pistem‰‰r‰n k‰ytt‰j‰n
     * antaman syˆtteen perusteella.
     * @throws Exception Poikkeus kertoo, ett‰ k‰ytt‰j‰n syˆte ei ollut
     * kokonaisluku tai pistem‰‰r‰ on virheellinen. 
     */
    private void asetaVoitonPistemaara() throws Exception {
        
        try {
            
            String teksti = this.pistemaaraSyotto.getText();
            int pistemaara = Integer.parseInt( teksti );

            this.peli.asetaVoitonPistemaara( pistemaara );
            
        }
        catch ( NumberFormatException virhe ) {
            
            throw new Exception( "Pistem‰‰r‰n t‰ytyy olla kokonaisluku." );
            
        }
        
    }

    /**
     * Kuuntelee Muokkaa-, Poista-, Lis‰‰ pelaaja-, Seuraava-, ja Lopeta-nappeja.
     * Muokkaa-napista avaa Muokkaa pelaajaa -ikkunan. Poista-napista poistaa napin
     * indeksi‰ vastaavan pelaajan. Lis‰‰ pelaaja-napista yritt‰ aukaista Lis‰‰ pelaaja-ikkunan.
     * Seuraava-napista yritt‰‰ asettaa peliin k‰ytt‰j‰n tekem‰t asetukset pelikent‰n
     * ja pistem‰‰r‰n suhteen; sitten sulkee t‰m‰n ikkunan ja aukaisee N‰kymien asetukset
     * -ikkunan. Lopeta-napista sulkee ikkunan.
     * @param event Tapahtumaolio.
     */
    public void actionPerformed( ActionEvent event ) {

        //Yritet‰‰n tehd‰ haluttu toimenpide, mutta virheiden sattuessa annetaan
        //siit‰ k‰ytt‰j‰lle ilmoitus.
        try {
            
            if ( this.muokkaaNapit != null ) {
                
                for ( int ind = 0; ind < this.muokkaaNapit.length; ind++ ) {
                    
                    if ( event.getSource() == this.muokkaaNapit[ind] ) {
                        
                        //Avataan ikkuna muokkaamaan pelaajan tietoja.
                        IkkunaLisaaPelaaja muokkausIkkuna = new IkkunaLisaaPelaaja( this, this.peli, ind ); 
                        this.dispose();
                        muokkausIkkuna.setVisible( true );
                        return;
                        
                    }
                    
                }
                
            }
            
            if ( this.poistaNapit != null ) {
                
                for ( int ind = 0; ind < this.poistaNapit.length; ind++ ) {
                    
                    if ( event.getSource() == this.poistaNapit[ind] ) {
                        
                        //Poistetaan napin indeksi‰ vastaava pelaaja.
                        this.peli.poistaPelaaja( ind );
                        //P‰ivitet‰‰n pelaajatiedot-paneeli.
                        this.paivitaPelaajatiedotPaneeli();
                        this.pack();
                        return;
                        
                    }
                    
                }
                
            }
            
            if ( event.getSource() == this.lisaaPelaajaNappi ) {
                
                //Yritt‰‰ avata ikkunan, jossa k‰ytt‰j‰ voi asettaa uuden pelaajan tiedot.
                IkkunaLisaaPelaaja lisaysIkkuna = new IkkunaLisaaPelaaja( this, this.peli );
                this.dispose();
                lisaysIkkuna.setVisible( true );
                return;
                
            }
            
            if ( event.getSource() == this.seuraavaNappi ) {
                
                //Yritet‰‰n asettaa k‰ytt‰j‰n tekem‰t asetukset peliin.
                this.pelikentanAsetuksetPaneeli.asetaPelikentta();
                this.asetaVoitonPistemaara();
            
                //Heitet‰‰n mahdollisista virheellisist‰ asetuksista poikkeus.
                this.peli.tutkiPelinKaynnistettavyys();
                
                //Avataan N‰kymien asetukset -ikkuna seuraavia asetuksia varten.
                IkkunaNakymienAsetukset uusiIkkuna = new IkkunaNakymienAsetukset( this, this.peli );
                this.dispose();
                uusiIkkuna.setVisible( true );
                return;
                
            }
            
            if ( event.getSource() == this.lopetaNappi ) {
                
                //Tyhjent‰‰ pelin ja sulkee t‰m‰n ikkunan.
                this.peli.tyhjennaPeli();
                this.dispose();
                return;
                
            }
        
        }
        catch ( Exception virhe ) {
            
            //N‰ytet‰‰n virheilmoitus k‰ytt‰j‰lle.
            ( new DialogiIlmoitus( this, "Virheelliset asetukset", virhe.getMessage() ) ).setVisible( true );
            return;
            
        }


    }

    /**
     * Ohjelman k‰ynnist‰v‰ metodi. Luo uuden Pelin asetukset -ikkunan ja laittaa
     * sen k‰ytt‰j‰lle n‰kyviin. Mahdolliset virheilmoitukset n‰ytet‰‰n k‰ytt‰j‰lle
     * dialogissa.
     * @param komentoriviparametrit Komentoriviparametrit, joita metodi ei k‰yt‰.
     */
    public static void main( String[] komentoriviparametrit ) {

        try {
            
            IkkunaPelinAsetukset ikkuna = new IkkunaPelinAsetukset();
            ikkuna.setVisible( true );
            
        }
        catch ( Exception virhe ) {

            //N‰ytet‰‰n virheilmoitus.
            ( new DialogiIlmoitus( null, "Virhe", virhe.getMessage() ) ).setVisible( true );
            //Lopetetaan ohjelma virhearvolla.
            System.exit( 1 );
            
        }
        
    }
  
}

import javax.swing.*;
//import java.awt.*;
import java.awt.event.*;

/**
 * 
 * Luokka <CODE>IkkunaLisaaPelaaja</CODE> kuvaa ikkunaa, jossa on mahdollista joko
 * luoda uusi pelaaja tai muokata vanhaa pelaajaa er‰‰ss‰ <CODE>Tulkki</CODE>-peliss‰.
 * Lis‰ys tai muokkaus valitaan kutsumalla eri konstruktoria. K‰ytt‰j‰ asettaa
 * ikkunassa pelaajan nimen, aluksen ja teko‰lyn. Peruuta-napilla k‰ytt‰j‰ voi
 * lopettaa lis‰yksen/muokkauksen ja palata yl‰ikkunaan. OK-napilla lis‰ys/muutos
 * yritet‰‰n suorittaa peliin. K‰ytt‰j‰lle ilmoitetaan, jos asetuksissa oli
 * jotain vikaa, eik‰ pelaajan lis‰ys onnistunut. OK-napilla ikkunan sulkeminen
 * onnistuu vain, jos kaikki asetukset ovat kunnossa ja lis‰ys/muutos saadaan
 * onnistuneesti tehty‰ peliin.
 * 
 * @author Jaakko Luttinen
 *
 */
public class IkkunaLisaaPelaaja extends JFrame implements ActionListener {
    
    /**
     * Ikkunan OK-nappi.
     */
    private JButton okNappi = new JButton( "OK" );
    
    /**
     * Ikkunan Peruuta-nappi.
     */
    private JButton peruutaNappi = new JButton( "Peruuta" );
    
    /**
     * Nimensyˆttˆ tekstikentt‰.
     */
    private JTextField nimiSyotto;
    
    /**
     * Radionapit teko‰lyn valintaan.
     */
    private JRadioButton[] ohjausRadiot;
    
    /**
     * Radionapit aluksen valintaan.
     */
    private JRadioButton[] alusRadiot;
    
    /**
     * Ikkunan yl‰ikkuna.
     */
    private JFrame ylaikkuna;
    
    /**
     * Muokattavan pelaajan indeksi tai -1, jos pelaajaa lis‰t‰‰n.
     */
    private int muokattavaPelaaja;
    
    /**
     * Peli, jonka pelaajia k‰sitell‰‰n.
     */
    private Tulkki peli;
    
    /**
     * Luo uuden pelaajan lis‰ys ikkunan.
     * @param ylaikkuna Ikkunan yl‰ikkuna.
     * @param peli Peli, johon pelaaja lis‰t‰‰n.
     * @throws Exception Poikkeus kertoo, ett‰ peli‰ tai yl‰ikkunaa ei ole m‰‰ritetty tai
     * peliss‰ on jo maksimim‰‰r‰ pelaajia.
     */
    public IkkunaLisaaPelaaja( JFrame ylaikkuna, Tulkki peli ) throws Exception {
        
        super( "Lis‰‰ pelaaja" );
        
        if ( ylaikkuna == null )
            throw new Exception( "Lis‰‰ pelaaja -ikkunaa ei voi luoda: Yl‰ikkunaa ei m‰‰ritetty." );
        
        if ( peli == null )
            throw new Exception( "Lis‰‰ pelaaja -ikkunaa ei voi luoda: Peli‰ ei ole m‰‰ritetty." );
        
        if ( peli.kerroPelaajienMaara() >= peli.kerroPelaajienMaksimimaara() ) {
            
            throw new Exception( "Peliin ei voi lis‰t‰ uutta pelaajaa: Peliss‰ on jo maksimim‰‰r‰ (" +
                    			 peli.kerroPelaajienMaksimimaara() + ") pelaajia." );
            
        }
        
        this.ylaikkuna = ylaikkuna;
        this.peli = peli;
        
        //Arvo -1 tarkoittaa, ett‰ pelaajaa lis‰t‰‰n.
        this.muokattavaPelaaja = -1;
        
        //Heitet‰‰n nimikentt‰‰n valmiiksi joku nimi.
        this.nimiSyotto = new JTextField( "Pelaaja" + ( this.peli.kerroPelaajienMaara() + 1 ) ); 
        this.nimiSyotto.setColumns( 12 );

        //Rakennetaan ikkuna.
        this.alusta();

    }
    
    /**
     * Luo uuden ikkunan pelaajan muokkaukseen.
     * @param ylaikkuna Ikkunan yl‰ikkuna.
     * @param peli Peli, jonka pelaajaa muokataan.
     * @param muokattavaPelaaja Muokattavan pelaajan indeksi.
     * @throws Exception Poikkeus kertoo, ettei peli‰ tai yl‰ikkunaa ole m‰‰ritetty tai
     * annettu pelaajan indeksi on virheellinen.
     */
    public IkkunaLisaaPelaaja( JFrame ylaikkuna, Tulkki peli, int muokattavaPelaaja ) throws Exception {
        
        super( "Muokkaa pelaajaa" );
        
        if ( ylaikkuna == null )
            throw new Exception( "Muokkaa pelaajaa -ikkunaa ei voi luoda: Yl‰ikkunaa ei m‰‰ritetty." );
        
        if ( peli == null )
            throw new Exception( "Muokkaa pelaajaa -ikkunaa ei voi luoda: Peli‰ ei ole m‰‰ritetty." );
        
        if ( muokattavaPelaaja < 0 || muokattavaPelaaja >= peli.kerroPelaajienMaara() )
            throw new Exception( "Muokkaa pelaajaa -ikkunaa ei voi luoda: Pelaajan indeksi virheellinen." );
        
        this.ylaikkuna = ylaikkuna;
        this.peli = peli;
        this.muokattavaPelaaja = muokattavaPelaaja;
        
        //Laitetaan nimikentt‰‰n pelaajan nimi.
        this.nimiSyotto = new JTextField( this.peli.kerroPelaajanNimi( muokattavaPelaaja ) );
        this.nimiSyotto.setColumns( 12 );
        
        //Rakennetaan ikkuna.
        this.alusta();
        
        //Asetetaan radionapit vastaamaan pelaajan asetuksia.
        int indeksi = this.peli.kerroPelaajanAlus( muokattavaPelaaja );
        if ( indeksi != -1 ) //Tarkastetaan, onko pelaajalla alusta.
            this.alusRadiot[indeksi].setSelected( true );
        
        this.ohjausRadiot[this.peli.kerroPelaajanTekoaly( muokattavaPelaaja )+1].setSelected( true );
        
    }
    
    /**
     * Rakentaa ikkunan eli laittaa paneelit paikoilleen ja objektit paneeleihin.
     */
    private void alusta() {
        
        //Paneeli on koko ikkunan paneeli.
        JPanel paneeli = new JPanel();
        paneeli.setLayout( new BoxLayout( paneeli, BoxLayout.Y_AXIS ) );
        paneeli.setBorder( BorderFactory.createEmptyBorder( 20, 20, 20, 20 ) );
        
        //Paneelit sek‰ nimelle, tiedoille (alus, teko‰ly) ja napeille.
        JPanel nimiPaneeli = new JPanel();
        JPanel tiedotPaneeli = new JPanel();
        JPanel napitPaneeli = new JPanel();
        paneeli.add( nimiPaneeli );
        paneeli.add( tiedotPaneeli );
        paneeli.add( napitPaneeli );
        
        //Nimipaneeli kuntoon.
        nimiPaneeli.add( new JLabel( "Nimi:" ) );
        nimiPaneeli.add( this.nimiSyotto );
        
        //Tiedot-paneeli kuntoon. Se koostuu vierekk‰isist‰ ohjaus- ja alus-paneeleista.
        JPanel ohjausPaneeli = new JPanel();
        JPanel alusPaneeli = new JPanel();
        ohjausPaneeli.setLayout( new BoxLayout( ohjausPaneeli, BoxLayout.Y_AXIS ) );
        alusPaneeli.setLayout( new BoxLayout( alusPaneeli, BoxLayout.Y_AXIS ) );
        tiedotPaneeli.add( ohjausPaneeli );
        tiedotPaneeli.add( alusPaneeli );
        
        //Tiedot-paneelin Ohjaus-paneeli. Otsikko ja radionapit vaan alakkain.
        ohjausPaneeli.add( new JLabel( "Ohjaus" ) );
        ButtonGroup ohjausRadioRyhma = new ButtonGroup();
        this.ohjausRadiot = new JRadioButton[this.peli.kerroTekoalyjenMaara()+1];
        this.ohjausRadiot[0] = new JRadioButton( "Ihminen" );
        ohjausPaneeli.add( this.ohjausRadiot[0] );
        ohjausRadioRyhma.add( this.ohjausRadiot[0] );
        //Radionappien indeksit ovat yhden liian isoja, koska indeksill‰ 0 on Ihminen.
        for ( int ind = 0; ind < this.ohjausRadiot.length - 1; ind++ ) {
            
            this.ohjausRadiot[ind+1] = new JRadioButton( this.peli.kerroTekoaly( ind ) );
            ohjausRadioRyhma.add( this.ohjausRadiot[ind+1] );
            ohjausPaneeli.add( this.ohjausRadiot[ind+1] );
            
        }
        
        //Tiedot-paneelin Alus-paneeli. Otsikko ja radionapit vaan alakkain.
        alusPaneeli.add( new JLabel( "Alus" ) );
        ButtonGroup alusRadioRyhma = new ButtonGroup();
        this.alusRadiot = new JRadioButton[this.peli.kerroAlustenMaara()];
        for ( int ind = 0; ind < this.alusRadiot.length; ind++ ) {
            
            this.alusRadiot[ind] = new JRadioButton( this.peli.kerroAlus( ind ) );
            alusRadioRyhma.add( this.alusRadiot[ind] );
            alusPaneeli.add( this.alusRadiot[ind] );
            
        }
        
        //Laitetaan napit kuntoon.
        this.okNappi.addActionListener( this );
        this.peruutaNappi.addActionListener( this );
        napitPaneeli.add( this.okNappi );
        napitPaneeli.add( this.peruutaNappi );
        
        //Laitetaan paneeli ikkunaan.
        this.setContentPane( paneeli );
        
        //Laitetaan ikkuna kuntoon.
        this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        this.setResizable( false );
        this.pack();
       
        
    }
    
    /**
     * Sulkee ikkunan ja tuhoaa taulukot.
     */
    public void sulje() {
        
        this.ohjausRadiot = null;
        this.alusRadiot = null;
        this.nimiSyotto = null;
        this.dispose();

    }
    
    /**
     * Kertoo annetun radionappitaulukon valitun napin indeksin tai -1, jos yksik‰‰n
     * ei ole valittu.
     * @param radionapit Radionappitaulukko.
     * @return Valitun napin indeksi tai -1.
     */
    private int kerroValinta( JRadioButton[] radionapit ) {
        
        if ( radionapit != null ) {
            
            for ( int ind = 0; ind < radionapit.length; ind++ ) {
                
                if ( radionapit[ind].isSelected() )
                    return ind;
                
            }
        
        }
        
        return -1;
        
    }

    /**
     * Kuuntelee OK-ja Peruuta-nappeja. OK-nappia painettaessa yritt‰‰ suorittaa
     * lis‰yksen/muokkauksen ja antaa mahdollisista virheist‰ ilmoituksen k‰ytt‰j‰lle.
     * @param event Tapahtumaolio.
     */
    public void actionPerformed( ActionEvent event ) {
        
        if ( event.getSource() == this.okNappi ) {
            
            int alusIndeksi = this.kerroValinta( this.alusRadiot );
            int ohjausIndeksi = this.kerroValinta( this.ohjausRadiot ) - 1;
            
            //Yritet‰‰n lis‰t‰/muokata pelaajaa. N‰ytet‰‰n syntyv‰t virheilmoitukset k‰ytt‰j‰lle.
            try {
                
                //Ei alusta valittuna.
                if ( alusIndeksi == -1 )
                    throw new Exception( "Valitse alus." );
                
                //Ei teko‰ly‰ valittuna.
                if ( ohjausIndeksi == -2 )
                    throw new Exception( "Aseta ohjaus." );
                
                //Jos peli on t‰ynn‰ ihmisi‰ ja yritet‰‰n joko luoda uutta ihmispelaajaa tai
                //VAIHTAA pelaaja ihmispelaajaksi, heitet‰‰n poikkeus.
                int ihmismax = KuuntelijaNappainkomennot.NAPPAIMISTOOHJATTAVIEN_MAKSIMIMAARA;
                boolean yrittaaTayteen = ( ohjausIndeksi == -1 ) && ( this.peli.kerroIhmispelaajienMaara() >= ihmismax ); 
                if ( yrittaaTayteen &&
                     ( ( this.muokattavaPelaaja == -1 ) ||
                       ( this.muokattavaPelaaja != -1 && this.peli.kerroPelaajanTekoaly( this.muokattavaPelaaja ) != -1 ) ) ) {
                    
                    throw new Exception( "Peliin ei voi lis‰t‰ ihmispelaaja: Peliss‰ on jo maksimim‰‰r‰ (" +
                            			 ihmismax + ") ihmispelaajia." );
                    
                }

                if ( this.muokattavaPelaaja == -1 ) {
                    
                    //Annettuja tietoja vastaava pelaaja lis‰t‰‰n peliin.
                    this.peli.lisaaPelaaja( this.nimiSyotto.getText(), ohjausIndeksi, alusIndeksi );
                    
                }
                else {

                    //Muokataan haluttua pelaajaa.
                    this.peli.asetaPelaajanNimi( this.muokattavaPelaaja, this.nimiSyotto.getText() );
                    this.peli.asetaPelaajanTekoaly( this.muokattavaPelaaja, ohjausIndeksi );
                    this.peli.asetaPelaajanAlus( this.muokattavaPelaaja, alusIndeksi );
                    
                    
                }
                
            }
            catch ( Exception virhe ) {

                //Virheist‰ annetaan k‰ytt‰j‰lle ilmoitus.
                ( new DialogiIlmoitus( this, "Seis", virhe.getMessage() ) ).setVisible( true );
                return;
                
            }

            this.sulje();
            this.ylaikkuna.setVisible( true );
            
        }

        if ( event.getSource() == this.peruutaNappi ) {

            //Uuutta pelaaja ei luoda eik‰ muutoksia tehd‰.
            this.sulje();
            this.ylaikkuna.setVisible( true );
            
        }

    }
   
}
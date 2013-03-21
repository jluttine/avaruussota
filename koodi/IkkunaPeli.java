
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.*;

/**
 * 
 * Luokka <CODE>IkkunaPeli</CODE> kuvaa ikkunaa, jossa <CODE>Tulkki</CODE>-peli pyˆrii
 * ja toimii. Ikkuna rakentuu n‰kym‰osasta ja marginaaliosasta. N‰kym‰osassa n‰ytet‰‰n
 * haluttujen pelaajien n‰kym‰t. Sen vieress‰ on marginaaliosa, jossa n‰kyy pelaajien
 * pistetilanne sek‰ kaksi nappia: Pys‰yt‰/Jatka- ja Lopeta-napit. Pys‰yt‰/Jatka-napilla
 * voi pelin kulun pys‰ytt‰‰ ja taas jatkaa. Lopeta-napilla peli p‰‰ttyy ja luodaan
 * ikkuna n‰ytt‰m‰‰n pelin lopputilannetta. 
 * 
 * @author Jaakko Luttinen
 *
 */
public class IkkunaPeli extends JFrame implements ActionListener {

    /**
     * Ikkunan yl‰ikkuna.
     */
    private JFrame ylaikkuna;
    
    /**
     * Pys‰yt‰/Jatka-nappi, jolla k‰ytt‰j‰ voi pys‰ytt‰‰ ja taas jatkaa peli‰.
     */
    private JButton pysaytaNappi = new JButton( "Pys‰yt‰" );
    
    /**
     * Lopeta-nappi, jolla k‰ytt‰j‰ voi lopettaa pelin ja siirty‰ katsomaan lopputilannetta.
     */
    private JButton lopetaNappi = new JButton( "Lopeta" );
    
    /**
     * Olio, joka huolehtii n‰ytˆnp‰ivityksest‰.
     */
    private SaieNaytonpaivitys naytonpaivitysSaie;
    
    /**
     * Peli, jota ikkuna k‰sittelee.
     */
    private Tulkki peli;

    /**
     * Luo uuden ikkunan pyˆritt‰m‰‰n peli‰. Laittaa pelin k‰yntiin.
     * @param ylaikkuna Ikkunan yl‰ikkuna.
     * @param peli Peli, jota ikkuna k‰sittelee.
     * @param nakymienPelaajat Piirrett‰vien n‰kymien pelaajat.
     * @param nappaintenlukija Olio, joka huolehtii k‰ytt‰j‰n n‰pp‰insyˆtteest‰.
     * @throws Exception Poikkeus kertoo, ett‰ jokin parametreista on null tai
     * asetukset ovat muuten virheelliset.
     */
	public IkkunaPeli( JFrame ylaikkuna, Tulkki peli, int[] nakymienPelaajat, KuuntelijaNappainkomennot nappaintenlukija ) throws Exception {
	    
	    super( "AVARUUSSOTA" );
	    
	    if ( ylaikkuna == null )
	        throw new Exception( "Peli-ikkunaa ei voi luoda: Yl‰ikkunaa ei m‰‰ritetty." );
	    
	    if ( peli == null )
	        throw new Exception( "Peli-ikkunaa ei voi luoda: Peli‰ ei m‰‰ritetty." );
	    
	    if ( nakymienPelaajat == null )
	        throw new Exception( "Peli-ikkunaa ei voi luoda: N‰kymien pelaajia ei m‰‰ritetty." );
	    
	    if ( nappaintenlukija == null )
	        throw new Exception( "Peli-ikkunaa ei voi luoda: N‰pp‰intenlukijaa ei m‰‰ritetty." );
	    
	    this.ylaikkuna = ylaikkuna;
	    this.peli = peli;

	    //N‰iss‰ voi lent‰‰ poikkeus. Tehd‰‰n ne ensin, jottein j‰‰ ik‰vi‰ sotkuja, jos
	    //poikkeus tapahtuukin.
	    PaneeliPelikentta pelikenttaPaneeli = new PaneeliPelikentta( this.peli, nakymienPelaajat );
		PaneeliPistetilanne pistetilannePaneeli = new PaneeliPistetilanne( this.peli );
		this.peli.kaynnistaPeli();
	    
	    //Ikkunan paneeli. Siihen tulee paneeli n‰kymi‰ varten sek‰ marginaalipaneeli viereen.
	    JPanel kokoPaneeli = new JPanel();
		kokoPaneeli.setLayout( new BoxLayout( kokoPaneeli, BoxLayout.X_AXIS ) );
		JPanel marginaaliPaneeli = new JPanel();
	    kokoPaneeli.add( pelikenttaPaneeli );
	    kokoPaneeli.add( marginaaliPaneeli );
	    
	    //Marginaalipaneeliin tulee pistetilanne-paneeli ja n‰pp‰imet-paneeli alakkain.
		marginaaliPaneeli.setLayout( new BoxLayout( marginaaliPaneeli, BoxLayout.Y_AXIS ) );
        JPanel napitPaneeli = new JPanel();
		marginaaliPaneeli.add( pistetilannePaneeli );
        marginaaliPaneeli.add( napitPaneeli );

        //Napit paneeli kuntoon.
        GridLayout napitLayout = new GridLayout( 2, 1 );
        napitLayout.setHgap( 5 );
        napitLayout.setVgap( 5 );
        napitPaneeli.setLayout( napitLayout );
        napitPaneeli.setBorder( BorderFactory.createEmptyBorder( 5, 5, 5, 5 ) );
        //Jotta n‰pp‰inpainalluksilla voidaan pelata eiv‰tk‰ napit sotke sit‰:
        this.pysaytaNappi.setFocusable( false );
        this.lopetaNappi.setFocusable( false );
        napitPaneeli.add( this.pysaytaNappi );
        napitPaneeli.add( this.lopetaNappi );
        this.pysaytaNappi.addActionListener( this );
        this.lopetaNappi.addActionListener( this );
        
        //Laitetaan ikkuna kuntoon.
	    this.setContentPane( kokoPaneeli );
	    //N‰pp‰intenlukija huolehtii n‰pp‰inten lukemisesta.
	    this.addKeyListener( nappaintenlukija );
	    this.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
        this.setResizable( false );
	    this.pack();

	    //N‰ytˆnp‰ivityss‰ie huolehtii peligrafiikan p‰ivitt‰misest‰.
		this.naytonpaivitysSaie = new SaieNaytonpaivitys();
		this.naytonpaivitysSaie.lisaaPiirrettava( pelikenttaPaneeli );
		this.naytonpaivitysSaie.lisaaPiirrettava( pistetilannePaneeli );
	    this.naytonpaivitysSaie.kaynnista();
	    
	    //P‰ivitet‰‰n Pys‰yt‰/Jatka-nappi.
	    this.paivitaPysaytaNappi();
	    
	}
	
	/**
	 * Asettaa pelin k‰yntitilan ja p‰ivitt‰‰ Pys‰yt‰/Jatka-napin. Jos joku ulkopuolinen
	 * haluaa muuttaa pelin k‰yntitilan, tulisi sen kutsua t‰t‰ metodia, eik‰ suoraan
	 * pelin vastaavaa metodia. Tuolloin Pys‰yt‰/Jatka-nappi ei p‰ivittyisik‰‰n.
	 * @param kay Pelin k‰yntitila.
	 */
	public void asetaPelinKaynti( boolean kay ) {
	    
	    this.peli.asetaPelinKaynti( kay );
	    this.paivitaPysaytaNappi();
	    
	}

	/**
	 * P‰ivitt‰‰ Pys‰yt‰/Jatka-napissa olevan tekstin pelin tilan mukaan.
	 */
	public void paivitaPysaytaNappi() {

	    //Pys‰yt‰/Jatka-napin teksti m‰‰r‰ytyy pelin tilasta.
        if ( this.peli.kerroPelinKaynti() )
            this.pysaytaNappi.setText( "Pys‰yt‰" );
        else
            this.pysaytaNappi.setText( "Jatka" );
        
    }

	/**
	 * Yritt‰‰ sulkea ikkunan ja n‰ytt‰‰ Lopputilanne-ikkunan. Jos syntyy virheit‰,
	 * n‰ytet‰‰n virheen viesti k‰ytt‰j‰lle dialogissa.
	 */
    public void suljeIkkuna() {
        
        try {
            
            this.naytonpaivitysSaie.lopeta();
            this.peli.lopetaPeli();
            //Annetaan Lopputilanne-ikkunalle sama yl‰ikkuna!
            IkkunaLopputilanne ikkuna = new IkkunaLopputilanne( this.ylaikkuna, this.peli );
            this.dispose();
            ikkuna.setVisible( true );
            
        }
        catch ( Exception virhe ) {
            
            //T‰h‰n ei pit‰isi koskaan joutua, koska ylaikkuna ja peli eiv‰t ole null.
            ( new DialogiIlmoitus( this, "Virhe", virhe.getMessage() ) ).setVisible( true );
            
        }
        
    }

    /**
     * Metodi kuuntelee Pys‰yt‰/Jatka- ja Lopeta-nappeja. Pys‰yt‰/Jatka-napista
     * muuttaa pelin k‰yntitilan p‰invastaiseksi, asettaa n‰ytˆnp‰ivityss‰ikeen
     * p‰ivitystilan pelin k‰yntitilaa vastaavaksi ja p‰ivitt‰‰ napin tekstin.
     * Lopeta-napista sulkee ikkunan ja n‰ytt‰‰ Lopputilanne-ikkunan.
     * @param event Tapahtumaolio.
     */
    public void actionPerformed( ActionEvent event ) {

        if ( event.getSource() == this.pysaytaNappi ) {
            
            this.peli.asetaPelinKaynti( !this.peli.kerroPelinKaynti() );
            this.naytonpaivitysSaie.asetaPaivitys( this.peli.kerroPelinKaynti() );
            this.paivitaPysaytaNappi();
            
        }
        
        if ( event.getSource() == this.lopetaNappi ) {
            
            this.suljeIkkuna();
            
        }

    }
    
}
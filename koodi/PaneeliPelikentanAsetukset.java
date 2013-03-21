
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * Luokka <CODE>PaneeliPelikentanAsetukset</CODE> kuvaa paneelia, jossa k‰ytt‰j‰ laittaa
 * <CODE>Tulkki</CODE>-pelin pelikentt‰asetukset kuntoon. K‰ytt‰j‰ voi joko valita
 * generoitavan kent‰n ja asettaa t‰lle mitat tai sitten h‰n voi valita valmiin
 * kent‰n (jonkun pelin tarjoamista).
 * 
 * @author Jaakko Luttinen
 *
 */
public class PaneeliPelikentanAsetukset extends JPanel implements ActionListener {

    /**
     * Peli, jonka pelikentt‰asetuksia laitetaan kuntoon.
     */
    private Tulkki peli;
    
    /**
     * Radionapit, joilla pelikentt‰ valitaan.
     */
    private JRadioButton[] pelikenttaRadiot;
    
    /**
     * Tekstikentt‰, johon generoitavan kent‰n leveys syˆtet‰‰n.
     */
    private JTextField leveysSyotto = new JTextField( "1000" );
    
    /**
     * Tekstikentt‰, johon generoitavan kent‰n korkeus syˆtet‰‰n.
     */
    private JTextField korkeusSyotto = new JTextField( "1000" );
    
    /**
     * Luo uuden paneelin, joka antaa k‰ytt‰j‰lle valittavaksi generoitavan kent‰n
     * tai jonkun annetun pelin tarjoamista valmiista kentist‰.
     * @param peli Peli, jonka pelikentt‰‰ asetetaan.
     * @throws Exception Poikkeus kertoo, ett‰ peli‰ ei ole m‰‰ritetty.
     */
    public PaneeliPelikentanAsetukset( Tulkki peli ) throws Exception {
        
        if ( peli == null )
            throw new Exception( "Ei voi luoda pelikent‰nasetus-paneelia: Peli‰ ei ole m‰‰ritetty." );
        
        this.peli = peli;
        
        //Luodaan pelikent‰n valinta radionapit. Muistetaan, ett‰ valmiiden lis‰ksi tulee generointi-
        //vaihtoehto.
        int pelikenttienMaara = this.peli.kerroPelikenttienMaara();
        this.pelikenttaRadiot = new JRadioButton[1+pelikenttienMaara];
        this.pelikenttaRadiot[0] = new JRadioButton( "Generointi" );
        for ( int ind = 0; ind < pelikenttienMaara; ind++ )
            this.pelikenttaRadiot[ind+1] = new JRadioButton( this.peli.kerroPelikentta( ind ) );
        
        //Muodostetaan radionapeista ryhm‰, jotta vain yksi voi olla valittuna.
        ButtonGroup pelikenttaGroup = new ButtonGroup();
        for ( int ind = 0; ind < this.pelikenttaRadiot.length; ind++ )
            pelikenttaGroup.add( this.pelikenttaRadiot[ind] );
        
        this.setLayout( new BoxLayout( this, BoxLayout.Y_AXIS ) );
        
        //Paneeli, josta k‰ytt‰j‰ valitsee pelikent‰n.
        JPanel pelikentanValintaPaneeli = new JPanel();
        
        this.add( pelikentanValintaPaneeli );
        
        //Paneeli, jossa k‰ytt‰j‰ syˆtt‰‰ generoitavan pelikent‰n mitat.
        JPanel mittojenSyottoPaneeli = new JPanel();
        //Mittojen syˆttˆpaneelin rakennus...
        mittojenSyottoPaneeli.setLayout( new BoxLayout( mittojenSyottoPaneeli, BoxLayout.X_AXIS ) );
        JPanel tekstitPaneeli = new JPanel();
        JPanel syototPaneeli = new JPanel();
        mittojenSyottoPaneeli.add( tekstitPaneeli );
        mittojenSyottoPaneeli.add( syototPaneeli );
        tekstitPaneeli.setLayout( new GridLayout( 2, 1 ) );
        syototPaneeli.setLayout( new GridLayout( 2, 1 ) );
        tekstitPaneeli.add( new JLabel( "Leveys:" ) );
        tekstitPaneeli.add( new JLabel( "Korkeus:" ) );
        syototPaneeli.add( this.leveysSyotto );
        syototPaneeli.add( this.korkeusSyotto );
        
        //Pelikent‰n valinta paneeli.
        pelikentanValintaPaneeli.setLayout( new BoxLayout( pelikentanValintaPaneeli, BoxLayout.Y_AXIS ) );
        pelikentanValintaPaneeli.add( new JLabel( "Valitse pelikentt‰:" ) );
        //Laitetaan radionapit
        for ( int ind = 0; ind < this.pelikenttaRadiot.length; ind++ ) {
            
            this.pelikenttaRadiot[ind].addActionListener( this );
            pelikentanValintaPaneeli.add( this.pelikenttaRadiot[ind] );

            //Laitetaan generoitavan pelikent‰n mittojen syˆttˆ heti
            //"Generointi"-napin per‰‰n.
            if ( ind == 0 )
                pelikentanValintaPaneeli.add( mittojenSyottoPaneeli );
            
        }

        //Valitaan "Generointi"-radio.
        this.pelikenttaRadiot[0].setSelected( true );
        
    }
    
    /**
     * Kertoo k‰ytt‰j‰n syˆtt‰m‰n kent‰n leveyden.
     * @return Generoitavan kent‰n leveys.
     * @throws NumberFormatException Poikkeus kertoo, ettei k‰ytt‰j‰n syˆte ollut kokonaisluku.
     */
    public int kerroLeveys() throws NumberFormatException {
        
        return Integer.parseInt( this.leveysSyotto.getText() );
        
    }
    
    /**
     * Kertoo k‰ytt‰j‰n syˆtt‰m‰n kent‰n korkeuden.
     * @return Generoitavan kent‰n korkeus.
     * @throws NumberFormatException Poikkeus kertoo, ettei k‰ytt‰j‰n syˆte ollut kokonaisluku.
     */
    public int kerroKorkeus() throws NumberFormatException {
        
        return Integer.parseInt( this.korkeusSyotto.getText() );
        
    }
    
    /**
     * Kertoo k‰ytt‰j‰n pelikentt‰valinnan. Palautusarvon arvot kertovat seuraavaa:
     * -2=ei valittu mit‰‰n; -1=generointi; 0->=valitun valmiin pelikent‰n indeksi.
     * @return K‰ytt‰j‰n pelikentt‰valinta.
     */
    public int kerroValinta() {
        
        for ( int ind = 0; ind < this.pelikenttaRadiot.length; ind++ ) {
            
            if ( this.pelikenttaRadiot[ind].isSelected() )
                return ind - 1;
            
        }
        
        //Ei mit‰‰n valittuna.
        return -2;
        
    }
    
    /**
     * Asettaa pelin pelikent‰ksi k‰ytt‰j‰n valitseman pelikent‰n.
     * @throws Exception Poikkeus kertoo, ett‰ pelikentt‰‰ ei ole valittu,
     * generoitavan pelikent‰n mitat eiv‰t ole kokonaislukuja tai ne ovat
     * muuten virheelliset. Poikkeus heitet‰‰n myˆs, jos peli on parhaillaan
     * k‰ynniss‰.
     */
    public void asetaPelikentta() throws Exception {
        
        try {

            int valinta = this.kerroValinta();

            //Ei valintaa.
            if ( valinta == -2 )
                throw new Exception( "Pelikentt‰‰ ei ole valittu." );
            
            if ( valinta == -1 ) {
                
                //Generoitava kentt‰.
                this.peli.asetaPelimaailma(  this.kerroLeveys(),
                        				   	 this.kerroKorkeus() );
                
            }
            else {
                
                //Valmis kentt‰.
                this.peli.asetaPelimaailma( valinta );
                
            }
            
        }
        catch ( NumberFormatException virhe ) {
            
            throw new Exception( "Pelikent‰n mittojen t‰ytyy olla kokonaislukuja." );
            
        }
        
    }
    
    /**
     * Asettaa mittojen tekstisyˆttˆjen tilan.
     * @param enabled Totuusarvo kertoo, ovatko tekstisyˆtˆt "enabloitu", k‰ytett‰viss‰.
     */
    private void asetaMittojenSyotonTila( boolean enabled ) {
        
        this.leveysSyotto.setEnabled( enabled );
        this.korkeusSyotto.setEnabled( enabled );
        
    }
    
    /**
     * Kuunnellaan radionappien painamista. Jos radionappi valitaan, disabloidaan
     * tai enabloidaan mittojen syˆttˆkent‰t, riippuen onko valinta valmis
     * kentt‰ vai generointi.
     * @param event Tapahtumaolio.
     */
    public void actionPerformed( ActionEvent event ) {
        
        for ( int ind = 0; ind < this.pelikenttaRadiot.length; ind++ ) {
            
            //0=generointi, ja vain se mahdollistaa mittojen syˆtt‰misen.
            if ( event.getSource() == this.pelikenttaRadiot[ind] )
                this.asetaMittojenSyotonTila( ind == 0 );
                
        }
        
    }
    
}
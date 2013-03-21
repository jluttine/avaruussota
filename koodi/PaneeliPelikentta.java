
import javax.swing.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.Rectangle2D;

/**
 * 
 * Luokka <CODE>PaneeliPelikentta</CODE> kuvaa sellaista paneelia, joka n‰ytt‰‰
 * sille kerrottujen pelaajien n‰kym‰‰ annetusta <CODE>Tulkki</CODE>-pelist‰.
 * N‰kymien m‰‰r‰ll‰ on rajoituksensa (1-4). N‰kym‰t esitet‰‰n kaksisarakkeisessa
 * taulukkomuodossa (jos enemm‰n kuin yksi n‰kym‰). Paneeli ei itse huolehdi
 * n‰ytˆp‰ivityksest‰, vaan edellytt‰‰, ett‰ joku ulkopuolinen taho l‰hett‰‰
 * sille <CODE>piirra</CODE>-komentoja. T‰m‰n vuoksi paneeli toteuttaa rajapinnan
 * <CODE>RajapintaPiirrettava</CODE>. Paneelilla on ns. backbuffer, johon
 * se ensin piirt‰‰ koko paneelin ja vasta kun se on valmis, piirt‰‰ kuvan
 * kokonaisuudessaan itse paneelille.
 * 
 * @author Jaakko Luttinen
 * 
 */
public class PaneeliPelikentta extends JPanel implements RajapintaPiirrettava {

    /**
     * N‰kymien maksimim‰‰r‰.
     */
    public static final int NAKYMIEN_MAKSIMIMAARA = 4;

    /**
     * Peli, josta n‰kymi‰ kuvataan.
     */
    private Tulkki peli;
	
    /**
     * Yksitt‰isen n‰kym‰n leveys.
     */
	private final int nakymanLeveys = 330;
	
	/**
	 * Yksitt‰isen n‰kym‰n korkeus.
	 */
	private final int nakymanKorkeus = 330;

	/**
	 * N‰kymien v‰li toisistaan ja reunoista.
	 */
	private final int nakymienVali = 10;

	/**
	 * N‰kymien pelaajien indeksit peliss‰.
	 */
	private int[] nakymienPelaajat;
	
	/**
	 * Backbuffer, johon piirret‰‰n ja joka kerralla sitten piirret‰‰n n‰ytˆlle.
	 */
	private Image backbuffer;
	
	/**
	 * Backbufferin piirtokonteksti.
	 */
	private Graphics backbufferKonteksti;
	
	/**
	 * Luo uuden paneelin, joka n‰ytt‰‰ pelaajien n‰kymi‰.
	 * @param peli Peli, jonka pelaajista on kyse.
	 * @param nakymienPelaajat Pelaajat joita n‰kymiss‰ n‰ytet‰‰n.
	 * @throws Exception Poikkeus kertoo, ett‰ n‰kymien m‰‰r‰ on virheellinen tai peli‰ ei
	 * ole m‰‰ritetty.
	 */
	public PaneeliPelikentta( Tulkki peli, int[] nakymienPelaajat ) throws Exception {
		
	    if ( nakymienPelaajat == null || nakymienPelaajat.length < 1 || nakymienPelaajat.length > PaneeliPelikentta.NAKYMIEN_MAKSIMIMAARA )
	        throw new Exception( "N‰kymi‰ t‰ytyy asettaa 1-" + PaneeliPelikentta.NAKYMIEN_MAKSIMIMAARA +
	                			 " kappaletta." );
	    
	    if ( peli == null )
	        throw new Exception( "Pelikentt‰paneelia ei voi luoda: Peli‰ ei ole m‰‰ritetty." );
	    
		this.peli = peli;
		
		this.nakymienPelaajat = nakymienPelaajat;
		int nakymienMaara = this.nakymienPelaajat.length;

		//N‰kym‰t n‰ytet‰‰n kaksisarakkeisessa taulukkomuodostelmassa (jos n‰kymi‰ v‰hint‰‰n
		//kaksi).
		int riveja = ( nakymienMaara + 1 ) / 2;
		int sarakkeita = 2;
		if ( nakymienMaara == 1 )
		    sarakkeita = 1;

		//Lasketaan paneelin mitat.
		int korkeus = ( riveja + 1 ) * this.nakymienVali +  riveja * this.nakymanKorkeus;
	    int leveys = ( sarakkeita + 1 ) * this.nakymienVali + sarakkeita * this.nakymanLeveys;

	    this.setPreferredSize( new Dimension( leveys, korkeus ) );
	    this.setBackground( Color.BLACK );
		
		//Yritet‰‰n luoda backbuffer.
	    this.luoBackbuffer();

	}
	
	/**
	 * Yritt‰‰ luoda paneelille backbufferin. V‰rj‰‰ backbufferin mustaksi.
	 * @return Totuusarvo kertoo, onnituiko backbufferin luominen.
	 */
	private boolean luoBackbuffer() {
	    
	    //Yritet‰‰n luoda backbuffer.
		this.backbuffer = this.createImage( this.getWidth(), this.getHeight() );
		
		//Tutkitaan, ep‰onnistuiko backbufferin luonti.
		if ( this.backbuffer == null )
		    return false;
		
		//Luominen onnistui. Luodaan piirtokonteksti.
		this.backbufferKonteksti = this.backbuffer.getGraphics();
		
		//V‰rj‰t‰‰n tausta mustaksi.
		this.backbufferKonteksti.setColor( Color.BLACK );
		this.backbufferKonteksti.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		
		return true;
		
	}
	
	public void paivitetaan( boolean paivitetaan ) {
	    
	    //Jos paneelille ei en‰‰ l‰hetet‰ piirr‰-komentoja, t‰ytyy huolehtia
	    //piirt‰misest‰ itse. Ja jos taas paneelille l‰hetet‰‰n piirr‰-komentoja,
	    //k‰sket‰‰n paneelin olla v‰litt‰m‰tt‰ k‰yttˆj‰rjestelm‰n piirtokomennoista.
	    this.setIgnoreRepaint( paivitetaan );
	    
	}

	/**
	 * K‰yttˆj‰rjestelm‰ l‰hett‰‰ n‰it‰ paneelin p‰ivityskomentoja. Piirret‰‰n
	 * paneeli.
	 */
	public void paintComponent( Graphics piirtokonteksti ) {

	    //Yl‰luokan metodia ei tarvitse kutsua, koska emme halua sen
	    //tekev‰n mit‰‰n. Piirr‰mme paneelin kokonaan itse.
		this.piirraPaneeli( piirtokonteksti );
	 
	}

	public void piirra() {
	
	    this.piirraPaneeli( this.getGraphics() );
	    
	}

	/**
	 * Piirt‰‰ paneelin n‰kym‰t ja tekstin, jos joku on voittanut pelin.
	 * @param piirtokonteksti Piirtokonteksti, johon paneeli piirret‰‰n.
	 */
	public synchronized void piirraPaneeli( Graphics piirtokonteksti ) throws IndexOutOfBoundsException {
	    
	    //Tarvittaessa luodaan backbuffer.
	    if ( this.backbuffer == null ) {
	        
	        //Jos backbufferin luominen ep‰onnistuu, lopetetaan metodin suorittaminen.
	        if ( !this.luoBackbuffer() )
	            return;
	        
	    }
	    
	    //Piirret‰‰n kukin n‰kym‰ yksitellen.
	    for ( int ind = 0; ind < this.nakymienPelaajat.length; ind++ ) {
	        
	        //Yritet‰‰n piirt‰‰ n‰kym‰, mutta jos pelaajan indeksi jostain syyst‰ onkin
	        //virheellinen, j‰tet‰‰n kyseinen alue piirt‰m‰tt‰.
	        try {
	            
	            //Lasketaan n‰kym‰n vasemman yl‰kulman sijainti paneelilla.
	            int x = ( 1 + ind % 2 ) * this.nakymienVali + ( ind % 2 ) * this.nakymanLeveys;
	            int y = ( 1 + ind / 2 ) * this.nakymienVali + ( ind / 2 ) * this.nakymanKorkeus;

	            //Jotta n‰kym‰t pysyv‰t t‰sm‰lleen rajojensa sis‰puolella, estet‰‰n
	            //piirt‰minen n‰kym‰n ulkopuolelle.
	            this.backbufferKonteksti.setClip( x,
	                    						  y,
	                    						  this.nakymanLeveys,
	                    						  this.nakymanKorkeus );
	    	    
	            //Piirret‰‰n n‰kym‰.
	            this.peli.piirraNakyma( this.backbufferKonteksti,
	                    				this.nakymienPelaajat[ind],
	                    				x,
	                    				y,
	                    				this.nakymanLeveys,
	                    				this.nakymanKorkeus );
	    	    
	        }
	        catch ( IndexOutOfBoundsException virhe ) {  }
	        
	    }
		
	    //Jos joku on voittanut pelin, tulostetaan siit‰ n‰ytˆlle viesti.
	    int voittajanIndeksi = this.peli.kerroVoittaja();
	    if ( voittajanIndeksi != -1 )
	        this.piirraVoittoteksti( this.peli.kerroPelaajanNimi( voittajanIndeksi ) );
	    
		//T‰m‰ hidastaa aika paljon... kun tulostetaan n‰ytˆlle...
		piirtokonteksti.drawImage( this.backbuffer, 0, 0, null );
		
	}
	
	/**
	 * Piirt‰‰ paneeliin tekstin, ett‰ pelaaja on voittanut.
	 * @param nimi Voittaneen pelaajan nimi.
	 */
	private void piirraVoittoteksti( String nimi ) {
	    
        //Teksti piirret‰‰n punaisella ja koko paneeliin.
	    this.backbufferKonteksti.setClip( 0, 0, this.getWidth(), this.getHeight() );
        this.backbufferKonteksti.setColor( Color.RED );
        
        //Asetetaan fontti.
        Font vanhaFontti = this.backbufferKonteksti.getFont();
        Font fontti = new Font( null, Font.BOLD, 40 );
        this.backbufferKonteksti.setFont( fontti );
        
        //Tekstit, jotka tulostetaan.
        String teksti1 = "Peli loppunut!";
        String teksti2 = nimi + " voitti!";
        
        //Lasketaan koordinaatit suurin piirtein keskelle (korkeus ei ole ihan tarkka).
        FontRenderContext fontRender = new FontRenderContext( null, false, false );
        Rectangle2D rect1 = fontti.getStringBounds( teksti1, fontRender );
        Rectangle2D rect2 = fontti.getStringBounds( teksti2, fontRender );
        int vali = 20; //Tekstien v‰linen ero korkeussuunnassa.
        int x1 = ( this.getWidth() - ( int )rect1.getWidth() ) / 2;
        int y1 = ( this.getHeight() - ( int )( rect1.getHeight() + rect2.getHeight() + vali - fontti.getSize() ) ) / 2;
        int x2 = ( this.getWidth() - ( int )rect2.getWidth() ) / 2;
        int y2 = y1 + vali + ( int )rect2.getHeight();
        
        //Piirret‰‰n tekstit.
        this.backbufferKonteksti.drawString( teksti1, x1, y1 );
        this.backbufferKonteksti.drawString( teksti2, x2, y2 );
        
        //Asetetaan vanha fontti takaisin.
        this.backbufferKonteksti.setFont( vanhaFontti );

	}

}

import java.awt.image.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;

/**
 * 
 * Luokka <CODE>Kuvasarja</CODE> kuvaa samankokoisista pikkukuvista muodostuvaa
 * kuvasarjaa, sprite‰. Yksitt‰iset kuvat (frame) ovat j‰rjestyksess‰ rivi rivilt‰.
 * Kuvasarjan tausta voi olla l‰pin‰kyv‰ ja se voi koostua myˆskin vain yhdest‰
 * kuvasta. 
 *
 * @author Jaakko Luttinen
 * 
 */

public class Kuvasarja {

    /**
     * Kuvasarja s‰ilytet‰‰n BufferedImage-tyypisess‰ oliossa.
     */
	private BufferedImage kuva;
	
	/**
	 * Kuvien m‰‰r‰ yhdell‰ rivill‰.
	 */
	private int sarakeMaara;
	
	/**
	 * Rivien m‰‰r‰.
	 */
	private int riviMaara;
	
	/**
	 * Lataa annetun kuvatiedoston.
	 * @param tiedostonimi Ladattava kuvatiedosto.
	 * @return BufferedImage-olio, joka sis‰lt‰‰ kuvan.
	 * @throws Exception Poikkeus, jos tiedoston lukemisessa ilmeni ongelmia.
	 */
	private static BufferedImage lataaKuva( String tiedostonimi ) throws Exception {
		
		File tiedosto = new File( tiedostonimi );
		
		try {
			
			return ImageIO.read( tiedosto );
			
			
		} catch ( IOException virhe ) {
			
		    throw new Exception( "Kuvatiedoston \"" + tiedostonimi + "\" lataaminen ep‰onnistui: " +
		            			 virhe.getMessage() );
		    
		}
		
		
	}

	/**
	 * Luo uuden kuvasarjan annetusta <CODE>kuvasta</CODE>. 
	 * @param kuva Kuvataulukko.
	 * @param sarakeMaara Kuvien m‰‰r‰ rivill‰.
	 * @param riviMaara Rivien m‰‰r‰.
	 */
	public Kuvasarja( BufferedImage kuva, int sarakeMaara, int riviMaara ) {
	    
		GraphicsConfiguration konfiguraatio = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		this.kuva = konfiguraatio.createCompatibleImage( kuva.getWidth(), kuva.getHeight(), kuva.getColorModel().getTransparency() );//Transparency.BITMASK );//new BufferedImage( leveys, korkeus, BufferedImage.TYPE_INT_RGB );
	    ( this.kuva.createGraphics() ).drawImage( kuva, null, 0, 0 );
	    this.sarakeMaara = sarakeMaara;
	    this.riviMaara = riviMaara;
	    
	}
	
	/**
	 * Luo uuden kuvasarjan annetusta tiedostosta. Kuvatiedostossa on rivej‰ ja sarakkeita
	 * annettu m‰‰r‰.
	 * @param tiedostonimi Kuvatiedostopolku.
	 * @param sarakeMaara Kuvien m‰‰r‰ rivill‰.
	 * @param riviMaara Rivien m‰‰r‰.
	 * @throws Exception Poikkeus, jos tiedoston lukemisessa ilmeni ongelmia.
	 */
	public Kuvasarja( String tiedostonimi, int sarakeMaara, int riviMaara ) throws Exception {
		
		this( Kuvasarja.lataaKuva( tiedostonimi ), sarakeMaara, riviMaara );
		
	}

	/**
	 * Luo uuden kuvasarjan, jonka yhden kuvan leveys ja korkeus ovat annetut
	 * ja niist‰ muodostuvan kuvataulukon rakenne on annetut sarake- ja rivim‰‰r‰t.
	 * @param leveys Yksitt‰isen kuvan leveys.
	 * @param korkeus Yksitt‰isen kuvan korkeus.
	 * @param sarakeMaara Kuvien m‰‰r‰ rivill‰.
	 * @param riviMaara Rivien m‰‰r‰.
	 */
	public Kuvasarja( int leveys, int korkeus, int sarakeMaara, int riviMaara ) {
	    
		this( new BufferedImage( sarakeMaara * leveys, riviMaara * korkeus, BufferedImage.TYPE_INT_ARGB ),
	          sarakeMaara,
	          riviMaara );
	    
	}
	
	/**
	 * Kertoo yksitt‰isen kuvan leveyden.
	 * @return Yksitt‰isen kuvan leveys.
	 */
	public int kerroLeveys() {
	    
	    return this.kuva.getWidth() / this.sarakeMaara;
	    
	}
	
	/**
	 * Kertoo yksitt‰isen kuvan korkeuden.
	 * @return Yksitt‰isen kuvan korkeus.
	 */
	public int kerroKorkeus() {
	    
	    return this.kuva.getHeight() / this.riviMaara;
	    
	}
	
	/**
	 * Kertoo kuvasarjan kuvien m‰‰r‰n (sarakkeet x rivit).
	 * @return Kuvasarjan kuvien m‰‰r‰.
	 */
	public int kerroKuvienMaara() {
		
		return this.sarakeMaara * this.riviMaara;
		
	}
	
	/**
	 * Kertoo kuvasarjan Graphics2D-olion.
	 * @return Kuvasarjan Graphics2D-olio.
	 */
	public Graphics2D kerroGraphics() {
	    
	    return this.kuva.createGraphics();
	    
	}

	/**
	 * Piirt‰‰ kuvasarjan annetun framen (<CODE>indeksi</CODE>) annetulle
	 * <CODE>piirtokontekstille</CODE> sijaintiin (<CODE>x</CODE>, <CODE>y</CODE>).
	 * Jos annettu framen indeksi on virheellinen, ei framea piirret‰.
	 * @param piirtokonteksti Piirtokonteksti, johon frame piirret‰‰n.
	 * @param indeksi Framen indeksi ( so. luku v‰lilt‰ 0 - (rivit x sarakkeet - 1) ).
	 * @param x Framen vasemman yl‰kulman sijainnin x-koordinaatti.
	 * @param y Framen vasemman yl‰kulman sijainnin y-koordinaatti.
	 */
	public void piirraFrame( Graphics piirtokonteksti, int indeksi, int x, int y ) {
		
		if ( indeksi < 0 || indeksi >= this.kerroKuvienMaara() )
		    return;
		
		int leveys = this.kerroLeveys();
		int korkeus = this.kerroKorkeus();
		int sx1 = leveys * ( indeksi % this.sarakeMaara );
		int sy1 = korkeus * ( indeksi / this.sarakeMaara );
		int sx2 = sx1 + leveys;
		int sy2 = sy1 + korkeus;
		int dx2 = x + leveys;
		int dy2 = y + korkeus;
	    piirtokonteksti.drawImage( this.kuva, x, y, dx2, dy2, sx1, sy1, sx2, sy2, null );

	}
	
}
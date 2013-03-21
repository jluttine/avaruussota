
import java.awt.Graphics;

/**
 *
 * Luokka <CODE>Kappale</CODE> kuvaa avaruuden kappaletta, joka vie avaruudesta
 * jonkin alueen. Kappaleilla on kuvasarja. Kappaleella voi olla tunnisteena jokin
 * tyyppi (=nimi). Lis�ksi kappaleella on ominaisuutena t�rm�tt�vyys, joka kertoo,
 * saako kappaleen kanssa sijaita p��llekk�in toista kappaletta t�rm�tt�viss� olevaa
 * kappaletta.
 *
 * @author Jaakko Luttinen
 *
 */

public abstract class Kappale {
	
    /**
     * Kappaleiden kimmoisuutta kuvaava vakio. 0=kimmoton, 1=kimmoisa.
     */
    protected static final double KIMMOISUUS = 0.4;
    
    /**
     * Kappaleen tunnisteena toimiva tyyppi. Ei tarvitse k�ytt�� eli voi asettaa
     * null.
     */
    private String tyyppi;
    
    /**
     * Avaruus, jossa kappale sijaitsee, tai null, jos ei sijaitse miss��n.
     */
    private Avaruus maailma;
    
    /**
     * Kappaleen kuvasarja.
     */
	private Kuvasarja kuva;
	
	/**
	 * Totuusarvo, joka kertoo, onko kappale t�rm�tt�viss�, eli saako kappaleen
	 * kanssa sijaita p��llek�in toista kappaletta.
	 */
	private boolean tormattavissa;
	
	/**
	 * Totuusarvo, joka kertoo, onko kappale tuhoutunut ja pit�isi poistaa avaruudesta.
	 * Toimii lippuna, jonka voi nostaa ja jonka suoritaSykays-metodi lukee. Kappaletta ei
	 * tule suoraan poistaa maailmasta, vaan nostaa t�m� lippu.
	 */
	private boolean tuhoutuu;

	/**
	 * Luo uuden kappaleen annettujen tietojen perusteella.
	 * @param tyyppi Kappaleen tunniste. Ei tarvitse k�ytt��, eli voi hyvin olla null.
	 * @param kuva Kappaleen kuvasarja.
	 * @throws Exception Poikkeus kertoo, ett� annetuissa parametreissa oli virhe.
	 */
	public Kappale( String tyyppi, Kuvasarja kuva ) throws Exception {
		
	    if ( kuva == null )
	        throw new Exception( "Ei voi luoda kappaletta: Kuvasarjaa ei ole m��ritetty." );
	    
	    this.tyyppi = tyyppi;
		this.kuva = kuva;
		this.tormattavissa = true;
		
		//Kun kappale asetetaan maailmaan, t�m� muutetaan false.
		this.tuhoutuu = true;
		
	}
	
	/**
	 * Luo uuden kappaleen, jolla on kopion kanssa sama tyyppi ja kuvasarja.
	 * @param kopio Kappale, jonka perusteella uusi kappale halutaan luoda.
	 */
	public Kappale( Kappale kopio ) {
	    
	    this.tyyppi = kopio.tyyppi;
		this.kuva = kopio.kuva;
		this.tormattavissa = true;
		
		//Kun kappale asetetaan maailmaan, t�m� muutetaan false.
		this.tuhoutuu = true;
	    
	}
	
	/**
	 * Kertoo maailman, jossa kappale sijaitsee.
	 * @return Avaruus, jossa kappale sijaitsee.
	 */
	public Avaruus kerroMaailma() {
		
		return this.maailma;
		
	}

	/**
	 * Kertoo kappaleen tunnisteena toimivan tyypin.
	 * @return Kappaleen tunnisteena toimiva tyyppi tai null.
	 */
	public String kerroTyyppi() {
	    
	    return this.tyyppi;
	    
	}

	/**
	 * Kertoo kappaleen kuvasarjan.
	 * @return Kappaleen kuvasarja.
	 */
	public Kuvasarja kerroKuva() {
	    
	    return this.kuva;
	    
	}
	
	/**
	 * Kertoo kappaleen alueen. Juuri t�m�n alueen perusteella kappaletta avaruudessa
	 * k�sitell��n huolimatta mink� kokoinen tai muotoinen kappaleen kuva on.
	 * @return Kappaleen alue.
	 */
	public abstract Alue kerroAlue();

	/**
	 * Kertoo kappaleen sijainnin maailmassa tai null, jos kappale ei sijaitse
	 * miss��n maailmassa.
	 * @return Kappaleen koordinaatit maailmassa tai null, jos ei ole maailmassa.
	 */
	public Koordinaatit kerroSijainti() {
		
		return this.kerroAlue().kerroSijainti();
		
	}
	
	/**
	 * Kertoo kappaleen leveyden avaruudessa.
	 * @return Kappaleen leveys.
	 */
	public int kerroLeveys() {
	    
	    return this.kerroAlue().kerroLeveys();
	    
	}
	
	/**
	 * Kertoo kappaleen korkeuden avaruudessa.
	 * @return Kappaleen korkeus.
	 */
	public int kerroKorkeus() {
	    
	    return this.kerroAlue().kerroKorkeus();
	    
	}

	/**
	 * Kertoo, onko kappale t�rm�tt�viss�. Kappaleen ei tarvitse reagoida
	 * kappaleisiin, jotka eiv�t ole t�rm�tt�viss�. Kappale ei oletusarvoisesti
	 * saisi sijaita sellaisen kappaleen kanssa p��llekk�in, joka on t�rm�tt�viss�.
	 * @return Totuusarvo, joka kertoo, onko kappale t�rm�tt�viss�.
	 */
	public boolean onTormattavissa() {
	    
	    return this.tormattavissa;
	    
	}
	
	/**
	 * Asettaa kappaleen t�rm�tt�vyyden.
	 * @param tormattavissa Totuusarvo, joka kertoo, onko kappale t�rm�tt�viss�.
	 */
	public void asetaTormattavyys( boolean tormattavissa ) {
	    
	    this.tormattavissa = tormattavissa;
	    
	}

	/**
	 * Nostaa <CODE>tuhoutuu</CODE> lipun pystyyn. Kun kappale halutaan tuhota ja
	 * poistaa maailmasta, tulisi kutsua t�t� metodia, joka nostaa lipun pystyyn.
	 * suoritaSykays-metodi tekee poiston.
	 */
	public void tuhoudu() {
	    
	    this.tuhoutuu = true;
	    
	}
	
	/**
	 * Totuusarvo, joka kertoo, onko kappale poissa maailmasta tai onko sen
	 * tuhoutumislippu nostettu pystyyn.
	 * @return Totuusarvo, joka kertoo onko kappale tuhoutunut tai tuhoutumassa.
	 */
	public boolean onTuhoutunut() {
	    
	    return this.tuhoutuu;
	    
	}
	
	/**
	 * Suorittaa kappaleeseen toimenpiteet, jotka tuhoa tekev� asia saa
	 * aikaan. Palauttaa totuusarvon, joka kertoo, tekik� ammus tuhoa.
	 * @param tuhonLahde Tuhoa tekev� asia.
	 * @return Totuusarvo, joka kertoo, tekik� ammus jotain tuhoa.
	 */
	public boolean suoritaOsuma( RajapintaTuhoatekeva tuhonLahde ) {
	    
	    return false;
	    
	}

	/**
	 * Kertoo kappaleen t�m�nhetkisen framen indeksin.
	 * @return Kappaleen t�m�nhetkisen framen indeksi.
	 */
	public int kerroFrame() {
	    
	    return 0;
	    
	}
	
	/**
	 * Antaa kappaleelle mahdollisuuden tehd� toimenpiteens�, kun aikaa on kulunut
	 * annettu m��r�. Poistaa kappaleen maailmasta, jos tuhoutuu-lippu on nostettu.
	 * @param aika Aika, joka on kulunut viime syk�yksen suorittamisesta.
	 */
	public void suoritaSykays( int aika ) {
		
		if ( !this.tuhoutuu )
		    this.toimi( aika );
		
		if ( this.tuhoutuu )
		    this.poistaMaailmasta();
		
	}
	
	/**
	 * Kappale tekee toimenpiteens�, kun aikaa on kulunut annettu m��r�.
	 * @param aika Aika, joka on kulunut viime syk�yksest�.
	 */
	protected abstract void toimi( int aika );

	/**
	 * Asettaa kappaleen maailmaksi ja sijainniksi annetut arvot. Lis�� kappaleen
	 * annettuun maailmaan. Poistaa kappaleen aiemmasta maailmasta. Metodi ei
	 * suorita mink��nlaisia tarkasteluja sijainnin kelvollisuudesta t�rm�yksi�
	 * ajatellen. Se on kutsujan vastuulla. Jos <CODE>maailma</CODE> on null tai
	 * kappale on jo annetussa maailmassa, ei metodi tee mit��n.
	 * @param maailma Maailma, johon kappale lis�t��n.
	 * @param sijaintiX X-koordinaatti, johon kappale asetetaan.
	 * @param sijaintiY Y-koordinaatti, johon kappale asetetaan.
	 * @throws Exception
	 */
	public void asetaMaailmaan( Avaruus maailma, double sijaintiX, double sijaintiY ) throws Exception {
	    
	    if ( maailma == null || this.maailma == maailma )
	        return;

	    this.poistaMaailmasta();
	    
	    //T�ss� saattaa lent�� poikkeus, jos kappaleen alue ei sovi koordinaatistoon.
	    this.kerroAlue().asetaSijainti( new Koordinaatit( maailma.kerroKoordinaatisto(),
				  										  sijaintiX,
				  										  sijaintiY ) );

	    this.tuhoutuu = false;
	    this.maailma = maailma;
	    this.maailma.lisaaKappale( this, sijaintiX, sijaintiY );
	    
	}
	
	/**
	 * Asettaa maailmaksi null. Poistaa kappaleen maailmasta, jossa se sijaitsi.
	 * Asettaa kappaleen sijainniksi null. 
	 */
	public void poistaMaailmasta() {
	    
	    if ( this.maailma != null ) {
	        
	        synchronized ( this.maailma ) {
	            
	            this.tuhoudu();
	            Avaruus vanhaMaailma = this.maailma;
	            this.maailma = null;
	            vanhaMaailma.poistaKappale( this );
	            
	        }
	        
	    }
	    
	    this.kerroAlue().poistaSijainti();
    
	}
	
	/**
	 * Metodi tutkii leikkaavatko ko. kappaleen ja parametrina saadun kappaleen alueet
	 * avaruudessa. Jos kumpikaan kappaleista on tuhoutunut/tuhoutumassa, ei leikkausta
	 * ole.
	 * @param testattava Kappale, jonka kanssa alueiden leikkaustesti halutaan suorittaa.
	 * @return Totuusarvo, joka kertoo, leikkaavatko kappaleiden alueet.
	 */
	public boolean leikkaavat( Kappale testattava ) {
	    
	    if ( !this.tuhoutuu && !testattava.tuhoutuu )
	        return this.kerroAlue().leikkaavat( testattava.kerroAlue() );
	    
	    return false;
	    
	}
	

	/**
	 * Kun kahden kappaleen alueet leikkaavat toisiaan, kutsutaan molempien metodia
	 * suoritaTormays. Parametrina annetaan tormayksen toinen osapuoli. Tormayksen
	 * vaikutukset, jotka vaikuttavat muihinkin, tulee pit�� n�kym�tt�min�, kunnes
	 * kaikki t�rm�ykset on tutkittu ja sen tormayksetTutkittu-metodia kutsutaan. Kaikkien
	 * aliluokkien tulee itse toteuttaa t�rm�ysten tarkastelu, jos se on tarpeen (esim.
	 * liikkuvilla kappaleilla se on tarpeen). Palautetaan totuusarvo, joka kertoo,
	 * sijaitsevatko kappaleet toisiinsa n�hden nyt laittomasti.
	 * @param tormattava Kappale, jonka kanssa t�rm�ys tapahtuu.
	 */
	public boolean suoritaTormays( Kappale tormattava ) {
	    
	    return this.tormattavissa && tormattava.tormattavissa;
	    
	}
	
	/**
	 * Tekee suoritaTormays-metodi tekem�t n�kym�tt�m�t muutokset lopullisiksi.
	 * Kun kappaleelle kutsutaan suoritaTormays-metodia tulisi sen pit�� muihin
	 * vaikuttavat tietojen muutokset (esim. nopeus) n�kym�tt�min�. Vasta t�t� metodia
	 * kutsuttaessa kappaleen on lupa saattaa muutokset pysyviksi.
	 */
	public void tormayksetTutkittu() {
	    
	    return;
	    
	}
	
	/**
	 * Piirt�� kappaleen piirtokontekstiin, jos n�kyy n�kym�ss�. N�kym�n keskipiste
	 * kertoo maailman pisteen, joka on n�kym�n keskell�. N�kym�ll� on annetut
	 * mitat. Jos n�kym�n mitat ovat suuremmat kuin avaruuden mitat, ei kappale
	 * v�ltt�m�tt� piirry tarpeeksi monta kertaa joka paikkaan. Yksinkertaisuuden
	 * ja tehokkuuden vuoksi kappale piirret��n korkeintaan nelj�sti, kerran kuhunkin
	 * n�kym�n nelj�nnekseen.
	 * @param piirtokonteksti Piirtokonteksti, johon kappale piirret��n.
	 * @param sijaintiX Maailman sen pisteen x-koordinaatti, johon n�kym� on keskitetty. 
	 * @param sijaintiY Maailman sen pisteen y-koordinaatti, johon n�kym� on keskitetty.
	 * @param vasen N�kym�n vasemman yl�kulman x-koordinaatti piirtokontekstilla.
	 * @param yla N�kym�n vasemman yl�kulman y-koordinaatti piirtokontekstilla.
	 * @param leveys N�kym�n leveys.
	 * @param korkeus N�kym�n korkeus.
	 */
	public void piirraKappale( Graphics piirtokonteksti, double sijaintiX, double sijaintiY, int vasen, int yla, int leveys, int korkeus ) {
		
	    //Synkronoidaan, jottei mit��n muutoksia tapahdu kesken piirt�misen.
	    synchronized ( this.maailma ) {
	    	
	        //Otetaan mitat nimenomaan kuvasarjasta!
	    	int kappaleenLeveys = this.kuva.kerroLeveys();
	    	int kappaleenKorkeus = this.kuva.kerroKorkeus();
	    	
	    	//Jatkuvuuden takia tarvitsee tutkia kappaleen sijainti jokaisen nelj�nneksen suhteen.
	    	for ( int ind = -1; ind < 2; ind += 2 ) {
	    		
	    		for ( int jnd = -1; jnd < 2; jnd += 2 ) {
	    		    
	    		    Koordinaatit sijainti = this.kerroAlue().kerroSijainti();
	    		    
	    		    if ( sijainti != null ) {
	    		       
	    		        //Koska poikkeama mitataan itsest� keskipisteeseen, pit�� ottaa vastaluku.
	    		        int etaisyysX = -( int )Math.round( sijainti.kerroMatkaX( sijaintiX, ind ) );
	    		        int etaisyysY = -( int )Math.round( sijainti.kerroMatkaY( sijaintiY, jnd ) );
	    		        
	    		        //Tutkitaan tarvitseeko piirt��...
	    		        if ( ind * etaisyysX < kappaleenLeveys + leveys / 2.0 && jnd * etaisyysY < kappaleenKorkeus + korkeus / 2.0 ) {
	    		            
	    		            int x = vasen + ( etaisyysX - kappaleenLeveys / 2 + leveys / 2 );
	    		            int y = yla + ( etaisyysY - kappaleenKorkeus / 2 + korkeus / 2 );
	    		            this.kuva.piirraFrame( piirtokonteksti, this.kerroFrame(), x, y );
	    		            
	    		        }
	    		        
	    		    }
	    		        
	    		}
	    		
	    	}
    	
	    }
	    
	}
	
}
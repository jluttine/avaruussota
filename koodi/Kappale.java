
import java.awt.Graphics;

/**
 *
 * Luokka <CODE>Kappale</CODE> kuvaa avaruuden kappaletta, joka vie avaruudesta
 * jonkin alueen. Kappaleilla on kuvasarja. Kappaleella voi olla tunnisteena jokin
 * tyyppi (=nimi). Lisäksi kappaleella on ominaisuutena törmättävyys, joka kertoo,
 * saako kappaleen kanssa sijaita päällekkäin toista kappaletta törmättävissä olevaa
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
     * Kappaleen tunnisteena toimiva tyyppi. Ei tarvitse käyttää eli voi asettaa
     * null.
     */
    private String tyyppi;
    
    /**
     * Avaruus, jossa kappale sijaitsee, tai null, jos ei sijaitse missään.
     */
    private Avaruus maailma;
    
    /**
     * Kappaleen kuvasarja.
     */
	private Kuvasarja kuva;
	
	/**
	 * Totuusarvo, joka kertoo, onko kappale törmättävissä, eli saako kappaleen
	 * kanssa sijaita päällekäin toista kappaletta.
	 */
	private boolean tormattavissa;
	
	/**
	 * Totuusarvo, joka kertoo, onko kappale tuhoutunut ja pitäisi poistaa avaruudesta.
	 * Toimii lippuna, jonka voi nostaa ja jonka suoritaSykays-metodi lukee. Kappaletta ei
	 * tule suoraan poistaa maailmasta, vaan nostaa tämä lippu.
	 */
	private boolean tuhoutuu;

	/**
	 * Luo uuden kappaleen annettujen tietojen perusteella.
	 * @param tyyppi Kappaleen tunniste. Ei tarvitse käyttää, eli voi hyvin olla null.
	 * @param kuva Kappaleen kuvasarja.
	 * @throws Exception Poikkeus kertoo, että annetuissa parametreissa oli virhe.
	 */
	public Kappale( String tyyppi, Kuvasarja kuva ) throws Exception {
		
	    if ( kuva == null )
	        throw new Exception( "Ei voi luoda kappaletta: Kuvasarjaa ei ole määritetty." );
	    
	    this.tyyppi = tyyppi;
		this.kuva = kuva;
		this.tormattavissa = true;
		
		//Kun kappale asetetaan maailmaan, tämä muutetaan false.
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
		
		//Kun kappale asetetaan maailmaan, tämä muutetaan false.
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
	 * Kertoo kappaleen alueen. Juuri tämän alueen perusteella kappaletta avaruudessa
	 * käsitellään huolimatta minkä kokoinen tai muotoinen kappaleen kuva on.
	 * @return Kappaleen alue.
	 */
	public abstract Alue kerroAlue();

	/**
	 * Kertoo kappaleen sijainnin maailmassa tai null, jos kappale ei sijaitse
	 * missään maailmassa.
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
	 * Kertoo, onko kappale törmättävissä. Kappaleen ei tarvitse reagoida
	 * kappaleisiin, jotka eivät ole törmättävissä. Kappale ei oletusarvoisesti
	 * saisi sijaita sellaisen kappaleen kanssa päällekkäin, joka on törmättävissä.
	 * @return Totuusarvo, joka kertoo, onko kappale törmättävissä.
	 */
	public boolean onTormattavissa() {
	    
	    return this.tormattavissa;
	    
	}
	
	/**
	 * Asettaa kappaleen törmättävyyden.
	 * @param tormattavissa Totuusarvo, joka kertoo, onko kappale törmättävissä.
	 */
	public void asetaTormattavyys( boolean tormattavissa ) {
	    
	    this.tormattavissa = tormattavissa;
	    
	}

	/**
	 * Nostaa <CODE>tuhoutuu</CODE> lipun pystyyn. Kun kappale halutaan tuhota ja
	 * poistaa maailmasta, tulisi kutsua tätä metodia, joka nostaa lipun pystyyn.
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
	 * Suorittaa kappaleeseen toimenpiteet, jotka tuhoa tekevä asia saa
	 * aikaan. Palauttaa totuusarvon, joka kertoo, tekikö ammus tuhoa.
	 * @param tuhonLahde Tuhoa tekevä asia.
	 * @return Totuusarvo, joka kertoo, tekikö ammus jotain tuhoa.
	 */
	public boolean suoritaOsuma( RajapintaTuhoatekeva tuhonLahde ) {
	    
	    return false;
	    
	}

	/**
	 * Kertoo kappaleen tämänhetkisen framen indeksin.
	 * @return Kappaleen tämänhetkisen framen indeksi.
	 */
	public int kerroFrame() {
	    
	    return 0;
	    
	}
	
	/**
	 * Antaa kappaleelle mahdollisuuden tehdä toimenpiteensä, kun aikaa on kulunut
	 * annettu määrä. Poistaa kappaleen maailmasta, jos tuhoutuu-lippu on nostettu.
	 * @param aika Aika, joka on kulunut viime sykäyksen suorittamisesta.
	 */
	public void suoritaSykays( int aika ) {
		
		if ( !this.tuhoutuu )
		    this.toimi( aika );
		
		if ( this.tuhoutuu )
		    this.poistaMaailmasta();
		
	}
	
	/**
	 * Kappale tekee toimenpiteensä, kun aikaa on kulunut annettu määrä.
	 * @param aika Aika, joka on kulunut viime sykäyksestä.
	 */
	protected abstract void toimi( int aika );

	/**
	 * Asettaa kappaleen maailmaksi ja sijainniksi annetut arvot. Lisää kappaleen
	 * annettuun maailmaan. Poistaa kappaleen aiemmasta maailmasta. Metodi ei
	 * suorita minkäänlaisia tarkasteluja sijainnin kelvollisuudesta törmäyksiä
	 * ajatellen. Se on kutsujan vastuulla. Jos <CODE>maailma</CODE> on null tai
	 * kappale on jo annetussa maailmassa, ei metodi tee mitään.
	 * @param maailma Maailma, johon kappale lisätään.
	 * @param sijaintiX X-koordinaatti, johon kappale asetetaan.
	 * @param sijaintiY Y-koordinaatti, johon kappale asetetaan.
	 * @throws Exception
	 */
	public void asetaMaailmaan( Avaruus maailma, double sijaintiX, double sijaintiY ) throws Exception {
	    
	    if ( maailma == null || this.maailma == maailma )
	        return;

	    this.poistaMaailmasta();
	    
	    //Tässä saattaa lentää poikkeus, jos kappaleen alue ei sovi koordinaatistoon.
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
	 * vaikutukset, jotka vaikuttavat muihinkin, tulee pitää näkymättöminä, kunnes
	 * kaikki törmäykset on tutkittu ja sen tormayksetTutkittu-metodia kutsutaan. Kaikkien
	 * aliluokkien tulee itse toteuttaa törmäysten tarkastelu, jos se on tarpeen (esim.
	 * liikkuvilla kappaleilla se on tarpeen). Palautetaan totuusarvo, joka kertoo,
	 * sijaitsevatko kappaleet toisiinsa nähden nyt laittomasti.
	 * @param tormattava Kappale, jonka kanssa törmäys tapahtuu.
	 */
	public boolean suoritaTormays( Kappale tormattava ) {
	    
	    return this.tormattavissa && tormattava.tormattavissa;
	    
	}
	
	/**
	 * Tekee suoritaTormays-metodi tekemät näkymättömät muutokset lopullisiksi.
	 * Kun kappaleelle kutsutaan suoritaTormays-metodia tulisi sen pitää muihin
	 * vaikuttavat tietojen muutokset (esim. nopeus) näkymättöminä. Vasta tätä metodia
	 * kutsuttaessa kappaleen on lupa saattaa muutokset pysyviksi.
	 */
	public void tormayksetTutkittu() {
	    
	    return;
	    
	}
	
	/**
	 * Piirtää kappaleen piirtokontekstiin, jos näkyy näkymässä. Näkymän keskipiste
	 * kertoo maailman pisteen, joka on näkymän keskellä. Näkymällä on annetut
	 * mitat. Jos näkymän mitat ovat suuremmat kuin avaruuden mitat, ei kappale
	 * välttämättä piirry tarpeeksi monta kertaa joka paikkaan. Yksinkertaisuuden
	 * ja tehokkuuden vuoksi kappale piirretään korkeintaan neljästi, kerran kuhunkin
	 * näkymän neljännekseen.
	 * @param piirtokonteksti Piirtokonteksti, johon kappale piirretään.
	 * @param sijaintiX Maailman sen pisteen x-koordinaatti, johon näkymä on keskitetty. 
	 * @param sijaintiY Maailman sen pisteen y-koordinaatti, johon näkymä on keskitetty.
	 * @param vasen Näkymän vasemman yläkulman x-koordinaatti piirtokontekstilla.
	 * @param yla Näkymän vasemman yläkulman y-koordinaatti piirtokontekstilla.
	 * @param leveys Näkymän leveys.
	 * @param korkeus Näkymän korkeus.
	 */
	public void piirraKappale( Graphics piirtokonteksti, double sijaintiX, double sijaintiY, int vasen, int yla, int leveys, int korkeus ) {
		
	    //Synkronoidaan, jottei mitään muutoksia tapahdu kesken piirtämisen.
	    synchronized ( this.maailma ) {
	    	
	        //Otetaan mitat nimenomaan kuvasarjasta!
	    	int kappaleenLeveys = this.kuva.kerroLeveys();
	    	int kappaleenKorkeus = this.kuva.kerroKorkeus();
	    	
	    	//Jatkuvuuden takia tarvitsee tutkia kappaleen sijainti jokaisen neljänneksen suhteen.
	    	for ( int ind = -1; ind < 2; ind += 2 ) {
	    		
	    		for ( int jnd = -1; jnd < 2; jnd += 2 ) {
	    		    
	    		    Koordinaatit sijainti = this.kerroAlue().kerroSijainti();
	    		    
	    		    if ( sijainti != null ) {
	    		       
	    		        //Koska poikkeama mitataan itsestä keskipisteeseen, pitää ottaa vastaluku.
	    		        int etaisyysX = -( int )Math.round( sijainti.kerroMatkaX( sijaintiX, ind ) );
	    		        int etaisyysY = -( int )Math.round( sijainti.kerroMatkaY( sijaintiY, jnd ) );
	    		        
	    		        //Tutkitaan tarvitseeko piirtää...
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
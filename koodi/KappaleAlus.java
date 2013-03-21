
/**
 * 
 * Kuvaa kappaletta, joka on ohjattava, jolla on ase, ja joka voi r‰j‰ht‰‰.
 * Aluksella on tietty energia. Kun alus saa osumia ammuksista, sen energia
 * laskee. Kun energia saavuttaa nollan, alus tuhoutuu ja r‰j‰ht‰‰.
 *
 * @author Jaakko Luttinen
 *
 */
public class KappaleAlus extends KappaleOhjattava {
    
    //Aluksen ominaisuuksia kuvaavat muuttujat
    
    /**
     * Aluksen maksimienergia.
     */
	private int maksimienergia;
	
	/**
	 * Aluksen ase.
	 */
	private Ase paaase;
	
	/**
	 * Malli aluksen r‰j‰hdyksest‰.
	 */
	private KappaleRajahdys rajahdysmalli;

	//Aluksen tilaa kuvaavat muuttujat
	
	/**
	 * Aluksen hetkellinen energia.
	 */
	private double energia;

	/**
	 * Luo uuden aluksen.
	 * @param tyyppi Kappaleen merkkijonotunniste. Voi olla null.
	 * @param kuva Kappaleen kuvasarja.
	 * @param kiihtyvyys Kappaleen kiihtyvyys.
	 * @param maksiminopeus Kappaleen maksiminopeus.
	 * @param kaantonopeus Kappaleen k‰‰ntˆnopeus.
	 * @param paaase Aluksen ase.
	 * @param rajahdysmalli Aluksen r‰j‰hdysnopeus.
	 * @param maksimienergia Aluksen maksimienergia.
	 * @throws Exception Poikkeus kertoo, ett‰ annetuissa parametreissa oli virhe.
	 */
	public KappaleAlus( String tyyppi, Kuvasarja kuva, double kiihtyvyys, double maksiminopeus, double kaantonopeus, Ase paaase, KappaleRajahdys rajahdysmalli, int maksimienergia ) throws Exception {
		
		super( tyyppi, kuva, kiihtyvyys, maksiminopeus, kaantonopeus );
		
		if ( paaase == null )
		    throw new Exception( "Alusta ei voi luoda: P‰‰asetta ei ole m‰‰ritetty." );
		
		if ( rajahdysmalli == null )
		    throw new Exception( "Alusta ei voi luoda: R‰j‰hdysmallia ei ole m‰‰ritetty." );
		
		if ( maksimienergia < 1 )
		    throw new Exception( "Alusta ei voi luoda: Maksimienergian oltava v‰hint‰‰n 1." );
		
		this.paaase = new Ase( paaase );
		this.rajahdysmalli = rajahdysmalli;
		this.maksimienergia = maksimienergia;
		this.asetaTormattavyys( true );
		
	}
	
	/**
	 * Luo uuden aluksen annetun kopion perusteella.
	 * @param kopio Alus, jonka perusteella uusi alus luodaan.
	 */
	public KappaleAlus( KappaleAlus kopio ) {
	    
	    super( kopio );
		this.paaase = new Ase( kopio.paaase );
		this.rajahdysmalli = kopio.rajahdysmalli;
		this.maksimienergia = kopio.maksimienergia;
		this.asetaTormattavyys( true );
	    
	}
	
	/**
	 * Kertoo aluksen aseen.
	 * @return Aluksen ase.
	 */
	public Ase kerroPaaase() {
	    
	    return this.paaase;
	    
	}
	
	/**
	 * Kertoo aluksen r‰j‰hdysmallin.
	 * @return Aluksen r‰j‰hdysmalli.
	 */
	public KappaleRajahdys kerroRajahdysmalli() {
	    
	    return this.rajahdysmalli;
	    
	}

	/**
	 * Kertoo aluksen maksimienergian.
	 * @return Aluksen maksimienergia.
	 */
	public int kerroMaksimienergia() {
	    
	    return this.maksimienergia;
	    
	}
	
	/**
	 * Kertoo aluksen energian kyseisell‰ hetkell‰.
	 * @return Aluksen energia.
	 */
	public double kerroEnergia() {
	    
	    return this.energia;
	    
	}
	
	/**
	 * Asettaa aluksen liipaisimen.
	 * @param ammutaan Totuusarvo, joka kertoo, onko liipaisin pohjassa.
	 */
	public void asetaLiipaisin( boolean ammutaan ) {
	    
	    this.paaase.asetaLiipaisin( ammutaan );
	    
	}
	
	public boolean suoritaOsuma( RajapintaTuhoatekeva tuhonLahde ) {
	    
	    this.energia -= tuhonLahde.kerroTuhovoima();
	    
	    if ( this.energia < 0 ) {
	        
	        this.energia = 0.0;
	        this.rajahda();
	        this.tuhoudu();
	        
	    }

	    return true;
	    
	}
	
	//Tehd‰‰n t‰st‰ oma metodi, jotta alaluokatkin saavat tiedon, milloin
	//alus r‰j‰ht‰‰ ja voivat muuttaa toiminnan, jos haluavat.
	/**
	 * Yritt‰‰ luoda r‰j‰hdysmallin mukaisen r‰j‰hdyksen aluksen sijaintiin, mutta jos se
	 * ep‰onnistuu, ei metodi tee mit‰‰n.
	 */
	public void rajahda() {
	    
	    try {
	        
	        ( new KappaleRajahdys( this.rajahdysmalli ) ).asetaMaailmaan( this.kerroMaailma(),
	                													  this.kerroSijainti().kerroSijaintiX(),
	                													  this.kerroSijainti().kerroSijaintiY() );
	    
	    }
	    catch ( Exception virhe ) {  }
	    
	}
	
	public void asetaMaailmaan( Avaruus maailma, double sijaintiX, double sijaintiY ) throws Exception {
	    
	    this.energia = this.maksimienergia;
	    super.asetaMaailmaan( maailma, sijaintiX, sijaintiY );
	    
	}
	
	/**
	 * Suorittaa ohjattavan kappaleen tilan muutokset ja antaa aseensa toimia.
	 * Jos ase laukaisee ammuksen, laittaa sen itsens‰ alle maailmaan.
	 * @param aika Syk‰yksen aika.
	 */
	protected void toimi( int aika ) {
		
	    //Aluksen liiketilan muutokset.
	    super.toimi( aika );
	
	    //Muut toimenpiteet: ampuminen.
		KappaleAmmus uusiAmmus = this.paaase.toimi( aika );
		
		if ( uusiAmmus != null ) {
		    
		    try {
		        
		        //Yritet‰‰n laittaa ammus maailmaan.
		        uusiAmmus.asetaAmpuja( this );
		        uusiAmmus.asetaMaailmaan( this.kerroMaailma(), this.kerroSijainti().kerroSijaintiX(), this.kerroSijainti().kerroSijaintiY() );
		        uusiAmmus.asetaNopeus( this.kerroNopeus().kerroX() + this.kerroNokanSuunta().kosini() * uusiAmmus.kerroLahtonopeus(),
		                			   this.kerroNopeus().kerroY() + this.kerroNokanSuunta().sini() * uusiAmmus.kerroLahtonopeus() );
		        
		    }
		    catch ( Exception virhe ) {  }
		    
		}
		
	}
    
}
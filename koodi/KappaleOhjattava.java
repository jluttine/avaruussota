
/**
 * 
 * Luokka <CODE>KappaleOhjattava</CODE> kuvaa liikkuvaa kappaletta, jolla on kiihtyvyys
 * ja ohjaus. Ohjaus tarkoittaa, että kappaletta pyöritetään myötä- tai vastapäivään tai
 * sitä pidetään pyörittämättä. Kaasu tarkoittaa, että kappaletta kiihdytetään nokan
 * osoittamaan suuntaan. Kappaleen pyörimisessäkin on hyödynnetty kiihtyvyyttä ja
 * maksimikääntönopeutta. Kiihtyvyys on kuitenkin sen verran suuri, että maksimikääntönopeus
 * saavutetaan nopeasti. Tämän vuoksi ulkopuolisten ei siihen tarvitse suuremmin
 * kiinnittää huomiota. Se vain antaa pientä pehmeyttä ja tarkkuutta kappaleen
 * teräviin ohjausliikkeisiin.
 *
 * @author Jaakko Luttinen
 *
 */

public class KappaleOhjattava extends KappaleLiikkuva {

    /**
     * Vakio, joka kuvaa suuntaa vastapäivään.
     */
	public static int SUUNTA_VASTAPAIVAAN = -1;
	
	/**
	 * Vakio, joka kuvaa suuntaa suoraan eteenpäin.
	 */
	public static int SUUNTA_SUORAAN = 0;
	
	/**
	 * Vakio, joka kuvaa suuntaa myötäpäivään.
	 */
	public static int SUUNTA_MYOTAPAIVAAN = 1;
	
	//Kappaleen ominaisuuksia kuvaavat muuttujat
	
	/**
	 * Kappaleen kiihtyvyys (yksikkö pikseliä/ms^2).
	 */
	private double kiihtyvyys;
	
	/**
	 * Kappaleen maksiminopeus.
	 */
	private double maksiminopeus;
	
	/**
	 * Kappaleen kääntökiihtyvyys (yksikkö rad/ms^2).
	 */
	private double kaantokiihtyvyys;
	
	/**
	 * Kappaleen maksimikääntönopeus. Käytännössä nopeus, jolla alus pyörii (rad/ms).
	 */
	private double maksimikaantonopeus;

	//Kappaleen tilaa kuvaavat muuttujat
	
	/**
	 * Kappaleen hetkellinen kääntönopeus. 
	 */
	private double kaantonopeus;
	
	/**
	 * Totuusarvo, joka kertoo, onko kaasu pohjassa.
	 */
	private boolean kaasuPohjassa;
	
	/**
	 * Kappaleen nokan suunta.
	 */
	private Kulma nokanSuunta;
	
	/**
	 * Kappaleen ohjauksen tila (myötä-/vastapäivään/suoraan).
	 */
	private int ohjausSuunta;
	
	/**
	 * Apumuuttuja törmäyksissä. Totuusarvo, joka kertoo, että kappale on ollut
	 * törmäyksessä, jossa aluksen nopeus on muuttunut, joten ensi sykäyksellä
	 * alusta ei kiihdytetä. Tällä apumuuttujalla saadaan estettyä muutamia likiarvoista 
	 * johtuvia poikkeustilanteita.
	 */
	private boolean eiKiihdyteta;

	/**
	 * Luo uuden ohjattavan kappaleen.
	 * @param tyyppi Kappaleen merkkijonotunniste. Voi olla null.
	 * @param kuva Kappaleen kuvasarja.
	 * @param kiihtyvyys Kappaleen kiihtyvyys (pikseliä/ms^2).
	 * @param maksiminopeus Kappaleen maksiminopeus (pikseliä/ms).
	 * @param maksimikaantonopeus Kappaleen maksimikääntönopeus (rad/ms).
	 * @throws Exception Poikkeus kertoo, että annetuissa parametreissa oli virhe.
	 */
	public KappaleOhjattava( String tyyppi, Kuvasarja kuva, double kiihtyvyys, double maksiminopeus, double maksimikaantonopeus ) throws Exception {
		
		super( tyyppi, kuva );
		
		if ( maksiminopeus <= 0 )
		    throw new Exception( "Ohjattavaa kappaletta ei voi luoda: Maksiminopeus oltava positiivinen." );
		
		if ( kiihtyvyys <= 0 )
		    throw new Exception( "Ohjattavaa kappaletta ei voi luoda: Kiihtyvyys oltava positiivinen." );
		
		if ( maksimikaantonopeus <= 0 )
		    throw new Exception( "Ohjattavaa kappaletta ei voi luoda: Maksimikääntönopeus oltava positiivinen." );
		
		this.maksiminopeus = maksiminopeus;
		this.kiihtyvyys = kiihtyvyys;
		this.maksimikaantonopeus = maksimikaantonopeus;
		this.nokanSuunta = new Kulma( 0 );
		
		//Näillä kahdella muuttujalla saadaan lyhyet pienet ohjaukset tarkemmiksi.
		//Kääntökiihtyvyys tekee ihmeitä ohjauksen hienosäädölle.
		this.kaantonopeus = 0;
		this.kaantokiihtyvyys = this.maksimikaantonopeus / 150;
		
	}
	
	/**
	 * Luo uuden ohjattavan kappaleen annetun ohjattavan kappaleen perusteella.
	 * @param kopio Ohjattava kappale, jonka perusteella uusi kappale luodaan.
	 */
	public KappaleOhjattava( KappaleOhjattava kopio ) {
	    
	    super( kopio );
		this.maksiminopeus = kopio.maksiminopeus;
		this.kiihtyvyys = kopio.kiihtyvyys;
		this.maksimikaantonopeus = kopio.maksimikaantonopeus;
		this.nokanSuunta = new Kulma( 0 );
		
		//Näillä kahdella muuttujalla saadaan lyhyet pienet ohjaukset tarkemmiksi.
		//Kääntökiihtyvyys tekee ihmeitä ohjauksen hienosäädölle.
		this.kaantonopeus = 0;
		this.kaantokiihtyvyys = this.maksimikaantonopeus / 150;
	    
	}
	
	/**
	 * Kertoo kappaleen kiihtyvyyden.
	 * @return Kappaleen kiihtyvyys (pikseliä/ms^2).
	 */
	public double kerroKiihtyvyys() {
	    
	    return this.kiihtyvyys;
	    
	}
	
	/**
	 * Kertoo kappaleen maksiminopeuden.
	 * @return Kappaleen maksiminopeus (pikseliä/ms).
	 */
	public double kerroMaksiminopeus() {
	    
	    return this.maksiminopeus;
	    
	}
	
	/**
	 * Kertoo kappaleen kääntönopeuden.
	 * @return Kappaleen kääntönopeus (rad/ms).
	 */
	public double kerroKaantonopeus() {
	    
	    return this.maksimikaantonopeus;
	    
	}
	
	/**
	 * Kertoo kappaleen ohjauksen (vasta-, myötäpäivään tai suoraan, ks. vakiot).
	 * @return Ohjauksen suunta.
	 */
	public int kerroOhjaus() {
	    
	    return this.ohjausSuunta;
	    
	}
	
	/**
	 * Kertoo, onko kappaleen kaasu pohjassa.
	 * @return Totuusarvo, joka kertoo, onko kaasu pohjassa.
	 */
	public boolean kerroKaasu() {
	    
	    return this.kaasuPohjassa;
	    
	}
	
	/**
	 * Kertoo kappaleen nokan suunnan.
	 * @return Kappaleen nokan suunta.
	 */
	public Kulma kerroNokanSuunta() {
	    
	    return this.nokanSuunta;
	    
	}
	
	/**
	 * Asettaa kappaleen ohjauksen suunnan. Jos annettu arvo ei ole mikään suuntia
	 * määrävistä vakioista (ks. luokan vakiot), ei metodi tee mitään.
	 * @param suunta Kappaleen ohjauksen suunta.
	 */
	public void asetaOhjaus( int suunta ) {
		
		if ( suunta == KappaleOhjattava.SUUNTA_VASTAPAIVAAN ||
		     suunta == KappaleOhjattava.SUUNTA_MYOTAPAIVAAN ||
		     suunta == KappaleOhjattava.SUUNTA_SUORAAN ) {
		    
			this.ohjausSuunta = suunta;
			
		}
		
	}
	
	/**
	 * Asettaa kappaleen kaasun tilan.
	 * @param kaasuPohjassa Totuusarvo, joka kertoo, onko kaasu pohjassa.
	 */
	public void asetaKaasu( boolean kaasuPohjassa ) {
		
		this.kaasuPohjassa = kaasuPohjassa;
		
	}
	
	protected void muutaNopeusTormayksenJalkeen( double muutosX, double muutosY ) {
	    
	    super.muutaNopeusTormayksenJalkeen( muutosX, muutosY );
	    this.eiKiihdyteta = true;
	    
	}
	
	public int kerroFrame() {
	    
	    int frameja = this.kerroKuva().kerroKuvienMaara();
	    return ( int )( frameja * ( this.nokanSuunta.kerroSuhteellinenKulma( Math.PI / frameja ) / ( 2 * Math.PI ) ) );
	    
	}
	
	/**
	 * Pyörittää kappaletta. Kiihdyttää pyörimisnopeutta ja pyöräyttää kappaletta
	 * ajan kuluman ja pyörimisnopeuden määrittämän määrän.
	 * @param aika Aika (ms), jonka verran kappaletta pyöritetään.
	 */
	private void suoritaPyoriminen( long aika ) {
	    
	    //Jos ruvetaan kääntämään vastakkaiseen suuntaan, ei tehdä mitään kiihdytystä, vaan
	    //pysäytetään pyöriminen ja aletaan kääntää sitten toiseen suuntaan. Kiihtyvyyden
	    //tarkoitushan on vain tuoda pehmeyttä ja tarkkuutta nopeisiin, alkaviin ohjauksiin.
	    if ( this.kaantonopeus != 0 ) {
	        
	        if ( this.ohjausSuunta / this.kaantonopeus <= 0 )
	            this.kaantonopeus = 0;
	            
	    }
	    
	    //Kasvatetaan kääntönopeutta.
	    this.kaantonopeus += this.ohjausSuunta * aika * this.kaantokiihtyvyys;
	    
	    //Estetään maksimikääntönopeuden ylittäminen.
	    if ( Math.abs( this.kaantonopeus ) > this.maksimikaantonopeus )
	        this.kaantonopeus = this.ohjausSuunta * this.maksimikaantonopeus;
	    
	    //Pyöritetään kappaletta.
		this.nokanSuunta.muuta( aika * this.kaantonopeus );
	    
	}

	/**
	 * Korjaa kappaleen nopeuden korkeintaan maksiminopeuden suuruiseksi.
	 */
	private void korjaaNopeus() {

	    //Ensin karkea suuruusvertailu.
	    if ( Math.abs( this.kerroNopeus().kerroX() ) + Math.abs( this.kerroNopeus().kerroY() ) > this.maksiminopeus ) {
	        
	        //Sitten tarkka suhde.
	        double suhde = this.maksiminopeus / this.kerroNopeus().kerroPituus();
	        
	        if ( suhde < 1.0 )
	            this.kerroNopeus().skaalaa( suhde );
	        
	    }
	    
	}

	public void tormayksetTutkittu() {
	    
	    super.tormayksetTutkittu();
	    this.korjaaNopeus();
	    
	}

	public void tuhoudu() {
	    
	    this.kaantonopeus = 0.0;
	    this.nokanSuunta.aseta( 0 );
	    super.tuhoudu();
	    
	}
	
	/**
	 * Pyörittää alusta ohjauksen mukaan ja muuttaa aluksen nopeutta kaasun ja nokan suunnan
	 * mukaan, ja liikuttaa lopuksi alusta.
	 * @param aika Sykäyksen aika.
	 */
	protected void toimi( int aika ) {
		
	    //Pyöritetään alusta ohjauksen mukaan.
		this.suoritaPyoriminen( aika );
	    
		//Kiihdytetään alusta, jos kaasu on pohjassa ja ei-kiihdytetä-lippua ei ole nostettu.
		if ( this.kaasuPohjassa && !this.eiKiihdyteta ) {
		    
		    this.kerroNopeus().muuta( this.nokanSuunta.kosini() * aika * this.kiihtyvyys,
		            				  this.nokanSuunta.sini() * aika * this.kiihtyvyys );
		    
		    //Hidastetaan vauhtia, jos nopeus yli maksiminopeuden.
		    this.korjaaNopeus();
		    
		}
		
		//Lasketaan ei-kiihdytetä-lippu.
		this.eiKiihdyteta = false;

		//Liikutetaan alusta.
		super.toimi( aika );
		
	}
	
}
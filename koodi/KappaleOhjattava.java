
/**
 * 
 * Luokka <CODE>KappaleOhjattava</CODE> kuvaa liikkuvaa kappaletta, jolla on kiihtyvyys
 * ja ohjaus. Ohjaus tarkoittaa, ett� kappaletta py�ritet��n my�t�- tai vastap�iv��n tai
 * sit� pidet��n py�ritt�m�tt�. Kaasu tarkoittaa, ett� kappaletta kiihdytet��n nokan
 * osoittamaan suuntaan. Kappaleen py�rimisess�kin on hy�dynnetty kiihtyvyytt� ja
 * maksimik��nt�nopeutta. Kiihtyvyys on kuitenkin sen verran suuri, ett� maksimik��nt�nopeus
 * saavutetaan nopeasti. T�m�n vuoksi ulkopuolisten ei siihen tarvitse suuremmin
 * kiinnitt�� huomiota. Se vain antaa pient� pehmeytt� ja tarkkuutta kappaleen
 * ter�viin ohjausliikkeisiin.
 *
 * @author Jaakko Luttinen
 *
 */

public class KappaleOhjattava extends KappaleLiikkuva {

    /**
     * Vakio, joka kuvaa suuntaa vastap�iv��n.
     */
	public static int SUUNTA_VASTAPAIVAAN = -1;
	
	/**
	 * Vakio, joka kuvaa suuntaa suoraan eteenp�in.
	 */
	public static int SUUNTA_SUORAAN = 0;
	
	/**
	 * Vakio, joka kuvaa suuntaa my�t�p�iv��n.
	 */
	public static int SUUNTA_MYOTAPAIVAAN = 1;
	
	//Kappaleen ominaisuuksia kuvaavat muuttujat
	
	/**
	 * Kappaleen kiihtyvyys (yksikk� pikseli�/ms^2).
	 */
	private double kiihtyvyys;
	
	/**
	 * Kappaleen maksiminopeus.
	 */
	private double maksiminopeus;
	
	/**
	 * Kappaleen k��nt�kiihtyvyys (yksikk� rad/ms^2).
	 */
	private double kaantokiihtyvyys;
	
	/**
	 * Kappaleen maksimik��nt�nopeus. K�yt�nn�ss� nopeus, jolla alus py�rii (rad/ms).
	 */
	private double maksimikaantonopeus;

	//Kappaleen tilaa kuvaavat muuttujat
	
	/**
	 * Kappaleen hetkellinen k��nt�nopeus. 
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
	 * Kappaleen ohjauksen tila (my�t�-/vastap�iv��n/suoraan).
	 */
	private int ohjausSuunta;
	
	/**
	 * Apumuuttuja t�rm�yksiss�. Totuusarvo, joka kertoo, ett� kappale on ollut
	 * t�rm�yksess�, jossa aluksen nopeus on muuttunut, joten ensi syk�yksell�
	 * alusta ei kiihdytet�. T�ll� apumuuttujalla saadaan estetty� muutamia likiarvoista 
	 * johtuvia poikkeustilanteita.
	 */
	private boolean eiKiihdyteta;

	/**
	 * Luo uuden ohjattavan kappaleen.
	 * @param tyyppi Kappaleen merkkijonotunniste. Voi olla null.
	 * @param kuva Kappaleen kuvasarja.
	 * @param kiihtyvyys Kappaleen kiihtyvyys (pikseli�/ms^2).
	 * @param maksiminopeus Kappaleen maksiminopeus (pikseli�/ms).
	 * @param maksimikaantonopeus Kappaleen maksimik��nt�nopeus (rad/ms).
	 * @throws Exception Poikkeus kertoo, ett� annetuissa parametreissa oli virhe.
	 */
	public KappaleOhjattava( String tyyppi, Kuvasarja kuva, double kiihtyvyys, double maksiminopeus, double maksimikaantonopeus ) throws Exception {
		
		super( tyyppi, kuva );
		
		if ( maksiminopeus <= 0 )
		    throw new Exception( "Ohjattavaa kappaletta ei voi luoda: Maksiminopeus oltava positiivinen." );
		
		if ( kiihtyvyys <= 0 )
		    throw new Exception( "Ohjattavaa kappaletta ei voi luoda: Kiihtyvyys oltava positiivinen." );
		
		if ( maksimikaantonopeus <= 0 )
		    throw new Exception( "Ohjattavaa kappaletta ei voi luoda: Maksimik��nt�nopeus oltava positiivinen." );
		
		this.maksiminopeus = maksiminopeus;
		this.kiihtyvyys = kiihtyvyys;
		this.maksimikaantonopeus = maksimikaantonopeus;
		this.nokanSuunta = new Kulma( 0 );
		
		//N�ill� kahdella muuttujalla saadaan lyhyet pienet ohjaukset tarkemmiksi.
		//K��nt�kiihtyvyys tekee ihmeit� ohjauksen hienos��d�lle.
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
		
		//N�ill� kahdella muuttujalla saadaan lyhyet pienet ohjaukset tarkemmiksi.
		//K��nt�kiihtyvyys tekee ihmeit� ohjauksen hienos��d�lle.
		this.kaantonopeus = 0;
		this.kaantokiihtyvyys = this.maksimikaantonopeus / 150;
	    
	}
	
	/**
	 * Kertoo kappaleen kiihtyvyyden.
	 * @return Kappaleen kiihtyvyys (pikseli�/ms^2).
	 */
	public double kerroKiihtyvyys() {
	    
	    return this.kiihtyvyys;
	    
	}
	
	/**
	 * Kertoo kappaleen maksiminopeuden.
	 * @return Kappaleen maksiminopeus (pikseli�/ms).
	 */
	public double kerroMaksiminopeus() {
	    
	    return this.maksiminopeus;
	    
	}
	
	/**
	 * Kertoo kappaleen k��nt�nopeuden.
	 * @return Kappaleen k��nt�nopeus (rad/ms).
	 */
	public double kerroKaantonopeus() {
	    
	    return this.maksimikaantonopeus;
	    
	}
	
	/**
	 * Kertoo kappaleen ohjauksen (vasta-, my�t�p�iv��n tai suoraan, ks. vakiot).
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
	 * Asettaa kappaleen ohjauksen suunnan. Jos annettu arvo ei ole mik��n suuntia
	 * m��r�vist� vakioista (ks. luokan vakiot), ei metodi tee mit��n.
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
	 * Py�ritt�� kappaletta. Kiihdytt�� py�rimisnopeutta ja py�r�ytt�� kappaletta
	 * ajan kuluman ja py�rimisnopeuden m��ritt�m�n m��r�n.
	 * @param aika Aika (ms), jonka verran kappaletta py�ritet��n.
	 */
	private void suoritaPyoriminen( long aika ) {
	    
	    //Jos ruvetaan k��nt�m��n vastakkaiseen suuntaan, ei tehd� mit��n kiihdytyst�, vaan
	    //pys�ytet��n py�riminen ja aletaan k��nt�� sitten toiseen suuntaan. Kiihtyvyyden
	    //tarkoitushan on vain tuoda pehmeytt� ja tarkkuutta nopeisiin, alkaviin ohjauksiin.
	    if ( this.kaantonopeus != 0 ) {
	        
	        if ( this.ohjausSuunta / this.kaantonopeus <= 0 )
	            this.kaantonopeus = 0;
	            
	    }
	    
	    //Kasvatetaan k��nt�nopeutta.
	    this.kaantonopeus += this.ohjausSuunta * aika * this.kaantokiihtyvyys;
	    
	    //Estet��n maksimik��nt�nopeuden ylitt�minen.
	    if ( Math.abs( this.kaantonopeus ) > this.maksimikaantonopeus )
	        this.kaantonopeus = this.ohjausSuunta * this.maksimikaantonopeus;
	    
	    //Py�ritet��n kappaletta.
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
	 * Py�ritt�� alusta ohjauksen mukaan ja muuttaa aluksen nopeutta kaasun ja nokan suunnan
	 * mukaan, ja liikuttaa lopuksi alusta.
	 * @param aika Syk�yksen aika.
	 */
	protected void toimi( int aika ) {
		
	    //Py�ritet��n alusta ohjauksen mukaan.
		this.suoritaPyoriminen( aika );
	    
		//Kiihdytet��n alusta, jos kaasu on pohjassa ja ei-kiihdytet�-lippua ei ole nostettu.
		if ( this.kaasuPohjassa && !this.eiKiihdyteta ) {
		    
		    this.kerroNopeus().muuta( this.nokanSuunta.kosini() * aika * this.kiihtyvyys,
		            				  this.nokanSuunta.sini() * aika * this.kiihtyvyys );
		    
		    //Hidastetaan vauhtia, jos nopeus yli maksiminopeuden.
		    this.korjaaNopeus();
		    
		}
		
		//Lasketaan ei-kiihdytet�-lippu.
		this.eiKiihdyteta = false;

		//Liikutetaan alusta.
		super.toimi( aika );
		
	}
	
}


/** 
 * 
 * Luokka <CODE>Koordinaatit<CODE> kuvaa sijaintia jossakin vakiokokoisessa
 * koordinaatistossa. Koordinaatistoa k‰sitell‰‰n jatkuvana sek‰ pysty- ett‰
 * vaakasuunnassa, joten kaikki x- ja y-koordinaattien arvot ovat palautettavissa
 * perusv‰lille 0 - leveys/korkeus. Jatkuvuusperiaate tarkoittaa k‰yt‰nnˆss‰ sit‰,
 * ett‰ x0 on sama kuin x0+n*leveys ja y0 on sama kuin y0+m*korkeus, miss‰ n ja m
 * ovat joitakin kokonaislukuja.
 * Vertailuja eri sijaintien kesken voidaan suorittaa kuitenkin jos ja vain jos
 * molemmat sijainnit ovat samassa koordinaatistossa.
 * 
 * @author Jaakko Luttinen
 *
 */

public class Koordinaatit {
	
    /**
     * Vakio, joka kuvaa suuntaa negatiiviseen suuntaan. Se on tietokoneen
     * koordinaateissa vasemmalle tai ylˆs.
     */
	public static int SUUNTA_NEGATIIVINEN = -1;
	
	/**
	 * Vakio, joka kuvaa suuntaa positiiviseen suuntaan. Se on tietokoneen
	 * koordinaateissa oikealle tai alas.
	 */
	public static int SUUNTA_POSITIIVINEN = 1;
	
	/**
	 * Koordinatisto, jossa t‰m‰ sijainti on.
	 */
    private Koordinaatisto koordinaatisto;
    
    /**
     * Sijainnin x-koordinaatti.
     */
	private double sijaintiX;
	
	/**
	 * Sijainnin y-koordinaatti.
	 */
	private double sijaintiY;
	
	/** 
	 * Muuttaa annetun luvun v‰lille 0 - maksimi jatkuvuusperiaatteella.
	 * 
	 * @param sijainti Lukuarvo, joka halutaan muuttaa.
	 * @param maksimi Positiivinen kokonaisluku, joka kertoo v‰lin yl‰rajan.
	 * @return V‰lille 0-maksimi korjattu lukuarvo.
	 */
	private static double korjaaKoordinaatti( double sijainti, int maksimi ) {
	    
	    sijainti %= maksimi;
	    
	    //Negatiivisen koordinaatin jakoj‰‰nnˆs saadaan oikeaksi
	    //seuraavalla lis‰yksell‰. (-9%7=-2 --> -2+7=5)
	    if ( sijainti < 0 )
	        sijainti += maksimi;
	    
	    return sijainti;
	    
	}
	
	/**
	 * Muuttaa koordinaatit v‰lille 0 - leveys/korkeus.
	 * 
	 * @param sijainti Koordinaatit-olio, joka halutaan korjata.
	 */
	private static void korjaaKoordinaatit( Koordinaatit sijainti ) {
	    
	    sijainti.sijaintiX = Koordinaatit.korjaaKoordinaatti( sijainti.sijaintiX,
	            											  sijainti.koordinaatisto.kerroLeveys() );
	    
	    sijainti.sijaintiY = Koordinaatit.korjaaKoordinaatti( sijainti.sijaintiY,
	            											  sijainti.koordinaatisto.kerroKorkeus() );
	    
	}

	/**
	 * Kertoo lyhimm‰n matkan koordinaatista <CODE>alkuSijainti</CODE> koordinaattiin
	 * <CODE>loppuSijainti</CODE> koordinaattiakselilla, jonka pituus on
	 * <CODE>maksimi</CODE>. Palautetun lyhimm‰n matkan etumerkki kertoo samalla
	 * myˆs suunnan: positiivinen -> kasvavaan suuntaan, negatiivinen -> v‰henev‰‰n
	 * suuntaan. 
	 * @param alkuSijainti Koordinaatti, josta lyhin matka mitataan. 
	 * @param loppuSijainti Koordinaatti, johon lyhin matka mitataan.
	 * @param maksimi Koordinaattiakselin pituus.
	 * @return Lyhin matka alkupisteest‰ loppupisteeseen. Etumerkki kertoo suunnan.
	 */
	private static double kerroLyhinMatka( double alkuSijainti, double loppuSijainti, int maksimi ) {
	    
	    double matka1 = loppuSijainti - alkuSijainti;
        double matka2 = matka1 - maksimi;
	    
	    if ( matka1 < 0 )
	        matka2 = matka1 + maksimi;
	    
	    if ( Math.abs( matka2 ) < Math.abs( matka1 ) )
	        return matka2;
	    
	    return matka1;
	    
	}

	/**
	 * Kertoo matkan <CODE>alkuSijainnista</CODE> <CODE>loppuSijaintiin</CODE> annettuun
	 * suuntaan koordinaattiakselilla, jonka pituus on <CODE>maksimi</CODE>.
	 * @param alkuSijainti L‰htˆpisteen koordinaatti.
	 * @param loppuSijainti M‰‰r‰np‰‰pisteen koordinaatti.
	 * @param maksimi Koordinaattiakselin pituus.
	 * @param suunta Luvun etumerkki kertoo, tutkitaanko positiiviseen vai negatiiviseen
	 * suuntaan. Nolla tarkoittaa positiivista suuntaa.
	 * @return Pisteiden v‰linen matka annettuun suuntaan. Etumerkki kertoo suunnan.
	 */
	private static double kerroMatka( double alkuSijainti, double loppuSijainti, int maksimi, int suunta ) {
		
		if ( ( loppuSijainti - alkuSijainti ) / suunta >= 0 )
			return loppuSijainti - alkuSijainti;
		
		return ( loppuSijainti - alkuSijainti ) + suunta * maksimi;
		
	}
	
	/**
	 * Kertoo, ovatko koordinaatit vertailukelpoisia. So. molemmat ovat eri suuria kuin null ja
	 * sijaitsevat samassa koordinaatistossa.
	 * @param sijainti1 Toiset koordinaatit.
	 * @param sijainti2 Toiset koordinaatit.
	 * @throws Exception Poikkeus kertoo, ett‰ koordinaatit eiv‰t ole vertailukelpoisia.
	 */
	public static void tutkiVertailukelpoisuus( Koordinaatit sijainti1, Koordinaatit sijainti2 ) throws Exception {
	    
	    if ( sijainti1 == null || sijainti2 == null )
	        throw new Exception( "Sijainnit eiv‰t ole vertailukelpoiset: Sijainteja ei ole m‰‰ritetty." );
	    
	    if ( sijainti1.koordinaatisto != sijainti2.koordinaatisto )
	        throw new Exception( "Sijainnit eiv‰t ole vertailukelpoiset: Sijainnit eri koordinaatistoissa." );
	    
	}
	
	/**
	 * Kertoo, onko <CODE>tutkittava</CODE>-koordinaatti koordinaattien <CODE>alku</CODE>
	 * ja <CODE>loppu</CODE> v‰liss‰. Metodi olettaa, ett‰ koordinaatit on jo korjattu
	 * perusv‰lilleen. V‰lin p‰‰tepisteet kuuluvat v‰lille ja v‰li‰ pidet‰‰n v‰lin‰
	 * alkupisteest‰ koordinaattiakselin kasvavaan suuntaan loppupisteeseen ulottuvana v‰lin‰.
	 * @param tutkittava Koordinaatti, jonka v‰liss‰oloa tutkitaan.
	 * @param alku V‰lin alkupisteen koordinaatti.
	 * @param loppu V‰lin loppupisteen koordinaatti.
	 * @return Totuusarvo, joka kertoo, onko tutkittava koordinaatti annetulla v‰lill‰.
	 */
	private static boolean onValissa( double tutkittava, double alku, double loppu ) {
	    
	    if ( alku == loppu )
	        return tutkittava == alku;
	    
	    if ( alku < loppu )
	        return ( tutkittava >= alku ) && ( tutkittava <= loppu );
	    
	    return ( tutkittava >= alku ) || ( tutkittava <= loppu );
	    
	}
	
	/**
	 * Luo uuden sijainnin annetuilla koordinaateilla annettuun koordinaatistoon.
	 * @param koordinaatisto Koordinaatisto, jossa koordinaatit sijaitsevat.
	 * @param sijaintiX Sijainnin x-koordinaatti.
	 * @param sijaintiY Sijainnin y-koordinaatti.
	 * @throws Exception Poikkeus kertoo, ettei koordinaatistoa ollut m‰‰ritetty.
	 */
	public Koordinaatit( Koordinaatisto koordinaatisto, double sijaintiX, double sijaintiY ) throws Exception {
		
	    if ( koordinaatisto == null )
	        throw new Exception( "Koordinaatteja ei voi luoda: Koordinaatistoa ei ole m‰‰ritetty." );
	    
	    this.koordinaatisto = koordinaatisto;
		this.sijaintiX = sijaintiX;
		this.sijaintiY = sijaintiY;
		Koordinaatit.korjaaKoordinaatit( this );
		
	}

	/**
	 * Luo uuden sijainnin annetun sijainnin perusteella.
	 * @param kopio Sijainti, jonka perusteella uusi sijainti luodaan.
	 */
	public Koordinaatit( Koordinaatit kopio ) {
	    
	    this.koordinaatisto = kopio.koordinaatisto;
		this.sijaintiX = kopio.sijaintiX;
		this.sijaintiY = kopio.sijaintiY;
		Koordinaatit.korjaaKoordinaatit( this );
	    
	}
	
	/**
	 * Kertoo koordinaatiston, jossa koordinaatit sijaitsevat.
	 * @return Koordinaatisto, jossa koordinaatit sijaitsevat.
	 */
	public Koordinaatisto kerroKoordinaatisto() {
	    
	    return this.koordinaatisto;
	    
	}
	
	/**
	 * Kertoo sijainnin x-koordinaatin.
	 * @return Sijainnin x-koordinaatti.
	 */
	public double kerroSijaintiX() {
		
		return this.sijaintiX;
		
	}
	
	/**
	 * Kertoo sijainnin y-koordinaatin.
	 * @return Sijainnin y-koordinaatti.
	 */
	public double kerroSijaintiY() {
		
		return this.sijaintiY;
		
	}
	
	/**
	 * Asettaa sijainnin koordinaatit. 
	 * @param sijaintiX Sijainnin uusi x-koordinaatti.
	 * @param sijaintiY Sijainnin uusi y-koordinaatti.
	 */
	public void asetaSijainti( double sijaintiX, double sijaintiY ) {
	    
	    this.sijaintiX = sijaintiX;
	    this.sijaintiY = sijaintiY;
	    Koordinaatit.korjaaKoordinaatit( this );
	    
	}

	/**
	 * Muuttaa sijainnin koordinaatteja.
	 * @param muutosX Sijainnin muutos x-koordinaatissa.
	 * @param muutosY Sijainnin muutos y-koordinaatissa.
	 */
	public void muuta( double muutosX, double muutosY ) {
		
		this.sijaintiX += muutosX;
		this.sijaintiY += muutosY;
		Koordinaatit.korjaaKoordinaatit( this );
		
	}
	
	/**
	 * Kertoo sijainnin, joka sijaitsee annetussa suhteellisessa sijainnissa.
	 * @param siirtymaX Sijainnin siirtym‰ x-koordinaatissa.
	 * @param siirtymaY Sijainnin siirtym‰ y-koordinaatissa.
	 * @return Suhteellinen sijainti.
	 */
	public Koordinaatit kerroSuhteellisetKoordinaatit( double siirtymaX, double siirtymaY ) {

	    //Luodaan uusi sijainti kopio-konstruktorilla, ettei tarvitse k‰sitell‰
	    //poikkeusta, jota ei kuitenkaan syntyisi.
		Koordinaatit uusiSijainti = new Koordinaatit( this );
		uusiSijainti.muuta( siirtymaX, siirtymaY );
		return uusiSijainti;
		
	}

	/**
	 * Kertoo lyhimm‰n matkan x-koordinaattiin <CODE>sijaintiX</CODE> x-suunnassa.
	 * Palautusarvon etumerkki kertoo suunnan.
	 * @param sijaintiX X-koordinaatti, johon lyhin matka halutaan selvitt‰‰.
	 * @return Lyhin matka x-suunnassa. Etumerkki kertoo suunnan.
	 */
	public double kerroLyhinMatkaX( double sijaintiX ) {
		
	    int leveys = this.koordinaatisto.kerroLeveys();
	    
	    //Korjataan t‰m‰ x-koordinaatti.
	    sijaintiX = Koordinaatit.korjaaKoordinaatti( sijaintiX, leveys );

	    return Koordinaatit.kerroLyhinMatka( this.sijaintiX, sijaintiX, leveys );
	    
	}
	
	/**
	 * Kertoo lyhimm‰n matkan y-koordinaattiin <CODE>sijaintiY</CODE> y-suunnassa.
	 * Palautusarvon etumerkki kertoo suunnan.
	 * @param sijaintiY Y-koordinaatti, johon lyhin matka halutaan selvitt‰‰.
	 * @return Lyhin matka y-suunnassa. Etumerkki kertoo suunnan.
	 */
	public double kerroLyhinMatkaY( double sijaintiY ) {

	    int korkeus = this.koordinaatisto.kerroKorkeus();
	    
	    //Korjataan t‰m‰ y-koordinaatti.
	    sijaintiY = Koordinaatit.korjaaKoordinaatti( sijaintiY, korkeus );
	    
	    return Koordinaatit.kerroLyhinMatka( this.sijaintiY, sijaintiY, korkeus );
		
	}
	
	/**
	 * Kertoo et‰isyyden annettuun sijaintiin. Ei tutki, ovatko sijainnit samasta
	 * koordinaatistosta, vaan j‰tt‰‰ sen kutsujan vastuulle. Koordinaatteja
	 * k‰sitell‰‰n kuin ne olisivat kutsutun olion koordinaatistossa.
	 * @param sijainti Sijainti, jonka et‰isyys m‰‰ritet‰‰n.
	 * @return Sijaintien v‰linen et‰isyys.
	 * @throws Exception Poikkeus kertoo, ett‰ koordinaatit ovat eri koordinaatistoista.
	 */
	public double kerroEtaisyys( Koordinaatit sijainti ) throws Exception {
	    
	    if ( this.koordinaatisto != sijainti.koordinaatisto )
	        throw new Exception( "Eri koordinaatistojen koordinaattien et‰isyyksi‰ ei voida m‰‰ritt‰‰." );
	    
	    //Pythagoranin avulla et‰isyys ratkeaa..
		return this.kerroEtaisyys( sijainti.sijaintiX, sijainti.sijaintiY );
	
	}
	
	/**
	 * Kertoo et‰isyyden annettuihin koordinaatteihin.
	 * @param sijaintiX X-koordinaatti.
	 * @param sijaintiY Y-koordinaatti.
	 * @return Et‰isyys annettuun sijaintiin.
	 */
	public double kerroEtaisyys( double sijaintiX, double sijaintiY ) {
	    
	    double matkaX = this.kerroLyhinMatkaX( sijaintiX );
	    double matkaY = this.kerroLyhinMatkaY( sijaintiY );
	    
	    //Pythagoranin avulla et‰isyys ratkeaa..
		return Math.sqrt( matkaX * matkaX + matkaY * matkaY );
	    
	}
	
	/**
	 * Kertoo et‰isyyden annettuun x-koordinaattiin annettuun suuntaan x-suunnassa.
	 * @param sijaintiX X-koordinaatti, johon et‰isyys halutaan m‰‰ritt‰‰.
	 * @param suunta Etumerkki kertoo, m‰‰ritet‰‰nkˆ et‰isyys positiiviseen vai
	 * negatiiviseen suuntaan. Nolla tarkoittaa positiivista suuntaa.
	 * @return Et‰isyys x-suunnassa annettuun suuntaan. Etumerkki kertoo suunnan.
	 */
	public double kerroMatkaX( double sijaintiX, int suunta ) {

		int leveys = this.koordinaatisto.kerroLeveys();
		
		sijaintiX = Koordinaatit.korjaaKoordinaatti( sijaintiX, leveys );

		return Koordinaatit.kerroMatka( this.sijaintiX, sijaintiX, leveys, suunta );
		
	}
	
	/**
	 * Kertoo et‰isyyden annettuun y-koordinaattiin annettuun suuntaan y-suunnassa.
	 * @param sijaintiY Y-koordinaatti, johon et‰isyys halutaan m‰‰ritt‰‰.
	 * @param suunta Etumerkki kertoo, m‰‰ritet‰‰nkˆ et‰isyys positiiviseen vai
	 * negatiiviseen suuntaan. Nolla tarkoittaa positiivista.
	 * @return Et‰isyys y-suunnassa annettuun suuntaan. Etumerkki kertoo suunnan.
	 */
	public double kerroMatkaY( double sijaintiY, int suunta ) {

		int korkeus = this.koordinaatisto.kerroKorkeus();
		
		sijaintiY = Koordinaatit.korjaaKoordinaatti( sijaintiY, korkeus );

		return Koordinaatit.kerroMatka( this.sijaintiY, sijaintiY, korkeus, suunta );
		
	}
	
	/**
	 * Kertoo, onko koordinaatti x-suunnassa v‰lill‰ <CODE>alkuSijaintiX</CODE> -
	 * <CODE>loppuSijaintiX</CODE>. V‰li‰ tarkastellaan alkupisteest‰ positiiviseen
	 * suuntaan. V‰lien p‰‰tepisteet kuuluvat v‰lille. 
	 * @param alkuSijaintiX V‰lin alkupisteen x-koordinaatti.
	 * @param loppuSijaintiX V‰lin loppupisteen x-koordinaatti.
	 * @return Totuusarvo, joka kertoo, onko koordinaatti annetulla v‰lill‰ x-suunnassa.
	 */
	public boolean onValissaX( double alkuSijaintiX, double loppuSijaintiX ) {
	    
	    int leveys = this.koordinaatisto.kerroLeveys();
	    
	    Koordinaatit.korjaaKoordinaatti( alkuSijaintiX, leveys );
	    Koordinaatit.korjaaKoordinaatti( loppuSijaintiX, leveys );
	    
	    return Koordinaatit.onValissa( this.sijaintiX, alkuSijaintiX, loppuSijaintiX );
	    
	}
	
	/**
	 * Kertoo, onko koordinaatti y-suunnassa v‰lill‰ <CODE>alkuSijaintiY</CODE> -
	 * <CODE>loppuSijaintiY</CODE>. V‰li‰ tarkastellaan alkupisteest‰ positiiviseen
	 * suuntaan. V‰lien p‰‰tepisteet kuuluvat v‰lille. 
	 * @param alkuSijaintiY V‰lin alkupisteen y-koordinaatti.
	 * @param loppuSijaintiY V‰lin loppupisteen y-koordinaatti.
	 * @return Totuusarvo, joka kertoo, onko koordinaatti annetulla v‰lill‰ y-suunnassa.
	 */
	public boolean onValissaY( double alkuSijaintiY, double loppuSijaintiY ) {
	    
	    int korkeus = this.koordinaatisto.kerroKorkeus();
	    
	    Koordinaatit.korjaaKoordinaatti( alkuSijaintiY, korkeus );
	    Koordinaatit.korjaaKoordinaatti( loppuSijaintiY, korkeus );
	    
	    return Koordinaatit.onValissa( this.sijaintiY, alkuSijaintiY, loppuSijaintiY );
	    
	}

}
	
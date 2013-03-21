
/**
 * 
 * Luokka <CODE>KappalePelaajanAlus<CODE> kuvaa sellaisia avaruuden aluksia,
 * jotka ovat myˆs osana peli‰. Toisin sanoen n‰ill‰ aluksilla on
 * kuljettaja, joka on jokin pelaaja.
 *
 * @author Jaakko Luttinen
 *
 */

public class KappalePelaajanAlus extends KappaleAlus {

    /**
     * Kun alus tuhoutuu, luo se r‰j‰hdyksen omalle paikalleen. T‰m‰ r‰j‰dys pidet‰‰n
     * t‰ss‰ muuttujassa, kunnes alus taas laitetaan maailmaan.
     */
	private KappalePelaajanRajahdys rajahdys;
	
	/**
	 * Alusta ohjaava pelaaja.
	 */
	private Pelaaja pelaaja;

	/**
	 * Luo aluksen annetun mallin perusteella.
	 * @param malli Alus, jonka perusteella uusi alus luodaan.
	 */
	public KappalePelaajanAlus( KappaleAlus malli ) {
	    
	    super( malli );
	    this.pelaaja = null;
	    
	}
	
	/**
	 * Kertoo alusta ohjaavan pelaajan.
	 * @return Alusta ohjaava pelaaja.
	 */
	public Pelaaja kerroPelaaja() {
	    
	    return this.pelaaja;
	    
	}
	
	/**
	 * Asettaa alusta ohjaavan pelaajan. Jos annettu pelaaja jo ohjaa alusta tai annettu
	 * pelaaja on null, ei metodi tee mit‰‰n. Poistaa mahdollisen vanhan ohjaajan.
	 * @param pelaaja Alusta ohjaava pelaaja.
	 */
	public void asetaPelaaja( Pelaaja pelaaja ) {
	    
	    if ( this.pelaaja == pelaaja || pelaaja == null )
	        return;
	    
	    this.poistaPelaaja();
	    this.pelaaja = pelaaja;
	    this.pelaaja.asetaAlus( this );
	    
	}
	
	/**
	 * Poistaa alusta ohjaavan pelaajan. Poistaa aluksen myˆs pelaajalta.
	 */
	public void poistaPelaaja() {
	    
	    if ( this.pelaaja != null ) {
	        
	        Pelaaja vanhaPelaaja = this.pelaaja;
	        this.pelaaja = null;
	        vanhaPelaaja.poistaAlus();
	        
	    }
	    
	}
	
	public void asetaMaailmaan( Avaruus maailma, double sijaintiX, double sijaintiY ) throws Exception {
	    
	    super.asetaMaailmaan( maailma, sijaintiX, sijaintiY );
	    //Uudelleensyntyy - ei seuraa en‰‰ r‰j‰hdyst‰‰n.
	    this.rajahdys = null;
	    
	}
	
	/**
	 * Kertoo aluksen r‰j‰hdyksen. Jos alus on maailmassa hyviss‰ voimissaan,
	 * on palautusarvo silloin null.
	 * @return Aluksen r‰j‰hdys.
	 */
	public KappalePelaajanRajahdys kerroRajahdys() {
	    
	    return this.rajahdys;
	    
	}
	
	public boolean suoritaOsuma( RajapintaTuhoatekeva osunutAmmus ) {
	
	    boolean totuusarvo = super.suoritaOsuma( osunutAmmus );
	    
	    if ( this.onTuhoutunut() )
	        this.pelaaja.kerroPeli().suoritaTappo( this.pelaaja, osunutAmmus.kerroTuhonLahde() );
	        	    
	    return totuusarvo;
	    
	}
	
	public void rajahda() {
	    
	    try {
	        
	        //Yritet‰‰n luoda r‰j‰hdys pelaajan aluksen paikalle. T‰m‰ r‰j‰hdys sitten
	        //tuhoutuessaan l‰hett‰‰ k‰skyn arpoa pelaajan alukselle uusi sijainti.
	        this.rajahdys = new KappalePelaajanRajahdys( this.kerroRajahdysmalli(), this.pelaaja );
	        this.rajahdys.asetaMaailmaan( this.kerroMaailma(), this.kerroSijainti().kerroSijaintiX(), this.kerroSijainti().kerroSijaintiY() );
	        
	    }
	    catch ( Exception virhe ) {
	    
	        //Koska r‰j‰hdys ei tuhoutuessaan arvokaan uutta sijaintia, on se teht‰v‰ nyt.
	        this.rajahdys = null;
	        this.pelaaja.kerroPeli().arvoPelaajanSijainti( this.pelaaja );
	        
	    }
 	    
	}
	
}

/**
 * 
 * Luokka <CODE>KappaleLiikkuva</CODE> kuvaa avaruuden kappaleita, jotka ovat
 * muodoltaan ympyröitä ja joilla on nopeutta johonkin suuntaan. Liikkuva
 * kappale voi törmäillä toisiin kappaleisiin ja sillä on näitä tilanteita
 * varten omat rutiininsa.
 *
 * @author Jaakko Luttinen
 *
 */

public class KappaleLiikkuva extends Kappale {
	
    /**
     * Kappaletta käsitellään ympyränä, jonka halkaisija on
     * <CODE>TODELLISENHALKAISIJANSUHDE</CODE> * kuvasarjan lyhyempi sivu.
     */
    private static final double TODELLISENHALKAISIJANSUHDE = 0.8;

    /**
     * Kappaleen nopeusvektori. Nopeuden yksikkö: pikseliä / millisekunti.
     */
 	private Vektori nopeus;
 	
 	/**
 	 * Koska törmäyksissä ei voida heti tehdä pysyviä muutoksia nopeusvektoriin,
 	 * käytetään tällaista apuvektoria.
 	 */
	private Vektori nopeusTormayksenJalkeen;
	
	/**
	 * Kappaleen alue, joka on ympyrä.
	 */
	private AlueYmpyra alueSijainti;
	
	/**
	 * Luo uuden liikkuvan kappaleen annettujen tietojen perusteella.
	 * @param tyyppi Kappaleen tunniste. Ei tarvitse käyttää, eli voi hyvin olla null.
	 * @param kuva Kappaleen kuvasarja.
	 * @throws Exception Poikkeus kertoo, että annetuissa parametreissa oli virhe.
	 */
	public KappaleLiikkuva( String tyyppi, Kuvasarja kuva ) throws Exception {
		
		super( tyyppi, kuva );
		
		//Luodaan ympyräalue, joka mallintaa kappaletta avaruudessa.
		int halkaisija = ( kuva.kerroKorkeus() + kuva.kerroLeveys() ) / 2;
		this.alueSijainti = new AlueYmpyra( KappaleLiikkuva.TODELLISENHALKAISIJANSUHDE * halkaisija / 2.0 );
		
		//Luodaan nopeusvektorit.
		this.nopeus = new Vektori( 0, 0 );
		this.nopeusTormayksenJalkeen = new Vektori( 0, 0 );
		
	}
	
	/**
	 * Luo uuden liikkuvan kappaleen annetun kopion perusteella.
	 * @param kopio Kappale, jonka perusteella liikkuva kopio luodaan.
	 */
	public KappaleLiikkuva( KappaleLiikkuva kopio ) {
	    
	    //Tällä tavalla luomalla ei synny mahdollisia poikkeustilanteita.
	    //Tätä tapaa käytetään monissa muissakin luokassa.

	    super( kopio );
	    
	    this.alueSijainti = new AlueYmpyra( kopio.alueSijainti );
	    this.alueSijainti.poistaSijainti();

	    //Luodaan nopeusvektorit.
		this.nopeus = new Vektori( 0, 0 );
		this.nopeusTormayksenJalkeen = new Vektori( 0, 0 );
	    
	}
	
	public Alue kerroAlue() {
	    
	    return this.alueSijainti;
	    
	}
	
	/**
	 * Kertoo liikkuvan kappaleen alueen säteen.
	 * @return Liikkuvan kappaleen alueen säde.
	 */
	public double kerroSade() {
	    
	    return this.alueSijainti.kerroSade();
	    
	}
	
	/**
	 * Asettaa kappaleen nopeuden.
	 * @param nopeusX Kappaleen nopeuden x-komponentti.
	 * @param nopeusY Kappaleen nopeuden y-komponentti.
	 */
	public void asetaNopeus( double nopeusX, double nopeusY ) {
		
		this.nopeus.aseta( nopeusX, nopeusY );
		
	}
	
	/**
	 * Muuttaa kappaleen nopeutta törmäyksen jälkeen. Luokkahierarkian sisäiseen
	 * käyttöön.
	 * @param muutosX Kappaleen nopeuden muutos x-suunnassa.
	 * @param muutosY Kappaleen nopeuden muutos y-suunnassa.
	 */
	protected void muutaNopeusTormayksenJalkeen( double muutosX, double muutosY ) {
	    
	    this.nopeusTormayksenJalkeen.muuta( muutosX, muutosY );
	    
	}
	
	/**
	 * Kertoo kappaleen nopeuden.
	 * @return Kappaleen nopeusvektori.
	 */
	public Vektori kerroNopeus() {
	    
	    return this.nopeus;
	    
	}
		
	/**
	 * Laskee oman nopeutensa törmäyksen jälkeen, ottaen huomioon törmättävän kappaleen
	 * ominaisuuksia. Ei tee vielä pysyviä muutoksia, jotta törmäyksen toinenkin
	 * osapuoli voi tutkia törmäystä edeltäviä nopeuksia.
	 * @param tormattava Kappale, jonka kanssa törmäys tapahtuu.
	 */
	public void laskeNopeusTormayksenJalkeen( Kappale tormattava ) {
	    
	    //Nopeus muuttuu vain, jos toinen kappale on törmättävissä.
	    if ( tormattava.onTormattavissa() ) {
	        
	        if ( this.onTormattavissa() && tormattava instanceof KappaleLiikkuva ) {
	  
	            //Tässä semmoinen törmäys, jossa molempien nopeus muuttuu.
		        Vektori akseliVektori = new Vektori( this.kerroSijainti().kerroLyhinMatkaX( tormattava.kerroSijainti().kerroSijaintiX() ),
						 	 						 this.kerroSijainti().kerroLyhinMatkaY( tormattava.kerroSijainti().kerroSijaintiY() ) );

	            KappaleLiikkuva tormattavaLiikkuva = ( KappaleLiikkuva )tormattava;
	            
	            this.muutaNopeusTormayksenJalkeen( -this.nopeus.kerroVektoriprojektio( akseliVektori ).kerroX() + Kappale.KIMMOISUUS * tormattavaLiikkuva.nopeus.kerroVektoriprojektio( akseliVektori ).kerroX(),
	                    						   -this.nopeus.kerroVektoriprojektio( akseliVektori ).kerroY() + Kappale.KIMMOISUUS * tormattavaLiikkuva.nopeus.kerroVektoriprojektio( akseliVektori ).kerroY() );
	            
	        }
	        else {
	            
	            try {
	                
	                //Tässä semmoinen kimmoisa törmäys.
	                Vektori akseliVektori = tormattava.kerroAlue().kerroEtanormaali( this.kerroSijainti() );
	                Vektori nopeudenProjektio = this.kerroNopeus().kerroVektoriprojektio( akseliVektori );
	                
	                //Täytyy tehdä seuraavanlainen komponentin suunnan tarkastelu alueiden
	                //epätarkoista leikkaustesteistä ja etänormaaleista johtuen..
	                int kerroin = 0;
	                //Jos komponentti on vastakkaissuuntainen. Kulmatarkastelu likiarvojen takia.
	                if ( Math.abs( Math.PI - Vektori.kerroKulma( akseliVektori, nopeudenProjektio ) ) < 0.1 )
	                    kerroin = -1;
	                
	                this.muutaNopeusTormayksenJalkeen( ( kerroin * 1 + ( kerroin * 2 + 1 ) * Kappale.KIMMOISUUS ) * nopeudenProjektio.kerroX(),
	                        						   ( kerroin * 1 + ( kerroin * 2 + 1 ) * Kappale.KIMMOISUUS ) * nopeudenProjektio.kerroY() );
	                
	            }
	            catch ( Exception virhe ) {  } //Sijainnit eri koordinaatistoista, ei tehdä mitään.
	            
	        }
	            
	    }
	    
	}
	
	public boolean suoritaTormays( Kappale tormattava ) {

	    if ( tormattava.onTormattavissa() ) {
	    
	        //Tässä laskee uuden nopeuden...
	        this.laskeNopeusTormayksenJalkeen( tormattava );
	        
	        //Sijainti on laiton, jos molemmat kappaleet ovat törmättävissä.
	        return this.onTormattavissa();
	        
	    }
	    
	    return false;
	    
	}

	public void tormayksetTutkittu() {
	    
	    //Tehdään nopeuden muutoksesta vasta nyt näkyvä ja pysyvä.
	    this.nopeus.aseta( this.nopeusTormayksenJalkeen.kerroX(), this.nopeusTormayksenJalkeen.kerroY() );
	    
	}

	public void tuhoudu() {
	    
	    this.asetaNopeus( 0.0, 0.0 );
	    super.tuhoudu();
	    
	}
	
	/**
	 * Tutkii törmäykset, jotka tapahtuisivat, jos kappale liikahtaisi parametrina annettuun
	 * uuteen alueeseen. Metodi tutkii törmäykset kaikkiin muihin saman maailman kappaleisiin,
	 * kunnes tapahtuu yksikin sellainen leikkaus, jossa kappaleet sijaitsisivat toisiinsa
	 * nähden laittomasti. Kutsuu kappaleiden <CODE>suoritaTormays</CODE>-metodeita ja aina kun
	 * yhden kappaleen törmäykset on tutkittu, kutsutaan sen <CODE>tormayksetTutkittu</CODE>-
	 * metodia.
	 * @param uusiAlue Alue, johon kappale haluaisi liikahtaa.
	 * @return Totuusarvo, joka kertoo, tapahtuiko sellainen törmäys, jonka perusteella
	 * uusi sijainti olisi laiton.
	 */
	public boolean tutkiTormaykset( Alue uusiAlue ) {
	    
	    boolean tormays = false;
	    boolean laitonSijainti = false;

        for ( int ind = 0;
        	  ind < this.kerroMaailma().kerroKappaleidenMaara() && !laitonSijainti && !this.onTuhoutunut();
        	  ind++ ) {
	       
            Kappale testattava = this.kerroMaailma().kerroKappale( ind );
            
            if ( testattava != this && !testattava.onTuhoutunut() && ( testattava.onTormattavissa() || this.onTormattavissa() ) ) {

                if ( uusiAlue.leikkaavat( testattava.kerroAlue() ) ) {

                    if ( this.suoritaTormays( testattava ) )
                        laitonSijainti = true;
                    
                    if ( testattava.suoritaTormays( this ) )
                        laitonSijainti = true;
                    
                    testattava.tormayksetTutkittu();
                    tormays = true;
                    
                }

            }
	        
	    }
        
        if ( tormays )
            this.tormayksetTutkittu();

        return laitonSijainti;
	    
	}
	
	/**
	 * Yrittää liikuttaa kappaletta nopeuden ja kuluneen ajan määrämän siirtymän.
	 * Tutkii ja suorittaa törmäykset. Siirtää kappaleen uuteen sijaintiin vain, jos
	 * uusi sijainti ei ole laiton.
	 * @param aika Aika, joka on kulunut viime sykäyksestä.
	 */
	protected void toimi( int aika ) {

	    this.nopeusTormayksenJalkeen.aseta( this.nopeus.kerroX(), this.nopeus.kerroY() );
	    
	    //Luodaan alueesta kopio, jonka sijaintia sitten muutellaan.
	    AlueYmpyra uusiAlue = new AlueYmpyra( this.alueSijainti );
	    uusiAlue.kerroSijainti().muuta( this.nopeus.kerroX() * aika,
	            						this.nopeus.kerroY() * aika );
	    
	    //Tutkitaan törmäykset ja jos uusi sijainti ei ole laiton, siirretään kappaletta.
	    if ( !this.tutkiTormaykset( uusiAlue ) )
	        this.alueSijainti = uusiAlue;
		    		
	}
	
}
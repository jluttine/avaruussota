
/**
 * 
 * Luokka <CODE>KappaleLiikkuva</CODE> kuvaa avaruuden kappaleita, jotka ovat
 * muodoltaan ympyr�it� ja joilla on nopeutta johonkin suuntaan. Liikkuva
 * kappale voi t�rm�ill� toisiin kappaleisiin ja sill� on n�it� tilanteita
 * varten omat rutiininsa.
 *
 * @author Jaakko Luttinen
 *
 */

public class KappaleLiikkuva extends Kappale {
	
    /**
     * Kappaletta k�sitell��n ympyr�n�, jonka halkaisija on
     * <CODE>TODELLISENHALKAISIJANSUHDE</CODE> * kuvasarjan lyhyempi sivu.
     */
    private static final double TODELLISENHALKAISIJANSUHDE = 0.8;

    /**
     * Kappaleen nopeusvektori. Nopeuden yksikk�: pikseli� / millisekunti.
     */
 	private Vektori nopeus;
 	
 	/**
 	 * Koska t�rm�yksiss� ei voida heti tehd� pysyvi� muutoksia nopeusvektoriin,
 	 * k�ytet��n t�llaista apuvektoria.
 	 */
	private Vektori nopeusTormayksenJalkeen;
	
	/**
	 * Kappaleen alue, joka on ympyr�.
	 */
	private AlueYmpyra alueSijainti;
	
	/**
	 * Luo uuden liikkuvan kappaleen annettujen tietojen perusteella.
	 * @param tyyppi Kappaleen tunniste. Ei tarvitse k�ytt��, eli voi hyvin olla null.
	 * @param kuva Kappaleen kuvasarja.
	 * @throws Exception Poikkeus kertoo, ett� annetuissa parametreissa oli virhe.
	 */
	public KappaleLiikkuva( String tyyppi, Kuvasarja kuva ) throws Exception {
		
		super( tyyppi, kuva );
		
		//Luodaan ympyr�alue, joka mallintaa kappaletta avaruudessa.
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
	    
	    //T�ll� tavalla luomalla ei synny mahdollisia poikkeustilanteita.
	    //T�t� tapaa k�ytet��n monissa muissakin luokassa.

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
	 * Kertoo liikkuvan kappaleen alueen s�teen.
	 * @return Liikkuvan kappaleen alueen s�de.
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
	 * Muuttaa kappaleen nopeutta t�rm�yksen j�lkeen. Luokkahierarkian sis�iseen
	 * k�ytt��n.
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
	 * Laskee oman nopeutensa t�rm�yksen j�lkeen, ottaen huomioon t�rm�tt�v�n kappaleen
	 * ominaisuuksia. Ei tee viel� pysyvi� muutoksia, jotta t�rm�yksen toinenkin
	 * osapuoli voi tutkia t�rm�yst� edelt�vi� nopeuksia.
	 * @param tormattava Kappale, jonka kanssa t�rm�ys tapahtuu.
	 */
	public void laskeNopeusTormayksenJalkeen( Kappale tormattava ) {
	    
	    //Nopeus muuttuu vain, jos toinen kappale on t�rm�tt�viss�.
	    if ( tormattava.onTormattavissa() ) {
	        
	        if ( this.onTormattavissa() && tormattava instanceof KappaleLiikkuva ) {
	  
	            //T�ss� semmoinen t�rm�ys, jossa molempien nopeus muuttuu.
		        Vektori akseliVektori = new Vektori( this.kerroSijainti().kerroLyhinMatkaX( tormattava.kerroSijainti().kerroSijaintiX() ),
						 	 						 this.kerroSijainti().kerroLyhinMatkaY( tormattava.kerroSijainti().kerroSijaintiY() ) );

	            KappaleLiikkuva tormattavaLiikkuva = ( KappaleLiikkuva )tormattava;
	            
	            this.muutaNopeusTormayksenJalkeen( -this.nopeus.kerroVektoriprojektio( akseliVektori ).kerroX() + Kappale.KIMMOISUUS * tormattavaLiikkuva.nopeus.kerroVektoriprojektio( akseliVektori ).kerroX(),
	                    						   -this.nopeus.kerroVektoriprojektio( akseliVektori ).kerroY() + Kappale.KIMMOISUUS * tormattavaLiikkuva.nopeus.kerroVektoriprojektio( akseliVektori ).kerroY() );
	            
	        }
	        else {
	            
	            try {
	                
	                //T�ss� semmoinen kimmoisa t�rm�ys.
	                Vektori akseliVektori = tormattava.kerroAlue().kerroEtanormaali( this.kerroSijainti() );
	                Vektori nopeudenProjektio = this.kerroNopeus().kerroVektoriprojektio( akseliVektori );
	                
	                //T�ytyy tehd� seuraavanlainen komponentin suunnan tarkastelu alueiden
	                //ep�tarkoista leikkaustesteist� ja et�normaaleista johtuen..
	                int kerroin = 0;
	                //Jos komponentti on vastakkaissuuntainen. Kulmatarkastelu likiarvojen takia.
	                if ( Math.abs( Math.PI - Vektori.kerroKulma( akseliVektori, nopeudenProjektio ) ) < 0.1 )
	                    kerroin = -1;
	                
	                this.muutaNopeusTormayksenJalkeen( ( kerroin * 1 + ( kerroin * 2 + 1 ) * Kappale.KIMMOISUUS ) * nopeudenProjektio.kerroX(),
	                        						   ( kerroin * 1 + ( kerroin * 2 + 1 ) * Kappale.KIMMOISUUS ) * nopeudenProjektio.kerroY() );
	                
	            }
	            catch ( Exception virhe ) {  } //Sijainnit eri koordinaatistoista, ei tehd� mit��n.
	            
	        }
	            
	    }
	    
	}
	
	public boolean suoritaTormays( Kappale tormattava ) {

	    if ( tormattava.onTormattavissa() ) {
	    
	        //T�ss� laskee uuden nopeuden...
	        this.laskeNopeusTormayksenJalkeen( tormattava );
	        
	        //Sijainti on laiton, jos molemmat kappaleet ovat t�rm�tt�viss�.
	        return this.onTormattavissa();
	        
	    }
	    
	    return false;
	    
	}

	public void tormayksetTutkittu() {
	    
	    //Tehd��n nopeuden muutoksesta vasta nyt n�kyv� ja pysyv�.
	    this.nopeus.aseta( this.nopeusTormayksenJalkeen.kerroX(), this.nopeusTormayksenJalkeen.kerroY() );
	    
	}

	public void tuhoudu() {
	    
	    this.asetaNopeus( 0.0, 0.0 );
	    super.tuhoudu();
	    
	}
	
	/**
	 * Tutkii t�rm�ykset, jotka tapahtuisivat, jos kappale liikahtaisi parametrina annettuun
	 * uuteen alueeseen. Metodi tutkii t�rm�ykset kaikkiin muihin saman maailman kappaleisiin,
	 * kunnes tapahtuu yksikin sellainen leikkaus, jossa kappaleet sijaitsisivat toisiinsa
	 * n�hden laittomasti. Kutsuu kappaleiden <CODE>suoritaTormays</CODE>-metodeita ja aina kun
	 * yhden kappaleen t�rm�ykset on tutkittu, kutsutaan sen <CODE>tormayksetTutkittu</CODE>-
	 * metodia.
	 * @param uusiAlue Alue, johon kappale haluaisi liikahtaa.
	 * @return Totuusarvo, joka kertoo, tapahtuiko sellainen t�rm�ys, jonka perusteella
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
	 * Yritt�� liikuttaa kappaletta nopeuden ja kuluneen ajan m��r�m�n siirtym�n.
	 * Tutkii ja suorittaa t�rm�ykset. Siirt�� kappaleen uuteen sijaintiin vain, jos
	 * uusi sijainti ei ole laiton.
	 * @param aika Aika, joka on kulunut viime syk�yksest�.
	 */
	protected void toimi( int aika ) {

	    this.nopeusTormayksenJalkeen.aseta( this.nopeus.kerroX(), this.nopeus.kerroY() );
	    
	    //Luodaan alueesta kopio, jonka sijaintia sitten muutellaan.
	    AlueYmpyra uusiAlue = new AlueYmpyra( this.alueSijainti );
	    uusiAlue.kerroSijainti().muuta( this.nopeus.kerroX() * aika,
	            						this.nopeus.kerroY() * aika );
	    
	    //Tutkitaan t�rm�ykset ja jos uusi sijainti ei ole laiton, siirret��n kappaletta.
	    if ( !this.tutkiTormaykset( uusiAlue ) )
	        this.alueSijainti = uusiAlue;
		    		
	}
	
}


import java.awt.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;

/**
 * 
 * Luokka <CODE>Avaruus<\CODE> mallintaa avaruutta, jossa on erilaisia
 * kappaleita. Avaruudella on tietyt vakioarvoiset minimi- ja maksimi-
 * mitat, jotka m‰‰ritet‰‰n luonnin yhteydess‰. Lis‰ksi avaruuden taustalla
 * on t‰htitaivas. Avaruudessa kuluu aikaa aina, kun sille v‰litet‰‰n syk‰ys.
 * Avaruus v‰litt‰‰ t‰m‰n syk‰yksen kaikille kappaleille ja antaa niille n‰in
 * mahdollisuuden toimia kuluneen ajan edellytt‰m‰ll‰ tavalla.
 *
 * @author Jaakko Luttinen
 *
 */

public class Avaruus {

    /**
     * T‰htitaivaan koon ja liikkeen suhde avaruuteen kappaleineen.
     */
    private static final double TAHTITAIVAAN_SUHDE = 0.1;
    
    /**
     * Avaruuden leveyden ja korkeuden minimimitta.
     */
    public static final int PELIKENTAN_MINIMIMITTA = 500;
    
    /**
     * Avaruuden leveyden ja korkeuden maksimimitta.
     */
    public static final int PELIKENTAN_MAKSIMIMITTA = 3000;
	
    /**
     * Lista kaikista avaruudessa olevista kappaleista.
     */
	private ArrayList kappaleet;
	
	/**
	 * Avaruuden koordinaatisto.
	 */
	private Koordinaatisto koordinaatisto;
	
	/**
	 * Avaruuden t‰htitaivaan kuva.
	 */
	private Kuvasarja tahtitaivas;
	
	/**
	 * Luo uuden avaruuden annettujen mittojen perusteella. Jos annetut mitat
	 * ovat virheelliset, heitet‰‰n poikkeus <CODE>Exception</CODE>. Piirt‰‰
	 * t‰htitaivaan kuvan. Huomioitavaa on, ett‰ avaruuden mittoja ei ole
	 * mahdollista muuttaa, vaan on luotava t‰ysin uusi avaruus, jos kokoa
	 * halutaan muuttaa.
	 * @param leveys Avaruuden leveys.
	 * @param korkeus Avaruuden korkeus.
	 * @throws Exception Poikkeus, joka kertoo, ett‰ annetut mitat olivat virheelliset.
	 */
	public Avaruus( int leveys, int korkeus ) throws Exception {
		
        if ( leveys < Avaruus.PELIKENTAN_MINIMIMITTA || leveys > Avaruus.PELIKENTAN_MAKSIMIMITTA ||
             korkeus < Avaruus.PELIKENTAN_MINIMIMITTA || korkeus > Avaruus.PELIKENTAN_MAKSIMIMITTA ) {
               
               throw new Exception( "Maailmaa ei voida luoda. Leveyden ja korkeuden t‰ytyy olla v‰lill‰ " +
                       			 	Avaruus.PELIKENTAN_MINIMIMITTA + "-" +
                       			 	Avaruus.PELIKENTAN_MAKSIMIMITTA + "." );
           
        }
           
		this.kappaleet = new ArrayList();
		this.koordinaatisto = new Koordinaatisto( leveys, korkeus );
		this.luoTahtitaivas();
		
	}
	
	/**
	 * Piirt‰‰ avaruuden t‰htitaivaan laittamalla valkoisia pisteit‰ satunnaisesti
	 * mustalle taustalle. T‰htitaivaan mitat m‰‰r‰ytyv‰t vakion <CODE>TAHTITAIVAAN_SUHDE</CODE>
	 * perusteella (suhde avaruuden mittoihin). T‰htien m‰‰r‰ m‰‰r‰ytyy t‰htien tiheydest‰,
	 * esim. 1 t‰hti / 400 neliˆpikseli‰.
	 */
	private void luoTahtitaivas() {
	    
	    int leveys = ( int )( Avaruus.TAHTITAIVAAN_SUHDE * this.koordinaatisto.kerroLeveys() );
	    int korkeus = ( int )( Avaruus.TAHTITAIVAAN_SUHDE * this.koordinaatisto.kerroKorkeus() );
	    double tiheys = 1.0 / 400;
	    
	    BufferedImage tahtitaivas = new BufferedImage( leveys, korkeus, BufferedImage.TYPE_INT_RGB );

	    int tahtienmaara = ( int )( leveys * korkeus * tiheys );

		for ( int ind = 0; ind < tahtienmaara; ind++ ){
		    
		    tahtitaivas.setRGB( ( int )( Math.random() * leveys ),
			      				( int )( Math.random() * korkeus ),
			      				Color.WHITE.getRGB() );
		
		}
		
		this.tahtitaivas = new Kuvasarja( tahtitaivas, 1, 1 );
		
	}

	/**
	 * Lis‰‰ kappaleen avaruuteen. Jos <CODE>uusiKappale</CODE> on null tai
	 * avaruudessa jo on lis‰tt‰v‰ kappale, ei metodi tee mit‰‰n. Metodi
	 * kutsuu kappaleen <CODE>asetaMaailmaan</CODE>-metodia. Annetun sijainnin
	 * suhteen metodi ei tee mit‰‰n laillisuus tarkasteluja - ne j‰‰v‰t
	 * kutsujan vastuulle, jos haluaa niit‰ tutkia.
	 * @param uusiKappale Kappale, joka avaruuteen halutaan lis‰t‰.
	 * @param sijaintiX X-koordinaatti, johon kappale halutaan asettaa.
	 * @param sijaintiY Y-koordinaatti, johon kappale halutaan asettaa.
	 * @throws Exception Poikkeus kertoo, ettei kappale sovi avaruuteen.
	 */
	public synchronized void lisaaKappale( Kappale uusiKappale, double sijaintiX, double sijaintiY ) throws Exception {

	    if ( uusiKappale == null || this.kappaleet.contains( uusiKappale ) )
	        return;
	    
	    this.kappaleet.add( uusiKappale );
	    
	    try { 
	        
	        uusiKappale.asetaMaailmaan( this, sijaintiX, sijaintiY );
	        
	    }
	    catch ( Exception virhe ) {
	        
	        //Lis‰yst‰ ei suoritetakaan, joten:
	        this.poistaKappale( uusiKappale );
	        throw virhe;
	        
	    }
		
	}
	
	/**
	 * Jos kappale <CODE>poistettava</CODE> on avaruudessa, poistetaan se
	 * avaruudesta. 
	 * @param poistettava Kappale, joka avaruudesta halutaan poistaa.
	 */
	public synchronized void poistaKappale( Kappale poistettava ) {
	    
	    int indeksi = this.kappaleet.indexOf( poistettava );
	    
	    if ( indeksi >= 0 )
	        this.poistaKappale( indeksi );
	    
	}
	
	/**
	 * Poistaa kappaleen, joka lˆytyy annetulla indeksill‰. Kutsutaan kappaleen
	 * <CODE>poistaMaailmasta</CODE> metodia.
	 * @param indeksi Poistettavan kappaleen indeksi.
	 */
	private synchronized void poistaKappale( int indeksi ) {
	    
	    Kappale poistettava = ( Kappale )this.kappaleet.remove( indeksi );
	    poistettava.poistaMaailmasta();
	    
	}
	
	/**
	 * Poistaa kaikki kappaleet, jotka avaruudessa ovat.
	 */
	public synchronized void poistaKaikkiKappaleet() {
	    
	    if ( this.kappaleet != null ) {
	        
	        int koko;
	        while ( ( koko = this.kappaleet.size() ) > 0 )
	            this.poistaKappale( koko - 1 );
	        
	    }
	  
	}
	
	/**
	 * Kertoo kappaleen, joka annetulla indeksin arvolla lˆytyy tai null,
	 * jos indeksi on ep‰kelpo.
	 * @param indeksi Kappaleen, joka halutaan saada, indeksi.
	 * @return Kappale, joka annetulla indeksill‰, tai null.
	 */
	public Kappale kerroKappale( int indeksi ) {
		
		if ( indeksi < 0 || indeksi >= this.kappaleet.size() )
			return null;
		
		return ( Kappale )this.kappaleet.get( indeksi );
		
	}

	/**
	 * Kertoo avaruudessa sijaitsevien kappaleiden m‰‰r‰n.
	 * @return Avaruudessa sijaitsevien kappaleiden m‰‰r‰.
	 */
	public int kerroKappaleidenMaara() {
	    
	    return this.kappaleet.size();
	    
	}
	
	/**
	 * Kertoo avaruuden koordinaatiston.
	 * @return Avaruuden koordinaatisto.
	 */
	public Koordinaatisto kerroKoordinaatisto() {
	    
	    return this.koordinaatisto;
	    
	}
	
	/**
	 * Kertoo, onko jokin alue laiton, eli on mitoiltaan liian suuri
	 * tai leikkaa avaruuden tˆrm‰tt‰viss‰ olevia kappaleita. lisaaKappale -metodi ei
	 * tee leikkaus tarkastelua, joten ep‰varmoissa tilanteissa kutsujan on
	 * itse tutkittava onko alue laiton ja miten toimia, jos on.
	 * @param tutkittava Alue, jonka laittomuutta halutaan tutkia.
	 * @return Totuusarvo, joka kertoo, onko annettu alue avaruudesta laiton.
	 */
	public boolean onLaitonAlue( Alue tutkittava ) {
	    
	    //Liian iso alue on laiton.
	    if ( tutkittava.onLaitonKoordinaatisto( this.koordinaatisto ) )
	        return true;
	        
	    //Alue, joka leikkaa yht‰kin tˆrm‰tt‰viss‰ olevaa kappaletta, on laiton.
	    for ( int ind = 0; ind < this.kappaleet.size(); ind++ ) {
	        
	        Kappale kappale = ( Kappale )this.kappaleet.get( ind );
	        if ( kappale.onTormattavissa() ) {
	            
	            if ( kappale.kerroAlue().leikkaavat( tutkittava ) )
	                return true;
	            
	        }
	        
	    }
	    
	    return false;
	    
	}
	
	/**
	 * L‰hett‰‰ kaikille avaruuden kappaleille <CODE>suoritaSykays</CODE>-metodikutsun.
	 * K‰y kappaleet sis‰lt‰v‰n listan k‰‰nteisess‰ j‰rjestyksess‰ l‰pi, jottei
	 * listasta poistuvat kappaleet sotke seuraavien indeksej‰. Se myˆs ehk‰isee
	 * aluksen osumista omaan ammukseensa. 
	 * @param aika Aika, joka viime syk‰yksest‰ on kulunut, millisekunteina.
	 */
	public synchronized void suoritaSykays( int aika ) {
		
		for ( int ind = this.kappaleet.size() - 1; ind < this.kappaleet.size() && ind >= 0; ind-- )
		    ( ( Kappale )this.kappaleet.get( ind ) ).suoritaSykays( aika );

	}

	/**
	 * Piirt‰‰ avaruudesta "valokuvan" piirtokontekstille. Kuva on keskitetty annettuun
	 * sijaintiin, ja kuvan mitat ovat annetut. Kuvaan piirret‰‰n t‰htitaivas ja kaikki
	 * kuvaan osuvat kappaleet. Kappaleet piirret‰‰n listan k‰‰nteisess‰ j‰rjestyksess‰.
	 * N‰in ollen pisimp‰‰n avaruudessa olleet kappaleet ovat p‰‰llimm‰isi‰. N‰in ollen
	 * myˆs ammukset piirtyv‰t ampujansa alle. Jos kuvan mitat ovat suurempia kuin avaruuden
	 * mitat, ei taata, ett‰ metodi v‰ltt‰m‰tt‰ piirt‰isi kuvan t‰ysin oikein.
	 * @param piirtokonteksti Graphics-olio, johon kuva piirret‰‰n.
	 * @param sijaintiX Kuvan keskipisteen x-koordinaatti avaruudessa.
	 * @param sijaintiY Kuvan keskipisteen y-koordinaatti avaruudessa.
	 * @param vasen N‰kym‰n vasemman yl‰kulman x-koordinaatti piirtokontekstilla.
	 * @param yla N‰kym‰n vasemman yl‰kulman y-koordinaatti piirtokontekstilla.
	 * @param leveys Kuvan leveys.
	 * @param korkeus Kuvan korkeus.
	 */
    public void piirraNakyma( Graphics piirtokonteksti, double sijaintiX, double sijaintiY, int vasen, int yla, int leveys, int korkeus ) {
        
        //Piirret‰‰n t‰htitaivas.
        int sijaintiTahtitaivaallaX = ( int )( Avaruus.TAHTITAIVAAN_SUHDE * sijaintiX );
        int sijaintiTahtitaivaallaY = ( int )( Avaruus.TAHTITAIVAAN_SUHDE * sijaintiY );
        for ( int ind = 0; -sijaintiTahtitaivaallaX + ind * this.tahtitaivas.kerroLeveys() < leveys; ind++ ) {
            
            for ( int jnd = 0; -sijaintiTahtitaivaallaY + jnd * this.tahtitaivas.kerroKorkeus() < korkeus; jnd++ )
                this.tahtitaivas.piirraFrame( piirtokonteksti,
                        					  0,
                        					  vasen + -sijaintiTahtitaivaallaX + ind * this.tahtitaivas.kerroLeveys(),
                        					  yla + -sijaintiTahtitaivaallaY + jnd * this.tahtitaivas.kerroKorkeus() );
            
        }
        
        //Synkronoidaan, jotta kappaleet voidaan piirt‰‰ turvallisesti ilman muualla
        //tapahtuvia sotkevia muutoksia.
        synchronized ( this ) {
            
            //Piirret‰‰n avaruuden kappaleet.
            for ( int ind = this.kappaleet.size() - 1; ind >= 0; ind-- ) {
                
                ( ( Kappale )this.kappaleet.get( ind ) ).piirraKappale( piirtokonteksti,
                        												sijaintiX,
                        												sijaintiY,
                        												vasen,
                        												yla,
                        												leveys,
                        												korkeus );
                
            }
        
        }
        
    }
 	
}

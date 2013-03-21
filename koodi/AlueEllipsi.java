
/**
 * 
 * Luokka <CODE>AlueEllipsi</CODE> kuvaa ellipsin muotoista aluetta.
 * Ellipsi luodaan m‰‰r‰‰m‰ll‰ polttopistevektori eli vektori polttopisteest‰
 * yksi polttopisteeseen kaksi sek‰ halkaisija eli ellipsin keh‰n pisteen
 * yhteen laskettu et‰isyys molemmista polttopisteist‰. Kun ellipsin sijaintia
 * m‰‰ritet‰‰n, sijainnilla on mahdollista m‰‰r‰t‰ polttopisteen yksi sijainti. 
 *
 * @author Jaakko Luttinen
 *
 */
public class AlueEllipsi extends Alue {
    
    /**
     * Vektori polttopisteest‰ 1 polttopisteeseen 2.
     */
    private Vektori polttopistevektori;
    
    /**
     * Ellipsin keh‰n pisteen yhteenlaskettu et‰isyys molempiin polttopisteisiin.
     */
    private double halkaisija;
    
    /**
     * Kun ellipsin m‰‰ritt‰vi‰ arvoja muutetaan, lasketaan t‰m‰ apumuuttuja, jotta
     * niiden tiedustelu olisi aina j‰lkeenp‰in tehokasta.
     */
    private double isoakseli;

    /**
     * Kun ellipsin m‰‰ritt‰vi‰ arvoja muutetaan, lasketaan t‰m‰ apumuuttuja, jotta
     * niiden tiedustelu olisi aina j‰lkeenp‰in tehokasta.
     */
    private double pikkuakseli;
    
    /**
     * Kertoo ellipsin isoakselin pituuden ellipsin halkaisijan perusteella.
     * @param halkaisija Ellipsin halkaisija.
     * @return Ellipsin isoakselin pituus.
     * @throws Exception Poikkeus, kertoo, ett‰ halkaisija on negatiivinen.
     */
    public static double kerroIsoakseli( double halkaisija ) throws Exception {
        
        if ( halkaisija < 0 )
            throw new Exception( "Ellipsin halkaisijan t‰ytyy olla ep‰negatiivinen." );
        
        return halkaisija / 2.0;
        
    }
    
    /**
     * Kertoo ellipsin pikkuakselin pituuden ellipsin polttopisteiden v‰lisen et‰isyyden ja
     * halkaisijan perusteella.
     * @param polttopisteidenEtaisyys Ellipsin polttopisteiden v‰linen et‰isyys.
     * @param halkaisija Ellipsin halkaisija.
     * @return Ellipsin pikkuakselin pituus.
     * @throws Exception Poikkeus kertoo, ett‰ halkaisija on pienempi kuin polttopisteiden
     * v‰linen et‰isyys.
     */
    public static double kerroPikkuakseli( double polttopisteidenEtaisyys, double halkaisija ) throws Exception {
        
        if ( polttopisteidenEtaisyys > halkaisija )
            throw new Exception( "Ellipsin halkaisijan t‰ytyy olla polttopisteiden et‰isyytt‰ suurempi." );
        
        return Math.sqrt( ( halkaisija * halkaisija - polttopisteidenEtaisyys * polttopisteidenEtaisyys ) / 4.0 );
        
    }
    
    /**
     * Kertoo ellipsi‰ rajaavan pienimm‰n mahdollisen suorakaiteen leveyden.
     * @param isoakseli Ellipsin isoakseli.
     * @param pikkuakseli Ellipsin pikkuakseli.
     * @param kulma Ellipsin kallistuskulma.
     * @return Ellipsi‰ rajaavan pienimm‰n mahdollisen suorakaiteen leveys.
     */
    public static double kerroSuorakaiteenLeveys( double isoakseli, double pikkuakseli, double kulma ) {
        
        double sini = Math.sin( kulma );
        double kosini = Math.cos( kulma );
        
        return Math.sqrt( 4 * isoakseli * isoakseli * kosini * kosini +
                		  4 * pikkuakseli * pikkuakseli * sini * sini );
        
    }
    
    /**
     * Kertoo ellipsi‰ rajaavan pienimm‰n mahdollisen suorakaiteen korkeuden.
     * @param isoakseli Ellipsin isoakseli.
     * @param pikkuakseli Ellipsin pikkuakseli.
     * @param kulma Ellipsin kallistuskulma.
     * @return Ellipsi‰ rajaavan pienimm‰n mahdollisen suorakaiteen korkeus.
     */
    public static double kerroSuorakaiteenKorkeus( double isoakseli, double pikkuakseli, double kulma ) {
        
        double sini = Math.sin( kulma );
        double kosini = Math.cos( kulma );
        
        return Math.sqrt( 4 * isoakseli * isoakseli * sini * sini +
                		  4 * pikkuakseli * pikkuakseli * kosini * kosini );
        
    }
    
    /**
     * Luo uuden ellipsin muotoisen alueen polttopistevektorin ja halkaisijan perusteella.
     * @param polttopistevektori Vektori polttopisteest‰ 1 polttopisteeseen 2.
     * @param halkaisija Ellipsin halkaisija.
     * @throws Exception Poikkeus kertoo, ett‰ ellipsin halkaisija on pienempi kuin
     * polttopistevektorin pituus.
     */
    public AlueEllipsi( Vektori polttopistevektori, double halkaisija ) throws Exception {
        
        super( ( int )AlueEllipsi.kerroSuorakaiteenLeveys( AlueEllipsi.kerroIsoakseli( halkaisija ),
                										   AlueEllipsi.kerroPikkuakseli( polttopistevektori.kerroPituus(), halkaisija ),
	   	   		  										   polttopistevektori.kerroKulma() ), 
	   	   	   ( int )AlueEllipsi.kerroSuorakaiteenKorkeus( AlueEllipsi.kerroIsoakseli( halkaisija ),
	   	   	           										AlueEllipsi.kerroPikkuakseli( polttopistevektori.kerroPituus(), halkaisija ),
	   	   	           										polttopistevektori.kerroKulma() ) );

        this.isoakseli = AlueEllipsi.kerroIsoakseli( halkaisija );
        this.pikkuakseli = AlueEllipsi.kerroPikkuakseli( polttopistevektori.kerroPituus(), halkaisija );
        this.polttopistevektori = polttopistevektori;
        this.halkaisija = halkaisija;
        
    }
    
    public AlueEllipsi( AlueEllipsi kopio ) {
        
        super( kopio );
        this.polttopistevektori = new Vektori( kopio.polttopistevektori );
        this.halkaisija = kopio.halkaisija;
        this.pikkuakseli = kopio.pikkuakseli;
        this.isoakseli = kopio.isoakseli;
        
    }
    
    /**
     * Kertoo vektorin polttopisteest‰ yksi polttopisteeseen kaksi.
     * @return Ellipsin polttopistevektori.
     */
    public Vektori kerroPolttopistevektori() {
        
        return this.polttopistevektori;
        
    }
    
    /**
     * Asettaa polttopisteen 1 sijainnin. Eli asettaa alueen keskipisteen siihen
     * n‰hden oikeaan paikkaan.
     * @param sijainti Koordinaatit, joihin ellipsin polttopiste 1 sijoitetaan.
     */
    public void asetaPolttopisteenSijainti( Koordinaatit sijainti ) throws Exception {
        
        super.asetaSijainti( sijainti.kerroSuhteellisetKoordinaatit( this.polttopistevektori.kerroX() / 2,
                													 this.polttopistevektori.kerroY() / 2 ) );
        
    }
    
    /**
     * Asettaa ellipsin halkaisijan ja p‰ivitt‰‰ muut apumitat.
     * @param halkaisija Ellipsin uusi halkaisija.
     * @throws Exception Poikkeus, joka kertoo, ett‰ halkaisija on virheellinen.
     */
    public void asetaHalkaisija( double halkaisija ) throws Exception {
        
        double isoakseli = AlueEllipsi.kerroIsoakseli( halkaisija );
        double pikkuakseli = AlueEllipsi.kerroPikkuakseli( this.polttopistevektori.kerroPituus(), halkaisija );
        double kulma = this.polttopistevektori.kerroKulma();

        //T‰ss‰ voi lent‰‰ poikkeus, joten asetetaan muut mitat vasta t‰m‰n j‰lkeen.
        this.asetaMitat( ( int )AlueEllipsi.kerroSuorakaiteenLeveys( isoakseli,
                										   	   		 pikkuakseli,
                										   	   		 kulma ),
                         ( int )AlueEllipsi.kerroSuorakaiteenKorkeus( isoakseli,
                        										 	  pikkuakseli,
                        										 	  kulma ) );
        this.halkaisija = halkaisija;
        this.isoakseli = isoakseli;
        this.pikkuakseli = pikkuakseli;
      
    }
    
    /**
     * Kertoo ellipsin halkaisijan.
     * @return Ellipsin halkaisija.
     */
    public double kerroHalkaisija() {
        
        return this.halkaisija;
        
    }

    public double kerroAla() {
        
        return Math.PI * this.isoakseli * this.pikkuakseli;
        
    }

    /**
     * Kertoo ellipsin isoakselin pituuden.
     * @return Ellipsin isoakselin pituus.
     */
    public double kerroIsoakseli() {
        
        return this.isoakseli;
        
    }
    
    /**
     * Kertoo ellipsin pikkuakselin pituuden.
     * @return Ellipsin pikkuakselin pituus.
     */
    public double kerroPikkuakseli() {
        
        return this.pikkuakseli;
        
    }
    
    public double kerroEtaisyys( Koordinaatit sijainti ) throws Exception {
        
        Koordinaatit.tutkiVertailukelpoisuus( this.kerroSijainti(), sijainti );
        
        //T‰m‰ ei ole l‰hellek‰‰n tarkka arvo (ei kannata k‰ytt‰‰ tˆrm‰ystesteiss‰),
        //mutta antaa muuten riitt‰v‰n tarkan likiarvon.
        
        //Tutkitaan, kuinka paljon suurempi ellipsin halkaisijan t‰ytyisi olla, jotta
        //annettu piste olisi ellipsin keh‰ll‰.
        
        double sijaintiX = sijainti.kerroSijaintiX();
        double sijaintiY = sijainti.kerroSijaintiY();
        
        Koordinaatit polttopiste1Sijainti = this.kerroPolttopisteen1Sijainti();
        Koordinaatit polttopiste2Sijainti = this.kerroPolttopisteen2Sijainti();
        Vektori polttopiste1;
        Vektori polttopiste2;
        
        //Muodostetaan vektorit l‰hemm‰n polttopisteen perusteella. T‰m‰ t‰ytyy
        //tehd‰ jatkuvuuden takia, etteiv‰t polttopisteet ole saman ellipsin eri
        //"versioista".
        if ( polttopiste1Sijainti.kerroEtaisyys( sijaintiX, sijaintiY ) < polttopiste2Sijainti.kerroEtaisyys( sijaintiX, sijaintiY ) ) {
            
            polttopiste1 = new Vektori( polttopiste1Sijainti.kerroLyhinMatkaX( sijaintiX ),
                    					polttopiste1Sijainti.kerroLyhinMatkaY( sijaintiY ) );
            polttopiste2 = new Vektori( polttopiste1.kerroX() - this.polttopistevektori.kerroX(),
                    					polttopiste1.kerroY() - this.polttopistevektori.kerroY() );
            
        }
        else {
            
            polttopiste2 = new Vektori( polttopiste2Sijainti.kerroLyhinMatkaX( sijaintiX ),
                    					polttopiste2Sijainti.kerroLyhinMatkaY( sijaintiY ) );
            polttopiste1 = new Vektori( polttopiste2.kerroX() + this.polttopistevektori.kerroX(),
                    					polttopiste2.kerroY() + this.polttopistevektori.kerroY() );
            
        }

        //Ellipsin halkaisijan tuossa pisteess‰.
        double uusiHalkaisija = polttopiste1.kerroPituus() + polttopiste2.kerroPituus();
        
        //Jos piste alueen sis‰ll‰, et‰isyys on nolla.
        if ( uusiHalkaisija < this.halkaisija )
            return 0;
        
        return ( uusiHalkaisija - this.halkaisija ) / 2;
        
    }
    
    /**
     * Kertoo ellipsin polttopisteen yksi sijainnin.
     * @return Ellipsin polttopisteen yksi sijainti tai null, jos sijaintia ei ole.
     */
    public Koordinaatit kerroPolttopisteen1Sijainti() {
        
        Koordinaatit sijainti = this.kerroSijainti();
        
        if ( sijainti == null )
            return null;
        
        return sijainti.kerroSuhteellisetKoordinaatit( -0.5 * this.polttopistevektori.kerroX(),
                									   -0.5 * this.polttopistevektori.kerroY() );
        
    }
    
    /**
     * Kertoo ellipsin polttopisteen kaksi sijainnin.
     * @return Ellipsin polttopisteen kaksi sijainti tai null, jos sijaintia ei ole.
     */
    public Koordinaatit kerroPolttopisteen2Sijainti() {
        
        Koordinaatit sijainti = this.kerroSijainti();
        
        if ( sijainti == null )
            return null;
        
        return sijainti.kerroSuhteellisetKoordinaatit( 0.5 * this.polttopistevektori.kerroX(),
				   									   0.5 * this.polttopistevektori.kerroY() );
        
    }
    
    /**
     * Ellipsin polttopisteiden kautta kulkevat suorat muodostavat tangentin kanssa
     * samassa pisteess‰ yht‰ suuren kulman. T‰m‰n perusteella lasketaan ellipsin 
     * et‰normaalille pisteess‰ P aika tarkka arvio seuraavasti: Muodostetaan vektorit 
     * polttopisteist‰ pisteeseen P. Jatkuvuuden takia t‰ytyy valita se ellipsi, jonka
     * l‰hin polttopiste on l‰hinn‰. Muodostetaan toinen polttopisteest‰ l‰htenyt vektori
     * lyhyemm‰n perusteella, jotta ne ovat samasta ellipsist‰ l‰htevi‰ vektoreita.
     * Lasketaan n‰iden vektoreiden yksikkˆvektorit yhteen, niin tulos on et‰normaalivektori.
     */
    public Vektori kerroEtanormaali( Koordinaatit sijainti ) {
        
        if ( this.kerroSijainti() == null || sijainti == null ||
             this.kerroSijainti().kerroKoordinaatisto() != sijainti.kerroKoordinaatisto() ) {
            
            return null;
            
        }
        
        Koordinaatit polttopisteen1Sijainti = this.kerroPolttopisteen1Sijainti();
        Koordinaatit polttopisteen2Sijainti = this.kerroPolttopisteen2Sijainti();
        
        //Muodostetaan vektorit polttopisteist‰ sijaintiin.
        Vektori polttopiste1 = new Vektori( polttopisteen1Sijainti.kerroLyhinMatkaX( sijainti.kerroSijaintiX() ),
                							polttopisteen1Sijainti.kerroLyhinMatkaY( sijainti.kerroSijaintiY() ) );//-0.5 * this.polttopistevektori.kerroX(),

        Vektori polttopiste2 = new Vektori( polttopisteen2Sijainti.kerroLyhinMatkaX( sijainti.kerroSijaintiX() ),
											polttopisteen2Sijainti.kerroLyhinMatkaY( sijainti.kerroSijaintiY() ) ); //0.5 * this.polttopistevektori.kerroX(),

        
        //Muodostetaan toinen vektori toisen perusteella. Lyhyempi m‰‰r‰‰ tahdin.
        if ( polttopiste1.kerroPituus() < polttopiste2.kerroPituus() ) {
            
            polttopiste2 = Vektori.kerroSumma( polttopiste1,
                    						   Vektori.kerroTulo( this.polttopistevektori, -1 ) );
            
        }
        else {
            
            polttopiste1 = Vektori.kerroSumma( polttopiste2,
					   						   this.polttopistevektori );

        }
        
        //Lasketaan yksikkˆvektorit yhteen. 
        return Vektori.kerroSumma( polttopiste1.kerroYksikkovektori(),
                				   polttopiste2.kerroYksikkovektori() );
        
    }
    
    protected int leikkaustesti( Alue testattava ) {

        //T‰m‰ olio-ohjelmoinnin kannalta omituinen viittaus aliluokkaan on tehty,
        //jotta tˆrm‰ystestaus olisi mahdollisimman hyv‰.
        //Koska AlueEllipsi.leikkaustesti ja AlueYmpyra.leikkaustesti eiv‰t saa
        //antaa eri‰vi‰ vastauksia, on t‰m‰n toimittava ympyr‰n tapauksessa
        //kuten aliluokkansa. Kompromissi on tehty, jotta ympyr‰n ja ellipsin
        //leikkaustestaus saataisiin paremmaksi.
        if ( testattava instanceof AlueYmpyra ) {
            
            if ( AlueYmpyra.leikkaavatYmpyraJaEllipsi( ( AlueYmpyra )testattava, this ) )
                return 1;
            
            return 0;
            
        }
        
        if ( testattava instanceof AlueEllipsi ) {
            
            if ( AlueEllipsi.leikkaavatEllipsiJaEllipsi( this, ( AlueEllipsi )testattava ) )
                return 1;
            
            return 0;
        }

        //Ei testausta viel‰ --> -1
        return -1;
        
    }
    
    /**
     * Kertoo, leikkaavatko kaksi annetua ellipsi‰ toisensa. Ensin tutkitaan leikkaavatko
     * ellipsej‰ rajaavat suorakaiteet. Jos ne leikkaavat, tehd‰‰n tarkempi tutkimus.
     * Koska ellipsien leikkaustestille ei ole olemassa yksinkertaista lauseketta, vaan
     * ainoastaan erilaisia menetelmi‰, k‰ytet‰‰n t‰ss‰ leikkaustestiss‰ eritt‰in yksinkertaisia
     * ja nopeita menetelmi‰. Suurilla ellipseill‰ virheet ovat jo selv‰sti havaittavissa,
     * mutta pienill‰ virheet ovat mit‰ttˆm‰n pieni‰.
     * 
     * Leikkaustesti on yksinkertaisesti seuraavanlainen: Jos ellipsien polttopisteiden
     * v‰linen summa on alle halkaisijoiden kaksinkertaisen summan, alueet leikaavat.
     * T‰m‰ tarkastelu t‰ytyy suorittaa kaikkiin nelj‰‰n eri suuntaan, koska l‰himm‰n
     * keskipisteen tai polttopisteen tarkastelu ei tuo oikotiet‰.
     * @param ellipsi1 Leikkaustestin toinen ellipsin muotoinen alue.
     * @param ellipsi2 Leikkaustestin toinen ellipsin muotoinen alue.
     * @return Totuusarvo, joka kertoo, leikkaavatko ellipsit.
     */
    public static boolean leikkaavatEllipsiJaEllipsi( AlueEllipsi ellipsi1, AlueEllipsi ellipsi2 ) {

        if ( !Alue.leikkaavatSuorakulmioJaSuorakulmio( ellipsi1, ellipsi2 ) )
            return false;
        
        Vektori keskipistevektoriE1E2 = new Vektori( 0, 0 );
        
        //Muodostetaan polttopisteiden v‰liset vektorit. Huomaa kerroin 0.5 sek‰ suunta +/-!
        //Keskipisteiden v‰linen vektori lis‰t‰‰n sitten aina suunnan mukaan.
        Vektori polttopistevektoriE11E21 = new Vektori( 0.5 * ( ellipsi1.kerroPolttopistevektori().kerroX() - ellipsi2.kerroPolttopistevektori().kerroX() ),
                										0.5 * ( ellipsi1.kerroPolttopistevektori().kerroY() - ellipsi2.kerroPolttopistevektori().kerroY() ) );
        Vektori polttopistevektoriE11E22 = new Vektori( 0.5 * ( ellipsi1.kerroPolttopistevektori().kerroX() + ellipsi2.kerroPolttopistevektori().kerroX() ),
														0.5 * ( ellipsi1.kerroPolttopistevektori().kerroY() + ellipsi2.kerroPolttopistevektori().kerroY() ) );
        Vektori polttopistevektoriE12E21 = new Vektori( 0.5 * ( -ellipsi1.kerroPolttopistevektori().kerroX() - ellipsi2.kerroPolttopistevektori().kerroX() ),
														0.5 * ( -ellipsi1.kerroPolttopistevektori().kerroY() - ellipsi2.kerroPolttopistevektori().kerroY() ) );
        Vektori polttopistevektoriE12E22 = new Vektori( 0.5 * ( -ellipsi1.kerroPolttopistevektori().kerroX() + ellipsi2.kerroPolttopistevektori().kerroX() ),
                										0.5 * ( -ellipsi1.kerroPolttopistevektori().kerroY() + ellipsi2.kerroPolttopistevektori().kerroY() ) );

        for ( int ind = -1; ind <= 1; ind += 2 ) {

            for ( int jnd = -1; jnd <= 1; jnd += 2 ) {
                
                //Tutkitaan ellipsi ind ja jnd m‰‰r‰‰m‰ss‰ suunnassa.
                keskipistevektoriE1E2.aseta( ellipsi1.kerroSijainti().kerroMatkaX( ellipsi2.kerroSijainti().kerroSijaintiX(), ind ),
                        					 ellipsi1.kerroSijainti().kerroMatkaY( ellipsi2.kerroSijainti().kerroSijaintiY(), jnd ) );
                
                //Lis‰t‰‰n keskipistevektori.
                polttopistevektoriE11E21.lisaa( keskipistevektoriE1E2 );
                polttopistevektoriE11E22.lisaa( keskipistevektoriE1E2 );
                polttopistevektoriE12E21.lisaa( keskipistevektoriE1E2 );
                polttopistevektoriE12E22.lisaa( keskipistevektoriE1E2 );
                
                //Lasketaan polttopisteiden v‰liset et‰isyydet.
                double polttopisteidenEtaisyydet = polttopistevektoriE11E21.kerroPituus();
                polttopisteidenEtaisyydet += polttopistevektoriE11E22.kerroPituus();
                polttopisteidenEtaisyydet += polttopistevektoriE12E21.kerroPituus();
                polttopisteidenEtaisyydet += polttopistevektoriE12E22.kerroPituus();
                
                //Jos polttopisteiden yhteenlasketut et‰isyydet ovat alle 2 * halkaisijoiden
                //summa, ellipsit leikkaavat.
                if ( polttopisteidenEtaisyydet / 2 < ellipsi1.halkaisija + ellipsi2.halkaisija )
                    return true;
                
                //V‰hennet‰‰n keskipistevektori.
                polttopistevektoriE11E21.vahenna( keskipistevektoriE1E2 );
                polttopistevektoriE11E22.vahenna( keskipistevektoriE1E2 );
                polttopistevektoriE12E21.vahenna( keskipistevektoriE1E2 );
                polttopistevektoriE12E22.vahenna( keskipistevektoriE1E2 );
                
            }
            
        }
        
        return false;

    }

}
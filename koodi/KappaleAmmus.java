
/**
 *
 * Luokka <CODE>KappaleAmmus</CODE> kuvaa sellaista liikkuvaa kappaletta, joka
 * tekee tuhoa osuessaan. Ammuksella voi olla jokin elinaika sekä ampuja. Ammus myös
 * toteuttaa animoinnin, mikäli sen kuvasarjassa on useampia frameja. Frame
 * valitaan ihan puhtaasti siten että elinkaarensa aikana edetään tasaisesti
 * frame framelta.
 *  
 * @author Jaakko Luttinen
 *
 */
public class KappaleAmmus extends KappaleLiikkuva implements RajapintaTuhoatekeva {
    
    /**
     * Kappale, joka ammuksen on ampunut.
     */
    private Kappale ampuja;
    
    /**
     * Kun kappale ammutaan se syntyy ampujansa alle. Se ei kuitenkaan saa osua tällöin
     * ampujaansa. Tämä totuusarvo kertoo, onko ammus vielä ampujansa alla.
     */
    private boolean omistajanAlla;
    
    /**
     * Kappaleen elinaika (ms) tai nolla, jos elinajalla ei ole rajaa.
     */
    private int elinaika;
    
    /**
     * Kuinka kauan kappale on ollut elossa.
     */
    private int elonKesto;
    
    /**
     * Ammuksen tuhovoima.
     */
    private double tuhovoima;
    
    /**
     * Ammuksen lähtönopeus.
     */
    private double lahtonopeus;

    /**
     * Luo uuden ammuksen.
     * @param tyyppi Kappaleen merkkijonotunniste. Voi olla null.
     * @param kuva Kappaleen kuvasarja.
     * @param lahtonopeus Ammuksen lähtönopeus.
     * @param elinaika Ammuksen elinaika tai nolla, jos ei rajaa.
     * @param tuhovoima Ammuksen tuhovoima.
	 * @throws Exception Poikkeus kertoo, että annetuissa parametreissa oli virhe.
     */
    public KappaleAmmus( String tyyppi, Kuvasarja kuva, double lahtonopeus, int elinaika, double tuhovoima ) throws Exception {
        
        super( tyyppi, kuva );
        
        if ( elinaika < 0 )
            throw new Exception( "Ei voi luoda ammusta: Elinaika ei saa olla negatiivinen." );
        
        if ( lahtonopeus < 0 )
            throw new Exception( "Ei voi luoda ammusta: Lähtönopeus ei saa olla negatiivinen." );
        
        if ( tuhovoima < 0 )
            throw new Exception( "Ei voi luoda ammusta: Tuhovoima ei saa olla negatiivinen." );
        
        this.lahtonopeus = lahtonopeus;
        this.elinaika = elinaika;
        this.tuhovoima = tuhovoima;
        this.asetaTormattavyys( false );
        
    }

    /**
     * Luo uuden ammuksen annetun kopion perusteella.
     * @param kopio Ammus, jonka perusteella uusi ammus luodaan.
     */
    public KappaleAmmus( KappaleAmmus kopio ) {
        
        super( kopio );
        this.lahtonopeus = kopio.lahtonopeus;
        this.elinaika = kopio.elinaika;
        this.tuhovoima = kopio.tuhovoima;
        this.asetaTormattavyys( false );
       
    }
    
    /**
     * Luo uuden ammuksen tämän ammuksen perusteella. Kaikkien aliluokkien
     * tulisi kirjoittaa oma toteutuksensa tälle metodille.
     * @return Kopio ammuksesta.
     */
    public KappaleAmmus kerroKopio() {
        
        return new KappaleAmmus( this );
        
    }
    
    /**
     * Asettaa ammuksen ampujan.
     * @param ampuja Ammuksen ampuja.
     */
    public void asetaAmpuja( Kappale ampuja ) {
        
        this.ampuja = ampuja;
        
    }
    
    public Kappale kerroTuhonLahde() {
        
        return this.ampuja;
        
    }

    /**
     * Kertoo ammuksen alkuperäisen tuhovoiman.
     * @return Ammuksen alkuperäinen tuhovoima.
     */
    public double kerroTuhovoimaAlussa() {
        
        return this.tuhovoima;
        
    }
    
    public double kerroTuhovoima() {
        
        return this.kerroTuhovoimaAlussa();
        
    }
 
    public void asetaMaailmaan( Avaruus maailma, double sijaintiX, double sijaintiY ) throws Exception {
        
        super.asetaMaailmaan( maailma, sijaintiX, sijaintiY );
        this.elonKesto = 0;
        this.omistajanAlla = true;
        
    }

    public boolean suoritaTormays( Kappale tormattava ) {

        if ( tormattava == null )
            return false;
        
        if ( tormattava.onTormattavissa() && ( tormattava != this.ampuja || !this.omistajanAlla ) ) {
            
            boolean tekiTuhoa = false;
     
            //Ei yritetä tehdä tuhoa, jos kohde on ampuja ja ampujaan ei saa tehdä tuhoa.
            if ( tormattava != this.ampuja || this.tuhoaaAmpujaa() )
                tekiTuhoa = tormattava.suoritaOsuma( this );
            
            //Annettaan ammuksen tehdä toimenpiteitä ja ammus niin pyytää, niin suoritetaan
            //normaali liikkuvan kappaleen törmäystarkastelu (kimmahdus).
            if ( this.osumaSuoritettu( tormattava, tekiTuhoa ) && !this.onTuhoutunut() ) {
                
                //Halutaan tehdä normaali törmäystarkastelu...
                super.suoritaTormays( tormattava );
                //Sijainti on laiton, koska tehdään törmäys tarkastelu, vaikka
                //ammus oikeasti onkin ei-törmättävissä.
                return true;
                
            }
            
        }
        
        //Ammuksen sijainti ei ole nyt laiton.
        return false;
        
    }
    
    /**
     * Tekee toimenpiteet, kun ammus on törmännyt annettuun kappaleeseen. Totuusarvo
     * kertoo, onko tuhoa mennyt läpi. Metodin ei tule enää tehdä uudestaan tuhoa kappaleeseen.
     * Palautusarvo kertoo, tehdäänkö ammukselle normaali liikkuvan kappaleen
     * törmäyskäsittely (kimpoaminen).
     * @param osuttava Kappale, johon ammus on osunut.
     * @param osuttu Totuusarvo, joka kertoo, saiko ammus tehtyä tuhoa kappaleeseen.
     * @return Totuusarvo, joka kertoo, tehdäänkö kappaleelle vielä normaali liikkuvan
     * kappaleen törmäyskäsittely (kimpoaminen).
     */
    protected boolean osumaSuoritettu( Kappale osuttava, boolean osuttu ) {
        
        this.tuhoudu();
        return false;
        
    }

    /**
     * Kertoo, tekeekö ammus tällä hetkellä tuhoa ampujaansa.
     * @return Totuusarvo, joka kertoo, tekeekö ammus tuhoa ampujaansa.
     */
	public boolean tuhoaaAmpujaa() {
	    
	    return !this.omistajanAlla;
	    
	}
   /**
     * Kertoo ammuksen lähtönopeuden.
     * @return Ammuksen lähtönopeus.
     */
    public double kerroLahtonopeus() {
        
        return this.lahtonopeus;
        
    }
    
    /**
     * Kertoo ammuksen alkuperäisen elinajan.
     * @return Ammuksen alkuperäinen elinaika.
     */
    public int kerroElinaikaAlussa() {
        
        return this.elinaika;
        
    }
    
    /**
     * Kertoo ammuksen jäljellä olevan elinajan.
     * @return Ammuksen jäljellä oleva elinaika.
     */
    public int kerroElinaika() {
        
        return this.elinaika - this.elonKesto;
        
    }
    
    public int kerroFrame() {
        
        //Ammus on animoitu.
        return this.kerroKuva().kerroKuvienMaara() * this.elonKesto / this.elinaika;
        
    }

    /**
     * Tutkii, onko ammus vielä ampujansa alla, liikuttaa alusta ja tuhoaa ammuksen, jos
     * sen elinaika on loppunut.
     * @param aika Sykäyksen aika.
     */
    protected void toimi( int aika ) {
 
        if ( this.omistajanAlla && this.ampuja != null ) {
            
            //Katsotaan, onko ammus enää ampujan alla, jos ampuja edes on enää maailmassa (voinut tuhoutuakin)...
            if ( this.ampuja.kerroMaailma() == this.kerroMaailma() && !this.leikkaavat( this.ampuja ) ) {
                
                this.omistajanAlla = false;
                
            }
            
        }

        super.toimi( aika );
             
        this.elonKesto += aika;

        //Katsotaan, onko ammuksen aika kuolla, jos elinaika on määritetty.
        if ( this.elinaika > 0 ) {
            
            if ( this.elonKesto >= this.elinaika )
                this.tuhoudu();
            
        }
        
    }
    
}
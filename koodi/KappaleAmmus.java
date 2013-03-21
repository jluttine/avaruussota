
/**
 *
 * Luokka <CODE>KappaleAmmus</CODE> kuvaa sellaista liikkuvaa kappaletta, joka
 * tekee tuhoa osuessaan. Ammuksella voi olla jokin elinaika sek� ampuja. Ammus my�s
 * toteuttaa animoinnin, mik�li sen kuvasarjassa on useampia frameja. Frame
 * valitaan ihan puhtaasti siten ett� elinkaarensa aikana edet��n tasaisesti
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
     * Kun kappale ammutaan se syntyy ampujansa alle. Se ei kuitenkaan saa osua t�ll�in
     * ampujaansa. T�m� totuusarvo kertoo, onko ammus viel� ampujansa alla.
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
     * Ammuksen l�ht�nopeus.
     */
    private double lahtonopeus;

    /**
     * Luo uuden ammuksen.
     * @param tyyppi Kappaleen merkkijonotunniste. Voi olla null.
     * @param kuva Kappaleen kuvasarja.
     * @param lahtonopeus Ammuksen l�ht�nopeus.
     * @param elinaika Ammuksen elinaika tai nolla, jos ei rajaa.
     * @param tuhovoima Ammuksen tuhovoima.
	 * @throws Exception Poikkeus kertoo, ett� annetuissa parametreissa oli virhe.
     */
    public KappaleAmmus( String tyyppi, Kuvasarja kuva, double lahtonopeus, int elinaika, double tuhovoima ) throws Exception {
        
        super( tyyppi, kuva );
        
        if ( elinaika < 0 )
            throw new Exception( "Ei voi luoda ammusta: Elinaika ei saa olla negatiivinen." );
        
        if ( lahtonopeus < 0 )
            throw new Exception( "Ei voi luoda ammusta: L�ht�nopeus ei saa olla negatiivinen." );
        
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
     * Luo uuden ammuksen t�m�n ammuksen perusteella. Kaikkien aliluokkien
     * tulisi kirjoittaa oma toteutuksensa t�lle metodille.
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
     * Kertoo ammuksen alkuper�isen tuhovoiman.
     * @return Ammuksen alkuper�inen tuhovoima.
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
     
            //Ei yritet� tehd� tuhoa, jos kohde on ampuja ja ampujaan ei saa tehd� tuhoa.
            if ( tormattava != this.ampuja || this.tuhoaaAmpujaa() )
                tekiTuhoa = tormattava.suoritaOsuma( this );
            
            //Annettaan ammuksen tehd� toimenpiteit� ja ammus niin pyyt��, niin suoritetaan
            //normaali liikkuvan kappaleen t�rm�ystarkastelu (kimmahdus).
            if ( this.osumaSuoritettu( tormattava, tekiTuhoa ) && !this.onTuhoutunut() ) {
                
                //Halutaan tehd� normaali t�rm�ystarkastelu...
                super.suoritaTormays( tormattava );
                //Sijainti on laiton, koska tehd��n t�rm�ys tarkastelu, vaikka
                //ammus oikeasti onkin ei-t�rm�tt�viss�.
                return true;
                
            }
            
        }
        
        //Ammuksen sijainti ei ole nyt laiton.
        return false;
        
    }
    
    /**
     * Tekee toimenpiteet, kun ammus on t�rm�nnyt annettuun kappaleeseen. Totuusarvo
     * kertoo, onko tuhoa mennyt l�pi. Metodin ei tule en�� tehd� uudestaan tuhoa kappaleeseen.
     * Palautusarvo kertoo, tehd��nk� ammukselle normaali liikkuvan kappaleen
     * t�rm�ysk�sittely (kimpoaminen).
     * @param osuttava Kappale, johon ammus on osunut.
     * @param osuttu Totuusarvo, joka kertoo, saiko ammus tehty� tuhoa kappaleeseen.
     * @return Totuusarvo, joka kertoo, tehd��nk� kappaleelle viel� normaali liikkuvan
     * kappaleen t�rm�ysk�sittely (kimpoaminen).
     */
    protected boolean osumaSuoritettu( Kappale osuttava, boolean osuttu ) {
        
        this.tuhoudu();
        return false;
        
    }

    /**
     * Kertoo, tekeek� ammus t�ll� hetkell� tuhoa ampujaansa.
     * @return Totuusarvo, joka kertoo, tekeek� ammus tuhoa ampujaansa.
     */
	public boolean tuhoaaAmpujaa() {
	    
	    return !this.omistajanAlla;
	    
	}
   /**
     * Kertoo ammuksen l�ht�nopeuden.
     * @return Ammuksen l�ht�nopeus.
     */
    public double kerroLahtonopeus() {
        
        return this.lahtonopeus;
        
    }
    
    /**
     * Kertoo ammuksen alkuper�isen elinajan.
     * @return Ammuksen alkuper�inen elinaika.
     */
    public int kerroElinaikaAlussa() {
        
        return this.elinaika;
        
    }
    
    /**
     * Kertoo ammuksen j�ljell� olevan elinajan.
     * @return Ammuksen j�ljell� oleva elinaika.
     */
    public int kerroElinaika() {
        
        return this.elinaika - this.elonKesto;
        
    }
    
    public int kerroFrame() {
        
        //Ammus on animoitu.
        return this.kerroKuva().kerroKuvienMaara() * this.elonKesto / this.elinaika;
        
    }

    /**
     * Tutkii, onko ammus viel� ampujansa alla, liikuttaa alusta ja tuhoaa ammuksen, jos
     * sen elinaika on loppunut.
     * @param aika Syk�yksen aika.
     */
    protected void toimi( int aika ) {
 
        if ( this.omistajanAlla && this.ampuja != null ) {
            
            //Katsotaan, onko ammus en�� ampujan alla, jos ampuja edes on en�� maailmassa (voinut tuhoutuakin)...
            if ( this.ampuja.kerroMaailma() == this.kerroMaailma() && !this.leikkaavat( this.ampuja ) ) {
                
                this.omistajanAlla = false;
                
            }
            
        }

        super.toimi( aika );
             
        this.elonKesto += aika;

        //Katsotaan, onko ammuksen aika kuolla, jos elinaika on m��ritetty.
        if ( this.elinaika > 0 ) {
            
            if ( this.elonKesto >= this.elinaika )
                this.tuhoudu();
            
        }
        
    }
    
}
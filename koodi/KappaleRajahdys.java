
/**
 * 
 * Luokka <CODE>KappaleRajahdys</CODE> kuvaa kappaletta, joka on paikallaan,
 * el‰‰ joitakin sekunteja ja on ulkon‰ˆlt‰‰n animoitu r‰j‰hdys. R‰j‰hdyst‰
 * k‰sitell‰‰n avaruudessa ympyr‰n muotoisena alueena. R‰j‰hdys ei vaikuta
 * muihin kappaleisiin mitenk‰‰n.
 *
 * @author Jaakko Luttinen
 *
 */
public class KappaleRajahdys extends Kappale {

    /**
     * Aika, kuinka kauan r‰j‰hdys saa el‰‰.
     */
    private long elinika;
    
    /**
     * Aika, kuinka kauan r‰j‰hdys on el‰nyt.
     */
    private long elonKesto;
    
    /**
     * R‰j‰hdyksen alue.
     */
    private AlueYmpyra sijaintiAlue;
    
    /**
     * Luo uuden r‰j‰hdyksen.
     * @param tyyppi Kappaleen merkkijonotunniste. Voi olla null.
     * @param kuva Kappaleen kuvasarja.
     * @param elinika Kappaleen elinaika millisekunteina tai 0, jos ei rajaa.
	 * @throws Exception Poikkeus kertoo, ett‰ annetuissa parametreissa oli virhe.
     */
    public KappaleRajahdys( String tyyppi, Kuvasarja kuva, long elinika ) throws Exception {
        
        super( tyyppi, kuva );
        
        if ( elinika < 0 )
            throw new Exception( "Ei voi luoda r‰j‰hdyst‰: Elinik‰ oltava ep‰negatiivinen." );

        this.sijaintiAlue = new AlueYmpyra( ( kuva.kerroLeveys() + kuva.kerroKorkeus() ) / 4.0 );
        this.elinika = elinika;
        this.asetaTormattavyys( false );
        
    }
    
    /**
     * Luo uuden r‰j‰hdyksen annetun r‰j‰hdyksen perusteella.
     * @param kopio R‰j‰hdys, jonka perusteella uusi r‰j‰hdys luodaan.
     */
    public KappaleRajahdys( KappaleRajahdys kopio ) {
        
        super( kopio );

        this.elinika = kopio.elinika;
        this.sijaintiAlue = new AlueYmpyra( kopio.sijaintiAlue );
        this.sijaintiAlue.poistaSijainti();
        this.asetaTormattavyys( false );
        
    }
    
    public Alue kerroAlue() {
        
        return this.sijaintiAlue;
        
    }

    public int kerroFrame() {
        
        return ( int )( this.kerroKuva().kerroKuvienMaara() * this.elonKesto / this.elinika );
        
    }

    public void asetaMaailmaan( Avaruus maailma, double sijaintiX, double sijaintiY ) throws Exception {
        
        super.asetaMaailmaan( maailma, sijaintiX, sijaintiY );
        this.elonKesto = 0;
        
    }
    
    public void toimi( int kulunutAika ) {
        
        this.elonKesto += kulunutAika;
        
        //El‰nyt tarpeeksi kauan, aika tuhoutua.
        if ( this.elonKesto >= this.elinika )
            this.tuhoudu();
        
    }
    
}
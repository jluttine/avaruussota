
/**
 *
 * Luokka <CODE>KappaleAmmusLiekki</CODE> kuvaa liekkiammusta. Tällainen ammus
 * ei välttämättä tuhoudu osuessaan. Ammus pienenee ja sen tuhovoima heikkenee
 * ajan kuluessa. Tuhovoima riippuu myös ajasta, kuinka kauan liekki leikkaa
 * kohdetta (vrt. normaaliin ammukseen, jossa tuhovoima on joka osumalla aina sama).
 *  
 * @author Jaakko Luttinen
 *
 */
public class KappaleAmmusLiekki extends KappaleAmmus {
    
    /**
     * Muuttuja pitää tallessa edellisen sykäyksen suuruutta. Näin ollen tuhovoim
     */
    private int viimeAika;
    
    /**
     * Luo uuden liekkiammuksen.
     * @param tyyppi Kappaleen merkkijonotunniste. Voi olla null.
     * @param kuva Kappaleen kuvasarja.
     * @param lahtonopeus Ammuksen lähtönopeus.
     * @param elinaika Ammuksen elinaika.
     * @param tuhovoima Ammuksen tuhovoima.
	 * @throws Exception Poikkeus kertoo, että annetuissa parametreissa oli virhe.
     */
    public KappaleAmmusLiekki( String tyyppi, Kuvasarja kuva, double lahtonopeus, int elinaika, double tuhovoima ) throws Exception {
        
        super( tyyppi, kuva, lahtonopeus, elinaika, tuhovoima );
        
    }

    /**
     * Luo uuden liekkiammuksen annetun mallin perusteella.
     * @param kopio 
     */
    public KappaleAmmusLiekki( KappaleAmmusLiekki kopio ) {
        
        super( kopio );
       
    }
    
    public KappaleAmmus kerroKopio() {
        
        return new KappaleAmmusLiekki( this );
        
    }

    public boolean osumaSuoritettu( Kappale osuttava, boolean osuttu ) {
        
        //Liekki sujahtaa liikkuvista kappaleista läpi, mutta kiinteisiin esineisiin se tyssää.
        if ( !( osuttava instanceof KappaleLiikkuva ) )
            this.tuhoudu();
            
        //Liekki ei missään tilanteessa kimpoa.
        return false;
        
    }
    
    /**
     * Kertoo tuhovoiman juuri tällä hetkellä. Tuhovoima riippuu viime sykäyksen
     * ajasta sekä suhteellisen elinajan neliöjuuresta. 
     */
    public double kerroTuhovoima() {
        
        return this.viimeAika * this.kerroTuhovoimaAlussa() * Math.sqrt( ( double )this.kerroElinaika() / this.kerroElinaikaAlussa() );
        
    }

    /**
     * Pienentää liekin halkaisijaa jäljellä olevaan aikaan verrannolisesti ja suorittaa
     * normaalit ammuksen toimenpiteet. 
     * @param aika Sykäyksen aika.
     */
    public void toimi( int aika ) {
        
        //Otetaan muistiin tämä aika. Liekin tuhovoima kerrotaan koko tältä ajalta, koska
        //tuhovoima on kerrotuu millisekunteja kohden.
        this.viimeAika = aika;

        //Yritetään tehdä liekistä pienempi. Jos ei onnistu, niin eipä sitten mitään.
        try {
            
            ( ( AlueYmpyra )this.kerroAlue() ).asetaHalkaisija( ( double )this.kerroKuva().kerroLeveys() * this.kerroElinaika() / this.kerroElinaikaAlussa() );
            
        }
        catch ( Exception virhe ) {  }
        
        super.toimi( aika );
        
    }
    
}
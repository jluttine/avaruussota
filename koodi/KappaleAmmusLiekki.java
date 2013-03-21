
/**
 *
 * Luokka <CODE>KappaleAmmusLiekki</CODE> kuvaa liekkiammusta. T�llainen ammus
 * ei v�ltt�m�tt� tuhoudu osuessaan. Ammus pienenee ja sen tuhovoima heikkenee
 * ajan kuluessa. Tuhovoima riippuu my�s ajasta, kuinka kauan liekki leikkaa
 * kohdetta (vrt. normaaliin ammukseen, jossa tuhovoima on joka osumalla aina sama).
 *  
 * @author Jaakko Luttinen
 *
 */
public class KappaleAmmusLiekki extends KappaleAmmus {
    
    /**
     * Muuttuja pit�� tallessa edellisen syk�yksen suuruutta. N�in ollen tuhovoim
     */
    private int viimeAika;
    
    /**
     * Luo uuden liekkiammuksen.
     * @param tyyppi Kappaleen merkkijonotunniste. Voi olla null.
     * @param kuva Kappaleen kuvasarja.
     * @param lahtonopeus Ammuksen l�ht�nopeus.
     * @param elinaika Ammuksen elinaika.
     * @param tuhovoima Ammuksen tuhovoima.
	 * @throws Exception Poikkeus kertoo, ett� annetuissa parametreissa oli virhe.
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
        
        //Liekki sujahtaa liikkuvista kappaleista l�pi, mutta kiinteisiin esineisiin se tyss��.
        if ( !( osuttava instanceof KappaleLiikkuva ) )
            this.tuhoudu();
            
        //Liekki ei miss��n tilanteessa kimpoa.
        return false;
        
    }
    
    /**
     * Kertoo tuhovoiman juuri t�ll� hetkell�. Tuhovoima riippuu viime syk�yksen
     * ajasta sek� suhteellisen elinajan neli�juuresta. 
     */
    public double kerroTuhovoima() {
        
        return this.viimeAika * this.kerroTuhovoimaAlussa() * Math.sqrt( ( double )this.kerroElinaika() / this.kerroElinaikaAlussa() );
        
    }

    /**
     * Pienent�� liekin halkaisijaa j�ljell� olevaan aikaan verrannolisesti ja suorittaa
     * normaalit ammuksen toimenpiteet. 
     * @param aika Syk�yksen aika.
     */
    public void toimi( int aika ) {
        
        //Otetaan muistiin t�m� aika. Liekin tuhovoima kerrotaan koko t�lt� ajalta, koska
        //tuhovoima on kerrotuu millisekunteja kohden.
        this.viimeAika = aika;

        //Yritet��n tehd� liekist� pienempi. Jos ei onnistu, niin eip� sitten mit��n.
        try {
            
            ( ( AlueYmpyra )this.kerroAlue() ).asetaHalkaisija( ( double )this.kerroKuva().kerroLeveys() * this.kerroElinaika() / this.kerroElinaikaAlussa() );
            
        }
        catch ( Exception virhe ) {  }
        
        super.toimi( aika );
        
    }
    
}
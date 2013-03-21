
/**
 * 
 * Luokka <CODE>Ase</CODE> kuvaa asetta, joka voi ampua tietty‰ ammustyyppi‰
 * tietyin v‰liajoin. Aseessa on liipaisin, jonka voi asettaa pohjaan. Aseelle
 * v‰litet‰‰n syk‰yksi‰, jolloin se yritt‰‰ ampua ja palauttaa laukaistun ammuksen.
 * 
 * @author Jaakko Luttinen
 *
 */
public class Ase {
    
    /**
     * Aseen k‰ytt‰m‰ ammusmalli.
     */
    private KappaleAmmus ammustyyppi;
    
    /**
     * V‰liaika kahden laukauksen v‰lill‰.
     */
    private int intervalli;
    
    /**
     * Aika, joka on kulunut viime laukauksesta.
     */
    private long aikaaLaukauksesta;
    
    /**
     * Totuusarvo kertoo, onko liipaisin pohjassa.
     */
    private boolean ammutaan;
    
    /**
     * Luo uuden aseen.
     * @param ammustyyppi Aseen k‰ytt‰m‰ ammus.
     * @param intervalli Aika (ms) kahden laukausen v‰lill‰.
     * @throws Exception Poikkeus kertoo, ett‰ annetut parametrit olivat virheelliset.
     */
    public Ase( KappaleAmmus ammustyyppi, int intervalli ) throws Exception {
        
        if ( ammustyyppi == null )
            throw new Exception( "Asetta ei voida luoda: Ammusta ei ole m‰‰ritetty." );
        
        if ( intervalli < 0 )
            throw new Exception( "Asetta ei voida luoda: Intervalli ei saa olla negatiivinen." );
        
        this.ammustyyppi = ammustyyppi;
        this.intervalli = intervalli;
        
    }
    
    /**
     * Luo uuden aseen annetun mallin perusteella.
     * @param kopio Ase, jonka perusteella uusi ase luodaan.
     */
    public Ase( Ase kopio ) {
        
        this.ammustyyppi = kopio.ammustyyppi;
        this.intervalli = kopio.intervalli;
        
    }
    
    /**
     * Kertoo aseen k‰ytt‰m‰n ammuksen.
     * @return Aseen k‰ytt‰m‰ ammus.
     */
    public KappaleAmmus kerroAmmusmalli() {
        
        return this.ammustyyppi;
        
    }
    
    /**
     * Jos aikaa viime laukauksesta on kulunut tarpeeksi, luo uuden ammuksen
     * ammusmallia kopioimalla ja korjaa viime laukauksesta kuluneen ajan.
     * Jos tarpeeksi aikaa ei ole kulunut, palauttaa null.
     * @return Ammusmallin kopio tai null.
     */
    private KappaleAmmus yritaAmpua() {
        
        if ( this.aikaaLaukauksesta >= this.intervalli ) {
            
            this.aikaaLaukauksesta -= this.intervalli;
            return this.ammustyyppi.kerroKopio();
            
        }
        
        return null;
        
    }
    
    /**
     * Asettaa aseen ampumistilan.
     * @param ammutaan Totuusarvo kertoo, onko aseen liipaisin pohjassa.
     */
    public void asetaLiipaisin( boolean ammutaan ) {
        
        this.ammutaan = ammutaan;
        
    }
    
    /**
     * Antaa aseen toimia annetun ajan verran. Jos ase onnistuu ampumaan laukauksen,
     * palauttaa ammutun ammuksen, muuten palauttaa null.
     * @param aikaaKulunut Kulunut aika millisekunteina.
     * @return Ammuttu ammus tai null.
     */
    public KappaleAmmus toimi( long aikaaKulunut ) {
        
        this.aikaaLaukauksesta += aikaaKulunut;
        
        if ( !this.ammutaan ) {
            
            if ( this.aikaaLaukauksesta > this.intervalli )
                this.aikaaLaukauksesta = this.intervalli;
            
            return null;
            
        }
        
        return yritaAmpua();
        
    }
    
}
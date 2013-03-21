
/**
 * 
 * Luokka <CODE>Nappain</CODE> kuvaa n‰pp‰int‰, jolla on jokin n‰pp‰inkoodi (key code)
 * ja tila (painettu/ylh‰‰ll‰).
 * 
 * @author Jaakko Luttinen
 *
 */
public class Nappain {
    
    /**
     * N‰pp‰inkoodi, key code.
     */
    private int nappainkoodi;
    
    /**
     * Totuusarvo kertoo, onko n‰pp‰in pohjassa.
     */
    private boolean nappaintila;
    
    /**
     * Luo uuden n‰pp‰imen, jolla on annettu n‰pp‰inkoodi.
     * @param nappainkoodi N‰pp‰imen key code.
     */
    public Nappain( int nappainkoodi ) {
        
        this.nappainkoodi = nappainkoodi;
        this.nappaintila = false;
        
    }
    
    /**
     * Asettaa n‰pp‰imen tilan.
     * @param pohjassa Totuusarvo kertoo, onko n‰pp‰in pohjassa.
     */
    public void asetaTila( boolean pohjassa ) {
        
        this.nappaintila = pohjassa;
        
    }
    
    /**
     * Kertoo, onko n‰pp‰in pohjassa.
     * @return Totuusarvo kertoo, onko n‰pp‰in pohjassa.
     */
    public boolean onPohjassa() {
        
        return this.nappaintila;
        
    }
    
    /**
     * Kertoo n‰pp‰imen koodin (key code).
     * @return N‰pp‰inkoodi.
     */
    public int kerroNappainkoodi() {
        
        return this.nappainkoodi;
        
    }
    
}
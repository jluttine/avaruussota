
/**
 * 
 * Luokka <CODE>Koordinaatisto<CODE> kuvaa tason koordinaatistoa, jonka
 * leveys ja korkeus pysyv‰t vakioina.
 * 
 * @author Jaakko Luttinen
 *
 */

public class Koordinaatisto {
    
    /**
     * Koordinaatiston leveys.
     */
    private int leveys;
    
    /**
     * Koordinaatiston korkeus.
     */
    private int korkeus;

    /**
     * Luo uuden koordinaatiston annetulla leveydell‰ ja korkeudella. Mittojen t‰ytyy olla
     * positiiviset tai konstruktori heitt‰‰ poikkeuksen <CODE>Exception</CODE>.
     * @param leveys Koordinaatiston leveys.
     * @param korkeus Koordinaatiston korkeus.
     * @throws Exception Poikkeus, joka heitet‰‰n, jos leveys tai korkeus oli ep‰positiivinen.
     */
    public Koordinaatisto( int leveys, int korkeus ) throws Exception {
        
        if ( leveys <= 0 || korkeus <= 0 )
            throw new Exception( "Koordinaatiston mittojen t‰ytyy olla positiivisia." );
        
        this.leveys = leveys;
        this.korkeus = korkeus;
        
    }
    
    /**
     * Kertoo koordinaatiston leveyden.
     * @return Positiivinen kokonaisluku, joka kertoo koordinaatiston leveyden.
     */
    public int kerroLeveys() {
        
        return this.leveys;
        
    }
    
    /**
     * Kertoo koodinaatiston korkeuden.
     * @return Positiivinen kokonaisluku, joka kertoo koordinaatiston korkeuden.
     */
    public int kerroKorkeus() {
        
        return this.korkeus;
        
    }
    
}
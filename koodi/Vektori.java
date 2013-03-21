
/**
 * 
 * Kuvaa tason vektoreita koordinaatistossa.
 *
 * @author Jaakko Luttinen
 *
 */

public class Vektori {
    
    /**
     * Vektorin x-komponentti.
     */
    private double komponenttiX;
    
    /**
     * Vektorin y-komponentti.
     */
    private double komponenttiY;
    
    /**
     * Palauttaa kahden vektorin pistetulon.
     * @param vektori1 Pistetulon toinen vektori.
     * @param vektori2 Pistetulon toinen vektori.
     * @return Kahden vektorin pistetulo.
     */
    public static double kerroPistetulo( Vektori vektori1, Vektori vektori2 ) {
        
        return ( vektori1.komponenttiX * vektori2.komponenttiX ) + ( vektori1.komponenttiY * vektori2.komponenttiY );
        
    }
    
    /**
     * Palauttaa kahden vektorin v‰lisen kulman v‰lill‰ 0 - 2*PI. Kulma lasketaan
     * vektorista <CODE>vektori1</CODE> kasvavan kulman suuntaan.
     * @param vektori1 Vektori, josta kulma mitataan kasvavaan suuntaan.
     * @param vektori2 Vektori, johon kulma mitataan.
     * @return Vektoreiden v‰linen kulma radiaaneina.
     */
    public static double kerroKulma( Vektori vektori1, Vektori vektori2 ) {
        
        return Kulma.korjaaKulma( vektori2.kerroKulma() - vektori1.kerroKulma() );
        
    }
    
    /**
     * Palauttaa kahden parametrina saadun vektorin summavektorin.
     * @param vektori1 Vektori, joka on summan toinen osapuoli.
     * @param vektori2 Vektori, joka on summan toinen osapuoli.
     * @return Parametrina saatujen vektoreiden summavektori.
     */
    public static Vektori kerroSumma( Vektori vektori1, Vektori vektori2 ) {
        
        return new Vektori( vektori1.komponenttiX + vektori2.komponenttiX, vektori1.komponenttiY + vektori2.komponenttiY );
        
    }
    
    /**
     * Kertoo parametrina annetun vektorin, annetulla luvulla skaalatun vektorin. Annettu
     * vektori pysyy muuttumattomana ja palautusarvoa varten luodaan uusi vektori.
     * @param skaalattava Vektori, jonka skaalaus halutaan saada.
     * @param luku Luku, jolla vektoria halutaan skaalata.
     * @return Vektori, joka on vektorin <CODE>skaalattava</CODE> skaalaus.
     */
    public static Vektori kerroTulo( Vektori skaalattava, double luku ) {
        
        return new Vektori( luku * skaalattava.komponenttiX, luku * skaalattava.komponenttiY );
        
    }
   
    /**
     * Luo uuden vektorin, jolla on annetut x- ja y-komponentit.
     * @param komponenttiX Vektorin x-komponentti.
     * @param komponenttiY Vektorin y-komponentti.
     */
    public Vektori( double komponenttiX, double komponenttiY ) {
        
        this.aseta( komponenttiX, komponenttiY );
        
    }
    
    /**
     * Luo uuden vektorin, joka on samanlainen kuin parametrina annettu <CODE>kopio</CODE>.
     * @param kopio Vektori, jonka kaltainen uuden vektorin halutaan olevan.
     */
    public Vektori( Vektori kopio ) {
        
        this( kopio.komponenttiX, kopio.komponenttiY );
        
    }
    
    /**
     * Asettaa vektorin x- ja y-komponentit.
     * @param komponenttiX Vektorin uusi x-komponentti.
     * @param komponenttiY Vektorin uusi y-komponentti.
     */
    public void aseta( double komponenttiX, double komponenttiY ) {
        
        this.komponenttiX = komponenttiX;
        this.komponenttiY = komponenttiY;
        
    }
    
    /**
     * Muuttaa vektorin x- ja y-komponentteja.
     * @param muutosX Vektorin muutos x-komponentissa.
     * @param muutosY Vektorin muutos y-komponentissa.
     */
    public void muuta( double muutosX, double muutosY ) {
        
        this.komponenttiX += muutosX;
        this.komponenttiY += muutosY;
        
    }
    
    /**
     * Skaalaa vektoria, annetulla skalaarilla.
     * @param skalaari Luku, jolla vektorin komponentit skaalataan.
     */
    public void skaalaa( double skalaari ) {
        
        this.komponenttiX *= skalaari;
        this.komponenttiY *= skalaari;
        
    }
    
    /**
     * Lis‰‰ vektoriin parametrina annetun vektorin, jos se ei ole <CODE>null</CODE>.
     * @param lisattava Vektori, joka lis‰t‰‰n.
     */
    public void lisaa( Vektori lisattava ) {
        
        if ( lisattava != null ) {
            
            this.komponenttiX += lisattava.komponenttiX;
            this.komponenttiY += lisattava.komponenttiY;
            
        }
        
    }
    
    /**
     * V‰hent‰‰ vektorista parametrina annetun vektorin, jos se ei ole <CODE>null</CODE>.
     * @param vahennettava Vektori, joka v‰hennet‰‰n.
     */
    public void vahenna( Vektori vahennettava ) {
        
        if ( vahennettava != null ) {
            
            this.komponenttiX -= vahennettava.komponenttiX;
            this.komponenttiY -= vahennettava.komponenttiY;
            
        }
        
    }
    
    /**
     * Kertoo vektorin pituuden.
     * @return Vektorin pituus.
     */
    public double kerroPituus() {
        
        double luku = this.komponenttiX * this.komponenttiX + this.komponenttiY * this.komponenttiY;
        
        if ( luku < 0 )
            luku = 1;
        
        return Math.sqrt( luku );
        
    }
    
    /**
     * Kertoo, onko vektori nollavektori.
     * @return Totuusarvo, joka kertoo, onko vektori nollavektori.
     */
    public boolean onNollavektori() {
        
        return this.komponenttiX == 0 && this.komponenttiY == 0;
        
    }
        
    /**
     * Kertoo vektorin x-komponentin.
     * @return Vektorin x-komponentti.
     */
    public double kerroX() {
        
        return this.komponenttiX;
        
    }
    
    /**
     * Kertoo vektorin y-komponentin.
     * @return Vektorin y-komponentti.
     */
    public double kerroY() {
        
        return this.komponenttiY;
        
    }
    
    /**
     * Kertoo vektorin ja positiivisen x-akselin v‰lisen kulman v‰lill‰ 0 - 2*PI
     * kulman kasvavaan suuntaan.
     * @return Vektorin ja positiivisen x-akselin v‰linen kulma v‰lill‰ 0-2*PI.
     */
    public double kerroKulma() {

        return Kulma.kerroKulmaKateeteista( this.komponenttiX, this.komponenttiY );
        
    }

    /**
     * Kertoo vektorin suuntaisen yksikkovektorin.
     * @return Vektorin suuntainen yksikkovektori.
     */
    public Vektori kerroYksikkovektori() {
        
        Vektori yksikkovektori = new Vektori( this.komponenttiX, this.komponenttiY );
        yksikkovektori.skaalaa( 1 / this.kerroPituus() );
        return yksikkovektori;
        
    }
    
    /**
     * Kertoo vektorin vektoriprojektion annetussa kannassa.
     * @param kanta Vektori, joka toimii projektion kantana.
     * @return Vektorin vektoriprojektio annetussa kannassa.
     */
    public Vektori kerroVektoriprojektio( Vektori kanta ) {
        
        double pistetulo = Vektori.kerroPistetulo( kanta, kanta );
        
        if ( pistetulo == 0 )
            return new Vektori( 0, 0 );
        
        Vektori projektio = new Vektori( kanta.komponenttiX, kanta.komponenttiY );
        projektio.skaalaa( Vektori.kerroPistetulo( this, kanta ) / pistetulo );
        return projektio;
        
    }
    
}
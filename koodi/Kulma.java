
/**
 * 
 * Luokka <CODE>Kulma</CODE> kuvaa tason kulmaa radiaaneina. Sen arvo on
 * aina perusv�lill� 0 - 2*PI.
 *
 * @author Jaakko Luttinen
 *
 */
public class Kulma {
    
    /**
     * Kulman arvo radiaaneina.
     */
    private double radiaani;

    /**
     * Korjaa annetun kulman arvon v�lille 0-2*PI.
     * @param kulma Korjattava kulman arvo.
     * @return Korjattu kulman arvo.
     */
    public static double korjaaKulma( double kulma ) {
        
        kulma = kulma % ( 2 * Math.PI );
        
        if ( kulma < 0 )
            kulma += 2 * Math.PI;
        
        return kulma;
        
    }
    
    /**
     * Kertoo kulman, joka annetuilla kateeteilla muodostuu x-akselin v�lille. Huomioitavaa
     * on, ett� palautusarvo ei ole puhtaasti tangentti kateeteista, vaan kulma selvitet��n
     * koko perusv�lill�. Metodi osaa my�s tutkia tangentille muuten vaikeat arvot. Jos molemmat
     * kateetit ovat nolla, palautetaan kulman arvo nolla.
     * @param kateettiX X-suuntainen kateetti.
     * @param kateettiY Y-suuntainen kateetti.
     * @return Kateettien muodostama kulma.
     */
    public static double kerroKulmaKateeteista( double kateettiX, double kateettiY ) {
        
        double radiaani = 0.0;
        
        if ( kateettiX == 0 ) {
            
            if ( kateettiY > 0 )
                radiaani = Math.PI / 2.0;
            else if ( kateettiY < 0 )
                radiaani = 3.0 * Math.PI / 2.0;
            else
                return 0;//throw new Exception( "Kulmaa ei voida m��ritt�� nollakateeteista." );
            
        }
        else if ( kateettiY == 0 ) {
            
            if ( kateettiX > 0)
                radiaani = 0.0;
            else
                radiaani = Math.PI;
            
        }
        else {
            
            double tan = kateettiY / kateettiX;
            radiaani = Math.atan( tan );
            
            //Palautetaan v�lille 0-PI.
            if ( radiaani < 0 )
                radiaani += Math.PI;
            
            if ( kateettiY < 0 )
                radiaani = ( radiaani + Math.PI ) % ( 2 * Math.PI );
                        
        }
       
        return radiaani;
        
    }
    
    /**
     * Palauttaa kahden annetun kulman summan perusv�lill�.
     * @param radiaani1 Summan toinen osapuoli.
     * @param radiaani2 Summan toinen osapuoli.
     * @return Kulmien summa perusv�lill�.
     */
    public static double summa( double radiaani1, double radiaani2 ) {
        
        return Kulma.korjaaKulma( radiaani1 + radiaani2 );
        
    }
    
    /**
     * Luo uuden kulman, jonka suuruus on annettu kulma perusv�lille korjattuna.
     * @param radiaani Kulman suuruus.
     */
    public Kulma( double radiaani ) {
        
        this.aseta( radiaani );
        
    }

    /**
     * Kertoo kulman suuruuden radiaaneina perusv�lill�.
     * @return Kulman suuruus radiaaneina perusv�lill�.
     */
    public double kerroKulma() {
        
        return this.radiaani;
        
    }

    /**
     * Asettaa kulman suuruuden. Korjaa kulman perusv�lille.
     * @param radiaani Kulman suuruus radiaaneina.
     */
    public void aseta( double radiaani ) {
        
        this.radiaani = radiaani;
        this.radiaani = Kulma.korjaaKulma( this.radiaani );
        
    }
    
    /**
     * Muuttaa kulmaa. Korjaa kulman perusv�lille.
     * @param muutos Kulman muutos.
     */
    public void muuta( double muutos ) {
        
        this.radiaani += muutos;
        this.radiaani = Kulma.korjaaKulma( this.radiaani );
        
    }
    
    /**
     * Kertoo suhteellisen kulman absoluuttisen suuruuden radiaaneina perusv�lill�.
     * @param muutos Suhteellinen kulma.
     * @return Suhteelisen kulman absoluuttinen suuruus radiaaneina perusv�lill�.
     */
    public double kerroSuhteellinenKulma( double muutos ) {
        
        double radiaani = this.radiaani;
        radiaani += muutos;
        return Kulma.korjaaKulma( radiaani );
        
    }
    
    /**
     * Kertoo kulman kosinin.
     * @return Kulman kosini.
     */
    public double kosini() {
        
        return Math.cos( this.radiaani );
        
    }
    
    /**
     * Kertoo kulman sinin.
     * @return Kulman sini.
     */
    public double sini() {
        
        return Math.sin( this.radiaani );
        
    }
    
}
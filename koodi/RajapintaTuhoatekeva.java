
/**
 * 
 * Rajapinta <CODE>RajapintaTuhoatekeva</CODE> m‰‰ritt‰‰ tuhoa tekev‰n asian rajapinnan.
 * T‰ll‰ tuhoa tekev‰ll‰ asialla t‰ytyy olla jokin tuhovoima sek‰ tuhon l‰hde. L‰hteen
 * pit‰‰ olla jokin avaruuden kappale. Esimerkiksi ammuksen tuhonl‰hde voisi olla ammuksen
 * ampunut alus.
 * 
 * @author Jaakko Luttinen
 *
 */
public interface RajapintaTuhoatekeva {

    /**
     * Kertoo tuhovoiman juuri t‰ll‰ hetkell‰.
     * @return Tuhovoima juuri t‰ll‰ hetkell‰.
     */
    public abstract double kerroTuhovoima();
    
    /**
     * Kertoo tuhon l‰hteen, esim. ammuksen ampuneen aluksen.
     * @return Tuhoa tekev‰n asian l‰hteen‰ toiminut kappale.
     */
    public abstract Kappale kerroTuhonLahde();
    
}
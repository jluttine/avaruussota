
import java.io.*;

/**
 * 
 * Luokka <CODE>TiedostonlukijaPelikentta</CODE> lukee pelikentt�tiedostoja.
 * Tiedosto t�ytyy olla oikeassa muodossa tai lukeminen keskeytyy virheeseen.
 * Tiedosto alkaa otsikko-osalla, jossa kerrotaan pelikent�n mitat: leveys ja
 * korkeus. Sit� seuraa erotinrivi. T�m�n j�lkeen on jokaisen maa-alueen tiedot
 * erotettuna toisistaan erotinrivill�. Viimeisen maa-alueen j�lkeen on my�s
 * erotinrivi. Tyhji� rivej� saa olla v�liss�, mutta muuten yksikin ylim��r�inen
 * merkki voi aiheuttaa sen, ettei tiedoston perusteella pystyt� luomaan
 * pelikentt��. Kun tiedosto on luettu k�ytt�j�n tulee sulkea lukija!
 * 
 * <p>Yhden maa-alueen tiedot annetaan seuraavassa muodossa: 1.polttopisteen
 * x-koordinaatti, 1.polttopisteen y-koordinaatti, polttopistevektorin
 * x-komponentti, polttopistevektorin y-komponentti, ellipsin paksuus.
 * Kaikki tiedoston luvut ovat kokonaislukuja - leveys, korkeus ja paksuus
 * t�ytyy olla positiivisia. Huomioitavaa on my�s, ett� itse avaruus
 * saattaa asettaa omia rajoituksiaan, esim. avaruuden mitat, kappaleen
 * maksimikoko. Niit� t�m� luokka ei ota mitenk��n huomioon, koska luokka
 * ei aseta maa-alueita mihink��n avaruuteen.
 * 
 * <p>Esimerkkitiedosto (kaksi maa-aluetta):<CODE>
 * <p>1000
 * <p>1500
 * <p>---
 * <p>400
 * <p>600
 * <p>200
 * <p>300
 * <p>20
 * <p>---
 * <p>0
 * <p>0
 * <p>-300
 * <p>0
 * <p>30
 * <p>---</CODE>
 * 
 * Luokan kehittelyss� on hieman hy�dynnetty kurssin harjoitusteht�v�n 10.2. esimerkkiratkaisua.
 * 
 * @author Jaakko Luttinen
 *
 */
public class TiedostonlukijaPelikentta {

    /**
     * Tiedostomuodon k�ytt�m� erotinrivi osioiden erottamiseksi.
     */
    private static final String EROTINRIVI = "---";
    
    /**
     * Tiedoston puskuroitu syotevirta.
     */
    private BufferedReader syotevirta;
    
    /**
     * Pelikent�n tiedosto.
     */
    private String tiedosto;
    
    /**
     * Tiedostosta luettu pelikent�n leveys.
     */
    private int leveys;
    
    /**
     * Tiedostosta luettu pelikent�n korkeus.
     */
    private int korkeus;

    /**
     * Viimeksi luetun maa-alueen x-koordinaatti.
     */
    private double sijaintiX;
    
    /**
     * Viimeksi luetun maa-alueen y-koordinaatti.
     */
    private double sijaintiY;
    
    /**
     * Viimeksi luettu maa-alue.
     */
    private KappaleMaaAlue maaAlue;
    
    /**
     * Luo uuden tiedostonlukijan ja lukee tiedoston otsikkotiedot.
     * @param tiedosto Pelikentt�tiedostopolku.
     * @throws IOException Poikkeus kertoo, ett� tiedostonlukemisessa tapahtui virhe.
     * @throws Exception Poikkeus kertoo, ett� tiedoston otsikkotiedoissa on muotovirhe.
     */
    public TiedostonlukijaPelikentta( String tiedosto ) throws IOException, Exception {
        
        this.tiedosto = tiedosto;
        this.aloitaAlusta();
        
    }
    
    /**
     * Aloittaa tiedoston lukemisen alusta ja lukee otsikkotiedot.
     * @throws IOException Poikkeus kertoo, ett� tiedostonlukemisessa tapahtui virhe.
     * @throws Exception Poikkeus kertoo, ett� tiedoston otsikkotiedoissa on muotovirhe.
     */
    public void aloitaAlusta() throws IOException, Exception {
        
    	this.syotevirta = new BufferedReader( new FileReader( this.tiedosto ) );
    	this.lueOtsikkotiedot();
        
    }
    
    /**
     * Lukee tiedoston seuraavan ei tyhj�n rivin. Metodi on tehty kurssin harjoituksen
     * 10.2. esimerkkiratkaisun inspiroimana.
     * @return Seuraava ei-tyhj� rivi tai null, jos tiedosto on luettu loppuun.
     * @throws IOException Poikkeus kertoo, ett� tiedoston lukemisessa tapahtui virhe.
     */
    private String lueSeuraavaEiTyhjaRivi() throws IOException {

        String rivi;

        do {
    	
    	    rivi = this.syotevirta.readLine();
    	
    	} while( rivi != null && rivi.trim().equals( "" ) );
       
    	return rivi;
    	
    }

    /**
     * Lukee tiedostosta seuraavan ei-tyhj�n rivin ja muuttaa sen kokonaisluvuksi.
     * @return Luettu rivi kokonaislukuna.
     * @throws NumberFormatException Poikkeus kertoo, ettei rivi� saatu kokonaislukumuotoon.
     * @throws Exception Poikkeus kertoo, ett� tiedosto loppui kesken.
     */
    private int lueSeuraavaKokonaisluku() throws NumberFormatException, Exception {
        
        String luku = this.lueSeuraavaEiTyhjaRivi();
        
        if ( luku == null )
            throw new Exception( "Tiedosto loppui kesken." );
        
        return Integer.parseInt( luku );
        
    }

    /**
     * Lukee tiedoston otsikkotiedot (leveys ja korkeus).
     * @throws IOException Poikkeus kertoo, ett� tiedoston lukemisessa tapahtui virhe.
     * @throws Exception Poikkeus kertoo, ett� tiedoston muoto on virheellinen.
     */
    private void lueOtsikkotiedot() throws IOException, Exception {
        
        try {
            
            this.leveys = this.lueSeuraavaKokonaisluku();
            this.korkeus = this.lueSeuraavaKokonaisluku();
            
        }
        catch ( NumberFormatException virhe ) {
            
            throw new Exception( "Pelikent�n mitat t�ytyy olla kokonaislukuja." );
            
        }
        catch ( Exception virhe ) {
            
            throw new Exception( virhe.getMessage() + " Otsikkotiedot (leveys ja korkeus) puuttuvat." );
            
        }
        
        if ( this.leveys <= 0 || this.korkeus <= 0 )
            throw new Exception( "Pelikent�n mittojen t�ytyy olla positiivisia." );
            
        //Otsikkotietoja t�ytyy seurata erotinrivi.
        if ( !this.lueSeuraavaEiTyhjaRivi().equals( TiedostonlukijaPelikentta.EROTINRIVI ) )
            throw new Exception( "Otsikkotietoja t�ytyy seurata \"" + TiedostonlukijaPelikentta.EROTINRIVI +
                    			 "\" -erotinrivi." );
        
    }
    
    /**
     * Kertoo pelikent�n leveyden.
     * @return Pelikent�n leveys.
     */
    public int kerroLeveys() {
        
        return this.leveys;
        
    }
    
    /**
     * Kertoo pelikent�n korkeuden.
     * @return Pelikent�n korkeus.
     */
    public int kerroKorkeus() {
        
        return this.korkeus;
        
    }
    
    /**
     * Kertoo viimeksi luetun maa-alueen x-koordinaatin.
     * @return Viimeksi luetun maa-alueen x-koordinaatti.
     */
    public double kerroMaaAlueenSijaintiX() {
        
        return this.sijaintiX;
        
    }
    
    /**
     * Kertoo viimeksi luetun maa-alueen y-koordinaatin.
     * @return Viimeksi luetun maa-alueen y-koordinaatti.
     */
    public double kerroMaaAlueenSijaintiY() {
        
        return this.sijaintiY;
        
    }
    
    /**
     * Kertoo viimeksi luetun maa-alueen.
     * @return Viimeksi luettu maa-alue.
     */
    public KappaleMaaAlue kerroMaaAlue() {
        
        return this.maaAlue;
        
    }
    
    /**
     * Lukee seuraavan maa-alueen.
     * @return Totuusarvo kertoo, oliko tiedostossa viel� maa-alueen tietoja. 
     * @throws IOException Poikkeus kertoo, ett� tiedoston lukemisessa tapahtui virhe.
     * @throws Exception Poikkeus kertoo, ett� tiedoston muoto on virheellinen.
     */
    public boolean lueSeuraavaMaaAlue() throws IOException, Exception {
        
        String sijaintiX = this.lueSeuraavaEiTyhjaRivi();
        
        //Tiedosto loppunut.
        if ( sijaintiX == null )
            return false;
        
        try {
            
            //Yritet��n lukea maa-alueen tiedot.
            this.sijaintiX = Integer.parseInt( sijaintiX );
            this.sijaintiY = this.lueSeuraavaKokonaisluku();
            int polttopistevektorinX = this.lueSeuraavaKokonaisluku();
            int polttopistevektorinY = this.lueSeuraavaKokonaisluku();
            int paksuus = this.lueSeuraavaKokonaisluku();
            
            if ( paksuus <= 0 )
                throw new Exception( "Maa-alueen paksuuden (" + paksuus + ") t�ytyy olla positiivinen." );
            
            Vektori polttopistevektori = new Vektori( polttopistevektorinX, polttopistevektorinY );
            this.maaAlue = new KappaleMaaAlue( null, polttopistevektori, polttopistevektori.kerroPituus() + paksuus );
            
        }
        catch ( NumberFormatException virhe ) {
            
            throw new Exception( "Maa-alueen tietojen t�ytyy olla kokonaislukuja." );
            
        }

        //Maa-aluetta t�ytyy aina seurata erotinrivi.
        String erotinrivi = this.lueSeuraavaEiTyhjaRivi();
        
        if ( erotinrivi == null )
            throw new Exception( "Tiedosto loppui kesken. Ei erotinrivi�." );
        
        if ( !erotinrivi.equals( TiedostonlukijaPelikentta.EROTINRIVI ) )
            throw new Exception( "Maa-alueen tietoja t�ytyy seurata \"" + TiedostonlukijaPelikentta.EROTINRIVI +
            					 "\" -erotinrivi." );
        
        //Saatiin luettua maa-alueen tiedot.
        return true;
        
    }
    
    /**
     * Sulkee syotevirran.
     * @throws IOException Poikkeus kertoo, ett� tiedoston k�sittelyss� tapahtui virhe.
     */
    public void sulje() throws IOException {
        
        this.syotevirta.close();
        
    }
        
}

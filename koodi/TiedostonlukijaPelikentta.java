
import java.io.*;

/**
 * 
 * Luokka <CODE>TiedostonlukijaPelikentta</CODE> lukee pelikenttätiedostoja.
 * Tiedosto täytyy olla oikeassa muodossa tai lukeminen keskeytyy virheeseen.
 * Tiedosto alkaa otsikko-osalla, jossa kerrotaan pelikentän mitat: leveys ja
 * korkeus. Sitä seuraa erotinrivi. Tämän jälkeen on jokaisen maa-alueen tiedot
 * erotettuna toisistaan erotinrivillä. Viimeisen maa-alueen jälkeen on myös
 * erotinrivi. Tyhjiä rivejä saa olla välissä, mutta muuten yksikin ylimääräinen
 * merkki voi aiheuttaa sen, ettei tiedoston perusteella pystytä luomaan
 * pelikenttää. Kun tiedosto on luettu käyttäjän tulee sulkea lukija!
 * 
 * <p>Yhden maa-alueen tiedot annetaan seuraavassa muodossa: 1.polttopisteen
 * x-koordinaatti, 1.polttopisteen y-koordinaatti, polttopistevektorin
 * x-komponentti, polttopistevektorin y-komponentti, ellipsin paksuus.
 * Kaikki tiedoston luvut ovat kokonaislukuja - leveys, korkeus ja paksuus
 * täytyy olla positiivisia. Huomioitavaa on myös, että itse avaruus
 * saattaa asettaa omia rajoituksiaan, esim. avaruuden mitat, kappaleen
 * maksimikoko. Niitä tämä luokka ei ota mitenkään huomioon, koska luokka
 * ei aseta maa-alueita mihinkään avaruuteen.
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
 * Luokan kehittelyssä on hieman hyödynnetty kurssin harjoitustehtävän 10.2. esimerkkiratkaisua.
 * 
 * @author Jaakko Luttinen
 *
 */
public class TiedostonlukijaPelikentta {

    /**
     * Tiedostomuodon käyttämä erotinrivi osioiden erottamiseksi.
     */
    private static final String EROTINRIVI = "---";
    
    /**
     * Tiedoston puskuroitu syotevirta.
     */
    private BufferedReader syotevirta;
    
    /**
     * Pelikentän tiedosto.
     */
    private String tiedosto;
    
    /**
     * Tiedostosta luettu pelikentän leveys.
     */
    private int leveys;
    
    /**
     * Tiedostosta luettu pelikentän korkeus.
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
     * @param tiedosto Pelikenttätiedostopolku.
     * @throws IOException Poikkeus kertoo, että tiedostonlukemisessa tapahtui virhe.
     * @throws Exception Poikkeus kertoo, että tiedoston otsikkotiedoissa on muotovirhe.
     */
    public TiedostonlukijaPelikentta( String tiedosto ) throws IOException, Exception {
        
        this.tiedosto = tiedosto;
        this.aloitaAlusta();
        
    }
    
    /**
     * Aloittaa tiedoston lukemisen alusta ja lukee otsikkotiedot.
     * @throws IOException Poikkeus kertoo, että tiedostonlukemisessa tapahtui virhe.
     * @throws Exception Poikkeus kertoo, että tiedoston otsikkotiedoissa on muotovirhe.
     */
    public void aloitaAlusta() throws IOException, Exception {
        
    	this.syotevirta = new BufferedReader( new FileReader( this.tiedosto ) );
    	this.lueOtsikkotiedot();
        
    }
    
    /**
     * Lukee tiedoston seuraavan ei tyhjän rivin. Metodi on tehty kurssin harjoituksen
     * 10.2. esimerkkiratkaisun inspiroimana.
     * @return Seuraava ei-tyhjä rivi tai null, jos tiedosto on luettu loppuun.
     * @throws IOException Poikkeus kertoo, että tiedoston lukemisessa tapahtui virhe.
     */
    private String lueSeuraavaEiTyhjaRivi() throws IOException {

        String rivi;

        do {
    	
    	    rivi = this.syotevirta.readLine();
    	
    	} while( rivi != null && rivi.trim().equals( "" ) );
       
    	return rivi;
    	
    }

    /**
     * Lukee tiedostosta seuraavan ei-tyhjän rivin ja muuttaa sen kokonaisluvuksi.
     * @return Luettu rivi kokonaislukuna.
     * @throws NumberFormatException Poikkeus kertoo, ettei riviä saatu kokonaislukumuotoon.
     * @throws Exception Poikkeus kertoo, että tiedosto loppui kesken.
     */
    private int lueSeuraavaKokonaisluku() throws NumberFormatException, Exception {
        
        String luku = this.lueSeuraavaEiTyhjaRivi();
        
        if ( luku == null )
            throw new Exception( "Tiedosto loppui kesken." );
        
        return Integer.parseInt( luku );
        
    }

    /**
     * Lukee tiedoston otsikkotiedot (leveys ja korkeus).
     * @throws IOException Poikkeus kertoo, että tiedoston lukemisessa tapahtui virhe.
     * @throws Exception Poikkeus kertoo, että tiedoston muoto on virheellinen.
     */
    private void lueOtsikkotiedot() throws IOException, Exception {
        
        try {
            
            this.leveys = this.lueSeuraavaKokonaisluku();
            this.korkeus = this.lueSeuraavaKokonaisluku();
            
        }
        catch ( NumberFormatException virhe ) {
            
            throw new Exception( "Pelikentän mitat täytyy olla kokonaislukuja." );
            
        }
        catch ( Exception virhe ) {
            
            throw new Exception( virhe.getMessage() + " Otsikkotiedot (leveys ja korkeus) puuttuvat." );
            
        }
        
        if ( this.leveys <= 0 || this.korkeus <= 0 )
            throw new Exception( "Pelikentän mittojen täytyy olla positiivisia." );
            
        //Otsikkotietoja täytyy seurata erotinrivi.
        if ( !this.lueSeuraavaEiTyhjaRivi().equals( TiedostonlukijaPelikentta.EROTINRIVI ) )
            throw new Exception( "Otsikkotietoja täytyy seurata \"" + TiedostonlukijaPelikentta.EROTINRIVI +
                    			 "\" -erotinrivi." );
        
    }
    
    /**
     * Kertoo pelikentän leveyden.
     * @return Pelikentän leveys.
     */
    public int kerroLeveys() {
        
        return this.leveys;
        
    }
    
    /**
     * Kertoo pelikentän korkeuden.
     * @return Pelikentän korkeus.
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
     * @return Totuusarvo kertoo, oliko tiedostossa vielä maa-alueen tietoja. 
     * @throws IOException Poikkeus kertoo, että tiedoston lukemisessa tapahtui virhe.
     * @throws Exception Poikkeus kertoo, että tiedoston muoto on virheellinen.
     */
    public boolean lueSeuraavaMaaAlue() throws IOException, Exception {
        
        String sijaintiX = this.lueSeuraavaEiTyhjaRivi();
        
        //Tiedosto loppunut.
        if ( sijaintiX == null )
            return false;
        
        try {
            
            //Yritetään lukea maa-alueen tiedot.
            this.sijaintiX = Integer.parseInt( sijaintiX );
            this.sijaintiY = this.lueSeuraavaKokonaisluku();
            int polttopistevektorinX = this.lueSeuraavaKokonaisluku();
            int polttopistevektorinY = this.lueSeuraavaKokonaisluku();
            int paksuus = this.lueSeuraavaKokonaisluku();
            
            if ( paksuus <= 0 )
                throw new Exception( "Maa-alueen paksuuden (" + paksuus + ") täytyy olla positiivinen." );
            
            Vektori polttopistevektori = new Vektori( polttopistevektorinX, polttopistevektorinY );
            this.maaAlue = new KappaleMaaAlue( null, polttopistevektori, polttopistevektori.kerroPituus() + paksuus );
            
        }
        catch ( NumberFormatException virhe ) {
            
            throw new Exception( "Maa-alueen tietojen täytyy olla kokonaislukuja." );
            
        }

        //Maa-aluetta täytyy aina seurata erotinrivi.
        String erotinrivi = this.lueSeuraavaEiTyhjaRivi();
        
        if ( erotinrivi == null )
            throw new Exception( "Tiedosto loppui kesken. Ei erotinriviä." );
        
        if ( !erotinrivi.equals( TiedostonlukijaPelikentta.EROTINRIVI ) )
            throw new Exception( "Maa-alueen tietoja täytyy seurata \"" + TiedostonlukijaPelikentta.EROTINRIVI +
            					 "\" -erotinrivi." );
        
        //Saatiin luettua maa-alueen tiedot.
        return true;
        
    }
    
    /**
     * Sulkee syotevirran.
     * @throws IOException Poikkeus kertoo, että tiedoston käsittelyssä tapahtui virhe.
     */
    public void sulje() throws IOException {
        
        this.syotevirta.close();
        
    }
        
}

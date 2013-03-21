
import java.awt.*;
import java.io.File;

/**
 * 
 * Luokka <CODE>Tulkki</CODE> kuvaa pelin ja k‰yttˆliittym‰n v‰list‰ liit‰nt‰‰. Luokka
 * on kooltaan aika iso, sill‰ se yritt‰‰ tarjota kaikki ne palvelut, joita k‰yttˆliittym‰
 * pelist‰ tarvitsee, siten, ettei k‰yttˆliittym‰n tarvitsisi tiet‰‰ mit‰‰n pelin sis‰ll‰
 * olevista luokista. Tulkki ei anna tehd‰ pelin ollessa k‰ynniss‰ mit‰‰n vaikuttavia
 * muutoksia peliin (esim. pelaajia ei voi muuttaa).
 * 
 * @author Jaakko Luttinen
 *
 */
public class Tulkki {
    
    /**
     * Kappaleiden k‰ytt‰m‰t spritet.
     */
    private Kuvasarja[] malliKuva;
    
    /**
     * Pelin tarjoamien ammuksien mallit.
     */
    private KappaleAmmus[] malliAmmus;
    
    /**
     * Pelin tarjoamien aseiden mallit.
     */
    private Ase[] malliAse;
    
    /**
     * Pelin tarjoamien r‰j‰hdysten mallit.
     */
    private KappaleRajahdys[] malliRajahdys;
    
    /**
     * Pelin tarjoamien alusten mallit.
     */
    private KappaleAlus[] malliAlus;
    
    /**
     * Pelin tarjoamien teko‰lyjen mallit.
     */
    private Tekoaly[] malliTekoaly;
    
    /**
     * Pelin tarjoamien pelikenttien nimet ja tiedostopolut.
     */
    private String[][] malliPelikentta;
    
    /**
     * Avaruussotapeli, jota k‰sitell‰‰n.
     */
    private AvaruussotaPeli peli;
    
    /**
     * Lataa ja luo pelin tarjoamat kappaleet, teko‰lyt ja pelikent‰t.
     * @throws Exception Poikkeus kertoo, ett‰ mallien lataaminen ep‰onnistui.
     */
    private void lataaMallit() throws Exception {
        
        String resurssipolku = "resurssit" + File.separator;
        
        //Asetetaan maa-alueiden tekstuuri.
        Kuvasarja tekstuuriMaaAlue = new Kuvasarja( resurssipolku + "maaalue_01.gif", 1, 1);
        KappaleMaaAlue.asetaTekstuuri( tekstuuriMaaAlue );

        int ind = 0;
        this.malliKuva = new Kuvasarja[7];
        this.malliKuva[ind++] = new Kuvasarja( resurssipolku + "alus_01.gif", 5, 4 );
        this.malliKuva[ind++] = new Kuvasarja( resurssipolku + "alus_02.gif", 10, 4 );
        this.malliKuva[ind++] = new Kuvasarja( resurssipolku + "alus_03.gif", 10, 4 );
        this.malliKuva[ind++] = new Kuvasarja( resurssipolku + "ammus_01.gif", 1, 1 );
        this.malliKuva[ind++] = new Kuvasarja( resurssipolku + "ammus_02.gif", 5, 2 );
        this.malliKuva[ind++] = new Kuvasarja( resurssipolku + "ammus_03.gif", 1, 1 );
        this.malliKuva[ind++] = new Kuvasarja( resurssipolku + "rajahdys_01.gif", 5, 3 );
        
        ind = 0;
        this.malliAmmus = new KappaleAmmus[3];
        this.malliAmmus[ind++] = new KappaleAmmus( "Luoti", malliKuva[3], 0.3, 500, 5 );
        this.malliAmmus[ind++] = new KappaleAmmusLiekki( "Liekki", malliKuva[4], 0.2, 500, 0.08 );
        this.malliAmmus[ind++] = new KappaleAmmus( "Iso-luoti", malliKuva[5], 0.3, 600, 100 );
        
        ind = 0;
        this.malliAse = new Ase[3];
        this.malliAse[ind++] = new Ase( malliAmmus[0], 45 );
        this.malliAse[ind++] = new Ase( malliAmmus[1], 60 );
        this.malliAse[ind++] = new Ase( malliAmmus[2], 720 );
        
        ind = 0;
        this.malliRajahdys = new KappaleRajahdys[1];
        this.malliRajahdys[ind++] = new KappaleRajahdys( null, malliKuva[6], 1000 );
        
        ind = 0;
        this.malliAlus = new KappaleAlus[3];
        this.malliAlus[ind++] = new KappaleAlus( "H‰vitt‰j‰", malliKuva[0], 0.0003, 0.2, 0.006, malliAse[0], malliRajahdys[0], 160 );
        this.malliAlus[ind++] = new KappaleAlus( "Liekitt‰j‰", malliKuva[1], 0.0002, 0.15, 0.004, malliAse[1], malliRajahdys[0], 200 );
        this.malliAlus[ind++] = new KappaleAlus( "Tykitt‰j‰", malliKuva[2], 0.00015, 0.1, 0.003, malliAse[2], malliRajahdys[0], 210 );
        
        ind = 0;
        this.malliTekoaly = new Tekoaly[4];
        this.malliTekoaly[ind++] = new TekoalySatunnainen( "Idiootti" );
        this.malliTekoaly[ind++] = new TekoalyKohde( "Kamikatse" );
        this.malliTekoaly[ind++] = new TekoalyNormaali( "Normaali" );
        this.malliTekoaly[ind++] = new TekoalyTangentti( "ƒss‰" );
        
        //Kenttien nimet ja tiedostot.
        ind = 0;
        this.malliPelikentta = new String[3][2];
        this.malliPelikentta[ind][0] = "Tyhjiˆ";
        this.malliPelikentta[ind++][1] = resurssipolku + "pelikentta01.txt";
        this.malliPelikentta[ind][0] = "Areena";
        this.malliPelikentta[ind++][1] = resurssipolku + "pelikentta02.txt";
        this.malliPelikentta[ind][0] = "Labyrintti";
        this.malliPelikentta[ind++][1] = resurssipolku + "pelikentta03.txt";
        
    }
    
    /**
     * Luo uuden tulkin. Lataa mallit.
     * @throws Exception Poikkeus kertoo, ett‰ mallien lataaminen ep‰onnistui.
     */
    public Tulkki() throws Exception {
        
        this.lataaMallit();
        this.peli = new AvaruussotaPeli();
        
    }
    
    /**
     * Heitt‰‰ poikkeuksen, jos pelin asetuksissa on jokin pelin k‰ynnist‰misen
     * est‰v‰ virhe.
     * @throws Exception Poikkeus kertoo, ett‰ pelin asetuksissa on jokin pelin
     * k‰ynnist‰misen est‰v‰ virhe.
     */
    public void tutkiPelinKaynnistettavyys() throws Exception {
        
        this.peli.tutkiKaynnistettavyys();
        
    }

    /**
     * K‰ynnist‰‰ pelin. Mahdollinen aiempi peli loppuu samalla.
     * @throws Exception Poikkeus kertoo, ett‰ pelin k‰ynnist‰minen ep‰onnistui.
     */
    public void kaynnistaPeli() throws Exception {

        this.peli.kaynnista();
        
    }

    /**
     * Lopettaa pelin. T‰m‰n j‰lkeen on viel‰ mahdollista tutkia pelaajien pisteit‰ ym.
     */
    public void lopetaPeli() {

        this.peli.lopeta();        
        
    }

    /**
     * Tyhjent‰‰ pelin. Poistaa pelaajat.
     */
    public void tyhjennaPeli() {
        
        //Poistetaan pelaajien teko‰lyt. Ei ole ihan tarpeellinen, mutta varmuuden vuoksi
        //jottei j‰‰ mit‰‰n viittaussotkuja.
        for ( int ind = 0; ind < this.peli.kerroPelaajienMaara(); ind++ )
            this.peli.kerroPelaaja( ind ).poistaTekoaly();
        
        this.peli.tyhjenna();
        
    }
    
    /**
     * Kertoo pelin k‰yntitilan. Huomaa, ett‰ k‰yntitila on eri asia kuin se,
     * onko peli k‰ynnistetty. K‰yntitila ei riipu siit‰, onko peli k‰ynnistetty.
     * Kun peli on k‰ynnistetty, peli etenee, jos k‰yntitila on <CODE>true</CODE>.
     * @return Pelin k‰yntitila.
     */
    public boolean kerroPelinKaynti() {
        
        return this.peli.kerroPelimaailmaSaie().kerroKaynti();
        
    }
    
    /**
     * Asettaa pelin k‰yntitilan.
     * @param kay Pelin k‰yntitila.
     */
    public void asetaPelinKaynti( boolean kay ) {
        
        this.peli.kerroPelimaailmaSaie().asetaKaynti( kay );
        
    }
    
    /**
     * Kertoo, onko peli k‰ynnistetty.
     * @return Totuusarvo kertoo, onko peli k‰ynnistetty.
     */
    public boolean onKaynnissa() {
        
        return this.peli.kerroPelimaailmaSaie().onKaynnissa();
        
    }
    
    /**
     * Asettaa pelikent‰ksi annettujen mittojen mukaisen satunnaisesti generoitavan
     * maailman.
     * @param leveys Pelikent‰n leveys.
     * @param korkeus Pelikent‰n korkeus.
     * @throws Exception Poikkeus kertoo, ett‰ mitat olivat virheelliset tai peli on
     * k‰ynniss‰.
     */
    public void asetaPelimaailma( int leveys, int korkeus ) throws Exception {
        
        if ( this.onKaynnissa() )
            throw new Exception( "Pelin ollessa k‰ynniss‰ ei voi luoda uutta pelimaailmaa." );
        
        this.peli.asetaPelimaailma( leveys, korkeus );
        
    }

    /**
     * Asettaa pelikent‰ksi indeksi‰ vastaavan mallipelikent‰n.
     * @param indeksi Mallipelikent‰n indeksi.
     * @throws Exception Poikkeus kertoo, ett‰ peli on k‰ynniss‰ tai indeksi on
     * virheellinen.
     */
    public void asetaPelimaailma( int indeksi ) throws Exception {
        
        if ( this.onKaynnissa() )
            throw new Exception( "Pelin ollessa k‰ynniss‰ ei voi asettaa pelikentt‰‰." );
        
        if ( indeksi < 0 || indeksi >= this.malliPelikentta.length )
            throw new Exception( "Halutun pelikent‰n indeksi on virheellinen." );
        
        this.peli.asetaPelimaailma( this.malliPelikentta[indeksi][1] );
        
    }
    
    /**
     * Kertoo annetua indeksi‰ vastaavan pelikentt‰mallin nimen.
     * @param indeksi Pelikentt‰mallin indeksi.
     * @return Pelikentt‰mallin nimi.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ annettu indeksi on virheellinen.
     */
    public String kerroPelikentta( int indeksi ) throws IndexOutOfBoundsException {

        if ( indeksi < 0 || indeksi >= this.malliPelikentta.length )
            throw new IndexOutOfBoundsException( "Pelikent‰n indeksi on virheellinen." );
        
        return this.malliPelikentta[indeksi][0];
        
    }
    
    /**
     * Kertoo pelikentt‰mallien m‰‰r‰n.
     * @return Pelikentt‰mallien m‰‰r‰.
     */
    public int kerroPelikenttienMaara() {
        
        return this.malliPelikentta.length;
        
    }
    
    /**
     * Kertoo, onko annettu pelaaja ihmispelaaja, so. h‰nell‰ ei ole teko‰ly‰.
     * @param indeksi Pelaajan indeksi.
     * @return Totuusarvo, joka kertoo, onko pelaaja ihmispelaaja.
     */
    public boolean onIhmispelaaja( int indeksi ) {
        
        return this.peli.kerroPelaaja( indeksi ).kerroTekoaly() == null;

    }

    /**
     * Kertoo pelin pelaajien m‰‰‰r‰n.
     * @return Pelin pelaajien m‰‰r‰.
     */
    public int kerroPelaajienMaara() {
        
        return this.peli.kerroPelaajienMaara();
        
    }
    
    /**
     * Kertoo annetun pelaajan pisteet.
     * @param indeksi Pelaajan indeksi.
     * @return Pelaajan pisteet.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ indeksi on virheellinen.
     */
    public int kerroPelaajanPisteet( int indeksi ) {
        
        return this.peli.kerroPelaaja( indeksi ).kerroPisteet();
        
    }
    
    /**
     * Kertoo kuinka monta kertaa <CODE>pelaaja</CODE> on tappanut <CODE>tapetunPelaajan</CODE>.
     * @param pelaaja Pelaajan indeksi.
     * @return Pelaajan tappokerrat.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ indeksi on virheellinen.
     */
    public int kerroPelaajanTapot( int pelaaja, int tapettuPelaaja ) {
        
        return this.peli.kerroPelaaja( pelaaja ).kerroTapot( this.peli.kerroPelaaja( tapettuPelaaja ) );
        
    }
    
    /**
     * Kertoo annetun pelaajan nimen.
     * @param indeksi Pelaajan indeksi.
     * @return Pelaajan nimi.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ indeksi on virheellinen.
     */
    public String kerroPelaajanNimi( int indeksi ) {
        
        return this.peli.kerroPelaaja( indeksi ).kerroNimi();
        
    }
    
    /**
     * Kertoo annetun pelaajan aluksen indeksin tai -1, jos alusta ei ole.
     * @param indeksi Pelaajan indeksi.
     * @return Pelaajan aluksen indeksi tai -1.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ indeksi on virheellinen.
     */
    public int kerroPelaajanAlus( int indeksi ) {
        
        String alus = this.peli.kerroPelaaja( indeksi ).kerroAlus().kerroTyyppi();
        
        for ( int ind = 0; ind < this.malliAlus.length; ind++ ) {
            
            if ( this.malliAlus[ind].kerroTyyppi().equals( alus ) )
                return ind;
            
        }
        
        return -1;
        
    }
    
    /**
     * Kertoo annetun pelaajan teko‰lyn tai -1, jos pelaaja on ihmispelaaja.
     * @param indeksi Pelaajan indeksi.
     * @return Pelaajan teko‰lyn indeksi tai -1.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ indeksi on virheellinen.
     */
    public int kerroPelaajanTekoaly( int indeksi ) {
        
        Tekoaly tekoaly = this.peli.kerroPelaaja( indeksi ).kerroTekoaly();
        
        if ( tekoaly != null ) {
            
            for ( int ind = 0; ind < this.malliTekoaly.length; ind++ )
                if ( this.malliTekoaly[ind].kerroNimi().equals( tekoaly.kerroNimi() ) )
                    return ind;
            
        }
         
        return -1;
        
    }
    
    /**
     * Asettaa annetun pelaajan nimen.
     * @param indeksi Pelaajan indeksi.
     * @param nimi Nimi.
     * @throws Exception Poikkeus kertoo, ett‰ peli on k‰ynniss‰ tai nimi on virheellinen.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ indeksi on virheellinen.
     */
    public void asetaPelaajanNimi( int indeksi, String nimi ) throws Exception {
        
        if ( this.onKaynnissa() )
            throw new Exception( "Pelin ollessa k‰ynniss‰ ei voi asettaa pelaajan nime‰." );
        
        this.peli.kerroPelaaja( indeksi ).asetaNimi( nimi );
        
    }
    
    /**
     * Asettaa annetun pelaajan aluksen.
     * @param indeksi Pelaajan indeksi.
     * @param alus Aluksen indeksi.
     * @throws Exception Poikkeus kertoo, ett‰ peli on k‰ynniss‰, tai aluksen indeksi
     * on virheellinen.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ indeksi on virheellinen.
     */
    public void asetaPelaajanAlus( int indeksi, int alus ) throws Exception {
        
        if ( this.onKaynnissa() )
            throw new Exception( "Pelin ollessa k‰ynniss‰ ei voi asettaa pelaajan alusta." );
        
        if ( alus < 0 || alus >= this.malliAlus.length )
            throw new Exception( "Ei voi asettaa pelaajan alusta: Annettu aluksen indeksi virheellinen." );
        
        this.peli.kerroPelaaja( indeksi ).asetaAlus( new KappalePelaajanAlus( this.malliAlus[alus] ) );
        
    }
    
    /**
     * Asettaa annetun pelaajan teko‰lyn.
     * @param pelaaja Pelaajan indeksi.
     * @param tekoalyIndeksi Teko‰lyn indeksi tai -1, jos ei haluta teko‰ly‰.
     * @throws Exception Poikkeus kertoo, ett‰ peli on k‰ynniss‰, tai teko‰lyn indeksi
     * on virheellinen.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ indeksi on virheellinen.
     */
    public void asetaPelaajanTekoaly( int pelaaja, int tekoalyIndeksi ) throws Exception {
        
        if ( this.onKaynnissa() )
            throw new Exception( "Pelin ollessa k‰ynniss‰ ei voi asettaa pelaajan teko‰ly‰." );
        
        if ( tekoalyIndeksi < -1 || tekoalyIndeksi >= this.malliTekoaly.length )
            throw new Exception( "Ei voi asettaa pelaajan teko‰ly‰: Annettu teko‰lyn indeksi virheellinen." );
        
        if ( tekoalyIndeksi == -1 ) {
            
            this.peli.kerroPelaaja( pelaaja ).poistaTekoaly();
            
        }
        else {
            
            Tekoaly tekoaly = this.malliTekoaly[tekoalyIndeksi].kerroKopio();
            tekoaly.asetaPelaaja( this.peli.kerroPelaaja( pelaaja ) );

        }
            
    }

    /**
     * Lis‰‰ peliin annettujen tietojen perusteella pelaajan.
     * @param nimi Pelaajan nimi.
     * @param tekoalyIndeksi Teko‰lyn indeksi tai -1, jos ei haluta teko‰ly‰.
     * @param alusIndeksi Aluksen indeksi.
     * @return Luodun pelaajan indeksi.
     * @throws Exception Poikkeus kertoo, ett‰ annetuissa tiedoissa oli vikaa,
     * pelaajaa ei pystytty lis‰‰m‰‰n peliin tai peli on k‰ynniss‰.
     */
    public int lisaaPelaaja( String nimi, int tekoalyIndeksi, int alusIndeksi ) throws Exception {
        
        if ( this.onKaynnissa() )
            throw new Exception( "Pelin ollessa k‰ynniss‰ ei voi lis‰t‰ pelaajia." );
            
        if ( tekoalyIndeksi < -1 || tekoalyIndeksi >= this.malliTekoaly.length )
            throw new Exception( "Valittu teko‰ly on virheellinen." );
        
        if ( alusIndeksi < 0 || alusIndeksi >= this.malliAlus.length )
            throw new Exception( "Valittu alus on virheellinen." );
            
        Pelaaja uusiPelaaja = new Pelaaja( nimi );
        uusiPelaaja.asetaAlus( new KappalePelaajanAlus( this.malliAlus[alusIndeksi] ) );
        if ( tekoalyIndeksi != -1 )
            uusiPelaaja.asetaTekoaly( this.malliTekoaly[tekoalyIndeksi].kerroKopio() );
        
        //Koska pelaaja ei ole peliss‰, ei -1 palautusarvoa tarvitse pel‰t‰.
        return this.peli.lisaaPelaaja( uusiPelaaja );
        
    }
    
    /**
     * Poistaa pelaajan pelist‰, jos peli ei ole k‰ynniss‰.
     * @param pelaaja Pelaajan indeksi.
     */
    public void poistaPelaaja( int pelaaja ) {
        
        if ( this.onKaynnissa() )
            return;
        
        this.peli.kerroPelaaja( pelaaja ).poistaTekoaly();
        this.peli.poistaPelaaja( pelaaja );

    }

    /**
     * Kertoo pelaajien maksimim‰‰r‰n peliss‰.
     * @return Pelaajien maksimim‰‰r‰ peliss‰.
     */
    public int kerroPelaajienMaksimimaara() {
        
        return AvaruussotaPeli.PELAAJIEN_MAKSIMIMAARA;
        
    }
    
    /**
     * Asettaa pistem‰‰r‰n, jolla pelin voittaa.
     * @param voitonPistemaara Voittoon tarvittava pistem‰‰r‰.
     * @throws Exception Poikkeus kertoo, ett‰ peli on k‰ynniss‰ tai pistem‰‰r‰ on
     * virheellinen.
     */
    public void asetaVoitonPistemaara( int voitonPistemaara ) throws Exception {
        
        if ( this.onKaynnissa() )
            throw new Exception( "Voiton pistem‰‰r‰‰ ei voi asettaa: Peli on k‰ynniss‰." );
        
        this.peli.asetaVoitonPistemaara( voitonPistemaara );
        
    }
    
    /**
     * Jos peli on voitettu, kertoo pelin voittajan indeksin. Muuten palauttaa -1.
     * @return Voittajan indeksi tai -1.
     */
    public int kerroVoittaja() {
        
        if ( this.peli.onVoitettu() )
            return this.peli.tutkiVoittaja();
        
        return -1;
        
    }
    
    /**
     * Kertoo mallialuksen nimen.
     * @param indeksi Mallialuksen indeksi.
     * @return Mallialuksen nimi.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ annettu indeksi on virheellinen.
     */
    public String kerroAlus( int indeksi ) throws IndexOutOfBoundsException {
        
        if ( indeksi < 0 || indeksi >= this.malliAlus.length )
            throw new IndexOutOfBoundsException( "Annettu mallialuksen indeksi on virheellinen." );
        
        return this.malliAlus[indeksi].kerroTyyppi();
        
    }

    /**
     * Kertoo mallialusten m‰‰r‰n.
     * @return Mallialusten m‰‰r‰.
     */
    public int kerroAlustenMaara() {
        
        return this.malliAlus.length;
        
    }
    
    /**
     * Kertoo malliteko‰lyn nimen.
     * @param indeksi Malliteko‰lyn indeksi.
     * @return Malliteko‰lyn nimi.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ annettu indeksi on virheellinen.
     */
    public String kerroTekoaly( int indeksi ) throws IndexOutOfBoundsException {
        
        if ( indeksi < 0 || indeksi >= this.malliTekoaly.length )
            throw new IndexOutOfBoundsException( "Annettu teko‰lyn indeksi on virheellinen." );
        
        return this.malliTekoaly[indeksi].kerroNimi();
        
    }

    /**
     * Kertoo malliteko‰lyjen m‰‰r‰n.
     * @return Malliteko‰lyjen m‰‰r‰.
     */
    public int kerroTekoalyjenMaara() {
        
        return this.malliTekoaly.length;
        
    }
    
    /**
     * Kertoo ihmispelaajien m‰‰r‰n peliss‰, so. laskee sellaiset pelaajat, joilla
     * ei ole teko‰ly‰.
     * @return Ihmispelaajien m‰‰r‰.
     */
    public int kerroIhmispelaajienMaara() {
        
        int maara = 0;
        
        for ( int ind = 0; ind < this.peli.kerroPelaajienMaara(); ind++ ) {
            
            if ( this.onIhmispelaaja( ind ) )
                maara++;
            
        }
        
        return maara;

    }

    /**
     * Kertoo indeksi‰ vastaavan ihmispelaajan indeksin kaikkien pelaajien joukossa tai
     * palauttaa -1, jos peliss‰ ei ole niin paljon ihmispelaajia.
     * @param indeksi Ihmispelaajaindeksi.
     * @return Indeksi kaikkien pelaajien joukossa tai -1.
     */
    public int kerroIhmispelaajanIndeksi( int indeksi ) {
        
        if ( indeksi < 0 )
            return -1;
        
        int maara = 0;
        int ind;
        
        //K‰yd‰‰n kaikki pelaajat l‰pi.
        for ( ind = 0; ind < this.peli.kerroPelaajienMaara(); ind++ ) {
            
            if ( this.onIhmispelaaja( ind ) )
                maara++;
            
            if ( maara == indeksi + 1 )
                return ind;
            
        }
        
        //Ei ollut niin montaa ihmispelaajaa.
        return -1;

    }
    
    /**
     * Asettaa ihmispelaajan ohjauksen. Jos annettu indeksi on virheellinen tai suunta
     * ei ole joukosta (-1,0,1), ei metodi tee mit‰‰n.
     * @param indeksi Ihmispelaajaindeksi.
     * @param suunta Ohjauksen suunta.
     */
    public void asetaIhmispelaajanOhjaus( int indeksi, int suunta ) {
        
        //Muutetaan indeksi.
        indeksi = this.kerroIhmispelaajanIndeksi( indeksi );
        
        if ( indeksi == -1 )
            return;
        
        KappalePelaajanAlus alus = this.peli.kerroPelaaja( indeksi ).kerroAlus();
        
        if ( alus != null )
            alus.asetaOhjaus( suunta );
       
    }
    
    /**
     * Asettaa ihmispelaajan kaasun. Jos annettu indeksi on virheellinen, ei metodi
     * tee mit‰‰n.
     * @param indeksi Ihmispelaajaindeksi.
     * @param kaasuPaalla Totuusarvo kertoo, onko kaasu pohjassa.
     */
    public void asetaIhmispelaajanKaasu( int indeksi, boolean kaasuPaalla ) {
        
        indeksi = this.kerroIhmispelaajanIndeksi( indeksi );
        
        if ( indeksi == -1 )
            return;
        
        KappalePelaajanAlus alus = this.peli.kerroPelaaja( indeksi ).kerroAlus();
        
        if ( alus != null )
            alus.asetaKaasu( kaasuPaalla );
        
    }
    
    /**
     * Asettaa ihmispelaajan liipaisimen. Jos annettu indeksi on virheellinen, ei metodi
     * tee mit‰‰n.
     * @param indeksi Ihmispelaajaindeksi.
     * @param ammutaan Totuusarvo kertoo, onko liipaisin pohjassa.
     */
    public void asetaIhmispelaajanLiipaisin( int indeksi, boolean ammutaan ) {
        
        indeksi = this.kerroIhmispelaajanIndeksi( indeksi );
        
        if ( indeksi == -1 )
            return;
        
        KappalePelaajanAlus alus = this.peli.kerroPelaaja( indeksi ).kerroAlus();
        
        if ( alus != null )
            alus.asetaLiipaisin( ammutaan );
        
    }
    
    /**
     * Piirt‰‰ n‰kym‰n pelimaailmasta keskitt‰en annettua indeksi‰ vastaavaan pelaajaan.
     * Jos peli ei ole k‰ynniss‰ tai annettu pinta on null, ei metodi tee mit‰‰n.
     * N‰kym‰‰n piirret‰‰n myˆs pelaajan nimi, pisteet ja energiapalkki. Jos pelaaja on tuhoutunut,
     * keskitet‰‰n n‰kym‰ pelaajan r‰j‰hdykseen. Piirt‰‰ n‰kym‰n ymp‰rille pikselin
     * levyisen kehyksen.
     * @param piirtokonteksti Piirtopinta, johon n‰kym‰ piirret‰‰n.
     * @param indeksi Keskitett‰v‰n pelaajan indeksi.
 	 * @param x N‰kym‰n vasemman yl‰kulman x-koordinaatti piirtokontekstilla.
	 * @param y N‰kym‰n vasemman yl‰kulman y-koordinaatti piirtokontekstilla.
 	 * @param leveys N‰kym‰n leveys.
	 * @param korkeus N‰kym‰n korkeus.
	 * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ pelaajan indeksi on virheellinen.
     */
    public void piirraNakyma( Graphics piirtokonteksti, int indeksi, int x, int y, int leveys, int korkeus ) throws IndexOutOfBoundsException {
        
        if ( !this.onKaynnissa() || piirtokonteksti == null )
        	return;

        synchronized ( this.peli.kerroMaailma() ) {
            
            Pelaaja pelaaja = this.peli.kerroPelaaja( indeksi );
            Kappale keskitettava = pelaaja.kerroAlus();
            
            //Pelaajan ollessa kuollut, keskitet‰‰n r‰j‰hdykseen.
            if ( keskitettava.onTuhoutunut() )
                keskitettava = pelaaja.kerroAlus().kerroRajahdys();
            
            //Piirt‰‰ n‰kym‰n avaruudesta.
            this.peli.piirraNakyma( piirtokonteksti,
                    				keskitettava,
                    				x,
                    				y,
                    				leveys,
                    				korkeus );
            
            //Piirt‰‰ nimen, pisteet, kehyksen ja energiapalkin sinisell‰.
            piirtokonteksti.setColor( Color.BLUE );
            piirtokonteksti.drawString( ( pelaaja.kerroNimi() + ": " + pelaaja.kerroPisteet() ), x + 10, y + 30 );
            piirtokonteksti.drawRect( x, y, leveys - 1, korkeus - 1 );
            piirtokonteksti.fillRect( x + 10,
                    				  y + 10,
                    				  ( int )( ( leveys - 20 ) * ( pelaaja.kerroAlus().kerroEnergia() / pelaaja.kerroAlus().kerroMaksimienergia() ) ),
                    				  5 );
            
        }
        
    }
    
}
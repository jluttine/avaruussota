
import java.util.ArrayList;
import java.awt.*;

/**
 * 
 * Luokka <CODE>AvaruussotaPeli<CODE> kuvaa johonkin <CODE>Avaruus<CODE>-luokan olion
 * mallintamaan maailmaan sijoittuvaa melee-taistelupeli‰. Peliss‰ voi olla useita
 * pelaajia ja niille voi m‰‰r‰t‰ tietokoneohjauksen.
 * Oliota luodessa ja sit‰ k‰ytetett‰ess‰ on hyv‰ toimia tiettyjen s‰‰ntˆjen mukaan:
 * Ensin asetetaan pelimaailma ja lis‰t‰‰n halutut pelaajat. T‰m‰n j‰lkeen tarvitsee
 * vain l‰hett‰‰ kaynnista-komento ja peli alkaa pyˆri‰ aivan itsest‰‰n. Kun peli
 * lopetetaan, kutsutaan lopeta-metodia. T‰llˆin on mahdollista viel‰ tutkia pelaajien
 * pisteit‰ ym. Kun kutsutaan tyhjenna-metodia, kaikki pelaajat ja tiedot poistetaan
 * eik‰ olio tied‰ juuri loppuneesta pelist‰ en‰‰ mit‰‰n.
 *
 * @author Jaakko Luttinen
 *
 */

public class AvaruussotaPeli {
    
    /**
     * Pelaajien maksimim‰‰r‰ peliss‰.
     */
    public static final int PELAAJIEN_MAKSIMIMAARA = 12;
    
    /**
     * Voiton pistem‰‰r‰n yl‰raja. Alaraja on 1.
     */
    public static final int VOITON_MAKSIMIPISTEMAARA = 99;
    
    /**
     * Lista peliss‰ olevista pelaajista.
     */
    private ArrayList pelaajat;
    
    /**
     * Avaruusmaailma, johon peli sijoittuu.
     */
    private Avaruus maailma;
    
    /**
     * Pelimaailmaa pyˆritt‰v‰ s‰ie.
     */
    private SaiePelimaailma maailmaSaie;
    
    /**
     * Teko‰lyjen miettimisist‰ huolehtiva s‰ie.
     */
    private SaieTietokoneohjaus tekoalySaie;
    
    /**
     * Pistem‰‰r‰, joka voittoon tarvitaan.
     */
    private int voitonPistemaara;
    
    /**
     * Tiedosto, jossa pelikentt‰ on, tai null, jos pelikentt‰ halutaan generoida.
     */
    private String pelikenttaTiedosto;
    
    /**
     * Totuusarvo, joka kertoo, onko joku pelaajista saavuttanut voiton.
     */
    private boolean peliVoitettu;
    
    /**
     * Luo uuden pelin. Luo tietokoneohjauksen s‰ikeen huolehtimaan t‰m‰n pelin
     * teko‰lyjen miettimisist‰. Asettaa voittoon tarvittavaksi pistem‰‰r‰ksi 1.
     */
    public AvaruussotaPeli() {
        
        this.pelaajat = new ArrayList();
        this.tekoalySaie = new SaieTietokoneohjaus( this );
        this.maailmaSaie = new SaiePelimaailma();
        this.voitonPistemaara = 1;
        
    }

    /**
     * Kertoo pelimaailmaa pyˆritt‰v‰n s‰ikeen.
     * @return Pelimaailmaa pyˆritt‰v‰ s‰ie.
     */
    public SaiePelimaailma kerroPelimaailmaSaie() {
        
        return this.maailmaSaie;
        
    }
    
    /**
     * Kertoo pelaajan, joka annetulla indeksill‰ lˆytyy.
     * @param indeksi Pelaajan indeksi.
     * @return Pelaaja, joka annetulla indeksill‰ lˆytyy tai null.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ indeksi on virheellinen eli
     * ei ole v‰lill‰ 0 - (pelaajien m‰‰r‰-1).
     */
    public Pelaaja kerroPelaaja( int indeksi ) throws IndexOutOfBoundsException{
        
        if ( indeksi < 0 || indeksi >= this.pelaajat.size() )
            throw new IndexOutOfBoundsException( "Annettu pelaajan indeksi on virheellinen." );
        
        return ( Pelaaja )this.pelaajat.get( indeksi );
        
    }
    
    /**
     * Kertoo pelaajien m‰‰r‰n peliss‰.
     * @return Pelaajien m‰‰r‰ peliss‰.
     */
    public int kerroPelaajienMaara() {
        
        return this.pelaajat.size();
        
    }

    /**
     * Lis‰‰ pelaajan peliin ja huolehtii siit‰, ett‰ myˆs pelaaja saa tiedon kuuluvansa
     * t‰h‰n peliin. Lis‰‰minen ep‰onnistuu, jos 1:pelaaja kuuluu jo peliin, 2:peliss‰ on
     * jo maksimim‰‰r‰ pelaajia tai 3:peliss‰ on jo samanniminen pelaaja.
     * @param uusiPelaaja Pelaaja, joka peliin halutaan lis‰t‰.
     * @return Lis‰tyn pelaajan indeksi tai -1 jos pelaaja on jo peliss‰.
     * @throws Exception Poikkeus kertoo, ett‰ pelaajan lis‰‰minen ep‰onnistui (tapaus 2 tai 3).
     */
    public int lisaaPelaaja( Pelaaja uusiPelaaja ) throws Exception {
        
        if ( this.pelaajat.contains( uusiPelaaja ) ) 
            return -1;
        
        //Jos pelaajien maksimim‰‰r‰ saavutettu --> poikkeus!
        if ( this.pelaajat.size() >= AvaruussotaPeli.PELAAJIEN_MAKSIMIMAARA )
            throw new Exception( "Pelaajaa ei voida lis‰t‰. Peliss‰ on jo maksimim‰‰r‰ (" +
                    			  AvaruussotaPeli.PELAAJIEN_MAKSIMIMAARA + ") pelaajia." );
        
        //Jos saman niminen pelaaja jo on peliss‰ --> poikkeus!
        if ( this.onPelaaja( uusiPelaaja.kerroNimi() ) )
            throw new Exception( "Pelaajaa ei voida lis‰t‰. \"" + uusiPelaaja.kerroNimi() +
                    			 "\"-niminen pelaaja on jo peliss‰." );
        
        this.pelaajat.add( uusiPelaaja );
        uusiPelaaja.asetaPeliin( this );

        return this.pelaajat.size() - 1;
        
    }

    /**
     * Poistaa annettua indeksi‰ vastaavan pelaajan pelist‰.
     * @param indeksi Poistettavan pelaajan indeksi.
     * @throws IndexOutOfBoundsException Poikkeus kertoo, ett‰ indeksi on virheellinen eli
     * ei ole v‰lill‰ 0 - (pelaajien m‰‰r‰-1).
     */
    public void poistaPelaaja( int indeksi ) throws IndexOutOfBoundsException {
        
        if ( indeksi < 0 || indeksi >= this.pelaajat.size() )
            throw new IndexOutOfBoundsException( "Annettu pelaajan indeksi on virheellinen." );
        
        Pelaaja vanhaPelaaja = ( Pelaaja )this.pelaajat.remove( indeksi );
        vanhaPelaaja.poistaPelista();

    }

    /**
     * Jos annettu pelaaja on peliss‰, poistetaan se pelist‰.
     * @param poistettava Pelaaja, joka pelist‰ halutaan poistaa.
     */
    public void poistaPelaaja( Pelaaja poistettava ) {
        
        int indeksi = this.pelaajat.indexOf( poistettava );
        
        if ( indeksi >= 0 )
            this.poistaPelaaja( indeksi );
        
    }

    /**
     * Poistaa kaikki pelaajat pelist‰.
     */
    public void poistaKaikkiPelaajat() {

        int koko = 0;
        
        while ( ( koko = this.pelaajat.size() ) > 0 )
            this.poistaPelaaja( koko - 1 );
        
    }

    /**
     * Kertoo, onko peliss‰ annetun niminen pelaaja.
     * @param nimi Nimi, jota etsit‰‰n.
     * @return Totuusarvo, joka kertoo, onko peliss‰ annetun niminen pelaaja.
     */
    public boolean onPelaaja( String nimi ) {
        
        for ( int ind = 0; ind < this.pelaajat.size(); ind++ ) {
            
            if ( ( ( Pelaaja )this.pelaajat.get( ind ) ).kerroNimi().equals( nimi ) )
                return true;
            
        }
        
        return false;
        
    }
    
    /**
     * Nollaa kaikkien pelin pelaajien pisteet. Laskee voittoehtojen t‰yttymisen
     * merkiksi nostetun lipun.
     */
    public void nollaaPelaajienPisteet() {
        
        for ( int ind = 0; ind < this.pelaajat.size(); ind++ )
            ( ( Pelaaja )this.pelaajat.get( ind ) ).nollaaPisteet();
        
        this.peliVoitettu = false;
            
    }

    /**
     * Kertoo avaruusmaailman, johon peli sijoittuu.
     * @return Avaruusmaailma, johon peli sijoittuu.
     */
    public Avaruus kerroMaailma() {
        
        return this.maailma;
        
    }
    
    /**
     * Asettaa pelikent‰ksi tiedostosta lˆytyv‰n kent‰n. Jos annettu tiedosto on null,
     * ei metodi tee mit‰‰n.
     * @param tiedosto Pelikent‰n tiedosto.
     */
    public void asetaPelimaailma( String tiedosto ) {
        
        if ( tiedosto != null ) {
            
            this.lopeta();
            this.pelikenttaTiedosto = tiedosto;
            this.maailma = null;
            
        }
        
    }
    
    /**
     * Asettaa pelikent‰ksi generoitavan kent‰n, jonka mitat ovat annetut.
     * @param leveys Generoitavan kent‰n leveys.
     * @param korkeus Generoitavan kent‰n korkeus.
     * @throws Exception Poikkeus, joka kertoo, ett‰ annetut mitat olivat virheelliset.
     */
    public void asetaPelimaailma( int leveys, int korkeus ) throws Exception {
        
        this.lopeta();
        this.maailma = new Avaruus( leveys, korkeus );
        this.pelikenttaTiedosto = null;
        
    }

    /**
     * Arpoo annetulle kappaleelle laillisen sijainnin avaruusmaailmasta. Sijaintia
     * yritet‰‰n arpoa niin kauan, kunnes laillinen sijainti saavutetaan. T‰m‰ voi
     * jossakin tapauksessa aiheuttaa ikuisen silmukan, joten kutsujan on todella
     * tiedett‰v‰, mit‰ tekee. Laillinen alue on sellainen, joka ei leikkaa yht‰k‰‰n
     * avaruuden tˆrm‰tt‰viss‰ olevaa kappaletta.
     * @param kappale Kappale, jolle sijainti arvotaan.
     * @throws Exception Poikkeus kertoo, ett‰ kappale on liian iso avaruuteen tai
     * kappaletta ei onnistuttu lukuisista yrityksist‰ huolimatta sovittamaan
     * avaruuteen.
     */
    public void arvoSijainti( Kappale kappale ) throws Exception {

        //Arvotaan sijainti.
        Koordinaatit sijainti = new Koordinaatit( this.maailma.kerroKoordinaatisto(),
                								  Math.random() * this.maailma.kerroKoordinaatisto().kerroLeveys(),
                								  Math.random() * this.maailma.kerroKoordinaatisto().kerroKorkeus() );
        
        Alue paikka = kappale.kerroAlue();
        
        paikka.asetaSijainti( sijainti );
        
        //Arvotaan kappaleelle sijaintia, kunnes sijainti on laillinen (so. alue ei leikkaa
        //mit‰‰n tˆrm‰tt‰viss‰ olevaa kappaletta).
        int ind = 0;
        while ( this.maailma.onLaitonAlue( paikka ) && ind < 10000 ) {
            
            sijainti.asetaSijainti( Math.random() * this.maailma.kerroKoordinaatisto().kerroLeveys(),
                    				Math.random() * this.maailma.kerroKoordinaatisto().kerroKorkeus() );
            
            ind++;
            
        }
        
        if ( ind == 10000 ) {
            
            throw new Exception( "Kappaletta ei saatu mahtumaan avaruuteen huolimatta tuhansista yrityksist‰. " + 
                            	 "Kappaleen alue olisi kuitenkin koordinaatistoon sopiva." );
            
        }
        
        kappale.asetaMaailmaan( this.maailma, sijainti.kerroSijaintiX(), sijainti.kerroSijaintiY() );
        
    }
    
    /**
     * Arpoo annetun pelaajan alukselle uuden sijainnin. Jos pelaaja ei ole peliss‰ tai
     * pelaajalla ei ole alusta, ei metodi tee mit‰‰n. 
     * @param pelaaja Pelaaja, jonka alukselle uusi sijainti arvotaan.
     * @throws RuntimeException Vapaasti heitett‰v‰ poikkeus kertoo, ett‰ alusta ei kerta
     * kaikkiaan saatu mahtumaan avaruuteen. T‰llaista tilannetta ei pit‰isi synty‰, mutta
     * jos t‰t‰ tapahtuu, on ohjelman jollakin hallitulla tavalla kyett‰v‰ se k‰ytt‰j‰lle
     * kertomaan.
     */
    public void arvoPelaajanSijainti( Pelaaja pelaaja ) {
        
        try {
            
            if ( pelaaja.kerroPeli() == this ) {
                
                KappalePelaajanAlus alus = pelaaja.kerroAlus();
                
                if ( alus != null )
                    this.arvoSijainti( pelaaja.kerroAlus() );
                
            }
            
        }
        catch ( Exception virhe ) {

            throw new RuntimeException( "Pelaajan \"" + pelaaja.kerroNimi() + "\" alukselle " +
                    					"ei kyetty arpomaan uutta sijaintia:\n" + virhe.getMessage() );
            
        }
        
    }
    
    /**
     * Jos peli‰ ei ole viel‰ voitettu, muuttaa pelaajien pisteit‰ sellaisen tilanteen
     * edellytt‰m‰ll‰ tavalla, miss‰ <CODE>syy</CODE> on tappanut <CODE>tapetun</CODE>.
     * Jos pelaaja tappaa toisen pelaajan, saa tappaja yhden pisteen. Jos pelaaja tappaa
     * itsens‰ tai tappajana ei ole mik‰‰n pelaajan ohjaama alus, saa h‰n -1 pistett‰.
     * Tutkii, onko joku pelaaja nyt t‰ytt‰nyt voiton ehdot.
     * @param tapettu Pelaaja, joka on tapettu.
     * @param syy Kappale, joka on syyn‰ pelaajan kuolemiseen (esim. ammuksen ampunut alus).
     */
    public void suoritaTappo( Pelaaja tapettu, Kappale syy ) {
    
        if ( this.peliVoitettu || tapettu == null )
            return;
        
        if ( syy == tapettu.kerroAlus() ) {
            
            //Tappoi itse itsens‰. -1 piste?
            tapettu.muutaPisteet( tapettu, -1 );
            
        }
        else if ( syy instanceof KappalePelaajanAlus ) {
            
            Pelaaja tappaja = ( ( KappalePelaajanAlus )syy ).kerroPelaaja();
            
            if ( tappaja != null ) {
                
                //Lis‰t‰‰n pelaajalle jokin tappo tai jotain.
                tappaja.muutaPisteet( tapettu, 1 );
                
            }
            
        }
        else {
            
            //Syyn‰ kuolemaan jokin muu. -1 piste.
            //Jos syy == null, tullaan myˆs t‰nne.
            tapettu.muutaPisteet( null, -1 );
            
        }

        this.tutkiVoittaja();

    }
    
    /**
     * Asettaa pistem‰‰r‰n, joka voittoon tarvitaan. Jos annettu pistem‰‰r‰ on virheellinen
     * (ei ole v‰lill‰ 1-<CODE>VOITON_MAKSIMIPISTEMAARA</CODE>), heitet‰‰n poikkeus.
     * Nollataan pelaajien pisteet.
     * @param voitonPistemaara Pistem‰‰r‰, joka voittoon tarvitaan.
     * @throws Exception Poikkeus, joka kertoo, ett‰ annettu pistem‰‰r‰ on virheellinen.
     */
    public void asetaVoitonPistemaara( int voitonPistemaara ) throws Exception {
        
        if ( voitonPistemaara < 1 || voitonPistemaara > AvaruussotaPeli.VOITON_MAKSIMIPISTEMAARA )
            throw new Exception( "Pelin voiton pistem‰‰r‰‰ ei voitu asettaa. Pistem‰‰r‰n t‰ytyy olla v‰lill‰ 1-" +
                    			 AvaruussotaPeli.VOITON_MAKSIMIPISTEMAARA + "." );
        
        this.voitonPistemaara = voitonPistemaara;
        this.nollaaPelaajienPisteet();
        
    }
    
    /**
     * Kertoo pelin voittaneen pelaajan indeksin tai -1, jos pelin voittamisen ehtoja
     * ei ole t‰ytt‰nyt viel‰ kukaan pelaaja. Nostaa tai laskee pelin voittamisen merkkin‰
     * olevan lipun.
     * @return Pelaajan, joka pelin on voittanut, indeksi tai -1, jos peli‰ ei ole voitettu.
     */
    public int tutkiVoittaja() {
        
        for ( int ind = 0; ind < this.pelaajat.size(); ind ++ ) {
            
            Pelaaja tutkittava = ( Pelaaja )this.pelaajat.get( ind );
            if ( tutkittava.kerroPisteet() >= this.voitonPistemaara ) {
                
                this.peliVoitettu = true;
                return ind;
                
            }
            
        }
        
        this.peliVoitettu = false;
        return -1;
        
    }
    
    /**
     * Kertoo, onko pelin voittamisen merkiksi nostettu lippu. Metodi ei etsi voittajaa,
     * vaan kertoo, vain onko sellainen aiemmin lˆydetty ja lippu sen merkiksi nostettu.
     * @return Totuusarvo, joka kertoo, onko peli voitettu.
     */
    public boolean onVoitettu() {
        
        return this.peliVoitettu;
        
    }

    /**
     * Luo uuden pelimaailman. Jos pelimaailmaksi on asetettu tiedosto, ladataan se.
     * Jos pelimaailmaksi on asetettu maailman mitat, generoidaan se. Jos latauksessa
     * tai generoinnissa tapahtuu virheit‰, tai tiedostoa tai mittoja ei ole asetettu,
     * heitet‰‰n poikkeus.  
     * @throws Exception Poikkeus, jos latauksessa, generoinnissa tai alkuasetuksissa on
     * virheit‰.
     */
    private void luoUusiMaailma() throws Exception {
        
        GeneroijaPelikentta generoija = new GeneroijaPelikentta();
        
        if ( this.pelikenttaTiedosto != null ) {
            
            //Pelimaailmaksi on asetettu tiedosto.
            this.maailma = generoija.generoiPelimaailma( this.pelikenttaTiedosto );
            
        }
        else if ( this.maailma != null ) {
            
            //Pelimaailmaksi on asetettu tiettyjen mittojen mukaan generoitava kentt‰.
            int leveys = this.maailma.kerroKoordinaatisto().kerroLeveys();
            int korkeus = this.maailma.kerroKoordinaatisto().kerroKorkeus();
            
            this.maailma = generoija.generoiPelimaailma( leveys, korkeus );
            
        }
        else {
            
            //Pelimaailmaksi ei ole valittu mit‰‰n.
            throw new Exception( "Pelimaailmaa ei ole asetettu." );
            
        }
        
    }
    
    /**
     * Heitt‰‰ poikkeuksen, jos pelin asetuksissa on jokin k‰ynnistyst‰ est‰v‰
     * virhe.
     * @throws Exception Poikkeus kertoo, ett‰ pelin asetuksissa on k‰ynnistyksen
     * est‰v‰ virhe.
     */
    public void tutkiKaynnistettavyys() throws Exception {
        
        if ( this.pelaajat.size() < 2 || this.pelaajat.size() > AvaruussotaPeli.PELAAJIEN_MAKSIMIMAARA ) {
            
            throw new Exception( "Peli‰ ei voida k‰ynnist‰‰. Peliss‰ pit‰‰ olla 2-" +
                    			 AvaruussotaPeli.PELAAJIEN_MAKSIMIMAARA + " pelaajaa." );
        
        }
        
    }

    /**
     * K‰ynnist‰‰ pelin luomalla maailman, nollaamalla pelaajien pisteet, arpomalla pelaajien
     * aluksille sijainnit, pist‰m‰ll‰ maailman pyˆrim‰‰n ja teko‰lyt miettim‰‰n. Lopettaa
     * mahdollisen aiemman pelin.
     * @throws Exception Poikkeus kertoo, ett‰ alkuasetukset ovat pieless‰.
     */
    public void kaynnista() throws Exception {

        this.lopeta();

        //Heitt‰‰ poikkeuksia, jos jokin on asetus on pieless‰.
        this.tutkiKaynnistettavyys();
        
        this.luoUusiMaailma();
        this.nollaaPelaajienPisteet();
        
        //Laitetaan pelaajien alukset maailmaan.
        for ( int ind = 0; ind < this.pelaajat.size(); ind++ )
            this.arvoSijainti( ( ( Pelaaja )this.pelaajat.get( ind ) ).kerroAlus() );

        this.maailmaSaie.kaynnista( this.maailma );
        this.tekoalySaie.kaynnista();
        
    }
    
    /**
     * Lopettaa pelin lopettamalla teko‰lyjen miettimisen, poistamalla kaikki kappaleet
     * avaruudesta ja lopettamalla maailmaa pyˆritt‰v‰n s‰ikeen.
     */
    public void lopeta() {
        
        this.tekoalySaie.lopeta();
        
        if ( this.maailma != null )
            this.maailma.poistaKaikkiKappaleet();
        
        this.maailmaSaie.lopeta();
        
    }
    
    /**
     * Lopettaa pelin, poistaa maailman ja poistaa kaikki pelaajat pelist‰.
     */
    public void tyhjenna() {
        
        this.lopeta();
        this.maailma = null;
        this.poistaKaikkiPelaajat();
        
    }
    
    /**
     * Piirt‰‰ pelimaailmasta halutun kokoisen kuvan annetulle piirtokontekstille keskitt‰en
     * kuvan annettuun kappaleeseen. Jos kappaleella ei ole sijaintia tai se ei sijaitse 
     * maailman koordinaatistossa, keskitt‰‰ koordinaatteihin (0,0). Jos n‰kym‰n mitat
     * ovat avaruuden mittoja suuremmat, ei n‰kym‰ v‰ltt‰m‰tt‰ piirry t‰ysin oikein. 
     * @param piirtokonteksti Piirtokonteksti, jolle n‰kym‰ piirret‰‰n.
     * @param keskitettava Kappale, johon n‰kym‰ keskitet‰‰n.
	 * @param vasen N‰kym‰n vasemman yl‰kulman x-koordinaatti piirtokontekstilla.
	 * @param yla N‰kym‰n vasemman yl‰kulman y-koordinaatti piirtokontekstilla.
     * @param leveys N‰kym‰n leveys.
     * @param korkeus N‰kym‰n korkeus.
     */
    public void piirraNakyma( Graphics piirtokonteksti, Kappale keskitettava, int vasen, int yla, int leveys, int korkeus ) {
        
        //Synkronoidaan, ett‰ voidaan laskea kappaleen keskipiste, eik‰ se sitten en‰‰ muutu.
        synchronized ( this.maailma ) {
            
            double x = 0;
            double y = 0;
            Koordinaatit sijaintiKeskipiste = keskitettava.kerroAlue().kerroSijainti();        
            
            if ( sijaintiKeskipiste != null && sijaintiKeskipiste.kerroKoordinaatisto() == this.maailma.kerroKoordinaatisto() ) {
                
                x = sijaintiKeskipiste.kerroSijaintiX();
                y = sijaintiKeskipiste.kerroSijaintiY();
                
            }
            
            this.maailma.piirraNakyma( piirtokonteksti, x, y, vasen, yla, leveys, korkeus );
            
        }
        
    }
    
}

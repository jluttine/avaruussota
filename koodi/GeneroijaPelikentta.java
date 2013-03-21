
import java.io.IOException;

/**
 * 
 * Luokka <CODE>GeneroijaPelikentta</CODE> kuvaa pelikent‰n generoijaa.
 * Generoija rakentaa pelikent‰n joko annetun tiedoston tai annetujen
 * mittojen perusteella. Jos generointi tapahtuu mittojen perusteella,
 * luodaan pelikentt‰‰n maa-alueita ja niiden muodostamia maa-aluesysteemej‰. 
 * 
 * @author Jaakko Luttinen
 *
 */
public class GeneroijaPelikentta {
    
    /**
     * Vakio kertoo, kuinka paljon testiellipsin halkaisija on todellista ellipsi‰ suurempi.
     */
    private static final int TESTIELLIPSIN_ISOMMUUS = 80;

    /**
     * Pelikentt‰, jota generoidaan.
     */
    private Avaruus maailma;
    
    /**
     * Generoi uuden pelikent‰n annettujen mittojen perusteella. Luo avaruuden ja
     * lis‰‰ sinne maa-alueita ja niist‰ muodostuvia systeemej‰.
     * @param leveys Pelikent‰n leveys.
     * @param korkeus Pelikent‰n korkeus.
     * @return Generoitu pelikentt‰.
     * @throws Exception Poikkeus kertoo, ett‰ annetut mitat olivat virheelliset.
     */
    public Avaruus generoiPelimaailma( int leveys, int korkeus ) throws Exception {
        
        //Luo avaruuden.
        this.maailma = new Avaruus( leveys, korkeus );

        //T‰m‰ luku kertoo, kuinka suuri osuus maa-alueiden pinta-alalla tulisi avaruudesta
        //olla. Osuus voi kasvaa suuremmaksi tai j‰‰d‰ jopa hieman pienemm‰ksikin (jos maa-alueita
        //ei vain saa sopimaan).
        double maanosuus = 0.4;
        
        double kentanAla = this.maailma.kerroKoordinaatisto().kerroLeveys() * this.maailma.kerroKoordinaatisto().kerroKorkeus();
        double kaytettyAla = 0;
        int kappaleitaMaailmassa = this.maailma.kerroKappaleidenMaara();
        
        int ind = 0;
        
        //Lis‰t‰‰n maa-alue systeemej‰ kunnes niiden ala on riitt‰v‰ tai
        //maa-alueita on yritetty luoda tarpeeksi monta kertaa (ei haluta
        //ikuista silmukkaa). Fakta kuitenkin on, ettei avaruuteen v‰ltt‰m‰tt‰
        //sovi niin paljon maa-alueita kuin maanosuudella on pyydetty.
        while ( kaytettyAla / kentanAla < maanosuus && ind < 10000 ) {
        
            //Sys‰t‰‰n maa-alue systeemin generointi liikkeelle.
            this.luoLapsiMaaAlueet( null, 1 );
            
            //Lis‰t‰‰n uusien maa-alueiden pinta-alat.
            for ( ; kappaleitaMaailmassa < this.maailma.kerroKappaleidenMaara(); kappaleitaMaailmassa++ )
                kaytettyAla += this.maailma.kerroKappale( kappaleitaMaailmassa ).kerroAlue().kerroAla();

            ind++;

        }
        
        return this.maailma;
       
    }
    
    /**
     * Luo pelikent‰n annetun tiedoston perusteella. Tiedoston tulee noudattaa
     * niit‰ muotorajoituksia, jotka luokka TiedostonlukijaPelikentta asettaa.
     * Lis‰ksi kappaleiden tulee olla sellaisia, ett‰ ne sopivat avaruuteen.
     * Kappaleet eiv‰t saa myˆsk‰‰n olla niin isoja, ettei muisti riit‰
     * niiden varastoimiseen.
     * @param pelikenttaTiedosto Pelikentt‰tiedostopolku.
     * @return Rakennettu pelikentt‰.
     * @throws Exception Poikkeus kertoo, ett‰ tiedostossa oli jokin virhe.
     */
    public Avaruus generoiPelimaailma( String pelikenttaTiedosto ) throws Exception {
        
        try {
            
            //Luodaan tiedostonlukija.
            TiedostonlukijaPelikentta pelikenttaLukija = new TiedostonlukijaPelikentta( pelikenttaTiedosto );
            
            //Luodaan avaruus otsikkotietojen perusteella.
            this.maailma = new Avaruus( pelikenttaLukija.kerroLeveys(), pelikenttaLukija.kerroKorkeus() );
            
            //Luetaan kaikki maa-alueet.
            while ( pelikenttaLukija.lueSeuraavaMaaAlue() ) {
                
                KappaleMaaAlue lisattavaMaaAlue = pelikenttaLukija.kerroMaaAlue();
                
                //T‰ss‰ pistet‰‰n maa-alue maailmaan. Jos kappale on liian iso maailmaan,
                //heitt‰‰ metodi poikkeuksen.
                this.maailma.lisaaKappale( pelikenttaLukija.kerroMaaAlue(),
                        				   pelikenttaLukija.kerroMaaAlueenSijaintiX(),
                        				   pelikenttaLukija.kerroMaaAlueenSijaintiY() );
                
            }
            
            //Suljetaan tiedoston lukeminen.
            pelikenttaLukija.sulje();
            
        }
        catch ( IOException virhe ) {
            
            throw new Exception( "Tiedostoa \"" + pelikenttaTiedosto + "\" ei pystytty lukemaan." );
            
        }
        catch ( Exception virhe ) {
            
            throw new Exception( "Virhe tiedoston \"" + pelikenttaTiedosto + "\" luvussa: " + virhe.getMessage() );
            
        }
        catch ( OutOfMemoryError virhe ) {
            
            throw new Exception( "Virhe tiedoston \"" + pelikenttaTiedosto + "\" luvussa: Liian suuria kappaleita, muisti ei riit‰." );
            
        }

        return this.maailma;
        
    }
    
    /**
     * Metodi luo annetulle maa-alueelle annetun m‰‰r‰n lapsi maa-alueita. N‰iden
     * lapsimaa-alueiden 1.polttopiste on ‰idin 2.polttopisteess‰.
     * @param aitiMaa ƒitimaa-alue tai null, jos halutaan luoda uusi maa-aluesysteemi.
     * @param maara Lapsimaa-alueiden m‰‰r‰. Jos ‰itimaa-alue on null, k‰sitell‰‰n
     * maara ykkˆsen‰. Jos maara < 0 --> maara = 0.
     */
    private void luoLapsiMaaAlueet( KappaleMaaAlue aitiMaa, int maara ) {
        
        //Korjataan virheelliset m‰‰r‰t.
        if ( maara < 0 )
            maara = 0;
        
        //ƒidin alue.
        AlueEllipsi aitiMaaEllipsi = null;
        
        if ( aitiMaa != null )
            aitiMaaEllipsi = ( AlueEllipsi )aitiMaa.kerroAlue();
        else
            maara = 1;
        
        //Luodaan n‰ille lapsille taulukko.
        KappaleMaaAlue[] sisarusMaa = new KappaleMaaAlue[maara];

        for ( int ind = 0; ind < maara; ind++ ) {
            
            //Tˆrm‰ykset ‰itiin tai sisaruksiin eiv‰t haittaa.
            this.asetaSukulaistenTormattavyys( aitiMaa, sisarusMaa, ind, false );
            
            boolean laitonAlue = true;
            AlueEllipsi testiAlue = null;
            
            //Yritet‰‰n muutamia kertoja luoda ellipsi, mutta koska se ei v‰ltt‰m‰tt‰
            //ole edes mahdollista kaikkialla, lopetetaan yritt‰minen muutaman kerran
            //j‰lkeen.
            for ( int jnd = 0; laitonAlue && jnd < 10; jnd++ ) {
                
                //Luodaan ellipsi testausta varten.
                testiAlue = this.generoiTestiEllipsi( aitiMaaEllipsi, maara, ind );
                
                if ( testiAlue != null )
                    laitonAlue = this.maailma.onLaitonAlue( testiAlue );
                
            }
            
            //Muut eiv‰t saa n‰ihin tˆrm‰ill‰!
            this.asetaSukulaistenTormattavyys( aitiMaa, sisarusMaa, ind, true );

            if ( !laitonAlue ) {
                
                //Luodaan maa-alue. Testiellipsin halkaisijaa t‰ytyy pienent‰‰ vakiolla.
                sisarusMaa[ind] = this.lisaaMaaAlue( testiAlue.kerroPolttopisteen1Sijainti().kerroSijaintiX(),
                        							 testiAlue.kerroPolttopisteen1Sijainti().kerroSijaintiY(),
                        							 testiAlue.kerroPolttopistevektori(),
                        							 testiAlue.kerroHalkaisija() - GeneroijaPelikentta.TESTIELLIPSIN_ISOMMUUS );
                
                //Luodaan lapset, jos maa-alueen lis‰‰minen onnistui. Luodaan lapsia
                //satunnaisesti todenn‰kˆisyyksill‰: 0: 0.3, 1: 0.5, 2: 0.2.
                if ( sisarusMaa[ind] != null )
                    this.luoLapsiMaaAlueet( sisarusMaa[ind], ( int )( 2.0 * Math.random() + 0.4 ) );
                
            }
            
        }
        
    }

    /**
     * Lis‰‰ annettujen tietojen perusteella maa-alueen maailmaan. Jos tietojen
     * perusteella ei kyetty luomaan ellipsi‰, palautetaan null.
     * @param sijaintiX 1.polttopisteen x-koordinaatti.
     * @param sijaintiY 1.polttopisteen y-koordinaatti.
     * @param polttopistevektori Vektori polttopisteest‰ 1 polttopisteeseen 2.
     * @param halkaisija Ellipsin halkaisija.
     * @return Annettujen tietojen perusteella luotu maa-alue tai null, jos
     * luominen ep‰onnistui.
     */
    private KappaleMaaAlue lisaaMaaAlue( double sijaintiX, double sijaintiY, Vektori polttopistevektori, double halkaisija ) {
        
        try { 
            
            KappaleMaaAlue uusiMaaAlue = new KappaleMaaAlue( null, polttopistevektori, halkaisija );
            uusiMaaAlue.asetaMaailmaan( this.maailma, sijaintiX, sijaintiY );
            
            return uusiMaaAlue;
            
        }
        catch ( Exception virhe ) {
            
            return null;
            
        }
        
    }
    
    /**
     * Generoi lapsiellipsin annettujen tietojen perusteella. Lapsiellipsin koko
     * on testikokoinen, eli sen halkaisija on hieman lopullisen maa-alueen halkaisijaa
     * suurempi.
     * @param aitiMaaEllipsi ƒitiellipsi.
     * @param lastenMaara Lapsiellipsien m‰‰r‰ - pit‰‰ sis‰ll‰‰n t‰m‰n luotavan ellipsin.
     * @param omaIndeksi Oma indeksi lapsiperheess‰, so. luku 0 - lastenMaara-1.
     * @return Luodun lapsiellipsin testiellipsi.
     */
    private AlueEllipsi generoiTestiEllipsi( AlueEllipsi aitiMaaEllipsi, int lastenMaara, int omaIndeksi ) {
        
        try {
            
            //Generoidaan ellipsin tiedot.
            int akselinPituus = this.generoiAkselinPituus();
            double suunta = this.generoiAkselinSuunta( aitiMaaEllipsi, lastenMaara, omaIndeksi );
            double paksuus = this.generoiPaksuus();
            Vektori akseli = new Vektori( Math.cos( suunta ) * akselinPituus,
                    	  				  Math.sin( suunta ) * akselinPituus );

            //Luodaan testialue.
            AlueEllipsi testiAlue = new AlueEllipsi( akseli, akselinPituus + paksuus + GeneroijaPelikentta.TESTIELLIPSIN_ISOMMUUS );
            
            Koordinaatit sijainti;
            
            //Selvitet‰‰n 1. polttopisteen sijainti.
            if ( aitiMaaEllipsi != null ) {
                
                sijainti = aitiMaaEllipsi.kerroPolttopisteen2Sijainti();
                
            }
            else {
                
                sijainti = new Koordinaatit( this.maailma.kerroKoordinaatisto(),
                        					 Math.random() * this.maailma.kerroKoordinaatisto().kerroLeveys(),
                        					 Math.random() * this.maailma.kerroKoordinaatisto().kerroKorkeus() );
                
            }
            
            testiAlue.asetaPolttopisteenSijainti( sijainti );
            return testiAlue;
            
        }
        catch ( Exception virhe ) {
            
            return null;
            
        }
        
    }
    
    /**
     * Generoi satunnaisesti akselin pituuden joltakin v‰lilt‰. T‰m‰ v‰li on
     * 150-650.
     * @return Akselin pituus.
     */
    private int generoiAkselinPituus() {

        //Ei tarvitse tehd‰ koon suhteen tarkasteluja, koska Avaruus.onLaitonAlue
        //tekee sen tarkastelun valmiille ellipsille.
        return ( int )( 150 + Math.random() * 500 );
        
    }
    
    /**
     * Generoi akselin suunnan satunnaisesti tiettyjen s‰‰ntˆjen mukaan. Kaikkien perheen
     * ellipsien akselien v‰lill‰ on tietty turvav‰li. Se on esim. PI/3. T‰t‰ l‰hemp‰n‰
     * akselien kulmat eiv‰t ole toisiaan, ellei ole pakko (lapsia niin paljon). Lapsiellipsit
     * saavat jakaa j‰ljelle j‰‰v‰n alueen sektoreiksi, joilta arpovat itselleen kulman.
     * @param aitiMaaEllipsi Perheen ‰itiellipsi.
     * @param lastenMaara Perheen lapsiellipsien m‰‰r‰.
     * @param omaIndeksi Sen lapsen, jolle akseli generoidaan, indeksi (0 - lapsien m‰‰r‰-1).
     * @return Generoitu akselin suunta.
     * @throws Exception Poikkeus kertoo, ett‰ lasten m‰‰r‰ tai oma indeksi on virheellinen.
     */
    private double generoiAkselinSuunta( AlueEllipsi aitiMaaEllipsi, int lastenMaara, int omaIndeksi ) throws Exception {
        
        if ( lastenMaara < 1 )
            throw new Exception( "Lapsia t‰ytyy olla v‰hint‰‰n yksi, koska akseli kuuluu lapselle." );

        if ( omaIndeksi < 0 || omaIndeksi >= lastenMaara )
            throw new Exception( "Lapsiellipsin oma indeksi virheellinen." );
        
        //Perheen akselien turvav‰li.
        double vali = Math.PI / 3;
        
        //Turvav‰lien m‰‰r‰.
        int valienMaara = lastenMaara;
        double aidinSuunta = 0;
        
        if ( aitiMaaEllipsi != null ) {
            
            aidinSuunta = aitiMaaEllipsi.kerroPolttopistevektori().kerroKulma() - Math.PI;
            valienMaara++;
            
        }
        
        //Yksin‰‰n ei tarvitse mit‰‰n turvav‰lej‰ pit‰‰.
        if ( valienMaara == 1 )
            valienMaara = 0;
        
        //T‰m‰n akselin kulman satunnaisvaihteluv‰li.
        //T‰m‰ on siis jokaisen lapsen akselin sektorin suuruus.
        double vaihteluKulma = ( 2 * Math.PI - valienMaara * vali ) / lastenMaara;
       
        //Korjaus, jos vali on liian iso n‰in monelle sisarukselle. Nyt lapset
        //astuvat turvav‰lien p‰‰lle, mutta se on ainoa keino saada lapset mahtumaan.
        if ( vaihteluKulma < 0 )
            vaihteluKulma = 0;
        
        //Oma kulma omasta sektorista.
        double omaKulma = Math.random() * vaihteluKulma; 

        //Lis‰t‰‰n satunnaiskulmaan oman sektorin alkukulma.
        return aidinSuunta + omaIndeksi * vaihteluKulma + ( omaIndeksi + 1 ) * vali + omaKulma;
        
    }
    
    /**
     * Generoi satunnaisesti ellipsin paksuuden v‰lilt‰ 60-100
     * @return Ellipsin paksuus.
     */
    private double generoiPaksuus() {
        
        return Math.random() * 40 + 60;
        
    }
    
    /**
     * Asettaa ‰idin ja itse‰‰n indeksilt‰‰n pienempien sisarusten tˆrm‰tt‰vyyden annetuksi.
     * Metodi tekee null tarkastelut. Jos annettu <CODE>omaIndeksi</CODE> on virheellinen
     * ei kenenk‰‰n sisaruksen tˆrm‰tt‰vyytt‰ aseteta.
     * @param aitiMaa ƒitimaa-alue.
     * @param sisarusMaa Sisarusmaa-alueet.
     * @param omaIndeksi Oma indeksi sisaruskatraassa.
     * @param tormattavissa Totuusarvo kertoo, ovatko maa-alueet tˆrm‰tt‰viss‰.
     */
    private void asetaSukulaistenTormattavyys( KappaleMaaAlue aitiMaa, KappaleMaaAlue[] sisarusMaa, int omaIndeksi, boolean tormattavissa ) {
        
        if ( aitiMaa != null )
            aitiMaa.asetaTormattavyys( tormattavissa );
        
        if ( sisarusMaa == null || omaIndeksi < 0 || omaIndeksi >= sisarusMaa.length )
            return;
        
        for ( int jnd = 0; jnd < omaIndeksi; jnd++ ) {
            
            if ( sisarusMaa[jnd] != null )
                sisarusMaa[jnd].asetaTormattavyys( tormattavissa );
            
        }
       
    }
    
}
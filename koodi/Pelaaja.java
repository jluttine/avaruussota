
/**
 * 
 * Luokka <CODE>Pelaaja</CODE> kuvaa pelaajaa, jolla on nimi, alus, mahdollinen teko‰ly
 * sek‰ peli, jossa pelaaja on. Pelaaja tiet‰‰ tappamansa pelaajat. 
 *
 * @author Jaakko Luttinen
 *
 */
public class Pelaaja {
    
    /**
     * Pelaajan nimen maksimipituus.
     */
    public static final int NIMEN_MAKSIMIPITUUS = 12;
    
    /**
     * Pelaajan nimi.
     */
    private String nimi;
    
    /**
     * Pelaajan pisteet peliss‰.
     */
    private int pisteet;
    
    /**
     * Pelaajan alus.
     */
    private KappalePelaajanAlus alus;
    
    /**
     * Peli, jossa pelaaja on.
     */
    private AvaruussotaPeli peli;
    
    /**
     * Pelaajan teko‰ly tai null, jos sit‰ ei ole.
     */
    private Tekoaly tekoaly;
    
    /**
     * Taulukko pelaajan tappamista pelaajista.
     */
    private Pelaaja[] tapetutPelaajat;
    
    /**
     * Taulukko siit‰, ket‰ pelaaja on tappanut montakin kertaa. Indeksi
     * vastaa <CODE>tapetutPelaajat</CODE> taulukon indeksi‰.
     */
    private int[] pelaajanTappokerrat;
    
    /**
     * Luo uuden pelaajan, jolla on annettu nimi. 
     * @param nimi Pelaajan nimi. 
     * @throws Exception Poikkeus, joka kertoo, ett‰ annettu nimi oli virheellinen.
     */
    public Pelaaja( String nimi ) throws Exception {

        this.asetaNimi( nimi );
        this.peli = null;
        this.tekoaly = null;
        
    }
    
    /**
     * Kertoo pelaajan nimen.
     * @return Pelaajan nimi.
     */
    public String kerroNimi() {
        
        return this.nimi;
        
    }
    
    /**
     * Kertoo pelaajan aluksen.
     * @return Pelaajan alus.
     */
    public KappalePelaajanAlus kerroAlus() {
        
        return this.alus;
        
    }
    
    /**
     * Kertoo pelaajan teko‰lyn.
     * @return Pelaajan teko‰ly.
     */
    public Tekoaly kerroTekoaly() {
        
        return this.tekoaly;
        
    }
    
    /**
     * Kertoo pelin, jossa pelaaja on.
     * @return Peli, jossa pelaaja on.
     */
    public AvaruussotaPeli kerroPeli() {
        
        return this.peli;
        
    }
    
    /**
     * Kertoo pelaajan pisteet.
     * @return Pelaajan pisteet.
     */
    public int kerroPisteet() {
        
        return this.pisteet;
        
    }
    
    /**
     * Kertoo, kuinka monta kertaa pelaaja on tappanut annetun pelaajan.
     * @param tapettuPelaaja Pelaaja, jota ollaan tapettu.
     * @return Kuinka monta kertaa pelaaja on tappanut annetun pelaajan.
     */
    public int kerroTapot( Pelaaja tapettuPelaaja ) {
        
        if ( this.tapetutPelaajat == null )
            return 0;
        
        for ( int ind = 0; ind < this.tapetutPelaajat.length; ind++ ) {
            
            if ( this.tapetutPelaajat[ind] == tapettuPelaaja )
                return this.pelaajanTappokerrat[ind];
            
        }
        
        return 0;
        
    }
    
    /**
     * Asettaa pelaajan teko‰lyn. Jos annettu teko‰ly on null tai pelaajalla on jo kyseinen
     * teko‰ly, ei lis‰yst‰ suoriteta. Poistaa aiemman teko‰lyn. Asettaa teko‰lyn pelaajaksi
     * itsens‰.
     */
    public void asetaTekoaly( Tekoaly tekoaly ) {

        if ( tekoaly == null || this.tekoaly == tekoaly )
            return;
        
        this.poistaTekoaly();
        
        this.tekoaly = tekoaly;
        this.tekoaly.asetaPelaaja( this );
        
    }
    
    /**
     * Asettaa pelaajan teko‰lyksi null. Poistaa pelaajan teko‰lylt‰.
     */
    public void poistaTekoaly() {

        if ( this.tekoaly != null ) {
            
            Tekoaly vanhaTekoaly = this.tekoaly;
            this.tekoaly = null;
            vanhaTekoaly.poistaPelaaja();
            
        }
        
    }

    /**
     * Asettaa pelaajan peliksi <CODE>pelin</CODE>. Lis‰‰ pelaajan peliin. Jos
     * pelaaja on jo kyseisess‰ peliss‰ tai annettu peli on null, ei metodi
     * tee mit‰‰n.
     * @param peli Peli, johon pelaaja halutaan lis‰t‰. 
     * @throws Exception Poikkeus, jos pelaajan lis‰‰misess‰ peliin ilmeni ongelmia.
     */
    public void asetaPeliin( AvaruussotaPeli peli ) throws Exception {
        
        if ( peli == null || this.peli == peli )
            return;
       
        this.poistaPelista();
        
        this.peli = peli;
        this.peli.lisaaPelaaja( this );
        
    }
    
    /**
     * Poistaa pelaajan aluksen pelimaailmasta. Asettaa peliksi null ja poistaa pelaajan
     * pelist‰, jossa oli.
     */
    public void poistaPelista() {

        if ( this.alus != null )
            this.alus.poistaMaailmasta();
       
        if ( this.peli != null ) {
            
            AvaruussotaPeli vanhaPeli = this.peli;
            this.peli = null;
            vanhaPeli.poistaPelaaja( this );
            
        }
        
    }

    /**
     * Asetaa aluksekseen annetun aluksen ja asettaa itsens‰ annetun aluksen rattiin.
     * Jos pelaajalla on jo kyseinen alus tai annettu alus on null, ei metodi tee mit‰‰n.
     * @param uusiAlus Pelaajan alus.
     */
    public void asetaAlus( KappalePelaajanAlus uusiAlus ) {
        
        if ( this.alus == uusiAlus || uusiAlus == null )
            return;
        
        this.poistaAlus();
        
        this.alus = uusiAlus;
        this.alus.asetaPelaaja( this );
        
    }
    
    /**
     * Poistaa pelaajalta aluksensa ja poistaa pelaajan aluksen ratista. 
     */
    public void poistaAlus() {
        
        if ( this.alus != null ) {
            
            KappalePelaajanAlus vanhaAlus = this.alus;
            this.alus = null;
            vanhaAlus.poistaPelaaja();
            
        }
        
    }
    
    /**
     * Asettaa pelaajan nimen. Jos pelaajan nimen pituus on virheellinen, se on null tai
     * peliss‰, jossa pelaaja on, on jo entuudestaan joku sen niminen pelaaja, heitet‰‰n
     * poikkeus <CODE>Exception</CODE>.
     * @param nimi Pelaajan nimi.
     * @throws Exception Poikkeus, jos nimi on null, virheellisen pituinen tai pelaajan peliss‰
     * on jo annetun niminen pelaaja.
     */
    public void asetaNimi( String nimi ) throws Exception {
       
        //Nimen pituuden tarkastelu.
        if ( nimi == null || nimi.length() < 1 || nimi.length() > Pelaaja.NIMEN_MAKSIMIPITUUS ) {
            
            throw new Exception( "Pelaajaa ei voida luoda. Nimen pituuden t‰ytyy olla 1-" +
                    			 Pelaaja.NIMEN_MAKSIMIPITUUS + " merkki‰." );
        
        }
        
        if ( this.nimi != null ) {
            
            //Tehd‰‰n t‰m‰ tarkastus heti, jottei heitet‰ poikkeusta turhaan.
            if ( this.nimi.equals( nimi ) )
                return;
            
            //Onko peliss‰ jo t‰m‰n niminen henkilˆ??
            if ( this.peli != null && this.peli.onPelaaja( nimi ) ) {
                
                throw new Exception( "Pelaajan nime‰ ei voida muuttaa. Peliss‰ on jo \"" +
                        			 nimi + "\"-niminen pelaaja." );
                
            }
            
        }
        
        this.nimi = nimi;
        
    }

    /**
     * Nollaa pelaajan pisteet ja tapot.
     */
    public void nollaaPisteet() {
        
        this.tapetutPelaajat = null;
        this.pelaajanTappokerrat = null;
        this.pisteet = 0;
        
    }
    
    /**
     * Muuttaa pelaajan pisteit‰. Jos <CODE>tapettuPelaaja</CODE> ei ole null,
     * lis‰t‰‰n tappo pelaajan taulukoihin.
     * @param tapettuPelaaja Pelaajan tappama pelaaja. Voi olla null.
     * @param muutos Pisteisiin teht‰v‰ muutos.
     */
    public void muutaPisteet( Pelaaja tapettuPelaaja, int muutos ) {
        
        this.lisaaTappo( tapettuPelaaja );
        
        this.pisteet += muutos;
        
    }

    /**
     * Lis‰‰ tapon. Jos pelaajaa ei ole ennen tapettu, lis‰‰ <CODE>tapetutPelaajat</CODE>
     * taulukkoon annetun pelaajan. Kasvattaa samalla indeksill‰ lˆytyv‰‰ <CODE>pelaajanTappokerrat</CODE>
     * taulukon alkiota yhdell‰.
     * @param tapettuPelaaja Pelaaja, joka tapettiin.
     */
    private void lisaaTappo( Pelaaja tapettuPelaaja ) {
        
        if ( tapettuPelaaja == null )
            return;
        
        if ( this.tapetutPelaajat == null ) {
            
            this.tapetutPelaajat = new Pelaaja[1];
            this.pelaajanTappokerrat = new int[1];
            this.tapetutPelaajat[0] = tapettuPelaaja;
            this.pelaajanTappokerrat[0] = 1;
            return;
            
        }
        
        //Etsit‰‰n lˆytyykˆ tapettua pelaajaa entuudestaan.
        for ( int ind = 0; ind < this.tapetutPelaajat.length; ind++ ) {
            
            if ( tapettuPelaaja == this.tapetutPelaajat[ind] ) {
                
                //Lis‰t‰‰n yksi tappo t‰t‰ pelaajaa kohden.
                this.pelaajanTappokerrat[ind]++;
                return;
                
            }
            
        }
        
        //Ei lˆytynyt. Kasvatetaan taulukoita.
        int tapettujaPelaajia = this.tapetutPelaajat.length;
        Pelaaja[] pelaajaAputaulukko = this.tapetutPelaajat;
        int[] tappoAputaulukko = this.pelaajanTappokerrat;
        this.tapetutPelaajat = new Pelaaja[++tapettujaPelaajia];
        this.pelaajanTappokerrat = new int[tapettujaPelaajia];
        
        for ( int ind = 0; ind < tapettujaPelaajia - 1; ind++ ) {
            
            this.tapetutPelaajat[ind] = pelaajaAputaulukko[ind];
            this.pelaajanTappokerrat[ind] = tappoAputaulukko[ind];
            
        }
        
        this.tapetutPelaajat[tapettujaPelaajia-1] = tapettuPelaaja;
        this.pelaajanTappokerrat[tapettujaPelaajia-1] = 1;
        
    }
    
}
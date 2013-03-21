
/**
 *
 * Luokka <CODE>TekoalyKohde</CODE> mallintaa teko‰ly‰, joka valitsee itselleen
 * pelaajan ohjaaman aluksen kohteeksi ja pyrkii vain lent‰m‰‰n sit‰ p‰in ampuen,
 * kun kohde on riitt‰v‰n tarkasti haarukassa.
 *  
 * @author Jaakko Luttinen
 *
 */
public class TekoalyKohde extends Tekoaly {
   
    /**
     * Teko‰lyn valitsema kohde.
     */
    private KappalePelaajanAlus kohde;
    
    /**
     * Luo uuden teko‰lyn.
     * @param nimi Teko‰lyn nimi.
     */
    public TekoalyKohde( String nimi ) {
        
        super( nimi );

    }
    
    public Tekoaly kerroKopio() {
        
        return new TekoalyKohde( this.kerroNimi() );
        
    }

    /**
     * Kertoo teko‰lyn m‰‰ritt‰m‰n kohteen.
     * @return Teko‰lyn hyˆkk‰yksen kohde.
     */
    public KappalePelaajanAlus kerroKohde() {
        
        return this.kohde;
        
    }

    /**
     * Kertoo kohteen sijainnin tai null, jos kohdetta ei ole tai sill‰ ei ole sijaintia.
     * @return Kohteen sijainti tai null.
     */
    public Koordinaatit kerroKohteenSijainti() {
        
        if ( this.kohde == null )
            return null;
        
        return this.kohde.kerroSijainti();
        
    }
    
    /**
     * Kertoo l‰himm‰n toisen pelaajan aluksen tai null, jos teko‰lylle ei ole asetettu
     * pelaajaa, pelaajalle ei ole asetettu peli‰ tai teko‰ly ei lˆyd‰ mit‰‰n toista
     * pelaajan alusta.
     * @return L‰hin toisen pelaajan alus tai null.
     */
    protected KappalePelaajanAlus kerroLahinKohde() {
        
        AvaruussotaPeli peli;
        
        if ( this.kerroPelaaja() == null || ( peli = this.kerroPelaaja().kerroPeli() ) == null )
            return null;
        
        KappalePelaajanAlus lahinKohde = null;
        double lahimmanEtaisyys = -1;
        
        for ( int ind = 0; ind < peli.kerroPelaajienMaara(); ind++ ) {
            
            Pelaaja pelaaja = peli.kerroPelaaja( ind );
            
            if ( pelaaja != this.kerroPelaaja() ) {
                
                KappalePelaajanAlus tarkasteltava = pelaaja.kerroAlus();
                
                if ( !tarkasteltava.onTuhoutunut() ) {
                    
                    try {
                        
                        double etaisyys = tarkasteltava.kerroSijainti().kerroEtaisyys( this.kerroPelaaja().kerroAlus().kerroSijainti() );
                        
                        if ( lahimmanEtaisyys < 0 || lahimmanEtaisyys > etaisyys ) {
                            
                            lahimmanEtaisyys = etaisyys;
                            lahinKohde = tarkasteltava;
                            
                        }
                        
                    }
                    catch ( Exception virhe ) {  }
                    
                }
                
            }
            
        }

        return lahinKohde;
        
    }
    
    /**
     * Etsii uuden kohteen.
     */
    protected void etsiUusiKohde() {
        
        this.kohde = this.kerroLahinKohde();
        
    }
    
    /**
     * Kertoo suunnan, johon teko‰lyn pit‰isi alusta ohjata. Haluttu suunta kohteesta
     * annetaan parametrina sen takia, ettei sit‰ tarvitse laskea moneen kertaan.
     * @param haluttuSuuntaKohteesta Suunta kohteelle.
     * @return Haluttu ohjaussuunta.
     */
    protected Vektori kerroHaluttuSuunta( Vektori haluttuSuuntaKohteesta ) {

        return haluttuSuuntaKohteesta;
        
    }
    
    /**
     * Kertoo kohteen paikkavektorin oman aluksen suhteen tai null, jos molemmat alukset
     * eiv‰t sijaitse samassa maailmassa tai aluksia ei ole.
     * @return Kohteen paikkavektori oman aluksen suhteen tai null.
     */
    protected Vektori kerroKohteenPaikkavektori() {
        
        KappalePelaajanAlus omaAlus = this.kerroAlus();
        
        if ( omaAlus == null || this.kohde == null ||
             omaAlus.kerroMaailma() != this.kohde.kerroMaailma() ) {
            
            return null;
            
        }
        
        Koordinaatit omaSijainti = omaAlus.kerroSijainti();
        Koordinaatit kohteenSijainti = this.kohde.kerroSijainti();
        
        if ( omaSijainti == null || kohteenSijainti == null )
            return null;
        
        return new Vektori( omaSijainti.kerroLyhinMatkaX( kohteenSijainti.kerroSijaintiX() ),
                			omaSijainti.kerroLyhinMatkaY( kohteenSijainti.kerroSijaintiY() ) );
        
    }
    
    /**
     * Kertoo paikkavektorin siihen pisteeseen, johon tulisi t‰hd‰t‰.
     * @return Paikkavektori t‰ht‰yspisteeseen.
     */
    protected Vektori kerroKohteenTahtaysPaikkavektori() {
        
        return this.kerroKohteenPaikkavektori();
        
    }

    /**
     * Kertoo suuntavektorin sellaiseen pisteeseen, johon t‰ht‰‰m‰ll‰ kuvittelee
     * osuvansa kohteeseen. T‰t‰ vektoria voi olla skaalattu vertailukelpoiseksi -
     * riippuu aliluokkien toteutuksista.
     * @return Suuntavektori kohteen osumapisteeseen.
     */
    protected Vektori kerroHaluttuSuuntaKohteesta() {

        return this.kerroKohteenTahtaysPaikkavektori();

    }
   
    public void toimi() {
        
        Pelaaja pelaaja = this.kerroPelaaja();
        AvaruussotaPeli peli;
        Avaruus maailma;
        
        //null-testit.
        if ( pelaaja == null || ( peli = pelaaja.kerroPeli() ) == null || ( maailma = peli.kerroMaailma() ) == null )
            return;
        
        //Synkronoidaan, ettei tapahdu ihme muutoksia maailmassa.
        synchronized ( maailma ) {
            
            KappalePelaajanAlus omaAlus = pelaaja.kerroAlus();
            
            //Ei tarvitse mietti‰, jos oma alus ei ole peliss‰.
            if ( omaAlus == null || omaAlus.onTuhoutunut() ) {
                
                this.kohde = null;
                return;
                
            }
            
            //Jos kohde on poissa pelist‰, yritet‰‰n saada uusi.
            if ( this.kohde == null || this.kohde.onTuhoutunut() ||
                 this.kohde.kerroMaailma() != omaAlus.kerroMaailma() ) {
                
                this.etsiUusiKohde();
                
            }

            //Pidet‰‰n kaasu aina pohjassa.
            this.asetaKaasu( true );
            
            //Pidet‰‰n liipaisin vapaana, ellei myˆhemmin toisin p‰‰tet‰.
            this.asetaLiipaisin( false );
            
            Vektori haluttuSuuntaKohteesta = this.kerroHaluttuSuuntaKohteesta();
            Vektori haluttuSuunta = this.kerroHaluttuSuunta( haluttuSuuntaKohteesta );
            
            //Jos kohde on haarukassa, liipaisin pohjaan.
            if ( haluttuSuuntaKohteesta != null ) {
                
                double kulmaKohteelle = Kulma.summa( -omaAlus.kerroNokanSuunta().kerroKulma(), haluttuSuuntaKohteesta.kerroKulma() );
                double ampumarajakulma = Math.PI / 8;
                if ( kulmaKohteelle < ampumarajakulma || kulmaKohteelle > 2 * Math.PI - ampumarajakulma )
                    this.asetaLiipaisin( true );
                
            }
            
            //Asetetaan ohjaus halutun suunnan perusteella. Jos suunta on riitt‰v‰n
            //tarkasti kohdallaan, painetaan suoraan.
            if ( haluttuSuunta != null ) {
                
                double kulma = Kulma.summa( -omaAlus.kerroNokanSuunta().kerroKulma(), haluttuSuunta.kerroKulma() );
                if ( kulma <= Math.PI / 36 || kulma >=  71 * Math.PI / 36 )
                    this.asetaOhjaus( KappaleOhjattava.SUUNTA_SUORAAN );
                else if ( kulma <= Math.PI )
                    this.asetaOhjaus( KappaleOhjattava.SUUNTA_MYOTAPAIVAAN );
                else
                    this.asetaOhjaus( KappaleOhjattava.SUUNTA_VASTAPAIVAAN );
                
            }
            
        }
        
    }
    
}
import java.awt.*;

/**
 * 
 * Luokka <CODE>KappaleMaaAlue</CODE> kuvaa yksitt�ist� maa-alue kappaletta. Kappaletta
 * k�sitell��n ellipsin�, mutta sen ulkon�k� on r�pelletty ellipsi. Maa-alue ei liiku
 * ja se on t�rm�tt�viss�. Kun maa-alue asetetaan maailmaan, sijainti kertoo ellipsin
 * ensimm�isen polttopisteen sijainnin. N�in ollen on helppo luoda maa-alue systeemej�,
 * joilla on yhteinen polttopiste. 
 *
 * @author Jaakko Luttinen
 *
 */
public class KappaleMaaAlue extends Kappale {

    /**
     * Koska ellipsi� r�pellet��n, tehd��n kuvasta hieman "sis�ll� piilev��" ellipsi�
     * suurempi. Vakio kertoo, kuinka paljon kuvan ellipsin halkaisija on suurempi.
     */
    private static final int ROPELLETYN_ELLIPSIN_ISOMMUUS = 7;

    /**
     * Maa-alue yritet��n tehd� hienoksi teksturoimalla se kyseisell� tiedostolla. 
     */
    private static Kuvasarja TEKSTUURI = null;

    /**
     * Kappaleen alue.
     */
    private AlueEllipsi sijaintiAlue;
    
    /**
     * Asettaa kuvan, jolla maa-alueet teksturoidaan. Vaikuttaa vain asetuksen
     * j�lkeen luotaviin maa-alueisiin. <CODE>null</CODE> tarkoittaa, ettei
     * teksturointia haluta. Kuvasarjasta k�ytet��n vain ensimm�ist� framea.
     * @param kuva Kuva, jolla teksturoidaan, tai null.
     */
    public static void asetaTekstuuri( Kuvasarja kuva ) {
        
        KappaleMaaAlue.TEKSTUURI = kuva;
        
    }

    /**
     * Luo uuden maa-alue kappaleen.
     * @param tyyppi Kappaleen merkkijonotunniste. Voi olla null.
     * @param akselivektori Ellipsin akselivektori.
     * @param halkaisija Ellipsin halkaisija.
     * @throws Exception Poikkeus kertoo, ett� annetuissa parametreissa oli virhe.
     */
    public KappaleMaaAlue( String tyyppi, Vektori akselivektori, double halkaisija) throws Exception {

        //Huomaa, ett� kuvasarja luodaan ellipsin, jonka halkaisija on aluetta suurempi, perusteella.
        super( tyyppi, new Kuvasarja( ( int )AlueEllipsi.kerroSuorakaiteenLeveys( AlueEllipsi.kerroIsoakseli( halkaisija ) + KappaleMaaAlue.ROPELLETYN_ELLIPSIN_ISOMMUUS,
                																  AlueEllipsi.kerroPikkuakseli( akselivektori.kerroPituus(), halkaisija ) + KappaleMaaAlue.ROPELLETYN_ELLIPSIN_ISOMMUUS,
                																  akselivektori.kerroKulma() ),
                					  ( int )AlueEllipsi .kerroSuorakaiteenKorkeus( AlueEllipsi.kerroIsoakseli( halkaisija ) + KappaleMaaAlue.ROPELLETYN_ELLIPSIN_ISOMMUUS,
                					          										AlueEllipsi.kerroPikkuakseli( akselivektori.kerroPituus(), halkaisija ) + KappaleMaaAlue.ROPELLETYN_ELLIPSIN_ISOMMUUS,
                					          										akselivektori.kerroKulma() ),
                					  1,
                					  1 ) );

        this.sijaintiAlue = new AlueEllipsi( akselivektori, halkaisija );
        this.asetaTormattavyys( true );
        this.piirraMaaAlue();

    }

    /**
     * Piirt�� maa-alueen, eli r�pelletyn ellipsin. Yritt�� teksturoida maa-alueen. Jos
     * teksturointi ei onnistu, tekee maa-alueesta perusruskean.
     */
    private void piirraMaaAlue() {

        /*
         * Ellipsin parametrisoitu yht�l� (Mathematicalla):
         * x[t_] = a*Cos[t]*Cos[phi] - b*Sin[t]*Sin[phi] + kx;
         * y[t_] = a*Cos[t]*Sin[phi] + b*Sin[t]*Cos[phi] + ky;
         */

        double a = this.sijaintiAlue.kerroIsoakseli();
        double b = this.sijaintiAlue.kerroPikkuakseli();
        double kulma = this.sijaintiAlue.kerroPolttopistevektori().kerroKulma();

        //Tehd��n pisteist� taulukko parametrisen esityksen avulla...
        int pisteita = ( int )Math.sqrt( a * b );
        int[] x = new int[pisteita];
        int[] y = new int[pisteita];

        for ( int ind = 0; ind < pisteita; ind++ ) {

            //T�m� satunnaismuuttuja saa aikaan pient� r�pellyst�.
            double rnd = ( 2 * Math.random() - 1.0 ) * KappaleMaaAlue.ROPELLETYN_ELLIPSIN_ISOMMUUS;
            double t = 2 * Math.PI * ind / pisteita;
            x[ind] = ( int )( ( this.kerroKuva().kerroLeveys() / 2 ) +
                    		  ( ( rnd + a ) * Math.cos( t ) * Math.cos( kulma ) ) -
                    		  ( ( rnd + b ) * Math.sin( t ) * Math.sin( kulma ) ) );
            y[ind] = ( int )( ( this.kerroKuva().kerroKorkeus() / 2 ) +
                    		  ( ( rnd + a ) * Math.cos( t ) * Math.sin( kulma ) ) +
                    		  ( ( rnd + b ) * Math.sin( t ) * Math.cos( kulma ) ) );

        }

        Graphics2D piirtokonteksti = this.kerroKuva().kerroGraphics();

        if ( KappaleMaaAlue.TEKSTUURI != null ) {
            
            //Teksturoidaan maa-alue.

            piirtokonteksti.setColor( new Color( 255, 255, 255 ) );
            piirtokonteksti.fillPolygon( x, y, pisteita );
            piirtokonteksti.setXORMode( new Color( 255, 255, 255 ) );


            for ( int ind = 0; ind <= this.kerroKuva().kerroLeveys() / KappaleMaaAlue.TEKSTUURI.kerroLeveys(); ind++ ) {

                for ( int jnd = 0; jnd <= this.kerroKuva().kerroKorkeus() / KappaleMaaAlue.TEKSTUURI.kerroKorkeus(); jnd++ )
                    
                    KappaleMaaAlue.TEKSTUURI.piirraFrame( piirtokonteksti,
                            							  0,
                            							  ind * KappaleMaaAlue.TEKSTUURI.kerroLeveys(), 
                            							  jnd * KappaleMaaAlue.TEKSTUURI.kerroKorkeus());

            }

        } 
        else {

            //Jos tuo hienompi kuvan muodostus ep�onnistuu, tehd��n perusruskea.
            piirtokonteksti.setColor(new Color(70, 30, 0));
            piirtokonteksti.fillPolygon(x, y, pisteita);

        }

    }

    public Alue kerroAlue() {

        return this.sijaintiAlue;

    }

    public void asetaMaailmaan( Avaruus maailma, double sijaintiX, double sijaintiY ) throws Exception {

        //Asettaa sijainnin ensimm�isen polttopisteen perusteella.
        //Tuolla kokonaislukumuunnoksella saadaan kaikki maa-alueet synkronoitua
        //ja maa-aluesysteemitkin n�ytt�v�t yhten�isilt�. Est�� maa-alueiden v�rin�n.
        super.asetaMaailmaan( maailma,
                			 ( int )( sijaintiX + this.sijaintiAlue.kerroPolttopistevektori().kerroX() / 2 ),
                			 ( int )( sijaintiY + this.sijaintiAlue.kerroPolttopistevektori().kerroY() / 2 ) );

    }

    public void toimi(int aika) {

        return;

    }

}
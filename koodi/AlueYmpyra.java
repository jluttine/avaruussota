/**
 *
 *  
 *  Luokka <CODE>AlueYmpyra</CODE> kuvaa avaruuden ympyr�n muotoista aluetta.
 *  Ympyr� on ellipsi, jonka polttopistevektori on nollavektori ja s�de on
 *  puolet halkaisijasta.
 * 
 *  @author Jaakko Luttinen
 * 
 * 
 */

public class AlueYmpyra extends AlueEllipsi {

    /**
     * Luo uuden ympyr�n annetulla s�teell�.
     * @param sade Ympyr�n s�de.
     * @throws Exception Poikkeus kertoo, ett� alueen s�de on virheellinen (negatiivinen).
     */
    public AlueYmpyra( double sade ) throws Exception {

        super( new Vektori( 0, 0 ), sade * 2 );

    }

    /**
     * Luo uuden ympyr�n annetun ympyr�n perusteella.
     * @param kopio Ympyr�, jonka perusteella uusi ympyr� luodaan.
     */
    public AlueYmpyra( AlueYmpyra kopio ) {

        super( kopio );

    }

    /**
     * Kertoo ympyr�n s�teen.
     * @return Ympyr�n s�de.
     */
    public double kerroSade() {

        return this.kerroHalkaisija() / 2;

    }

    protected int leikkaustesti( Alue verrattava ) {

        if ( verrattava instanceof AlueYmpyra ) {

            if ( AlueYmpyra.leikkaavatYmpyraJaYmpyra( this, ( AlueYmpyra )verrattava ) )
                return 1;

            return 0;

        }

        if ( verrattava instanceof AlueEllipsi ) {

            if ( AlueYmpyra.leikkaavatYmpyraJaEllipsi( this, ( AlueEllipsi ) verrattava ) )
                return 1;

            return 0;

        }

        //Ei ole kehitelty vertailua n�ille kahdelle..
        return super.leikkaustesti(verrattava);

    }

    /**
     * Tutkii leikkaavatko kaksi ympyr�� toisensa. Tutkii ensin leikkaavatko ympyr�it�
     * rajaavat suorakaiteet. Ympyr�t leikkaavat jos niiden keskipisteiden et�isyys on
     * pienempi kuin niiden s�teiden summa. Huomattavaa on, ett� metodi ei anna v�ltt�m�tt�
     * samaa vastausta kuin jos alueita tarkastaltaisiin ellipsin� ja ympyr�n� tai
     * ellipsin� ja ellipsin�. T�m� on hieman h�mment�v�� olio-ohjelmoinnin kannalta, mutta
     * luokat on toteutettu niin, ett� kunkin yhdistelm�n leikkaus on aina yksik�sitteinen
     * eik� ole riippuvainen kumman leikkaustestin osapuolen leikkaustesti-metodia
     * kutsutaan. 
     * @param ympyra1 Leikkaustestin toinen osapuoli.
     * @param ympyra2 Leikkaustestin toinen osapuoli.
     * @return Totuusarvo, joka kertoo, leikkaavatko alueet.
     */
    public static boolean leikkaavatYmpyraJaYmpyra( AlueYmpyra ympyra1, AlueYmpyra ympyra2 ) {

        if ( !Alue.leikkaavatSuorakulmioJaSuorakulmio( ympyra1, ympyra2 ) )
            return false;

        try {

            double etaisyys = ympyra1.kerroSijainti().kerroEtaisyys( ympyra2.kerroSijainti() );

            return etaisyys < ( ympyra1.kerroSade() + ympyra2.kerroSade() );

        } catch ( Exception virhe ) {

            return false;

        }

    }

    /**
     * Kertoo leikkaavatko annetun ympyr�n ja ellipsin alueet. Tarkastelu ei ole
     * matemaattisesti aivan tarkka, mutta se antaa (varsinkin pienill� ympyr�ill�)
     * kohtalaisen tarkan vastauksen. Huomioitavaa on, ett� k�sitelt�ess�
     * molempia ellipsein� ja kutsuttaessa metodia leikkaavatEllipsiJaEllipsi,
     * vastaus voi olla eri. T�m� johtuu siit�, ett� kahden ellipsin v�list� leikkausta
     * on vaikea nopeasti arvioida. Se ei anna riitt�v�n tarkkaa vastausta, joten
     * ympyr�n ja ellipsin tapaukseen on kehitetty eri metodi. T�m� aiheuttaa sen,
     * ett� ellipsi luokan leikkaustesti-metodiin on teht�v� tarkastus, jos
     * toinen alue onkin ympyr�. T�m� on hieman h�mment�v�� olio-ohjelmointia
     * ajatellen, mutta t�ss� tilanteessa tarkempi vastaus on ollut t�rke�.
     * Leikkaustesti tehd��n tutkimalla leikkausta sellaisen ellipsin suhteen,
     * jonka polttopiste on l�hinn�. Ympyr�n l�hin piste t�t� ellipsi� kohtaan
     * selvitet��n et�normaalin avulla ja jos t�m�n pisteen yhteenlaskettu
     * et�isyys ellipsin polttopisteist� on alle ellipsin halkaisijan, ympyr�
     * ja ellipsi leikkaavat.
     * @param ympyra Ympyr�n muotoinen alue.
     * @param ellipsi Ellipsin muotoinen alue.
     * @return Totuusarvo kertoo, leikkaavatko ympyr�n ja ellipsin alueet.
     */
    public static boolean leikkaavatYmpyraJaEllipsi( AlueYmpyra ympyra, AlueEllipsi ellipsi ) {

        if ( !Alue.leikkaavatSuorakulmioJaSuorakulmio( ympyra, ellipsi ) )
            return false;
        
        Koordinaatit polttopisteen1Sijainti = ellipsi.kerroPolttopisteen1Sijainti();
        Koordinaatit polttopisteen2Sijainti = ellipsi.kerroPolttopisteen2Sijainti();
        Koordinaatit sijainti = ympyra.kerroSijainti();

        //Vektorit ympyr�n keskipisteest� ellipsin polttopisteisiin.
        Vektori polttopiste1 = new Vektori( sijainti.kerroLyhinMatkaX( polttopisteen1Sijainti.kerroSijaintiX() ),
                							sijainti.kerroLyhinMatkaY( polttopisteen1Sijainti.kerroSijaintiY() ) );

        Vektori polttopiste2 = new Vektori( sijainti.kerroLyhinMatkaX( polttopisteen2Sijainti.kerroSijaintiX() ),
                							sijainti.kerroLyhinMatkaY( polttopisteen2Sijainti.kerroSijaintiY() ) );

        //Muodostetaan toinen polttopiste l�hemp�n� olleen polttopisteen perusteella.
        if ( polttopiste1.kerroPituus() < polttopiste2.kerroPituus() ) {

            polttopiste2 = Vektori.kerroSumma( polttopiste1, ellipsi.kerroPolttopistevektori() );

        }
        else {

            //Polttopistevektori t�ytyy k��nt�� toisinp�in.
            polttopiste1 = Vektori.kerroSumma( polttopiste2,
                    						   Vektori.kerroTulo( ellipsi.kerroPolttopistevektori(), -1 ) );

        }

        //T�m� et�normaali on nyt ellipsi� kohden!
        Vektori etanormaalivektori = Vektori.kerroSumma( polttopiste1.kerroYksikkovektori(), polttopiste2.kerroYksikkovektori() );

        //Skaalataan ympyr�n s�teen mittaiseksi ja vastakkaissuuntaiseksi.
        etanormaalivektori.skaalaa( -ympyra.kerroSade() / etanormaalivektori.kerroPituus() );

        //N�in saadaan vektorit ympyr�n ellipsi� l�himp�n� olevasta pisteest� ellipsin
        //polttopisteisiin.
        polttopiste1.lisaa( etanormaalivektori );
        polttopiste2.lisaa( etanormaalivektori );

        return ( polttopiste1.kerroPituus() + polttopiste2.kerroPituus() ) < ellipsi.kerroHalkaisija();

    }

}
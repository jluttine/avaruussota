/**
 *
 *  
 *  Luokka <CODE>AlueYmpyra</CODE> kuvaa avaruuden ympyrän muotoista aluetta.
 *  Ympyrä on ellipsi, jonka polttopistevektori on nollavektori ja säde on
 *  puolet halkaisijasta.
 * 
 *  @author Jaakko Luttinen
 * 
 * 
 */

public class AlueYmpyra extends AlueEllipsi {

    /**
     * Luo uuden ympyrän annetulla säteellä.
     * @param sade Ympyrän säde.
     * @throws Exception Poikkeus kertoo, että alueen säde on virheellinen (negatiivinen).
     */
    public AlueYmpyra( double sade ) throws Exception {

        super( new Vektori( 0, 0 ), sade * 2 );

    }

    /**
     * Luo uuden ympyrän annetun ympyrän perusteella.
     * @param kopio Ympyrä, jonka perusteella uusi ympyrä luodaan.
     */
    public AlueYmpyra( AlueYmpyra kopio ) {

        super( kopio );

    }

    /**
     * Kertoo ympyrän säteen.
     * @return Ympyrän säde.
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

        //Ei ole kehitelty vertailua näille kahdelle..
        return super.leikkaustesti(verrattava);

    }

    /**
     * Tutkii leikkaavatko kaksi ympyrää toisensa. Tutkii ensin leikkaavatko ympyröitä
     * rajaavat suorakaiteet. Ympyrät leikkaavat jos niiden keskipisteiden etäisyys on
     * pienempi kuin niiden säteiden summa. Huomattavaa on, että metodi ei anna välttämättä
     * samaa vastausta kuin jos alueita tarkastaltaisiin ellipsinä ja ympyränä tai
     * ellipsinä ja ellipsinä. Tämä on hieman hämmentävää olio-ohjelmoinnin kannalta, mutta
     * luokat on toteutettu niin, että kunkin yhdistelmän leikkaus on aina yksikäsitteinen
     * eikä ole riippuvainen kumman leikkaustestin osapuolen leikkaustesti-metodia
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
     * Kertoo leikkaavatko annetun ympyrän ja ellipsin alueet. Tarkastelu ei ole
     * matemaattisesti aivan tarkka, mutta se antaa (varsinkin pienillä ympyröillä)
     * kohtalaisen tarkan vastauksen. Huomioitavaa on, että käsiteltäessä
     * molempia ellipseinä ja kutsuttaessa metodia leikkaavatEllipsiJaEllipsi,
     * vastaus voi olla eri. Tämä johtuu siitä, että kahden ellipsin välistä leikkausta
     * on vaikea nopeasti arvioida. Se ei anna riittävän tarkkaa vastausta, joten
     * ympyrän ja ellipsin tapaukseen on kehitetty eri metodi. Tämä aiheuttaa sen,
     * että ellipsi luokan leikkaustesti-metodiin on tehtävä tarkastus, jos
     * toinen alue onkin ympyrä. Tämä on hieman hämmentävää olio-ohjelmointia
     * ajatellen, mutta tässä tilanteessa tarkempi vastaus on ollut tärkeä.
     * Leikkaustesti tehdään tutkimalla leikkausta sellaisen ellipsin suhteen,
     * jonka polttopiste on lähinnä. Ympyrän lähin piste tätä ellipsiä kohtaan
     * selvitetään etänormaalin avulla ja jos tämän pisteen yhteenlaskettu
     * etäisyys ellipsin polttopisteistä on alle ellipsin halkaisijan, ympyrä
     * ja ellipsi leikkaavat.
     * @param ympyra Ympyrän muotoinen alue.
     * @param ellipsi Ellipsin muotoinen alue.
     * @return Totuusarvo kertoo, leikkaavatko ympyrän ja ellipsin alueet.
     */
    public static boolean leikkaavatYmpyraJaEllipsi( AlueYmpyra ympyra, AlueEllipsi ellipsi ) {

        if ( !Alue.leikkaavatSuorakulmioJaSuorakulmio( ympyra, ellipsi ) )
            return false;
        
        Koordinaatit polttopisteen1Sijainti = ellipsi.kerroPolttopisteen1Sijainti();
        Koordinaatit polttopisteen2Sijainti = ellipsi.kerroPolttopisteen2Sijainti();
        Koordinaatit sijainti = ympyra.kerroSijainti();

        //Vektorit ympyrän keskipisteestä ellipsin polttopisteisiin.
        Vektori polttopiste1 = new Vektori( sijainti.kerroLyhinMatkaX( polttopisteen1Sijainti.kerroSijaintiX() ),
                							sijainti.kerroLyhinMatkaY( polttopisteen1Sijainti.kerroSijaintiY() ) );

        Vektori polttopiste2 = new Vektori( sijainti.kerroLyhinMatkaX( polttopisteen2Sijainti.kerroSijaintiX() ),
                							sijainti.kerroLyhinMatkaY( polttopisteen2Sijainti.kerroSijaintiY() ) );

        //Muodostetaan toinen polttopiste lähempänä olleen polttopisteen perusteella.
        if ( polttopiste1.kerroPituus() < polttopiste2.kerroPituus() ) {

            polttopiste2 = Vektori.kerroSumma( polttopiste1, ellipsi.kerroPolttopistevektori() );

        }
        else {

            //Polttopistevektori täytyy kääntää toisinpäin.
            polttopiste1 = Vektori.kerroSumma( polttopiste2,
                    						   Vektori.kerroTulo( ellipsi.kerroPolttopistevektori(), -1 ) );

        }

        //Tämä etänormaali on nyt ellipsiä kohden!
        Vektori etanormaalivektori = Vektori.kerroSumma( polttopiste1.kerroYksikkovektori(), polttopiste2.kerroYksikkovektori() );

        //Skaalataan ympyrän säteen mittaiseksi ja vastakkaissuuntaiseksi.
        etanormaalivektori.skaalaa( -ympyra.kerroSade() / etanormaalivektori.kerroPituus() );

        //Näin saadaan vektorit ympyrän ellipsiä lähimpänä olevasta pisteestä ellipsin
        //polttopisteisiin.
        polttopiste1.lisaa( etanormaalivektori );
        polttopiste2.lisaa( etanormaalivektori );

        return ( polttopiste1.kerroPituus() + polttopiste2.kerroPituus() ) < ellipsi.kerroHalkaisija();

    }

}
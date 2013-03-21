
/**
 * 
 * Luokka <CODE>TekoalyNormaali</CODE> kuvaa sellaista tekoälyä, joka osaa
 * ottaa avaruuden esteitäkin huomioon siten, että pyrkii
 * lentämään poispäin esteistä. Haluttuja suuntia laskiessaan suuntavektorit
 * skaalataan siten, että ne ovat kääntäen verrannollisia ajan, joka
 * osumaan/törmäykseen kestäisi (suunnilleen), neliöön. Näin ollen vektoreista
 * saadaan jotenkin vertailukelpoisia. Skaalausaika kohteen vektorissa
 * on aika, joka ammuksella kuluu lentää kohteen sijaintiin.
 *  
 * @author Jaakko Luttinen
 *
 */
public class TekoalyNormaali extends TekoalyKohde {
   
    /**
     * Luo uuden tekoälyn.
     * @param nimi Tekoälyn nimi.
     */
    public TekoalyNormaali( String nimi ) {
        
        super( nimi );

    }

    public Tekoaly kerroKopio() {
        
        return new TekoalyNormaali( this.kerroNimi() );
        
    }
    
    /**
     * Palauttaa halutuista suunnista kohteesta ja esteista pituudeltaan suuremman.
     * @return Haluttu suunta.
     */
    protected Vektori kerroHaluttuSuunta( Vektori haluttuSuuntaKohteesta ) {
        
        //Selvittää halutun suunnan esteistä.
        Vektori haluttuSuuntaEsteista = this.kerroHaluttuSuuntaEsteista();
        
        //Vektorit ovat suuruusvertailukelpoiset.
        if ( haluttuSuuntaKohteesta == null || haluttuSuuntaKohteesta.kerroPituus() < haluttuSuuntaEsteista.kerroPituus() ) {
            
            //Jos joudutaan väistelemään esteitä, etsitään sitten myös uusi kohde.
            this.etsiUusiKohde();
            return haluttuSuuntaEsteista;
            
        }
        else {
            
            return haluttuSuuntaKohteesta;

       }
        
    }
    
    /**
     * Selvittää kohteen tähtäyspaikkavektorin ja skaalaa sen ajan, joka ammuksella
     * tuohon pisteeseen kestää lentää, neliön käänteisluvulla.
     * @return Haluttu suunta kohteesta -vektori.
     */
    protected Vektori kerroHaluttuSuuntaKohteesta() {
        
        Vektori paikkavektori = this.kerroKohteenTahtaysPaikkavektori();
        KappalePelaajanAlus omaAlus = this.kerroAlus();
        
        if ( paikkavektori == null || omaAlus == null  )
            return null;
            
        double ammuksenNopeus = omaAlus.kerroPaaase().kerroAmmusmalli().kerroLahtonopeus();
        double etaisyys = paikkavektori.kerroPituus();

        if ( etaisyys == 0 )
            return null;
            
        //Ammuksen lentoon kuluvan ajan käänteisluku.
        double kerroin = ammuksenNopeus / etaisyys;
        
        //Skaalataan yksikkövektori kertoimen neliöllä (etaisyyshan on myös vektorin pituus).
        paikkavektori.skaalaa( kerroin * kerroin / etaisyys );
        
        return paikkavektori;
            
    }
    
    /**
     * Laskee halutun suunnan vektorin suhteessa sellaiseen esteeseen, jonka etäisyys ja
     * etänormaali ovat annetut. Oman aluksen nopeuskin on annettu. Jos nopeuden
     * etänormaali-komponentti ei ole aluetta kohti, palautetaan null. Haluttu
     * suunta on etänormaalin osoittama suunta. Skaalataan tämä suuntavektori
     * ajalla, joka etänormaali-komponentin suuruisella vauhdilla
     * kuluisi lentää etäisyyden mittainen matka, neliön käänteisluvulla.
     * @param nopeusvektori Oman aluksen nopeusvektori.
     * @param etaisyys Oman aluksen etäisyys esteestä.
     * @return Haluttu suuntavektori esteen suhteen.
     */
    protected Vektori laskeHaluttuSuuntaEsteestaVektori( Vektori etanormaali, Vektori nopeusvektori, double etaisyys ) {
        
        if ( etanormaali == null || nopeusvektori == null || etaisyys <= 0 )
            return null;
        
        Vektori etanormaaliKomponentti = nopeusvektori.kerroVektoriprojektio( etanormaali );
        Vektori etatangenttiKomponentti = new Vektori( nopeusvektori.kerroX() - etanormaaliKomponentti.kerroX(),
                					   				   nopeusvektori.kerroY() - etanormaaliKomponentti.kerroY() );
        
        //Ei käytetä samansuuntaisuutta, koska likiarvot aiheuttavat virheitä
        if ( Math.abs( Math.PI - Vektori.kerroKulma( etanormaaliKomponentti, etanormaali ) ) < 0.1 ) {
            
            double kerroin = etanormaaliKomponentti.kerroPituus() / etaisyys;
            etanormaaliKomponentti.skaalaa( -kerroin * kerroin / etanormaaliKomponentti.kerroPituus() );
            
            return etanormaaliKomponentti;
            
        }
        
        return null;
        
    }
    /**
     * Kertoo halutun suunnan suhteessa annettuun alueeseen, jota tulisi väistää.
     * Skaalataan suuntavektori siten, että se on kääntäen verrannollinen ajan,
     * joka törmäykseen kuluisi, neliöön.
     * @param vaistettavaAlue Alue, jota pitäisi väistää.
     * @return Haluttu suuntavektori.
     */
    protected Vektori kerroHaluttuSuuntaEsteesta( Alue vaistettavaAlue ) {
        
        KappalePelaajanAlus omaAlus = this.kerroAlus();
        Vektori etanormaali;
        
        if ( omaAlus == null || vaistettavaAlue == null ||
             ( etanormaali = vaistettavaAlue.kerroEtanormaali( omaAlus.kerroSijainti() ) ) == null ) {
            
            return null;
            
        }
        
        try {
            
            double etaisyys = vaistettavaAlue.kerroEtaisyys( omaAlus.kerroSijainti() ) - omaAlus.kerroLeveys() / 2;//omaAlus.kerroSijainti().kerroEtaisyys( vaistettavaAlue.kerroSijainti() );
            return this.laskeHaluttuSuuntaEsteestaVektori( etanormaali, omaAlus.kerroNopeus(), etaisyys );
                
        }
        catch ( Exception virhe ) {
            
            return null;
        
        }
        
    }
    
    /**
     * Kertoo halutun suunnan kaikista avaruuden kaikista esteistä.
     * @return Yksittäisten haluttujen suuntien esteistä muodostama
     * summavektori. Tämä vektori on aina olemassa (vähintään nollavektori),
     * ei ole koskaan null.
     */
    protected Vektori kerroHaluttuSuuntaEsteista() {
        
        Pelaaja pelaaja = this.kerroPelaaja();
        AvaruussotaPeli peli;
        Avaruus maailma;
        KappalePelaajanAlus omaAlus;
        
        if ( pelaaja == null || ( peli = pelaaja.kerroPeli() ) == null ||
             ( maailma = peli.kerroMaailma() ) == null || ( omaAlus = pelaaja.kerroAlus() ) == null ) {
            
            return new Vektori( 0, 0 );
            
        }
        
        Vektori kokonaisHaluttuSuunta = new Vektori( 0, 0 );

        //Käydään kaikki avaruuden törmättävissä olevat kappaleet läpi paitsi oma alus.
        for ( int ind = 0; ind < maailma.kerroKappaleidenMaara(); ind++ ) {
            
            Kappale tutkittava = maailma.kerroKappale( ind );
            
            if ( tutkittava != omaAlus && tutkittava.onTormattavissa() ) {
                
                Alue vaistettavaAlue = tutkittava.kerroAlue();
                
                //Haluttu suunta yksittäisestä esteestä.
                Vektori haluttuSuunta = this.kerroHaluttuSuuntaEsteesta( vaistettavaAlue );
                
                if ( haluttuSuunta != null )
                    kokonaisHaluttuSuunta.lisaa( haluttuSuunta );
                
            }
            
        }
        
        return kokonaisHaluttuSuunta;
        
    }
    
}
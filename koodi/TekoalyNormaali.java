
/**
 * 
 * Luokka <CODE>TekoalyNormaali</CODE> kuvaa sellaista teko�ly�, joka osaa
 * ottaa avaruuden esteit�kin huomioon siten, ett� pyrkii
 * lent�m��n poisp�in esteist�. Haluttuja suuntia laskiessaan suuntavektorit
 * skaalataan siten, ett� ne ovat k��nt�en verrannollisia ajan, joka
 * osumaan/t�rm�ykseen kest�isi (suunnilleen), neli��n. N�in ollen vektoreista
 * saadaan jotenkin vertailukelpoisia. Skaalausaika kohteen vektorissa
 * on aika, joka ammuksella kuluu lent�� kohteen sijaintiin.
 *  
 * @author Jaakko Luttinen
 *
 */
public class TekoalyNormaali extends TekoalyKohde {
   
    /**
     * Luo uuden teko�lyn.
     * @param nimi Teko�lyn nimi.
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
        
        //Selvitt�� halutun suunnan esteist�.
        Vektori haluttuSuuntaEsteista = this.kerroHaluttuSuuntaEsteista();
        
        //Vektorit ovat suuruusvertailukelpoiset.
        if ( haluttuSuuntaKohteesta == null || haluttuSuuntaKohteesta.kerroPituus() < haluttuSuuntaEsteista.kerroPituus() ) {
            
            //Jos joudutaan v�istelem��n esteit�, etsit��n sitten my�s uusi kohde.
            this.etsiUusiKohde();
            return haluttuSuuntaEsteista;
            
        }
        else {
            
            return haluttuSuuntaKohteesta;

       }
        
    }
    
    /**
     * Selvitt�� kohteen t�ht�yspaikkavektorin ja skaalaa sen ajan, joka ammuksella
     * tuohon pisteeseen kest�� lent��, neli�n k��nteisluvulla.
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
            
        //Ammuksen lentoon kuluvan ajan k��nteisluku.
        double kerroin = ammuksenNopeus / etaisyys;
        
        //Skaalataan yksikk�vektori kertoimen neli�ll� (etaisyyshan on my�s vektorin pituus).
        paikkavektori.skaalaa( kerroin * kerroin / etaisyys );
        
        return paikkavektori;
            
    }
    
    /**
     * Laskee halutun suunnan vektorin suhteessa sellaiseen esteeseen, jonka et�isyys ja
     * et�normaali ovat annetut. Oman aluksen nopeuskin on annettu. Jos nopeuden
     * et�normaali-komponentti ei ole aluetta kohti, palautetaan null. Haluttu
     * suunta on et�normaalin osoittama suunta. Skaalataan t�m� suuntavektori
     * ajalla, joka et�normaali-komponentin suuruisella vauhdilla
     * kuluisi lent�� et�isyyden mittainen matka, neli�n k��nteisluvulla.
     * @param nopeusvektori Oman aluksen nopeusvektori.
     * @param etaisyys Oman aluksen et�isyys esteest�.
     * @return Haluttu suuntavektori esteen suhteen.
     */
    protected Vektori laskeHaluttuSuuntaEsteestaVektori( Vektori etanormaali, Vektori nopeusvektori, double etaisyys ) {
        
        if ( etanormaali == null || nopeusvektori == null || etaisyys <= 0 )
            return null;
        
        Vektori etanormaaliKomponentti = nopeusvektori.kerroVektoriprojektio( etanormaali );
        Vektori etatangenttiKomponentti = new Vektori( nopeusvektori.kerroX() - etanormaaliKomponentti.kerroX(),
                					   				   nopeusvektori.kerroY() - etanormaaliKomponentti.kerroY() );
        
        //Ei k�ytet� samansuuntaisuutta, koska likiarvot aiheuttavat virheit�
        if ( Math.abs( Math.PI - Vektori.kerroKulma( etanormaaliKomponentti, etanormaali ) ) < 0.1 ) {
            
            double kerroin = etanormaaliKomponentti.kerroPituus() / etaisyys;
            etanormaaliKomponentti.skaalaa( -kerroin * kerroin / etanormaaliKomponentti.kerroPituus() );
            
            return etanormaaliKomponentti;
            
        }
        
        return null;
        
    }
    /**
     * Kertoo halutun suunnan suhteessa annettuun alueeseen, jota tulisi v�ist��.
     * Skaalataan suuntavektori siten, ett� se on k��nt�en verrannollinen ajan,
     * joka t�rm�ykseen kuluisi, neli��n.
     * @param vaistettavaAlue Alue, jota pit�isi v�ist��.
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
     * Kertoo halutun suunnan kaikista avaruuden kaikista esteist�.
     * @return Yksitt�isten haluttujen suuntien esteist� muodostama
     * summavektori. T�m� vektori on aina olemassa (v�hint��n nollavektori),
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

        //K�yd��n kaikki avaruuden t�rm�tt�viss� olevat kappaleet l�pi paitsi oma alus.
        for ( int ind = 0; ind < maailma.kerroKappaleidenMaara(); ind++ ) {
            
            Kappale tutkittava = maailma.kerroKappale( ind );
            
            if ( tutkittava != omaAlus && tutkittava.onTormattavissa() ) {
                
                Alue vaistettavaAlue = tutkittava.kerroAlue();
                
                //Haluttu suunta yksitt�isest� esteest�.
                Vektori haluttuSuunta = this.kerroHaluttuSuuntaEsteesta( vaistettavaAlue );
                
                if ( haluttuSuunta != null )
                    kokonaisHaluttuSuunta.lisaa( haluttuSuunta );
                
            }
            
        }
        
        return kokonaisHaluttuSuunta;
        
    }
    
}
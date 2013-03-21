
/**
 *
 * Luokka <CODE>TekoalyTangentti</CODE> kuvaa teko‰ly‰, joka toimii muuten
 * yl‰luokkansa tavoin, mutta ottaa ajattelussaan maa-alueet sujuvammin
 * huomioon. Teko‰ly suostuu lent‰m‰‰n alueiden tangenttien suuntaisesti
 * eik‰ vaadi alusta lent‰m‰‰n puhtaasti et‰normaalin suuntaisesti. N‰in
 * ollen aluksen lent‰misest‰ tulee luontevampaa. Kohteen t‰ht‰yksess‰
 * luokka k‰ytt‰‰ hieman tarkempaa arviota: Laskee miss‰ kohde on sen
 * ajan kuluttua, mik‰ ammuksella sen nykyiseen sijaintiin kest‰isi,
 * ja t‰ht‰‰ tuohon sijaintiin.
 *  
 * @author Jaakko Luttinen
 *
 */
public class TekoalyTangentti extends TekoalyNormaali {
   
    /**
     * Luo uuden teko‰lyn.
     * @param nimi Teko‰lyn nimi.
     */
    public TekoalyTangentti( String nimi ) {
        
        super( nimi );

    }
    
    public Tekoaly kerroKopio() {
        
        return new TekoalyTangentti( this.kerroNimi() );
        
    }
    
    protected Vektori kerroKohteenTahtaysPaikkavektori() {

        //Tutkitaan, miss‰ kohde on sen ajan kuluttua, mik‰ ammukselta kuluisi
        //lent‰‰ kohteen nykyiseen sijaintiin. T‰hd‰t‰‰n tuohon uuteen sijaintiin.
        
        KappalePelaajanAlus omaAlus = this.kerroAlus();
        KappalePelaajanAlus kohteenAlus = this.kerroKohde();
        Vektori kohteenSijainti0 = this.kerroKohteenPaikkavektori();
        
        if ( omaAlus == null || kohteenAlus == null || kohteenSijainti0 == null )
            return null;

        //Kiihdytyskerroin..
        int kaasuKerroin = 0;
        if ( kohteenAlus.kerroKaasu() )
            kaasuKerroin = 1;

        //Ohjausta ei oteta huomioon, joten nokan suunta ja kiihtyvyys ovat vakioita.
        Kulma kohteenNokanSuunta = new Kulma( kohteenAlus.kerroNokanSuunta().kerroKulma() );
        Vektori kohteenKiihtyvyys = new Vektori( kaasuKerroin * Math.cos( kohteenNokanSuunta.kerroKulma() ) * kohteenAlus.kerroKiihtyvyys(),
				  								 kaasuKerroin * Math.sin( kohteenNokanSuunta.kerroKulma() ) * kohteenAlus.kerroKiihtyvyys() );
        Vektori kohteenNopeus0 = new Vektori( kohteenAlus.kerroNopeus().kerroX() - omaAlus.kerroNopeus().kerroX(),
                							  kohteenAlus.kerroNopeus().kerroY() - omaAlus.kerroNopeus().kerroY() );
        
        //Aika, joka ammukselta kuluu lent‰‰ aluksen nykyiseen sijaintiin.
        double ammuksenNopeus = omaAlus.kerroPaaase().kerroAmmusmalli().kerroLahtonopeus();
        double aika = kohteenSijainti0.kerroPituus() / ammuksenNopeus;

        //Lasketaan uusi sijainti ajan kuluttua. Ei voida k‰ytt‰‰ ihan perus yht‰lˆ‰ x=x0+v0t+0.5*at^2,
        //koska aluksella on maksiminopeus, joka t‰ytyy ottaa huomioon. Aika hyv‰ arvio
        //saadaan alkunopeuden ja loppunopeuden m‰‰r‰‰m‰ll‰ keskinopeudella.
        Vektori kohteenNopeus1 = new Vektori( kohteenNopeus0.kerroX() + aika * kohteenKiihtyvyys.kerroX(),
  			  								  kohteenNopeus0.kerroY() + aika * kohteenKiihtyvyys.kerroY() );
        //Korjataan liian suuri loppunopeus.
        double ylinopeussuhde = this.kerroKohde().kerroMaksiminopeus() / kohteenNopeus1.kerroPituus();
        if ( ylinopeussuhde < 1 )
            kohteenNopeus1.skaalaa( ylinopeussuhde );
        
        //Lasketaan lopullinen sijainti.
        Vektori kohteenSijainti1 = new Vektori( kohteenSijainti0.kerroX() + aika * 0.5 * ( kohteenNopeus0.kerroX() + kohteenNopeus1.kerroX() ),
												kohteenSijainti0.kerroY() + aika * 0.5 * ( kohteenNopeus0.kerroY() + kohteenNopeus1.kerroY() ) );
                
        return kohteenSijainti1;

    }

    /**
     * Laskee halutun suunnan esteest‰. Jos nopeuden et‰normaali-komponentti ei ole aluetta kohti,
     * palautetaan null. K‰‰nnet‰‰n et‰normaali-komponentti vastakkaissuuntaiseksi ja
     * skaalataan koko nopeus sen ajalla, joka et‰normaali-komponentin suuruisella vauhdilla
     * kuluisi lent‰‰ et‰isyyden mittainen matka, neliˆn k‰‰nteisluvulla.
     * @return Haluttu suunta esteest‰.
     */
    protected Vektori laskeHaluttuSuuntaEsteestaVektori( Vektori etanormaali, Vektori nopeusvektori, double etaisyys ) {
        
        if ( etanormaali == null || nopeusvektori == null || etaisyys <= 0 )
            return null;
        
        Vektori etanormaaliKomponentti = nopeusvektori.kerroVektoriprojektio( etanormaali );
        Vektori etatangenttiKomponentti = new Vektori( nopeusvektori.kerroX() - etanormaaliKomponentti.kerroX(),
                					   				   nopeusvektori.kerroY() - etanormaaliKomponentti.kerroY() );
        
        //Ei k‰ytet‰ samansuuntaisuutta, koska likiarvot aiheuttavat virheit‰.
        //Jos eiv‰t ole suunnilleen vastakkaissuuntaiset, palautetaan null.
        if ( Math.abs( Math.PI - Vektori.kerroKulma( etanormaaliKomponentti, etanormaali ) ) > 0.1 )
            return null;
        
        etanormaaliKomponentti.aseta( -etanormaaliKomponentti.kerroX() + etatangenttiKomponentti.kerroX(),
                					  -etanormaaliKomponentti.kerroY() + etatangenttiKomponentti.kerroY() );

        //Aika, joka tˆrm‰ykseen kest‰isi. Ei ihan todellinen arvo, mutta antaa
        //hyv‰n vertailukelpoisen arvon.
        double kerroin = etanormaaliKomponentti.kerroPituus() / etaisyys;
        
        etanormaaliKomponentti.skaalaa( kerroin * kerroin / etanormaaliKomponentti.kerroPituus() ); //( haluttuSuunta.kerroPituus() * ( etaisyys * etaisyys ) ) );
        
        return etanormaaliKomponentti;
        
    }

}
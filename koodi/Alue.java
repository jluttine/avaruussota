
/**
 * 
 * Luokka <CODE>Alue</CODE> kuvaa jonkinmuotoista aluetta. Se voidaan asettaa
 * johonkin koordinaatistoon johonkin sijaintiin. Alue rajataan suorakulmiolla,
 * jonka avulla alueen leveys ja korkeus m‰‰ritet‰‰n. Alueelle voi l‰hett‰‰
 * metodi-kutsun, jolla se tutkii leikkaako t‰m‰ alue annetun alueen kanssa.
 * Jokaisen alueen tulee kyet‰ m‰‰ritt‰m‰‰n pisteen et‰isyys alueesta sek‰
 * alueen et‰normaali jossakin pisteess‰. Et‰normaali on suuntavektori alueen
 * l‰himm‰st‰ pisteest‰ annettua pistett‰ kohti.
 *
 * @author Jaakko Luttinen
 * 
 */

public abstract class Alue {
    
    /**
     * Alueen sijainti tai null.
     */
    private Koordinaatit sijaintiKeskipiste;
    
    /**
     * Aluetta rajaavan suorakulmion leveys.
     */
    private int leveys;
    
    /**
     * Aluetta rajaavan suorakulmion korkeus.
     */
    private int korkeus;
    
    /**
     * Luo uuden alueen, jota rajaa annetun kokoinen suorakulmio.
     * @param leveys Aluetta rajaavan suorakulmion leveys.
     * @param korkeus Aluetta rajaavan suorakulmion korkeus.
     * @throws Exception Poikkeus, joka kertoo, ett‰ leveys tai korkeus oli negatiiviset.
     */
    protected Alue( int leveys, int korkeus ) throws Exception {

        this.tarkastaMitat( leveys, korkeus );
        
        this.leveys = leveys;
        this.korkeus = korkeus;
        
    }
    
    /**
     * Luo uuden alueen, jonka mitat ja koordinaatit ovat samat kuin kopiot, mutta
     * luo niist‰ kuitenkin omat kopionsa. Koordinaatisto on sama.
     * @param kopio Alue, jonka perusteella uusi alue luodaan.
     */
    protected Alue( Alue kopio ) {
        
        this.leveys = kopio.leveys;
        this.korkeus = kopio.korkeus;
        
        if ( kopio.sijaintiKeskipiste != null )
            this.sijaintiKeskipiste = new Koordinaatit( kopio.sijaintiKeskipiste );
        
    }
    
    /**
     * Asettaa alueen sijainnin.
     * @param sijainti Alueen uusi sijainti.
     * @throws Exception Poikkeus, jos alue on liian suuri koordinaatistoon.
     */
    public void asetaSijainti( Koordinaatit sijainti ) throws Exception {
        
        if ( this.onLaitonKoordinaatisto( sijainti.kerroKoordinaatisto() ) ) {
            
            throw new Exception( "Aluetta ei voida asettaa annettuun koordinaatistoon. " +
            					 "Alue on mitoiltaan liian suuri." );
            
        }
        
        this.sijaintiKeskipiste = sijainti;
        
    }
    
    /**
     * Poistaa kappaleen koordinaatistosta, jossa se on, eli asettaa sijainniksi null.
     */
    public void poistaSijainti() {
        
        this.sijaintiKeskipiste = null;
        
    }

    /**
     * Kertoo, onko koordinaatisto alueelle laiton. So. alue on mitoiltaan
     * liian suuri koordinaatistoon.
     * @param koordinaatisto Koordinaatisto, johon alue haluttaisiin laittaa.
     * @return Totuusarvo, joka kertoo, onko koordinaatisto alueelle laiton.
     */
    public boolean onLaitonKoordinaatisto( Koordinaatisto koordinaatisto ) {
        
        return this.leveys > koordinaatisto.kerroLeveys() ||
        	   this.korkeus > koordinaatisto.kerroKorkeus();
        
    }
    
    /**
     * Asettaa aluetta rajaavan suorakulmion leveyden. Vain luokkahierarkian
     * sis‰iseen k‰yttˆˆn! Poikkeus heitet‰‰n, jos leveys/korkeus on suurempi kuin
     * koordinaatiston, jossa alue on, leveys/korkeus.
     * @param leveys Aluetta rajaavan suorakulmion leveys.
     * @param korkeus Aluetta rajaavan suorakulmion korkeus.
     * @throws Exception Poikkeus kertoo, ett‰ leveys ja/tai korkeus on liian suuri.
     */
    protected void asetaMitat( int leveys, int korkeus ) throws Exception {
        
        this.tarkastaMitat( leveys, korkeus );
        
        this.leveys = leveys;
        this.korkeus = korkeus;
        
    }
    
    private void tarkastaMitat( int leveys, int korkeus ) throws Exception {
        
        if ( leveys < 0 || korkeus < 0 )
            throw new Exception( "Alueen mittojen t‰ytyy olla ep‰negatiivisia." );
        
        if ( this.sijaintiKeskipiste != null && 
             ( leveys > this.sijaintiKeskipiste.kerroKoordinaatisto().kerroLeveys() ||
               korkeus > this.sijaintiKeskipiste.kerroKoordinaatisto().kerroKorkeus() ) ) {
               
            throw new Exception( "Alueen mittoja ei voida asettaa koordinaatistoa suuremmaksi." );
               
        }
           
    }

    /**
     * Kertoo aluetta rajaavan suorakulmion leveyden.
     * @return Aluetta rajaavan suorakulmion leveys.
     */
    public int kerroLeveys() {
        
        return this.leveys;
        
    }
    
    /**
     * Kertoo aluetta rajaavan suorakulmion korkeuden.
     * @return Aluetta rajaavan suorakulmion korkeus.
     */
    public int kerroKorkeus() {
        
        return this.korkeus;
        
    }
    
    /**
     * Kertoo annetun pisteen lyhimm‰n et‰isyyden alueesta. Jos et‰isyytt‰
     * ei voida m‰‰ritt‰‰, palautetaan negatiivinen luku. Et‰isyys ei
     * v‰ltt‰m‰tt‰ ole tarkka. Sen m‰‰ritt‰misess‰ saatetaan k‰ytt‰‰
     * nopeita ja karkeita arvioita, jotka antavat likimain oikean
     * arvon.
     * @param sijainti Sijainti, jonka et‰isyys alueesta halutaan selvitt‰‰.
     * @return Koordinaattien et‰isyys alueesta.
     * @throws Exception Poikkeus, kertoo, ettei et‰isyytt‰ voida m‰‰ritt‰‰.
     */
    public abstract double kerroEtaisyys( Koordinaatit sijainti ) throws Exception;
    
    /**
     * Kertoo aluetta rajaavan suorakulmion keskipisteen sijainnin tai
     * null, jos sijaintia ei ole asetettu.
     * @return Aluetta rajaavan suorakulmion keskipisteen sijainti tai null,
     * jos sijaintia ei ole asetettu.
     */
    public Koordinaatit kerroSijainti() {
        
        return this.sijaintiKeskipiste;
        
    }
    
    /**
     * Kertoo alueen pinta-alan mahdollisimman tarkasti, mutta ennen kaikkea nopeasti.
     * @return Alueen pinta-ala.
     */
    public abstract double kerroAla();
    
    /**
     * Et‰normaalivektori on vektori, joka osoittaa <CODE>sijaintia</CODE> l‰hinn‰
     * olevasta alueen pisteest‰ kohti <CODE>sijaintia</CODE>. Jos alue ei sijaitse
     * miss‰‰n, palautetaan null. Et‰normaalia ei v‰ltt‰m‰tt‰ kyet‰ m‰‰ritt‰m‰‰n
     * tarkasti ja nopeasti, joten sen m‰‰rityksess‰ saatetaan k‰ytt‰‰ nopeita ja
     * karkeita arvioita, jotka antavat likimain oikean suuntaisen vastauksen.
     * Palautetaan null, myˆs jos <CODE>sijainti</CODE> ei ole samasta koordinaatistosta.
     * @param sijainti Koordinaatit, joiden suhteen et‰normaali kerrotaan.
     * @return Et‰normaalivektori tai null, jos alue ei sijaitse miss‰‰n tai sijainti
     * ei ole samasta koordinaatistosta.
     */
    public abstract Vektori kerroEtanormaali( Koordinaatit sijainti );
    
    /**
     * Kertoo, leikkaako alue parametrina annetun alueen kanssa. Palauttaa 0, jos
     * eiv‰t leikkaa. Palauttaa 1, jos leikkaavat. Palauttaa -1, jos metodi ei
     * osaa kertoa, leikkaavatko alueet. Palautusarvo -1 on sen takia, ett‰
     * aliluokan toteutusvaiheessa ei v‰ltt‰m‰tt‰ tiedet‰, mit‰ kaikkia muita
     * aliluokkia <CODE>Alueesta</CODE> toteutetaan.
     * @param verrattava Alue, jonka kanssa tapahtuvaa leikkausta tutkitaan.
     * @return 0=ei leikkausta, 1=leikkaus, -1=ei tied‰.
     */
    protected abstract int leikkaustesti( Alue verrattava );
    
    /**
     * Kutsuu ensin omaa <CODE>leikkaustesti</CODE>-metodiaan. Jos sill‰ ei ole
     * leikkaustesti‰ m‰‰riteltyn‰ (palautusarvo=-1), kutsuu parametrina saadun
     * alueen <CODE>leikkaustesti</CODE>-metodia. Jos sill‰k‰‰n ei ole leikkaustesti‰
     * m‰‰riteltyn‰ palauttaa totuusarvon, joka kertoo, leikkaavatko alueita
     * rajaavat suorakaiteet. Muutoin palauttaa totuusarvon, joka kertoo leikkaavatko
     * alueet jomman kumman alueen <CODE>leikkaustesti</CODE>-metodin perusteella, so.
     * palautusarvo on 1.  
     * @param verrattava Alue, jonka kanssa tapahtuvaa leikkausta tutkitaan.
     * @return Totuusarvo, joka kertoo, leikkaavatko alueet.
     */
    public boolean leikkaavat( Alue verrattava ) {
        
        int palautusarvo = this.leikkaustesti( verrattava );
        
        if ( palautusarvo == -1 )
            palautusarvo = verrattava.leikkaustesti( this );
        
        //Jos palautusarvo == -1, niin mink‰‰nlaista tˆrm‰yksen testausta ei ole ohjelmoitu...
        if ( palautusarvo == -1 )
            return Alue.leikkaavatSuorakulmioJaSuorakulmio( this, verrattava );
        
        return palautusarvo == 1;
        
    }
    
    /**
     * Kertoo, leikkaavatko kahden parametrina saatuja alueita rajaavat suorakulmiot. Jos
     * alueet ovat eri koordinaatistoissa tai eiv‰t ole miss‰‰n koordinaatistossa, eiv‰t
     * alueet leikaa toisiaan.
     * @param verrattava1 Toinen alue, jota rajaavaa suorakaidetta k‰ytet‰‰n.
     * @param verrattava2 Toinen alue, jota rajaavaa suorakaidetta k‰ytet‰‰n.
     * @return Totuusarvo, joka kertoo, leikkaavatko alueita rajaavat suorakulmiot.
     */
    public static boolean leikkaavatSuorakulmioJaSuorakulmio( Alue verrattava1, Alue verrattava2 ) {
        
        if ( verrattava2.sijaintiKeskipiste == null || verrattava1.sijaintiKeskipiste == null ||
             verrattava2.sijaintiKeskipiste.kerroKoordinaatisto() != verrattava1.sijaintiKeskipiste.kerroKoordinaatisto() ) {
            
            //Alueet eiv‰t voi leikata jos ne eiv‰t sijaitse samassa koordinaatistossa.
            return false;
            
        }
        
        return ( Math.abs( verrattava1.kerroSijainti().kerroLyhinMatkaX( verrattava2.kerroSijainti().kerroSijaintiX() ) ) <= 0.5 * ( verrattava1.leveys + verrattava2.leveys ) ) &&
        	   ( Math.abs( verrattava1.kerroSijainti().kerroLyhinMatkaY( verrattava2.kerroSijainti().kerroSijaintiY() ) ) <= 0.5 * ( verrattava1.korkeus + verrattava2.korkeus ) );
        
    }
    
    
}
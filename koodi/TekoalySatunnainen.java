
/**
 * 
 * Luokka <CODE>TekoalySatunnainen</CODE> kuvaa tekoälyä, joka välittää
 * satunnaisia ohjauskomentoja alukselleen. Kaasu ja liipaisin pidetään
 * pohjassa, mutta ohjaussuunta arvotaan aina muutaman toimi-kerran
 * jälkeen.
 * 
 * @author Jaakko Luttinen
 *
 */
public class TekoalySatunnainen extends Tekoaly {

    /**
     * Laskee, kuinka monta miettimisvuoroa on kulunut viime ohjauksen muutosta.
     */
    private int laskuri;
    
    /**
     * Luo uuden tekoälyn annetulla nimellä.
     * @param nimi Tekoälyn nimi.
     */
    public TekoalySatunnainen( String nimi ) {
        
        super( nimi );

    }

    public Tekoaly kerroKopio() {
        
        return new TekoalySatunnainen( this.kerroNimi() );
        
    }
    
    /**
     * Jos miettimiskertoja viime suunnan arpomisesta on kulunut
     * riittävästi, arvotaan uusi ohjaussuunta.
     */
    public void toimi() {
        
        Pelaaja pelaaja = this.kerroPelaaja();
        AvaruussotaPeli peli;
        Avaruus maailma;
        
        //null-testit.
        if ( pelaaja == null || ( peli = pelaaja.kerroPeli() ) == null || ( maailma = peli.kerroMaailma() ) == null )
            return;
        
        synchronized ( this.kerroPelaaja().kerroPeli().kerroMaailma() ) {
            
            KappalePelaajanAlus omaAlus = this.kerroPelaaja().kerroAlus();
            
            //Ei tarvitse miettiä, jos oma alus ei ole pelissä.
            if ( omaAlus == null || omaAlus.onTuhoutunut() )
                return;
            
            //Onko aika vaihtaa ohjaussuuntaa?
            if ( laskuri >= 5 ) {
                
                //Arvotaan jokin ohjaussuunnista: -1, 0, 1.
                this.asetaOhjaus( ( int )( Math.random() * 3 ) - 1 );
                laskuri = 0;
                
            }
            
            laskuri++;
            
            this.asetaKaasu( true );
            this.asetaLiipaisin( true );
 
        }
        
    }
    
}
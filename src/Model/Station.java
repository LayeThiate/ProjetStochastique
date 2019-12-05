package Model;


public class Station {
    public String description;

    public String code;

    public int id;

    public int capaMax;

    public int disponible;

    public int nbPlaceDispo;

    public int nbVeloInitial;

    public int coutAjout;

    public int coutManque;

    public int coutTempsPerdu;

    public float latitude;

    public float longitude;
    
    @Override
    public boolean equals(Object obj) {
    	if(this == obj)
    		return true;
    	if(obj == null || this.getClass() != obj.getClass())
    		return false;
    	return ((Station) obj).id == this.id;
    }
 
}

package Model;

import java.util.ArrayList;
import java.util.List;

public class Scenario {
	
	private static final int ADD_CYCLE = 1;
	private static final int REMOVE_CYCLE =  2;
	private static final int TRANSFER_CYCLE =  3;

	
    public int id;

    public float proba;

    public List<Station> listStation = new ArrayList<Station> ();

    public List<Trajet> listTrajet = new ArrayList<Trajet> ();

    public void run() {
    	
    }

    public double cout() {
    	double rslt = 0.0;
    	Station depart, arrivee;
    	int demande;
    	for(int i = 0; i < listStation.size(); i++) {
    		//ajout de ci*xi
    		rslt += listStation.get(i).coutAjout * listStation.get(i).nbVeloInitial;
    		
    		double Iminus = 0.0;
    		double Ominus = listStation.get(i).nbVeloInitial - listStation.get(i).capaMax;
    		for(int j = 0; j < listStation.size(); j++) {
    			Iminus += listStation.get(i).demande.get(j);
    			Ominus -= listStation.get(i).demande.get(j);
    			Ominus += listStation.get(j).demande.get(i);
    		}
    		
    		//ajout de vi*Iminus
    		rslt += listStation.get(i).coutManque * Iminus;
    		//ajout de wi*Ominus
    		rslt += listStation.get(i).coutTempsPerdu * Ominus;
    	}
    	return rslt;
    }

    public double coutStocha() {
        // TODO Auto-generated return
        return 0;
    }

    public double voisinage() {
        // TODO Auto-generated return
    	//selection de la méthode de selection du voisin
    	int modifType = (int) Math.random()/3 + 1;
    	switch(modifType) {
    		case ADD_CYCLE:
    			return this.addCycle();
    		case REMOVE_CYCLE:
    			return this.removeCycle();
    		case TRANSFER_CYCLE:
    			return this.transferCycle();
    		default:
    			return 0.0;
    	}
    }
    
    private double addCycle() {
    	Station changeLoc;
    	int repNum = 0;
    	while(repNum < listStation.size()) {
    		//essayer un certain nombre
    		changeLoc = listStation.get((int) Math.random() / (listStation.size()));
        	if(changeLoc.capaMax - 1 > changeLoc.nbVeloInitial) {
        		changeLoc.nbVeloInitial++;
        		return changeLoc.coutAjout; //renvoyer le cout de l'ajout
        	}
        	else
        		repNum++; // compter le nombre d'essai
    	}
    	return 0.0;	
    }
    
    private double removeCycle() {
    	Station changeLoc = listStation.get((int) Math.random() / (listStation.size()));
    	changeLoc.nbVeloInitial--;
    	return 0; //aucun cout pour la suppression d'un velo
    }
    
    private double transferCycle() {
    	Station removeLoc = listStation.get((int) Math.random() / (listStation.size()));
    	Station addLoc;
    	int repNum = 0;
    	while(repNum < listStation.size()) {
    		//essayer un certain nombre
    		addLoc = listStation.get((int) Math.random() / (listStation.size()));
        	if(addLoc.capaMax - 1 > addLoc.nbVeloInitial) {
        		addLoc.nbVeloInitial++;
        		removeLoc.nbVeloInitial--;
        		return addLoc.coutAjout; //renvoyer le cout de l'ajout
        	}
        	else
        		repNum++; // compter le nombre d'essai
    	}
    	return 0.0;
    }
    
    
}
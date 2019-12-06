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
    	for(int i = 0; i < listTrajet.size(); i++) {//cout lie aux trajets
    		//extraction des données
    		depart = listTrajet.get(i).depart;
    		arrivee = listTrajet.get(i).arrivee;
    		//vérification de la disponiibilite
    		if(depart.disponible == 0) //pas de velo disponible
    			rslt += depart.coutManque;
			else {//velo disponible
				depart.disponible -= 1;//un velo en moins disponible
				depart.nbPlaceDispo += 1;
				if(arrivee.nbPlaceDispo == 0) //pas de place pour deposer le velo
					rslt += arrivee.coutTempsPerdu;
				else {
					arrivee.disponible += 1; //ajout d'un velo
					arrivee.nbPlaceDispo -= 1;
				}
			}
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
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
    	for(int i = 0; i < listTrajet.size(); i++) {//cout lie aux trajets
    		//extraction des données
    		depart = listTrajet.get(i).depart;
    		arrivee = listTrajet.get(i).arrivee;
    		demande = listTrajet.get(i).demande;
    		//vérification de la disponiibilite
    		if(depart.disponible <= demande) { //pas assez de velo disponible
    			int veloParti = (demande - depart.disponible);
    			rslt += veloParti* depart.coutManque;
    			depart.disponible = 0;
				depart.nbPlaceDispo = depart.capaMax;
				
				if(arrivee.nbPlaceDispo <= veloParti) { //pas assez de place pour deposer le velo
					rslt += (veloParti - arrivee.nbPlaceDispo) * arrivee.coutTempsPerdu;
					arrivee.disponible = arrivee.capaMax;
					arrivee.nbPlaceDispo = 0;
				}
				else { 
					arrivee.disponible += veloParti; 
					arrivee.nbPlaceDispo -= veloParti;
				}
    		}
			else {//assez de velo disponible au depart
				depart.disponible -= demande;
				depart.nbPlaceDispo += demande;
				
				if(arrivee.nbPlaceDispo <= demande ) { //pas de place pour deposer le velo
					rslt += (demande - arrivee.nbPlaceDispo) * arrivee.coutTempsPerdu;
					arrivee.disponible = arrivee.capaMax;
					arrivee.nbPlaceDispo = 0;
				}
				else {
					arrivee.disponible += demande;
					arrivee.nbPlaceDispo -= demande;
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
package Model;

import java.util.ArrayList;
import java.util.List;

import Controller.*;

public class RecuitSup {
    public int taille;
    
    public ApproxGenerique approxGenerique;

    public List<Scenario> listScenario = new ArrayList<Scenario> ();
    
    public List<Integer> listSolution = new ArrayList<Integer>();
    
    public FonctionObjectif fonctionObjectif;
    
    public Data data;

    public void resolution() {
    	//recuit 
    	List<RecuitStochastique> listRecuit = new ArrayList<RecuitStochastique>();
    	
    	for(int i = 0 ; i < taille; i++) {
    		listRecuit.get(i).resoudreFoncObjectif(listScenario.get(0));
    		listSolution.add(listRecuit.get(i).valeurFoncObjectif);
    	}
    }

    public void genererScenario(String filePath) throws Exception {
    	//parser les données entrées par l'utilisateur et créer les objets
    	data.parseCSV(filePath);
    	int numScenario = data.getNumScenario();
    	int numStation = data.getNumStation();
    	if(numScenario <= 0) {
    		System.err.println("MODEL ERROR: Incorrect number of scenarios, check CSV file");
    		return;
    	}
    	if(numStation <= 0) {
    		System.err.println("MODEL ERROR: Incorrect number of stations, check CSV file");
    		return;
    	}
    	this.taille = numScenario;
    	for(int i = 0; i < numScenario; i++) {
    		Scenario scenario = listScenario.get(i);
    		for(int j = 0; j < numStation; j++) {
    			Station station = new Station();
    			station.id = data.getId()[j];
    			station.capaMax = (int) data.getK()[j];
    			station.coutAjout = data.getC()[j];
    			station.coutManque = data.getV()[j];
    			station.coutTempsPerdu = data.getW()[j];
    			station.disponible = data.getDisponible()[j];
    			station.nbPlaceDispo = data.getNbPlaceDispo()[j];
    			station.latitude = data.getLatitude()[j];
    			station.longitude = data.getLongitude()[j];
    			scenario.listStation.add(station);
    		}
    		for(int j = 0; j < numStation; j++) {
    			for(int k = 0  ; k < numStation; k++) {
    				Trajet trajet = new Trajet();
    				trajet.depart = scenario.listStation.get(j);
    				trajet.arrivee = scenario.listStation.get(k);
    				trajet.demande = (int) data.getEps()[j][k][i];
    			}
    		}
    	}
    }

    public double getSolution() {
    	if(listSolution.size() <= 0) {
    		System.err.println("MODEL ERROR: No solutions found in solution list");
    		return 0.0;
    	}
    	double meilleureSolution = listSolution.get(0);
    	for(int i = 1 ; i < taille; i++) {
    		if(fonctionObjectif.betterSolution(meilleureSolution, listSolution.get(i))) {
    			meilleureSolution = listSolution.get(i);
    		}
    	}
    	return meilleureSolution;
    }

    public double getMoyenne() {
    	if(listSolution.size() <= 0) {
    		System.err.println("MODEL ERROR: No solutions found in solution list");
    		return 0.0;
    	}
    	double rslt = 0;
    	for(int i =0 ; i<listSolution.size() ; i++)
    		rslt += listSolution.get(i);
    	return rslt/listSolution.size();
    }
    
    //fonction pour renvoyer l'affichage de la solution a l'UI
    public String replyToUI() {
    	//lancer la resolution
    	this.resolution();
    	//reponse a l'UI sous forme de string
    		//solution optimale
    	String rslt ="Solution optimale: " + getSolution();
    		//moyenne des solutions
    	rslt += "\nMoyenne des solutions: " + getMoyenne();
    	return rslt;
    }

}

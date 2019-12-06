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
    		listRecuit.get(i).resoudreFoncObjectif(listScenario.get(i));
    		listSolution.add(listRecuit.get(i).valeurFoncObjectif);
    	}
    }

    public void genererScenario() throws Exception {
    	//parser les données entrées par l'utilisateur et créer les objets
    	String pathFile = " ";
    	data.parseCSV(pathFile);
    	int numScenario = data.getNumScenario();
    	int numStation = data.getNumStation();
    	for(int i = 0; i < numScenario; i++) {
    		Scenario scenario = listScenario.get(i);
    		for(int j = 0; j < numStation; j++) {
    			
    		}
    	}
    }

    public double getSolution() {
    	double meilleureSolution = listSolution.get(0);
    	for(int i = 1 ; i < taille; i++) {
    		if(fonctionObjectif.betterSolution(meilleureSolution, listSolution.get(i))) {
    			meilleureSolution = listSolution.get(i);
    		}
    	}
    	return meilleureSolution;
    }

    public float getMoyenne() {
    	float rslt = 0;
    	for(int i =0 ; i<listSolution.size() ; i++)
    		rslt += listSolution.get(i);
    	return rslt/listSolution.size();
    }

}

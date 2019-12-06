package Model;

import java.util.ArrayList;
import java.util.List;

import Controller.RecuitStochastique;

public class RecuitSup {
    public int taille;

    public List<Scenario> listScenario = new ArrayList<Scenario> ();
    
    public List<Integer> listSolution = new ArrayList<Integer>();

    public void resolution() {
        // TODO Auto-generated return
    	List<RecuitStochastique> listRecuit = new ArrayList<RecuitStochastique>();
    	for(int i = 0 ; i < taille; i++) {
    		listRecuit.get(i).resoudreFoncObjectif(listScenario.get(i));
    		listSolution.add(listRecuit.get(i).valeurFoncObjectif);
    	}
    }

    public void genererScenario() {
    	
    }

    public void getSolution() {
    	
    }

    public float getMoyenne() {
    	float rslt = 0;
    	for(int i =0 ; i<listSolution.size() ; i++)
    		rslt += listSolution.get(i);
    	return rslt/listSolution.size();
    }

}

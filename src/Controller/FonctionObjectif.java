package Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.*;

public class FonctionObjectif {
    public double meilleureSolution;
    
    public HashMap<String , Double> variables;
    	//variables de la fonctions

    public double[] parametre;

    public String[] coefficients;
    
    public String objectif = "minimize";
    	//maximize ou minimize
    	//set to minimize by default

    public List<Trajet> listTrajet = new ArrayList<Trajet> ();
    	//liste des demandes pour chaque couple de stations
    
    public float getValue(Scenario scenario) {
    	return 0.0f;
    }
    
    public boolean betterSolution(double champion, double contestant) {
    	boolean rslt = (objectif == "maximize") ? champion <= contestant: champion >= contestant;
    	return rslt;
    }
}

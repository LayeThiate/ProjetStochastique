package Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Model.*;

public class FonctionObjectif {
    public int meilleureSolution;
    
    public HashMap<String , Integer> variables;

    public int[] parametre;

    public String[] coefficients;
    
    public String objectif;
    	//maximize ou minimize

    public List<Trajet> listTrajet = new ArrayList<Trajet> ();
    
    public float getValue(Scenario scenario) {
    	return 0.0f;
    }
    
    public boolean betterSolution(int champion, int contestant) {
    	boolean rslt = (objectif == "maximize") ? champion <= contestant: champion >= contestant;
    	return rslt;
    }
}

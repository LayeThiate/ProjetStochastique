package Controller;

import Model.Scenario;

public class ApproxGenerique {
    public int tailleEchant;

    public Scenario scenario;
    
    public int solutionOptimale;

    public void resoudreEchant() {
    	//affecter la solution optimale par la r�solution Cplex
    	solutionOptimale = 0;
    }

}

package Controller;

import Model.Scenario;

public class ApproxGenerique {
    public int tailleEchant;

    public Scenario scenario;
    
    public double solutionOptimale;

    public void resoudreEchant() {
    	//affecter la solution optimale par la résolution Cplex
    	solutionOptimale = 0;
    }

}

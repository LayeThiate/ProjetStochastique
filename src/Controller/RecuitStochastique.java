package Controller;

import java.util.List;

import Model.Scenario;

public class RecuitStochastique extends RecuitGlobal {
	
    public void resoudreFoncObjectif() {
    	//fonction de resolution par recuit
    		//variables necessaires pour le recuit
    	int k = 0;
    	int kmax = 10;
    	float p = 0.0f;
    	float newCout = 0.0f;
    	Scenario scenario = new Scenario();
    	Scenario minScenario = scenario;
    	float cout = fonctionObjectif.getValue(scenario);
    	boolean endCondition = false;
    	//boucle du recuit
    	while(!endCondition) {    		//condition sur la temperature
    		while(k<kmax){        		//iteration maximale du recuit pris a n**2 habituellement
        		scenario = scenario.voisinage(); //voisin du scenario
        		newCout = fonctionObjectif.getValue(scenario); //calcul du cout pour nouveau scenario
        		if(newCout < cout) {
        			//scenario donne plus petit cout
        			minScenario = scenario;
        			cout = newCout;
        		}
        		else {
        			//scneario donne plus grand cout
        			p = (float) Math.random();
        			if(p <= Math.exp((newCout-cout)/temperature)) { // on decide de garder cette solution
            			minScenario = scenario;
            			cout = newCout;
        			}
        		}
        		k++;
        	}
			temperature = foncDecroissante();
			endCondition = false;
    	}
    	
    }

    public float foncDecroissante() {
    	return 0.0f;
    }

}

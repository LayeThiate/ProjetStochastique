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
    	//while(!endCondition) {    		//condition sur la temperature
    		while(k<kmax){        		//iteration maximale du recuit pris a n**2 habituellement
        		int coutVoisin = scenario.voisinage(); //voisin du scenario et cout de mis en place
        		newCout = fonctionObjectif.getValue(scenario) + coutVoisin; //calcul du cout pour nouveau scenario 
    																		//avec cout de mise en place
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
			this.valeurFoncObjectif = calculCout(minScenario);
    	//}
    	
    }

    public float foncDecroissante() {
    	//on garde la meme temperature pour le moment
    	return temperature;
    }

}

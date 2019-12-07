package Controller;

import java.util.List;

import Model.Scenario;

public class RecuitStochastique extends RecuitGlobal {
	
	public RecuitStochastique() {
		super();
	}
	
    public void resoudreFoncObjectif(Scenario scenario) {
    	//fonction de resolution par recuit
    		//variables necessaires pour le recuit
    	int k = 0;
    	int kmax = 10;
    	float p = 0.0f;
    	double newCout = 0.0f;
    	boolean acceptedSolution = false;
    	Scenario minScenario = scenario;
    	double cout = scenario.cout();
    	//boucle du recuit
    	//while(!endCondition) {    		//condition sur la temperature
    		while(k<kmax){        		//iteration maximale du recuit pris a n**2 habituellement
        		double coutVoisin = scenario.voisinage(); //voisin du scenario et cout de mis en place
        		newCout = scenario.cout() + coutVoisin; //calcul du cout pour nouveau scenario 
    													//avec cout de mise en place
        		
        		if(fonctionObjectif.betterSolution(cout, newCout)) {
        			//scenario donne meilleur solution
        			minScenario = scenario;   
        			cout = newCout;
        		}
        		else {
        			//scneario donne moins bonne solution
        			p = (float) Math.random();
        			if(p <= Math.exp((newCout-cout)/temperature)) { // on decide de garder cette solution
            			minScenario = scenario;
            			cout = newCout;
        			}
        		}
        		k++;
        	}
			temperature = foncDecroissante();
			this.valeurFoncObjectif = cout;
			fonctionObjectif.meilleureSolution = this.valeurFoncObjectif;
    	//}
    	
    }

    public float foncDecroissante() {
    	//on garde la meme temperature pour le moment
    	return temperature;
    }

}

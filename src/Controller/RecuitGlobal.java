package Controller;

import java.util.List;

import Model.Scenario;
import Model.Trajet;

public class RecuitGlobal {
    public int valeurFoncObjectif;

    public float temperature;

    public float delta;

    public FonctionObjectif fonctionObjectif;

    public ApproxGenerique approxGenerique;

    public void resoudreFonctObectif(Scenario scenario) {
    	//boucle sur les différents recuits pour trouver une solution
    }

    public int calculCout(Scenario minScenario) {
        // TODO Auto-generated return 
    	int rslt = 0;
    	for(int i = 0; i < this.fonctionObjectif.coefficients.length; i++) {
    		rslt += Float.parseFloat(this.fonctionObjectif.coefficients[i]) * this.fonctionObjectif.parametre[i];
    	}
        return rslt;
    }

    public float foncDecroissante() {
    	return 0.0f;
    }

    public boolean estAcceptee1() {
    	
        return false;
    }

}

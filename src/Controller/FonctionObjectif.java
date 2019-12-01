package Controller;

import java.util.ArrayList;
import java.util.List;

import Model.*;

public class FonctionObjectif {
    public int meilleureSolution;

    public int[] parametre;

    public String[] coefficients;

    public List<Trajet> listTrajet = new ArrayList<Trajet> ();
    
    public float getValue(Scenario scenario) {
    	return 0.0f;
    }
}

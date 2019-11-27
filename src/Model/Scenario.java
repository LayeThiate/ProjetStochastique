package Model;

import java.util.ArrayList;
import java.util.List;

public class Scenario {
    public int id;

    public float proba;

    public List<Station> listStation = new ArrayList<Station> ();

    public List<Trajet> listTrajet = new ArrayList<Trajet> ();

    public void run() {
    }

    public int cout() {
        // TODO Auto-generated return
        return 0;
    }

    public int coutStocha() {
        // TODO Auto-generated return
        return 0;
    }

    public Scenario voisinage() {
        // TODO Auto-generated return
        return null;
    }

}

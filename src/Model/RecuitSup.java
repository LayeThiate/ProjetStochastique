package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import Controller.*;

public class RecuitSup {
    public int taille;
    
    public ApproxGenerique approxGenerique;

    public List<Scenario> listScenario = new ArrayList<Scenario> ();
    
    public List<Double> listSolution = new ArrayList<Double>();
    
    public FonctionObjectif fonctionObjectif;
    
    public Data data;

    public void resolution() {
    	//recuit 
    	List<RecuitStochastique> listRecuit = new ArrayList<RecuitStochastique>();
    	for(int i = 0 ; i < taille; i++) {
    		RecuitStochastique recuitStochastique = new RecuitStochastique();
    		recuitStochastique.fonctionObjectif = this.fonctionObjectif;
    		recuitStochastique.resoudreFoncObjectif(listScenario.get(0));
    		Double solution = recuitStochastique.fonctionObjectif.meilleureSolution;
    		listSolution.add(solution);
    	}
    }

    public void genererScenario(String filePath) throws Exception {
    	//parser les données entrées par l'utilisateur et créer les objets
    	data = Data.getInstance();
    	Data.fillData(data,filePath);
    	int numScenario = data.getNumScenario();
    	int numStation = data.getNumStation();
    	if(numScenario <= 0) {
    		System.err.println("MODEL ERROR: Incorrect number of scenarios, check CSV file");
    		return;
    	}
    	if(numStation <= 0) {
    		System.err.println("MODEL ERROR: Incorrect number of stations, check CSV file");
    		return;
    	}
    	this.taille = numScenario;
    	for(int i = 0; i < numScenario; i++) {
    		Scenario scenario =  new Scenario();
    		listScenario.add(scenario);
    	}
    	for(int i = 0; i < numScenario; i++) {
    		Scenario scenario = listScenario.get(i);
    		for(int j = 0; j < numStation; j++) {
    			Station station = new Station();
    			station.id = data.getId()[j];
    			station.capaMax = (int) data.getK()[j];
    			station.coutAjout = data.getC()[j];
    			station.coutManque = data.getV()[j];
    			station.coutTempsPerdu = data.getW()[j];
    			station.disponible = data.getDisponible()[j];
    			station.nbPlaceDispo = data.getNbPlaceDispo()[j];
    			station.latitude = data.getLatitude()[j];
    			station.longitude = data.getLongitude()[j];
    			for(int k = 0; k < numStation; k++) {
    				int demande = data.getEps()[j][k][i];
        			station.demande.add(demande);
    			}
    			scenario.listStation.add(station);
    		}
    	}
    	fonctionObjectif = new FonctionObjectif();
    	this.fonctionObjectif.objectif = data.getObjectif();
    }

    public double getSolution() {
    	if(listSolution.size() <= 0) {
    		System.err.println("MODEL ERROR: No solutions found in solution list");
    		return 0.0;
    	}
    	double meilleureSolution = listSolution.get(0);
    	for(int i = 1 ; i < taille; i++) {
    		if(fonctionObjectif.betterSolution(meilleureSolution, listSolution.get(i))) {
    			meilleureSolution = listSolution.get(i);
    		}
    	}
    	return meilleureSolution;
    }

    public double getMoyenne() {
    	if(listSolution.size() <= 0) {
    		System.err.println("MODEL ERROR: No solutions found in solution list");
    		return 0.0;
    	}
    	double rslt = 0;
    	for(int i =0 ; i<listSolution.size() ; i++)
    		rslt += listSolution.get(i);
    	return rslt/listSolution.size();
    }
    
    //fonction pour renvoyer l'affichage de la solution a l'UI
    public String replyToUI(){
    	//lancer la resolution
    	final ExecutorService executor = Executors.newFixedThreadPool(4);
    	final Future<?> future = executor.submit(() -> {
    		this.resolution();
    	});
    	try {
			future.get((long) data.timeLimit, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//reponse a l'UI sous forme de string
    		//solution optimale
    	String rslt ="Solution optimale: " + getSolution();
    		//moyenne des solutions
    	rslt += "\nMoyenne des solutions: " + getMoyenne();
    	return rslt;
    }

    public static void main(String[] args) throws Exception {
    	//TEST UNIT
    	String filePath = "C:\\Users\\Candassamy\\Documents\\PolytechParisSud\\ET5\\S9\\Stochastique\\ProjetStochastique\\test.csv";
    	System.out.println("Generation des scenarios");
    	RecuitSup recuitSup = new RecuitSup();
    	recuitSup.genererScenario(filePath);
    	System.out.println("Generation des scenarios: DONE");
    	System.out.println(recuitSup.data.toString());
    	System.out.println("Resolution du recuit");
		//recuitSup.resolution();
		System.out.println(recuitSup.replyToUI());
    	System.out.println("Resolution du recuit: DONE");
    }
}

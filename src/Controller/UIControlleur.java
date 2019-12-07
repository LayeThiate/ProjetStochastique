package Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

//import Model.Scenario;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Callback;

public class UIControlleur {
    //public RecuitGlobal recuitGlobal;
    //public FonctionObjectif fonctionObjectif;
    //public Scenario scenario;
	
	@FXML
	private AnchorPane anchorPane;
	
	@FXML
	private TextField textFieldFctObj;
	
	@FXML
	private TextArea textAreaSubConstraints;
	
	@FXML
	private TextArea textAreaBoundaries;
	
	@FXML
	private TextArea textAreaVariables;
	
	@FXML
	private RadioButton btnSolveDeterm;
	
	@FXML
	private RadioButton btnSolveStocha;
	
	@FXML
	private Slider sliderTimeMinutes;
	
	@FXML
	private TextField fileNameCSV;
	
	@FXML
	private Button btnLaunchSolve;
	
	@FXML
	private ScrollPane scrollPane;
	
	@FXML
	private AnchorPane subAnchorPane;
	
	@FXML
	private Label lblReadTime;
	
	@FXML
	private Label lblSolveTime;
	
	@FXML
	private Label lblSolStatus;
	
	@FXML
	private Label lblOptiIntSol;
	
	@FXML
	private Label lblAvgSol;
	
	@FXML
	private Label lblNbIterations;

	@FXML
	private TableView<Map.Entry<String,String>> tableView;
	
	@FXML
	private TableColumn columnVarName;
	
	@FXML
	private TableColumn columnVarValue;
		
	/**
	 * attributs pour le traitement
	 */
	final ToggleGroup toggleGroup = new ToggleGroup(); //permet de ne sélectionner qu'un bouton radio à la fois
	private String choixResolution = new String(); //stocke le type de résolution choisie par l'utilisateur
    private String strFileNameCSV = new String();
    private String minutesSliderTime = new String();
    private String fonctionObjectif = new String();
    private ArrayList<String> arrayListSubConstraints = new ArrayList<String>();
    private ArrayList<String> arrayListBoundaries = new ArrayList<String>();
    private ArrayList<String> arrayListVariables = new ArrayList<String>();
    private final String defaultStringReadTime = "Temps de lecture : ";
    private final String defaultStringSolveTime = "Temps de résolution : ";
    private final String defaultStringSolStatus = "Statut de la solution : ";
    private final String defaultStringOptiIntSol = "Solution entière optimale : ";
    private final String defaultAvgSol = "Moyenne des solutions :";
    private final String defaultStringNbIterations = "Itérations : ";
    private double readTime;
    private double solveTime;
    private String solStatus;
    private String optiIntSolAndAvgSol;
    private int nbIterations;    
    
	/**
	 * Constructeur
	 */
	public UIControlleur(){
		
    }	
	
	/**
	 * Initialisaton des boutons et boutons radio
	 */
	@FXML
	private void initButtons(){
		//boutons radio
		btnSolveDeterm.setToggleGroup(toggleGroup);
		btnSolveDeterm.setSelected(true); //on met sur résolution déterministe par défaut
		btnSolveStocha.setToggleGroup(toggleGroup);
		choixResolution = "deterministe";

		btnSolveDeterm.setOnAction((event)-> {
			choixResolution = "deterministe";
         });
		
		btnSolveStocha.setOnAction((event)-> {
			choixResolution = "stochastique";
         });
		
		btnLaunchSolve.setOnAction((event)-> {
			//on remet juste l'affichage des labels résultats par défaut au cas où 
			resetLabelResults();
			tableView.setItems(null); //et on reset l'affichage du tableau
			
			//recuperation de toutes les donnees
			//si aucun champ n'était vide (false), on peut lancer la résolution
			if(getFonctionObj() && getSubConstraints() && getBoundaries()
					&& getAllVariables() && getSliderTime() && getFileNameCSV()){
				
				//si choix == stochastique, résolution avec le recuit sup
				if(choixResolution == "stochastique"){
					RecuitSup recuitSup = new RecuitSup();
					try {
						recuitSup.genererScenario(strFileNameCSV);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					//récupération des résultats et mise à jour de l'IHM
					optiIntSolAndAvgSol = recuitSup.replyToUI();
					lblOptiIntSol.setText(optiIntSolAndAvgSol.split("\n")[0]);
					lblAvgSol.setText(optiIntSolAndAvgSol.split("\n")[1]);
				}
				else{ // choix == déterministe
					columnVarName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
			            @Override
			            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
			                // ce callback retourne la propriete pour une seule cellule, pas de boucle possible ici
			                // on utilise cle en premiere colonne
			                return new SimpleStringProperty(p.getValue().getKey());
			            }
			        });
					
			        columnVarValue.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<String, String>, String>, ObservableValue<String>>() {
			            @Override
			            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<String, String>, String> p) {
			                // on utilise valeur en seconde colonne
			                return new SimpleStringProperty(p.getValue().getValue());
			            }
			        });

			        //Hashmap hardcodée de test, à remplacer par la vraie HashMap retournée
			        HashMap<String, String> hmMockData = new HashMap<String, String>();
			        hmMockData.put("x1", "999");
			        hmMockData.put("x2", "666");
			        hmMockData.put("x3", "333");
			        hmMockData.put("x4", "444");
			        hmMockData.put("x5", "555");
			        hmMockData.put("x6", "222");
			        
			        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(hmMockData.entrySet());
			        tableView.setItems(items);
					tableView.getColumns().setAll(columnVarName, columnVarValue);
				}
			} // fin if tous les champs ont bien été récupéré
			//A la fin, on nettoie après chaque clic sur le bouton de résolution
			cleanseArrays();
         });
	} 
	
	/**
	 * Initialisation
	 */
	@FXML
	public void initialize() {
		initButtons();
		//System.out.println("Init done successfully");
	}
	
	boolean getFonctionObj(){
		if(!textFieldFctObj.getText().isEmpty()){
			fonctionObjectif = textFieldFctObj.getText();
			return true;
		}
		else
			return false;
	}
	
	boolean getSubConstraints(){
		if(!textAreaSubConstraints.getText().isEmpty()){
			for(String line : textAreaSubConstraints.getText().split("\\n")) 
				arrayListSubConstraints.add(line);
			return true;
		}
		else 
			return false;
	}
	
	boolean getBoundaries(){
		if(!textAreaBoundaries.getText().isEmpty()){
			for(String line : textAreaBoundaries.getText().split("\\n")) 
				arrayListBoundaries.add(line);
			return true;
		}
		else 
			return false;
	}
	
	boolean getAllVariables(){
		if(!textAreaVariables.getText().isEmpty()){
			for(String line : textAreaVariables.getText().split("\\n")) 
				arrayListVariables.add(line);
			return true;
		}
		else 
			return false;
	}
	
	boolean getSliderTime(){
		double timeMinutes = sliderTimeMinutes.getValue();
		if(timeMinutes >= 1.0){
			int minutesEntieres = (int) timeMinutes;
			minutesSliderTime = Integer.toString(minutesEntieres);
			return true;
		}
		else
			return false;
	}
	
	boolean getFileNameCSV(){
		if(!fileNameCSV.getText().isEmpty()){
			strFileNameCSV = fileNameCSV.getText();
			return true;
		}
		else 
			return false;
	}
	
	//test d'affichage pour voir qu'on a bien tout récupéré
	public void displayAllData(){
		System.out.println("Choix: " + choixResolution);
		System.out.println("SliderTime: " + minutesSliderTime);
		System.out.println("Nom csv: " + strFileNameCSV);
		System.out.println("Fonction Objectif: " + fonctionObjectif);
		System.out.println("SubConstraints:");
		for(String s : arrayListSubConstraints)
			System.out.println(s);
		System.out.println("Bounds:");
		for(String s : arrayListBoundaries)
			System.out.println(s);
		System.out.println("Variables:");
		for(String s : arrayListVariables)
			System.out.println(s);
	}
	
	//fonction à appeler après chaque clic du bouton de résolution
	public void resetLabelResults(){
		lblReadTime.setText(defaultStringReadTime);
		lblSolveTime.setText(defaultStringSolveTime);
		lblSolStatus.setText(defaultStringSolStatus);
		lblOptiIntSol.setText(defaultStringOptiIntSol);
		lblNbIterations.setText(defaultStringNbIterations);
		lblAvgSol.setText(defaultAvgSol);
	}
	
	//fonction à appeler après chaque clic du bouton de résolution
	public void cleanseArrays(){
		arrayListSubConstraints.clear();
		arrayListBoundaries.clear();
		arrayListVariables.clear();
	}
	
	//getters public pour les autres classes
	public String getChoixResolution(){
		return this.choixResolution;
	}
	
	public String getStrFileNameCSV(){
		return this.strFileNameCSV;
	}
	
	public String getMinutesSliderTime(){
		return this.minutesSliderTime;
	}
	
	public String getFonctionObjectif(){
		return this.fonctionObjectif;
	}
	
	public ArrayList<String> getArrayListSubConstraints(){
		return this.arrayListSubConstraints;
	}
	
	public ArrayList<String> getArrayListBoundaries(){
		return this.arrayListBoundaries;
	}
	
	public ArrayList<String> getArrayListVariables(){
		return this.arrayListVariables;
	}

}
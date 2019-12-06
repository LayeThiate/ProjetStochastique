package Controller;

import java.util.ArrayList;
import java.util.HashMap;

//import Model.Scenario;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

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
	private Label lblNbIterations;

	@FXML
	private TableView tableView;
	
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
			//recuperation de toutes les donnees
			//si aucun champ n'était vide (false), on peut lancer la résolution
			if(getFonctionObj() && getSubConstraints() && getBoundaries()
					&& getAllVariables() && getSliderTime() && getFileNameCSV()){
				//envoi vers le recuit global
				//TODO
				
				//test d'affichage pour voir qu'on a bien tout récupéré
				//displayAllData();
			}
			//sinon on ne fait rien, pour ne pas créer d'erreurs dans le programme
			//A appeler après chaque clic sur le bouton de résolution
			cleanseArrays();
         });
	} 
	
	/**
	 * Initialisation
	 */
	@FXML
	public void initialize() {
		initButtons();
		System.out.println("Init done successfully");
	}
	
	boolean getFonctionObj(){
		if(!textFieldFctObj.getText().isEmpty()){
			fonctionObjectif = textFieldFctObj.getText();
			//System.out.println("getFonctionObjectif OK");
			return true;
		}
		else
			return false;
	}
	
	boolean getSubConstraints(){
		if(!textAreaSubConstraints.getText().isEmpty()){
			for(String line : textAreaSubConstraints.getText().split("\\n")) 
				arrayListSubConstraints.add(line);
			//System.out.println("getSubConstraints OK");
			return true;
		}
		else 
			return false;
	}
	
	boolean getBoundaries(){
		if(!textAreaBoundaries.getText().isEmpty()){
			for(String line : textAreaBoundaries.getText().split("\\n")) 
				arrayListBoundaries.add(line);
			//System.out.println("getBoundaries OK");
			return true;
		}
		else 
			return false;
	}
	
	boolean getAllVariables(){
		if(!textAreaVariables.getText().isEmpty()){
			for(String line : textAreaVariables.getText().split("\\n")) 
				arrayListVariables.add(line);
			//System.out.println("getAllVariables OK");
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
			//System.out.println("getSliderTime OK");
			return true;
		}
		else
			return false;
	}
	
	boolean getFileNameCSV(){
		if(!fileNameCSV.getText().isEmpty()){
			strFileNameCSV = fileNameCSV.getText();
			//System.out.println("getFileNameCSV OK");
			return true;
		}
		else 
			return false;
	}
	
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
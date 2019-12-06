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

		btnSolveDeterm.setOnAction((event)-> {
			choixResolution = "deterministe";
         });
		
		btnSolveStocha.setOnAction((event)-> {
			choixResolution = "stochastique";
         });
		
		btnLaunchSolve.setOnAction((event)-> {
			//recuperation de toutes les donnees
			//si aucun champ n'était vide (false), on peut lancer la résolution
			if(getFonctionObjectif() && getSubConstraints() && getBoundaries()
					&& getAllVariables() && getSliderTime() && getFileNameCSV()){
				//envoi vers le recuit global
				//TODO
				
				// test d'affichage pour voir qu'on a bien tout récupéré
				displayAllData();
			}
			//sinon on ne fait rien, pour ne pas créer d'erreurs dans le programme

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
	
	public boolean getFonctionObjectif(){
		if(textFieldFctObj.getText().isEmpty()){
			fonctionObjectif = textFieldFctObj.getText();
			return true;
		}
		else
			return false;
	}
	
	public boolean getSubConstraints(){
		if(!textAreaSubConstraints.getText().isEmpty()){
			for(String line : textAreaSubConstraints.getText().split("\\n")) 
				arrayListSubConstraints.add(line);
			return true;
		}
		else 
			return false;
	}
	
	public boolean getBoundaries(){
		if(!textAreaBoundaries.getText().isEmpty()){
			for(String line : textAreaSubConstraints.getText().split("\\n")) 
				arrayListBoundaries.add(line);
			return true;
		}
		else 
			return false;
	}
	
	public boolean getAllVariables(){
		if(!textAreaVariables.getText().isEmpty()){
			for(String line : textAreaSubConstraints.getText().split("\\n")) 
				arrayListVariables.add(line);
			return true;
		}
		else 
			return false;
	}
	
	public boolean getSliderTime(){
		double timeMinutes = sliderTimeMinutes.getValue();
		if(timeMinutes >= 1.0){
			minutesSliderTime = Double.toString(timeMinutes);
			return true;
		}
		else
			return false;
	}
	
	public boolean getFileNameCSV(){
		if(!fileNameCSV.getText().isEmpty()){
			strFileNameCSV = fileNameCSV.getText();
			return true;
		}
		else 
			return false;
	}
	
	public void displayAllData(){
		System.out.println("Choix: " + choixResolution);
		System.out.println("Nom csv: " + strFileNameCSV);
		System.out.println("Fonction Objectif: " + fonctionObjectif);
		System.out.println("SubConstrainst:");
		for(String s : arrayListSubConstraints)
			System.out.println(s);
		System.out.println("Bounds:");
		for(String s : arrayListBoundaries)
			System.out.println(s);
		System.out.println("Variables:");
		for(String s : arrayListVariables)
			System.out.println(s);
	}

	

}
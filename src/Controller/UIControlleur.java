package Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Model.Data;
import Model.RecuitSup;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class UIControlleur {
	
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
	final ToggleGroup toggleGroup = new ToggleGroup(); //permet de ne s�lectionner qu'un bouton radio � la fois
	private String choixResolution = new String(); //stocke le type de r�solution choisie par l'utilisateur
    private String strFileNameCSV = new String();
    private String minutesSliderTime = new String();
    private String fonctionObjectif = new String();
    private ArrayList<String> arrayListSubConstraints = new ArrayList<String>();
    private ArrayList<String> arrayListBoundaries = new ArrayList<String>();
    private ArrayList<String> arrayListVariables = new ArrayList<String>();
    private final String defaultStringReadTime = "Temps de lecture : ";
    private final String defaultStringSolveTime = "Temps de r�solution : ";
    private final String defaultStringSolStatus = "Statut de la solution : ";
    private final String defaultStringOptiIntSol = "Solution enti�re optimale : ";
    private final String defaultAvgSol = "Moyenne des solutions :";
    private final String defaultStringNbIterations = "It�rations : ";
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
		btnSolveDeterm.setSelected(true); //on met sur r�solution d�terministe par d�faut
		btnSolveStocha.setToggleGroup(toggleGroup);
		choixResolution = "deterministe";

		btnSolveDeterm.setOnAction((event)-> {
			choixResolution = "deterministe";
         });
		
		btnSolveStocha.setOnAction((event)-> {
			choixResolution = "stochastique";
         });
		
		btnLaunchSolve.setOnAction((event)-> {
			//on remet juste l'affichage des labels r�sultats par d�faut au cas o� 
			resetLabelResults();
			tableView.setItems(null); //et on reset l'affichage du tableau
			
			//Lancer le cplex sur le modele du projet
			if(choixResolution.equals("deterministe") && getFileNameCSV()) {
				//Initialisation des donn�es
				try {
					Data.init(strFileNameCSV);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				ResolutionCplex cplex = new ResolutionCplex();
				cplex.solve();
				
				optiIntSolAndAvgSol = cplex.getResult();
				String [] res = optiIntSolAndAvgSol.split("\n");
				lblSolveTime.setText(res[0]);
				lblReadTime.setText(res[1]);
				lblSolStatus.setText(res[2]);
				lblOptiIntSol.setText(res[3]);
				lblNbIterations.setText(res[4]);
				
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
				
				Map<String, String> varSol = cplex.getSolutions();
				ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(varSol.entrySet());
		        tableView.setItems(items);
				tableView.getColumns().setAll(columnVarName, columnVarValue);
			}
			else if((choixResolution == "stochastique") && getFonctionObj() && getSubConstraints() && getBoundaries()
					&& getAllVariables() && getSliderTime() && getFileNameCSV()){
				//recuperation de toutes les donnees
				//si aucun champ n'�tait vide (false), on peut lancer la r�solution
				//si choix == stochastique, r�solution avec le recuit sup
	
				//Initialisation des donn�es
				try {
					Data.init(strFileNameCSV);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Data.getInstance().setBoundaries(arrayListBoundaries);
				Data.getInstance().setObjFunc(fonctionObjectif);
				Data.getInstance().setVariables(getArrayListVariables());
				Data.getInstance().setSubConstraints(getArrayListSubConstraints());
				
				RecuitSup recuitSup = new RecuitSup();
				try {
					recuitSup.genererScenario(strFileNameCSV);
				} catch (Exception e) {
					e.printStackTrace();
				}
				recuitSup.data.timeLimit = Double.parseDouble(minutesSliderTime);
				//r�cup�ration des r�sultats et mise � jour de l'IHM
				optiIntSolAndAvgSol = recuitSup.replyToUI();
				lblOptiIntSol.setText(optiIntSolAndAvgSol.split("\n")[0]);
				lblAvgSol.setText(optiIntSolAndAvgSol.split("\n")[1]);
				
			}// fin if tous les champs ont bien �t� r�cup�r�
			//A la fin, on nettoie apr�s chaque clic sur le bouton de r�solution
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
	
	//test d'affichage pour voir qu'on a bien tout r�cup�r�
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
	
	//fonction � appeler apr�s chaque clic du bouton de r�solution
	public void resetLabelResults(){
		lblReadTime.setText(defaultStringReadTime);
		lblSolveTime.setText(defaultStringSolveTime);
		lblSolStatus.setText(defaultStringSolStatus);
		lblOptiIntSol.setText(defaultStringOptiIntSol);
		lblNbIterations.setText(defaultStringNbIterations);
		lblAvgSol.setText(defaultAvgSol);
	}
	
	//fonction � appeler apr�s chaque clic du bouton de r�solution
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
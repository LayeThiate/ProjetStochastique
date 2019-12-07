package Model;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * Sigleton class contening the data of parameters
 *
 */
public class Data {
	private String objectif = "minimize";
	private int numStation;
	private int numScenario = 1;
	private String []description;
	private String []code;
	private int []disponible;
	private int []nbPlaceDispo;
	private int []nbVeloInitial;
	private float []latitude;
	private float []longitude;
	private int []id;
	private double []c;				//Acquisition cost of a bike in all station
	private double []v; 				//missing cost of bike in a station
	private double []w; 				//lost time cost by a user to find a bike in a station
	private double []k;				//capacity in number of bike for a station
	private int[][][] eps = new int[numStation][numStation][numScenario]; // stochastic parameters
	public double timeLimit = 10.0;
	
	private static Data instance = null;
	
	//Default constructor
	private Data() {
		super();
		for (int i = 0; i < numStation; i++) {
			c[i] = Math.random() * 50;
			v[i] = Math.random() * 50;
			w[i] = Math.random() * 50;
			k[i] = Math.random() * 50;
		}

		for (int i = 0; i < numStation; i++) {
			for (int j = 0; j < numStation; j++) {
				for (int s = 0; s < numScenario; s++) {
					eps[i][j][s] = 0;
				}
			}
		}
	}
	
	public static Data getInstance() {
		if(instance == null) {
			instance = new Data();
		}
		return instance;
	}
	
	/**
	 * Init the data whith the csv file
	 * 
	 * @param file
	 * @throws Exception
	 */
	public static void init(String file) throws Exception {
        fillData(Data.getInstance(), file);
    }
	
	
	/**
	 * Fill the elements of data
	 * put each element whith it value corresponding
	 * 
	 * @param data
	 * @param file
	 * @throws Exception
	 */
	public static void fillData(Data data, String file) throws Exception {
		List<String[]> list = parseCSV(file);
		data.numStation = list.size();
		
		data.description = new String[data.numStation];
		data.code = new String[data.numStation];
		data.id = new int[data.numStation];
		data.k = new double[data.numStation];
		data.disponible = new int[data.numStation];
		data.nbPlaceDispo = new int[data.numStation];
		data.nbVeloInitial = new int[data.numStation];
		data.c = new double[data.numStation];
		data.v =  new double[data.numStation];
		data.w =  new double[data.numStation];
		data.latitude = new float[data.numStation];
		data.longitude = new float[data.numStation];
		data.eps = new int[data.numStation][data.numStation][data.numScenario];
		//description;code;id;capaMax;disponible;nbPlaceDispo;nbVeloInitial;
		//coutAjout;coutManque;coutTempsPerdu;latitude;longitude
		String []str ;

		//For each station, assign its values
		for(int i=0; i<list.size(); i++) {
			
			str = list.get(i);
			
			data.description[i] = str[0];
			data.code[i] = str[1];
			data.id[i] = new Integer(str[2]);
			data.k[i] = new Integer(str[3]);
			data.disponible[i] = new Integer(str[4]);
			data.nbPlaceDispo[i] = new Integer(str[5]);
			data.nbVeloInitial[i] = new Integer(str[6]);
			data.c[i] = new Double(str[7]);
			data.v[i] = new Double(str[8]);
			data.w[i] = new Double(str[9]);
			data.latitude[i] = new Float(str[10]);
			data.longitude[i] = new Float(str[11]);
			
			for(int j=0; j<list.size(); j++){
				data.eps[i][j][0] = new Integer(str[12+j]);
			}
		}
		/*
		data.eps = new double[data.numStation][data.numStation][data.numScenario];
		//init the scenario
		for (int i = 0; i < data.numStation; i++) {
			for (int j = 0; j < data.numStation; j++) {
				for (int s = 0; s < data.numScenario; s++) {
					data.eps[i][j][s] = Math.random() * 25; //TODO: change to data from csv file
				}
			}
		}
		*/
	}
	
	/**
	 * parse the csv file and return list of string table whose each element represents a line 
	 * 
	 * @param pathFile
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> parseCSV(String pathFile) throws Exception {
		Reader reader = new FileReader(pathFile);
		
	    List<String[]> list = new ArrayList<>();
	    
	    CSVParser parser = new CSVParserBuilder()
	    	    .withSeparator(';')
	    	    .withIgnoreQuotations(true)
	    	    .build();
	    	 
	    	CSVReader csvReader = new CSVReaderBuilder(reader)
	    	    .withSkipLines(1) // ignore the first line, header
	    	    .withCSVParser(parser)
	    	    .build();
	    	
	    String[] line;
	    while ((line = csvReader.readNext()) != null) {
	        list.add(line);
	    }
	    
	    reader.close();
	    csvReader.close();
	    return list;
	}
	
	public String getObjectif() {
		return objectif;
	}
	
	public void setObjectif(String o) {
		objectif = o;
	}

	public int getNumStation() {
		return numStation;
	}

	public void setNumStation(int numStation) {
		this.numStation = numStation;
	}

	public int getNumScenario() {
		return numScenario;
	}

	public void setNumScenario(int numScenario) {
		this.numScenario = numScenario;
	}

	public double[] getC() {
		return c;
	}

	public void setC(double[] c) {
		this.c = c;
	}

	public double[] getV() {
		return v;
	}

	public void setV(double[] v) {
		this.v = v;
	}

	public double[] getW() {
		return w;
	}

	public void setW(double[] w) {
		this.w = w;
	}

	public double[] getK() {
		return k;
	}

	public void setK(double[] k) {
		this.k = k;
	}

	public int[][][] getEps() {
		return eps;
	}

	public void setEps(int[][][] eps) {
		this.eps = eps;
	}

	public String[] getDescription() {
		return description;
	}

	public void setDescription(String[] description) {
		this.description = description;
	}

	public String[] getCode() {
		return code;
	}

	public void setCode(String[] code) {
		this.code = code;
	}

	public int[] getDisponible() {
		return disponible;
	}

	public void setDisponible(int[] disponible) {
		this.disponible = disponible;
	}

	public int[] getNbPlaceDispo() {
		return nbPlaceDispo;
	}

	public void setNbPlaceDispo(int[] nbPlaceDispo) {
		this.nbPlaceDispo = nbPlaceDispo;
	}

	public int[] getNbVeloInitial() {
		return nbVeloInitial;
	}

	public void setNbVeloInitial(int[] nbVeloInitial) {
		this.nbVeloInitial = nbVeloInitial;
	}

	public float[] getLatitude() {
		return latitude;
	}

	public void setLatitude(float[] latitude) {
		this.latitude = latitude;
	}

	public float[] getLongitude() {
		return longitude;
	}

	public void setLongitude(float[] longitude) {
		this.longitude = longitude;
	}

	public int[] getId() {
		return id;
	}

	public void setId(int[] id) {
		this.id = id;
	}

	@Override
	public String toString() {
		String rslt = "Data [numStation=" + numStation + ", numScenario=" + numScenario + ", description="
				+ Arrays.toString(description) + ", code=" + Arrays.toString(code) + ", disponible="
				+ Arrays.toString(disponible) + ", nbPlaceDispo=" + Arrays.toString(nbPlaceDispo) + ", nbVeloInitial="
				+ Arrays.toString(nbVeloInitial) + ", latitude=" + Arrays.toString(latitude) + ", longitude="
				+ Arrays.toString(longitude) + ", id=" + Arrays.toString(id) + ", c=" + Arrays.toString(c) + ", v="
				+ Arrays.toString(v) + ", w=" + Arrays.toString(w) + ", k=" + Arrays.toString(k) + ", eps="
				/*+ Arrays.toString(eps) + "]"*/;
		for(int i = 0; i < numStation; i++) {
			rslt += "[";
			for(int j = 0; j < numStation - 1; j++) {
				rslt += eps[i][j][0] + ", ";
			}
			rslt += rslt += eps[i][numStation-1][0] + "]";
		}
		return rslt;
	}

}

package Model;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import ilog.concert.IloException;

public class Data {
	private int numStation;
	private int numScenario;
	private double []c;				//Acquisition cost of a bike in all station
	private double []v; 				//missing cost of bike in a station
	private double []w; 				//lost time cost by a user to find a bike in a station
	private double []k;				//capacity in number of bike for a station
	private double[][][] eps = new double[numStation][numStation][numScenario]; // stochastic parameters
	
	private static Data instance = null;
	private String fileName;
	
	//Default constructor
	public Data() {
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
					eps[i][j][s] = Math.random() * 25;
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
	
	public static void init(String file) throws Exception {
        fillData(Data.getInstance(), file);
    }
	
	private static void fillData(Data data, String file) throws Exception {
		List<String[]> list = parseCSV(file);
		
		String []str ;
		for(int i=0; i<list.size(); i++) {
			str = list.get(i);
			data.c[i] = new Double(str[i]);
		}
	}
	
	public static List<String[]> parseCSV(String pathFile) throws Exception {
		System.out.println(pathFile);
		Reader reader = new FileReader(pathFile);
		
	    List<String[]> list = new ArrayList<>();
	    
	    CSVParser parser = new CSVParserBuilder()
	    	    .withSeparator(';')
	    	    .withIgnoreQuotations(true)
	    	    .build();
	    	 
	    	CSVReader csvReader = new CSVReaderBuilder(reader)
	    	    .withSkipLines(1)
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

	public double[][][] getEps() {
		return eps;
	}

	public void setEps(double[][][] eps) {
		this.eps = eps;
	}

}

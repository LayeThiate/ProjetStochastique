package view;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class ResolutionCplex {
	public static void main(String []args){

		int numStation = 5;
		int numScenario = 5;
		
		IloNumVar [][]varSol = new IloNumVar[6][];

		// Data
		// deterministic parameters
		double[] c = new double[numStation]; // Acquisition cost of a bike in all station
		double[] v = new double[numStation]; // vi
		double[] w = new double[numStation]; // wi
		double[] k = new double[numStation]; // ki

		for (int i = 0; i < numStation; i++) {
			c[i] = Math.random() * 50;
			v[i] = Math.random() * 50;
			w[i] = Math.random() * 50;
			k[i] = Math.random() * 50;
		}

		// stochastic parameters
		double[][][] eps = new double[numStation][numStation][numScenario];
		for (int i = 0; i < numStation; i++) {
			for (int j = 0; j < numStation; j++) {
				for (int s = 0; s < numScenario; s++) {
					eps[i][j][s] = Math.random() * 25;
				}
			}
		}

		// Model
		IloCplex cplex;
		IloNumVar[] x = new IloNumVar[numStation]; // first level variable, number of bike assigned to a station*

		// Second level variable
		// Number of bike rent to go from a station to a station for a scenario
		IloNumVar[][][] beta = new IloNumVar[numStation][numStation][numScenario];
		IloNumVar[][] iPlus = new IloNumVar[numStation][numScenario]; 				// I^+(i,s)
		IloNumVar[][][] iMinus = new IloNumVar[numStation][numStation][numScenario];// I^-(i,j,s)
		IloNumVar[][] oPlus = new IloNumVar[numStation][numScenario]; 				// O^+(i,s)
		IloNumVar[][] oMinus = new IloNumVar[numStation][numScenario];// O^-(i,s)

		try {
			cplex = new IloCplex();

			// Variables
			// xi
			for (int i = 0; i < numStation; i++) {
				x[i] = cplex.numVar(0, Double.MAX_VALUE, "x"+i); 
				varSol[0] = x;
			}
			// Beta(i,j,s)
			for (int i = 0; i < numStation; i++) {
				for (int j = 0; j < numStation; j++) {
					for (int s = 0; s < numScenario; s++) {
						beta[i][j][s] = cplex.numVar(0, Double.MAX_VALUE, "beta("+i+","+j+","+s+")"); 
					}
					varSol[1] = beta[i][j];
				}
			}
			// I^+(i,s)
			for (int i = 0; i < numStation; i++) {
				for (int s = 0; s < numScenario; s++) {
					iPlus[i][s] =  cplex.numVar(0, Double.MAX_VALUE, "Iplus("+i+","+s+")");
				}
				varSol[2] = iPlus[i];
			}
			// I^-(i,j,s)
			for (int i = 0; i < numStation; i++) {
				for (int j = 0; j < numStation; j++) {
					for (int s = 0; s < numScenario; s++) {
						iMinus[i][j][s] = cplex.numVar(0, Double.MAX_VALUE, "Iminus("+i+","+j+","+s+")");
					}
					varSol[3] = iMinus[i][j];
				}
			}
			// O^+(i,s)
			for (int i = 0; i < numStation; i++) {
				for (int s = 0; s < numScenario; s++) {
//					for(int j=0; j<numScenario; j++) {
//					}
					oPlus[i][s] = cplex.numVar(0, Double.MAX_VALUE, "Oplus("+i+","+s+")");
				}
				varSol[4] = oPlus[i];
			}
			// O^-(i,j,s)
			for (int i = 0; i < numStation; i++) {
					for (int s = 0; s < numScenario; s++) {
						oMinus[i][s] = cplex.numVar(0, Double.MAX_VALUE, "Iminus("+i+","+s+")");
					}
					varSol[5] = oMinus[i];
			}

			// Objective
			IloLinearNumExpr exprFirstLevel = cplex.linearNumExpr();
			for (int i = 0; i < numStation; i++) {
				exprFirstLevel.addTerm(x[i], c[i]);
			}
			for(int s=0; s<numScenario; s++) {
				IloLinearNumExpr exprSecond = cplex.linearNumExpr();
				double ps = Math.random();
				for(int i=0; i<numStation; i++) {
					for(int j=0; j<numStation; j++) {
						exprSecond.addTerm(ps*v[i], iMinus[i][j][s]);
					}
					exprSecond.addTerm(ps*w[i], oPlus[i][s]);
				}
				exprFirstLevel.add(exprSecond);
			}
			cplex.addMinimize(exprFirstLevel);
			
			//Constraintes
			//(1a)
			for(int i=0; i<numStation; i++) {
				cplex.addLe(x[i], k[i]);
			}
			//(1b)
			for(int i=0; i<numStation; i++) {
				for(int j=0; j<numStation; j++) {
					for(int s=0; s<numScenario; s++) {
						cplex.addEq(cplex.sum(cplex.prod(1., beta[i][j][s]), cplex.prod(1., iMinus[i][j][s])),
								eps[i][j][s]);
					}
				}
			}
			//(1c)
			for(int i=0; i<numStation; i++) {
				for(int s=0; s<numScenario; s++) {
					IloLinearNumExpr exprI = cplex.linearNumExpr();
					int sum = 0;
					for(int j=0; j<numStation; j++) {
						sum += eps[i][j][s];
						exprI.addTerm(-1., iMinus[i][j][s]);
					}
					exprI.addTerm(1., iPlus[i][s]);
					exprI.addTerm(-1., x[i]);
					cplex.addEq(cplex.sum(exprI, 1), sum);
				}
			}
			//(1d)
			for(int i=0; i<numStation; i++) {
				for(int s=0; s<numScenario; s++) {
					IloLinearNumExpr exprBi = cplex.linearNumExpr();
					IloLinearNumExpr exprBj = cplex.linearNumExpr();
					for(int j=0; j<numStation; j++) {
						exprBi.addTerm(-1., beta[i][j][s]);
						exprBj.addTerm(1., beta[j][i][s]);
						
					}
					IloLinearNumExpr expr = cplex.linearNumExpr();
					expr.addTerm(1., oPlus[i][s]);
					expr.addTerm(-1., oMinus[i][s]);
					expr.addTerm(1., x[i]);
					expr.add(exprBi);
					expr.add(exprBj);
					
					cplex.addEq(cplex.sum(expr, 1), k[i]);
				}
			}
			
			if(cplex.solve()){
				double[][] solVal = new double[6][];
				for(int i=0; i<solVal.length; i++) {
					solVal[i] = cplex.getValues(varSol[i]);
				}
				
				
				cplex.output().println("Solution status = " + cplex.getStatus());
				System.out.println("Solution value = " + cplex.getObjValue());
				
				for(int i=0; i<solVal.length; i++) {
					for(int j=0; j<solVal[i].length; j++) {
						System.out.print( "  " +solVal[i][j]);
					}
					System.out.println();
				}
			}
			cplex.end();
			
			try {
				for(String s : parseCSV("C:\\Users\\abdou\\Documents\\Import Linux"
						+ "\\ET5\\PS\\velib-disponibilite-en-temps-reel.csv").get(0)) {
					System.out.println(s);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (IloException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String[]> parseCSV(String pathFile) throws Exception {
		System.out.println(pathFile);
		Reader reader = new FileReader(pathFile);
//		Reader reader = Files.newBufferedReader(Paths.get(
//			      ClassLoader.getSystemResource(pathFile).toURI()));
		
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
	
}

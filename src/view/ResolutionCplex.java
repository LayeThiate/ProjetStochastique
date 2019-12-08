package view;

import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class ResolutionCplex {

	private Map<String, String> solutions = new HashMap<>();
	private double resolutionTime;
	private double loadTime;
	private double solution;
	private String statut;

	int numStation = 100;
	int numScenario = 100;

	// Data
	// deterministic parameters
	double[] c = new double[numStation]; // Acquisition cost of a bike in all station
	double[] v = new double[numStation]; // vi
	double[] w = new double[numStation]; // wi
	double[] k = new double[numStation]; // ki

	public ResolutionCplex() {
		super();
	}

	public void solve() {

		IloNumVar[][] varSol = new IloNumVar[6][];

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
		IloNumVar[][] iPlus = new IloNumVar[numStation][numScenario]; // I^+(i,s)
		IloNumVar[][][] iMinus = new IloNumVar[numStation][numStation][numScenario];// I^-(i,j,s)
		IloNumVar[][] oPlus = new IloNumVar[numStation][numScenario]; // O^+(i,s)
		IloNumVar[][] oMinus = new IloNumVar[numStation][numScenario];// O^-(i,s)

		try {
			cplex = new IloCplex();

			// Variables
			// xi
			for (int i = 0; i < numStation; i++) {
				x[i] = cplex.numVar(0, Double.MAX_VALUE, "x" + i);
				varSol[0] = x;
			}
			// Beta(i,j,s)
			for (int i = 0; i < numStation; i++) {
				for (int j = 0; j < numStation; j++) {
					for (int s = 0; s < numScenario; s++) {
						beta[i][j][s] = cplex.numVar(0, Double.MAX_VALUE, "beta(" + i + "," + j + "," + s + ")");
					}
					varSol[1] = beta[i][j];
				}
			}
			// I^+(i,s)
			for (int i = 0; i < numStation; i++) {
				for (int s = 0; s < numScenario; s++) {
					iPlus[i][s] = cplex.numVar(0, Double.MAX_VALUE, "Iplus(" + i + "," + s + ")");
				}
				varSol[2] = iPlus[i];
			}
			// I^-(i,j,s)
			for (int i = 0; i < numStation; i++) {
				for (int j = 0; j < numStation; j++) {
					for (int s = 0; s < numScenario; s++) {
						iMinus[i][j][s] = cplex.numVar(0, Double.MAX_VALUE, "Iminus(" + i + "," + j + "," + s + ")");
					}
					varSol[3] = iMinus[i][j];
				}
			}
			// O^+(i,s)
			for (int i = 0; i < numStation; i++) {
				for (int s = 0; s < numScenario; s++) {
//					for(int j=0; j<numScenario; j++) {
//					}
					oPlus[i][s] = cplex.numVar(0, Double.MAX_VALUE, "Oplus(" + i + "," + s + ")");
				}
				varSol[4] = oPlus[i];
			}
			// O^-(i,j,s)
			for (int i = 0; i < numStation; i++) {
				for (int s = 0; s < numScenario; s++) {
					oMinus[i][s] = cplex.numVar(0, Double.MAX_VALUE, "Iminus(" + i + "," + s + ")");
				}
				varSol[5] = oMinus[i];
			}

			// Objective
			IloLinearNumExpr exprFirstLevel = cplex.linearNumExpr();
			for (int i = 0; i < numStation; i++) {
				exprFirstLevel.addTerm(x[i], c[i]);
			}
			for (int s = 0; s < numScenario; s++) {
				IloLinearNumExpr exprSecond = cplex.linearNumExpr();
				double ps = Math.random();
				for (int i = 0; i < numStation; i++) {
					for (int j = 0; j < numStation; j++) {
						exprSecond.addTerm(ps * v[i], iMinus[i][j][s]);
					}
					exprSecond.addTerm(ps * w[i], oPlus[i][s]);
				}
				exprFirstLevel.add(exprSecond);
			}
			cplex.addMinimize(exprFirstLevel);

			// Constraintes
			// (1a)
			for (int i = 0; i < numStation; i++) {
				cplex.addLe(x[i], k[i]);
			}
			// (1b)
			for (int i = 0; i < numStation; i++) {
				for (int j = 0; j < numStation; j++) {
					for (int s = 0; s < numScenario; s++) {
						cplex.addEq(cplex.sum(cplex.prod(1., beta[i][j][s]), cplex.prod(1., iMinus[i][j][s])),
								eps[i][j][s]);
					}
				}
			}
			// (1c)
			for (int i = 0; i < numStation; i++) {
				for (int s = 0; s < numScenario; s++) {
					IloLinearNumExpr exprI = cplex.linearNumExpr();
					int sum = 0;
					for (int j = 0; j < numStation; j++) {
						sum += eps[i][j][s];
						exprI.addTerm(-1., iMinus[i][j][s]);
					}
					exprI.addTerm(1., iPlus[i][s]);
					exprI.addTerm(-1., x[i]);
					cplex.addEq(cplex.sum(exprI, 1), sum);
				}
			}
			// (1d)
			for (int i = 0; i < numStation; i++) {
				for (int s = 0; s < numScenario; s++) {
					IloLinearNumExpr exprBi = cplex.linearNumExpr();
					IloLinearNumExpr exprBj = cplex.linearNumExpr();
					for (int j = 0; j < numStation; j++) {
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

			if (cplex.solve()) {
				double[][] solVal = new double[6][];
				for (int i = 0; i < solVal.length; i++) {
					solVal[i] = cplex.getValues(varSol[i]);
				}

				cplex.output().println("Solution status = " + cplex.getStatus());
				this.statut = cplex.getStatus().toString();
				System.out.println("Solution value = " + cplex.getObjValue());
				this.solution = cplex.getObjValue();
				System.out.println("Time value = " + cplex.getCplexTime());
				this.resolutionTime = cplex.getCplexTime();

				for (int i = 0; i < solVal.length; i++) {
					for (int j = 0; j < solVal[i].length; j++) {
						System.out.print("  " + solVal[i][j]);
						solutions.put(varSol[i][j].getName(), "" + solVal[i][j]);
					}
					System.out.println();
				}
			}
			cplex.end();

		} catch (IloException e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getSolutions() {
		return solutions;
	}

	public void setSolutions(Map<String, String> solutions) {
		this.solutions = solutions;
	}

	public double getResolutionTime() {
		return resolutionTime;
	}

	public void setResolutionTime(double resolutionTime) {
		this.resolutionTime = resolutionTime;
	}

	public double getLoadTime() {
		return loadTime;
	}

	public void setLoadTime(double loadTime) {
		this.loadTime = loadTime;
	}

	public double getSolution() {
		return solution;
	}

	public void setSolution(double solution) {
		this.solution = solution;
	}

	public String getStatut() {
		return statut;
	}

	public void setStatut(String statut) {
		this.statut = statut;
	}

	@Override
	public String toString() {
		return "ResolutionCplex [solutions=" + solutions + ", resolutionTime=" + resolutionTime + ", loadTime="
				+ loadTime + ", solution=" + solution + ", statut=" + statut + "]";
	}

}

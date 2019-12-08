package Controller;

import java.util.Map;

/**
 * This interface contains all neccesary methods which return the result to the view 
 * @author 
 *
 */
public interface IResulsat {
	/* (temps de résolution, statut de solution, temps de lecture, solution optimale, nombre itérations)*/
	
	/**
	 * the solution for each variable
	 * @return
	 */
	public Map<String, String> VariableSolution();
	/**
	 * return the time resolution + load time + value of optimal solution + number of iterations
	 * @return
	 */
	public String getResult();
	
	
	
}

package Controller;

import java.util.Map;

/**
 * This interface contains all neccesary methods which return the result to the view 
 * @author 
 *
 */
public interface IResulsat {
	/* (temps de r�solution, statut de solution, temps de lecture, solution optimale, nombre it�rations)*/
	
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

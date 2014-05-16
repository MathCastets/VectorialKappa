/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorialkappa;

import com.vividsolutions.jts.geom.Polygon;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class for computing the kappa
 * @author Castets
 */
public class KappaCalculator {
	
	private Layer layer1; // The first layer
	private Layer layer2; // the layer that will be compared
	private double[][] matrix; // confusion matrix used for the kappa computation
	private HashMap<Object, Integer> valPos = new HashMap<Object, Integer>(); //if the values cant be sorted, we used this indexation
	private double kappa; // kappa value
	private double sd; // standard deviation
	
	public KappaCalculator(Layer layer1, Layer layer2){
		
		this.layer1 = layer1;
		this.layer2 = layer2;
		
		if(checkTypes()){
			
			matrix = new double[layer1.getTypes().size()][layer1.getTypes().size()];
			
			for(int i = 0; i < layer1.getTypes().size(); i ++){
				
				valPos.put(layer1.getTypes().get(i), i);
				
				for(int j = 0; j < layer1.getTypes().size(); j ++){
					matrix[i][j] = 0;
				}
			}
		}
	}
	
	public boolean checkTypes(){
		
		for(int i = 0; i < layer1.getTypes().size(); i ++){
			
			if(!layer1.getTypes().get(i).equals(layer2.getTypes().get(i))){
				
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Fill the confusion matrix by computing the intersection between elements from the different shapefile
	 */
	
	public void fillMatrix(){
		
		for(Element e1 : layer1.getElements()){
			
			for(Element e2 : layer2.getElements()){
				
				int pos1 = valPos.get(e1.getValue());
				int pos2 = valPos.get(e2.getValue());
				
				try {
					
					Element inter = e1.intersection(e2);
					if(inter.getGeometry() instanceof Polygon){
						matrix[pos1][pos2] += inter.getArea();
					}
					
				} catch (Exception ex) {
					Logger.getLogger(KappaCalculator.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}
	
	/**
	 * Compute the kappaValue
	 * the confucsion matrix must be filled before
	 */
	public void compute(){
		
		kappa = 0;
		Vector<Double> vectorRow = new Vector<Double>();
		Vector<Double> vectorColumn = new Vector<Double>();
		double diagonal = 0;
		double sommePe = 0;
		double sumTotal =0;
		
		for (int i = 0; i<matrix.length; i++ ){
			
			diagonal = diagonal + matrix[i][i];
			double sumrow = 0;
			double sumcolumn = 0;
			
			for (int j = 0; j<matrix.length; j++){
				
				sumcolumn = sumcolumn + matrix[j][i];
				sumrow = sumrow + matrix[i][j];
			}
			
			vectorRow.addElement(sumrow);
			vectorColumn.addElement(sumcolumn);
			
			sommePe = sommePe + (vectorRow.elementAt(i) * vectorColumn.elementAt(i));
			sumTotal += vectorRow.elementAt(i);
		}
		if ((Math.pow(sumTotal, 2) - sommePe) != 0) {
			kappa = ((diagonal * sumTotal) - sommePe) / (Math.pow(sumTotal, 2) - sommePe);
		}
		
                
                //Calcul de sd (Jules 27/12/12)
                double theta3=0.;
                for (int i = 0; i<matrix.length; i++ ){
                    theta3 = theta3 + matrix[i][i]*(vectorRow.elementAt(i)+vectorColumn.elementAt(i))/(Math.pow(sumTotal, 2));
                }
                
                double theta4=0.;
                for (int i = 0; i<matrix.length; i++ ){
                    for (int j = 0; j<matrix.length; j++ ){
                        theta4 = theta4 + matrix[i][j]*(vectorRow.elementAt(i)+vectorColumn.elementAt(j))/(Math.pow(sumTotal, 3));
                    }
                }
                
                double theta1 = (diagonal * sumTotal)/(Math.pow(sumTotal, 2));
                double theta2 = sommePe /(Math.pow(sumTotal, 2));
                
                double theta1b = 1.0 - theta1;
                double theta2b = 1.0 - theta2;
                
                double kv1 = (theta1*theta1b)/(Math.pow(theta2b, 2));
                double kv2 = (2 * theta1b * ((2 * theta1 * theta2) - theta3))/(Math.pow(theta2b, 3));
		double kv3 = ((Math.pow(theta1b, 2)) * (theta4 - (4 * (Math.pow(theta2, 2)))))/(Math.pow(theta2b, 4));
                
                double var = (kv1+kv2+kv3)/sumTotal;                         
                sd = Math.pow(var, 0.5);
	}
                
	
	/**
	 * Print the results
	 */
	public void consolePrint(){
		System.out.println("Kappa value : "+kappa);
		System.out.println("Matrix : ");
		for(String s : getMatrix()){
			System.out.println(s);
		}
	}
	
	/**
	 * Return a String tab representation of the confusion matrix
	 * @return
	 */
	public String[] getMatrix(){
		
		String[] matrixString = new String[matrix.length];
		for(int i = 0; i < matrix.length; i ++){
			String value = round(matrix[i][0], 1)+"";
			for(int j = 1; j < matrix.length; j ++){
				value+= " "+round(matrix[i][j], 1);
			}
			matrixString[i] = value;
		}
		return matrixString;
	}
	/**
	 * return the kappa value if it as been computed
	 * @return 
	 */	
	public double getKappa(){
		return kappa;
	}
	
	public double getStandardDeviation(){
		return sd;
	}
	
	/**
	 * round a double value
	 * @param number value
	 * @param decimal number of decimal wanted
	 * @return 
	 */	
	public static double round(double number, int decimal){
		
		Double rounded = (Math.round(number * Math.pow(10, decimal))) / (Math.pow(10, decimal));
		return rounded;
	}
	/**
	 * Writethe kappa value and the matrix into the given file
	 * @param file 
	 */	
	public void export(File file){
		
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write("Kappa value :"+kappa);
			for(String s : getMatrix()){
				out.newLine();
				out.write(s);
			}
			out.close();
		} catch (IOException ex) {
			Logger.getLogger(KappaUi.class.getName()).log(Level.SEVERE, null, ex);
		}
	}


	public double[][] getMatrixValues(){
		return matrix;
	}
}

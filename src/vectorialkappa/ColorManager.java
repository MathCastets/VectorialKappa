/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorialkappa;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Used for color generation for a number of values (features from the shepfile)
 * Coors are not implemented yet
 * @author Castets
 */
public class ColorManager {

	private ArrayList<Object> values  = new ArrayList<Object>(); // All values in this array will be used to generate colors
	private HashMap<Object, Color> colorValues = new HashMap<Object, Color>(); // contains one color for one value
	private static ColorManager instance; // the single instance 
	
	private ColorManager(){}
	
	/**
	 * Return the color associated for this value
	 * @param value 
	 * @return 
	 */
	public Color getColor(Object value){
		return colorValues.get(value);	
	}

	/**
	 * 
	 * @param value 
	 */
	public void addValue(Object value){
		values.add(value);
	}
	public void generate(){
		Color[] colors = getColorsRGB(values.size());
		for(int i = 0; i < values.size(); i ++){
			colorValues.put(values.get(i), colors[i]);
		}
	}
	/**
	 * Return the only instance of this class 
	 * @return 
	 */
	public static ColorManager getInstance(){
		if(instance == null){
			instance = new ColorManager();
		}
		return instance;
	}

	/**
	 * Generate a number of different colors 
	 * @param number 
	 * @return 
	 */
	public Color[] getColorsRGB(int number) {
		Color[] result = new Color[number];
		
		double[] values = new double[number];
		if (number > 1) {
			values[1] = 1.0 / (number - 1);
			for (int i = 2; i < number; i++) {
				values[i] = i * values[1];
			}
		}
		
		for (int i = 0; i < number; i++) {
			int position = (int) Math.round(values[i] * 1275);
			if (position < 255) {
				result[i] = new Color(0, position, 255);
			} else if (position < 510) {
				result[i] = new Color(0, 255, 510 - position);
			} else if (position < 765) {
				result[i] = new Color(position - 510, 255, 0);
			} else if (position < 1020) {
				result[i] = new Color(255, 1020 - position, 0);
			} else {
				result[i] = new Color(255, 245,255-( position - 1020));
			}
		}
		return result;
	}
}

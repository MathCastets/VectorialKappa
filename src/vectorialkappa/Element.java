/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorialkappa;

import com.vividsolutions.jts.geom.Geometry;
import java.awt.Color;

/**
 * An Element class that contain a geometry and a value
 * @author Castets
 */
public class Element {

	private Geometry geometry; // the geometry of this element
	private Object value; // the value from the features of the shape file

	public Element(Geometry geom, Object value) throws Exception{
			this.geometry = (Geometry)geom;
			this.value = value;
	}

	/**
	 * The area of this element
	 * @return 
	 */
	public double getArea(){
		return geometry.getArea();
	}

	/**
	 * Return an new instance of element with the geometry equals to the intersection of this element and an other
	 * (See JTS spec for more info)
	 * @param element
	 * @return
	 * @throws Exception 
	 */
	public Element intersection(Element element) throws Exception{
		Geometry geom = geometry.intersection(element.getGeometry());
		if(diff(element)){
			return new Element(geom, Color.BLACK); // in case of color representation
		}
		return new Element(geom, value);
	}

	/**
	 * Return the geometry of this element
	 * @return 
	 */
	public Geometry getGeometry(){
		return geometry;
	}

	/**
	 * Return an new instance of element with the geometry equals to the intersection of this element and an other
	 * (See JTS spec for more info)
	 * @param element
	 * @return 
	 */
	private boolean diff(Element element){
		return value.equals(element.getValue());
	}

	/**
	 * Return the value of this element (Feature value from shapefile)
	 * @return 
	 */
	public Object getValue(){
		return value;
	}

	/**
	 * In case of map representation, return the associated color for this element
	 * @return 
	 */
	public Color getColor(){
		if(value.equals(Color.BLACK)){
			return Color.BLACK;
		}
		return ColorManager.getInstance().getColor(value);
	}

}

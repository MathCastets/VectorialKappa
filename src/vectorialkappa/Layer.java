/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorialkappa;

import com.vividsolutions.jts.geom.Geometry;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.*;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.simple.SimpleFeatureTypeImpl;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * 
 * @author Castets
 */
public class Layer {

	private ArrayList<Element> elements = new ArrayList<Element>(); // array containing one element for each geometry in the shapefile
	private FeatureSource source; //source of the shapefile
	private String type; // the feature used to compare the shapeFiles
	private ArrayList<Object> values = new ArrayList<Object>(); // array containg each different value
	private int id;
	private static int number = 1;

        public Layer(File url){
            
            FileDataStore store = null;
                
            try {
                store = FileDataStoreFinder.getDataStore(url);
            } catch (IOException ex) {
                Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                    source = store.getFeatureSource();
            } catch (IOException ex) {
                    Logger.getLogger(Layer.class.getName()).log(Level.SEVERE, null, ex);
            }
            id = number;
            number++;

	}
/**
 * Return every features
 * @return 
 */
	public String[] getFeaturesName(){

		ArrayList<String> names = new ArrayList<String>();
		SimpleFeatureTypeImpl featureType = (SimpleFeatureTypeImpl)source.getSchema();
		for(AttributeDescriptor at : featureType.getAttributeDescriptors()){
			if(!at.getLocalName().equals("the_geom")){
				names.add(at.getLocalName());					
			}
		}
		return names.toArray(new String[names.size()]);
	}

	/**
	 * read the shapefile and fill the elements array
	 * @throws IOException 
	 */
	public void readShapeFile() throws IOException{
		
		SimpleFeatureTypeImpl featureType = (SimpleFeatureTypeImpl)source.getSchema();
		FeatureCollection featureCollection = source.getFeatures();
		
		try {
			for (FeatureIterator i = (FeatureIterator) featureCollection.features(); i.hasNext();) {
				
				SimpleFeatureImpl feature = (SimpleFeatureImpl) i.next();
				Geometry geom = ((Geometry)feature.getDefaultGeometry()).getGeometryN(0);
				
					
				Element element = new Element(geom.getGeometryN(0), feature.getAttribute(type) );
				addValues(element.getValue());
				elements.add(element);		
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		sort();
	}

	/**
	 * this method must be used to check if a value as an object has already been loaded
	 * @param o 
	 */
	private void addValues(Object o){
		boolean contain = false;
		for(Object val : values){
			if(val.equals(o)){
				contain = true;
			}	

		}
		if(!contain){
			values.add(o);
		}
	}

	/**
	 * Set the wanted type
	 * Must be used before reading the shapefile
	 * @param type 
	 */
	public void setType(String type){
		this.type = type;
	}

	/**
	 * Return every differents values forthe type
	 * @return 
	 */
	public ArrayList<Object> getTypes(){
		return values;
	}

	private void sort(){
		Object[] vals = values.toArray(new Object[values.size()]);
		Arrays.sort(vals);
		values = new ArrayList<Object>();
		values.addAll(Arrays.asList(vals));
	}

	/**
	 * Return every loaded elements
	 * Shapefile must have been read before
	 * @return 
	 */
	public ArrayList<Element> getElements(){
		return elements;
	}

	/**
	 * Return the area of all the elements
	 * @return 
	 */
	public double getArea(){
		double area = 0;
		for(Element e : elements){
			area += e.getArea();
		}
		return area;
	}

	public int getId(){
		return id;
	}

	public static void reset(){
		number = 1;
	}

	public String getType(){
		return type;
	}
}

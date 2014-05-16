/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorialkappa;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Castets
 */
public class VectorialKappa {

	private ConsoleReader consoleReader = new ConsoleReader();// used to parse the args
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		// TODO code application logic here
		
		VectorialKappa vK = new VectorialKappa();
		if(args.length > 0){
			vK.noGuicompute(args);
		}else{
			vK.setLook();
			vK.showGui();
		}
	}


	/**
	 * Launch a computation in console
	 * @param args 
	 */
	public void noGuicompute(String[] args){
		
		if(args[0].equals("-help")){
			showHelp();
		}else{
			consoleReader.read(args);
			
			Layer layer1 = getLayer1();
			Layer layer2 = getLayer2();
		
			KappaCalculator kC = new KappaCalculator(layer1, layer2);
			kC.fillMatrix();
			kC.compute();
			kC.consolePrint();
			
			if(consoleReader.get("-export") != null){
				File file = new File(consoleReader.get("-export")[0]);
				kC.export(file);
				System.out.println("File writed : "+file.toString());
			}
		}
	}

	/**
	 * Launch the gui computation
	 */
	public void showGui(){
		KappaUi kappaUi = new KappaUi();
		kappaUi.setLocationRelativeTo(null);
		kappaUi.setVisible(true);
	}

	/**
	 * An inner class used to parse the main args
	 */
	
	public static class ConsoleReader{

		HashMap<String, String[]> parsedArgs= new HashMap<String, String[]>();
		
		public void read(String[] args){


			for(int i = 0; i < args.length; i ++){
				
				if(args[i].startsWith("-")){

					ArrayList<String> options = new ArrayList<String>();
					for(int j = i + 1; j < args.length; j ++){

						if(args[j].startsWith("-")){
							break;
						}else{
							options.add(args[j]);							
						}
					}
					parsedArgs.put(args[i], options.toArray(new String[options.size()]) );
						   
				}
				
			}
		}

		public String[] get(String value){
			if(parsedArgs.containsKey(value)){
			return parsedArgs.get(value);
			}else{
				return null; 
			}
			
		}

	}

	/**
	 * Return the first layer in case of console launch
	 * @return 
	 */
	public Layer getLayer1(){
		String path = consoleReader.get("-run")[0];
		File file = new File(path);
		Layer layer = null;
		//try {	
			//layer = new Layer(file.toURI().toURL());
                        layer = new Layer(file);
			layer.setType(getFeature1());
			try {
				layer.readShapeFile();
			} catch (IOException ex) {
				Logger.getLogger(VectorialKappa.class.getName()).log(Level.SEVERE, null, ex);
			}
		//} catch (MalformedURLException ex) {
		//	Logger.getLogger(VectorialKappa.class.getName()).log(Level.SEVERE, null, ex);
		//}
		return layer;
		
	}

	/**
	 * Return the layer to compare in case of console launch
	 * @return 
	 */
	public Layer getLayer2(){
		String path = consoleReader.get("-run")[2];
		File file = new File(path);
		Layer layer = null;
                layer.setType(getFeature2());
                try {
                        layer.readShapeFile();
                } catch (IOException ex) {
                        Logger.getLogger(VectorialKappa.class.getName()).log(Level.SEVERE, null, ex);
                }
		return layer;

	}

	public String getFeature1(){
		String feature= consoleReader.get("-run")[1];
		return feature;

	}

	public String getFeature2(){
		String feature = consoleReader.get("-run")[3];
		return feature;

	}
	

	public static void showHelp(){

		System.out.println();
		System.out.println ("**************************************************************************");
		System.out.println ("*************************** CONSOLE HELP *********************************");
		System.out.println ("**************************************************************************");
		System.out.println();
		System.out.println ("**************************************************************************");
		System.out.println ("**************************** MANDATORY ***********************************");
		System.out.println ("**************************************************************************");
		System.out.println();
		
		System.out.println( "Running a kapp computation : -run firstShapefilePath feature1 secondShapefile feature2");
		System.out.println();
		System.out.println ("**************************************************************************");
		System.out.println ("************************** NO SPACE IN PATH ******************************");
		System.out.println ("**************************************************************************");
		
		System.out.println ("**************************************************************************");
		System.out.println ("**************************** OPTIONAL ************************************");
		System.out.println ("**************************************************************************");
		System.out.println();
		System.out.println( "Exporting file : -export filePath");
		System.out.println();
		
		System.out.println ("**************************************************************************");
		System.out.println ("***************************** EXEMPLE ************************************");
		System.out.println ("**************************************************************************");
		System.out.println();
		
		System.out.println( "Exemple for -export :");
		System.out.println("java -jar vectorialKappa.jar -run C:\\windows\\userName\\documents\\myShape.shp OS C:\\windows\\userName\\documents\\compare.shp OS");
		System.out.println();
		System.out.println();
		
	
	}

	private void setLook(){
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			  if ("Nimbus".equals(info.getName())) {
				  UIManager.setLookAndFeel(info.getClassName());
				  break;
			  }
			}
			  
			
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
	}
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vectorialkappa;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;

/**
 *
 * @author Castets
 * A Simple UI to load a shape file
 */
public class ShapeFileUi extends javax.swing.JPanel {

	private Layer layer = null;
	private DefaultListModel listModel;
	/**
	 * Creates new form ShapeFileUi
	 */
	public ShapeFileUi() {
		initComponents();
	}

	/**
	 * This method is called from within the constructor to initialize the
	 * form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jButtonOpen = new javax.swing.JButton();
                jComboBoxNames = new javax.swing.JComboBox();
                jLabel1 = new javax.swing.JLabel();
                jButtonValidate = new javax.swing.JButton();

                setBorder(javax.swing.BorderFactory.createTitledBorder(""));

                jButtonOpen.setText("Open Shapefile");
                jButtonOpen.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonOpenActionPerformed(evt);
                        }
                });

                jLabel1.setText("Select feature to compare :");

                jButtonValidate.setText("Validate");
                jButtonValidate.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonValidateActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButtonOpen)
                                .addGap(84, 84, 84)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxNames, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButtonValidate, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(14, Short.MAX_VALUE))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButtonOpen)
                                        .addComponent(jComboBoxNames, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1)
                                        .addComponent(jButtonValidate))
                                .addContainerGap(16, Short.MAX_VALUE))
                );
        }// </editor-fold>//GEN-END:initComponents

        private void jButtonOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOpenActionPerformed
                // TODO add your handling code here:
		JFileChooser chooser = new JFileChooser();

		chooser.setFileFilter(new ShapeFilter());
		chooser.setAcceptAllFileFilterUsed(false);
		
		int returnVal = chooser.showOpenDialog(getParent());
		if (returnVal == JFileChooser.APPROVE_OPTION){
			File file= chooser.getSelectedFile();
                        layer = new Layer(file);			
			String[] names = layer.getFeaturesName();
			DefaultComboBoxModel dcm = new DefaultComboBoxModel(names);
			jComboBoxNames.setModel(dcm);
		}
        }//GEN-LAST:event_jButtonOpenActionPerformed

        private void jButtonValidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonValidateActionPerformed
		// TODO add your handling code here:
		layer.setType((String)jComboBoxNames.getSelectedItem());
		try {
			layer.readShapeFile();
			if(listModel != null){
				listModel.addElement("Layer "+layer.getId()+" loaded ("+layer.getElements().size()+" elements)");	
			}
		} catch (IOException ex) {
			Logger.getLogger(ShapeFileUi.class.getName()).log(Level.SEVERE, null, ex);
		}
        }//GEN-LAST:event_jButtonValidateActionPerformed

	public void reset(){
		jComboBoxNames.setModel(new DefaultComboBoxModel());
		layer = null;
	}
	public Layer getLayer(){
		return layer;
	}

	public void setListModel(DefaultListModel listModel){
		this.listModel = listModel;
	}
        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButtonOpen;
        private javax.swing.JButton jButtonValidate;
        private javax.swing.JComboBox jComboBoxNames;
        private javax.swing.JLabel jLabel1;
        // End of variables declaration//GEN-END:variables
}
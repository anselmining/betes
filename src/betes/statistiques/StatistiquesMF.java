package betes.statistiques;


	import java.awt.*; 
	import java.awt.event.*; 
	import javax.swing.*; 

	import org.jfree.chart.*; 
	import org.jfree.data.general.DefaultPieDataset;

import betes.donnees.EnvironnementDepot;
	

	public class StatistiquesMF extends JFrame { 
		/**
		 * 
		 */
		private static final long serialVersionUID = 7495869935385274087L;
		private static EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
	  private JPanel pnl; 

	  public JPanel statistiquesFromage() { 
	    addWindowListener(new WindowAdapter() { 
	      public void windowClosing(WindowEvent e) { 
	        dispose(); 
	        System.exit(0); 
	      } 
	    }); 
	    pnl = new JPanel(new BorderLayout()); 
	    setContentPane(pnl); 
	    setSize(600,600); 

	    DefaultPieDataset pieDataset = new DefaultPieDataset(); 
	    pieDataset.setValue("Nombre de Femelles-"+eDepot.getNombreDeFemelles()[eDepot.getTempsEcoule()]+"-", new Integer(eDepot.getNombreDeFemelles()[eDepot.getTempsEcoule()])); 
	    pieDataset.setValue("Nombre de Mâles-"+eDepot.getNombreDeMales()[eDepot.getTempsEcoule()]+"-", new Integer(eDepot.getNombreDeMales()[eDepot.getTempsEcoule()])); 

	    JFreeChart pieChart = ChartFactory.createPieChart("Nombre de Mâles et de Femelles", 
	      pieDataset, true, true, true); 
	    ChartPanel cPanel = new ChartPanel(pieChart); 
	    pnl.add(cPanel); 
	   

	return pnl;
	  } 
	}
	


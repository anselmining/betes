package betes.statistiques;

import log.LoggerUtility;
import betes.donnees.EnvironnementDepot;

//cette exemple est le début de mes recherche de graphe et je suis entrain d'étudie la 
//bonne méthode à utiliser car j'en ai plusieurs 

import java.awt.Dimension;
import org.apache.log4j.Logger;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.*;

public class Statistiques {

	@SuppressWarnings("unused")
	private static Logger logger = LoggerUtility.getLogger(Statistiques.class);
	private static EnvironnementDepot eDepot = EnvironnementDepot.getInstance();


	public static final String Title = "Nombre de bêtes par rapport au temps";
	public static final String Title1 = "Nombre Accouplement par rapport au temps";
	public static final String Title2 = "Nombre de mâles par rapport au temps";
	public static final String Title3 = "Nombre femelles par rapport au temps";
	
	/**Affiche des courbes de statistiques par rapport au temps.
	 * 
	 * @return des courbes. 
	 */
	public ChartPanel display() {
		XYSeries series = new XYSeries(Title);
		XYSeries series1 = new XYSeries(Title1);
		XYSeries series2 = new XYSeries(Title2);
		XYSeries series3 = new XYSeries(Title3);
		
		for (int i = 0; i <= eDepot.getTempsEcoule()-1; i++) {
			series.add(i, eDepot.getNombreBetes()[i]);
			series1.add(i, eDepot.getNombreAcouplements()[i]);
			series2.add(i, eDepot.getNombreDeMales()[i]);
			series3.add(i, eDepot.getNombreDeFemelles()[i]);

		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		NumberAxis domain1 = new NumberAxis("temps écoulé(s)");
		NumberAxis range1 = new NumberAxis("nombre de bêtes et accouplement");
		XYSplineRenderer r1 = new XYSplineRenderer(5);
		XYPlot xyplot1 = new XYPlot(dataset, domain1, range1, r1);
		JFreeChart chart1 = new JFreeChart(xyplot1);
		ChartPanel chartPanel = new ChartPanel(chart1) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2484334881352220289L;

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(800, 600);
			}
		};
		return chartPanel;
	}

}

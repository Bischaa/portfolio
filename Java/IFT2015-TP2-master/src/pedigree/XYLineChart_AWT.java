//Code fait par Pierre-Olivier Tremblay: 20049076  et Maxime Ton: 20143044

package pedigree;

/*
 * Classe prise de https://www.tutorialspoint.com/jfreechart/jfreechart_xy_chart.htm
 * pour afficher un graphique de coalescence
 * 
 * Ajouter les librairie dans le classpath du projet pour faire marcher cette classe (Eclipse)
*/

import java.awt.Color;
import java.util.Iterator;
import java.util.Map;
import java.awt.BasicStroke;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYLineChart_AWT extends ApplicationFrame {

   public XYLineChart_AWT(String applicationTitle, String chartTitle) {
      super(applicationTitle);
      JFreeChart xylineChart = ChartFactory.createXYLineChart(chartTitle, "Temps", "PA", createDataset(),
            PlotOrientation.VERTICAL, true, true, false);

      ChartPanel chartPanel = new ChartPanel(xylineChart);
      chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
      final XYPlot plot = xylineChart.getXYPlot();

      XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
      renderer.setSeriesPaint(0, Color.RED);
      renderer.setSeriesPaint(1, Color.GREEN);
      renderer.setSeriesPaint(2, Color.YELLOW);
      renderer.setSeriesStroke(0, new BasicStroke(4.0f));
      renderer.setSeriesStroke(1, new BasicStroke(3.0f));
      renderer.setSeriesStroke(2, new BasicStroke(2.0f));
      plot.setRenderer(renderer);
      setContentPane(chartPanel);
   }

   private XYDataset createDataset() {

      Coalescence test = new Coalescence();
      test.init();

      // Pour la s�rie d'hommes
      Iterator PA_setIterator_m = test.PA_set_m.entrySet().iterator();

      final XYSeries male = new XYSeries("Male");
      while (PA_setIterator_m.hasNext()) {
         Map.Entry PA_m = (Map.Entry) PA_setIterator_m.next();
         double n_m = (int) PA_m.getValue();
         male.add((double) PA_m.getKey(), n_m);
      }

      // Pour la s�rie de femmes
      Iterator PA_setIterator_f = test.PA_set_f.entrySet().iterator();

      final XYSeries female = new XYSeries("Female");
      while (PA_setIterator_f.hasNext()) {
         Map.Entry PA_f = (Map.Entry) PA_setIterator_f.next();
         double n_f = (int) PA_f.getValue();
         female.add((double) PA_f.getKey(), n_f);
      }

      // Pour la taille de la population
      Iterator taillePopulation = test.taillePop.entrySet().iterator();

      final XYSeries population = new XYSeries("Population");
      while (taillePopulation.hasNext()) {
         Map.Entry Pop = (Map.Entry) taillePopulation.next();
         double n_pop = (int) Pop.getValue();
         population.add((double) Pop.getKey(), n_pop);

      }

      final XYSeriesCollection dataset = new XYSeriesCollection();
      dataset.addSeries(male);
      dataset.addSeries(female);
      dataset.addSeries(population);
      return dataset;

   }

   public static void main(String[] args) {
      XYLineChart_AWT chart = new XYLineChart_AWT("Coalescence des lign�es", "Taille de l'ensemble PA selon le temps");
      chart.pack();
      RefineryUtilities.centerFrameOnScreen(chart);
      chart.setVisible(true);
   }
}
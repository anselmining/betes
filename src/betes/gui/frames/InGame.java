package betes.gui.frames;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import betes.donnees.ContraintesParametres;
import betes.donnees.EnvironnementDepot;
import betes.donnees.GenerationAleatoire;
import betes.gui.GraphicUserInterface;
import betes.gui.dessin.JCanvas;
import betes.gui.moteur.EvenementsRegistre;
import betes.modeles.environnement.Case;
import betes.statistiques.Statistiques;
import betes.statistiques.StatistiquesMF;
import betes.statistiques.StatistiquesMoteur;
import betes.visiteurs.VisiteurBouger;
import betes.visiteurs.VisiteurMiseAjourGrille;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import java.awt.Font;
import java.awt.Rectangle;

import javax.swing.JLabel;

import org.jfree.chart.ChartPanel;

public class InGame extends JFrame implements Runnable {

	private static final long serialVersionUID = 5289154482687445310L;
	private InGame instance = this;
	private EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
	private EvenementsRegistre rEvent = EvenementsRegistre.getInstance();
	private GraphicUserInterface gui = GraphicUserInterface.getInstance();
	private VisiteurBouger bougeurDeBetes = VisiteurBouger.getInstance();
	private static int maxTabs = 10;
	private int r = eDepot.getR();
	private int g = eDepot.getG();
	private int b = eDepot.getB();
	private Boolean stop;

	public JFrame frame;
	public JFrame dimensionFrame;
	public JTextArea infoArea = new JTextArea();
	private JPanel mainTab = new JPanel();
	private JPanel infoTab = new JPanel();
	private JPanel mapPanel = new JPanel();
	private JPanel infoPanel = new JPanel();
	private JPanel flowPanelRight = new JPanel();
	private JPanel gridPanelRight = new JPanel();
	private JPanel infoPanelBorder = new JPanel();
	private JPanel gridPanelRightUp = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel infoFlowPanel = new JPanel();
	private JPanel choixGrillePanel = new JPanel();
	private JPanel jcPanel = new JPanel();
	private JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JScrollPane scrollPane = new JScrollPane(infoArea);
	private JCanvas jc = new JCanvas();
	private JButton startButton = new JButton("Start");
	private JButton statsButton = new JButton("Statistiques");
	private JButton deleteButton = new JButton("Supprimer");
	private JButton sprDecorButton = new JButton("Supprimer Decor");
	private JButton ajtDecorButton = new JButton("Ajouter Decor");

	private JComboBox<String> grilleDecorChoixX = new JComboBox<String>();
	private JComboBox<String> grilleDecorChoixY = new JComboBox<String>();
	public int grilleX = eDepot.getGrille().getTailleX() + 1;
	public int grilleY = eDepot.getGrille().getTailleY() + 1;
	private VisiteurMiseAjourGrille grilleVisiteur = new VisiteurMiseAjourGrille();

	@SuppressWarnings("unused")
	private Case[][] tempCase = new Case[grilleX][grilleY];
	private String infoUpdate;
	private String typeClimat;
	private final JLabel lblTemps = new JLabel("Temps écoulé: 0ms.");

	public InGame() {
		stop = true;
		init();
		initGrilleChoix();
		addListeners();
	}

	private void addListeners() {
		startButton.addActionListener(new StartStopAction());
		deleteButton.addActionListener(new DeleteTabAction());
		statsButton.addActionListener(new StatsAction());
		sprDecorButton.addActionListener(new DeleteDecorAction());
		ajtDecorButton.addActionListener(new CreateDecorAction());
	}

	/**
	 * Initialize the contents of the frame.
	 */

	public void init() {
		frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		tabbedPane.setFont(new Font("DejaVu Serif", Font.BOLD, 12));
		tabbedPane.addTab("Onglet Principal", null, mainTab, null);
		mainTab.setLayout(new BorderLayout(0, 0));
		mainTab.add(mapPanel, BorderLayout.CENTER);

		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		mapPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		mapPanel.setLayout(new BorderLayout(0, 0));
		mapPanel.add(jc, BorderLayout.CENTER);

		 mapPanel.setBorder(new EmptyBorder(10, 100, 100,100));

		mapPanel.setPreferredSize(new Dimension(grilleX
				* ContraintesParametres.DIMX, grilleY
				* ContraintesParametres.DIMY));

		mapPanel.setBackground(new Color(r - 10, g - 10, b - 10));
		jcPanel.setBackground(new Color(r - 10, g - 10, b - 10));
		jc.setPreferredSize(new Dimension(grilleX * ContraintesParametres.DIMX,
				grilleY * ContraintesParametres.DIMY));
		jcPanel.add(jc);
		jcPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
		mapPanel.add(jcPanel, BorderLayout.CENTER);
		mapPanel.add(lblTemps, BorderLayout.SOUTH);

		mainTab.add(infoPanel, BorderLayout.EAST);
		infoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		infoPanel.setLayout(new BorderLayout(0, 0));
		infoPanel.add(flowPanelRight, BorderLayout.NORTH);

		flowPanelRight.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		infoPanelBorder.setBorder(new EmptyBorder(0, 0, 0, 0));
		flowPanelRight.add(infoPanelBorder);
		infoArea.setEditable(false);
		infoArea.setMaximumSize(new Dimension(700, 500));
		infoArea.setBounds(new Rectangle(0, 0, 150, 800));
		infoArea.setWrapStyleWord(true);
		infoArea.setMinimumSize(new Dimension(200, 500));
		infoArea.setFont(new Font("DejaVu Serif", Font.PLAIN, 12));
		infoArea.setColumns(20);
		infoArea.setTabSize(0);
		infoArea.setRows(23);
		infoPanelBorder.add(scrollPane);
		scrollPane.setPreferredSize(new Dimension(220, grilleX * 10));

		infoPanel.add(gridPanelRight, BorderLayout.CENTER);
		gridPanelRight.setLayout(new GridLayout(2, 0, 0, 0));
		gridPanelRight.add(gridPanelRightUp);
		gridPanelRightUp.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		gridPanelRightUp.add(buttonPanel);

		choixGrillePanel.setLayout(new GridLayout(0, 2, 0, 0));
		choixGrillePanel.add(grilleDecorChoixX);
		choixGrillePanel.add(grilleDecorChoixY);

		buttonPanel.setLayout(new GridLayout(5, 0, 0, 0));
		buttonPanel.add(startButton);
		buttonPanel.add(statsButton);
		buttonPanel.add(choixGrillePanel);
		buttonPanel.add(sprDecorButton);
		buttonPanel.add(ajtDecorButton);

		startButton.setPreferredSize(new Dimension(180, 30));
		statsButton.setPreferredSize(new Dimension(180, 30));

		infoArea.setLineWrap(true);
		infoArea.append("");

		frame.pack();
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);

	}

	public void creerGrille() {
		jc = grilleVisiteur.creerGrille(jc, r, g, b);
		jc = grilleVisiteur.creerFilet(jc);
		jc = grilleVisiteur.creerNature(jc);
		frame.repaint();
	}

	public void createTab(String text) {
		if (tabbedPane.getTabCount() < maxTabs) {
			infoTab = new JPanel();
			tabbedPane.addTab(text, null, infoTab, null);
			infoTab.setLayout(new BorderLayout(0, 0));

			Statistiques statistiques = new Statistiques();
			ChartPanel graphique = statistiques.display();
			JPanel fromage= new StatistiquesMF().statistiquesFromage();

			if (text == "Statistiques") {
				infoTab.add(graphique, BorderLayout.CENTER);				
			}
			else {
				infoTab.add(fromage, BorderLayout.CENTER);
			}
			
			deleteButton = new JButton("Fermer");
			deleteButton.addActionListener(new DeleteTabAction());
			infoFlowPanel = new JPanel();
			infoTab.add(infoFlowPanel, BorderLayout.SOUTH);
			infoFlowPanel.add(deleteButton);

		}
	}

	public void deleteTab() {
		if (tabbedPane.getTabCount() > -1) {
			tabbedPane.remove(tabbedPane.getSelectedIndex());
		}
	}

	public void creerBetes(int nombreBetes) {
		creerNourriture();
		gui.initialiserBetesNature(nombreBetes);
		jc = grilleVisiteur.visite(this, jc, r, g, b);
		frame.repaint();
		ajouterEvent();
	}

	public void ajouterEvent() {
		infoUpdate = rEvent.getEvent();
		if (infoUpdate != "") {
			infoArea.append(infoUpdate);
		}
		rEvent.clearEvent();
	}

	public void nextTurn() {
		rEvent.clearEvent();
		creerNourriture();
		bougeurDeBetes.deplacerIA();
		StatistiquesMoteur.enregistrerNombreDeBetesVivantes();
		eDepot.setTempsEcoule(eDepot.getTempsEcoule() + 1);
		ajouterEvent();
		jc = grilleVisiteur.visite(this, jc, r, g, b);
		frame.repaint();
		Toolkit.getDefaultToolkit().sync();
	}

	private class StatsAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			createTab("Statistiques");
			createTab("StatistiquesMF");
		}
	}

	private class StartStopAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (stop) {
				Thread guiThread = new Thread(instance);
				stop = false;
				guiThread.start();
				startButton.setText("Pause");
			} else {
				stop = true;
				startButton.setText("Start");
			}

			if (eDepot.getNombreBetesVivantes() <= 0) {
				frame.dispose();
			}
		}
	}

	private class DeleteTabAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			deleteTab();
		}
	}

	private class DeleteDecorAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int i = Integer.parseInt(grilleDecorChoixX.getSelectedItem()
					.toString());
			int j = Integer.parseInt(grilleDecorChoixY.getSelectedItem()
					.toString());
			jc = grilleVisiteur.supprimerDecor(jc, i, j);
			frame.repaint();
			ajouterEvent();
		}
	}

	private class CreateDecorAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int i = Integer.parseInt(grilleDecorChoixX.getSelectedItem()
					.toString());
			int j = Integer.parseInt(grilleDecorChoixY.getSelectedItem()
					.toString());
			jc = grilleVisiteur.ajouterDecor(jc, i, j);
			frame.repaint();
			ajouterEvent();
		}
	}

	public void setClimat(String typeClimat) {
		if (typeClimat == "Aride") {
			this.typeClimat = "Tropique";
			infoArea.append("Le climat est tropique !");
			eDepot.setR(212);
			eDepot.setG(202);
			eDepot.setB(120);
		} else if (typeClimat == "Tropique") {
			this.typeClimat = "Tempéré";
			infoArea.append("Le climat est tempéré !");
			eDepot.setR(76);
			eDepot.setG(110);
			eDepot.setB(73);
		} else {
			this.typeClimat = "Aride";
			infoArea.append("Le climat est aride !");
			eDepot.setR(110);
			eDepot.setG(133);
			eDepot.setB(25);
		}
	}

	public void changeClimatAutomatique() {
		if (typeClimat == "Tropique") {
			setClimat("Tropique");
		} else if (typeClimat == "Aride") {
			setClimat("Aride");
		} else {
			setClimat("Tempéré");
		}
		
		r = eDepot.getR();
		g = eDepot.getG();
		b = eDepot.getB();
		
		jc = grilleVisiteur.creerGrille(jc, r, g, b);
		mapPanel.setBackground(new Color(r - 10, g - 10, b - 10));
		jcPanel.setBackground(new Color(r - 10, g - 10, b - 10));
		frame.repaint();		
	}

	private void creerNourriture() {
		if (typeClimat == "tropique"
				&& GenerationAleatoire.nombreAleatoire(0, 25) > 15) {
			gui.initialiserNourriture();
		}
		if (typeClimat == "aride"
				&& GenerationAleatoire.nombreAleatoire(0, 25) > 24) {
			gui.initialiserNourriture();
		}
		if (typeClimat == "tempéré"
				&& GenerationAleatoire.nombreAleatoire(0, 25) > 17) {
			gui.initialiserNourriture();
		}
	}

	private void initGrilleChoix() {
		int i;
		for (i = 0; i < grilleX; i++) {
			grilleDecorChoixX.addItem(String.valueOf(i));
		}
		for (i = 0; i < grilleY; i++) {
			grilleDecorChoixY.addItem(String.valueOf(i));
		}
	}

	@Override
	public void run() {

		while (!stop) {
			if (eDepot.getNombreBetesVivantes() <= 0) {
				stop = true;
				infoArea.append("\n La simulation est terminé!");
				startButton.setText("Exit");
				break;
			}

			try {
				Thread.sleep(ContraintesParametres.VITESSE_EXECUTION);
			} catch (InterruptedException e) {

			}

			nextTurn();
			eDepot.setNombreAcouplements();
			StatistiquesMoteur.enregistrerNombreMales();
			eDepot.setJour(eDepot.getJour() + 1);
			lblTemps.setText("Temps écoulé : "
					+ String.valueOf(eDepot.getJour()) + "ms.");

			if (eDepot.getJour() % 20 == 0) {
				changeClimatAutomatique();
			}

			try {
				testFin();
			} catch (InterruptedException e) {

			}
		}

	}

	public synchronized void testFin() throws InterruptedException {
		if (stop) {
			throw new InterruptedException();
		}
	}
}
package betes.gui.frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;

import betes.donnees.EnvironnementDepot;
import betes.donnees.EnvironnementFabrique;
import betes.gui.moteur.ImageLoading;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;

public class Accueil extends JFrame {
	private EnvironnementFabrique eFabrique = new EnvironnementFabrique();
	private EnvironnementDepot eDepot = EnvironnementDepot.getInstance();
	private JComboBox<String> climateChoix = new JComboBox<String>();

	private JLabel lblBtes = new JLabel("Bêtes ! ");
	private ImageLoading loadImage = new ImageLoading();
	private JLabel lblBtesLabel;
	private JLabel lblM = new JLabel(
			"Mini-simulation d’une évolution génétique simplifiée.");
	private JButton btnModeNature = new JButton("Lancer la simulation !");
	/**
	 * 
	 */
	private static final long serialVersionUID = 1407883905955698398L;
	private JPanel contentPane;
	private final JPanel topPanel = new JPanel();
	private final JPanel centerPanel = new JPanel();
	private final JPanel topFlowPanel = new JPanel();
	private final JPanel centerFlowPanel1 = new JPanel();
	private final JPanel centerFlowPanel2 = new JPanel();
	private final JPanel climatPanelGrille = new JPanel();
	private final JLabel lblClimat = new JLabel("Choisissez le climat initial:");
	private final JPanel climatPanel = new JPanel();
	private final JLabel lblChoisissezLeNombre = new JLabel(
			"Choisissez le nombre de bêtes initial:");
	private final JLabel lblGrille = new JLabel(
			"Choisissez la taille de la grille:");
	private final JComboBox<Integer> betesChoix = new JComboBox<Integer>();
	private final JComboBox<Integer> grilleXChoix = new JComboBox<Integer>();
	private final JComboBox<Integer> grilleYChoix = new JComboBox<Integer>();
	private final JLabel lblLancezLaSimulation = new JLabel(
			"Lancez la simulation:");
	
	private Color color = new Color(88, 135, 47);

	public Accueil() {
		init();
		addListeners();

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();

		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;

		climateChoix.addItem("Tempéré");
		climateChoix.addItem("Tropique");
		climateChoix.addItem("Aride");

		betesChoix.addItem(10);
		betesChoix.addItem(15);
		betesChoix.addItem(20);

		grilleXChoix.addItem(30);
		grilleXChoix.addItem(40);
		grilleYChoix.addItem(30);
		grilleYChoix.addItem(40);

		contentPane.setLocation(screenWidth / 4, screenHeight / 6);
		this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH);
	}

	private void init() {
		contentPane = new JPanel();
		
		contentPane.setBackground(color);
		topPanel.setBackground(color);
		topFlowPanel.setBackground(color);
		centerPanel.setBackground(color);
		centerFlowPanel1.setBackground(color);
		centerFlowPanel2.setBackground(color);
		climatPanel.setBackground(color);
		climatPanelGrille.setBackground(color);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 660, 400);
		setContentPane(contentPane);
		setLocationRelativeTo(null);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(topPanel, BorderLayout.NORTH);
		contentPane.add(centerPanel, BorderLayout.CENTER);

		topPanel.setLayout(new BorderLayout(0, 0));

		topFlowPanel.add(lblBtes);
		lblBtes.setFont(new Font("Serif", Font.BOLD, 48));
//		lblBtesLabel = new JLabel(new ImageIcon("./src/ressources/img/BetesImg2.png"));
		lblBtesLabel = new JLabel(new ImageIcon(loadImage.getImage("ressources/img/BetesImg2.png")));

		topPanel.add(lblBtesLabel);
		topPanel.add(topFlowPanel, BorderLayout.NORTH);

		centerPanel.setLayout(new BorderLayout(0, 0));
		centerPanel.add(centerFlowPanel1, BorderLayout.NORTH);
		centerPanel.add(centerFlowPanel2, BorderLayout.CENTER);

		centerFlowPanel1.add(lblM);
		centerFlowPanel2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		centerFlowPanel2.add(climatPanel);

		climatPanel.setLayout(new GridLayout(4, 1, 0, 0));

		climatPanel.add(lblClimat);
		climatPanel.add(climateChoix);

		climatPanel.add(lblChoisissezLeNombre);
		climatPanel.add(betesChoix);

		climatPanel.add(lblGrille);
		climatPanel.add(climatPanelGrille);
		grilleXChoix.setPreferredSize(new Dimension(80, 40));
		grilleYChoix.setPreferredSize(new Dimension(80, 40));
		climatPanelGrille.add(grilleXChoix);
		climatPanelGrille.add(grilleYChoix);

		climatPanel.add(lblLancezLaSimulation);
		climatPanel.add(btnModeNature);

		btnModeNature.setPreferredSize(new Dimension(300, 50));

	}

	private void addListeners() {
		btnModeNature.addActionListener(new NatureListener());
	}

	public class NatureListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Accueil.this.dispose();

			eFabrique.creerGrille(
					Integer.valueOf(grilleXChoix.getSelectedItem().toString()),
					Integer.valueOf(grilleYChoix.getSelectedItem().toString()));
			eDepot.setClimat(climateChoix.getSelectedItem().toString());
			InGame window = new InGame();
			window.setClimat(climateChoix.getSelectedItem().toString());
			window.creerGrille();
			window.frame.setVisible(true);
			window.creerBetes(Integer.valueOf(betesChoix.getSelectedItem()
					.toString()));
		}

	}

}

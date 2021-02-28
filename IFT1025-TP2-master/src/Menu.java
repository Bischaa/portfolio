import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileSystemView;

public class Menu {
	
	//Dictionnaire qui pourra être chargé à l'aide du bouton Dictionnaire
	public static ArrayList<String> dict = new ArrayList<String>();
	
	public static void createMenu() {
	
	//Créer le frame dans lequel le menu sera créer
	final JFrame f = new JFrame("Correcteur d'orthographe");
	
	//Propriétés du frame
	f.setSize(500,650);
	f.setResizable(false);
	f.setVisible(true);
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	//Utilisons le layout manager BorderLayout pour nos Panels
	f.getContentPane().setLayout(new BorderLayout(0,0));
	
	//Créons le premier panel qui aura les boutons
	JPanel pane1 = new JPanel();
	pane1.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
	f.getContentPane().add(pane1, BorderLayout.PAGE_START);
	
	//Créons le deuxième panel qui aura le TextArea
	JPanel pane2 = new JPanel();
	pane2.setLayout(new FlowLayout());
	f.getContentPane().add(pane2, BorderLayout.CENTER);
	
	//Créons le TextArea
	JTextArea text = new JTextArea(30,40);
	text.setLineWrap(true);
	
	//Et ajoutons une scrollbar
	JScrollPane scroll = new JScrollPane(text);
	scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	pane2.add(scroll);
	
	//Créons les boutons en question
	JButton a = new JButton("Ficher");
	JButton b = new JButton("Dictionnaire");
	JButton c = new JButton("Vérifier");
	
	//Propriétés des boutons
	a.setSize(10,10);
	b.setSize(10,10);
	c.setSize(10,10);
	
	//Ajouter les boutons au premier panel
	pane1.add(a);
	pane1.add(b);
	pane1.add(c);
	
	//Lorsqu'on clique sur le bouton Fichier
	a.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
		//Créons un nouveau frame
		JFrame secondFrame = new JFrame("Fichier");
		//Il y a deux boutons: Ouvrir et enregistrer
		JButton ouv = new JButton("Ouvrir");
		ouv.setBounds(20,20,120,30);
		
		JButton save = new JButton("Enregistrer");
		save.setBounds(20, 60, 120, 30);
		
		secondFrame.add(ouv);
		secondFrame.add(save);
		secondFrame.setSize(180,150);
		secondFrame.setLayout(null);
		secondFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		secondFrame.setVisible(true);
		//Lorsqu'on clique le bouton ouvrir
		ouv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				//Ouvrir un file chooser
				int returnValue = jfc.showOpenDialog(null);
				//Lire le fichier
				if(returnValue == JFileChooser.APPROVE_OPTION) {
					try {
						File selectedFile = jfc.getSelectedFile();
						LectureFichier lec = new LectureFichier();
						String output = lec.lire_lignes(selectedFile.getAbsolutePath());
						text.setText(output);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				secondFrame.dispose();
			}
		});
		//Lorsqu'on clique le bouton enregistrer
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				//Ouvrir un file chooser
			    int returnValue = jfc.showSaveDialog(null);
			    //Write le fichier
			    if (returnValue == JFileChooser.APPROVE_OPTION) {
			        try {
			            FileWriter writer = new FileWriter(jfc.getSelectedFile()+".txt");
		            writer.write(text.getText());
		            writer.close();
				    } catch (Exception ex) {
				    	ex.printStackTrace();
			        }
		    }
				    secondFrame.dispose();
			}
		});
	}
	});
	
	//Lorsqu'on clique le bouton dictionnaire
	b.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JFileChooser jfc = new JFileChooser();
			//Ouvrir un file chooser
			int returnValue = jfc.showSaveDialog(null);
			//Write le fichier
			if(returnValue == JFileChooser.APPROVE_OPTION) {
				try {
					File selectedFile = jfc.getSelectedFile();
					Dictionnaire d = new Dictionnaire();
					d.indexer_dictionnaire(selectedFile.getAbsolutePath());
					dict = d.dict;	//Stocker le fichier dans la variable static
					//System.out.println(dict.get(1));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	});
	
	//Lorsqu'on clique le bouton vérifier
	c.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Verify ve = new Verify();
			ve.verify_lignes(text);
		}
	});
	}
}

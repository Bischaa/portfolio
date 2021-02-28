import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JTextArea;

public class Verify extends Menu {
	ArrayList<String> dictionnaire = Menu.dict;
	public void verify_lignes(JTextArea text) {
		 
		//Lire ce qui se trouve dans le TextArea et tokeniser
		ArrayList<String> words = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(text.getText());
	    while (st.hasMoreTokens()) {
	         words.add(st.nextToken().replaceAll("\\p{Punct}", ""));
	    }
	    
	    for (int i = 0; i < words.size(); i++) {
	    	boolean verify = false;
	    	for (int j = 0; j < dictionnaire.size(); j++) {
	    		if (words.get(i).equalsIgnoreCase(dictionnaire.get(j))) {
	    			verify = true;
	    		}
	    	}
	    	if (verify == false) {
	    		//highlight le mot
	    		System.out.println(words.get(i));
	    	}
	    }
	}
}

import vue.Application;
import vue.LogicielAdministration;
import vue.Vue;

import java.util.Scanner;

public class Main {

	private static Vue vue;

	public static void main(String[] args) throws Exception
	{
		boolean loop = true;

		while(loop)
		{
			System.out.println("\n----- Interface ------");
			System.out.println("1. Application");
			System.out.println("2. Logiciel administratif");
			System.out.println("3. Quitter");

			System.out.print("Sélectionnez une option de simulation: ");
			int option = new Scanner(System.in).nextInt();

			switch(option)
			{
				case 1:
					vue = new Application();
					vue.simulation();
					break;
				case 2:
					vue = new LogicielAdministration();
					vue.simulation();
					break;
				case 3:
				default:
					vue = null;
					loop = false;
					break;
			}
		}
	}

}

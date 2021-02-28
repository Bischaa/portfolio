/*
Modifier par:
-Maxime Ton : 20143044
-Pierre-Olivier Tremblay: 20049076
*/

TP3.Lindenmayer = {

	//Vérifier si une clé est dans un dict : key in dict  , retourne vrai ou faux
	/* Pour itéré dans un string:
	for (var i=0; i<str.length; i++){
		str.charAt(i)
	}*/

	//Nous allons faire l'implémentation naïve
	iterateGrammar: function (str, dict, iters) {

		var strFin = str; //La chaîne finale
		var strTemp = ""; //Une chaîne temporaire

		//Boucle pour le nombre d'itérations totale
		for (var i = 0; i < iters; i++) {

			//Boucle sur les caractères
			strTemp = "";
			for (var j = 0; j < strFin.length; j++) {
				var car = strFin.charAt(j);

				//Si le caractère est dans le dictionnaire
				if (car in dict) {
					strTemp = strTemp.concat(dict[car]["default"]); //Ajoute la valeur de remplacement
				} else {
					strTemp = strTemp.concat(car);
				}
			}
			strFin = strTemp; //Nouvelle chaîne finale
		}
		return strFin;
	},

	//Nous allons faire l'implémentation naïve
	iterateGrammarProb: function (str, dict, iters) {
		var strFin = str; //La chaîne finale
		var strTemp = ""; //Une chaîne temporaire

		//Boucle pour le nombre d'itérations totale
		for (var i = 0; i < iters; i++) {

			//Boucle sur les caractères
			strTemp = "";
			for (var j = 0; j < strFin.length; j++) {
				var car = strFin.charAt(j);

				//Si le caractère est dans le dictionnaire
				if (car in dict) {
					//S'il contient plusieurs valeurs possibles avec probabilité
					if ("prob" in dict[car]){
						var randomNum = Math.random(); //Nombre entre [0,1[ pour la probabilité

						var lim=0; //Limite supérieur pour les probabilités
						//Boucle sur les probabilités
						for (var p = 0; p<dict[car]["prob"].length; p++){
							lim += dict[car]["prob"][p]; //Ajustement de la limite

							//Si randomNum est dans la prob
							if(randomNum < lim){
								//On prend le remplacement correspondant
								strTemp = strTemp.concat(dict[car]["val"][p]);
								break;
							} //Sinon on continue de chercher le remplacement
						}
					}
					//Valeur par défault sinon
					else {
						strTemp = strTemp.concat(dict[car]["default"]); //Ajoute la valeur de remplacement
					}
				} else {
					strTemp = strTemp.concat(car);
				}
			}
			strFin = strTemp; //Nouvelle chaîne finale
		}
		return strFin;
	}
}
/*

//S'il contient plusieurs valeurs possibles avec probabilité
if ("prob" in dict[car]){
	var randomNum = Math.random(); //Nombre entre [0,1[ pour la probabilité

	var lim=0; //Limite supérieur pour les probabilités
	//Boucle sur les probabilités
	for (var p = 0; p<dict[car]["prob"].length; p++){
		lim += dict[car]["prob"][p]; //Ajustement de la limite

		//Si randomNum est dans la prob
		if(randomNum < lim){
			//On prend le remplacement correspondant
			strTemp = strTemp.concat(dict[car]["val"][p]);
			break;
		} //Sinon on continue de chercher le remplacement
	}
}
//Valeur par défault sinon
else{

 */
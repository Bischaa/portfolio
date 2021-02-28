/* devoir2.js
 * Auteur: Maxime Ton
 * Ensemble de fonctions permettant d'analyser des tweets
 */

load("Devoir2/tweets.js");

var print = function(string) {
 	console.log(string);
}																				 //À désactiver si sur codeboot.

//Fonction getTweetsEcrisPar

var getTweetsEcrisPar = function(id){
	var tab = [];
	for(var i = 0; i < tweets.length; i++) {									//On observe chaque objet du tableau tweets
		if(tweets[i].Auteur.ID === id){											//Si on trouve l'id cherché
			//print(tweets[i].Auteur.ID);
			//print(tweets[i].IdTweets);
			tab.push(i);														//On ajoute l'indice du tweet au tableau
		}
	}
	//print(tab[0]);
	tab.sort(function(a, b){return a - b});										//On tri le tableau en ordre croissant d'indices
	//print(tab);
	return(tab);
}

//Fonction getTweetsAvecHTag

var getTweetsAvecHTag = function(tag){
	var tab = [];
	for(var i = 0; i < tweets.length; i++) {									//On observe chaque objet du tableau tweets
		for(var j = 0; j < tweets[i].Hashtags.length; j++) {					//On observe chaque hashtag du tweet observé
			if(tweets[i].Hashtags[j] == tag) {									//Si on trouve le hashtag cherché
				tab.push(i);													//On ajoute l'indice du tweet au tableau
			}
		}
	}
	tab.sort(function(a, b){return a - b});										//On tri le tablaeu en ordre croissant d'indices
	//print(tab);
	return(tab);
}

//Fonction getTweetsEcrisParAuteurPopulaire

var getTweetsEcrisParAuteurPopulaire = function(nb) {
	var tab = [];
	for(var i = 0; i < tweets.length; i++) {									//On observe chaque objet du tableau tweets
		if(tweets[i].Auteur.Friend_Count >= nb) {								//Si on trouve un utilisateur qui a au moins nb amis
			tab.push(i);														//On ajoute l'indice du tweet écrit par l'utilisateur au tableau
		}
	}
	tab.sort(function(a,b){return a-b});										//On tri le tableau en ordre croissant d'indices
	//print(tab);
	return(tab);
}

//Fonction getTweetsEnReponseAuTweet

var getTweetsEnReponseAuTweet = function(id) {
	var tab = [];
	for(var i = 0; i < tweets.length; i++) {									//On observe chaque objet du tableau tweets
		if(tweets[i].response_To_Tweet === id) {								//Si on trouve un réponse au tweet cherché
			tab.push(i);														//On ajoute l'indice de la réponse au tableau
		}
	}
	tab.sort(function(a,b){return a-b});										//On tri le tableau en ordre croissant
	//print(tab);
	return(tab);
}

//Fonction getHTags

var getHTags = function(nb) {
	var tab = [];
	for(var i = 0; i < tweets.length; i++){										//On observe chaque objet du tableau tweets
		for(var j = 0; j < tweets[i].Hashtags.length; j++){						//On observe chaque hashtag du tweet observé
			var b = false;														//Variable booléenne nous permettant de dire si nous avons observé un hashtag spécifique
			for(var k = 0; k <= tab.length; k++) {								//On observe chaque valeur de notre tableau
				if((k === tab.length) && (b === false)) {						//Si on a observé tout le tableau et le hashtag n'est pas inclu
					var Ht = function(hashtag) {								//Constructeur d'un objet contenant le hashtag et combien de fois qu'on le retrouve
						this.hashtag = hashtag;
						this.frequence = 1;
					}
					var a = new Ht(tweets[i].Hashtags[j]);						//Création d'un objet ayant comme valeur le hashtag observé
					tab.push(a);												//On ajoute l'objet dans notre tableau
					b = true;													//Le hashtag a été observé
				}
				else if(tab[k] === undefined) {									//Si nous avons fini le tableau
					break;														//On sort de la boucle
				}
				else if(tweets[i].Hashtags[j] === tab[k].hashtag) {				//Si on trouve le hashtag observé dans notre tableau
					tab[k].frequence += 1;										//On ajoute 1 à sa fréquence
					b = true;													//Le hashtag a été observé
				}
			}
		}
	}
	var objectsort = function(a,b){												//Fonction qui nous permet d'utiliser sort sur tab.frequence
		if(a.frequence > b.frequence) {
			return -1;
		}
		if(a.frequence < b.frequence) {
			return 1;
		}
		return 0;
	}
	tab.sort(objectsort);														//On tri le tableau en ordre décroissant de fréquence
	//print(tab);
	return(tab.slice(0,nb));
}

//Fonction getHTagFrequency

var getHTagFrequency = function(htag) {
	var tab = [];
	for(var i = 0; i < tweets.length; i++){
		for(var j = 0; j < tweets[i].Hashtags.length; j++){
			var b = false;
			for(var k = 0; k <= tab.length; k++) {
				if((k === tab.length) && (b === false)) {
					var Ht = function(hashtag) {
						this.hashtag = hashtag;
						this.frequence = 1;
					}
					var a = new Ht(tweets[i].Hashtags[j]);
					tab.push(a);
					b = true;
				}
				else if(tab[k] === undefined) {
					break;
				}
				else if(tweets[i].Hashtags[j] === tab[k].hashtag) {
					tab[k].frequence += 1;
					b = true;
				}																//Même fonction de création du tableau vu dans getHTags
			}
		}
	}
	var a = 0;																	//Si le hashtag n'est pas trouver, la fonction retourne 0
	for(var l = 0; l < tab.length; l++) {										//On observe chaque hashtag dans le tableau
		if(tab[l].hashtag === htag) {											//Si on trouve le hashtag cherché, on retourne sa fréquence
			a = tab[l].frequence;
		}
	}
	return a;
}

//Fonction getAuteurs

var getAuteurs = function() {
	var tab = [];
	for(var i = 0; i < tweets.length; i++){										//On observe chaque objet dans le tableau tweets
		var b = false;															//Variable booléenne nous permettant de dire si nous avons observé un message spécifique
		for(var k = 0; k <= tab.length; k++) {									//On observe chaque valeur de notre tableau
			if((k === tab.length) && (b === false)) {							//Si on a observé tout le tableau et le message n'est pas inclu
				var Aut = function(auteur, message) {							//Constructeur d'un objet contenant le nom de l'auteur, le nombre de tweets et le texte des tweets
					this.auteur = auteur;
					this.nbmessages = 1;
					this.messages = message;
				}
				var a = new Aut(tweets[i].Auteur.ID, tweets[i].Text);			//Création d'un objet avec le premier message de l'auteur
				tab.push(a);													//On ajoute l'objet dans notre tableau
				b = true;														//Le message a été observé
			}
			else if(tab[k] === undefined) {										//Si nous avons fini le tableau
				break;															//On sort de la boucle
			}
			else if(tweets[i].Auteur.ID === tab[k].auteur) {					//Si on trouve un autre message par un auteur déjà dans le tableau
				tab[k].nbmessages += 1;											//On augmente le nombre de tweets d'un
				tab[k].messages += " "+tweets[i].Text;							//On ajoute le message aux messages de l'auteur
				b = true;														//Le message a été observé
			}
		}
	}
	tab.sort(function(a,b){return ((a.auteur).toUpperCase()).localeCompare((b.auteur).toUpperCase())});		//On tri le tableau en ordre lexicographique
	return(tab);
}

//Fonction getWords

var getWords = function(id,nb) {
	var tabAuteurs = getAuteurs();												//On rappelle la dernière fonction qiu nous donne le tableau avec le nom de l'auteur et ses tweets
	var tab = [];
	for(var i = 0; i < tabAuteurs.length; i++) {								//On observe chaque objet du tableau tweets
		if(tabAuteurs[i].auteur === id) {										//Si on trouve l'auteur voulu
			var mots = tabAuteurs[i].messages.split(" ");						//On sépare ses messages en mots individuels dans un tableau
			for(var j = 0; j < mots.length; j++){								//On observe chaque mot dans ce tableau
				var b = false;													//Variable booléenne nous permettantde dire si nous avons observés un mot précis
				for(var k = 0; k <= tab.length; k++){							//On observe chaque valeur de notre tableau
					if((k === tab.length) && (b === false)) {					//Si on a observé tout le tableau et le mot n'est pas inclu
						var Words = function(word) {							//Constructeur d'un objet contenant le mot et la fréquence à laquelle on le retrouve
							this.mot = word;
							this.frequence = 1;
						}
						var a = new Words(mots[j]);								//Création d'un nouvel objet ayant comme valeur le mot observé
						tab.push(a);											//On ajoute l'objet dans notre tableau
						b = true;												//Le mot a été observé
					}
					else if(tab[k] === undefined) {								//Si nous avons fini le tableau
						break;													//On sort de la boucle
					}
					else if(mots[j] === tab[k].mot) {							//Si on retrouve le mot plus d'une fois dans les messages de l'auteur
						tab[k].frequence += 1;									//On augmente la fréquence d'un
						b = true;												//Le mot a été observé
					}
				}
			}
		}
	}
	var objectsort = function(a,b){												//Fonction qui nous permet d'utiliser sort sur tab.fréquence
		if(a.frequence > b.frequence) {
			return -1;
		}
		if(a.frequence < b.frequence) {
			return 1;
		}
		return 0;
	}
	tab.sort(objectsort);
	return(tab.slice(0,nb));
}

//Fonction getChaines

var getChaines = function(nb, id) {
	var tab = [];
	var count = nb;																//On compte le nb demandé pour respecter la longueur de la chaine
	for(var i = 0; i < tweets.length; i++) {									//On observe chaque objet du tableau tweets
		var chaine = function(id) {
			if(tweets[i].IdTweets === id) {										//Si on trouve le tweet cherché dans le tableau tweets
				tab.push(id);													//On ajoute le id a notre tableau
				count--;
				id = tweets[i].response_To_Tweet;								//Le id devient le id du tweet auquel il est une réponse
				i = 0;															//On repart la recherche de notre élément
				return (id);
			}
			return(id);
		}
		id = chaine(id);														//On repart notre sous-fonction chaine "récusivement" avec notre nouvel id
		if(id === -1) {															//Si le tweet ne répond a rien
			return(tab);														//On sort
		} 
		if(count === 0) {														//Si on a atteint la longueur maximale de la chaine
			return(tab);														//On sort
		}
	}
	tab.push(id);																//On ajoute le dernier id enregistré.
	return(tab);
}
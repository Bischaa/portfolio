// ======================================================
// js / scripts.js
// ======================================================

// When document is ready
// ======================================================

/**
 * Execute all my functions.
 *
 * @param {*} data : Your aunt's data.
 */
var dynamicActions = function(data) {
  $(document).ready(function() {
    updateDocumentTitle();
    logo();
    accueiltitle();
    article1();
    article2();
    article3();
    recettestitle();
    recette1();
    recette2();
    recette3();
    recette4();
    recette5();
    recette6();
    recette7();
    recette8();
    recette9();
    recette10();
    recette11();
    recette12();
    // function2()...
    // function3()...
    // function4()...
    // etc.
  });
};

// My functions
// ======================================================

//ajax the JSON file
$.ajax({
    url: "https://api.jsonbin.io/b/5c951b129c83133c027b0810",
    type: "GET",
    contentType: "application/json; charset=utf-8",
    dataType: 'json',
    success: function(data){
    	console.log(data);
    	$(data.articles).each(function(index, value) {
    		console.log(value);
    	})
	}
  });

/**
 * Update the document's title by using the provided data
 * from my aunt.
 */
var updateDocumentTitle = function() {
  // Some code...
  document.title = data.documentTitle;
};

//Logo
var logo = function() {
	$("#nomtante").html(data.firstName+" "+data.lastName);
}

//Accueil
var accueiltitle = function() {
	$("#accueil-header-title").html(data.documentTitle);
}

//Article 1
var article1 = function() {
	$(".article1title").html(data.articles[0].title);
	$("#article1subtitle").html(data.articles[0].subtitle);
	$("#article1content").html(data.articles[0].content);
}

//Article 2
var article2 = function() {
	$(".article2title").html(data.articles[1].title);
	$("#article2subtitle").html(data.articles[1].subtitle);
	$("#article2content").html(data.articles[1].content);
}

//Article 3
var article3 = function() {
	$(".article3title").html(data.articles[2].title);
	$("#article3subtitle").html(data.articles[2].subtitle);
	$("#article3content").html(data.articles[2].content);
}

//Titre de la section recettes
var recettestitle = function() {
	$("#recettestitle").html(data.documentTitle);
}

//Recette 1
var recette1 = function() {
	$("#recette1img").html(data.recipes[0].imgUrl);
	$("#recette1description").html(data.recipes[0].description);
	$("#recette1prix").html(data.recipes[0].price);
}

//Recette 2
var recette2 = function() {
	$("#recette2img").html(data.recipes[1].imgUrl);
	$("#recette2description").html(data.recipes[1].description);
	$("#recette2prix").html(data.recipes[1].price);
}

//Recette 3
var recette3 = function() {
	$("#recette3img").html(data.recipes[2].imgUrl);
	$("#recette3description").html(data.recipes[2].description);
	$("#recette3prix").html(data.recipes[2].price);
}

//Recette 4
var recette4 = function() {
	$("#recette4img").html(data.recipes[3].imgUrl);
	$("#recette4description").html(data.recipes[3].description);
	$("#recette4prix").html(data.recipes[3].price);
}

//Recette 5
var recette5 = function() {
	$("#recette5img").html(data.recipes[4].imgUrl);
	$("#recette5description").html(data.recipes[4].description);
	$("#recette5prix").html(data.recipes[4].price);
}

//Recette 6
var recette6 = function() {
	$("#recette6img").html(data.recipes[5].imgUrl);
	$("#recette6description").html(data.recipes[5].description);
	$("#recette6prix").html(data.recipes[5].price);
}

//Recette 7
var recette7 = function() {
	$("#recette7img").html(data.recipes[6].imgUrl);
	$("#recette7description").html(data.recipes[6].description);
	$("#recette7prix").html(data.recipes[6].price);
}

//Recette 8
var recette8 = function() {
	$("#recette8img").html(data.recipes[7].imgUrl);
	$("#recette8description").html(data.recipes[7].description);
	$("#recette8prix").html(data.recipes[7].price);
}

//Recette 9
var recette9 = function() {
	$("#recette9img").html(data.recipes[8].imgUrl);
	$("#recette9description").html(data.recipes[8].description);
	$("#recette9prix").html(data.recipes[8].price);
}

//Recette 10
var recette10 = function() {
	$("#recette10img").html(data.recipes[9].imgUrl);
	$("#recette10description").html(data.recipes[9].description);
	$("#recette10prix").html(data.recipes[9].price);
}

//Recette 11
var recette11 = function() {
	$("#recette11img").html(data.recipes[10].imgUrl);
	$("#recette11description").html(data.recipes[10].description);
	$("#recette11prix").html(data.recipes[10].price);
}

//Recette 12
var recette12 = function() {
	$("#recette12img").html(data.recipes[11].imgUrl);
	$("#recette12description").html(data.recipes[11].description);
	$("#recette12prix").html(data.recipes[11].price);
}
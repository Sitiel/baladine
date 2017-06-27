function sendActions() {
	var recap_pub 		= "aucune pub n'a été placée";
	var recap_recettes 	= "";
	var recap_prod		= "";

	var actions = [];
	console.log(new_recettes);;
	for (var i in pubs) {
		pub = "un total de "+(i+1)+" ont été placée(s)";
		var l = {
			latitude : pubs[i]["x"] / canvas.width * largeur_map,
			longitude: pubs[i]["y"] / canvas.height * longueur_map
		};
		actions.push({
			kind    : "ad",
			location: l,
			rayon   : pubs[i]['rayon'] / canvas.width * largeur_map
		});
	}
	for (i in new_recettes) {
		recap_recettes += "-->"+new_recettes[i]['nom'] + "\r\n"
		var ingredients = [];
		for (var ing_i in new_recettes[0]['ingredients']) {
			ingredients.push({
				name      : new_recettes[0]['ingredients'][ing_i],
				cost      : 0,
				hasAlcohol: false,
				isCold    : false
			});
		}
		var recipe = {
			name       : new_recettes[i]['nom'],
			ingredients: ingredients
		};
		actions.push({
			kind  : "recipe",
			recipe: recipe
		});
	}

	for (i in production) {
		recap_recettes += "-->"+production[i]['quantite'] + " " + production[i]['nom'] +" a " +production[i]['prix']+"€\r\n"
		var drinkName         = production[i]['nom'];
		var quantity          = production[i]['quantite'];
		var price             = production[i]['prix'];
		var prepare           = {};
		prepare[drinkName]    = quantity;
		var price_json        = {};
		price_json[drinkName] = price;
		var tmp               = {kind: "drinks"};
		tmp['prepare']        = prepare;
		tmp['price']          = price_json;
		actions.push(tmp);
	}


	$.ajax({
		type       : "POST",
		url        : "/ValerianKang/Balady_API/1.0.0/actions/" + pseudal,
		// The key needs to match your method's input parameter (case-sensitive).
		data       : JSON.stringify({actions: actions}),
		contentType: "application/json; charset=utf-8",
		dataType   : "json",
		success    : function (data) {
			alert("actions sauvegardées \r\n"+
					recap_pub+
					"Nouvelles recettes : "+recap_recettes+"\r\n"+
					"Production : "+recap_prod);
		}
	});
}

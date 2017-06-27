function sendActions() {
	var actions = [];
	console.log(new_recettes);;
	for (var i in pubs) {
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
			$("#send").attr("class", "btn btn-success");
			alert("actions sauvegardées");
		}
	});
}

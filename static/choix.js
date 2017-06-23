
var recettes = [];
var nb_produits = 0;

var production = [];
function getMesRecettes() {
	if (pseudal === "") {
		return 0;
	}
	$.ajax("/ValerianKang/Balady_API/1.0.0/map/" + pseudal)
		.done(function (data) {
			var recettes_joueur = data['playerInfo']['drinksOffered'];
			var tableau  = "";
			nb_produits = recettes_joueur.length;
			for (var i = 0; i< nb_produits ; i++) {
				if(recettes.indexOf(data['playerInfo']['drinksOffered'][i]['name']) == -1){
					tableau  += "<tr><td id=\"nom_"+i+"\">"+data['playerInfo']['drinksOffered'][i]['name']+"</td>";
					tableau  += "<td><input id=\"prod_"+i+"\" type=\"number\" placeholder=\"ex: 3\"></td>";
					tableau  += "<td><input id=\"prix_"+i+"\"type=\"text\" placeholder=\"ex: 0.15\">€/verre</td>";
					tableau  += "<td id=\"cout_"+i+"\">"+data['playerInfo']['drinksOffered'][i]['price']+"€/verre</td></tr>";
					recettes.push(data['playerInfo']['drinksOffered'][i]['name']);
				}
			}
			$("#mes_recettes").append(tableau);
		});
}

function savProd(){
	for (var i = 0; i< nb_produits ; i++) {
		if($("#prod_"+i).val() != "" && $("#prod_"+i).val() != 0 && $("#prix_"+i).val() != "" && $("#prix_"+i).val() != 0){
			var produit = {};
			produit['nom'] = $("#nom_"+i).html();
			produit['quantite'] = $("#prod_"+i).val();
			produit['prix'] = $("#prix_"+i).val();
			production.push(produit);
		}
	}
	console.log(production);
	sendAction();
}

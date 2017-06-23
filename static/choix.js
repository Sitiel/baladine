function getMesRecettes() {
	if (pseudal === "") {
		return 0;
	}
	$.ajax("/ValerianKang/Balady_API/1.0.0/map/" + pseudal)
		.done(function (data) {
			var recettes_joueur = data['playerInfo']['drinksOffered'];
			var tableau  = "Mes Recettes : <table><tr><td>Produit</td><td>Quantité</td><td>Prix</td><td>Cout production</td></tr>";
			for (var i = 0; i< recettes_joueur.length ; i++) {
				tableau  += "<tr><td>"+data['playerInfo']['drinksOffered'][i]['name']+"</td>";
				tableau  += "<td><input type=\"number\"></td>";
				tableau  += "<td>"+data['playerInfo']['drinksOffered'][i]['name']+"</td>";
				tableau  += "<td>"+data['playerInfo']['drinksOffered'][i]['price']+"€</td></tr>";
			}
			tableau  += "</table>";
			$("#mes_recettes").html(tableau);
		});
}

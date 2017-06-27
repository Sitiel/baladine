var nb_produits = 0;

function savProd(){
	for (var i = 0; i< nb_produits ; i++) {
		if($("#prod_"+i).val() != "" && $("#prod_"+i).val() != 0 && $("#prix_"+i).val() != "" && $("#prix_"+i).val() != 0){
			var produit = {};
			produit['nom'] = $("#nom_"+i).html();
			produit['quantite'] = $("#prod_"+i).val();
			produit['prix'] = $("#prix_"+i).val();
			production.push(produit);

			$("#prod_"+i).attr('placeholder', 'last : '+$("#prod_"+i).val());
			$("#prix_"+i).attr('placeholder', 'last : '+$("#prix_"+i).val());
		}
	}
	sendActions();
}

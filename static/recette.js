$.ajax("http://balady.herokuapp.com/ValerianKang/Balady_API/1.0.0/ingredients")
	 .done(function(data){
		for(var i = 0; i<data['ingredients'].length; i++){
			$("#ingredients").append('<div id="'+i+'" class="col-sm-3" onclick="addIngr('id+','+data['ingredients'][i]['nom']+', '+data['ingredients'][i]['cout']+')">'+data['ingredients'][i]['nom']+'</div>')	
		}
	});

ingredients = []
var dev = 0;
var prod = 0;
refresh_prices()

function refresh_prices(){
	dev = Math.pow(ingredients.length,2)
	$("#cout_dev").html(dev.toFixed(2));
	$("#cout_prod").html(Math.abs(prod).toFixed(2));
}

function addIngr(id, nom, cout){
	if($("#"+id).css("color") != "rgb(0, 255, 0)"){
		$("#"+id).css("color","rgb(0, 255, 0)");
		ingredients.push(nom);
		prod += cout;
		
	}else{
		$("#"+id).css("color","rgb(0, 0, 0)");
		
		var i = ingredients.indexOf(nom);
		if(i != -1) {
			ingredients.splice(i, 1);
		}
		prod -= cout;
	}
	refresh_prices()
}

new_recettes = [];
function create(){
	var new_recette = {};
	new_recette['nom'] =  $("#nom_new_recette").val();
	new_recette['ingredients'] = ingredients;
	alert("votre recette : ["+new_recette['nom']+"] sera bien créée demain");
	new_recettes.push(new_recette);
	
}

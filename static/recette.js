$.ajax("http://balady.herokuapp.com/ValerianKang/Balady_API/1.0.0/ingredients")
	 .done(function(data){
		for(var i = 0; i<data['ingredients'].length; i++){
			$("#ingredients").append('<div id="'+i+'" class="col-sm-3" onclick="addIngr('+i+', '+data['ingredients'][i]['cout']+')">'+data['ingredients'][i]['nom']+'</div>')	
		}
	});

ingredients = []
var dev = 0;
var prod = 0;
refresh_prices()

function refresh_prices(){
	dev = Math.pow(ingredients.length,2)
	$("#cout_dev").html(dev);
	$("#cout_prod").html(prod);
}

function addIngr(id, cout){
	if($("#"+id).css("color") != "rgb(0, 255, 0)"){
		$("#"+id).css("color","rgb(0, 255, 0)");
		ingredients.push(id);
		prod += cout;
		
	}else{
		$("#"+id).css("color","rgb(0, 0, 0)");
		
		var i = ingredients.indexOf(id);
		if(i != -1) {
			ingredients.splice(i, 1);
		}
		prod -= cout;
	}
	refresh_prices()
}

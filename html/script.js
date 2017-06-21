/*
$.ajax("http://127.0.0.1:5000/dayinfo")
   .done(function(data){
    $("#day").html(data['day']);
    $("#weather").html(data['weather']);
    $("#money").html(data['budget'].toFixed(2));
    $("#gameover").html(data['game']);  
   });
*/
$("#day").html(
 Math.floor((Math.random() * 29) + 1)+"/"
 +Math.floor((Math.random() * 12) + 1)+"/2017"
);

$("#hour").html(
 Math.floor((Math.random() * 23))+":"
 +Math.floor((Math.random() * 59) + 0)
);

$("#weather").html("SUNNY");

$("#futur_weather").html("CLOUD");




$("#recettes_en_ventes").html();
for(var i = 1; i<51; i++){
	var price = (Math.random() * (1.0 - 0.01) + 0.01)
	var sales = Math.floor((Math.random() * 10) + 1);
	$("#recettes_en_ventes").append("<li>Recette "+i+" => "+(price*sales).toFixed(2)+"€ ("+sales+" verres)</li>");
}


$("#budget").html((Math.random() * (100 - 0.01) + 0.01).toFixed(2));
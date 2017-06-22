setInterval(function (){ 
	$.ajax("http://balady.herokuapp.com/ValerianKang/Balady_API/1.0.0/meteorology")
	 .done(function(data){
		var hour = data['timestamp']%24
		console.log(data['timestamp']);
		if(hour < 10){
			hour = "0"+hour
		}
		$("#hour").html(hour+":00");
		$("#weather").html(data['weather']['0']['weather']);
		$("#futur_weather").html(data['weather']['1']['weather']);
	   });
}, 1000);


$("#day").html(
 Math.floor((Math.random() * 29) + 1)+"/"
 +Math.floor((Math.random() * 12) + 1)+"/2017"
);


$("#recettes_en_ventes").html();
for(var i = 1; i<51; i++){
	var price = (Math.random() * (1.0 - 0.01) + 0.01)
	var sales = Math.floor((Math.random() * 10) + 1);
	$("#recettes_en_ventes").append("<li>Recette "+i+" => "+(price*sales).toFixed(2)+"€ ("+sales+" verres)</li>");
}


$("#budget").html((Math.random() * (100 - 0.01) + 0.01).toFixed(2));

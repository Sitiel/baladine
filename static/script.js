var begin_day = 22;
var begin_month = 6;
var begin_year = 2017;


setInterval(function (){ 
	$.ajax("http://balady.herokuapp.com/ValerianKang/Balady_API/1.0.0/meteorology")
	 .done(function(data){
		var hour = data['timestamp']%24
		var day = begin_day + Math.floor(data['timestamp']/24)
		var month = begin_month + Math.floor(day/30) ;
		var year = begin_year + Math.floor(month/12);

		day = 1+day%30;
		month = month%12;

		if(hour < 10){
			hour = "0"+hour
		}
		if(day < 10){
			day = "0"+day
		}
		if(month < 10){
			month = "0"+month
		}

		$("#hour").html(hour+":00");
		$("#weather").html(data['weather']['0']['weather']);
		$("#futur_weather").html(data['weather']['1']['weather']);
		$("#day").html(day+"/"+month+"/"+year
		);
	   });
}, 1000);


$("#recettes_en_ventes").html();
for(var i = 1; i<51; i++){
	var price = (Math.random() * (1.0 - 0.01) + 0.01)
	var sales = Math.floor((Math.random() * 10) + 1);
	$("#recettes_en_ventes").append("<li>Recette "+i+" => "+(price*sales).toFixed(2)+"€ ("+sales+" verres)</li>");
}


$("#budget").html((Math.random() * (100 - 0.01) + 0.01).toFixed(2));

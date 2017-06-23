var begin_day   = 22;
var begin_month = 6;
var begin_year  = 2017;
var pseudal     = "";

var page = 0;

setPage("map_page");
$("#info_bar").hide();
$("#button_menu").show();

function setPage(page){
	$("#b_creation_recette").attr("class", "btn btn-primary");//debug
	$("#b_map_page").attr("class", "btn btn-primary");//debug
	$("#b_choix_page").attr("class", "btn btn-primary");//debug

	$("#creation_recette").hide();
	$("#choix_page").hide();
	$("#map_page").hide();
	$("#pub").hide();
	
	$("#"+page).show();
	$("#b_"+page).attr("class", "btn btn-secondary"); //debug
	if(page === "map_page"){
		$("#pub").show();
	}
}

function login() { 	
	$.ajax({
		type       : "POST",
		url        : "/ValerianKang/Balady_API/1.0.0/players",
		// The key needs to match your method's input parameter (case-sensitive).
		data       : JSON.stringify({name: $("#username").val()}),
		contentType: "application/json; charset=utf-8",
		dataType   : "json",
		success    : function (data) {
			pseudal = data["name"];
			$('#modal_login').modal('hide');
			getMap();
		}
	});
	setInterval(getMap, 2000);
	return false;
}

setInterval(function () {
	$.ajax("http://balady.herokuapp.com/ValerianKang/Balady_API/1.0.0/meteorology")
		.done(function (data) {
			var hour  = data['timestamp'] % 24;
			var day   = begin_day + Math.floor(data['timestamp'] / 24);
			var month = begin_month + Math.floor(day / 30);
			var year  = begin_year + Math.floor(month / 12);

			day   = 1 + day % 30;
			month = month % 12;

			if (hour < 10) {
				hour = "0" + hour
			}
			if (day < 10) {
				day = "0" + day
			}
			if (month < 10) {
				month = "0" + month
			}

			$("#hour").html(hour + ":00");
			$("#weather").html(data['weather']['0']['weather']);
			$("#futur_weather").html(data['weather']['1']['weather']);
			$("#day").html(day + "/" + month + "/" + year
			);
		});
}, 1000);

function getMap() {
	if (pseudal === "") {
		return 0;
	}
	getMesRecettes();
	$.ajax("/ValerianKang/Balady_API/1.0.0/map/" + pseudal)
		.done(function (data) {
			network_map_items = [];
			largeur_map       = data['map']['region']['span']['latitudeSpan'];
			longueur_map      = data['map']['region']['span']['longitudeSpan'];
			canvas.getContext('2d').clearRect(0, 0, canvas.width, canvas.height);
			drawPub(canvas.getContext('2d'), pubs);
			var itemsByPlayer = data['map']['itemsByPlayer'];
			for (var pseudal_joueur in itemsByPlayer) {
				for (var mapItem in itemsByPlayer[pseudal_joueur]) {
					var location = itemsByPlayer[pseudal_joueur][mapItem]['location'];
					if (pseudal_joueur === pseudal) {
						addCircle(location['latitude'] / largeur_map * canvas.width, location['longitude'] / longueur_map * canvas.height, itemsByPlayer[pseudal_joueur][mapItem]['influence'], 0, 255, 0, 0.3);
					} else {
						addCircle(location['latitude'] / largeur_map * canvas.width, location['longitude'] / longueur_map * canvas.height, itemsByPlayer[pseudal_joueur][mapItem]['influence'], 255, 0, 0, 0.3);
					}
				}
			}
			$("#budget").html(data['playerInfo']['cash'].toFixed(2));
		});
}


$("#recettes_en_ventes").html();
for (var i = 1; i < 51; i++) {
	var price = (Math.random() * (1.0 - 0.01) + 0.01);
	var sales = Math.floor((Math.random() * 10) + 1);
	$("#recettes_en_ventes").append("<li>Recette " + i + " => " + (price * sales).toFixed(2) + "€ (" + sales + " verres)</li>");
}

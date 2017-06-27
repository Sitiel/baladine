var begin_day   = 27;
var begin_month = 6;
var begin_year  = 2017;
var pseudal     = "";
var page 		= 0;

setPage("map_page");

function setPage(page) {

	$("#creation_recette").hide();
	$("#choix_page").hide();
	$("#map_page").hide();
	$("#pub").hide();

	$("#" + page).show();
	if (page === "map_page") {
		$("#pub").show();
	}
}


var lastDay = 0;
setInterval(function () {
	$.ajax("/ValerianKang/Balady_API/1.0.0/meteorology")
		.done(function (data) {
			if (data === 0) {
				return;
			}
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

			if (day !== lastDay) {
				pubs = [];
				new_recettes = [];
				production   = [];
				$("#file_attente").html("");
				for (var i = 0; i< nb_produits ; i++) {
					$("#prod_"+i).val() = "";
					$("#prix_"+i).val() = "";
				}
			}
			lastDay = day;

			$("#hour").html(hour + ":00");
			$("#weather").html(data['weather']['0']['weather']);
			$("#futur_weather").html(data['weather']['1']['weather']);
			$("#day").html(day + "/" + month + "/" + year
			);
		});
	getMessageFromChat();
}, 1000);



function getMap() {
	if (pseudal === "") {
		return 0;
	}
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
						addCircle(location['latitude'] / largeur_map * canvas.width, location['longitude'] / longueur_map * canvas.height, itemsByPlayer[pseudal_joueur][mapItem]['influence'] / largeur_map * canvas.width, 0, 255, 0, 0.3);
					} else {
						addCircle(location['latitude'] / largeur_map * canvas.width, location['longitude'] / longueur_map * canvas.height, itemsByPlayer[pseudal_joueur][mapItem]['influence'] / longueur_map * canvas.height, 255, 0, 0, 0.3);
					}
				}
			}
			$("#budget").html(data['playerInfo']['cash'].toFixed(2));
			budget = data['playerInfo']['cash'].toFixed(2);
			var recettes_joueur = data['playerInfo']['drinksOffered'];
			var tableau         = "";
			nb_produits         = recettes_joueur.length;
			for (var i = 0; i < nb_produits; i++) {
				if (recettes.indexOf(data['playerInfo']['drinksOffered'][i]['name']) == -1) {
					tableau += "<tr><td id=\"nom_" + i + "\">" + data['playerInfo']['drinksOffered'][i]['name'] + "</td>";
					tableau += "<td><input id=\"prod_" + i + "\" type=\"number\" placeholder=\"ex: 3\"></td>";
					tableau += "<td><input id=\"prix_" + i + "\"type=\"text\" placeholder=\"ex: 0.15\">€/verre</td>";
					tableau += "<td id=\"cout_" + i + "\">" + data['playerInfo']['drinksOffered'][i]['price'].toFixed(2) + "€/verre</td></tr>";
					recettes.push(data['playerInfo']['drinksOffered'][i]['name']);
				}
			}
			$("#mes_recettes").append(tableau);
		});
}

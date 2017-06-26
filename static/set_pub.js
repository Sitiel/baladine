$(document).ready(function () {
	$('#modal_login').modal({
		backdrop: 'static',
		keyboard: false
	});
	$("#pub").on('submit', active_pub);
});

var canvas  = document.getElementById('map');
var pressed = false;


var posX1        = 0;
var posY1        = 0;
var rayon        = 0;
var pubs         = [];
var production   = [];
var new_recettes = [];
var ingredients  = [];
var recettes     = [];
var budget       = 0;

var largeur_map  = 0;
var longueur_map = 0;

var setpub = false;
function active_pub() {
	if (!setpub) {
		$("#pub_button").attr("value", "Terminer");
		$("#pub_button").attr("class", "btn btn-danger");
	} else {
		$("#pub_button").attr("value", "Définir Publicité");
		$("#pub_button").attr("class", "btn btn-primary");
	}
	setpub = !setpub;
}


canvas.onmousemove = function (e) {
	if (canvas.getContext && setpub) {
		var ctx  = canvas.getContext('2d');
		var posX = (e.clientX * canvas.width) / $("#map").width();
		var posY = (e.clientY * canvas.height) / $("#map").height() - ($("#info_bar").height() * canvas.height) / $("#map").height();

		if (!pressed) {
			return;
		}

		rayon = Math.sqrt((posX - posX1) * (posX - posX1) + (posY - posY1) * (posY - posY1));
		ctx.clearRect(0, 0, canvas.width, canvas.height);

		drawPub(ctx, network_map_items);

		drawPub(ctx, pubs);
		var realRayon = rayon / canvas.width * largeur_map;
		var pub_price = Math.pow(realRayon, 1.8) / 2;
		pub_price     = pub_price.toFixed(2);
		//Les joies du JS -> Budget est un string pour je ne sais quelle raison
		if (parseInt(pub_price) > parseInt(budget)) {
			rayon     = ((Math.pow(budget * 2, 1 / 1.8)) / largeur_map) * canvas.width;
			pub_price = budget;
		}
		var metrics  = ctx.measureText("Prix : " + pub_price + "€");
		var textXpos = posX;
		if (textXpos + metrics.width > canvas.width) {
			textXpos -= ((textXpos + metrics.width) - canvas.width);
		}
		ctx.font         = '20px _sans';
		ctx.fillStyle    = '#000000';
		ctx.textBaseline = 'top';
		ctx.fillText("Prix : " + pub_price + "€", textXpos, posY);

		ctx.beginPath();
		ctx.moveTo(posX1 + rayon, posY1);
		ctx.arc(posX1, posY1, rayon, 0, 2 * Math.PI);
		ctx.fillStyle = "rgba(255,255,106,0.3)";
		ctx.fill();
		ctx.lineWidth = 0.3;
		ctx.stroke();
		ctx.closePath();
	}
};
canvas.onmousedown = function (e) {
	if (setpub) {
		posX1   = (e.clientX * canvas.width) / $("#map").width();
		posY1   = (e.clientY * canvas.height) / $("#map").height() - ($("#info_bar").height() * canvas.height) / $("#map").height();
		pressed = true;
	}
};

canvas.onmouseup = function () {
	if (setpub) {
		pressed = false;

		if (rayon > 5) {
			var publicite      = {};
			publicite["x"]     = posX1;
			publicite["y"]     = posY1;
			publicite["rayon"] = rayon;
			publicite["color"] = "rgba(51,204,255,0.3)";
			pubs.push(publicite);
		}
		rayon = 0;
		posX1 = 0;
		posY1 = 0;
		//addCircle(posX1, posY1, rayon, 0, 200, 0, 0.3);
	}
};


function drawPub(ctx, _pubs) {
	for (var i = 0; i < _pubs.length; i++) {
		ctx.beginPath();
		ctx.moveTo(_pubs[i].x + _pubs[i].rayon, _pubs[i].y);
		ctx.arc(_pubs[i].x, _pubs[i].y, _pubs[i].rayon, 0, 2 * Math.PI);
		ctx.fillStyle = _pubs[i].color;
		ctx.fill();
		ctx.lineWidth = 0.3;
		ctx.stroke();
		ctx.closePath();
	}
}


function getMessageFromChat() {
	$.ajax({
		type       : "GET",
		url        : "/ValerianKang/Balady_API/1.0.0/chat",
		contentType: "application/json; charset=utf-8",
		dataType   : "json",
		success    : function (data) {
			console.log(data);
		}
	});
}

function postAChatMessage(message) {
	if (pseudal === "") {
		return;
	}
	$.ajax({
		type       : "POST",
		url        : "/ValerianKang/Balady_API/1.0.0/chat",
		data       : JSON.stringify({
			sender : pseudal,
			message: message
		}),
		contentType: "application/json; charset=utf-8",
		dataType   : "json",
		success    : function (data) {
		}
	});
}


function sendActions() {
	var actions = [];
	for (var i in pubs) {
		var l = {
			latitude : pubs[i]["x"] / canvas.width * largeur_map,
			longitude: pubs[i]["y"] / canvas.height * longueur_map
		};
		actions.push({
			kind    : "ad",
			location: l,
			rayon   : pubs[i]['rayon'] / canvas.width * largeur_map
		});
	}
	for (i in new_recettes) {
		var ingredients = [];
		for (var ing_i in new_recettes[0]['ingredients']) {
			ingredients.push({
				name      : new_recettes[0]['ingredients'][ing_i],
				cost      : 0,
				hasAlcohol: false,
				isCold    : false
			});
		}
		var recipe = {
			name       : new_recettes[i]['nom'],
			ingredients: ingredients
		};
		actions.push({
			kind  : "recipe",
			recipe: recipe
		});
		//alert(JSON.stringify(actions));
	}

	for (i in production) {
		var drinkName         = production[i]['nom'];
		var quantity          = production[i]['quantite'];
		var price             = production[i]['prix'];
		var prepare           = {};
		prepare[drinkName]    = quantity;
		var price_json        = {};
		price_json[drinkName] = price;
		var tmp               = {kind: "drinks"};
		tmp['prepare']        = prepare;
		tmp['price']          = price_json;
		actions.push(tmp);
	}


	$.ajax({
		type       : "POST",
		url        : "/ValerianKang/Balady_API/1.0.0/actions/" + pseudal,
		// The key needs to match your method's input parameter (case-sensitive).
		data       : JSON.stringify({actions: actions}),
		contentType: "application/json; charset=utf-8",
		dataType   : "json",
		success    : function (data) {
			new_recettes = [];
			production   = [];
		}
	});
}

var network_map_items;

function addCircle(x, y, rayon, r, g, b, a) {
	var publicite      = {};
	publicite["x"]     = x;
	publicite["y"]     = y;
	publicite["rayon"] = rayon;
	publicite["color"] = "rgba(" + r + "," + g + "," + b + "," + a + ")";
	var ctx            = canvas.getContext('2d');
	ctx.beginPath();
	ctx.moveTo(x + rayon, y);
	ctx.arc(x, y, rayon, 0, 2 * Math.PI);
	ctx.fillStyle = publicite["color"];
	ctx.fill();
	ctx.lineWidth = 0.3;
	ctx.stroke();
	ctx.closePath();
	network_map_items.push(publicite);
}



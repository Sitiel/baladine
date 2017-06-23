$(document).ready(function () {
	$('#modal_login').modal({
		backdrop: 'static',
		keyboard: false
	});
	$("#pub").on('submit', active_pub);
});

var canvas  = document.getElementById('map');
var pressed = false;


var posX1 = 0;
var posY1 = 0;
var rayon = 0;
var pubs  = [];

var largeur_map = 0;
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

		ctx.beginPath();
		ctx.moveTo(posX1 + rayon, posY1);
		ctx.arc(posX1, posY1, rayon, 0, 2 * Math.PI);
		ctx.fillStyle = "rgba(0,200,0,0.3)";
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


function sendActions() {
	var pubs_actions = [];
	for (var i in pubs) {
		var l = {latitude: pubs[i]["x"]/canvas.width*largeur_map, longitude: pubs[i]["y"]/canvas.height*longueur_map};
		pubs_actions.push({ kind: "ad", location: l, rayon: pubs[i]['rayon']});
	}
	$.ajax({
		type       : "POST",
		url        : "/ValerianKang/Balady_API/1.0.0/actions/" + pseudal,
		// The key needs to match your method's input parameter (case-sensitive).
		data       : JSON.stringify({actions: pubs_actions}),
		contentType: "application/json; charset=utf-8",
		dataType   : "json",
		success    : function (data) {
			pubs = [];
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


canvas.onmouseup = function () {
	if (setpub) {
		pressed            = false;
		var publicite      = {};
		publicite["x"]     = posX1;
		publicite["y"]     = posY1;
		publicite["rayon"] = rayon;
		publicite["color"] = "rgba(0,200,0,0.3)";
		pubs.push(publicite);
		sendActions();
		//addCircle(posX1, posY1, rayon, 0, 200, 0, 0.3);
	}
};
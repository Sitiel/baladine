//GET REQUEST
function get_data() {
	var url = 'http://localhost:5000/dayinfo';
	$.get(url, function (data, status) {
		document.getElementById('day').value     = data['day'];
		document.getElementById('weather').value = data['weather'];
		document.getElementById('money').value   = data['budget'];
		$('body').css('background-image', 'url(' + data['weather'].replace(/ /g, "_") + '.jpg' + ')');
	});
}


function new_game() {
	$.get('http://localhost:5000/newgame', function (data, status) {
		get_data();
	});
}


function get_last_days() {
	$.get('http://localhost:5000/getlastdays', function (data, status) {
		//document.getElementById('content_last_days').innerHTML = data;
		var SQLTable  = document.getElementById("SQLTable");
		var rowCount = SQLTable.rows.length;
		for (var x = rowCount - 1; x >= 0; x--) {
			SQLTable.deleteRow(x);
		}
		$.each(
			data['days'],
			function (i, v) {
				var table                       = document.getElementById("SQLTable");
				var new_row                     = table.children[0].insertRow(0);
				new_row.insertCell(0).innerHTML = v['id'];
				new_row.insertCell(1).innerHTML = v['budget'];
				new_row.insertCell(2).innerHTML = v['day'];
				new_row.insertCell(3).innerHTML = v['weather'];
				//$("#content_last_days").append("<li>" + v['weather'] + "</li>");
			}
		);
	});
}

//POST REQUEST
function post_data(url, requested_glasses) {
	if (requested_glasses === "") {
		requested_glasses = 0;
	}
	$.ajax({
		url        : url,
		type       : "POST",
		data       : JSON.stringify({
			'requested_glasses': requested_glasses
		}),
		contentType: "application/json; charset=utf-8",
		dataType   : "json",
		success    : function (data, status) {
			document.getElementById('sell').value = data['sales'];
			get_data();
		},
		error      : function () {
			document.getElementById('sell').value = "Fonds insuffisant";
		}
	});
}

//LOADING SCREEN
$(document).ready(function () {
	$('body').addClass('loaded');
});
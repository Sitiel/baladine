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

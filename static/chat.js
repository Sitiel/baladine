var nbmsg = 0
function getMessageFromChat() {
	$.ajax({
		type       : "GET",
		url        : "/ValerianKang/Balady_API/1.0.0/chat",
		contentType: "application/json; charset=utf-8",
		dataType   : "json",
		success    : function (data) {
		var chat = "<br />";
		if(nbmsg != data.length){
			nbmsg = data.length;
			for(var i = 0; i< data.length; i++){
				var msg = data[i]["message"].replace(/</g, '&lt;').replace(/>/g, '&gt;');
				chat += "<strong>"+data[i]["sender"] + "</strong> : "+ msg + "<br />";
			}
			$("#chat").html(chat);
			$("#chat_users").scrollTop($("#chat_users")[0].scrollHeight);
			
		}
	   }
	});
}

function sendMessage(){
	var msgTest = $("#message_send").val().replace(/ /g, '');
	if(msgTest != "" && $("#message_send").val().length < 25){
		var msg = $("#message_send").val().replace(/</g, '&lt;').replace(/>/g, '&gt;');
		postAChatMessage(msg);
		$("#message_send").val("");

	}
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

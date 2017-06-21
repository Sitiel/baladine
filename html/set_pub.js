$("img").mousedown(function(){
    return false;
});


  var canvas = document.getElementById('map');
  
  canvas.onmousedown = function(e) {
   if (canvas.getContext) {
    var ctx = canvas.getContext('2d');
	var posX = e.clientX //- $("#info_bar").height();
	var posY = e.clientY //- $("#info_bar").height();
	console.log(posX+"|"+posY)
    ctx.beginPath();
	ctx.arc(100, 100, 10, 0, 2 * Math.PI);
	ctx.stroke();

  }
}
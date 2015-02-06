$(window).load(function(){
	// We are listening for the window load event instead of the regular document ready.
	function animSteam(){
		// Create a new span with the steam1, or steam2 class:
		$('<span>', {
			className: 'steam' + Math.floor(Math.random() * 2 + 1),
			css:{
				// Apply a random offset from 10px to the left to 10px to the right
				marginLeft: -10 + Math.floor(Math.random() * 20)
			}
		}).appendTo('#rocket').animate({
			left:'-=58',
			bottom:'-=100'
		}, 120, function(){
			// When the animation completes, remove the span and
			// set the function to be run again in 10 milliseconds
			$(this).remove();
			setTimeout(animSteam, 10);
		});
	}

	function moveRocket(){
		$('#rocket').animate({'left': '+=100'}, 5000).delay(1000)
					.animate({'left': '-=100'}, 5000, function(){
			setTimeout(moveRocket, 1000);
		});
	}

	// Run the functions when the document and all images have been loaded.
	moveRocket();
	animSteam();
});
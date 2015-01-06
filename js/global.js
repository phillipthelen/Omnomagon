$(document).ready(function() {
	$('.gallery').before('<nav id="control"><a id="prev">prev</a><a id="next">next</a></nav>').cycle({
		activePagerClass	: 'activeSlide',
		speed				: 500,
		timeout				: 0,
		fx					: 'scrollLeft',
		prev				: '#prev',
		next				: '#next'
	});
	$(function() {
        $('#gallery div a').lightBox({
        	overlayBgColor: '#333',
			overlayOpacity: 0.75,
			containerResizeSpeed: 350,
			txtImage: 'Imagem',
			txtOf: 'de'
        });
    });
});

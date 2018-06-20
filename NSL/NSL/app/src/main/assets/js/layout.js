$( document ).ready(function() {
	
 $('input').on('keypress', function(e) {
        if (e.which == 32)
            return false;
    });	
	
   $(".navbar.navbar-static-top .sidebar-toggle").click(function(){

	if($(".content-wrapper").hasClass( "offwidth" )){
		$(".content-wrapper").css("margin-left", "0px").removeClass("offwidth").addClass("fullwidth"); 
		$(".main-sidebar").css("transform", "translate(-230px, 0px)"); 
		
		
	}else if($(".content-wrapper").hasClass( "fullwidth" )){
		$(".content-wrapper").css("margin-left", "230px").removeClass("fullwidth").addClass("offwidth"); 
		$(".main-sidebar").css("transform", "translate(0, 0px)"); 		
	}
	else{
		$(".content-wrapper").css("margin-left", "0px").removeClass("offwidth").addClass("fullwidth"); 
		$(".main-sidebar").css("transform", "translate(-230px, 0px)");		
	}
})

});


$(function()
{
	// Variable to store your files
	var files;

	// Add events
	$('input[type=file]').on('change', prepareUpload);
	$('form').on('submit', uploadFiles);

	// Grab the files and set them to our variable
	function prepareUpload(event)
	{
		files = event.target.files;
	}

	// Catch the form submit and upload the files
	function uploadFiles(event)
	{
		event.stopPropagation(); // Stop stuff happening
        event.preventDefault(); // Totally stop stuff happening

        // START A LOADING SPINNER HERE

        // Create a formdata object and add the files
		var data = new FormData();
		$.each(files, function(key, value)
		{
			data.append(key, value);
		});
        
        $.ajax({
            url: 'https://d1mnzch1.versacomllc.com:8443/easy-ecm-service/FileUploadServlet?workspace=ecm&path=MyDocument',
            type: 'POST',
            data: data,
            cache: false,
            dataType: 'json',
            processData: false, // Don't process the files
            contentType: false, // Set content type to false as jQuery will tell the server its a query string request
            }).done(function( data ) {
            	console.log( "Sample of data:"+ data );
	        	if ( console && console.log ) {
	        	}
        		if(data.success == 'true'){
        			console.log("Data saved on the server:");
        			alert("Data saveed on the server");
        		}
        		else{
        			console.log("Failed to save data on the server");
        			alert("Failed to save data on the server");
        		}
        });
    }

});
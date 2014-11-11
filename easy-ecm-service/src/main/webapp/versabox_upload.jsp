<%@ page import="java.net.URL"%>
<html>
<head>
<title>File upload</title>
<style>
body {
	padding: 30px
}

form {
	display: block;
	margin: 20px auto;
	background: #eee;
	border-radius: 10px;
	padding: 15px
}

.progress {
	position: relative;
	width: 400px;
	border: 1px solid #ddd;
	padding: 1px;
	border-radius: 3px;
}

.bar {
	background-color: #B4F5B4;
	width: 0%;
	height: 20px;
	border-radius: 3px;
}

.percent {
	position: absolute;
	display: inline-block;
	top: 3px;
	left: 48%;
}
</style>
</head>
<body>

	<input type="hidden" name="status" id="status" value="closed" />
	<input type="hidden" name="result" id="result" value="" />

	<%
		String api_key = request.getParameter("api_key");
		String api_secreet = request.getParameter("api_secreet");
		String path = request.getParameter("path");
		String callback = "finish.jsp";
		String params = "api_key=" + api_key + "&api_secreet="
				+ api_secreet + "&path=" + path + "&callback=" + callback;

		String uploadURL = null;
		if (api_key == null || api_secreet == null || path == null) {
			out.println("<b> Missing required parameters. Please contact customer support</b>!");
		} else {

			final String uploadPath = request.getContextPath()
					+ "/FileUploadServlet?" + params;

			URL url = new URL(request.getScheme(), request.getServerName(),
					request.getServerPort(), uploadPath);

			uploadURL = url.toString();
			/* out.println("Final url: " + uploadURL); */
		}
	%>

	<h1>File Upload</h1>
	<div id="idUploadContent">
		<form action="<%=uploadURL%>" method="post"
			enctype="multipart/form-data">
			<input type="file" name="myfile" multiple><br> <br>
			<input type="submit" value="Upload File to Server">
		</form>

		<div class="progress">
			<div class="bar"></div>
			<div class="percent">0%</div>
		</div>

	</div>
	

	
 	<div id="statusBar"></div> 
	<p id="IdBottomBar">
		<button onclick="myFunction(event, this);">Finish</button>

	</p>
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7/jquery.js"></script>
	<script src="http://malsup.github.com/jquery.form.js"></script>
	<script type="text/javascript" src="VersaBox.js">
		
	</script>
	<script>
		var hostName = VersaBox.getUrlParameters('origin', '', true);

		function myFunction(evt, elem) {
			document.getElementById("status").value = 'success';
			window.close();
		}

		var msgHandler = function(data) {
			//var div = document.querySelector('div');
			//div.innerHTML += data.content;
		}
		var resultHandler = function() {
			var result = [ {
				name : "Text1.txt",
				link : "http://localhost/Text3.txt"
			}, {
				name : "Text2.txt",
				link : "http://localhost/Text2.txt"
			} ];
			return result;
		}
		VersaBox.onMessageReceived(msgHandler, resultHandler, hostName);

		(function() {

			var bar = $('.bar');
			var percent = $('.percent');
			var contentDiv = $('#idUploadContent');
			var bottomBar = $('#IdBottomBar');
			bottomBar.hide();
			
			$('form').ajaxForm(
					{
						beforeSend : function() {
							//status.empty();
							var percentVal = '0%';
							bar.width(percentVal)
							percent.html(percentVal);
						},
						uploadProgress : function(event, position, total,
								percentComplete) {
							var percentVal = percentComplete + '%';
							bar.width(percentVal)
							percent.html(percentVal);
						},
						success : function() {
							var percentVal = '100%';
							bar.width(percentVal)
							percent.html(percentVal);

						},
						complete : function(xhr) {
							//status.html(xhr.responseText);

							contentDiv.hide();
							bottomBar.show();
							$('h1').text('Upload completed.');
							
						}
					});

		})();
	</script>


</body>
</html>
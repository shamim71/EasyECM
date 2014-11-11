(function() {
	var instance;
	
	if ((instance = window.VersaBox) == null) {
		window.VersaBox = {};
	}

	VersaBox.createPopupWindow = function(url, w, h) {
	    // Fixes dual-screen position                         Most browsers      Firefox  
	    var dualScreenLeft = window.screenLeft != undefined ? window.screenLeft : screen.left;  
	    var dualScreenTop = window.screenTop != undefined ? window.screenTop : screen.top;  
	              
	    width = window.innerWidth ? window.innerWidth : document.documentElement.clientWidth ? document.documentElement.clientWidth : screen.width;  
	    height = window.innerHeight ? window.innerHeight : document.documentElement.clientHeight ? document.documentElement.clientHeight : screen.height;  
	              
	    var left = ((width / 2) - (w / 2)) + dualScreenLeft;  
	    var top = ((height / 2) - (h / 2)) + dualScreenTop;  
	   var newWindow = window.open(url, "ChidWindow", "width=" + w + ", height=" + h + ", top=" + top + ", left=" + left +"");  
	    // Puts focus on the newWindow  
	    if (window.focus) {  
	        newWindow.focus();  
	    }  
	    
	};

	
	VersaBox.parseUri = function(str) {
		var options = {
			strictMode: false,
			key: ["source","protocol","authority","userInfo","user","password","host","port","relative","path","directory","file","query","anchor"],
			q:   {
				name:   "queryKey",
				parser: /(?:^|&)([^&=]*)=?([^&]*)/g
			},
			parser: {
				strict: /^(?:([^:\/?#]+):)?(?:\/\/((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?))?((((?:[^?#\/]*\/)*)([^?#]*))(?:\?([^#]*))?(?:#(.*))?)/,
				loose:  /^(?:(?![^:@]+:[^:@\/]*@)([^:\/?#.]+):)?(?:\/\/)?((?:(([^:@]*)(?::([^:@]*))?)?@)?([^:\/?#]*)(?::(\d*))?)(((\/(?:[^?#](?![^?#\/]*\.[^?#\/.]+(?:[?#]|$)))*\/?)?([^?#\/]*))(?:\?([^#]*))?(?:#(.*))?)/
			}
		};
		var	o   = options,
		m   = o.parser[o.strictMode ? "strict" : "loose"].exec(str),
		uri = {},
		i   = 14;

		while (i--) uri[o.key[i]] = m[i] || "";
	
		uri[o.q.name] = {};
		uri[o.key[12]].replace(o.q.parser, function ($0, $1, $2) {
			if ($1) uri[o.q.name][$1] = $2;
		});

	return uri;

	};
	//validate options
	VersaBox.validateOptions = function(options) {

		if (options.success == null) {
			showWarn("success callback function is required to get respnose from popup window");
		}
		if(options.dialog == null){
			showWarn("Dialog argument is missing in options");
		}
		if(options.dialog.path == null){
			showWarn("Dialog path is missing in options");
		}
		if(options.dialog.heigth == null){
			options.dialog.heigth = 300;
		}
		if(options.dialog.width == null){
			options.dialog.width = 500;
		}

		return options;
	};	
	
	VersaBox.postMessageToParentOn = function(element, event, message, originatingUrl) {
		
		var eventHandler = function(e) {
			e.currentTarget.opener.postMessage(message, originatingUrl);
		};
		VersaBox.addListener(element, event, eventHandler);
		
	};
	
	VersaBox.onMessageReceived = function(messageHandler,resultHandler,originatingUrl) {
		var msgHandler = function(e) {
			if (e.origin === originatingUrl) {
				messageHandler(e.data);
			} else {
				throw new Error('Origin domain is not allowed.');
			}
		};
		
		var documentReadyHandler = function(e) {
			var message = { status:'ready', result:null};

			e.currentTarget.opener.postMessage(message, originatingUrl);
		};
		
		var documentClosedHandler = function(e) {
			var closedStatus = document.getElementById("status").value;
			var message = { status:'closed', result:null};
			if(closedStatus =='success'){
				message.status= closedStatus;
				message.result = resultHandler();
			}
			
			e.currentTarget.opener.postMessage(message, originatingUrl);
		};
		
		//Initialize window message
		VersaBox.addListener(window, 'message', msgHandler);
		
		VersaBox.addListener(window, 'load', documentReadyHandler);
		VersaBox.addListener(window, 'unload', documentClosedHandler);
		
	};
	
	VersaBox.choose = function(options) {
		if (options == null) {
			options = {};
		}
		options = VersaBox.validateOptions(options);

		var parsedURI = VersaBox.parseUri(options.dialog.path);
		
		var host = parsedURI.protocol+'://'+ parsedURI.host +(parsedURI.port ? ':'+parsedURI.port: '');
		
		//Setting up parent url
		options.message.host =  host;
		
		var CrossDomainProcessor = {};

		CrossDomainProcessor.handler = function(event) {

			if (event.origin == host){
				switch (event.data.status) {
				
				case 'ready':
					event.source.postMessage(options.message, host);
					break;
					
				case 'success':
					VersaBox.removeListener(window, "message", CrossDomainProcessor.handler);
					if (typeof options.success === "function") {
						options.success(event.data.result);
					}
					break;
					
				case 'closed':
					//console.log("Child is closed.");
					VersaBox.removeListener(window, "message", CrossDomainProcessor.handler);
					
					if (typeof options.cancel === "function") {
						options.cancel();
					}

					
					break;
				}
			}

			
		
		};
		
		var childWindowUrl = VersaBox.updateQueryStringParameter(options.dialog.path,'origin', encodeURIComponent(host));
		//Initialize window message
		VersaBox.addListener(window, 'message', CrossDomainProcessor.handler);
		
		VersaBox.createPopupWindow(childWindowUrl, options.dialog.width,options.dialog.height);

	};
	VersaBox.updateQueryStringParameter = function(uri, key, value){
		
		var re = new RegExp("([?&])" + key + "=.*?(&|$)", "i");
		var separator = uri.indexOf('?') !== -1 ? "&" : "?";
		if (uri.match(re)) {
		    return uri.replace(re, '$1' + key + "=" + value + '$2');
		}
		else {
		    return uri + separator + key + "=" + value;
		}
	};
	VersaBox.getUrlParameters = function(parameter, staticURL, decode){

		   var currLocation = (staticURL.length)? staticURL : window.location.search,
		       parArr = currLocation.split("?")[1].split("&"),
		       returnBool = true;
		   
		   for(var i = 0; i < parArr.length; i++){
		        parr = parArr[i].split("=");
		        if(parr[0] == parameter){
		            return (decode) ? decodeURIComponent(parr[1]) : parr[1];
		            returnBool = true;
		        }else{
		            returnBool = false;            
		        }
		   }
		   
		   if(!returnBool) return false;  
	};
	
	VersaBox.showWarn = function(msg) {
		if (typeof console !== "undefined" && console !== null) {
			if (typeof console.warn === "function") {
				console.warn(msg);
			}
		}
	};
	VersaBox.addListener = function(element, eventName, eventHandler) {
		if (element.addEventListener) {
			element.addEventListener(eventName, eventHandler, false);
		} else {
			element.attachEvent("on" + eventName, function(event) {
				event.preventDefault = function() {
					return this.returnValue = false;
				};
				return eventHandler(event);
			});
		}
	};
	
	VersaBox.removeListener = function(element, eventName, eventHandler) {
		if (element.removeEventListener) {
			element.removeEventListener(eventName, eventHandler, false);
		} else {
			element.detachEvent("on" + eventName,eventHandler);
		}
	};
	
	VersaBox.VERSION = "2.0";

}).call(this);
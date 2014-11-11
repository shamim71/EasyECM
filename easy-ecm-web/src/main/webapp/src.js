dojo.require("dojo.parser");
dojo.require("dojo.data.ItemFileWriteStore");
dojo.require("dojo.date.locale");
dojo.require("dojo.date.stamp");

dojo.require("dijit.dijit");
dojo.require("dijit.Declaration");

dojo.require("dijit.Menu");
dojo.require("dijit.Tree");
dojo.require("dijit.Tooltip");
dojo.require("dijit.Dialog");
dojo.require("dijit.Toolbar");
dojo.require("dijit.Calendar");
dojo.require("dijit.ColorPalette");
dojo.require("dijit.Editor");
dojo.require("dijit._editor.plugins.LinkDialog");
dojo.require("dijit._editor.plugins.FontChoice");
dojo.require("dijit.ProgressBar");

dojo.require("dijit.form.ComboButton");
dojo.require("dijit.form.ComboBox");
dojo.require("dijit.form.CheckBox");
dojo.require("dijit.form.Textarea");
dojo.require("dijit.form.TextBox");
dojo.require("dijit.form.ComboBox");
dojo.require("dijit.form.FilteringSelect");
dojo.require("dijit.form.Textarea");
dojo.require("dijit.form.Form")

dojo.require("dijit.layout.BorderContainer");
dojo.require("dijit.layout.AccordionContainer");
dojo.require("dijit.layout.TabContainer");
dojo.require("dijit.layout.ContentPane");

dojo.require("dojox.grid.DataGrid");
dojo.require("dojox.widget.FisheyeLite");
dojo.require("dojox.analytics.Urchin");

dojo.addOnLoad(function(){

	dojo.parser.parse();
	dojo.body().setAttribute("role", "application");

	var n = dojo.byId("preLoader");
	dojo.fadeOut({
		node:n,
		duration:720,
		onEnd:function(){
			// dojo._destroyElement(n);
			dojo.style(n,"display","none");
		}
	}).play();


	
	// make tooltips go down (from buttons on toolbar) rather than to the right
	dijit.Tooltip.defaultPosition = ["above", "below"];
	
	// Write A-Z "links" on contactIndex tab to do filtering
	//genIndex();

	new dojox.analytics.Urchin({
		acct: "UA-3572741-1",
		GAonLoad: function(){
			this.trackPageView("/demos/dijitmail");
		}
	});

});

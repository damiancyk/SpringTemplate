var posX = 0;
var iframeId = 0;
var dialog;

$(function() {
	prepareTemplate();
	// enablePopupLinks();
	// popupResultPass();

	$(window).on("debouncedresize", function(event) {
		// setDialogPositions();
	});
	
	navbarHeightResize();
	$(window).resize(function() {
		navbarHeightResize();
	});
});

function invokeDialog(url, e) {
	if (isIframe()) {
		parent.invokeDialog(url, e);
		return;
	}
	if (typeof e !== "undefined") {
		e.preventDefault();
		e.stopPropagation();
		var link = $(e.target);
		url = link.attr('href');
	}

	var body = $('body', window.top);
	if (window != window.top) {
		// window.parent.$('body');
		// log('body ' + body.length)
	}
	body.append(dialog)
	dialog = $('<div class="windowPopup">', window.top.document);

	var idIframe = 'idIframe' + parent.iframeId;

	var iframe = $('<iframe id="'
			+ idIframe
			+ '" class="iframeMain" style="width:100%;height:100%;overflow:hidden;">');
	iframe.appendTo(dialog);
	iframe.attr('src', url);
	parent.iframeId++;

	iframe.load(function() {
		var header = iframe.contents().find('.box-header').first();
		headerTxt = header.text().trim();

		if (headerTxt.length == 0) {
			headerTxt = ''
		}

		// headerTxt=iframe.attr('src')
		dialog.dialog('option', 'title', headerTxt);
	});

	// var footer = $('<div class="footer">');
	// footer.html('footer')
	// footer.appendTo(dialog);
	// setDialogFooter(dialog);

	// var content = 'TreÅÄ <br> <strong>popupa</strong>';
	// dialog.append(content);
	var width = 800;
	var height = 500;
	var browserWidth = $(window).width();
	var marginTop = 100;
	var randomVal = Math.floor(Math.random() * 50);

	var dialogOptions = {
		title : '',
		autoOpen : true,
		width : width,
		modal : false,
		draggable : true,
		resizable : false,
		dialogClass : 'customClass',
		height : height,
		position : {
			my : 'center top',
			at : 'center+' + posX + ' center+' + (marginTop + posX),
			of : $('#idStartPositionDialog')
		},
		close : function(event, ui) {
			$(this).dialog('destroy').remove();
			window.scrollTo(0, 0);
			// setDialogPositions();

			// invokeDataFromBtn(event);
		}
	};

	initDialog(dialog, dialogOptions);
	if (window != window.top) {
		// window.parent.$('body');
		// log('body ' + body.length)
		// window.top.initDialog(dialog, dialogOptions);
	} else {
		// initDialog(dialog, dialogOptions);
		// dialog.dialog(dialogOptions);
	}

	posX += 20;
}

function initDialog(div, dialogOptions) {
	div.dialog(dialogOptions);
}

function prepareTemplate() {
	$('body').prepend('<div id="idStartPositionDialog"></div>');

	if (window != window.top) {
		$('body').addClass('popup');
		setTitleFromBreadcrumb();

		if (getIdIframe() != 'idIframeMessagePreview') {
			var footer = setDialogFooter();
			setDialogFooterContent(footer);
		}

		var blockScroll = $('#main');
		// blockScroll.niceScroll();
		// log('blockScroll '+blockScroll.length)
		// blockScroll.niceScroll().scrollstart(function(info) {
		// log('scroll')
		// });
	}
}

function setTitleFromBreadcrumb() {
	var parent = $(window.parent);
}

function getIdIframe(){
	if (window != window.top) {
		return frameElement.id;
	}
	return '';
}

function setDialogFooter() {
	var body = $('body');
	var submitBtn = $('.submit');

	var footer = body.find('#idFooter');
	if (footer.length == 0) {
		footer = $('<div id="idFooter" class="footer" ></div>');
		body.append(footer)
	}

	return footer;
}

function setDialogFooterContent(footer) {
	var windowType = 'info';
	var body = $('body');
	var submit = body.find('.submit');

	if (submit.length > 0) {
		windowType = 'submit';
	}
	
	var footerContent=$('.footerContent');
	footer.append(footerContent);
	
	setDialogFooterBreadcrumb(footer);

	if (windowType == 'info') {
		var btnOk = $('<button class="btn btnOk dx-button dx-button-normal dx-button-success">'+windowLocale.btnOk+'</button>');
		footer.append(btnOk);
		btnOk.click(function() {
			closeDialog();

			// window.parent.closeDialogs();
			// dialog.dialog('destroy').remove();
		});
	} else if (windowType == 'submit') {
		// footer.append(submit);

		var btnSave = $('<button class="dx-button-success btn btnSave  dx-button" >'+windowLocale.btnSave+'</button>');
		footer.append(btnSave);
		btnSave.click(function() {
			submit.click();
		});

		var btnCancel = $('<button class="btnNo btn btnCancel  dx-button" >'+windowLocale.btnCancel+'</button>');
		footer.append(btnCancel);
		btnCancel.click(function() {
			closeDialog();
		});

	}
}

function setDialogFooterBreadcrumb(footer) {
	// var btnHistoryBack = $('<div class="btnBack"
	// onclick="window.history.back()">&lsaquo;</div>');
	// footer.prepend(btnHistoryBack);
	
	var boxTitle = $('.box-title').html();
	var breadcrumb = $('<div class="breadcrumb">');
	var breadcrumbPath = $('<div class="breadcrumbPath">');
	breadcrumbPath.append(boxTitle);
	
	breadcrumb.append(breadcrumbPath);

	footer.append(breadcrumb);

	var fullUrl=window.location.href;
	var shortUrl=setShortUrl(fullUrl);
	
	var blockUrl='<span class="url">'+shortUrl+'</span>';
	breadcrumb.append(blockUrl);
}

function setShortUrl(url){
	if(typeof url==="undefined"){
		return "";
	}
	
	var urlLength=url.length;
	var maxLength=60;
	
	if(urlLength<=maxLength){
		return url;
	}
	
	url=url.substring(0,maxLength);
	url+='..';
	
	return url;
}

function closeDialog(){
	var idIframe=getIdIframe();
	var iframe=$('#'+idIframe, window.parent.document);
	var btnClose=iframe.closest(".ui-dialog").find('button.ui-dialog-titlebar-close');
	btnClose.click();
}

function navbarHeightResize() {
	if ($("#navbar-menu").height() >= 70) {
		$("#navbar-menu").addClass('navbar-menu-rwd');
	} else {
		$("#navbar-menu").removeClass('navbar-menu-rwd');
	}
}
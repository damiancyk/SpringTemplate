<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isELIgnored="false"%>

<c:if test="${success!=null || popupResultBean!=null}">
	<script>
		$(document).ready(function() {
			if(isIframe()){
				var resultData = '${popupResultBean}';
				returnJsonToParent(resultData);
				parent.reloadNgTable();
				parent.alertSuccess("<fmt:message key='${success}'/>");
				parent.$(".ui-dialog-content").dialog("close");
			}
		});
	</script>
</c:if>

<script>
var posX = 0;
var iframeId = 0;
var dialog;

$(function() {
	prepareTemplate();
	enablePopupLinks();
	popupResultPass();
});

function invokeDialog(url, e) {
	if(isIframe()){
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
			headerTxt = "Okno"
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
	var randomVal=Math.floor(Math.random() * 50);

	var dialogOptions = {
		title : '',
		autoOpen : true,
		width : width,
		modal : false,
		draggable : true,
		resizable:false,
		dialogClass : 'customClass',
		height : height,
		position : {
			my : 'center top',
			at : 'center+' + posX +  ' center+' + (marginTop + posX),
			of : $('#idStartPositionDialog')
		},
		close : function(event, ui) {
			$(this).dialog('destroy').remove();
			window.scrollTo(0, 0);
			setDialogPositions();
			
			invokeDataFromBtn(event);
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

function invokeDataFromBtn(e){
	var btn=$(e.target);
	var callback=btn.data("callback");
	var select = btn.data("select");
	
	var returnInput = $("#idInputPopupResultData");
	var data = new Array();

	if (returnInput.length > 0) {
		var returnInputVal = returnInput
				.val();
		if (returnInputVal.length > 0) {
			data = $
					.parseJSON(returnInputVal);
		}

	}
	
	if (typeof (callback) !== "undefined") {
		try {
			window[callback]
					(
							data,
							btn,
							e);
		} catch (e) {
			console
					.error("blad przy wywolywaniu funkcji "
							+ callback);
		}
	}

	if (typeof select !== "undefined") {
		window["setSelect"]
				(
						select,
						data);
	}
	
}

function initDialog(div, dialogOptions) {
	div.dialog(dialogOptions);
}


function prepareTemplate() {
	$('body').prepend('<div id="idStartPositionDialog"></div>');

	if (window != window.top) {
		$('body').addClass('popup');
		setTitleFromBreadcrumb();
		
		if(getIdIframe()!='idIframeMessagePreview'){
		var footer = setDialogFooter();
		setDialogFooterContent(footer);
		}

		var blockScroll=$('#main');
		// blockScroll.niceScroll();
		// log('blockScroll '+blockScroll.length)
		// blockScroll.niceScroll().scrollstart(function(info) {
		// log('scroll')
		// });
	}
}

function getIdIframe(){
	if (window != window.top) {
		return frameElement.id;
	}
	return '';
}

function setTitleFromBreadcrumb() {
	var parent = $(window.parent);
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
		var btnOk = $('<button class="btn btnOk btn-success">Ok</button>');
		footer.append(btnOk);
		btnOk.click(function() {
			closeDialog();

			// window.parent.closeDialogs();
			// dialog.dialog('destroy').remove();
		});
	} else if (windowType == 'submit') {
		// footer.append(submit);

		var btnSave = $('<button class="dx-button-success btn btnSave  dx-button" >Zapisz</button>');
		footer.append(btnSave);
		btnSave.click(function() {
			submit.click();
		});

		var btnCancel = $('<button class="btnNo btn btnCancel  dx-button" >Anuluj</button>');
		footer.append(btnCancel);
		btnCancel.click(function() {
			closeDialog();
		});

	}
}

function closeDialog(){
	var idIframe=getIdIframe();
	var iframe=$('#'+idIframe, window.parent.document);
	var btnClose=iframe.closest(".ui-dialog").find('button.ui-dialog-titlebar-close');
	btnClose.click();
}

function closeDialogs(){
	var btnClose=$(".ui-dialog").find('button.ui-dialog-titlebar-close');
	btnClose.click();
}

function setDialogFooterBreadcrumb(footer) {
	//var btnHistoryBack = $('<div class="btnBack" onclick="window.history.back()">&lsaquo;</div>');
	//footer.prepend(btnHistoryBack);
	
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

function setDialogFooterBtnBack(footer) {
	var btnHistoryBack = $('<div class="btnBack" onclick="window.history.back()">&lsaquo;</div>');
	footer.prepend(btnHistoryBack);
}

function enablePopupLinks() {
	var links = $('a.popup');
	links.each(function() {
		var link = $(this);
		
		
		link.click(function(e) {
			e.preventDefault();

			var url = link.attr('href');
			
			var urlFunc = link.data("url-func");
			if (typeof urlFunc !== "undefined") {
				var urlFunc = window[urlFunc]();
						url = urlFunc;
						link.attr('href', url);
			}
		
			invokeDialog(undefined, e);
		});
	});
}

/*
function closeDialogs() {
	var dialogs = $('div.windowPopup');
	dialogs.each(function() {
		var dialog = $(this);
		dialog.dialog('destroy');
	});
}
*/

function setDialogPositions() {
	var dialogs = $('div.windowPopup');

	var posX = 0;
	var marginTop = 100;
	var widthDialogDefault = 800;

	window.scrollTo(0, 0);

	dialogs.each(function() {
		var dialog = $(this);

		// TODO nie dziala
		// if (!dialog.hasClass('ui-dialog-content')) {
		// return;
		// }

		// dialog.hide()
		// dialog.dialog('destroy');
		// dialog.dialog("option", "position", {
		// my : 'center+' + posX + ' bottom+' + posX
		// });

		var bodyWidth = $('body').width();
		var dialogWidth = dialog.width();
		if (bodyWidth < widthDialogDefault) {
			dialog.dialog("widget").width(bodyWidth);
		} else {
			dialog.dialog("widget").width(widthDialogDefault);
		}

		dialog.dialog("widget").position({
			my : 'center top',
			at : 'center+' + posX + ' center+' + (marginTop + posX),
			of : $('#idStartPositionDialog')
		});

		posX += 20;
	});

	window.scrollTo(0, 0);
}

function popupResultPass(){
	var successMsg="${success}";
	var resultData = '${popupResultBean}';

	//var preventClosing = "${preventClosing}";

	//if (preventClosing != true) {
		//parent.$.fancybox.close();
		if(resultData.length>0){
			returnJsonToParent(resultData);
			closeDialog();
		}
		
	//}
	
	if(typeof successMsg!=="unedefined" && successMsg.length>0){
	//	returnSuccessMsgToParent(successMsg);
	}
}

function returnJsonToParent(resultData) {
	// TODO
	var preventClosing = false;

	// sprwadzanie czy juz jest input o takim id
	var parentContent = $(parent.document).contents();
	var returnInput = parentContent.find("#idInputPopupResultData");
	if (typeof returnInput !== "undefined" && returnInput.length > 0) {
		returnInput.val(resultData);
	} else {
		returnInput = $("<input>", {
			id : "idInputPopupResultData",
			val : resultData,
			type : "hidden"
		}).appendTo(parentContent);
	}

	if (returnInput.length > 0) {
		var returnInputVal = returnInput
				.val();
		if (returnInputVal.length > 0) {
			data = $
					.parseJSON(returnInputVal);
		}
	}
	
	return preventClosing;
}

function returnSuccessMsgToParent(successMsg) {
	var parent=$(window.parent);
	var parentContent = $(parent.document).contents();
	var returnInput = parentContent.find("#idInputPopupResultData");
	
	if(isIframe()){
		window.parent.alertSuccess(successMsg)
	}
	
}

function setSelect(select, data) {
	var select = $(select);
	if(select.length<=0){
		console.error("Błąd - brak selecta ustawionego w parametrze data-select")
		return;
	}
	var value = data.id;
	var text = data.text;

	if (typeof value === 'undefined') {
//		console.error('Błąd - ustaw atrybut "data-select" w przycisku');
//		console.error('Błąd - przekaz wartosc "id" w kontrolerze');
		return;
	}

	// zwykly albo ajaxowy select
	if (select.parent().find("select").length > 0) {
		setSelectValue(select, value, text);
	} else {
		setSelectAjaxValue(select, value);
	}

}

function setSelectValue(select, value, text) {
	var select = $(select);

	if (typeof select !== "undefined") {
		select.append("<option value='" + value + "' selected>" + text
				+ "</option>");
		select.select2("val", value);
	}
} 

function setSelectAjaxValue(select, val) {
	if(typeof val=="undefined"){
		return;
	}
		
	var vals="";
	if(isMultipleSelect2(select)){
		vals=select.val();
		if(vals.length>0){
			vals+=",";
		}
		vals+=val;
	}else{
		if((""+val).indexOf(",")>0){
			vals=val.split(",")[0];
		}else{
			vals=val;
		}
	}

	 select.select2("val", vals, true);
//	select.val(vals).trigger("change");
}

function isMultipleSelect2(select2){
	if(typeof select2==="undefined"){
		return false;
	}
	var container=$("#s2id_"+select2.attr("id"))
	if(container.length>0 && container.hasClass("select2-container-multi")){
		return true;
	}
	return false
}

function isIframe(){
	if (window != window.top) {
		return true;
	}else{
		return false;
	}
}
</script>
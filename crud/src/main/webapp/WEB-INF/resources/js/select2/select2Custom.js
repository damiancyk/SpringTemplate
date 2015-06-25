$(function(){
		initSelect2($("select.select"));
});

function initSelect2(select){
	if(typeof select!=="undefined"){
		$(select).select2({
			placeholder:'Wybierz',
			minimumResultsForSearch : -1,
			allowClear : true
		});
	}
} 

var displayFuncDefault = function(object, container, query) {
	return object.name;
};

function initAjaxParams(url) {
	var ajaxParams = {
		url :url,
		dataType : "json",
		contentType : "text/html;charset=UTF-8",
		quietMillis : 100,
		data : function(term, page) {
			return {
				q : term,
				page_limit : 10,
				page : page - 1
			};
		},
		results : function(data, page) {
			return {
				results : data,
				more : (data && data.length == 10 ? true : false)
			};
		}
	};

	return ajaxParams;
}

function initSelect(inputs, options, url, id, display) {
	if (typeof options === "undefined") {
		options = {};
	}

	if(typeof options.url!=="undefined"){
		url=options.url;
	}
	
	var displayFunc;
	if (typeof display !== "undefined") {
		displayFunc = display;
	} else {
		displayFunc = displayFuncDefault;
	}

	var optionsDefault = {
		placeholder : "Wyszukaj..",
		ajax : initAjaxParams(url),
		allowClear : true,
		formatResult : displayFunc,
		formatSelection : displayFunc,
		initSelection : function(element, callback) {
			var idSelect = $(element).val();
			var multiple=options.multiple;
			
			if (idSelect !== "") {
				var urlStr='';
				if(typeof url === "string"){
					var urlStr=url;
				}else if(typeof url==="function"){
					var urlStr=url();
				}
				
				var urlInit = '';
				
				if(multiple==true){
					urlInit = urlStr + '?ids='+idSelect;
				}else{
					urlInit = urlStr.replace(".html","-"+idSelect+".html");
				}
				
				$.ajax(urlInit, {
					dataType : "json"
				}).done(function(data) {
					callback(data);
				});
			}
			
		},
		id : function(data) {
			return data[id];
		}
	};

	
	
	options = $.extend(optionsDefault, options);

	var select = $(inputs).select2(options);

	return select;
}

function openSelect(idSelect) {
	if (typeof idSelect !== "undefined") {
		$("#" + idSelect).select2("open");
	}
}

(function($) {
	$.fn.select2Address = function(options) {
		var display = function(obj, container, query) {
			var html = '';
			html += '<strong>' + getStr(obj.city) + '</strong>';
			html += addSpace(html) + getStr(obj.street);
			html += addSpace(html) + getStr(obj.house);
			html += addSpace(html) + getStr(obj.apartment);
			
			html+='<span style="color:#aaa">';
			html += addSpace(html) + getStr(obj.postCode);
			html += addSpace(html) + getStr(obj.postOffice);
			html+='</span>';
			
			return html;
		};

		return initSelect(this, options, "addressEvidenceListAjax.html",
				"idAddress", display);
	};

	$.fn.select2Company = function(options) {
		var display = function(obj, container, query) {
			var html = '';
			html += getStr(obj.name);
			
			return html;
		};

		return initSelect(this, options, "companyViewAjax.html",
				"idCompany", display);
	};
	
	$.fn.select2Partner = function(options) {
		var display = function(obj, container, query) {
			var html = '';
			html += getStr(obj.partnerName);
			
			return html;
		};

		options=$.extend(options,{
			placeholder : "Partner",
		})
		return initSelect(this, options, "partnerViewAjax.html",
				"idPartner", display);
	};
	
	$.fn.select2Country = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.DescriptionEN + " <i> (" +obj.DescriptionUA + ") </i>");
			return html;
		};
		return initSelect(this, options, "countryMe24Ajax.html",
				"IDREF", display);
	};
	
	
	$.fn.select2Division = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.CityDescriptionEN + ' <i>(' + obj.RegionDescriptionEN+ ')</i>' + ' ' + obj.House + ' numer: ' + obj.DivisionCode );
			return html;
		};
		
		return initSelect(this, options, "divisionMe24Ajax.html",
				"DivisionIDRRef", display);
	};
	
	$.fn.select2Street = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.name);
			return html;
		};
		return initSelect(this, options, "streetMe24Ajax.html",
				"idRef", display);
	};
	
	$.fn.select2Vat = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.name);
			return html;
		};
		return initSelect(this, options, "vatViewAjax.html",
				"idVat", display);
	};
	
	$.fn.select2Currency = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.name);
			return html;
		};
		return initSelect(this, options, "currencyViewAjax.html",
				"idCurrency", display);
	};
	
	// z encji
	$.fn.select2CountryCode = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.namePL);
			return html;
		};
		return initSelect(this, options, "countryAjax.html",
				"idCountry", display);
	};
	
	$.fn.select2City = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.DescriptionEN + " <i>(" + obj.DescriptionUA + ")</i> ");
			return html;
		};
		return initSelect(this, options, "cityMe24Ajax.html",
				"IDREF", display);
	};
	
	
	$.fn.select2Parcel = function(options) {
		var display = function(obj, container, query) {
			var html = getStr( obj.shipmentNumberRosan);
			return html;
		};
		return initSelect(this, options, "parcelListAjax.html",
				"idParcel", display);
	};
	
	// province dlatego ze u nas zawsze uzywazlismy jako wojewodztwa w
	// ukrainskim api jako region
	$.fn.select2Province = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.DescriptionEN + " (" + obj.DescriptionUA  +") ");
			return html;
		};
		
		return initSelect(this, options, "regionMe24Ajax.html",
				"IDREF", display);
	};
	
	$.fn.select2District = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.DescriptionEN + " <i>(" + obj.DescriptionUA +  ")</i>");
			return html;
		};
		return initSelect(this, options, "districtMe24Ajax.html",
				"IDREF", display);
	};
	
	$.fn.select2Contract = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.number);
			return html;
		};
		return initSelect(this, options, "contractSelectAjax.html",
				"idContract", display);
	};
	
	$.fn.select2CustomsCode = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.name);
			return html;
		};
		return initSelect(this, options, "customsCodesAjax.html",
				"idCustomsCode", display);
	};
	
	$.fn.select2ShipmentFormat = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.Name);
			return html;
		};
		return initSelect(this, options, "shipmentFormatMe24Ajax.html",
				"IdRef", display);
	};
	
	$.fn.select2Account = function(options) {
		var display = function(obj, container, query) {
			var html = getStr(obj.email);
			return html;
		};
		return initSelect(this, options, "accountsViewAjax.html",
				"idAccount", display);
	};
})(jQuery);

function addSpace(html) {
	var lastChar = html.substr(html.length - 1);
	
	if (lastChar != ' ') {
		return ' ';
	}else{
		return '';
	}
}

function getStr(str) {
	
	
	
	if (typeof str === "undefined") {
		return "";
	} else {
		return str.trim();
	}
}


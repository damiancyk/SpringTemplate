package com.damiancyk.binders;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ControllerAdvice
@EnableWebMvc
public class CustomInitBinder {

	@InitBinder
	public void initBinderDate(WebDataBinder binder) {
		binder.setAutoGrowCollectionLimit(10000);
		DateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		datetimeFormat.setLenient(false);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);

		DateFormat timeFormat = new SimpleDateFormat("HH:mm");
		timeFormat.setLenient(false);

		binder.registerCustomEditor(Date.class,
				new MyCustomDateEditorBinder(Arrays.asList(datetimeFormat, dateFormat, timeFormat), true));
	}

	@InitBinder
	public void initBinderDouble(WebDataBinder binder) {
		DecimalFormat decimalFormat = new DecimalFormat();
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(symbols);
		binder.registerCustomEditor(Double.class, new MyCustomNumberEditorBinder(Double.class, decimalFormat));
	}

	// /**
	// * Metoda do zwikszania ilosc obiektów w tablicy wysyłanej GET lub POST
	// * limit domyślny 256
	// *
	// * @param binder
	// */
	// @InitBinder
	// public void initListBinder(WebDataBinder binder) {
	// binder.setAutoGrowCollectionLimit(100000);
	// }
	//
	// @InitBinder
	// public void initStringBinder(WebDataBinder binder) {
	// binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	// }

	// @ExceptionHandler(IOException.class)
	// public ModelAndView handleIOException(IOException exception) {
	// ModelAndView andView = new ModelAndView();
	// andView.setViewName("error");
	// return andView;
	// }
	//
	//
	// @ExceptionHandler(Exception.class)
	// public ModelAndView handleSQLException(Exception exception) {
	// ModelAndView andView = new ModelAndView();
	// andView.setViewName("error");
	// return andView;
	// }

}
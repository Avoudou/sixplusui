package statics;

import java.text.DecimalFormat;

public class StaticsMethods {

	public static String decimalFormat(double value, int decimalDigits) {
		String format = "#0.";
		for (int i = 0; i < decimalDigits; i++)
			format += "#";
		DecimalFormat df = new DecimalFormat(format);
		df.setMinimumIntegerDigits(1);
		return df.format(value);
	}

}

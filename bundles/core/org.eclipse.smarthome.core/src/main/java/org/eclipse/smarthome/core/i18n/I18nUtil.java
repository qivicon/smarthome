package org.eclipse.smarthome.core.i18n;


public class I18nUtil {

	/** The 'text' pattern (prefix) which marks constants. */
    private static final String CONSTANT_PATTERN = "@text/";


	public static boolean isConstant(String key) {
		return key != null && key.startsWith(CONSTANT_PATTERN);
	}

	public static String stripConstant(String key) {
		return key.replace(CONSTANT_PATTERN, "");
	}

}

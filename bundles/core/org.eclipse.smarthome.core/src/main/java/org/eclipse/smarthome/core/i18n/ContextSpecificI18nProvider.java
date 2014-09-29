package org.eclipse.smarthome.core.i18n;


public class ContextSpecificI18nProvider {

	/** The 'text' pattern (prefix) which marks constants. */
    private static final String CONSTANT_PATTERN = "@text/";


	protected boolean isConstant(String key) {
		return key != null && key.startsWith(CONSTANT_PATTERN);
	}

	protected String stripConstant(String key) {
		return key.replace(CONSTANT_PATTERN, "");
	}

}

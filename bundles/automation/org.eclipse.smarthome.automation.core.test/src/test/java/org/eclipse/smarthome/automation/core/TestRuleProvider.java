/**
 * 
 */
package org.eclipse.smarthome.automation.core;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.smarthome.automation.core.parser.RuleParserException;
import org.eclipse.smarthome.automation.core.parser.RulesParser;
import org.eclipse.smarthome.core.common.registry.AbstractProvider;
import org.osgi.service.component.ComponentContext;

/**
 * this implementation provides all rules specified in src/test/resources/rules
 * folder Only for Testing!
 * 
 * @author niehues
 *
 * 
 */
public class TestRuleProvider extends AbstractProvider<Rule> implements
		RuleProvider {

	private List<Rule> rules = new ArrayList<Rule>();

	protected void activate(ComponentContext context) {
		Enumeration<URL> urls = context.getBundleContext().getBundle()
				.findEntries("src/test/resources/rules", "*.json", true);
		logger.debug("URLS: {}", urls);
		RulesParser parser = new RulesParser();
		File rulesFolder = new File("src/test/resources/rules");
		// File rulesFolder = new File(rulesFolderURL.toURI());
		File[] ruleFiles = rulesFolder.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".json");
			}
		});
		for (File file : ruleFiles) {
			try {
				rules.add(parser.parseRule(new FileInputStream(file)));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (RuleParserException e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.smarthome.core.common.registry.Provider#getAll()
	 */
	@Override
	public Collection<Rule> getAll() {

		return rules;
	}

}

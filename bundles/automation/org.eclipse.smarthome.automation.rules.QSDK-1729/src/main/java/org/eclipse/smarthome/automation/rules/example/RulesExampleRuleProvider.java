/**
 * 
 */
package org.eclipse.smarthome.automation.rules.example;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import org.eclipse.smarthome.automation.core.Rule;
import org.eclipse.smarthome.automation.core.RuleProvider;
import org.eclipse.smarthome.automation.core.parser.RuleParserException;
import org.eclipse.smarthome.automation.core.parser.RulesParser;
import org.eclipse.smarthome.core.common.registry.AbstractProvider;
import org.osgi.service.component.ComponentContext;

/**
 * @author niehues
 *
 */
public class RulesExampleRuleProvider extends AbstractProvider<Rule> implements
		RuleProvider {

	private List<Rule> rules = new ArrayList<Rule>();

	protected void activate(ComponentContext context) {
		Enumeration<URL> resources = context.getBundleContext().getBundle().findEntries("rules", "*.json", true);
		RulesParser parser = new RulesParser();
		while(resources.hasMoreElements()){
			URL resource = resources.nextElement();
			try {
				rules.add(parser.parseRule(resource.openStream()));
			} catch (RuleParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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

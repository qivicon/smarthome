package org.eclipse.smarthome.automation.core.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.smarthome.automation.core.Rule;
import org.eclipse.smarthome.automation.core.RuleProvider;
import org.eclipse.smarthome.core.common.registry.AbstractProvider;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonFileRuleProvider extends AbstractProvider<Rule> implements
		RuleProvider {

	private Logger logger = LoggerFactory.getLogger(JsonFileRuleProvider.class);
	private List<Rule> rules = new ArrayList<Rule>();

	protected void activate(ComponentContext componentContext) {
		URL resource = componentContext.getBundleContext().getBundle()
				.getResource("ruleMotionDetected.json");
		try (InputStream inputStream = resource.openStream()) {
			rules.add(new RulesParser().parseRule(inputStream));
		} catch (RuleParserException | IOException ex) {
			logger.error("Could not parse rule: " + ex.getMessage(), ex);
		}

		resource = componentContext.getBundleContext().getBundle()
				.getResource("cronTriggeredRule.json");
		try (InputStream inputStream = resource.openStream()) {
			rules.add(new RulesParser().parseRule(inputStream));
		} catch (RuleParserException | IOException ex) {
			logger.error("Could not parse rule: " + ex.getMessage(), ex);
		}
		
		resource = componentContext.getBundleContext().getBundle()
                .getResource("resetLightStripe.json");
        try (InputStream inputStream = resource.openStream()) {
            rules.add(new RulesParser().parseRule(inputStream));
        } catch (RuleParserException | IOException ex) {
            logger.error("Could not parse rule: " + ex.getMessage(), ex);
        }

        resource = componentContext.getBundleContext().getBundle()
                .getResource("ruleAlarmOff.json");
        try (InputStream inputStream = resource.openStream()) {
            rules.add(new RulesParser().parseRule(inputStream));
        } catch (RuleParserException | IOException ex) {
            logger.error("Could not parse rule: " + ex.getMessage(), ex);
        }
	}

	protected void deactivate(ComponentContext componentContext) {

	}

	@Override
	public Collection<Rule> getAll() {
		return rules;
	}

}

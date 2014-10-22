package org.eclipse.smarthome.automation.core.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.eclipse.smarthome.automation.core.Rule;
import org.eclipse.smarthome.automation.core.RuleProvider;
import org.eclipse.smarthome.core.common.registry.AbstractProvider;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class JsonFileRuleProvider extends AbstractProvider<Rule> implements RuleProvider {

    private Logger logger = LoggerFactory.getLogger(JsonFileRuleProvider.class);
    private Rule rule;
    
    protected void activate(ComponentContext componentContext) {
            URL resource = componentContext.getBundleContext().getBundle().getResource("rule.json");
            try(InputStream inputStream = resource.openStream()) {
                rule = new RulesParser().parseRule(inputStream);
            } catch (RuleParserException | IOException ex) {
                logger.error("Could not parse rule: " + ex.getMessage(), ex);
            }
    }

    protected void deactivate(ComponentContext componentContext) {

    }

    @Override
    public Collection<Rule> getAll() {
        return Lists.newArrayList(rule);
    }

}

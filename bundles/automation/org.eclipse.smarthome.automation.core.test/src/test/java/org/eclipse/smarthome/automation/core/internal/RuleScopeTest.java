/**
 * Copyright (c) 2017 by Deutsche Telekom AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.automation.core.internal;

import org.eclipse.smarthome.automation.Rule;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing the scope functionality.
 *
 * @author Victor Toni - Initial contribution
 */
public class RuleScopeTest {
	
	private static final String TESTING_SCOPE = "Testing";

    /**
     * Testing Rules without UID / without prefix / empty prefix.
     * 
     * @see RuleUtils.SCOPE_DELIMITER
     *
     */
    @Test
    public void testEmptyScope() {
        final Rule rule0 = new Rule();
        Assert.assertNull("Returned an UID instead of null", rule0.getUID());
        Assert.assertNull("Returned a scope instead of null", RuleUtils.getScope(rule0));

        final String somethingWithoutDelimiter = "something_without_delimiter";
        final Rule rule1 = new Rule(somethingWithoutDelimiter);
        Assert.assertEquals("Returned wrong UID", somethingWithoutDelimiter, rule1.getUID());
        Assert.assertNull("Returned a scope instead of null", RuleUtils.getScope(rule1));

        final String withDelimiterButEmpty = RuleUtils.SCOPE_DELIMITER + "with_delimiter_but_empty";
        final Rule rule2 = new Rule(withDelimiterButEmpty);
        Assert.assertEquals("Returned wrong UID", withDelimiterButEmpty, rule2.getUID());
        Assert.assertNull("Returned a scope instead of null", RuleUtils.getScope(rule2));
    }

    /**
     * Testing Rules with manually created scope / empty parts after the delimiter / multiple delimiters.
     * 
     * @see RuleUtils.SCOPE_DELIMITER
     *
     */
    @Test
    public void testManualScope() {
    	final String testingScopePrefix = TESTING_SCOPE + RuleUtils.SCOPE_DELIMITER;

    	final String someName = "someName";
        final Rule rule0 = new Rule(testingScopePrefix + someName);
        Assert.assertEquals("Returned wrong scope", TESTING_SCOPE, RuleUtils.getScope(rule0));
        Assert.assertEquals("Returned wrong UID", testingScopePrefix + someName, rule0.getUID());

    	final String multipleDelimiterName =  RuleUtils.SCOPE_DELIMITER + "nameBetweenDelimiter + RuleUtils.SCOPE_DELIMITER";
        final Rule rule1 = new Rule(testingScopePrefix + multipleDelimiterName);
        Assert.assertEquals("Returned wrong scope", TESTING_SCOPE, RuleUtils.getScope(rule1));
        Assert.assertEquals("Returned wrong UID", testingScopePrefix + someName, rule0.getUID());

    	final String emptyName =  "";
        final Rule rule2 = new Rule(testingScopePrefix + emptyName);
        Assert.assertEquals("Returned wrong scope", TESTING_SCOPE, RuleUtils.getScope(rule2));
        Assert.assertEquals("Returned wrong UID", testingScopePrefix + emptyName, rule2.getUID());
    }
}

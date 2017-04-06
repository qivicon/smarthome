/**
 * Copyright (c) 2017 by Deutsche Telekom AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.automation.core.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Predicate;

import org.eclipse.smarthome.automation.Rule;

/**
 * Provides some commonly used {@link Predicate}s.
 * 
 * @author Victor Toni - initial contribution
 *
 */
public class RulePredicates {

    /**
     * Creates a {@link Predicate} which can be used to filter {@link Rule}s for a given scope or {@code null} scope.
     * 
     * @param scope to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasScope(String scope) {
        if (null == scope) {
        	return new Predicate<Rule>() {
        		public boolean apply(Rule r) {
        			return null == RuleUtils.getScope(r);
        		}
        	};
       } else {
        	return new Predicate<Rule>() {
        		public boolean apply(Rule r) {
        			return scope.equals(RuleUtils.getScope(r));
        		}
        	};
        }
    }
    
    /**
     * Creates a {@link Predicate} which can be used to filter {@link Rule}s for any of the given scopes and even {@code null} scope.
     * 
     * @param scopes to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasAnyScope(String... scopes) {
        final HashSet<String> scopeSet = new HashSet<String>(scopes.length);
        for(final String scope : scopes) {
            scopeSet.add(scope);
        }
    
    	return new Predicate<Rule>() {
    		public boolean apply(Rule r) {
		        // this will even work for null scopes
		        return scopeSet.contains(RuleUtils.getScope(r));
    		}
    	};
    }
    
    /**
     * Creates a {@link Predicate} which can be used to filter {@link Rule}s for all given tags or {@link Rule}s without tags.
     * All given tags must match, (the matched {@code Rule} might contain more).
     * 
     * @param tags to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasAllTags(Collection<String> tags) {
        if (null == tags || tags.isEmpty()) {
        	return new Predicate<Rule>() {
        		public boolean apply(Rule r) {
		            // everything without a tag is matching
		            // Rule.getTags() is never null
		            return r.getTags().isEmpty();
        		}
        	};
        } else {
            final Set<String> tagSet = new HashSet<String>(tags);
        	return new Predicate<Rule>() {
        		public boolean apply(Rule r) {    
		            // everything containing _all_ given tags is matching 
        			// (Rule might might have more tags than the given set)
		            return r.getTags().containsAll(tagSet);
        		}
        	};
        }
    }
    
    /**
     * Creates a {@link Predicate} which can be used to filter {@link Rule}s for any of the given tags or {@link Rule}s without tags.
     * @param tags to search for
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> hasAnyTags(Collection<String> tags) {
        if (null == tags || tags.isEmpty()) {
        	return new Predicate<Rule>() {
        		public boolean apply(Rule r) {
		            // everything without a tag is matching
		            // Rule.getTags() is never null
		            return r.getTags().isEmpty();
        		}
        	};
        } else {
            final Set<String> tagSet = new HashSet<String>(tags);
        	return new Predicate<Rule>() {
        		public boolean apply(Rule r) {    
		            // everything containing _any_ of the given tags is matching (more than one tag might match)
		            // if the collections are NOT disjoint, they have something in common
		            return !Collections.disjoint(r.getTags(), tagSet);
				}
			};
        }
    }

    /**
     * Creates a {@link Predicate} which uses a logical AND to combine two predicates.
     * 
     * @param first {@link Predicate}
     * @param second {@link Predicate}
     * @return created {@link Predicate}
     */
    public static Predicate<Rule> and(Predicate<Rule> first, Predicate<Rule> second) {
    	return new Predicate<Rule>() {
    		public boolean apply(Rule r) {    
    			return first.apply(r) && second.apply(r);
			}
		};
    }

}

/*
 * Copyright (C) 2013 4th Line GmbH, Switzerland
 *
 * The contents of this file are subject to the terms of either the GNU
 * Lesser General Public License Version 2 or later ("LGPL") or the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.jupnp.tool.cli;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Assert;

/**
 * Common functionality for test cases using JUPnPTool.
 */
public abstract class AbstractTestCase {

	protected JUPnPTool tool;
	protected ByteArrayOutputStream out;
	protected ByteArrayOutputStream err;

	public void createSilentTool() {
		// will write output to buffers to check content later in tests
		this.out = new ByteArrayOutputStream();
		this.err = new ByteArrayOutputStream();
		this.tool = new JUPnPToolWithRedirectionOfOutput(new PrintStream(out),
				new PrintStream(err));
	}

	public void releaseSilentTool() {
		try {
			this.err.close();
			this.err = null;
			this.out.close();
			this.out = null;
			this.tool = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkCommandLine(final JUPnPTool tool, final int rcExpected,
			final String argsAsString) {
		final String[] args = argsAsString.split(" ");
		final int rc = tool.doMain(args);
		Assert.assertEquals(rcExpected, rc);
	}

}

/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.binding.hue.internal.setup;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import nl.q42.jue.HueBridge;
import nl.q42.jue.exceptions.ApiException;
import nl.q42.jue.exceptions.UnauthorizedException;

import org.eclipse.smarthome.binding.hue.HueBindingConstants;
import org.eclipse.smarthome.binding.hue.config.HueBridgeConfiguration;
import org.eclipse.smarthome.config.core.Configuration;
import org.eclipse.smarthome.config.setupflow.SetupFlowContext;
import org.eclipse.smarthome.config.setupflow.SetupStepHandlerCallback;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This setup step handler is responsible for checking and setting the ip
 * address of the Hue Bridge.
 * 
 * @author Oliver Libutzki
 * 
 */
public class IpConfigurationSetupStepHandler extends BaseHueSetupStepHandler {

    private static Logger logger = LoggerFactory.getLogger(IpConfigurationSetupStepHandler.class);

    public IpConfigurationSetupStepHandler(BundleContext bundleContext) {
        super(bundleContext);
    }

    @Override
    protected void startInternal(final SetupFlowContext setupFlowContext,
            final SetupStepHandlerCallback callback) {
        final Map<String, Object> properties = setupFlowContext.getProperties();
        final String bridgeIpAddress = (String) properties.get(HueBridgeConfiguration.IP_ADDRESS);
        if (bridgeIpAddress == null) {
            logger.error("No ip address has been provided.");
            HueErrorCode bridgeNotFoundErrorCode = HueErrorCode.IP_ADDRESS_MISSING;
            callback.sendErrorOccurredEvent(setupFlowContext, bridgeNotFoundErrorCode.getCode(),
                    bridgeNotFoundErrorCode.getMessage());
            return;
        }

        Task connectionTask = new AbstractTask("HueIpConfiguration") {
            @Override
            public void executeTask(TaskExecutionState taskExecutionState) {
                // we just check if the ip address is reachable
                if (!isReachable(bridgeIpAddress)) {
                    logger.error("The bridge is not reachable on {}.", bridgeIpAddress);
                    HueErrorCode bridgeNotFoundErrorCode = HueErrorCode.BRIDGE_NOT_RESPONDING;
                    callback.sendErrorOccurredEvent(setupFlowContext,
                            bridgeNotFoundErrorCode.getCode(),
                            bridgeNotFoundErrorCode.getMessage());
                    return;
                }

                // The hue bridge is reachable. So let's obtain the serialnumber
                String bridgeSerialNumber = (String) properties.get(HueBridgeConfiguration.SERIAL_NUMBER);
                if (bridgeSerialNumber == null) {
                    bridgeSerialNumber = getHueBridgeSerialNumber(bridgeIpAddress);
                }

                String bridgeUserName = (String) properties.get(HueBridgeConfiguration.USER_NAME);

                if (bridgeUserName == null) {
                    // If the user name is not yet added in the flow, we try to
                    // obtain it from the stored configuration
                    Thing bridge = getThingRegistry().getByUID(
                            new ThingUID(HueBindingConstants.THING_TYPE_BRIDGE,
                                    bridgeSerialNumber));
                    if (bridge != null) {
                        Configuration hueBridgeConfiguration = (Configuration) bridge
                                .getConfiguration();
                        String userName = (String) hueBridgeConfiguration.get("userName");
                        if (hueBridgeConfiguration != null && userName != null) {
                            bridgeUserName = userName;
                        }
                    }
                }

                if (bridgeUserName == null) {
                    // The hue bridge is not paired so far, let's proceed with
                    // the pairing
                    logger.debug("No configuration found for hue bridge on IP: {}",
                            bridgeIpAddress);
                    if (taskExecutionState.isAborted()) {
                        logger.debug("The task execution has been aborted.");
                        return;
                    }
                    properties.put(HueBridgeConfiguration.IP_ADDRESS, bridgeIpAddress);
                    properties.put(HueBridgeConfiguration.USER_NAME, bridgeUserName);
                    properties.put(HueBridgeConfiguration.SERIAL_NUMBER, bridgeSerialNumber);
                    callback.sendStepSucceededEvent(setupFlowContext);
                    return;
                }

                // If we have a user name, we can try to connect as the hue bridge
                // might already been paired
                properties.remove(HueBridgeConfiguration.SERIAL_NUMBER);

                try {
                    new HueBridge(bridgeIpAddress, bridgeUserName);
                    // if no exception is thrown everything is fine, we might
                    // skip the linking step
                    logger.debug("Connection to Hue bridge established successfully. IP: {}",
                            bridgeIpAddress);
                    if (taskExecutionState.isAborted()) {
                        logger.debug("The task execution has been aborted.");
                        return;
                    }
                    properties.put(HueBridgeConfiguration.IP_ADDRESS, bridgeIpAddress);
                    properties.put(HueBridgeConfiguration.USER_NAME, bridgeUserName);
                    properties.put(HueBridgeConfiguration.SERIAL_NUMBER, bridgeSerialNumber);
                    // At this point we could also abort the flow (if this would be possible)
                    callback.sendStepSucceededEvent(setupFlowContext);

                } catch (UnauthorizedException e) {
                    // the authorization failed, in this step this is ok as the
                    // user seems not to be created on the bridge
                    logger.debug("Authorization failed while connecting to Hue bridge. IP: {}",
                            bridgeIpAddress);
                    if (taskExecutionState.isAborted()) {
                        logger.debug("The task execution has been aborted.");
                        return;
                    }
                    properties.put(HueBridgeConfiguration.IP_ADDRESS, bridgeIpAddress);
                    properties.put(HueBridgeConfiguration.USER_NAME, bridgeUserName);
                    properties.put(HueBridgeConfiguration.SERIAL_NUMBER, bridgeSerialNumber);
                    callback.sendStepSucceededEvent(setupFlowContext);
                } catch (IOException | ApiException e) {
                    // this exception is unexpected
                    logger.error("The bridge is not reachable on {}.", bridgeIpAddress);
                    if (taskExecutionState.isAborted()) {
                        logger.debug("The task execution has been aborted.");
                        return;
                    }
                    HueErrorCode unexpectedErrorCode = HueErrorCode.UNEXPECTED_ERROR;
                    callback.sendErrorOccurredEvent(setupFlowContext,
                            unexpectedErrorCode.getCode(),
                            unexpectedErrorCode.getMessage());
                }
            }
        };
        logger.debug("Trying to connect to the hue bridge. IP: {}", bridgeIpAddress);
        runTask(connectionTask, setupFlowContext);

    }

    private String getHueBridgeSerialNumber(String ipAddress) {
        try {
        final Document doc = getDocument("http://" + ipAddress + "/description.xml");
        final String serialNumber = getStringValue(doc, "/root/device/serialNumber");
            return serialNumber;
        } catch (XPathExpressionException | IOException | SAXException
                | ParserConfigurationException e) {
            throw new RuntimeException("An error occurred while reading description.xml on "
                    + ipAddress, e);
        }
    }

    private String getStringValue(Document doc, String xPathExpression)
            throws XPathExpressionException {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();

            XPathExpression expr = xpath.compile(xPathExpression);
            String value = expr.evaluate(doc);
            return value;

    }

    private Document getDocument(String urlString) throws IOException, SAXException,
            ParserConfigurationException {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(connection.getInputStream());
            return doc;
    }
}

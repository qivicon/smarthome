package org.eclipse.smarthome.config.setupflow.test

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import org.eclipse.smarthome.config.core.ConfigDescription
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter
import org.eclipse.smarthome.config.core.ConfigDescriptionProvider
import org.eclipse.smarthome.config.setupflow.SetupFlowContext
import org.eclipse.smarthome.config.setupflow.SetupStepHandlerCallback
import org.eclipse.smarthome.config.setupflow.internal.GenericConfigurationStepHandler
import org.eclipse.smarthome.test.AsyncResultWrapper
import org.eclipse.smarthome.test.OSGiTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class GenericConfigurationStepHandlerOSGITest extends OSGiTest {
    ConfigDescriptionProvider configDescriptionProviderMock

    private final static BINDING_ID_WITHOUT_EXPLICIT_FLOW = "dummy"
    private final static THING_TYPE_WITHOUT_EXPLICIT_FLOW = "dummything"
    private final static CONCATENATED_THING_TYPE_WITHOUT_EXPLICIT_FLOW = BINDING_ID_WITHOUT_EXPLICIT_FLOW + ":" + THING_TYPE_WITHOUT_EXPLICIT_FLOW

    @Before
    void setUp() {
        def ConfigDescription configDescriptionWithoutExplicitFlow = createConfigDescription(CONCATENATED_THING_TYPE_WITHOUT_EXPLICIT_FLOW)

        configDescriptionProviderMock = [
            getConfigDescriptions: {
                -> [ configDescriptionWithoutExplicitFlow ]
            },
            getConfigDescription: {a,b -> configDescriptionWithoutExplicitFlow}
        ] as ConfigDescriptionProvider
        registerService configDescriptionProviderMock
    }

    @After
    void cleanUp() {
        unregisterService configDescriptionProviderMock
    }

    @Test
    void 'assert that required parameters are checked'() {
        GenericConfigurationStepHandler genericConfigurationStepHandler = new GenericConfigurationStepHandler(bundleContext)
        AsyncResultWrapper<Map<String, Object>> stepSucceededResultWrapper = new AsyncResultWrapper<Map<String, Object>>()
        AsyncResultWrapper<Map<String, Object>> errorOccurredResultWrapper = new AsyncResultWrapper<Map<String, Object>>()

        SetupStepHandlerCallback setupStepHandlerCallback = [
            sendStepSucceededEvent : { SetupFlowContext context ->
                stepSucceededResultWrapper.set(context.properties)
            },
            sendErrorOccurredEvent : {SetupFlowContext context, int errorCode, String errorMessage ->
                errorOccurredResultWrapper.set([
                    "context" : context,
                    "errorCode" : errorCode,
                    "errorMessage" : errorMessage
                ])
            }
        ] as SetupStepHandlerCallback
        genericConfigurationStepHandler.start(createSetupFlowContext(), setupStepHandlerCallback)

        waitForAssert {assertTrue errorOccurredResultWrapper.isSet}
        assertFalse stepSucceededResultWrapper.isSet

        Map<String, Object> resultMap = errorOccurredResultWrapper.wrappedObject
        assertThat resultMap.get("errorCode"), is(-1004)
        assertThat resultMap.get("errorMessage"), is("The following required parameter(s) have not been set: requiredParameter.")
    }

    @Test
    void 'assert that succeeded event is sent'() {
        GenericConfigurationStepHandler genericConfigurationStepHandler = new GenericConfigurationStepHandler(bundleContext)
        AsyncResultWrapper<Map<String, Object>> stepSucceededResultWrapper = new AsyncResultWrapper<Map<String, Object>>()
        AsyncResultWrapper<Map<String, Object>> errorOccurredResultWrapper = new AsyncResultWrapper<Map<String, Object>>()

        SetupStepHandlerCallback setupStepHandlerCallback = [
            sendStepSucceededEvent : { SetupFlowContext context ->
                stepSucceededResultWrapper.set(context.properties)
            },
            sendErrorOccurredEvent : {SetupFlowContext context, int errorCode, String errorMessage ->
                errorOccurredResultWrapper.set([
                    "context" : context,
                    "errorCode" : errorCode,
                    "errorMessage" : errorMessage
                ])
            }
        ] as SetupStepHandlerCallback

        def setupFlowContext = createSetupFlowContext().with {
            properties.put("requiredParameter", "Some Value")
            it
        }

        genericConfigurationStepHandler.start(setupFlowContext, setupStepHandlerCallback)

        waitForAssert {assertTrue stepSucceededResultWrapper.isSet}
        assertFalse errorOccurredResultWrapper.isSet
    }

    @Test
    void 'assert that existence of config description provider is checked'() {
        unregisterService configDescriptionProviderMock
        GenericConfigurationStepHandler genericConfigurationStepHandler = new GenericConfigurationStepHandler(bundleContext)
        AsyncResultWrapper<Map<String, Object>> stepSucceededResultWrapper = new AsyncResultWrapper<Map<String, Object>>()
        AsyncResultWrapper<Map<String, Object>> errorOccurredResultWrapper = new AsyncResultWrapper<Map<String, Object>>()

        SetupStepHandlerCallback setupStepHandlerCallback = [
            sendStepSucceededEvent : { SetupFlowContext context ->
                stepSucceededResultWrapper.set(context.properties)
            },
            sendErrorOccurredEvent : {SetupFlowContext context, int errorCode, String errorMessage ->
                errorOccurredResultWrapper.set([
                    "context" : context,
                    "errorCode" : errorCode,
                    "errorMessage" : errorMessage
                ])
            }
        ] as SetupStepHandlerCallback
        genericConfigurationStepHandler.start(createSetupFlowContext(), setupStepHandlerCallback)

        waitForAssert {assertTrue errorOccurredResultWrapper.isSet}
        assertFalse stepSucceededResultWrapper.isSet

        Map<String, Object> resultMap = errorOccurredResultWrapper.wrappedObject
        assertThat resultMap.get("errorCode"), is(-1003)
        assertThat resultMap.get("errorMessage"), is("No configuration description can be found for thing type " + CONCATENATED_THING_TYPE_WITHOUT_EXPLICIT_FLOW)
    }

    private createConfigDescription(String thingType) {
        new ConfigDescription(URI.create(thingType),
                [
                    createParameter("requiredParameter", true),
                    createParameter("optionalParameter", false)]
                )
    }

    private createParameter(String name, boolean required) {
        new ConfigDescriptionParameter(name, ConfigDescriptionParameter.Type.TEXT).with {
            it.required = required
            description = "Represents the " + name
            label = name
            it
        }
    }

    SetupFlowContext createSetupFlowContext() {
        new SetupFlowContext(THING_TYPE_WITHOUT_EXPLICIT_FLOW + "-genericflow", BINDING_ID_WITHOUT_EXPLICIT_FLOW, CONCATENATED_THING_TYPE_WITHOUT_EXPLICIT_FLOW, null, null, null).with {
            stepId = "generic-config"
            it
        }
    }
}

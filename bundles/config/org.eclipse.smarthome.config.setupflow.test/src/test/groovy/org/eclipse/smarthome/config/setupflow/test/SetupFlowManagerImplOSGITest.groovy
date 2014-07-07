package org.eclipse.smarthome.config.setupflow.test

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import org.eclipse.smarthome.config.core.ConfigDescription
import org.eclipse.smarthome.config.core.ConfigDescriptionListener
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter
import org.eclipse.smarthome.config.core.ConfigDescriptionProvider
import org.eclipse.smarthome.config.setupflow.ConfigurationStep
import org.eclipse.smarthome.config.setupflow.Property
import org.eclipse.smarthome.config.setupflow.SetupFlowManager
import org.eclipse.smarthome.config.setupflow.internal.GenericConfigurationStepHandler
import org.eclipse.smarthome.config.setupflow.internal.SetupFlowManagerImpl
import org.eclipse.smarthome.core.thing.ThingTypeUID
import org.eclipse.smarthome.test.OSGiTest
import org.junit.After
import org.junit.Before
import org.junit.Test


class SetupFlowManagerImplOSGITest extends OSGiTest {

    SetupFlowManager setupFlowManager
    ConfigDescriptionProvider configDescriptionProviderMock

    private final static BINDING_ID_UNKNOWN = "unknown"
    private final static THING_TYPE_UNKNOWN = "unknownthing"
    private final static BINDING_ID_WITHOUT_EXPLICIT_FLOW = "dummy"
    private final static THING_TYPE_WITHOUT_EXPLICIT_FLOW = "dummything"

    @Before
    void setUp() {
        setupFlowManager = getService(SetupFlowManager)
        assertThat setupFlowManager, isA(SetupFlowManagerImpl)
        def ConfigDescription configDescriptionWithoutExplicitFlow = createConfigDescription(new ThingTypeUID(BINDING_ID_WITHOUT_EXPLICIT_FLOW, THING_TYPE_WITHOUT_EXPLICIT_FLOW))

        configDescriptionProviderMock = [
            addConfigDescriptionListener: { def ConfigDescriptionListener listener ->
                listener.configDescriptionAdded(configDescriptionWithoutExplicitFlow)
            },
            removeConfigDescriptionListener: { def ConfigDescriptionListener listener ->
                listener.configDescriptionRemoved(configDescriptionWithoutExplicitFlow)
            }
        ] as ConfigDescriptionProvider
        registerService configDescriptionProviderMock
    }

    @After
    void cleanUp() {
        if (configDescriptionProviderMock != null) {
            unregisterService configDescriptionProviderMock
        }
    }

    private createConfigDescription(ThingTypeUID thingTypeUID) {
        new ConfigDescription(URI.create(thingTypeUID.toString()),[
            createParameter("requiredParameter", true),
            createParameter("optionalParameter", false)
        ])
    }

    private createParameter(String name, boolean required) {
        new ConfigDescriptionParameter(name, ConfigDescriptionParameter.Type.TEXT).with {
            it.required = required
            description = "Represents the " + name
            label = name
            it
        }
    }

    @Test
    void 'assert that generic setup flow without configuration parameters is generated correctly' () {

        def setupFlow = setupFlowManager.getSetupFlow(new ThingTypeUID(BINDING_ID_UNKNOWN, THING_TYPE_UNKNOWN))
        assertThat setupFlow, is(notNullValue())

        // check setup flow properties
        assertThat setupFlow.id, is(THING_TYPE_UNKNOWN + "-genericflow")
        assertThat setupFlow.bindingId, is(BINDING_ID_UNKNOWN)

        // check thing type
        def thingTypes = setupFlow.thingTypes
        assertThat thingTypes, is(notNullValue())
        def thingTypeList = thingTypes.thingTypes
        assertThat thingTypeList.size(), is(1)
        def actualThingType = thingTypeList.first()
        assertThat actualThingType, is(THING_TYPE_UNKNOWN)

        // check steps
        def steps = setupFlow.steps
        assertThat steps, is(notNullValue())
        def setupStepList = steps.searchStepsAndExternalEventStepsAndConfigurationSteps
        assertThat setupStepList.size(), is(0)
    }

    @Test
    void 'assert that generic setup flow with configuration parameters is generated correctly' () {

        def setupFlow = setupFlowManager.getSetupFlow(new ThingTypeUID(BINDING_ID_WITHOUT_EXPLICIT_FLOW, THING_TYPE_WITHOUT_EXPLICIT_FLOW))
        assertThat setupFlow, is(notNullValue())

        // check setup flow properties
        assertThat setupFlow.id, is(THING_TYPE_WITHOUT_EXPLICIT_FLOW + "-genericflow")
        assertThat setupFlow.bindingId, is(BINDING_ID_WITHOUT_EXPLICIT_FLOW)

        // check thing type
        def thingTypes = setupFlow.thingTypes
        assertThat thingTypes, is(notNullValue())
        def thingTypeList = thingTypes.thingTypes
        assertThat thingTypeList.size(), is(1)
        def actualThingType = thingTypeList.first()
        assertThat actualThingType, is(THING_TYPE_WITHOUT_EXPLICIT_FLOW)

        // check steps
        def steps = setupFlow.steps
        assertThat steps, is(notNullValue())
        def setupStepList = steps.searchStepsAndExternalEventStepsAndConfigurationSteps
        assertThat setupStepList.size(), is(1)
        def actualSetupStep = setupStepList.first()

        // check config step
        assertThat actualSetupStep, isA(ConfigurationStep)
        def ConfigurationStep configurationStep = actualSetupStep


        // check config step properties
        assertThat configurationStep.id, is("generic-config")

        // check step implementation
        def actualStepImplementation = configurationStep.implementation
        assertThat actualStepImplementation, is(notNullValue())
        assertThat actualStepImplementation.clazz, is(GenericConfigurationStepHandler.name)

        // check properties
        def properties = configurationStep.properties
        assertThat properties, is(notNullValue())
        def propertyList = properties.properties
        assertThat propertyList.size(), is(1)
        def Property actualProperty = propertyList.first()
        assertThat actualProperty, is(notNullValue())
        assertThat actualProperty.name, is("requiredParameter")
    }
}

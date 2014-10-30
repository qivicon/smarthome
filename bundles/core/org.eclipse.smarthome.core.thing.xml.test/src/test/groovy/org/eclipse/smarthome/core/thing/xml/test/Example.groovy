package org.eclipse.smarthome.core.thing.xml.test;

import static org.junit.Assert.*

import org.eclipse.smarthome.core.thing.xml.internal.ChannelTypeXmlResult
import org.eclipse.smarthome.core.thing.xml.internal.ThingDescriptionList
import org.eclipse.smarthome.core.thing.xml.internal.ThingDescriptionReader
import org.junit.Test


class Example {

    @Test
    public void test() {
        File file = new File("./example/channels.xml")
        URL channelsURL = file.toURI().toURL()

        ThingDescriptionReader reader = new ThingDescriptionReader()
        ThingDescriptionList thingList = reader.readFromXML(channelsURL)

        thingList.each {
            print it

            if (it instanceof ChannelTypeXmlResult) {
                print ", tags=" + ((ChannelTypeXmlResult) it).getChannelType().getTags()
            }

            print "\n"
        }        
    }

}

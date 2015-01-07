package org.eclipse.smarthome.core.thing.internal.console;

import java.util.Arrays;
import java.util.List;

import org.eclipse.smarthome.core.thing.setup.ThingSetupManager;
import org.eclipse.smarthome.io.console.Console;
import org.eclipse.smarthome.io.console.extensions.ConsoleCommandExtension;

/**
 * {@link SetupConsoleCommandExtension} provides console commands for setup of things.
 * 
 * @author Alex Tugarev - Initial contribution
 */
public class SetupConsoleCommandExtension implements ConsoleCommandExtension {

    private final static String COMMAND_SETUP = "setup";

    private ThingSetupManager thingSetupManager;

    @Override
    public boolean canHandle(String[] args) {
        String firstArgument = args[0];
        return COMMAND_SETUP.equals(firstArgument);
    }

    @Override
    public void execute(String[] args, Console console) {
        // TODO implement.
    }

    @Override
    public List<String> getUsages() {
        return Arrays.asList((new String[] {
                COMMAND_SETUP + " listHomeGroups - lists all home groups",
                COMMAND_SETUP + " addHomeGroup <groupItemName> <label> - creates new home group",
                COMMAND_SETUP + " removeHomeGroup <groupItemName> - creates new home group",
                COMMAND_SETUP + " addItemToHomeGroup <itemName> <groupItemName> - adds item to home group",
                COMMAND_SETUP + " removeItemFromHomeGroup <itemName> <groupItemName> - removes item from home group",
                COMMAND_SETUP + " enableChannel <channelUID> - removes all links and linked items of a channel", 
                COMMAND_SETUP + " disableChannel <channelUID> - creates links and linked items for a channel",
                COMMAND_SETUP + " setLabel <thingUID> <label> - sets new label of the item linked to the thing"
        }));
    }
    
    protected void setThingSetupManager(ThingSetupManager thingSetupManager) {
        this.thingSetupManager = thingSetupManager;
    }

    protected void unsetThingSetupManager(ThingSetupManager thingSetupManager) {
        this.thingSetupManager = null;
    }

}

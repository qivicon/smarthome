package org.eclipse.smarthome.config.discovery.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.smarthome.config.discovery.DiscoveryListener;
import org.eclipse.smarthome.config.discovery.DiscoveryResult;
import org.eclipse.smarthome.config.discovery.DiscoveryResultFlag;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryServiceRegistry;
import org.eclipse.smarthome.config.discovery.inbox.Inbox;
import org.eclipse.smarthome.config.discovery.inbox.InboxFilterCriteria;
import org.eclipse.smarthome.config.discovery.inbox.InboxListener;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link PersistentInbox} class is a concrete implementation of the
 * {@link Inbox}.
 * <p>
 * This implementation uses the {@link DiscoveryServiceRegistry} to register
 * itself as {@link DiscoveryListener} to receive {@link DiscoveryResult}
 * objects automatically from {@link DiscoveryService}s.
 * <p>
 * This implementation does neither handle memory leaks (orphaned listener
 * instances) nor blocked listeners. No performance optimizations have been done
 * (synchronization).
 * 
 * @author Michael Grammling
 */
public final class PersistentInbox implements Inbox, DiscoveryListener {

    /**
     * Internal enumeration to identify the correct type of the event to be
     * fired.
     */
    private enum EventType {
        added, removed, updated
    }

    private DiscoveryServiceRegistry discoveryServiceRegistry;

    private List<DiscoveryResult> entries = new CopyOnWriteArrayList<>();

    private List<InboxListener> listeners = new CopyOnWriteArrayList<>();
    private Logger logger = LoggerFactory.getLogger(PersistentInbox.class);


    @Override
    public synchronized boolean add(DiscoveryResult result) throws IllegalStateException {

        if (result != null) {
            DiscoveryResult inboxResult = get(result.getThingUID());

            if (inboxResult == null) {
                this.entries.add(result);
                notifyListeners(result, EventType.added);
            } else {
                inboxResult.synchronize(result);
                notifyListeners(inboxResult, EventType.updated);
            }

            return true;
        }

        return false;
    }

    @Override
    public void addInboxListener(InboxListener listener) throws IllegalStateException {
        if ((listener != null) && (!this.listeners.contains(listener))) {
            this.listeners.add(listener);
        }
    }

    @Override
    public void discoveryErrorOccurred(DiscoveryService source, Exception exception) {
        // nothing to do
    }

    @Override
    public void discoveryFinished(DiscoveryService source) {
        // nothing to do
    }

    @Override
    public List<DiscoveryResult> get(InboxFilterCriteria criteria)
            throws IllegalStateException {

        List<DiscoveryResult> filteredEntries = new ArrayList<>();

        for (DiscoveryResult discoveryResult : this.entries) {
            if (matchFilter(discoveryResult, criteria)) {
                filteredEntries.add(discoveryResult);
            }
        }

        return filteredEntries;
    }

    @Override
    public List<DiscoveryResult> getAll() {
        return get((InboxFilterCriteria) null);
    }

    @Override
    public synchronized boolean remove(ThingUID thingUID) throws IllegalStateException {

        if (thingUID != null) {
            DiscoveryResult discoveryResult = get(thingUID);
            if (discoveryResult != null) {
                this.entries.remove(discoveryResult);
                notifyListeners(discoveryResult, EventType.removed);

                return true;
            }
        }

        return false;
    }

    @Override
    public void removeInboxListener(InboxListener listener)
            throws IllegalStateException {
        if (listener != null) {
            this.listeners.remove(listener);
        }
    }

    @Override
    public void thingDiscovered(DiscoveryService source, DiscoveryResult result) {
        add(result);
    }

    @Override
    public void thingRemoved(DiscoveryService source, ThingUID thingUID) {
        remove(thingUID);
    }

    /**
     * Returns the {@link DiscoveryResult} in this {@link Inbox} associated with
     * the specified {@code Thing} ID, or {@code null}, if no
     * {@link DiscoveryResult} could be found.
     * 
     * @param thingId
     *            the Thing ID to which the discovery result should be returned
     * 
     * @return the discovery result associated with the specified Thing ID, or
     *         null, if no discovery result could be found
     */
    private DiscoveryResult get(ThingUID thingUID) {
        if (thingUID != null) {
            for (DiscoveryResult discoveryResult : this.entries) {
                if (discoveryResult.getThingUID().equals(thingUID)) {
                    return discoveryResult;
                }
            }
        }

        return null;
    }

    private boolean matchFilter(DiscoveryResult discoveryResult, InboxFilterCriteria criteria) {
        if (criteria != null) {
            String bindingId = criteria.getBindingId();
            if ((bindingId != null) && (!bindingId.isEmpty())) {
                if (!discoveryResult.getBindingId().equals(bindingId)) {
                    return false;
                }
            }

            ThingTypeUID thingTypeUID = criteria.getThingTypeUID();
            if (thingTypeUID != null) {
                if (!discoveryResult.getThingTypeUID().equals(thingTypeUID)) {
                    return false;
                }
            }

            ThingUID thingUID = criteria.getThingUID();
            if (thingUID != null) {
                if (!discoveryResult.getThingUID().equals(thingUID)) {
                    return false;
                }
            }

            DiscoveryResultFlag flag = criteria.getFlag();
            if (flag != null) {
                if (discoveryResult.getFlag() != flag) {
                    return false;
                }
            }
        }

        return true;
    }

    private void notifyListeners(DiscoveryResult result, EventType type) {
        for (InboxListener listener : this.listeners) {
            try {
                switch (type) {
                case added:
                    listener.thingAdded(this, result);
                    break;
                case removed:
                    listener.thingRemoved(this, result);
                    break;
                case updated:
                    listener.thingUpdated(this, result);
                    break;
                }
            } catch (Exception ex) {
                String errorMessage = String.format(
                        "Cannot notify the InboxListener '%s' about a Thing %s event!", listener
                                .getClass().getName(), type.name());

                this.logger.error(errorMessage, ex);
            }
        }
    }

    protected void deactivate(ComponentContext componentContext) {
        this.listeners.clear();
    }

    protected void setDiscoveryServiceRegistry(DiscoveryServiceRegistry discoveryServiceRegistry) {
        this.discoveryServiceRegistry = discoveryServiceRegistry;
        this.discoveryServiceRegistry.addDiscoveryListener(this);
    }

    protected void unsetDiscoveryServiceRegistry(DiscoveryServiceRegistry discoveryServiceRegistry) {
        this.discoveryServiceRegistry.removeDiscoveryListener(this);
        this.discoveryServiceRegistry = null;
    }

}

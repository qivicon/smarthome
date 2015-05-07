# Events

The Eclipse SmartHome framework offers to post and receive events through the SmartHome event bus in an asynchronous way. Events can be "Item/Thing added/removed/updated", "Item command sent", "Thing status changed", etc. This section shows how to subscribe and how to post such Events.

## The Interfaces

The following diagram introduces the involved interfaces of the Eclipse SmartHome event mechanism.

![Event Interfaces](diagrams/event_interfaces.png)

The `EventPublisher` posts `Event`s through the Eclipse SmartHome event bus in an asynchronous way. The `EventSubscriber` defines the callback interface for receiving Events from the event bus. The Event Subscriber provides an `EventFilter` in order to receive specific events by an Event Publisher if the filter applies. The Event itself will be subclassed for each event type, which exists in the System (e.g. ItemCommandEvent, ItemUpdateEvent, ThingStatusInfoEvent, etc.). As for different event types different concrete classes exist, an `EventFactory` creates concrete event type instances. 

## API Usage Example

### Receive Events

The following Java snippet shows how to listen for Events. Therefore, the `EventSubscriber` interface must be implemented.

```java
public class SomeItemEventSubscriber implements EventSubscriber {
    
	Set<String> getSubscribedEventTypes() {
		return Sets.newHashSet(ItemCommandEvent.TYPE);
	}

	public EventFilter getEventFilter() {
        return new TopicEventFilter("smarthome/items/*");
    }

    public void receiveEvent(Event event) {
        if(event instanceof ItemCommandEvent) {
            ItemCommandEvent itemCommandEvent = (ItemCommandEvent) event;
            itemCommandEvent.getItemName();
            // ...
        }
    }
}
```
The `SomeItemEventSubscriber` delivers event types to which the Event Subscriber is subscribed to and an `EventFilter` in order to receive events based on the topic. Received events can be casted to the implementation class for further processing.

Event Subscribers must be registered via OSGi Declarative Services (DS) with the interface `org.eclipse.smarthome.event.EventSubscriber` as illustrated below.

```xml
<scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" immediate="true" name="SomeItemEventSubscriber">
   <implementation class="org.eclipse.smarthome.core.items.events.SomeItemEventSubscriber"/>
   <service>
      <provide interface="org.eclipse.smarthome.core.events.EventSubscriber"/>
   </service>
</scr:component>
```  

### Send Events

The Java snippet below illustrates how to send Events. To create an Event the corresponding Event Factory can be used.
```java 
EventPublisher eventPublisher; 

ItemCommandEvent itemCommandEvent = ItemEventFactory.createCommandEvent("TheItemName", OnOffType.ON);
eventPublisher.postEvent(itemCommandEvent);
```

The Event Publisher will be injected via OSGi Declarative Services.

```xml
<scr:component xmlns:scr="http://www.osgi.org/xmlns/scr/v1.1.0" immediate="true" name="SomeComponentWantsToPublish">
	<!-- ... -->
   <reference bind="setEventPublisher" cardinality="1..1" interface="org.eclipse.smarthome.core.events.EventPublisher" 
   		name="EventPublisher" policy="static" unbind="unsetEventPublisher"/>
</scr:component>
```
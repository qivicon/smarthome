# Tags

Tags are used to describe *Items* in a more specific way than their type normally does. For example a *SwitchItem* can be used for different things: a sensor, a button or a switch. A sensor is read-only, whilst a button can be activated by the user. Other meta information is not available. 

Tags can overcome this situation by _tagging_ the respective items. A Button for instance could be represented by a *SwitchItem* with the tag "Button". A motion sensor could be a *SwitchItem* with the tag "MotionSensor". 

Tags can also be used to _classify_ items according the user's needs. Some sensors might be useful for triggering an alarm situation, those sensors might be tagged with "AlarmSensor". Some actors, used for indicating alarm situations, can be tagged with "AlarmDevice". Then an alarm rule could easily be defined: "If any sensor with the tag 'AlarmSensor' is triggered, then activate all devices tagged with 'AlarmDevice'". Whenever a possible alarm sensor is added to this scenario, it will also be tagged with 'AlarmSensor' and without extending the rule it now could also trigger alarms. 

## Implementation Notes

* Tags are implemented as plain strings. 
* Tag-libs for commonly used tags (e.g. device types) are not provided . 
* Items could have multiple tags. 
* The ItemRegistry provides methods to filter items by tag. 

## Default Tags

The XML definition of a *ThingType* allows to assign default tags to *Channels* for these *Things*. All items bound to this channel will automatically be tagged with these default tags. This gives the binding developer the chance, to classify the possible items of the binding. 

## Examples

### Handling Items

```java 
	// adding a tag to an item
	item.addTag("MotionSensor"); 
	
	// removing a tags 
	item.removeTag("Sensor"); 
	item.removeAllTags();  
	
	// search all items with "Alarm" tag
	List<Item> alarmItems = new ArrayList<>(); 
	for (Item item : items) {
		if (item.hasTag("Alarm")) {
			alarmItems.add(item); 
		}
	}
```

### Retrieving Items

```java
	// get all alarming motion sensors
	Collection<Item> alarmMotionSensors = itemRegistry.getItemsByTag("MotionSensor", "Alarm"); 
	
	// get all alarming switch items
	Collection<SwitchItem> = itemRegistry.getItemsByTag(SwitchItem.class, "Alarm"); 
```


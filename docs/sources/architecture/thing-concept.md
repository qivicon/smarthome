# Thing Concept

## Things

TBD

## Channels

A channel describes a specific functionality of a thing and can be linked to an item. So the basic information is, which command types the channel can handle and which state it sends to the linked item. This can be specified by the accepted item type. Inside the thing type description XML file a list of channels can be referenced. The channel type definition is specified on the same level as the thing type definition. That way channels can be reused in different things.

### Categories

The channel type definition allows to specify a category. Together with the definition of the `readOnly` attribute in the state description, user interfaces get an idea how to render an item for this channel. A binding should classify each channel into one of the existing categories. This is a list of all predefined categories with their usual accssible mode and the according item type:

| Category      | Accessible Mode | Item Type              |
|---------------|-----------------|------------------------|
| Alarm         | R, RW           | Switch                 |
| Battery       | R               | Switch, Number         |
| Blinds        | RW              | Rollershutter          |
| ColorLight    | RW              | Color                  |
| Contact       | R               | Contact                |
| DimmableLight | RW              | Dimmer                 |
| CarbonDioxide | R               | Switch, Number         |
| Door          | R, RW           | Switch                 |
| Energy        | R               | Number                 |
| Fan           | RW              | Switch, Number, String |
| Fire          | R               | Switch                 |
| Flow          | R               | Number                 |
| GarageDoor    | RW              | String                 |
| Gas           | R               | Switch, Number         |
| Humidity      | R               | Number                 |
| Light         | R, RW           | Switch, Number         |
| Motion        | R               | Switch                 |
| MoveControl   | RW              | String                 |
| Player        | RW              | Player                 |
| PowerOutlet   | RW              | Switch                 |
| Pressure      | R               | Number                 |
| Rain          | R               | Switch, Number         |
| Recorder      | RW              | String                 |
| Smoke         | R               | Switch                 |
| SoundVolume   | R, RW           | Number                 |
| Switch        | RW              | Switch                 |
| Temperature   | R, RW           | Number                 |
| Water         | R               | Switch, Number         |
| Wind          | R               | Number                 |
| Window        | R, RW           | String, Switch         |
| Zoom          | RW              | String                 |

The accessible mode indicates whether a category could have `read only` flag configured to true or not. For example the "Motion" category only can be used for sensors, so `read only` can not be false. Temperature can be either measured or adjusted, so its accessible mode is R and RW, which means the read only flag can be true or false. In addition categories are realated to specific item types. For example the 'Energy' category can only be used for Number items. But 'Rain' could be either expressed as Switch item, where it only indicates if it rains or not, or as Number, which gives information about the rain intensity.

The list of categories may not be complete and not every device will fit into one of these categories. It is possible to define own categories. If the category is used widly used, the list of predefined categories can be extended. Moreover not all user interfaces will support all categories. It is more important to specify the `read only` information and state information, so that default controls can rendered, even if the category is not supported.

### Channel Groups

Some devices might have a lot of channels. There are also complex devices like a multi-channel actuator, which is installed inside switchboard, but controls switches inside other rooms. Therefore channel groups can be used to group a set of channels together into one logical group.

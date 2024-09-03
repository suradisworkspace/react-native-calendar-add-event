# react-native-calendar-add-event

add event to calendar application from react-native

## Installation

```sh
yarn add react-native-calendar-add-event
```

## Android Specific
in `AndroidManifest.xml` add Permissions
```xml
    <uses-permission android:name="android.permission.READ_CALENDAR" />
    <uses-permission android:name="android.permission.WRITE_CALENDAR" />
```

## iOS Specific

In `Info.plist` update/add `Privacy - Calendars Usage Description`

```plist
<key>NSCalendarsUsageDescription</key>
<string>The reason to use calendar</string>
```


## Usage

```js
import CalendarEvent from 'react-native-calendar-add-event';

// ...

const result = CalendarEvent.addEvent('some event', startTime, endTime)
```


## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)

import { NativeModules, Platform } from 'react-native';
import type { Double } from 'react-native/Libraries/Types/CodegenTypes';
const LINKING_ERROR =
  `The package 'react-native-calendar-add-event' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const CalendarAddEventModule = isTurboModuleEnabled
  ? require('./NativeCalendarAddEvent').default
  : NativeModules.CalendarAddEvent;

const CalendarAddEvent = CalendarAddEventModule
  ? CalendarAddEventModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function addEvent(
  title: string,
  startDate: Date,
  endDate: Date
): Promise<string> {
  const convertedStartDate: Double = startDate.getTime();
  const convertedEndDate: Double = endDate.getTime();
  // const nowTime = Date.now();
  // if (nowTime > convertedStartDate) {
  //   throw 'startDate must not be the past';
  // }
  return CalendarAddEvent.addEvent(title, convertedStartDate, convertedEndDate);
}

export default {
  addEvent,
};

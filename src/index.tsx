import { NativeModules, Platform } from 'react-native';
// import type { Double } from 'react-native/Libraries/Types/CodegenTypes';
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

type AddEventParams = {
  title: string;
  startDate: Date;
  endDate: Date;
};
export function addEvent({
  title,
  startDate,
  endDate,
}: AddEventParams): Promise<string> {
  const convertedStartDate = startDate.toISOString();
  const convertedEndDate = endDate.toISOString();
  return CalendarAddEvent.addEvent({
    title,
    startDate: convertedStartDate,
    endDate: convertedEndDate,
  });
}

export default {
  addEvent,
};

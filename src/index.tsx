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
  startDate: Date | string;
  endDate: Date | string;
  allDay?: boolean;
  reminder?: number;
};

const formatDate = (date: string | Date) => {
  if (typeof date === 'string') {
    return date;
  }
  return date.toISOString();
};

export function addEvent({
  title,
  startDate,
  endDate,
  allDay,
  reminder,
}: AddEventParams): Promise<string> {
  const convertedStartDate = formatDate(startDate);
  const convertedEndDate = formatDate(endDate);
  return CalendarAddEvent.addEvent({
    title,
    startDate: convertedStartDate,
    endDate: convertedEndDate,
    allDay,
    reminder,
  });
}

export default {
  addEvent,
};

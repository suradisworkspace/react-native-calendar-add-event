import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
import type { Double } from 'react-native/Libraries/Types/CodegenTypes';

type AddEventParams = {
  title: string;
  startDate: Double;
  endDate: Double;
};
export interface Spec extends TurboModule {
  addEvent(params: AddEventParams): Promise<string>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('CalendarAddEvent');

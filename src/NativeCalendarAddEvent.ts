import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';
// import type { Double } from 'react-native/Libraries/Types/CodegenTypes';

export interface Spec extends TurboModule {
  addEvent(params: Object): Promise<string>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('CalendarAddEvent');

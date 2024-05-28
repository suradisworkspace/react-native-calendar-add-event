
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNCalendarAddEventSpec.h"

@interface CalendarAddEvent : NSObject <NativeCalendarAddEventSpec>
#else
#import <React/RCTBridgeModule.h>

@interface CalendarAddEvent : NSObject <RCTBridgeModule>
#endif

@end

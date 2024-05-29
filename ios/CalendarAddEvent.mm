#import "CalendarAddEvent.h"
#import <Foundation/Foundation.h>
#import <EventKit/EventKit.h>

@implementation CalendarAddEvent
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(addEvent:(NSString *)title startTime:(double)startTime endTime:(double)endTime resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
    EKEventStore *eventStore = [[EKEventStore alloc] init];
    [eventStore requestAccessToEntityType:EKEntityTypeEvent completion:^(BOOL granted, NSError *error) {
        if (granted) {
            EKEvent *event = [EKEvent eventWithEventStore:eventStore];
            event.title = title;
            NSTimeInterval startInterval = startTime / 1000;
            NSTimeInterval endInterval = endTime / 1000;
            event.startDate = [NSDate dateWithTimeIntervalSince1970:startInterval];
            event.endDate = [NSDate dateWithTimeIntervalSince1970:endInterval];
            event.calendar = eventStore.defaultCalendarForNewEvents;
            NSError *saveEventError = nil;
            BOOL success = [eventStore saveEvent:event span:EKSpanThisEvent commit:YES error:&saveEventError];
            if (success) {
                resolve(@"Event Added");
            } else {
                reject(@"react-native-calendar-add-event error", @"Error adding event", saveEventError);
            }
        } else {
            reject(@"react-native-calendar-add-event error",@"No calendar permission", error);
        }
    }];
}

// Don't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeCalendarAddEventSpecJSI>(params);
}
#endif

@end

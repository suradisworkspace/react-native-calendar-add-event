#import "CalendarAddEvent.h"
#import <Foundation/Foundation.h>
#import <EventKit/EventKit.h>
#import <React/RCTConvert.h>
#import "react_native_calendar_add_event-Swift.h"

@implementation CalendarAddEvent
RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(addEvent:(NSDictionary *)properties resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
{
    CalendarAddEventImpl *calendarImp = [CalendarAddEventImpl sharedInstance];
    [calendarImp addEventWithProperties:properties completionHandler:^(NSNumber * _Nullable staus, NSError * _Nullable err) {
        // do some code
    }];
    // (NSString *)title startTime:(double)startTime endTime:(double)endTime
    
//    EKEventStore *eventStore = [[EKEventStore alloc] init];
//    [eventStore requestAccessToEntityType:EKEntityTypeEvent completion:^(BOOL granted, NSError *error) {
//        if (granted) {
//            EKEvent *event = [EKEvent eventWithEventStore:eventStore];
//            NSString *title = [RCTConvert NSString:properties[@"title"]];
//            NSDate *startDate = [RCTConvert NSDate:properties[@"startDate"]];
//            NSDate *endDate = [RCTConvert NSDate:properties[@"endDate"]];
//            if (title) {
//                event.title = title;
//            }
//            if (startDate) {
//                event.startDate = startDate;
//            }
//            if (endDate) {
//                event.endDate = endDate;
//            }
//            event.calendar = eventStore.defaultCalendarForNewEvents;
//            NSError *saveEventError = nil;
//            BOOL success = [eventStore saveEvent:event span:EKSpanThisEvent commit:YES error:&saveEventError];
//            if (success) {
//                resolve(@"Event Added");
//            } else {
//                reject(@"react-native-calendar-add-event error", @"Error adding event", saveEventError);
//            }
//        } else {
//            reject(@"react-native-calendar-add-event error",@"No calendar permission", error);
//        }
//    }];
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

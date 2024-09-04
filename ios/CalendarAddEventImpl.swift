import Foundation
import EventKit
@objc public class CalendarAddEventImpl: NSObject {
    @objc public static let sharedInstance = CalendarAddEventImpl()

    @objc public func addEvent(properties:NSObject) async throws-> NSNumber {
        NSLog("Trigger Swift")
        var eventStore = EKEventStore()
        if #available(iOS 17.0, *) {
            do{
                var granted = try await eventStore.requestFullAccessToEvents()
                return 1
            } catch {
                throw NSError(domain: "permission not granted", code: 11)
            }
        } else {
            do {
                var granted = try await eventStore.requestAccess(to: EKEntityType.event)
                return 1
            } catch {
                throw NSError(domain: "permission not granted", code: 11)
            }
        }
    }
}


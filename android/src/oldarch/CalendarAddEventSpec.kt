package com.calendaraddevent

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReadableMap

abstract class CalendarAddEventSpec internal constructor(context: ReactApplicationContext) :
  ReactContextBaseJavaModule(context) {

  abstract fun addEvent(properties: ReadableMap, promise: Promise)
  abstract fun addEventIntent(properties: ReadableMap, promise: Promise)

}

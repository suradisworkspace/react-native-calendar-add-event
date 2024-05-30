package com.calendaraddevent

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.provider.CalendarContract
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap


class CalendarAddEventModule internal constructor(context: ReactApplicationContext) : CalendarAddEventSpec(context) {
  override fun getName(): String {
    return NAME
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @ReactMethod
  override fun addEvent(properties: ReadableMap, promise: Promise) {
    //    val cal: Calendar = Calendar.getInstance()
    val intent = Intent(Intent.ACTION_EDIT).setData(CalendarContract.Events.CONTENT_URI).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    if (properties.hasKey("title")) {
      intent.putExtra(CalendarContract.Events.TITLE, properties.getString("title"))
    }
    if(properties.hasKey("startDate")) {
      val formattedDate = dateFormatter.parse(properties.getString("startDate"))
      val cStartDate: Calendar = Calendar.getInstance()
      cStartDate.time = formattedDate
      intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cStartDate.timeInMillis)
    }
    if(properties.hasKey("endDate")) {
      val formattedDate = dateFormatter.parse(properties.getString("endDate"))
      val cEndDate: Calendar = Calendar.getInstance()
      cEndDate.time = formattedDate
      intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cEndDate.timeInMillis)


    }
    val context = getReactApplicationContext()
    context.startActivity(intent)
    promise.resolve(properties.getString("startDate"))
  }

  companion object {
    const val NAME = "CalendarAddEvent"
  }
}

package com.calendaraddevent

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.provider.CalendarContract
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap


class CalendarAddEventModule internal constructor(context: ReactApplicationContext) : CalendarAddEventSpec(context) {
  private var requestCode = 0
  override fun getName(): String {
    return NAME
  }

  fun requestPermission() {
    val context = reactApplicationContext
    val activity = context.currentActivity
      ?: throw IllegalStateException(
        "Tried to use permissions API while not attached to an " + "Activity.")
    val permissions = arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR)
    for(permission in permissions) {
      if(context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
        requestCode++
        activity.requestPermissions(arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR), requestCode)
      }
    }
  }

  private fun getCalendarId() : Long? {
    val context = reactApplicationContext
    val projection = arrayOf(CalendarContract.Calendars._ID, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)

    var calCursor = context.contentResolver.query(
      CalendarContract.Calendars.CONTENT_URI,
      projection,
      CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.IS_PRIMARY + "=1",
      null,
      CalendarContract.Calendars._ID + " ASC"
    )

    if (calCursor != null && calCursor.count <= 0) {
      calCursor = context.contentResolver.query(
        CalendarContract.Calendars.CONTENT_URI,
        projection,
        CalendarContract.Calendars.VISIBLE + " = 1",
        null,
        CalendarContract.Calendars._ID + " ASC"
      )
    }

    if (calCursor != null) {
      if (calCursor.moveToFirst()) {
//        val calName: String
        val calID: String
//        val nameCol = calCursor.getColumnIndex(projection[1])
        val idCol = calCursor.getColumnIndex(projection[0])

//        calName = calCursor.getString(nameCol)
        calID = calCursor.getString(idCol)

        calCursor.close()
        return calID.toLong()
      }
    }
    return null
  }

  private fun addEventToCalendar(context: ReactApplicationContext, properties: ReadableMap, promise: Promise) {
    val contentResolver = context.contentResolver
    val calendarValues = ContentValues()
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    calendarValues.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Bangkok")
    if (properties.hasKey("title")) {
      calendarValues.put(CalendarContract.Events.TITLE, properties.getString("title"))
    }
    if(properties.hasKey("startDate")) {
      val formattedDate = dateFormatter.parse(properties.getString("startDate"))
      val cStartDate: Calendar = Calendar.getInstance()
      cStartDate.time = formattedDate
      calendarValues.put(CalendarContract.Events.DTSTART, cStartDate.timeInMillis)
    }
    if(properties.hasKey("endDate")) {
      val formattedDate = dateFormatter.parse(properties.getString("endDate"))
      val cEndDate: Calendar = Calendar.getInstance()
      cEndDate.time = formattedDate
      calendarValues.put(CalendarContract.Events.DTEND, cEndDate.timeInMillis)
    }
    if(properties.hasKey("allDay")) {
      calendarValues.put(CalendarContract.Events.ALL_DAY, properties.getBoolean("allDay"))
    }
    if(properties.hasKey("calendarId")) {
      val calendarId = properties.getDouble("calendarId")
//          val uri =
//            ContentUris.withAppendedId(CalendarContract.Calendars.CONTENT_URI, calendarId.toLong())
//          val calendar = Calendar.getInstance()
//          if(calendar != null) {
//
//          }
      calendarValues.put(CalendarContract.Events.CALENDAR_ID, calendarId)
    } else {
      val calendarId = getCalendarId()
      if(calendarId != null) {
        calendarValues.put(CalendarContract.Events.CALENDAR_ID, calendarId)
      } else {
        promise.reject(Exception("Calendar Id not found"))
      }
    }
    val uri = contentResolver.insert(CalendarContract.Events.CONTENT_URI, calendarValues)
    val eventId = uri?.lastPathSegment?.toString()
    if(properties.hasKey("reminder")) {
      val reminderValues = ContentValues().apply {
        put(CalendarContract.Reminders.MINUTES, properties.getDouble("reminder"))
        put(CalendarContract.Reminders.EVENT_ID, eventId)
        put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
      }
      contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues)
    }
      promise.resolve(eventId)
  }

  @ReactMethod
  override fun addEvent( properties: ReadableMap, promise: Promise) {
    val context = reactApplicationContext
//    val activity = context.currentActivity
//      ?: throw IllegalStateException(
//        "Tried to use permissions API while not attached to an " + "Activity.")
    when {
      context.checkSelfPermission(
        Manifest.permission.WRITE_CALENDAR
      ) == PackageManager.PERMISSION_GRANTED -> {
        addEventToCalendar(context, properties, promise)

      }
//      activity.shouldShowRequestPermissionRationale(
//         Manifest.permission.WRITE_CALENDAR) -> {
//        // In an educational UI, explain to the user why your app requires this
//        // permission for a specific feature to behave as expected, and what
//        // features are disabled if it's declined. In this UI, include a
//        // "cancel" or "no thanks" button that lets the user continue
//        // using your app without granting the permission.
////        showInContextUI(...)
//      }
      else -> {
        requestPermission()
      }
    }
  }

  @ReactMethod
  override fun addEventIntent(properties: ReadableMap, promise: Promise) {
    val intent = Intent(Intent.ACTION_EDIT).setData(CalendarContract.Events.CONTENT_URI).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
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
    val context = reactApplicationContext
    context.startActivity(intent)
    promise.resolve(properties.getString("startDate"))
  }

  companion object {
    const val NAME = "CalendarAddEvent"
  }
}

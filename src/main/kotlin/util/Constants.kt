package util

object Constants {

    // gradle
    const val GRADLE_FOR_FCM = "com.google.firebase:firebase-messaging:20.0.0"

    // Title messages for the studio notification
    const val NOTIFICATION_TITLE = "FCM For Notification"
    const val NOTIFICATION_CONTENT = "FirebaseMessagingService has been added successfully"
    const val DISPLAY_ID = "Harsh Panchal"

    // Error Titles
    const val ERROR_TITLE_SYNC_FAILED = "SYNC FAILED"

    // Error Messages
    const val ERROR_BASE_PATH_NOT_FOUND = "Project base path not found."
    const val ERROR_PROJECT_BASE_DIR_NOT_FOUND = "Project base directory not found."
    const val ERROR_BUILD_GRADLE_NOT_FOUND = "Project doesn't contain any gradle file."
    const val ERROR_SYNC_FAILED = "Project sync failed."

    // Commons
    const val DEFAULT_MODULE_NAME = "app"
    const val IMPLEMENTATION = "implementation"
    const val DEPENDENCIES = "dependencies"
    const val APPLICATION = "application"
    const val COLOR_PRIMARY = "colorPrimary"
    const val COLOR_PRIMARY_DARK = "colorPrimaryDark"
    const val FCM_INSTRUCTION = "FCM_Instruction"
    const val FCM_DIRECTORY = "fcm"

    const val COLOR_PRIMARY_FROM_XML = "R.color.colorPrimary"
    const val COLOR_PRIMARY_FROM_ANDROID = "android.R.color.holo_blue_dark"

    // Panel
    const val MY_FIREBASE_MESSAGING_SERVICE = "MyFirebaseMessagingService"
    const val FCM_SERVICE_NAME = "FCM Service Name:"
    const val PENDINGINTENT_ACTIVITY_NAME = "PendingIntent Activity Name:"
    const val CONTENT_TITLE = "Content Title:"
    const val CONTENT_TEXT = "Content Text:"
    const val NEED_INSTRUCTION = "Need Instruction:"
    const val NOTES_INSTRUCTION = "Note: If you will check Instruction section, then you will get the instruction text file for the FCM notification."
}
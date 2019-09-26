package util

import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

object Methods {

    fun checkPrimaryColorInColorsFile(project: Project): Boolean {
        val basePath = project.basePath
        val projectBaseDir = LocalFileSystem.getInstance().findFileByPath(basePath!!)
        val colorsVirtualFile: VirtualFile? = projectBaseDir!!
            .findChild(Constants.DEFAULT_MODULE_NAME)!!
            .findChild("src")!!
            .findChild("main")!!
            .findChild("res")!!
            .findChild("values")!!
            .findChild("colors.xml")
        return if (colorsVirtualFile != null) {
            val colorsXML = FileDocumentManager.getInstance().getDocument(colorsVirtualFile)
            val documentText = colorsXML!!.text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val colorPrimary = documentText.indices.filter {
                (documentText[it].contains(Constants.COLOR_PRIMARY) && !documentText[it].contains(Constants.COLOR_PRIMARY_DARK))
            }
            colorPrimary.isNotEmpty()
        } else {
            false
        }
    }


    fun getAndroidManifestContent(
        packageName: String,
        serviceNameText: String
    ): String {
        return "\n        <service\n" +
                "            android:name=\"$packageName.$serviceNameText\"\n" +
                "            android:exported=\"false\">\n" +
                "            <intent-filter>\n" +
                "                <action android:name=\"com.google.firebase.MESSAGING_EVENT\" />\n" +
                "            </intent-filter>\n" +
                "        </service>\n" +
                "        \n" +
                "        <meta-data\n" +
                "            android:name=\"firebase_messaging_auto_init_enabled\"\n" +
                "            android:value=\"false\" />\n" +
                "        \n" +
                "        <meta-data\n" +
                "            android:name=\"firebase_analytics_collection_enabled\"\n" +
                "            android:value=\"false\" />\n"
    }

    fun getFileContent(
        packageName: String,
        serviceNameText: String,
        pendingIntentText: String,
        contentTitleText: String,
        contentTextText: String,
        color: String
    ): String {
        return "package $packageName\n\n" +
                "import android.app.ActivityManager\n" +
                "import android.app.ActivityManager.RunningAppProcessInfo\n" +
                "import android.app.NotificationChannel\n" +
                "import android.app.NotificationManager\n" +
                "import android.app.PendingIntent\n" +
                "import android.content.Context\n" +
                "import android.content.Intent\n" +
                "import android.media.RingtoneManager\n" +
                "import android.os.Build\n" +
                "import android.util.Log\n" +
                "import androidx.core.app.NotificationCompat\n" +
                "import androidx.core.content.ContextCompat\n" +
                "import com.google.firebase.messaging.FirebaseMessagingService\n" +
                "import com.google.firebase.messaging.RemoteMessage\n" +
                "import org.json.JSONObject\n" +
                "\n" +
                "\n" +
                "class $serviceNameText : FirebaseMessagingService() {\n" +
                "\n" +
                "    companion object {\n" +
                "        private const val TAG = \"MyFirebaseService\"\n" +
                "        private const val NOTIFICATION_TYPE = \"notificationType\"\n" +
                "    }\n" +
                "\n" +
                "    override fun onMessageReceived(remoteMessage: RemoteMessage) {\n" +
                "        super.onMessageReceived(remoteMessage)\n" +
                "        // [START_EXCLUDE]\n" +
                "        // There are two types of messages data messages and notification messages. Data messages are handled\n" +
                "        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type\n" +
                "        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app\n" +
                "        // is in the foreground. When the app is in the background an automatically generated notification is displayed.\n" +
                "        // When the user taps on the notification they are returned to the app. Messages containing both notification\n" +
                "        // and data payloads are treated as notification messages. The Firebase console always sends notification\n" +
                "        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options\n" +
                "        // [END_EXCLUDE]\n" +
                "\n" +
                "        // Check if message contains a data payload.\n" +
                "        remoteMessage.data.isNotEmpty().let {\n" +
                "            remoteMessage.data.let { data ->\n" +
                "                val jsonObject = JSONObject(data as Map<*, *>)\n" +
                "                if (jsonObject.has(NOTIFICATION_TYPE)) {\n" +
                "                    if (applicationInForeground()) {\n" +
                "                        createOrderNotification(jsonObject.toString())\n" +
                "                    } else {\n" +
                "                        createOrderNotification(jsonObject.toString())\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        // Check if message contains a notification payload.\n" +
                "        remoteMessage.notification?.let { notification -> \n" +
                "            Log.d(TAG, \"Message Notification Body: \${notification.body}\")\n" +
                "        }\n" +
                "    }\n" +
                "    // [END receive_message]\n" +
                "\n" +
                "    private fun createOrderNotification(notificationData: String) {\n" +
                "        val pendingIntent = PendingIntent.getActivity(this, \n" +
                "                System.currentTimeMillis().toInt(), \n" +
                "                Intent(this, $pendingIntentText::class.java).apply {\n" +
                "            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)\n" +
                "        }, PendingIntent.FLAG_ONE_SHOT)\n" +
                "\n" +
                "        val channelId = \"general\"\n" +
                "        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)\n" +
                "        val notificationBuilder = NotificationCompat.Builder(this, channelId)\n" +
                "                .setContentTitle(\"$contentTitleText\")\n" +
                "                .setContentText(\"$contentTextText\")\n" +
                "                .setAutoCancel(true)\n" +
                "                .setSound(defaultSoundUri)\n" +
                "                .setContentIntent(pendingIntent)\n" +
                "\n" +
                "        notificationBuilder.color = ContextCompat.getColor(this, $color)\n" +
                "\n" +
                "        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager\n" +
                "\n" +
                "        // Since android Oreo notification channel is needed.\n" +
                "        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {\n" +
                "            val channel = NotificationChannel(channelId,\n" +
                "                    \"Channel human readable title\",\n" +
                "                    NotificationManager.IMPORTANCE_DEFAULT)\n" +
                "            notificationManager.createNotificationChannel(channel)\n" +
                "        }\n" +
                "\n" +
                "        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())\n" +
                "    }\n" +
                "\n" +
                "    private fun applicationInForeground(): Boolean {\n" +
                "        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager\n" +
                "        val services = activityManager.runningAppProcesses\n" +
                "        var isActivityFound = false\n" +
                "        if (!services.isNullOrEmpty() && (services[0].processName.equals(packageName, ignoreCase = true)\n" +
                "                        && services[0].importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)) {\n" +
                "            isActivityFound = true\n" +
                "        }\n" +
                "        return isActivityFound\n" +
                "    }\n" +
                "}"
    }

    fun getInstructionFileContent(): String {
        return "\n" +
                "We have added the dependency in build.gradle file and Service for the messaging.\n" +
                "But we need to add following things in particular files.\n" +
                "\n" +
                "============================ IN AndroidManifest.xml  ============================\n" +
                "\n" +
                "<service\n" +
                "    android:name=\"Your Service Name\"\n" +
                "    android:exported=\"false\">\n" +
                "    <intent-filter>\n" +
                "        <action android:name=\"com.google.firebase.MESSAGING_EVENT\" />\n" +
                "    </intent-filter>\n" +
                "</service>\n" +
                "\n" +
                "============================ IN AndroidManifest.xml  ============================\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "============================ IN AndroidManifest.xml (Set notification icon color) (Optional) ============================\n" +
                "\n" +
                "Within the application component, metadata elements to set a default notification icon and color.\n" +
                "Android uses these values whenever incoming messages do not explicitly set icon or color.\n" +
                "\n" +
                "<!-- Set custom default icon. This is used when no icon is set for incoming notification messages.\n" +
                "     See README(https://goo.gl/l4GJaQ) for more. -->\n" +
                "<meta-data\n" +
                "    android:name=\"com.google.firebase.messaging.default_notification_icon\"\n" +
                "    android:resource=\"@drawable/ic_stat_ic_notification\" />\n" +
                "\n" +
                "<!-- Set color used with incoming notification messages. This is used when no color is set for the incoming\n" +
                "     notification message. See README(https://goo.gl/6BKBk7) for more. -->\n" +
                "<meta-data\n" +
                "    android:name=\"com.google.firebase.messaging.default_notification_color\"\n" +
                "    android:resource=\"@color/colorAccent\" />\n" +
                "\n" +
                "============================ IN AndroidManifest.xml (Set notification icon color) (Optional) ============================\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "============================ IN AndroidManifest.xml (Notification Channel) (Optional) ============================\n" +
                "\n" +
                "From Android 8.0 (API level 26) and higher, notification channels are supported and recommended.\n" +
                "FCM provides a default notification channel with basic settings.\n" +
                "If you prefer to create and use your own default channel,\n" +
                "  set default_notification_channel_id to the ID of your notification channel object as shown;\n" +
                "FCM will use this value whenever incoming messages do not explicitly set a notification channel.\n" +
                "\n" +
                "<meta-data\n" +
                "    android:name=\"com.google.firebase.messaging.default_notification_channel_id\"\n" +
                "    android:value=\"@string/default_notification_channel_id\" />\n" +
                "\n" +
                "============================ IN AndroidManifest.xml (Notification Channel) (Optional) ============================\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "============================ Retrieve the current registration token ============================\n" +
                "\n" +
                "## When you need to retrieve the current token, call following.\n" +
                "\n" +
                "        FirebaseInstanceId.getInstance().getInstanceId():\n" +
                "\n" +
                "\n" +
                "## You can set CompleteListener inside your MainActivity. Please follow below one.\n" +
                "\n" +
                "FirebaseInstanceId.getInstance().instanceId\n" +
                "        .addOnCompleteListener(OnCompleteListener { task ->\n" +
                "            if (!task.isSuccessful) {\n" +
                "                Log.w(TAG, \"getInstanceId failed\", task.exception)\n" +
                "                return@OnCompleteListener\n" +
                "            }\n" +
                "\n" +
                "            // Get new Instance ID token\n" +
                "            val token = task.result?.token\n" +
                "\n" +
                "            // Log and toast\n" +
                "            val msg = getString(R.string.msg_token_fmt, token)\n" +
                "            Log.d(TAG, msg)\n" +
                "            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()\n" +
                "        })\n" +
                "\n" +
                "\n" +
                "============================ Retrieve the current registration token ============================\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "============================ Monitor token generation ============================\n" +
                "\n" +
                "## The onNewToken callback fires whenever a new token is generated.\n" +
                "\n" +
                "/**\n" +
                " * Called if InstanceID token is updated. This may occur if the security of\n" +
                " * the previous token had been compromised. Note that this is called when the InstanceID token\n" +
                " * is initially generated so this is where you would retrieve the token.\n" +
                " */\n" +
                "override fun onNewToken(token: String) {\n" +
                "    Log.d(TAG, \"Refreshed token: \$token\")\n" +
                "\n" +
                "    // If you want to send messages to this application instance or\n" +
                "    // manage this apps subscriptions on the server side, send the\n" +
                "    // Instance ID token to your app server.\n" +
                "    sendRegistrationToServer(token)\n" +
                "}\n" +
                "\n" +
                "============================ Monitor token generation ============================\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "============================ Prevent auto initialization ============================\n" +
                "\n" +
                "Firebase generates an Instance ID, which FCM uses to generate a registration token and Analytics uses for data collection.\n" +
                "When an Instance ID is generated, the library will upload the identifier and configuration data to Firebase.\n" +
                "If you prefer to prevent Instance ID autogeneration,\n" +
                "disable auto initialization for FCM and Analytics (you must disable both)\n" +
                "by adding these metadata values to your AndroidManifest.xml\n" +
                "\n" +
                "<meta-data\n" +
                "    android:name=\"firebase_messaging_auto_init_enabled\"\n" +
                "    android:value=\"false\" />\n" +
                "<meta-data\n" +
                "    android:name=\"firebase_analytics_collection_enabled\"\n" +
                "    android:value=\"false\" />\n" +
                "\n" +
                "\n" +
                "## To re-enable FCM, make a runtime call\n" +
                "\n" +
                "        FirebaseMessaging.getInstance().isAutoInitEnabled = true\n" +
                "\n" +
                "## This value persists across app restarts once set.\n" +
                "\n" +
                "============================ Prevent auto initialization ============================\n"
    }
}
package de.max.trailblazerfrontendv1.location

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import de.max.trailblazerfrontendv1.R
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.MessageStrings
import de.max.trailblazerfrontendv1.Util.ViewModelHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient
    private lateinit var cameraPositionUpdater: CameraPositionUpdater

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        cameraPositionUpdater = ViewModelHolder.ViewModelHolderObject.mapsViewModel
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext),
            cameraPositionUpdater
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        GeneralConstants.fetchingGps = true
        val gpsNotification = NotificationCompat.Builder(this, "location")
            .setContentTitle(MessageStrings.gpsNotificationTitle)
            .setContentText(MessageStrings.gpsNotificationText)
            .setSmallIcon(R.drawable.trail_blazer_app_icon_32)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient.getLocationUpdates(15000L)
            .catch { e -> e.printStackTrace()}
            .onEach { location ->
                val lat = location.latitude.toString()
                val lng = location.longitude.toString()
                val updatedNotification = gpsNotification.setContentText(
                    MessageStrings.gpsNotificationText + "($lat, $lng)"
                )
                notificationManager.notify(1, updatedNotification.build())
            }
            .launchIn(serviceScope)
        startForeground(1, gpsNotification.build())
    }

    private fun stop() {
        GeneralConstants.fetchingGps = false
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            stopForeground(STOP_FOREGROUND_DETACH)
        } else {
            stopForeground(true)
        }
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}
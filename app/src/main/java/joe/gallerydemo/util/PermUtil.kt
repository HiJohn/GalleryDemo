package joe.gallerydemo.util

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.support.v4.app.ActivityCompat

/**
 * Created by takashi on 2018/2/25.
 */
@TargetApi(Build.VERSION_CODES.M)
object PermUtil {

    private const val SD_READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE
    private const val SD_WRITE_PERM = Manifest.permission.WRITE_EXTERNAL_STORAGE

    private const val CAMERA_PERM = Manifest.permission.CAMERA
    private const val AUDIO_RECORD_PERM = Manifest.permission.RECORD_AUDIO

    private const val GRANTED = PackageManager.PERMISSION_GRANTED
    private const val DENIED = PackageManager.PERMISSION_DENIED
    const val SD_REQ = 9111
//        private val TAG = "PermUtil"

    fun isGranted(context: Context, permission: String): Boolean {

        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun isDenied(context: Context, permission: String): Boolean {

        return context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED
    }

    fun isBelowVersionM():Boolean{
        return Build.VERSION.SDK_INT<Build.VERSION_CODES.M
    }

    fun isSdDenied(context: Context): Boolean {
        if (isBelowVersionM())return false

        return isDenied(context, Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    fun requestPermissions(activity: Activity, reqCode: Int, vararg perms: String) {
        ActivityCompat.requestPermissions(activity, perms, reqCode)
    }


    fun isSdGranted(context: Context): Boolean {
        if (isBelowVersionM())return true

        return isGranted(context, Manifest.permission.READ_EXTERNAL_STORAGE)
    }


    fun requestSDPermissions(activity: Activity, reqCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(SD_READ_PERM, SD_WRITE_PERM), reqCode)
    }

    fun requestSD(activity: Activity){
        if (isBelowVersionM())return
        ActivityCompat.requestPermissions(activity, arrayOf(SD_READ_PERM, SD_WRITE_PERM), SD_REQ)
    }

    fun isScreenOrientPhoneLocked(context: Context): Boolean {
        val str = Settings.System.getInt(context.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0)
        return str != 1

    }


}
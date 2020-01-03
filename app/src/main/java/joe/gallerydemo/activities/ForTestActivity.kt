package joe.gallerydemo.activities

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import joe.gallerydemo.R
import kotlinx.android.synthetic.main.activity_for_test.*
import java.io.File


class ForTestActivity : AppCompatActivity() {


    private val videoUrl = "https://youtu.be/DZx91yQqpjY"

    private lateinit var file: File
    private var mPathStr:String = ""
    companion object{
        private const val url:String = "https://7cf6ea0074a52901e6ff5d075dd350d8.dd.cdntips.com/imtt.dd.qq.com/16891/apk/09A216A5641CF170A81A393FA891908E.apk?mkey=5e0ecaaa3d32a411&f=0c58&fsname=com.tencent.qqmusic_9.7.6.4_1214.apk&csr=1bbd&cip=61.50.130.228&proto=https"
        private const val mFileName:String = "qqMusic.apk"
        private const val installSchemeType = "application/vnd.android.package-archive"
    }

    private  lateinit var downloadManager :DownloadManager


    private var downloadId:Long = -1

    private var receiver = DownloadReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_test)
        downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        openexo.setOnClickListener { openExoPlayer() }
    }


    private fun openExoPlayer(){
        var intent = Intent(this,ExoPlayerActivity::class.java)
        intent.putExtra("uri",Uri.parse(videoUrl))
        startActivity(intent)
    }

    fun dmTest(view: View) {

        var request:DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedOverRoaming(true)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setTitle("DownloadFile")
        request.setDescription(" new file arrive soon ")
        if (Build.VERSION.SDK_INT<29){
            request.setVisibleInDownloadsUi(true)
        }
        file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), mFileName)
        mPathStr = file.absolutePath
        request.setDestinationUri(Uri.fromFile(file))
        if (downloadManager!=null){
            downloadManager.enqueue(request)
        }
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(receiver)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    private fun checkStatus() {
        val query = DownloadManager.Query()
        query.setFilterById(downloadId)
        val cursor: Cursor = downloadManager.query(query)
        if (cursor.moveToFirst()) {
            val status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            when (status) {
                DownloadManager.STATUS_PAUSED -> {
                }
                DownloadManager.STATUS_PENDING -> {
                }
                DownloadManager.STATUS_RUNNING -> {
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                    installAPK()
                    cursor.close()
                }
                DownloadManager.STATUS_FAILED -> {
                    Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show()
                    cursor.close()
                    unregisterReceiver(receiver)
                }
            }
        }
    }

    private fun installAPK() {
        val intent = Intent(Intent.ACTION_VIEW)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        //Android 7.0 need FileProvider
        if (Build.VERSION.SDK_INT >= 24) {
            val file = File(mPathStr)
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            val apkUri = FileProvider.getUriForFile(this, "com.lxtwsw.weather.fileprovider", file)
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, installSchemeType)
        } else {
            intent.setDataAndType(Uri.fromFile(file),installSchemeType)
        }
        startActivity(intent)
    }

    private class DownloadReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {

        }
    }

}

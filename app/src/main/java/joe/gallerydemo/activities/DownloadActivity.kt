package joe.gallerydemo.activities

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import joe.gallerydemo.R
import joe.gallerydemo.fragments.MeDialogFragment
import joe.gallerydemo.interfaces.OnDialogDownloadListener
import java.io.File

class DownloadActivity : AppCompatActivity() {


    companion object{

        const val APK_URL = "https://7cf6ea0074a52901e6ff5d075dd350d8.dd.cdntips.com/imtt.dd.qq.com/16891/apk/09A216A5641CF170A81A393FA891908E.apk?mkey=5e0ecaaa3d32a411&f=0c58&fsname=com.tencent.qqmusic_9.7.6.4_1214.apk&csr=1bbd&cip=61.50.130.228&proto=https"
        const val APK_FILENAME = "qqMusic.apk"
        const val AUTHORITY = "joe.gallerydemo.fileprovider"
        const val APK_INSTALL_TYPE = "application/vnd.android.package-archive"
    }


    private val file:File by lazy {
        File(application.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), APK_FILENAME)
    }

    private val dirPath:String by lazy {
        file.parent
    }

    private val dialogFragment:MeDialogFragment by lazy {
        MeDialogFragment.newInstance("",false).apply {
            setDialogClickListener(object:OnDialogDownloadListener{
                override fun onDownload() {
                    download()
                }

                override fun onForce() {
                    download()
                }

                override fun onLater() {

                }
            })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
    }

    private fun download(){
        LogUtils.i("curry","dirPath:${dirPath}")
        PRDownloader.download(APK_URL,dirPath, APK_FILENAME)
                .build()
                .setOnProgressListener {
//                    LogUtils.i("joe"," total : "+it.totalBytes+", current :"+it.currentBytes)
                    val ret = (it.currentBytes.toFloat()/it.totalBytes.toFloat()) * 100
                    LogUtils.i(" ret :${ret.toInt()}")
                    if (dialogFragment!=null){
                        dialogFragment.updateProgress(ret.toInt())
                    }
                }.start(object: OnDownloadListener{
                    override fun onDownloadComplete() {
                        dialogFragment.dismiss()
                        openApk()
                    }

                    override fun onError(error: Error?) {
                        dialogFragment.dismiss()
                        ToastUtils.showShort(error?.serverErrorMessage)
                    }
                })
    }

    fun down(view: View) {
        dialogFragment.show(supportFragmentManager,"tag")
    }

    fun openApk(){
        var intent = Intent(Intent.ACTION_VIEW).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                val uri = FileProvider.getUriForFile(this@DownloadActivity,AUTHORITY,file)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setDataAndType(uri, APK_INSTALL_TYPE)
            }else{
                setDataAndType(Uri.fromFile(file), APK_INSTALL_TYPE)
            }
        }
        startActivity(intent)


    }
}

package joe.gallerydemo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import joe.gallerydemo.activities.*
import joe.gallerydemo.fragments.MeDialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    companion object {
        const val KEY_ORIENT: String = "orient"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start_keep.setOnClickListener { startActivity(Intent(this, KeepLauncherActivity::class.java)) }

        start_video_list.setOnClickListener { startActivity(Intent(this, VideoActivity::class.java)) }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.gallery_menu -> checkPermission()

            R.id.picture_menu -> startVideoPlayList()

            R.id.video_view_list -> startVideoViewList()

            R.id.for_test -> goForTest()
            else ->
                print("nothing")
        }

        return super.onOptionsItemSelected(item)
    }

    private fun checkPermission() {

        if (PermissionUtils.isGranted(PermissionConstants.STORAGE)) {
            startGallery()
        } else {
            PermissionUtils.permission(PermissionConstants.STORAGE).callback(object : PermissionUtils.SimpleCallback {
                override fun onDenied() {
                    finish()
                }

                override fun onGranted() {
                    startGallery()
                }
            }).request()
        }
    }

    private fun startGallery() {
        val i = Intent(this, GalleryActivity::class.java)
        i.putExtra(KEY_ORIENT, orientation_vp.isChecked)

        startActivity(i)
    }


    fun verticalGallery(view: View) {
        startActivity(Intent(this, Gallery2Activity::class.java))
    }

    fun verticalVideos(view: View) {
        startActivity(Intent(this, PlayerActivity::class.java))
    }

    private fun startVideoPlayList() {
        startActivity(Intent(this, VideoPagerListActivity::class.java))
    }

    private fun startVideoViewList() {
        startActivity(Intent(this, VideoViewListActivity::class.java))
    }

    fun gridVideo(view:View ) {
        startActivity(Intent(this, VideoGridActivity::class.java))
    }

    fun goForTest() {
        startActivity(Intent(this, ForTestActivity::class.java))
    }

    fun showDialogFragment(view: View) {
        var dialogFragment = MeDialogFragment.newInstance("Where?",false)
        dialogFragment.show(supportFragmentManager,"tag")

    }

    fun toDown(view: View) {
        startActivity(Intent(this, DownloadActivity::class.java))

    }
}

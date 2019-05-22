package joe.gallerydemo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import kotlinx.android.synthetic.main.activity_main.*

//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start_keep.setOnClickListener { startActivity(Intent(this,KeepLauncherActivity::class.java)) }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.gallery_menu -> checkPermission()

            R.id.picture_menu -> startPicture()
            else ->
                    print("nothing")
        }

        return super.onOptionsItemSelected(item)
    }

    private fun checkPermission() {

        if (PermissionUtils.isGranted(PermissionConstants.STORAGE)) {
            startGallery()
        } else {
            PermissionUtils.permission(PermissionConstants.STORAGE).callback(object: PermissionUtils.SimpleCallback{
                override fun onDenied() {}
                override fun onGranted() {
                    startGallery()
                }
            }).request()
        }
    }

    private fun  startGallery(){
        startActivity(Intent(this,GalleryActivity::class.java))
    }

    private fun startPicture(){
//        startActivity<GalleryActivity>()
    }

}

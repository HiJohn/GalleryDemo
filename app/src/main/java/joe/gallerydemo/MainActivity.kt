package joe.gallerydemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import joe.gallerydemo.util.PermUtil

//import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        if (PermUtil.isSdDenied(this)){
            PermUtil.requestSD(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermUtil.isSdDenied(this)){
            finish()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.gallery_menu -> startGallery()

            R.id.picture_menu -> startPicture()
            else ->
                    print("nothing")
        }


        return super.onOptionsItemSelected(item)
    }

    private fun  startGallery(){
        startActivity(Intent(this,GalleryActivity::class.java))
    }

    private fun startPicture(){
//        startActivity<GalleryActivity>()
    }

}

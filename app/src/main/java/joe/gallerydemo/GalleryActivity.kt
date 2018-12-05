package joe.gallerydemo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import joe.gallerydemo.animator.ZoomOutPagerTransformer
import joe.gallerydemo.fragments.ImageFragment
import joe.gallerydemo.util.AsyncHandler
import joe.gallerydemo.util.PermUtil
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity() , ImageFragment.OnFragmentInteractionListener {


    private var galleryAdapter = GalleryAdapter(supportFragmentManager)


    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            // ……
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        actionBar?.hide()

//        gallery_pager = findViewById(R.id.gallery_pager)
        gallery_pager.adapter = galleryAdapter
        gallery_pager.addOnPageChangeListener(onPageChangeListener)
        gallery_pager.setPageTransformer(true, ZoomOutPagerTransformer())
        galleryAdapter.widRate = 12f

        checkPermission()
    }

    private fun checkPermission() {

        if(PermissionUtils.isGranted(PermissionConstants.STORAGE)){
            initData()
        }else{
//            PermissionUtils.permission(PermissionConstants.STORAGE).callback(PermissionUtils.FullCallback{
//
//            }).request()
        }

        if (PermUtil.isSdGranted(this)) {
            initData()
        } else {
            PermUtil.requestSDPermissions(this, PermUtil.SD_REQ)
        }
    }

    private fun initData(){
        AsyncHandler.post(Runnable {
                val uris = getImageUris(this)
                runOnUiThread {
                    galleryAdapter.setUris(uris)
                    galleryAdapter.notifyDataSetChanged()
                }

        })
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermUtil.isSdDenied(this)) {
            finish()
        }
    }

    companion object {
        fun getImageUris(context:Context):ArrayList<Uri>{

            val imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            var cursor = context.contentResolver.query(imgUri,null,null,null,null)
            var uris = ArrayList<Uri>()

            var mUri: Uri? = null
            if (cursor != null) {
                while (cursor.moveToNext()) {

                    val data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA))

                    val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
                    mUri = Uri.withAppendedPath(imgUri, "" + id)

                    uris.add(mUri)

                }
                cursor.close()
            }
            return uris
        }
    }


    override fun onFragmentInteraction(uri: Uri) {

    }

}

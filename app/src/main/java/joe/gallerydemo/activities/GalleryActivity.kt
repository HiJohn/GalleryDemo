package joe.gallerydemo.activities

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import joe.gallerydemo.R
import joe.gallerydemo.adapters.GalleryAdapter
import joe.gallerydemo.animator.ZoomOutPagerTransformer
import joe.gallerydemo.fragments.ImageFragment
import joe.gallerydemo.util.RxAsync
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity(), ImageFragment.OnFragmentInteractionListener {


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
        checkPermission()
    }

    private fun checkPermission() {

        if (PermissionUtils.isGranted(PermissionConstants.STORAGE)) {
            initData()
        } else {
            PermissionUtils.permission(PermissionConstants.STORAGE).callback(object:PermissionUtils.SimpleCallback{
                override fun onDenied() {
                    finish()
                }
                override fun onGranted() {
                    initData()
                }
            }).request()
        }
    }

    private fun initData() {

        RxAsync.async(object:RxAsync.RxCallBack<ArrayList<Uri>>{
            override fun call(): ArrayList<Uri> {
                return getImageUris(this@GalleryActivity)
            }

            override fun onResult(t: ArrayList<Uri>) {
                galleryAdapter.setUris(t)
                galleryAdapter.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {

            }
        })


//        AsyncHandler.post(Runnable {
//            val uris = getImageUris(this@GalleryActivity)
//            runOnUiThread {
//                galleryAdapter.setUris(uris)
//                galleryAdapter.notifyDataSetChanged()
//            }
//        })
    }


    companion object {
        fun getImageUris(context: Context): ArrayList<Uri> {

            val imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val cursor = context.contentResolver.query(imgUri, null, null, null, null)
            val uris = ArrayList<Uri>()

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

package joe.gallerydemo.activities

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.constant.PermissionConstants
import com.blankj.utilcode.util.PermissionUtils
import joe.gallerydemo.MainActivity
import joe.gallerydemo.R
import joe.gallerydemo.adapters.GalleryAdapter
import joe.gallerydemo.animator.ZoomOutPagerTransformer
import joe.gallerydemo.fragments.ImageFragment
import joe.gallerydemo.util.RxAsync
import joe.gallerydemo.viewmodels.GalleryViewModel
import joe.gallerydemo.viewmodels.LiveDataVMFactory
import kotlinx.android.synthetic.main.activity_gallery.*

class GalleryActivity : AppCompatActivity(){


    private var galleryAdapter = GalleryAdapter(this)

    private val mAnimator = ViewPager2.PageTransformer { page, position ->
        val absPos = Math.abs(position)
        page.apply {
            val scale = if (absPos > 1) 0F else 1 - absPos
            scaleX = scale
            scaleY = scale

        }
    }

    private  val galleryViewModel:GalleryViewModel by viewModels{ LiveDataVMFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        actionBar?.hide()

        val orient = intent.getBooleanExtra(MainActivity.KEY_ORIENT,true)

        gallery_pager.adapter = galleryAdapter
        gallery_pager.orientation = if (orient) ViewPager2.ORIENTATION_HORIZONTAL else ViewPager2.ORIENTATION_VERTICAL
        gallery_pager.setPageTransformer(mAnimator)

        initData()
    }


    private fun initData() {
        galleryViewModel.loadUris()
        galleryViewModel.uris.observe(this, Observer{
            galleryAdapter.mUris = it
            galleryAdapter.notifyDataSetChanged()
        })


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

}

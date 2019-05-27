package joe.gallerydemo.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import joe.gallerydemo.R
import joe.gallerydemo.adapters.GalleryAdapter2
import joe.gallerydemo.util.RxAsync
import kotlinx.android.synthetic.main.activity_vertical_gallery.*

class VerticalGalleryActivity : AppCompatActivity() {

    private var isRotate = true
    private var isTransX = false
    private var isTransY = false


    private val translateX get() = vg_view_pager.orientation == ViewPager2.ORIENTATION_VERTICAL &&
            isTransX
    private val translateY get() = vg_view_pager.orientation == ViewPager2.ORIENTATION_HORIZONTAL &&
            isTransY

    private val mAnimator = ViewPager2.PageTransformer { page, position ->
        val absPos = Math.abs(position)
        page.apply {
            rotation = if (isRotate) position * 360 else 0f
            translationY = if (translateX) absPos * 500f else 0f
            translationX = if (translateY) absPos * 350f else 0f
            if (isRotate) {
                val scale = if (absPos > 1) 0F else 1 - absPos
                scaleX = scale
                scaleY = scale
            } else {
                scaleX = 1f
                scaleY = 1f
            }
        }
    }

    private val mAnimator2 = ViewPager2.PageTransformer { page, position ->
        val absPos = Math.abs(position)
        page.apply {
            val scale = if (absPos > 1) 0F else 1 - absPos
            scaleX = scale
            scaleY = scale

        }
    }

    private lateinit var adapter2: GalleryAdapter2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_gallery)

        vg_view_pager.orientation = ViewPager2.ORIENTATION_VERTICAL
//        vg_view_pager.setPageTransformer(mAnimator2)
        adapter2 = GalleryAdapter2()
        vg_view_pager.adapter = adapter2

        initData()
    }

    private fun initData() {
        RxAsync.async(object: RxAsync.RxCallBack<ArrayList<Uri>>{
            override fun call(): ArrayList<Uri> {
                return GalleryActivity.getImageUris(this@VerticalGalleryActivity)
            }

            override fun onResult(t: ArrayList<Uri>) {
                adapter2.mUris = t
                adapter2.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {

            }
        })

    }
}

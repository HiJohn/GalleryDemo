package joe.gallerydemo.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import joe.gallerydemo.R
import joe.gallerydemo.adapters.GalleryAdapter2
import joe.gallerydemo.animator.OnViewPagerListener
import joe.gallerydemo.animator.ViewPagerLayoutManager
import joe.gallerydemo.util.RxAsync
import kotlinx.android.synthetic.main.activity_gallery2.*

class Gallery2Activity : AppCompatActivity() {


    private lateinit var adapter2: GalleryAdapter2
    private lateinit var layoutManager: ViewPagerLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery2)
        layoutManager = ViewPagerLayoutManager(this,RecyclerView.VERTICAL)
        verticalRv.layoutManager = layoutManager
        layoutManager.setOnViewPagerListener(object:OnViewPagerListener{
            override fun onPageRelease(itemView: View, isNext: Boolean, position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageSelected(itemView: View, position: Int, isBottom: Boolean) {
            }
        })
        adapter2 = GalleryAdapter2()
        verticalRv.adapter = adapter2
        initData()
    }
    private fun initData() {

        RxAsync.async(object: RxAsync.RxCallBack<ArrayList<Uri>>{
            override fun call(): ArrayList<Uri> {
                return GalleryActivity.getImageUris(this@Gallery2Activity)
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

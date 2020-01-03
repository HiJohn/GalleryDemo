package joe.gallerydemo.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import joe.gallerydemo.R
import joe.gallerydemo.adapters.GalleryAdapter2
import joe.gallerydemo.animator.OnViewPagerListener
import joe.gallerydemo.animator.ViewPagerLayoutManager
import joe.gallerydemo.viewmodels.GalleryViewModel
import joe.gallerydemo.viewmodels.LiveDataVMFactory
import kotlinx.android.synthetic.main.activity_gallery2.*

class Gallery2Activity : AppCompatActivity() {


    private lateinit var adapter2: GalleryAdapter2
    private lateinit var layoutManager: ViewPagerLayoutManager

    private  val galleryViewModel: GalleryViewModel by viewModels{ LiveDataVMFactory }

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

        galleryViewModel.mUris.observe(this, Observer {
            adapter2.mUris = it
            adapter2.notifyDataSetChanged()
        })

    }

}

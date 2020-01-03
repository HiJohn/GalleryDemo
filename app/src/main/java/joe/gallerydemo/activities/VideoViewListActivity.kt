package joe.gallerydemo.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import joe.gallerydemo.R
import joe.gallerydemo.adapters.VideoHolder3
import joe.gallerydemo.adapters.VideoPlayAdapter3
import joe.gallerydemo.animator.OnViewPagerListener
import joe.gallerydemo.animator.ViewPagerLayoutManager
import joe.gallerydemo.model.VideoInfo
import joe.gallerydemo.util.RxAsync
import joe.gallerydemo.util.VideoStoreUtil
import joe.gallerydemo.viewmodels.GalleryViewModel
import joe.gallerydemo.viewmodels.LiveDataVMFactory
import joe.gallerydemo.viewmodels.VideoListViewModel
import joe.gallerydemo.viewmodels.VideoVMFactory
import kotlinx.android.synthetic.main.activity_video_view_list.*

class VideoViewListActivity : AppCompatActivity() {


    private lateinit var adapter: VideoPlayAdapter3
    private lateinit var layoutManager: ViewPagerLayoutManager

    private var mLastPosition = 0
    private  val viewModel: VideoListViewModel by viewModels{ VideoVMFactory }


    private val pagerListener = object: OnViewPagerListener {
        override fun onPageRelease(itemView: View, isNext: Boolean, position: Int) {
            val videoHolder = videoviewlist.findViewHolderForLayoutPosition(position) as
                    VideoHolder3
            videoHolder.stopPlayback()

        }

        override fun onPageSelected(itemView: View, position: Int, isBottom: Boolean) {
            mLastPosition = position
            val videoHolder = videoviewlist.findViewHolderForLayoutPosition(position) as
                    VideoHolder3
            videoHolder.startPlay()
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }
    private fun initData(){

        viewModel.mVideoInfoList.observe(this, Observer {
            adapter.videos = it
            adapter.notifyDataSetChanged()
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setContentView(R.layout.activity_video_view_list)


        adapter = VideoPlayAdapter3()
        layoutManager = ViewPagerLayoutManager(this, LinearLayoutManager.VERTICAL)
        layoutManager.setOnViewPagerListener(pagerListener)
        videoviewlist.layoutManager = layoutManager
        videoviewlist.adapter = adapter
        initData()
    }

    override fun onResume() {
        super.onResume()
        val videoHolder = videoviewlist.findViewHolderForLayoutPosition(mLastPosition)
        if (videoHolder!=null&&videoHolder is VideoHolder3) {
            videoHolder.startPlay()
//            videoHolder.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        val videoHolder = videoviewlist.findViewHolderForLayoutPosition(mLastPosition)
        if (videoHolder!=null&&videoHolder is VideoHolder3) {
            videoHolder.pause()
//            videoHolder.resume()
        }
    }
}

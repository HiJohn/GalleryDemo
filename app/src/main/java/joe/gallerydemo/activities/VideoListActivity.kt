package joe.gallerydemo.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.LogUtils
import joe.gallerydemo.GalleryApp
import joe.gallerydemo.R
import joe.gallerydemo.adapters.VideoHolder
import joe.gallerydemo.adapters.VideoPlayAdapter2
import joe.gallerydemo.animator.OnViewPagerListener
import joe.gallerydemo.animator.ViewPagerLayoutManager
import joe.gallerydemo.model.VideoInfo
import joe.gallerydemo.util.ExoplayerPool
import joe.gallerydemo.util.RxAsync
import joe.gallerydemo.util.VideoStoreUtil
import kotlinx.android.synthetic.main.activity_video_list.*
private const val TAG = "VideoListActivity"
class VideoListActivity : AppCompatActivity() {


    private lateinit var adapter: VideoPlayAdapter2

    private lateinit var layoutManager: ViewPagerLayoutManager

    private var mLastPosition = 0

    private val pagerListener = object:OnViewPagerListener{
        override fun onPageRelease(itemView: View, isNext: Boolean, position: Int) {

            val videoHolder = videoListRv.findViewHolderForLayoutPosition(position) as VideoHolder
            videoHolder.detachPlayer()
//            videoHolder.pauseOrPlayVideo(false)

        }

        override fun onPageSelected(itemView: View, position: Int, isBottom: Boolean) {
            mLastPosition = position
            val videoHolder = videoListRv.findViewHolderForLayoutPosition(position) as VideoHolder
            videoHolder.preparePlay()
//            LogUtils.i(TAG, " onPageSelected :$position")

        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setContentView(R.layout.activity_video_list)
        adapter = VideoPlayAdapter2()
        layoutManager = ViewPagerLayoutManager(this,LinearLayoutManager.VERTICAL)
        layoutManager.setOnViewPagerListener(pagerListener)
        videoListRv.layoutManager = layoutManager
        videoListRv.adapter = adapter
        initData()
    }



    private fun initData(){
        RxAsync.async(object: RxAsync.RxCallBack<ArrayList<VideoInfo>>{
            override fun call(): ArrayList<VideoInfo> {
                return  VideoStoreUtil.getVideoInfoList(this@VideoListActivity)
            }

            override fun onResult(t: ArrayList<VideoInfo>) {
                adapter.videos = t
                adapter.notifyDataSetChanged()
            }

            override fun onError(e: Throwable) {}
        })
    }

    private fun stopCurrent(){
        val videoHolder = videoListRv.findViewHolderForLayoutPosition(mLastPosition) as VideoHolder
        videoHolder.detachPlayer()
    }

    override fun onStop() {
        super.onStop()
        stopCurrent()
    }


}

package joe.gallerydemo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import joe.gallerydemo.fragments.VideoPlayFragment
import joe.gallerydemo.model.VideoInfo

class VideoPlayAdapter (fa:FragmentActivity): FragmentStateAdapter(fa) {



    var videoList:ArrayList<VideoInfo> = ArrayList()

    override fun getItem(position: Int): Fragment {
        val videoInfo = videoList[position]
        return VideoPlayFragment.newInstance(videoInfo)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

}
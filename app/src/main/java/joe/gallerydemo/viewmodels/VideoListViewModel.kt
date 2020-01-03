package joe.gallerydemo.viewmodels

import androidx.lifecycle.*
import joe.gallerydemo.model.VideoInfo
import joe.gallerydemo.util.VideoStoreUtil

class VideoListViewModel :ViewModel(){

    var mVideoInfoList:LiveData<ArrayList<VideoInfo>> = liveData {
        emit(VideoStoreUtil.getVideoInfoList())
    }


}

/**
 * Factory for [LiveDataViewModel].
 */
object VideoVMFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return VideoListViewModel() as T
    }
}
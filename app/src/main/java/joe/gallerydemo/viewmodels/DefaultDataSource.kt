package joe.gallerydemo.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import joe.gallerydemo.util.VideoStoreUtil
import kotlinx.coroutines.CoroutineDispatcher

class DefaultDataSource (private var ioDispatcher:CoroutineDispatcher):DataSource{



    override suspend fun getImagesUri(): LiveData<ArrayList<Uri>> = liveData {
        emit(VideoStoreUtil.getImagesUri())
    }


}


interface DataSource{
//    val cacheData:LiveData<ArrayList<Uri>>

    suspend fun getImagesUri(): LiveData<ArrayList<Uri>>
}
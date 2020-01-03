package joe.gallerydemo.viewmodels

import android.net.Uri
import androidx.lifecycle.*
import joe.gallerydemo.util.VideoStoreUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel:ViewModel() {


    var mUris:LiveData<ArrayList<Uri>> = liveData {
        emit(VideoStoreUtil.getImagesUri())
    }

}


/**
 * Factory for [LiveDataViewModel].
 */
object LiveDataVMFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return GalleryViewModel() as T
    }
}
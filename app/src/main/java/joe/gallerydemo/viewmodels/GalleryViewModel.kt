package joe.gallerydemo.viewmodels

import android.net.Uri
import androidx.lifecycle.*
import joe.gallerydemo.util.VideoStoreUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GalleryViewModel:ViewModel() {

    var uris:MutableLiveData<ArrayList<Uri>> = MutableLiveData()

    fun  loadUris(){
        GlobalScope.launch {
            val ret =VideoStoreUtil.getImagesUri()
            setValueToUri(ret)
        }
    }


    private suspend fun setValueToUri(ret:ArrayList<Uri>) = withContext(Dispatchers.Main){
        uris.value = ret
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
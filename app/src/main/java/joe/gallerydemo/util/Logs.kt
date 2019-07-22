package joe.gallerydemo.util

import android.util.Log

object Logs {
    private const val TAG  = "Gallery"

    fun i(content:String){
        Log.i(TAG,content)
    }

    fun i(tag:String,content:String){
        Log.i(TAG.plus("=>[").plus(tag).plus("]"),content)
    }
}
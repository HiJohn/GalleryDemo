package joe.gallerydemo

import android.os.Handler
import android.os.HandlerThread

/**
 * Created by takashi on 2018/2/24.
 */
class AsyncHandler private constructor(){

    companion object {
        private val sHT = HandlerThread("AsyncHandler")
        private val sH:Handler
        init {
            sHT.start()
            sH = Handler(sHT.looper)
        }


        fun  post(task: Runnable){
            sH.post(task)
        }

        fun postDelayed(task:Runnable,delay:Long){
            sH.postDelayed(task,delay)
        }

        fun postAtTime(task:Runnable,time:Long){
            sH.postAtTime(task,time)
        }

    }




}
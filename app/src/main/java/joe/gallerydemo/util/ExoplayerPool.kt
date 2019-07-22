package joe.gallerydemo.util

import android.content.Context
import androidx.core.util.Pools
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player.REPEAT_MODE_ALL
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import joe.gallerydemo.GalleryApp

object ExoplayerPool {


    private val pool: Pools.SynchronizedPool<SimpleExoPlayer> = Pools.SynchronizedPool(3)

    fun obtain(context: Context): SimpleExoPlayer{
        var player = pool.acquire()
        if (player==null){
            player = newInstancePlayer(context)
        }
        return player
    }

    fun obtain():SimpleExoPlayer{
        var player = pool.acquire()
        if (player==null){
            Logs.i("barry"," new instance .. ")
            player = newInstancePlayer(GalleryApp.instance.applicationContext)
        }
        return player
    }

    private fun newInstancePlayer(context: Context): SimpleExoPlayer {
        val player = ExoPlayerFactory.newSimpleInstance(context)
        player.repeatMode = REPEAT_MODE_ALL
        player.audioAttributes = AudioAttributes.DEFAULT
        return player
    }

    fun release(player: SimpleExoPlayer) {
        //clear state if needed
        val result = pool.release(player)
        Logs.i("barry"," release result:$result")
    }


    fun releasePlayer(player: SimpleExoPlayer){
        player.release()
    }

    fun clear(){

        var player:SimpleExoPlayer?
        while (pool.acquire().also { player = it}!=null){
            player?.release()
            Logs.i("barry"," clear ")
        }


    }
}
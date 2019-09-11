package joe.gallerydemo.util

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ExecutorsFactory {

    private val sGlobalExecutorService: ExecutorService = Executors.newCachedThreadPool()

}
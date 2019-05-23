package joe.gallerydemo.util


import com.blankj.utilcode.util.LogUtils
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

/**
 * Created by JonSno on 2018/3/27.
 */

object RxAsync {


    fun launch(context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) =
            block.startCoroutine(Continuation(context) { result ->
                result.onFailure { exception ->
                    val currentThread = Thread.currentThread()
                    currentThread.uncaughtExceptionHandler.uncaughtException(currentThread, exception)
                }
                result.onSuccess {
                    value -> LogUtils.i("value:$value")
                }
            })


    fun <T> async(callBack: RxCallBack<T>) {
        Observable.create(ObservableOnSubscribe<T> { emitter -> emitter.onNext(callBack.call()) }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<T> {

            lateinit var disposable: Disposable
            override fun onSubscribe(d: Disposable) {
                disposable = d
            }

            override fun onNext(t: T) {
                if (disposable.isDisposed) {
                    return
                }
                callBack.onResult(t)
                onComplete()
            }

            override fun onError(e: Throwable) {
                callBack.onError(e)
                disposable.dispose()

            }

            override fun onComplete() {
                disposable.dispose()
            }
        })
    }

    fun <T> async(callBack: RxCallBack2<T>) {
        Observable.create(ObservableOnSubscribe<T> { emitter -> emitter.onNext(callBack.call()) }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<T> {

            lateinit var disposable: Disposable

            override fun onSubscribe(d: Disposable) {
                disposable = d
                callBack.onSubscribe()
            }

            override fun onNext(t: T) {
                if (disposable.isDisposed) {
                    return
                }
                callBack.onResult(t)
                onComplete()
            }

            override fun onError(e: Throwable) {
                callBack.onError(e)
                disposable.dispose()

            }

            override fun onComplete() {
                callBack.onComplete()
                disposable.dispose()
            }
        })
    }

    interface RxCallBack<T> {

        fun call(): T

        fun onResult(t: T)

        fun onError(e: Throwable)
    }

    interface RxCallBack2<T> : RxCallBack<T> {
        fun onSubscribe()
        fun onComplete()
    }

}

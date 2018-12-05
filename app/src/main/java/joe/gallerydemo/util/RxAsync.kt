package joe.gallerydemo.util


import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by JonSno on 2018/3/27.
 */

object RxAsync {

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

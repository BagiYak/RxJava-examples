package kz.bagiyak.rxjava_examples

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.util.Log
import android.widget.TextView
import io.reactivex.rxjava3.disposables.*
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableSubscriber
import org.reactivestreams.Subscription


class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"
    private val MY_MAX_VALUE = 1000000

    //ui
    private var text: TextView? = null

    // vars
    lateinit var subscription: Subscription

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        text = findViewById(R.id.text)

        Flowable
            .range(0, MY_MAX_VALUE)
            .onBackpressureBuffer()
            .observeOn(Schedulers.computation())
            .subscribe(object : FlowableSubscriber<Int> {

                override fun onSubscribe(s: Subscription) {
                    Log.d(TAG, "onSubscribe: Thread name:: -> " + Thread.currentThread().name)
                    Log.d(TAG, "onSubscribe: called.")
                    // Since Flowable supports backpressure you have to actually control
                    // how many items you can consume by calling request method on your Subscription
                    // so that they can be emitted by Flowables
                    subscription = s
                    subscription.request(MY_MAX_VALUE.toLong())
                }

                override fun onNext(it: Int) {
                    if (it.compareTo(0) == 0)
                        Log.d(TAG, "onNext: Thread name:: -> " + Thread.currentThread().name)

                    Log.d(TAG, "onNext: -> $it")
                }

                override fun onError(t: Throwable?) {
                    Log.d(TAG, "onError: Thread name:: -> " + Thread.currentThread().name)
                    Log.d(TAG, "onError: ", t)
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete: Thread name:: -> " + Thread.currentThread().name)
                    Log.d(TAG, "onComplete: called.")
                }

            })

    }

    // to see calling method disposables.clear() in onDestroy()
    // just turn your device screen from vertical to horizontal
    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy: Thread name:: -> " + Thread.currentThread().name)

        subscription.cancel()
        Log.d(TAG, "onDestroy: subscription.cancel() called")
    }
}
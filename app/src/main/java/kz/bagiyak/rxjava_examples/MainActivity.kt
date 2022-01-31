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

    //vars
    private val disposables = CompositeDisposable() // Instantiate a new CompositeDisposable object. Typically this will be a global variable inside your Activity or ViewModel.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        text = findViewById(R.id.text)

        val disposable = Flowable
            .range(0, MY_MAX_VALUE)
            .onBackpressureBuffer()
            .observeOn(Schedulers.computation())
            .subscribe {
                if (it.compareTo(0) == 0)
                    Log.d(TAG, "onNext: Thread name:: -> " + Thread.currentThread().name)
                Log.d(TAG, "onNext: -> $it")
            }
        disposables.add(disposable)
    }

    // to see calling method disposables.clear() in onDestroy()
    // just turn your device screen from vertical to horizontal
    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy: Thread name:: -> " + Thread.currentThread().name)
        // Then when the Observer is no longer needed, clear the disposables.
        // A good place to do this is in the onDestroy() method of an Activity or Fragment.
        // Or in the onCleared() method of a ViewModel.
        disposables.clear()
        Log.d(TAG, "onDestroy: disposables.clear() called")
    }
}
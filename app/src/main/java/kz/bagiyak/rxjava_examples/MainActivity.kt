package kz.bagiyak.rxjava_examples

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.util.Log
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.schedulers.Schedulers
import android.widget.TextView
import io.reactivex.rxjava3.disposables.CompositeDisposable


class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"

    //ui
    private var text: TextView? = null

    //vars
    private val disposables = CompositeDisposable() // Instantiate a new CompositeDisposable object. Typically this will be a global variable inside your Activity or ViewModel.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        text = findViewById(R.id.text)

        val taskObservable: Observable<Task> = Observable
            .fromIterable(createTasksList())
            .subscribeOn(Schedulers.io())
            .filter {
                Log.d(TAG, "test sleep: Thread name:: -> " + Thread.currentThread().name)
                try {
                    Thread.sleep(2000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                return@filter it.isComplete
            }
            .observeOn(AndroidSchedulers.mainThread())

        taskObservable.subscribe(object : Observer<Task> {

            override fun onSubscribe(d: Disposable?) {
                Log.d(TAG, "onSubscribe: Thread name:: -> " + Thread.currentThread().name)
                Log.d(TAG, "onSubscribe: called.")
                disposables.add(d)  // Add the disposable to the CompositeDisposable.
            }

            override fun onNext(t: Task) {
                Log.d(TAG, "onNext: Thread name:: -> " + Thread.currentThread().name)
                Log.d(TAG, "onNext: task description:: -> " + t.description)
            }

            override fun onError(e: Throwable?) {
                Log.d(TAG, "onError: Thread name:: -> " + Thread.currentThread().name)
                Log.d(TAG, "onError: ", e)
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
        // Then when the Observer is no longer needed, clear the disposables.
        // A good place to do this is in the onDestroy() method of an Activity or Fragment.
        // Or in the onCleared() method of a ViewModel.
        disposables.clear()
        Log.d(TAG, "onDestroy: disposables.clear() called")
    }
}
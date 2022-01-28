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


class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"

    //ui
    private var text: TextView? = null

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

            override fun onSubscribe(d: Disposable?) {}

            override fun onNext(t: Task) {
                Log.d(TAG, "onNext: Thread name:: -> " + Thread.currentThread().name)
                Log.d(TAG, "onNext: task description:: -> " + t.description)
            }

            override fun onError(e: Throwable?) {}

            override fun onComplete() {}

        })

    }

}
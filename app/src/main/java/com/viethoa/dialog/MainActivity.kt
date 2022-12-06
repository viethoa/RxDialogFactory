package com.viethoa.dialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.viethoa.dialog.factory.RxDialogFactory
import com.viethoa.dialog.factory.RxDialogFactoryImpl
import com.viethoa.dialog.factory.config.DialogType
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()
    private val dialogFactory: RxDialogFactory = RxDialogFactoryImpl(
        this, supportFragmentManager
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.txt_open_dialog)
            .apply {
                setOnClickListener {
                    showDialog()
                }
            }

        showDialog()
    }

    private fun showDialog() {
        disposable.add(
            dialogFactory.builder(DialogType.CENTER)
                .flatMap { dialogBuilder ->
                    dialogBuilder
                        .setTitle(R.string.dg_center_title)
                        .setMessage(R.string.dg_center_message)
                        .setPositiveButton(R.string.str_got_it)
                        .setNegativeButton(R.string.str_close)
                        .build()
                        .show()
                }
                .flatMapCompletable { dialog ->
                    Completable.mergeArray(
                        dialog
                            .observePositiveClick(dialog)
                            .observeOn(AndroidSchedulers.mainThread())
                            .switchMapCompletable {
                                Completable.fromAction {
                                    Toast
                                        .makeText(this, "Positive Button click", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            },
                        dialog
                            .observeNegativeClick(dialog)
                            .observeOn(AndroidSchedulers.mainThread())
                            .switchMapCompletable {
                                Completable.fromAction {
                                    Toast
                                        .makeText(this, "Negative Button click", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    )
                }
                .subscribe()
        )
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}
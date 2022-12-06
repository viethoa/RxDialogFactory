package com.viethoa.dialog.factory

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.viethoa.dialog.factory.config.DialogType
import com.viethoa.dialog.factory.dialog.RxDialogBuilder
import com.viethoa.dialog.factory.dialog.RxDialogBuilderImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

interface RxDialogFactory {
    fun builder(dialogType: DialogType = DialogType.CENTER): Single<RxDialogBuilder>
}

class RxDialogFactoryImpl(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread(),
) : RxDialogFactory {

    override fun builder(dialogType: DialogType): Single<RxDialogBuilder> {
        return Single.just(
            RxDialogBuilderImpl(dialogType, context, fragmentManager, uiScheduler)
        )
    }
}
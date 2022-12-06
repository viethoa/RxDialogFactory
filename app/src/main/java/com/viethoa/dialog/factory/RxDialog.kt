package com.viethoa.dialog.factory

import com.viethoa.dialog.factory.dialog.RxDialogBuilder
import com.viethoa.dialog.factory.handler.DialogClickHandler
import com.viethoa.dialog.factory.handler.DialogVisibilityHandler
import com.viethoa.dialog.factory.listener.DialogFactoryDelegation
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single

interface RxDialog : DialogClickHandler, DialogVisibilityHandler {
    fun show(): Single<RxDialog>
    fun dismiss(): Completable
}

internal class RxDialogImpl(
    private val dialogBuilder: RxDialogBuilder,
    private val clickHandler: DialogClickHandler,
    private val visibilityHandler: DialogVisibilityHandler,
    private val uiScheduler: Scheduler
) : RxDialog,
    DialogClickHandler by clickHandler,
    DialogVisibilityHandler by visibilityHandler {

    /**
     * This function will dismiss existing {@link BaseDialog} before show a new {@link BaseDialog}
     */
    override fun show(): Single<RxDialog> {
        return dismiss()
            .andThen(beforeShow())
            .andThen(doShow())
            .toSingleDefault(this)
    }

    override fun dismiss(): Completable {
        return Completable
            .fromAction {
                dialogBuilder.dismiss()
            }
            .subscribeOn(uiScheduler)
    }

    private fun doShow(): Completable {
        return Completable
            .fromAction {
                dialogBuilder.show()
            }
            .subscribeOn(uiScheduler)
    }

    private fun beforeShow(): Completable {
        return Observable.fromArray(clickHandler, visibilityHandler)
            .ofType(DialogFactoryDelegation::class.java)
            .flatMapCompletable { it.beforeDialogShow(dialogBuilder.getDialog()) }
    }
}
package com.viethoa.dialog.factory.handler

import com.viethoa.dialog.factory.dialog.BaseDialog
import com.viethoa.dialog.factory.listener.DialogFactoryDelegation
import com.viethoa.dialog.factory.listener.DialogVisibilityListener
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Used when we need to perform any operation(s) on dialog state change
 * usage:
 *    dialogFactory.builder()
 *           .flatMap{ builder ->
 *              builder.setTitle(R.string.title)
 *                  .setMessage(R.string.message)
 *                  .setPositiveButton(R.string.ok)
 *                  .build()
 *                  .show()
 *           }. flatMapCompletable { dialog ->
 *              dialog.observeDismiss
 *                  .doOnComplete{
 *                      // Do something here
 *                  }
 *           }
 */
interface DialogVisibilityHandler {
    /**
     * observe onCancel event of {@link BaseDialogFactory}
     *
     * @return always return {@link Completable#complete()}
     */
    fun observeCancel(): Completable

    /**
     * observe onDismiss event of {@link BaseDialogFactory}
     *
     * @return always return {@link Completable#complete()}
     */
    fun observeDismiss(): Completable

    /**
     * observe onShow event of {@link BaseDialogFactory}
     *
     * @return always return {@link Completable#complete()}
     */
    fun observeShow(): Completable
}

internal class DialogVisibilityHandlerImpl : DialogVisibilityListener,
    DialogVisibilityHandler,
    DialogFactoryDelegation {

    private val cancelSubject = PublishSubject.create<Boolean>()

    private val dismissSubject = PublishSubject.create<Boolean>()

    private val showSubject = PublishSubject.create<Boolean>()

    override fun observeCancel(): Completable = cancelSubject.ignoreElements()

    override fun observeDismiss(): Completable = dismissSubject.ignoreElements()

    override fun observeShow(): Completable = showSubject.ignoreElements()

    override fun beforeDialogShow(baseDialog: BaseDialog?): Completable =
        Completable.fromAction {
            baseDialog?.setListener(this)
        }

    override fun onShow() {
        showSubject.onComplete()
    }

    override fun onCancel() {
        cancelSubject.onComplete()
    }

    override fun onDismiss() {
        dismissSubject.onComplete()
    }
}
package com.viethoa.dialog.factory.handler

import com.viethoa.dialog.factory.listener.DialogFactoryDelegation
import com.viethoa.dialog.factory.RxDialog
import com.viethoa.dialog.factory.dialog.BaseDialog
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * Used when we need to perform any operation(s) when button clicked
 * usage:
 *  dialogFactory.builder()
 *           .flatMap{ builder ->
 *              builder.setTitle(R.string.title)
 *                  .setMessage(R.string.message)
 *                  .setPositiveButton(R.string.ok)
 *                  .build()
 *                  .show()
 *           }. flatMapCompletable { dialog ->
 *              dialog.observePositiveClick(dialog)
 *                  .doOnNext{
 *                      // Do something here
 *                  }
 *           }
 */
interface DialogClickHandler {
    /**
     * Track the click event of Positive Button
     * @return always true {@link #onPositiveClickAction()}
     */
    fun observePositiveClick(dialog: RxDialog): Observable<Boolean>

    /**
     * Track the click event of Negative Button
     * @return always true {@link #onNegativeClickAction()}
     */
    fun observeNegativeClick(dialog: RxDialog): Observable<Boolean>

    /**
     * Track the click event of Close Button (on the top left of fullscreen dialog)
     * @return always true {@link #onCloseClickAction()}
     */
    fun observeCloseClick(dialog: RxDialog): Observable<Boolean>

    /**
     * Track the click event of outside dialog
     * @return always true {@link #observeOutsideClick()}
     */
    fun observeOutsideClick(dialog: RxDialog): Observable<Boolean>
}

open class DialogClickHandlerImpl(
    private val dismissAfterClick: Boolean = true
) : DialogClickHandler,
    DialogFactoryDelegation {

    private val positiveSubject = PublishSubject.create<Boolean>()
    private val negativeSubject = PublishSubject.create<Boolean>()
    private val closeSubject = PublishSubject.create<Boolean>()
    private val outsideSubject = PublishSubject.create<Boolean>()

    private fun onClick(dialog: RxDialog): Single<Boolean> =
        if (dismissAfterClick) {
            dialog
                .dismiss()
                .toSingleDefault(true)
        } else {
            Single.just(true)
        }

    override fun observePositiveClick(dialog: RxDialog): Observable<Boolean> =
        positiveSubject.switchMapSingle {
            onClick(dialog)
        }

    override fun observeNegativeClick(dialog: RxDialog): Observable<Boolean> =
        negativeSubject.switchMapSingle {
            onClick(dialog)
        }

    override fun observeCloseClick(dialog: RxDialog): Observable<Boolean> =
        closeSubject.switchMapSingle {
            onClick(dialog)
        }

    override fun observeOutsideClick(dialog: RxDialog): Observable<Boolean> =
        outsideSubject.switchMapSingle {
            onClick(dialog)
        }

    override fun beforeDialogShow(baseDialog: BaseDialog?): Completable =
        Completable.fromAction {
            baseDialog?.setPositiveClickListener {
                positiveSubject.onNext(true)
            }
            baseDialog?.setNegativeClickListener {
                negativeSubject.onNext(true)
            }
            baseDialog?.setCloseClickListener {
                closeSubject.onNext(true)
            }
            baseDialog?.setOutsideClickListener {
                outsideSubject.onNext(true)
            }
        }
}
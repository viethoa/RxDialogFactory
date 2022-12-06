package com.viethoa.dialog.factory.listener

import com.viethoa.dialog.factory.dialog.BaseDialog
import io.reactivex.rxjava3.core.Completable

/**
 * The delegation of {@link BaseDialog} to its handler
 * - {@link DialogClickHandler}
 * - {@link DialogVisibilityHandler}
 * to manage the visibility
 */
internal interface DialogFactoryDelegation {
    fun beforeDialogShow(baseDialog: BaseDialog?): Completable
}
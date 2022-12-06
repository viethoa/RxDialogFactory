package com.viethoa.dialog.factory.dialog

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.UiThread
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentManager
import com.viethoa.dialog.factory.RxDialog
import com.viethoa.dialog.factory.RxDialogImpl
import com.viethoa.dialog.factory.config.DialogType
import com.viethoa.dialog.factory.handler.DialogClickHandlerImpl
import com.viethoa.dialog.factory.handler.DialogVisibilityHandlerImpl
import io.reactivex.rxjava3.core.Scheduler

interface RxDialogBuilder {
    fun setTitle(@StringRes stringID: Int): RxDialogBuilder
    fun setMessage(@StringRes stringID: Int): RxDialogBuilder
    fun setImage(@DrawableRes drawableRes: Int): RxDialogBuilder
    fun setCancelable(cancelable: Boolean): RxDialogBuilder
    fun setPositiveButton(@StringRes stringID: Int): RxDialogBuilder
    fun setNegativeButton(@StringRes stringID: Int): RxDialogBuilder
    fun setHaveCloseButton(haveCloseButton: Boolean): RxDialogBuilder
    fun setDismissAfterClick(enable: Boolean): RxDialogBuilder
    fun setCenterTitle(centerTitle: Boolean): RxDialogBuilder
    fun setCenterMessage(centerMessage: Boolean): RxDialogBuilder

    fun getDialog(): BaseDialog?
    fun build(): RxDialog

    @UiThread
    fun show()

    @UiThread
    fun dismiss()
}

class RxDialogBuilderImpl constructor(
    type: DialogType,
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val uiScheduler: Scheduler
) : RxDialogBuilder {

    private var baseDialog: BaseDialog? = BaseDialog.createInstance(type)
    private var dismissAfterClick: Boolean = true

    override fun setTitle(stringID: Int): RxDialogBuilder {
        val title = context.getString(stringID)
        baseDialog?.setTitle(title)
        return this
    }

    override fun setMessage(stringID: Int): RxDialogBuilder {
        val message = context.getString(stringID)
        baseDialog?.setMessage(message)
        return this
    }

    override fun setImage(drawableRes: Int): RxDialogBuilder {
        val dialogImage = AppCompatResources.getDrawable(context, drawableRes)
        baseDialog?.setImage(dialogImage)
        return this
    }

    override fun setCancelable(cancelable: Boolean): RxDialogBuilder {
        baseDialog?.setDialogCancelable(cancelable)
        return this
    }

    override fun setPositiveButton(stringID: Int): RxDialogBuilder {
        val positiveButtonLabel = context.getString(stringID)
        baseDialog?.setPositiveButton(positiveButtonLabel)
        return this
    }

    override fun setNegativeButton(stringID: Int): RxDialogBuilder {
        val negativeButtonLabel = context.getString(stringID)
        baseDialog?.setNegativeButton(negativeButtonLabel)
        return this
    }

    override fun setHaveCloseButton(haveCloseButton: Boolean): RxDialogBuilder {
        baseDialog?.setHaveCloseButton(haveCloseButton)
        return this
    }

    override fun setCenterTitle(centerTitle: Boolean): RxDialogBuilder {
        baseDialog?.setCenterTitle(centerTitle)
        return this
    }

    override fun setCenterMessage(centerMessage: Boolean): RxDialogBuilder {
        baseDialog?.setCenterMessage(centerMessage)
        return this
    }

    override fun setDismissAfterClick(enable: Boolean): RxDialogBuilder {
        this.dismissAfterClick = enable
        return this
    }

    override fun getDialog(): BaseDialog? = baseDialog

    override fun build(): RxDialog {
        return RxDialogImpl(
            this,
            DialogClickHandlerImpl(dismissAfterClick),
            DialogVisibilityHandlerImpl(),
            uiScheduler
        )
    }

    @UiThread
    override fun show() {
        if (fragmentManager.isStateSaved || fragmentManager.isDestroyed) {
            return
        }
        baseDialog?.show(fragmentManager, BaseDialog.TAG)
    }

    @UiThread
    override fun dismiss() {
        try { // try/catch to prevent the crash
            (fragmentManager.findFragmentByTag(BaseDialog.TAG) as? BaseDialog)?.dismiss()
        } catch (e: Exception) {
            // Todo print debug error
        }
    }
}
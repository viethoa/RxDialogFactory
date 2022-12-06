package com.viethoa.dialog.factory.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.viethoa.dialog.R
import com.viethoa.dialog.extension.dimBehind
import com.viethoa.dialog.extension.hideStatusBar
import com.viethoa.dialog.factory.config.DialogType
import com.viethoa.dialog.factory.listener.DialogVisibilityListener

private const val KEY_DIALOG_TYPE = "VH_DIALOG_TYPE"

class BaseDialog : DialogFragment() {

    companion object {
        const val TAG = "VH_BaseDialog"
        fun createInstance(dialogType: DialogType): BaseDialog {
            return BaseDialog().apply {
                arguments = Bundle().apply {
                    putString(KEY_DIALOG_TYPE, dialogType.name)
                }
            }
        }
    }

    private var title: String? = null
    private var message: String? = null
    private var dialogImage: Drawable? = null
    private var positiveButtonLabel: String? = null
    private var negativeButtonLabel: String? = null
    private var haveCloseButton: Boolean = true
    private var dialogCancelable: Boolean = true
    private var centerTitle: Boolean = false
    private var centerMessage: Boolean = false

    private var onPositiveClickListener: View.OnClickListener? = null
    private var onNegativeClickListener: View.OnClickListener? = null
    private var onCloseClickListener: View.OnClickListener? = null
    private var onOutsideClickListener: View.OnClickListener? = null
    private var visibilityListener: DialogVisibilityListener? = null

    override fun onStart() {
        super.onStart()
        view?.parent?.run {
            if (this is View) {
                this.setBackgroundResource(0)
            }
        }
    }

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        visibilityListener?.onDismiss()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        visibilityListener?.onCancel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutID = when (getDialogType()) {
            DialogType.BOTTOM_SHEET -> R.layout.layout_dialog_bottom_sheet
            DialogType.FULL_SCREEN -> R.layout.layout_dialog_full_screen
            else -> R.layout.layout_dialog_center
        }
        return inflater.inflate(layoutID, container, false)
    }

    private fun getDialogType(): DialogType {
        arguments?.getString(KEY_DIALOG_TYPE)?.let {
            return DialogType.valueOf(it)
        }
        // Default is center
        return DialogType.CENTER
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(view)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.let {
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            if (getDialogType() != DialogType.FULL_SCREEN) {
                dialog.window?.dimBehind()
                dialog.window?.hideStatusBar()
            }
        }
        dialog.apply {
            setOnShowListener { visibilityListener?.onShow() }
            setCancelable(dialogCancelable)
            setCanceledOnTouchOutside(dialogCancelable)
        }
        return dialog
    }

    private fun setupViews(view: View) {
        setupTouchOutside(view.findViewById(R.id.gm_alert_dialog_layout))
        setupTitleView(view.findViewById(R.id.gm_alert_dialog_title))
        setupMessageView(view.findViewById(R.id.gm_alert_dialog_text))
        setupImageView(view.findViewById(R.id.gm_alert_dialog_img))
        setupPositiveButton(view.findViewById(R.id.gm_alert_dialog_positive_btn))
        setupNegativeButton(view.findViewById(R.id.gm_alert_dialog_negative_btn))
        setupCloseButton(view.findViewById(R.id.gm_alert_dialog_close_btn))
    }

    private fun setupTitleView(view: TextView?) {
        view?.text = title
        view?.visibility = if (TextUtils.isEmpty(title)) View.GONE else View.VISIBLE
        view?.gravity = if (centerTitle) Gravity.CENTER else Gravity.START
    }

    private fun setupMessageView(view: TextView?) {
        view?.text = message
        view?.visibility = if (TextUtils.isEmpty(message)) View.GONE else View.VISIBLE
        view?.gravity = if (centerMessage) Gravity.CENTER else Gravity.START
    }

    private fun setupImageView(view: ImageView?) {
        view?.setImageDrawable(dialogImage)
        view?.visibility = if (dialogImage == null) View.GONE else View.VISIBLE
    }

    private fun setupPositiveButton(view: Button?) {
        view?.text = positiveButtonLabel
        view?.visibility = if (TextUtils.isEmpty(positiveButtonLabel)) View.GONE else View.VISIBLE
        view?.setOnClickListener(onPositiveClickListener)
    }

    private fun setupNegativeButton(view: Button?) {
        view?.text = negativeButtonLabel
        view?.visibility = if (TextUtils.isEmpty(negativeButtonLabel)) View.GONE else View.VISIBLE
        view?.setOnClickListener(onNegativeClickListener)
    }

    private fun setupCloseButton(view: CardView?) {
        view?.visibility = if (haveCloseButton) View.VISIBLE else View.GONE
        view?.setOnClickListener(onCloseClickListener)
    }

    private fun setupTouchOutside(view: View?) {
        view?.setOnClickListener(onOutsideClickListener)
    }

    fun setCenterTitle(centerTitle: Boolean) {
        this.centerTitle = centerTitle
    }

    fun setCenterMessage(centerMessage: Boolean) {
        this.centerMessage = centerMessage
    }

    fun setHaveCloseButton(haveCloseButton: Boolean) {
        this.haveCloseButton = haveCloseButton
    }

    fun setTitle(title: String?) {
        this.title = title
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun setImage(drawable: Drawable?) {
        this.dialogImage = drawable
    }

    fun setPositiveButton(label: String?) {
        this.positiveButtonLabel = label
    }

    fun setDialogCancelable(dialogCancelable: Boolean) {
        this.dialogCancelable = dialogCancelable
    }

    fun setNegativeButton(label: String?) {
        this.negativeButtonLabel = label
    }

    fun setPositiveClickListener(onClickListener: View.OnClickListener?) {
        this.onPositiveClickListener = onClickListener
    }

    fun setNegativeClickListener(onClickListener: View.OnClickListener?) {
        this.onNegativeClickListener = onClickListener
    }

    fun setCloseClickListener(onClickListener: View.OnClickListener?) {
        this.onCloseClickListener = onClickListener
    }

    fun setOutsideClickListener(onClickListener: View.OnClickListener?) {
        this.onOutsideClickListener = onClickListener
    }

    fun setListener(listener: DialogVisibilityListener) {
        this.visibilityListener = listener
    }
}
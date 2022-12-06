package com.viethoa.dialog.factory.listener

interface DialogVisibilityListener {
    /**
     *  On {@link BaseDialog} showed
     */
    fun onShow()

    /**
     *  On {@link BaseDialog} cancelled
     */
    fun onCancel()

    /**
     *  On {@link BaseDialog} dismissed
     */
    fun onDismiss()
}
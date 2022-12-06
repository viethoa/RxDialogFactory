package com.viethoa.dialog.utils

import android.os.Build

inline fun isEqualOrMoreThanLollipop() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP // API Level 21

inline fun isEqualOrMoreThanMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M // API Level 23

inline fun isEqualOrMoreThanNougat() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N // API Level 24

inline fun isMoreThanNougatMr1() = Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 // API Level 25

inline fun isEqualOrMoreThanOreo() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O // API Level 26

inline fun isEqualOrMoreThanPie() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P // API Level 28

inline fun isEqualOrMoreThanQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q // API Level 29
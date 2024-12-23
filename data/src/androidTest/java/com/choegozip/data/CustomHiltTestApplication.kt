package com.choegozip.data

import android.app.Application
import dagger.hilt.android.testing.CustomTestApplication

open class CustomHiltTestApplication: Application()

@CustomTestApplication(CustomHiltTestApplication::class)
interface MyCustom
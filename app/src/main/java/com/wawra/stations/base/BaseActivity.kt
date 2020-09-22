package com.wawra.stations.base

import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity : DaggerAppCompatActivity() {

    protected fun Disposable.addToDisposables() {
        disposables.add(this)
    }

    private val disposables = CompositeDisposable()

}
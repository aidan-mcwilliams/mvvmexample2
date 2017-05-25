package com.exallium.mvvmcleanexample.presentation.utils.notifiable

import android.databinding.BaseObservable
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry

/**
 * A convenience class that implements the [NotifiableObservable] interface.
 *
 * It is ultimately an alternative implementation of [BaseObservable],
 * where the [notifyChange] and [notifyPropertyChanged] methods are exposed through the [NotifiableObservable] interface.
 *
 * Unlike [BaseObservable], this class is final, so delegation must be used.
 *
 * See [NotifiableObservable] for usage details.
 */
class BaseNotifiableObservable : NotifiableObservable {

    companion object {
        private const val ALL_PROPERTIES = 0
    }

    private val changeRegistryProperty = lazy { PropertyChangeRegistry() }
    private val changeRegistry: PropertyChangeRegistry by changeRegistryProperty
    private lateinit var delegator: NotifiableObservable

    @Synchronized
    override fun setDelegator(delegator: NotifiableObservable) {
        this.delegator = delegator
    }

    @Synchronized
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        changeRegistry.add(callback)
    }

    @Synchronized
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
        changeRegistry.remove(callback)
    }

    @Synchronized
    override fun notifyChange() {
        if (changeRegistryProperty.isInitialized()) {
            changeRegistry.notifyCallbacks(delegator, ALL_PROPERTIES, null)
        }
    }

    @Synchronized
    override fun notifyPropertyChanged(propertyId: Int) {
        if (changeRegistryProperty.isInitialized()) {
            changeRegistry.notifyCallbacks(delegator, propertyId, null)
        }
    }
}

package com.shin.recipeapp.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.ViewModelProviders
import com.shin.recipeapp.util.plusAssign
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.reflect.KClass

abstract class BaseBindingActivity<TBinding : ViewDataBinding, TViewModel : BaseViewModel> :
    BaseActivity() {

    @Inject
    lateinit var factory: ViewModelsFactory
    lateinit var viewModel: TViewModel
    lateinit var binding: TBinding

    protected abstract val layoutId: Int
    protected abstract val viewModelClass: KClass<TViewModel>
    protected var compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory)[viewModelClass.java]
        init(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.setVariable(BR.vm, viewModel)
        binding.lifecycleOwner = this

        initView(savedInstanceState)
    }

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun initView(savedInstanceState: Bundle?)

    protected inline infix fun <T> Observable<T>.bindTo(crossinline action: (T) -> Unit?) {
        compositeDisposable += observeOn(AndroidSchedulers.mainThread()).subscribe { action(it) }
    }

    protected inline infix fun <T> Observable<T>.bindToTheme(crossinline action: (T) -> Unit?) {
        compositeDisposable += subscribe { action(it) }
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
        super.onDestroy()
    }
}
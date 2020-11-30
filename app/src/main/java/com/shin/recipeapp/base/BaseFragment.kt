package com.shin.recipeapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.reflect.KClass
import androidx.databinding.library.baseAdapters.BR

abstract class BaseFragment<TBinding : ViewDataBinding, TViewModel : ViewModel> : Fragment(),
    HasSupportFragmentInjector {

    @Inject
    lateinit var factory: ViewModelsFactory

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    protected lateinit var viewModel: TViewModel
    protected lateinit var binding: TBinding
    protected val compositeDisposable = CompositeDisposable()

    protected abstract val layoutId: Int
    protected abstract val viewModelClass: KClass<TViewModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!, factory)[viewModelClass.java]

        init(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
//        binding.setVariable(BR.model, viewModel)
        binding.lifecycleOwner = this
        initView(savedInstanceState)

        return binding.root
    }

    abstract fun init(savedInstanceState: Bundle?)

    abstract fun initView(savedInstanceState: Bundle?)

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
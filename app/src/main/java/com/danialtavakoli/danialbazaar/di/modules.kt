package com.danialtavakoli.danialbazaar.di

import android.content.Context
import androidx.room.Room
import com.danialtavakoli.danialbazaar.model.db.AppDatabase
import com.danialtavakoli.danialbazaar.model.net.createApiService
import com.danialtavakoli.danialbazaar.model.repository.cart.CartRepository
import com.danialtavakoli.danialbazaar.model.repository.cart.CartRepositoryImpl
import com.danialtavakoli.danialbazaar.model.repository.comment.CommentRepository
import com.danialtavakoli.danialbazaar.model.repository.comment.CommentRepositoryImpl
import com.danialtavakoli.danialbazaar.model.repository.product.ProductRepository
import com.danialtavakoli.danialbazaar.model.repository.product.ProductRepositoryImpl
import com.danialtavakoli.danialbazaar.model.repository.user.UserRepository
import com.danialtavakoli.danialbazaar.model.repository.user.UserRepositoryImpl
import com.danialtavakoli.danialbazaar.ui.features.cart.CartViewModel
import com.danialtavakoli.danialbazaar.ui.features.category.CategoryViewModel
import com.danialtavakoli.danialbazaar.ui.features.main.MainViewModel
import com.danialtavakoli.danialbazaar.ui.features.product.ProductViewModel
import com.danialtavakoli.danialbazaar.ui.features.profile.ProfileViewModel
import com.danialtavakoli.danialbazaar.ui.features.signIn.SignInViewModel
import com.danialtavakoli.danialbazaar.ui.features.signUp.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {
    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single { createApiService() }
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_dataBase.db").build()
    }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }
    single<CartRepository> { CartRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get<AppDatabase>().productDao()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProductViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { CartViewModel(get(), get()) }
    viewModel { (isInternetConnected: Boolean) -> MainViewModel(get(), get(), isInternetConnected) }
}
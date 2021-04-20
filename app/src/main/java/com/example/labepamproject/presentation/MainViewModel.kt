package com.example.labepamproject.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.labepamproject.R
import com.example.labepamproject.data.NetworkPokemonRepository
import com.example.labepamproject.data.network.createPokedexApiService
import com.example.labepamproject.domain.Generation
import com.example.labepamproject.domain.Pokemon
import com.example.labepamproject.domain.PokemonRepository
import com.example.labepamproject.presentation.adapter.Item
import com.example.labepamproject.presentation.adapter.Item.GenerationItem.Companion.asItem
import com.example.labepamproject.presentation.adapter.Item.PokemonItem.Companion.asItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class MainViewModel(
    private val repository: PokemonRepository = NetworkPokemonRepository(
        createPokedexApiService
    )
) :
    ViewModel() {

    private lateinit var disposable: Disposable
    private val _state = MutableLiveData<MainViewState>()
    fun getState(): LiveData<MainViewState> = _state

    init {
        loadItems()
    }

    private fun loadItems() {
        _state.value = MainViewState.LoadingState(R.drawable.loading_animation)
        disposable = repository.getPokemons()
            .zipWith(repository.getGenerations(), { pokemons, generations ->
                listOf(Item.GenerationListItem(generations.map { it.asItem() })) + pokemons.map { it.asItem() }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { items ->
                    Timber.i("Values provided")
                    _state.postValue(MainViewState.ResultState(items))
                    Timber.i("Items loading is successful")
                },
                {
                    _state.value = MainViewState.ErrorState(R.drawable.ic_connection_error)
                }
            )
    }


    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }
}
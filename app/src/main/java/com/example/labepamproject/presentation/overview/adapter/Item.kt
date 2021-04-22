package com.example.labepamproject.presentation.overview.adapter

import com.example.labepamproject.domain.Generation
import com.example.labepamproject.domain.Pokemon

sealed class Item {
    data class PokemonItem(val name: String, val imgSrc: String) : Item() {
        companion object {
            fun Pokemon.asItem(): PokemonItem = PokemonItem(name, prevImgUrl)
        }
    }

    data class HeaderItem(val text: String) : Item()

    data class GenerationItem(val text: String) : Item() {
        companion object {
            fun Generation.asItem(): GenerationItem = GenerationItem(name)
        }
    }

    data class GenerationListItem(val generationList: List<GenerationItem>) : Item()
}
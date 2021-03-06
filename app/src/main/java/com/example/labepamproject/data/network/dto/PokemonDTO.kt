package com.example.labepamproject.data.network.dto

import com.example.labepamproject.data.database.PokemonDetailDatabaseEntity
import com.example.labepamproject.domain.entity.PokemonEntity
import com.example.labepamproject.presentation.generateUrlFromId
import com.squareup.moshi.Json

data class PokemonListResponse(
    val count: Int,
    val results: List<PokemonPartialResponse>,
)

data class PokemonPartialResponse(
    val name: String,
    val url: String,
)

data class PokemonDetailedResponse(
    val id: Int,
    val name: String,
    @field:Json(name = "base_experience") val experience: Float,
    val height: Int,
    val weight: Int,
    val abilities: List<PokemonAbilityData>,
    val stats: List<PokemonStatsData>,
    val types: List<PokemonTypesData>,
)

fun PokemonDetailedResponse.asEntity() =
    PokemonEntity(
        id = id,
        name = name,
        prevImgUrl = generateUrlFromId(id),
        experience = experience,
        height = height,
        weight = weight,
        abilities = abilities.map { it.ability.name },
        stats = stats.map { it.stat.name to it.base_stat },
        types = types.map { it.type.name }
    )

fun PokemonDetailedResponse.asDatabaseEntity() =
    PokemonDetailDatabaseEntity(
        id = id,
        name = name,
        experience = experience,
        height = height,
        weight = weight,
        abilities = abilities.map { it.ability.name },
        stats = stats.map { it.stat.name to it.base_stat },
        types = types.map { it.type.name }
    )

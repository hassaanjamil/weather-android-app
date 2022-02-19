package com.weather.app.data.remote.model.cities

data class ResponseCities(
    val `data`: List<Data>? = null,
    val links: List<Link>? = null,
    val metadata: Metadata? = null,
)
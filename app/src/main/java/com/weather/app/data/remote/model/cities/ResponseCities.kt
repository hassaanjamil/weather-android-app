package com.weather.app.data.remote.model.cities

data class ResponseCities(
    val `data`: List<Data>,
    val links: List<Link>,
    val metadata: Metadata,
)
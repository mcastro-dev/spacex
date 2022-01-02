package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v5query.response

import com.google.gson.annotations.SerializedName

data class SpacexLinks(
    @SerializedName("patch")
    val patchImage: SpacexPatchImage?,
    @SerializedName("article")
    val articleLink: String?,
    @SerializedName("wikipedia")
    val wikipediaLink: String?,
    @SerializedName("webcast")
    val videoLink: String?,
)

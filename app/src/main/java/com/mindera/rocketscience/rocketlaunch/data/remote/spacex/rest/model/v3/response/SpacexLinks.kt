package com.mindera.rocketscience.rocketlaunch.data.remote.spacex.rest.model.v3.response

import com.google.gson.annotations.SerializedName

data class SpacexLinks(
    @SerializedName("mission_patch_small")
    val patchImageLink: String?,
    @SerializedName("article_link")
    val articleLink: String?,
    @SerializedName("wikipedia")
    val wikipediaLink: String?,
    @SerializedName("video_link")
    val videoLink: String?,
)
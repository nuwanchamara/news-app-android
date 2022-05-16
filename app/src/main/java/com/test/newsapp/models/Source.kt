package com.test.newsapp.models

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
class Source(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("name")
    var name: String = ""
)
package com.mindera.rocketscience.common.presentation.imageloading

import android.widget.ImageView
import com.squareup.picasso.Picasso

// This could be an interface, and we could create a class implementing this interface using the Picasso library.
// That way it would be more flexible, but it's more likely that we would just replace Picasso by another library if we had to,
//  and there's nothing fancy we have to do (for now at least).
// Therefore, having this class here seems to be enough :)
object ImageLoader {
    fun load(imageUrl: String, view: ImageView) {
        Picasso.get()
            .load(imageUrl)
            .into(view)
    }
}
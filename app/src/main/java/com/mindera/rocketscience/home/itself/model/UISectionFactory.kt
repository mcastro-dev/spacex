package com.mindera.rocketscience.home.itself.model

import android.content.res.Resources
import com.mindera.rocketscience.R

object UISectionFactory {
    fun create(forType: UISection.Type, withResources: Resources) : UISection {
        return when(forType) {
            UISection.Type.COMPANY -> UISection(
                title = withResources.getString(R.string.header_company)
            )
            UISection.Type.ROCKET_LAUNCHES -> UISection(
                title = withResources.getString(R.string.header_launches),
                messageWhenEmpty = withResources.getString(R.string.empty_section_rocket_launches)
            )
        }
    }
}
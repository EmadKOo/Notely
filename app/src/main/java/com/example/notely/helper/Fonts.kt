package com.example.notely.helper

import android.content.Context
import android.graphics.Typeface

class Fonts(var context: Context) {
    val audioWide: Typeface
        get() = Typeface.createFromAsset(context.assets, "Fonts/Audiowide-Regular.ttf")

    val montserratAlternates: Typeface
        get() = Typeface.createFromAsset(
            context.assets,
            "Fonts/MontserratAlternates-Regular.ttf"
        )

    val newsCycle: Typeface
        get() {
            return Typeface.createFromAsset(context.assets, "Fonts/BalooTamma2-Regular.ttf")
        }

}
package com.world.edvora_assignment.Util

class Spannable_String {

    fun getColoredSpanned(text: String?, color: String): String? {
        return "<font color=$color>$text</font>"
    }

}
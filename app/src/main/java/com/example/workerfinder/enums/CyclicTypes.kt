package com.example.workerfinder.enums

enum class CyclicTypes(private val type: String) {
    NONE("NONE"),
    WEEKDAY("WEEKDAY"),
    MONTHDAY("MONTHDAY"),
    YEARDAY("YEARDAY"),
    DAY("DAY");

    override fun toString(): String {
        return type
    }
}
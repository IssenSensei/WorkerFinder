package com.issen.workerfinder.enums

enum class CompletionTypes(private val type: String) {
    COMPLETED("COMPLETED"),
    ONGOING("ONGOING"),
    ABANDONED("ABANDONED");

    override fun toString(): String {
        return type
    }
}
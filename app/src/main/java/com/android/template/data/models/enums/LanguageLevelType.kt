package com.android.template.data.models.enums

enum class LanguageLevelType(val code: Int, val value: String, val localizedTitle: String) {
    BASE(0, "Elementary", "level_base"),
    BELOW_AVERAGE(1, "PreIntermediate", "level_below_average"),
    AVERAGE(2, "Intermediate","level_average"),
    ABOVE_AVERAGE(3, "UpperIntermediate", "level_above_average"),
    ADVANCED(4, "Advanced", "level_advanced"),
    FLUENT(5, "Fluent", "level_fluent"),
    NATIVE(6, "Native", "level_native");

    companion object {
        fun getByCode(code: Int): LanguageLevelType? = values().firstOrNull { it.code == code }
    }
}
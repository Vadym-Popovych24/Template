package com.android.template.data.models.enums

enum class LoggedInMode constructor(private val type: Int) {
    LOGGED_IN_MODE_LOGGED_OUT(0),
    LOGGED_IN_MODE_GOOGLE(1),
    LOGGED_IN_MODE_FB(2),
    LOGGED_IN_MODE_SERVER(3),
    LOGGED_IN_MODE_SERVER_WITH_SELECTED_WORKSPACE_ID(4);

    fun getType(): Int {
        return type
    }
}
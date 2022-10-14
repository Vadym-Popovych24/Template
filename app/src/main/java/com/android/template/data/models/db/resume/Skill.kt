package com.android.template.data.models.db.resume

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Skill : Serializable {

    var id: String? = null

    @SerializedName("name")
    var name: String? = ""

    @SerializedName("level")
    var level: Int? = 0

    @SerializedName("skillType")
    var skillType: Int? = 0

    fun isHardSkill() = Type.HARD.code == skillType

    enum class Type(val code: Int) {
        HARD(0), SOFT(1)
    }
}
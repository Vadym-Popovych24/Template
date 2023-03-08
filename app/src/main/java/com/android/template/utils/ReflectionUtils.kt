package com.android.template.utils

import android.content.Context
import com.android.template.TemplateApp
import java.util.*

fun Class<*>.getGenericClassExtends(clazz: Class<*>): Class<*>? {
    var checkingClazz: Class<*>? = this
    var checkingFinished = false
    while (checkingFinished.not()) {
        if (checkingClazz != null) {
            checkingClazz.genericSuperclass?.apply {
                val signature = this.toString()
                val genericParamsBegin = signature.indexOfFirst {
                    it == '<'
                }
                val genericParamsEnd = signature.indexOfLast {
                    it == '>'
                }
                try {
                    val genericSignature =
                        signature.substring(genericParamsBegin + 1, genericParamsEnd)
                    val generics = genericSignature.split(",")
                    generics.forEach {
                        try {
                            val genericClazz = Class.forName(it.trim())
                            val genericHasParent = genericClazz.containsParent(clazz)
                            if (genericHasParent) {
                                checkingFinished = true
                                return genericClazz
                            }
                        } catch (e: Exception){
                            e.printStackTrace()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            checkingClazz = checkingClazz.superclass
        } else {
            checkingFinished = true
        }
    }
    return null
}

fun Class<*>.containsParent(clazz: Class<*>): Boolean {
    var result = false
    var parentClazz: Class<*>? = superclass
    var checkingFinished = false
    while (checkingFinished.not()) {
        if (parentClazz != null) {
            if (parentClazz.name == clazz.name) {
                result = true
                checkingFinished = true
            } else {
                parentClazz = parentClazz.superclass
            }
        } else {
            checkingFinished = true
        }
    }
    return result
}

fun Class<*>.getLayoutId(): Int {
    val layoutName = simpleName.replace("Binding", "")
        .replace(Regex("[A-Z]")) { "_${it.value.lowercase(Locale.getDefault())}" }
        .split("_")
        .filter { it.isNotEmpty() }
        .toMutableList()
        .run {
            joinToString(separator = "_")//form layoutId name
        }
    return TemplateApp.appContext.getIndentifier(layoutName)
}

private fun Context.getIndentifier(layoutId: String): Int =
    resources.getIdentifier(layoutId, "layout", packageName)
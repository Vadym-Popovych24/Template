package com.android.template.utils

class Connectivity private constructor() {

    companion object {
        private var instance: Connectivity? = null

        fun getInstance(): Connectivity? {
            if (instance == null)
                instance = Connectivity()

            return instance
        }

    }
}
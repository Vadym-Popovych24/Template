package com.android.template.data.remote.impl

import com.android.template.R
import com.android.template.utils.getStringFromResource

class FailedToRefreshTokenException() : IllegalStateException(R.string.unauthorized_error.getStringFromResource)
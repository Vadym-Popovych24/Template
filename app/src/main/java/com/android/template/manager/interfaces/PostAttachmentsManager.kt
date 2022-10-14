package com.android.template.manager.interfaces

import com.android.template.data.models.api.response.ResponseEmptyButNeedResult
import io.reactivex.Single

interface PostAttachmentsManager: BaseManager {

    fun uploadPostAttachment(filePath: String): Single<Int>

    fun deletePostAttachment(attachmentId: Int): Single<ResponseEmptyButNeedResult>
}
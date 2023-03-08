package com.android.template.ui.base

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

abstract class BasePostViewModel : BaseViewModel() {
    private val LENGTH_SHORT_TEXT = 200
    private var isOpenFullText: Boolean = false
    var postId: Int = 0

    val isInEditMode = ObservableBoolean()

    val postCreatorName = ObservableField<String>()
    val postCreatorAvatar = ObservableField<String>()
    val postDate = ObservableField<String>()
    val isShowMore = ObservableBoolean(false)
    val isHaveImage = ObservableBoolean(false)
    val isHaveMoreThatOneImage = ObservableBoolean(false)

    val userAvatar = ObservableField<String>()
    val textComment = ObservableField<String>()
    val isShowFieldComment = ObservableBoolean(false)
    val likesCount = ObservableInt(0)
    val isLikeButtonActive = ObservableBoolean(false)
    val isVisibleLikeCount = ObservableBoolean(false)

    val isClickMoreAction = ObservableBoolean(false)

    init {
     //   userAvatar.set(mPostManager.getUserAvatar())
    }

    val postBody = object : ObservableField<String>() {

        override fun get(): String? {
            val value: String? = super.get()
            if(value.isNullOrEmpty()) return null

            return if (value.length > LENGTH_SHORT_TEXT && !isOpenFullText) {
                isShowMore.set(true)
                value.substring(0, LENGTH_SHORT_TEXT) + "..."
            } else {
                isShowMore.set(false)
                value
            }
        }
    }


    fun clickLike(view: View) {
        if (isLikeButtonActive.get()) {
            unLike()
        } else {
            isLikeButtonActive.set(true)
            likesCount.set(likesCount.get() + 1)
            isVisibleLikeCount.set(true)
        }

 /*       compositeDisposable.add(
            mPostManager.likeOrUnlikePost(postId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.result == ReturnResult.ERROR) {
                        showToast(
                            view.context,
                            response.errorMessage ?: "Something happened when you liked post"
                        )
                        unLike()
                    }
                },
                    { error ->
                        Log.d(
                            "BasePostViewModel",
                            error.localizedMessage ?: "Something happened when like post"
                        )
                        showToast(
                            view.context,
                            error.localizedMessage ?: "Something happened when you liked post"
                        )
                        unLike()
                    })
        )*/
    }

    fun clickMoreAction() {
        isClickMoreAction.set(true)
    }

    fun clickShowMore() {
        isOpenFullText = true
        isShowMore.set(false)
        postBody.notifyChange()
    }

    /*fun deletePost(context: Context, postId: Int) {
        compositeDisposable.add(
            mPostManager.deletePostFromAPI(postId)
                .flatMapCompletable {
                    if (it.result == ReturnResult.SUCCESS) {
                        mPostManager.deletePostFromDB(postId)
                    } else throw IllegalStateException("You need to delete from API first")
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { error ->
                    Log.d(
                        "BasePostViewModel",
                        error.localizedMessage ?: "Something happened when delete post"
                    )
                    showToast(
                        context,
                        error.localizedMessage ?: "Something happened when delete post"
                    )
                    error.printStackTrace()
                }
                .subscribe()
        )
    }*/

   /* fun sendComment(view: View) {
        val text = textComment.get()
        if (text.isNullOrEmpty()) return

        compositeDisposable.add(
            mCommentManager.sendComment(postId, CommentParentType.POST, text)
                .subscribeOn(Schedulers.newThread())
                .subscribe({ response ->
                    mCommentManager.saveCommentToDB(response)
                        .subscribeOn(Schedulers.newThread())
                        .subscribe()
                }, {
                    Log.d(
                        "BasePostViewModel",
                        it.localizedMessage ?: "Something happened when you commented post"
                    )
                    showToast(
                        view.context,
                        it.localizedMessage ?: "Something happened when you commented post"
                    )
                })
        )

        val hideOrShowKeyboard =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Objects.requireNonNull(hideOrShowKeyboard).hideSoftInputFromWindow(view.windowToken, 0)

        textComment.set("")
        isShowFieldComment.set(false)
        Toast.makeText(view.context, R.string.comment_add_success, Toast.LENGTH_SHORT).show()
    }*/

    fun clickOpenCommentField() {
        isShowFieldComment.set(true)
    }

    private fun unLike() {
        isLikeButtonActive.set(false)
        likesCount.set(likesCount.get() - 1)
        isVisibleLikeCount.set(likesCount.get() > 0)
    }


}
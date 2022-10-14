package com.android.template.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.android.template.data.models.db.Profile
import com.android.template.data.models.db.ProfileAndAvatar

@Dao
interface ProfileDao: BaseDao<Profile> {
    @Transaction
    @Query("SELECT * FROM profiles WHERE id =:profileId")
    fun getProfileById(profileId: Int): LiveData<ProfileAndAvatar>
}
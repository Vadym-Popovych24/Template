package com.android.template.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.android.template.data.models.db.ProfileEntity
import com.android.template.data.models.db.ProfileAndAvatar
import io.reactivex.rxjava3.core.Single

@Dao
interface ProfileDao: BaseDao<ProfileEntity> {
    @Transaction
    @Query("SELECT * FROM profiles WHERE id =:id")
    fun getProfileLiveDataById(id: Long): LiveData<ProfileAndAvatar>

    @Transaction
    @Query("SELECT * FROM profiles WHERE email =:email")
    fun getProfileByEmail(email: String): Single<ProfileEntity>

    @Transaction
    @Query("UPDATE profiles SET first_name=:firstName, last_name =:lastName, username=:userName, email =:email, birthday =:birthday, phone_number =:phoneNumber, culture =:culture, gender =:gender WHERE email =:originalEmail")
    fun updateProfile(
        firstName: String, lastName: String, birthday: String, email: String,
        phoneNumber: String?, userName: String, gender: Int?, culture: Int?,
        originalEmail: String
    ): Int

    @Transaction
    @Query("UPDATE profiles SET password=:newPassword WHERE password =:oldPassword")
    fun updatePassword(oldPassword: String, newPassword: String): Single<Int>

    @Transaction
    @Query("SELECT id FROM profiles WHERE email =:email")
    fun getProfileIdByEmail(email: String): Long
}
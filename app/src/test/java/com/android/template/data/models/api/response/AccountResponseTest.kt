package com.android.template.data.models.api.response

import com.google.gson.Gson
import org.junit.Assert.*
import org.junit.Test

class AccountResponseTest {

    private val gson = Gson()

    @Test
    fun `test AccountResponse serialization and deserialization`() {
        val accountJson = """
            {
                "avatar": {
                    "gravatar": {
                        "hash": "test_gravatar_hash"
                    },
                    "tmdb": {
                        "avatar_path": "/path/to/avatar"
                    }
                },
                "id": 12345,
                "name": "Test User",
                "username": "testuser",
                "iso_639_1": "en",
                "iso_3166_1": "US",
                "include_adult": false
            }
        """

        // Deserialization
        val accountResponse = gson.fromJson(accountJson, AccountResponse::class.java)

        // Check if the object is deserialized correctly
        assertEquals(12345, accountResponse.id)
        assertEquals("Test User", accountResponse.name)
        assertEquals("testuser", accountResponse.username)
        assertFalse(accountResponse.includeAdult)

        // Serialization back to JSON to check if we can serialize back to the original JSON
        val serializedJson = gson.toJson(accountResponse)
        assertTrue(serializedJson.contains("test_gravatar_hash"))
        assertTrue(serializedJson.contains("/path/to/avatar"))
    }

    @Test
    fun `test AccountResponse equality`() {
        val avatar1 = Avatar(Gravatar("hash1"), Tmdb("/path/to/avatar1"))
        val avatar2 = Avatar(Gravatar("hash1"), Tmdb("/path/to/avatar1"))

        val account1 = AccountResponse(avatar1, 12345, "Test User", "testuser", "en", "US", false)
        val account2 = AccountResponse(avatar2, 12345, "Test User", "testuser", "en", "US", false)

        // Ensure they are equal (since all fields are the same)
        assertEquals(account1, account2)

        // Modify a field and ensure they are not equal
        val account3 = account1.copy(name = "New Name")
        assertNotEquals(account1, account3)
    }

    @Test
    fun `test AccountResponse with null avatarPath`() {
        val avatar = Avatar(Gravatar("hash"), Tmdb(null))
        val accountResponse = AccountResponse(avatar, 12345, "Test User", "testuser", "en", "US", false)

        assertNull(accountResponse.avatar.tmdb.avatarPath)
    }
}
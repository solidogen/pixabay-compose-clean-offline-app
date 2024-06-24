package com.example.pixabay.data.cache

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pixabay.data.cache.model.ImageEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var db: AppDatabase
    private lateinit var imagesDao: ImagesDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build() // Allow main thread for testing
        imagesDao = db.imagesDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun insertAndRetrieveImage() = runBlocking {
        val image = ImageEntity(
            id = "1",
            username = "user1",
            tags = "tag1, tag2",
            thumbnailUrl = "thumb1",
            largeImageUrl = "large1",
            likes = 10,
            downloads = 20,
            comments = 5,
            query = "query1"
        )
        imagesDao.insertOrReplaceImages(listOf(image))

        val retrievedImage = imagesDao.getImageById("1")

        assertThat(retrievedImage).isNotNull()
        assertThat(retrievedImage?.id).isEqualTo("1")
        assertThat(retrievedImage?.username).isEqualTo("user1")
        assertThat(retrievedImage?.tags).isEqualTo("tag1, tag2")
        assertThat(retrievedImage?.thumbnailUrl).isEqualTo("thumb1")
        assertThat(retrievedImage?.largeImageUrl).isEqualTo("large1")
        assertThat(retrievedImage?.likes).isEqualTo(10)
        assertThat(retrievedImage?.downloads).isEqualTo(20)
        assertThat(retrievedImage?.comments).isEqualTo(5)
        assertThat(retrievedImage?.query).isEqualTo("query1")
    }
}
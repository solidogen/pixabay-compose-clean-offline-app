package com.example.pixabay.data.utils

import com.example.pixabay.data.cache.model.ImageEntity
import com.example.pixabay.data.network.model.ImageDto
import com.example.pixabay.domain.model.ImageModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MappingFunctionsTest {

    @Test
    fun `ImageDto mapToDomain maps correctly`() {
        val dto= ImageDto(
            id = "1",
            username = "user1",
            tags = "tag1, tag2",
            thumbnailUrl = "thumb1",
            largeImageUrl = "large1",
            likes = 10,
            downloads = 20,
            comments = 5
        )

        val domainModel = dto.mapToDomain()

        assertThat(domainModel.id).isEqualTo(dto.id)
        assertThat(domainModel.username).isEqualTo(dto.username)
        assertThat(domainModel.tagsString).isEqualTo(dto.tags)
        assertThat(domainModel.thumbnailUrl).isEqualTo(dto.thumbnailUrl)
        assertThat(domainModel.largeImageUrl).isEqualTo(dto.largeImageUrl)
        assertThat(domainModel.likes).isEqualTo(dto.likes)
        assertThat(domainModel.downloads).isEqualTo(dto.downloads)
        assertThat(domainModel.comments).isEqualTo(dto.comments)
    }

    @Test
    fun `ImageEntity mapToDomain maps correctly`() {
        val entity = ImageEntity(
            id = "2",
            username = "user2",
            tags = "tag3, tag4",
            thumbnailUrl = "thumb2",
            largeImageUrl = "large2",
            likes = 15,
            downloads = 25,
            comments = 8,
            query = "query"
        )

        val domainModel = entity.mapToDomain()

        assertThat(domainModel.id).isEqualTo(entity.id)
        assertThat(domainModel.username).isEqualTo(entity.username)
        assertThat(domainModel.tagsString).isEqualTo(entity.tags)
        assertThat(domainModel.thumbnailUrl).isEqualTo(entity.thumbnailUrl)
        assertThat(domainModel.largeImageUrl).isEqualTo(entity.largeImageUrl)
        assertThat(domainModel.likes).isEqualTo(entity.likes)
        assertThat(domainModel.downloads).isEqualTo(entity.downloads)
        assertThat(domainModel.comments).isEqualTo(entity.comments)
    }

    @Test
    fun `ImageModel mapToEntity maps correctly`() {
        val domainModel = ImageModel(
            id = "3",
            username = "user3",
            tagsString = "tag5, tag6",
            thumbnailUrl = "thumb3",
            largeImageUrl = "large3",
            likes = 20,
            downloads = 30,
            comments = 10
        )
        val query = "testQuery"

        val entity = domainModel.mapToEntity(query)

        assertThat(entity.id).isEqualTo(domainModel.id)
        assertThat(entity.username).isEqualTo(domainModel.username)
        assertThat(entity.tags).isEqualTo(domainModel.tagsString)
        assertThat(entity.thumbnailUrl).isEqualTo(domainModel.thumbnailUrl)
        assertThat(entity.largeImageUrl).isEqualTo(domainModel.largeImageUrl)
        assertThat(entity.likes).isEqualTo(domainModel.likes)
        assertThat(entity.downloads).isEqualTo(domainModel.downloads)
        assertThat(entity.comments).isEqualTo(domainModel.comments)
        assertThat(entity.query).isEqualTo(query)
    }
}
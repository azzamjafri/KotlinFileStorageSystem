package com.example.bucket.repo

import com.example.bucket.model.url
import org.hibernate.validator.constraints.URL
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface urlRepo : JpaRepository<url, Long>{

    @Query(value = "SELECT file_url FROM url u WHERE u.id = :id", nativeQuery = true)
    fun returnFileUrl(@Param(value = "id") userId : Long) : String


}
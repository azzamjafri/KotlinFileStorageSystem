package com.example.bucket.repo

import com.example.bucket.model.url
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface urlRepo : JpaRepository<url, Long>
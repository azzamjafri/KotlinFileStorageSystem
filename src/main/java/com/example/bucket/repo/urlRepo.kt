package com.example.bucket.repo

import com.example.bucket.model.url
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.net.URL

@Repository
interface urlRepo : JpaRepository<url, Long> {

}
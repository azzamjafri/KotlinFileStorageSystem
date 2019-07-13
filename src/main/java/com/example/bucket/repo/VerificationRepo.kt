package com.example.bucket.repo

import com.example.bucket.model.Verification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface VerificationRepo : JpaRepository<Verification, Long>{

//    fun findId(id : Long) : Boolean

     @Query(value = "SELECT can_write FROM verification v WHERE v.id = :id", nativeQuery = true)
    fun findWritePermissionById(@Param("id") userId : Long) : Boolean

    @Query(value = "SELECT can_read FROM verification v WHERE v.id = :id", nativeQuery = true)
    fun findReadPermissionById(@Param("id") userId : Long) : Boolean

}
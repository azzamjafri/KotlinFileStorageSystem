package com.example.bucket.controller

import com.example.bucket.bucketservice.BucketServiceImpl
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class DeleteFromBucket {

    @DeleteMapping(value = ["/delete/{fileUrl}"])
    fun deleteFromBucket(@PathVariable fileUrl : String, bucketServiceImpl: BucketServiceImpl ) {

        println("request accepted")
        val fileName: String = fileUrl.substring(fileUrl.lastIndexOf("/") + 1)

        bucketServiceImpl.deleteFileFromS3Bucket(fileName)
    }
}
package com.example.bucket.bucketservice

import com.example.bucket.model.url
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
interface BucketService {

    fun uploadFileTos3bucket(fileName : String, file : File)

    fun generateFileName(multipart : MultipartFile) : String

    fun convertMultiPartToFile(multipart : MultipartFile) : File

    fun uploadFile(multipartFile: MultipartFile) : url

    fun deleteFileFromS3Bucket(fileUrl : String) : String
}

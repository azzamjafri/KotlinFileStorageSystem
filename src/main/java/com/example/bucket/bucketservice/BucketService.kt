package com.example.bucket.bucketservice

import org.springframework.web.multipart.MultipartFile
import java.io.File

interface BucketService {

    fun uploadFileTos3bucket(fileName : String, file : File)

    fun generateFileName(multipart : MultipartFile) : String

    fun convertMultiPartToFile(multipart : MultipartFile) : File

    fun uploadFile(multipartFile: MultipartFile) : String

    fun deleteFileFromS3Bucket(fileUrl : String) : String
}

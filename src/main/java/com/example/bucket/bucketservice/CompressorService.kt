package com.example.bucket.bucketservice

import com.example.bucket.controller.FileTemplate
import org.springframework.web.multipart.MultipartFile
import java.io.File

interface CompressorService {

    fun imageCompressor(fileTemplate : FileTemplate) : MultipartFile?

    fun convertMultiPartToFile(file : MultipartFile) : File
}
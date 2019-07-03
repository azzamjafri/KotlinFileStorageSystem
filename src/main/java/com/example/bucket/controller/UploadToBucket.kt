package com.example.bucket.controller

import com.example.bucket.bucketservice.BucketServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import kotlin.system.measureTimeMillis

@CrossOrigin
@RestController
class UploadToBucket {

    @PostMapping(value = ["/post"], consumes = ["multipart/form-data"])
    fun bucket(@ModelAttribute fileTemplate: FileTemplate) : String = runBlocking(Dispatchers.Default)  {

        var status : String? = null
        var bucketService = BucketServiceImpl()
        var filename : String

        var time = measureTimeMillis {
            fileTemplate.toString()


                if (fileTemplate.file == null) {
                    return@runBlocking "No file Attached !"
                }
               async {   bucketService.uploadFile(fileTemplate.file as MultipartFile) }

                async {
                    filename = bucketService.generateFileName(fileTemplate.file as MultipartFile)
                    println(filename)
                }.join()


                status = "Success !"
        }
        println("Time Taken - " + time)
        return@runBlocking status.toString()
    }
}

package com.example.bucket.controller

import com.example.bucket.bucketservice.BucketServiceImpl
import com.example.bucket.bucketservice.Compressor
import kotlinx.coroutines.*
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.system.measureTimeMillis

@CrossOrigin
@RestController
class UploadToBucket {


    @PostMapping(value = ["/post"], consumes = ["multipart/form-data"])
    fun bucket(@ModelAttribute fileTemplate: FileTemplate, compressor: Compressor) : String = runBlocking {



       var temp =  GlobalScope.launch {

            fileTemplate.file = compressor.imageCompressor(fileTemplate)
       }.join()


        var status : String? = null
        var bucketService = BucketServiceImpl()
        var filename : String

        var time = measureTimeMillis {
            fileTemplate.toString()


                if (fileTemplate.file == null) {
                    return@runBlocking "No file Attached !"
                }


//            if(temp.isCompleted) {

//                println("Async started !")
                async { bucketService.uploadFile(fileTemplate.file as MultipartFile) }

                async {
                    filename = bucketService.generateFileName(fileTemplate.file as MultipartFile)
                    println(filename)
                }.join()

//            }


                status = "Success !"
        }
        println("Time Taken - " + time)
        return@runBlocking status.toString()
    }
}

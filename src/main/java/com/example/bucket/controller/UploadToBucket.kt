package com.example.bucket.controller

import com.example.bucket.bucketservice.BucketServiceImpl
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@CrossOrigin
@RestController
class UploadToBucket {

    @PostMapping(value = ["/post"], consumes = ["multipart/form-data"])
    fun bucket(@ModelAttribute fileTemplate: FileTemplate): String {

//        val fileTemplate : FileTemplate? = null
//        println(fileTemplate.file.toString() + "****** Printed ! *****")

//        CoroutineScope(newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors(), "2")).async {
        fileTemplate.toString()
//        println("Flag 3")
        var bucketService = BucketServiceImpl()
//        bucketService.uploadFileTos3bucket(fileTemplate.file?.originalFilename.toString(), fileTemplate.file as File)

        if (fileTemplate.file == null) {
            return "No file Attached !"
        }
//        println("Flga 4")
//            delay(1000)
        bucketService.uploadFile(fileTemplate.file as MultipartFile)

        var filename = bucketService.generateFileName(fileTemplate.file as MultipartFile)

        println(filename)

        return "Success !"

//        }.await()




    }
}

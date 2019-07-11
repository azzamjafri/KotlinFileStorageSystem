package com.example.bucket.controller

import com.example.bucket.bucketservice.BucketServiceImpl
import com.example.bucket.bucketservice.Compressor
import com.example.bucket.bucketservice.VideoCompressor
import com.example.bucket.model.url
import com.example.bucket.repo.urlRepo
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import kotlin.system.measureTimeMillis


/*
 *  -> This class is getting the file from "/post" as a Multipart File .
 *  -> Content type of Multipart file is detected first and send for compression accordingly.
 *  -> Compressed file is than sent to the BukcketService class to get uploaded to the bucket.
 *  -> If the file is uploaded successfully a message will be shown as "SUCCESS !"
 *  -> Coroutines have been implemented from the compressing a file to uploading it to the bucket.
 *  -> Uploaded file generates a link for the file to get its access from the bucket and can be directly accessed from there.
 */


@CrossOrigin
@RestController
class UploadToBucket(@Autowired var urlRepo : urlRepo) {



    var url = url()

    @PostMapping(value = ["/post"], consumes = ["multipart/form-data"])
    fun bucket(@ModelAttribute fileTemplate: FileTemplate, compressor: Compressor) : String = runBlocking {


       var fileType = fileTemplate.file?.contentType


        if(fileType.equals("image/jpeg") || fileType.equals("image/jpg")) {        // Detecting file type for images

            if(fileTemplate.file?.size!! > 95000) {                                             // Compression will take place if image size is greater than 95kb else same sized image will be uploaded to bucket
                GlobalScope.launch {
                    fileTemplate.file = compressor.imageCompressor(fileTemplate)                //Passing image to the Compressor class
                }.join()                                                                        // .join() will wait for the thread to complete its execution ( Blocking parent(main) thread to go any futher)
            }                                                                                   // Waiting for this:CoroutineScope to return its result
        }





        else if(fileType.equals("application/pdf")) {                          // Detecting pdf file format
            GlobalScope.launch {
                fileTemplate.file = compressor.pdfCompressor(fileTemplate)            // Passing pdf for compression
            }.join()                                                                  // .join() will wait for the thread to complete its execution ( Blocking parent(main) thread to go any futher)
        }                                                                             // Waiting for this:CoroutineScope to return its result



        else if(fileType.equals("video/mp4")) {               // Detecting video type
            GlobalScope.launch {
                val viDeoCompressor = VideoCompressor()
                fileTemplate.file = viDeoCompressor.videoCompressor(fileTemplate)
            }.join()
        }




        var status : String? = null                                      // Status will be return as Success if upload is successful
        var bucketService = BucketServiceImpl()
        var filename : String
        var time = measureTimeMillis {                            // Calculating time required for upload (MILLISECONDS)
            fileTemplate.toString()

                if (fileTemplate.file == null) {                         // If no file is attached .
                    return@runBlocking "No file Attached !"
                }
                async { url = bucketService.uploadFile(fileTemplate.file as MultipartFile) }.join()                  // Uploading file to AWS Bucket
                async {
                    filename = bucketService.generateFileName(fileTemplate.file as MultipartFile)       // Generating file name with Time stamp
                    println(filename)
                }.join()
                status = "SUCCESS !"
        }

        urlRepo.save(url)

        println("Time Taken - " + time)

        return@runBlocking status.toString()

    }


}

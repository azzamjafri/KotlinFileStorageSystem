package com.example.bucket.bucketservice

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.util.*

class BucketServiceImpl() : BucketService {

    var bucketName: String = "project-striker-bucket"
    var endpointUrl: String = "https://s3.ap-south-1.amazonaws.com"
    var s3client: AmazonS3? = null
    var accessKey = "YOUR ACCESS KEY"
    var secretKey = "YOUR SCRET KEY"

    init {
        var credentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
        s3client = AmazonS3Client(credentials)
    }


    override fun uploadFileTos3bucket(fileName: String, file: File) {

//        println("Flag 2")
//        GlobalScope.async {
//            delay(500)
//            println("Flag 2")


        s3client?.putObject(PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead))


//            println("Flag 3")
//        }
    }


    override fun generateFileName(multiPart: MultipartFile): String {

        return Date().time.toString() + "-" + multiPart.getOriginalFilename().replace(" ", "_")
    }


    override fun convertMultiPartToFile(file: MultipartFile): File {

        var convFile: File = File(file.getOriginalFilename())

//        CoroutineScope(newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors(), "local")).async {

//                    delay(500)
//                    println("Flag 1")

        var fos = FileOutputStream(convFile)
        fos.write(file.getBytes())
        fos.close()

//                }.await()
        return convFile

    }


    override fun uploadFile(multipartFile: MultipartFile): String {

        var fileUrl: String = ""
        try {
            var file: File
            var fileName: String

//            CoroutineScope(newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors(), "local1")).async {

            file = convertMultiPartToFile(multipartFile)

//                    }.join()


//            CoroutineScope(newFixedThreadPoolContext(Runtime.getRuntime().availableProcessors(), "local2")).async {
            fileName = generateFileName(multipartFile)

//                    }.join()


//            GlobalScope.async {

            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName

//            println("Flag 1")

            println(fileUrl)
            uploadFileTos3bucket(fileName, file)
            file!!.delete()
//            }.onJoin

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return fileUrl
    }


    override fun deleteFileFromS3Bucket(fileUrl: String): String {
        var fileName: String = fileUrl.substring(fileUrl.lastIndexOf("/") + 1)
        s3client?.deleteObject(DeleteObjectRequest(bucketName, fileName))
        return "Successfully deleted"
    }

}

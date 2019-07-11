package com.example.bucket.bucketservice

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.PutObjectRequest
import com.example.bucket.model.url
import com.example.bucket.repo.urlRepo
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/*
 *  Bucket Credentials will be set first using constructor of this class.
 *  Multipart File is converted to File type (java) first.
 *  File name and url is then generated.
 *  File will be locally saved to the system then get upload to the Bucket and the local copy is deleted from the system.
 *  File can be deleted from the bucket by passing the generated file URL.
 */

class BucketServiceImpl() : BucketService {


    var urlObj = url()

    var bucketName: String = "project-striker-bucket"
    var endpointUrl: String = "https://s3.ap-south-1.amazonaws.com"
    var s3client: AmazonS3? = null
    var accessKey = "AKIA2JIA246E4PQQ4Y2H"
    var secretKey = "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"

    init {              //  Constructor setting up client credentials using defined details.
        val credentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
        s3client = AmazonS3Client(credentials)
    }


    override fun uploadFileTos3bucket(fileName: String, file: File) {


        s3client?.putObject(PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead))         // putObject puts the file in the bucket.


    }


    override fun generateFileName(multiPart : MultipartFile): String {       // Adding timestamp to file name

        return Date().time.toString() + "-" + multiPart.getOriginalFilename().replace(" ", "_")
    }


    override fun convertMultiPartToFile(file: MultipartFile): File {        // Converting multipart file to java file type

        val convFile: File = File(file.getOriginalFilename() as String)


        val fos = FileOutputStream(convFile)
        fos.write(file.getBytes())
        fos.close()

        return convFile

    }


    override fun uploadFile(multipartFile: MultipartFile): url = runBlocking {

//        var fileUrl: String = ""
        val customDispatcher = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
                .asCoroutineDispatcher()                //Custom Dispatcher is defined.
        try {
            val file : File
            val fileName : String

            file = withContext(customDispatcher){ convertMultiPartToFile(multipartFile) }  // Converting multipart to file
            fileName = withContext(customDispatcher) { generateFileName(multipartFile) }    // Generating file name

            urlObj.fileUrl = endpointUrl + "/" + bucketName + "/" + fileName
            println(urlObj.fileUrl)
//            println("Reaching save")

            println(urlObj.id)


            val job = async{ uploadFileTos3bucket(fileName, file) }   // Uplaoding to bucket using putObject
            job.join()
            file.delete()                                                         // Deleting local copy from the system.
            (customDispatcher.executor as ExecutorService).shutdown()               // Shutting down Executor services.


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@runBlocking urlObj        // Returning file URL. Here runBlocking keeps a check whether all the threads completed their jobs or not
                                                              // if all the jobs are completed their results are combined using .join() and then the fianl result is return when Main thread is not blocked.
    }


    override fun deleteFileFromS3Bucket(fileName: String): String {          // Deleting the file using file Url
//        println(fileName)
        s3client?.deleteObject(DeleteObjectRequest(bucketName, fileName))
        return "Successfully deleted"
    }

}

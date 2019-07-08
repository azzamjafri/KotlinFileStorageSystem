package com.example.bucket.bucketservice

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.amazonaws.services.s3.model.PutObjectRequest
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

class BucketServiceImpl() : BucketService {

    @Autowired
    private var urlRepo: urlRepo? = null

    var bucketName: String = "project-striker-bucket"
    var endpointUrl: String = "https://s3.ap-south-1.amazonaws.com"
    var s3client: AmazonS3? = null
    var accessKey = "AKIA2JIA246E4PQQ4Y2H"
    var secretKey = "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"

    init {
        var credentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
        s3client = AmazonS3Client(credentials)
    }


    override fun uploadFileTos3bucket(fileName: String, file: File) {


        s3client?.putObject(PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead))


    }


    override fun generateFileName(multiPart: MultipartFile): String {

        return Date().time.toString() + "-" + multiPart.getOriginalFilename().replace(" ", "_")
    }


    override fun convertMultiPartToFile(file: MultipartFile): File {

        var convFile: File = File(file.getOriginalFilename())


        var fos = FileOutputStream(convFile)
        fos.write(file.getBytes())
        fos.close()

        return convFile

    }


    override fun uploadFile(multipartFile: MultipartFile): String = runBlocking {

//        println("BucketService started ")
        var fileUrl: String = ""
        val customDispatcher = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
                .asCoroutineDispatcher()
        try {
            var file : File
            var fileName : String

            file = withContext(customDispatcher){ convertMultiPartToFile(multipartFile) }
            fileName = withContext(customDispatcher) { generateFileName(multipartFile) }

            fileUrl = endpointUrl + "/" + bucketName + "/" + fileName
            println(fileUrl)

//            var url : Url(fileUrl)
//            urlRepo.save(url)

            val job = async{ uploadFileTos3bucket(fileName, file) }
            job.join()
            file!!.delete()
            (customDispatcher.executor as ExecutorService).shutdown()


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@runBlocking fileUrl
    }


    override fun deleteFileFromS3Bucket(fileUrl: String): String {
        var fileName: String = fileUrl.substring(fileUrl.lastIndexOf("/") + 1)
        s3client?.deleteObject(DeleteObjectRequest(bucketName, fileName))
        return "Successfully deleted"
    }

}

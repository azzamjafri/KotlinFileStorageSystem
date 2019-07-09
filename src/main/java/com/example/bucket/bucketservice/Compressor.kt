package com.example.bucket.bucketservice


import com.example.bucket.controller.FileTemplate
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.io.*
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import java.nio.file.Files
import java.nio.file.Paths
import java.io.FileOutputStream
import com.itextpdf.text.pdf.PdfStamper
import java.io.FileInputStream
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.DocumentException
import kotlinx.coroutines.*
import java.util.concurrent.Executors

/*
 *   Receive the Multipart file to be compressed and convert it to the java File type.
 *   Compress the File.
 *   Compression doesn't change the resolution of the file.
 *   Convert it back to the multipart file type.
 *   Returns back the compressed file.
 *   No returned file can be greater than 800 kB
 */



class Compressor : CompressorService{


    @Throws(IOException::class)
    override fun imageCompressor(fileTemplate: FileTemplate) : MultipartFile? = runBlocking {

        val customDispatcher = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
                .asCoroutineDispatcher()                                        // Creating a new Executor Pool

        var file = withContext(customDispatcher) { convertMultiPartToFile(fileTemplate.file as MultipartFile) }         // Converting Multipart file to File type java
        val imageName : String = file!!.absolutePath

        val input = File(imageName)
        val image = withContext(customDispatcher) { ImageIO.read(input) }
        val output = File(imageName)
        val out = FileOutputStream(output)

        val writer = ImageIO.getImageWritersByFormatName("jpeg").next()
        val ios = ImageIO.createImageOutputStream(out)

        writer.output = ios
        val param = writer.defaultWriteParam



        if (param.canWriteCompressed()) {
            param.compressionMode = ImageWriteParam.MODE_EXPLICIT
            if(fileTemplate.file!!.size > 5000000){             // Checking orignal size to be greater than 5 MB
                param.compressionQuality = 0.220f               // Compressing it to around nearly 85 %
            }
            else {
                param.compressionQuality = 0.285f               // If original size is less than 5 MB
            }                                                   // Compressing it to around 90 %
        }



        CoroutineScope(customDispatcher).launch {

            writer.write(null, IIOImage(image, null, null), param)                  // Compressing image
        }.join()



       var fileToBeReturned : MultipartFile? = null

        CoroutineScope(customDispatcher).launch {
        val path = Paths.get(imageName).toAbsolutePath()
        val name = fileTemplate.file!!.name
        val originalFileName = file.name
        val contentType = "image/jpeg"
        var content: ByteArray? = null
        try {
            content = Files.readAllBytes(path)
        } catch (e: IOException) {}

            fileToBeReturned = MockMultipartFile(name,
                    originalFileName, contentType, content)
        }.join()

        out.close()
        ios.close()
        writer.dispose()

        return@runBlocking fileToBeReturned             // Returns file when main thread is again unblocked.

    }


    override fun pdfCompressor( fileTemplate: FileTemplate ) : MultipartFile? {         // ** NOTE **
                                                                                        // PDF compression is not yet working.
        var file = convertMultiPartToFile(fileTemplate.file as MultipartFile)
        val reader = PdfReader(FileInputStream(file.absolutePath.toString()))
        val stamper = PdfStamper(reader, FileOutputStream(file.absolutePath.toString().replace(".pdf", "-copressed.pdf")))
        reader.removeFields()
        reader.removeUnusedObjects()

        val total = reader.numberOfPages + 1
        for (i in 1 until total) {
            reader.setPageContent(i + 1, reader.getPageContent(i + 1))
        }
        try {

            stamper.setFullCompression()
            stamper.close()
        } catch (e: DocumentException) {
            e.printStackTrace()
        }



        var path = Paths.get(file.absolutePath.toString().replace(".pdf", "-copressed.pdf")).toAbsolutePath()
        var name = file.absolutePath.toString().replace(".pdf", "-OUTcopressed.pdf")
        var originalFileName = file.absolutePath.toString().replace(".pdf", "-copressed.pdf")
        var contentType = "application/pdf"
        var content: ByteArray? = null

        try {
            content = Files.readAllBytes(path)

        } catch (e: IOException) {}

        var fileToBeReturned : MultipartFile = MockMultipartFile(name,
                originalFileName, contentType, content)



        return fileToBeReturned

    }

    override fun convertMultiPartToFile(file: MultipartFile): File {

        var convFile = File(file.getOriginalFilename())
        var fos = FileOutputStream(convFile)

        fos.write(file.getBytes())
        fos.close()

        return convFile
    }

}
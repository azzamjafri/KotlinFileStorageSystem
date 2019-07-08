package com.example.bucket.bucketservice


import com.example.bucket.controller.FileTemplate
import com.spire.pdf.FileFormat
import com.spire.pdf.PdfCompressionLevel
import com.spire.pdf.PdfDocument
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

class Compressor : CompressorService{


    @Throws(IOException::class)
    override fun imageCompressor(fileTemplate: FileTemplate) : MultipartFile? {

        var file = convertMultiPartToFile(fileTemplate.file as MultipartFile)
        val imageName : String = file!!.absolutePath
        val input = File(imageName)
        val image = ImageIO.read(input)


        val output = File(imageName)
        val out = FileOutputStream(output)


        val writer = ImageIO.getImageWritersByFormatName("jpeg").next()
        val ios = ImageIO.createImageOutputStream(out)
        writer.output = ios

        val param = writer.defaultWriteParam
        if (param.canWriteCompressed()) {
            param.compressionMode = ImageWriteParam.MODE_EXPLICIT
            param.compressionQuality = 0.118f
        }

        writer.write(null, IIOImage(image, null, null), param)


        var path = Paths.get(imageName).toAbsolutePath()
        var name = fileTemplate.file!!.name
        var originalFileName = file.name
        var contentType = "image/jpeg"
        var content: ByteArray? = null

        try {
            content = Files.readAllBytes(path)

        } catch (e: IOException) {}

        var fileToBeReturned : MultipartFile = MockMultipartFile(name,
                originalFileName, contentType, content)


        out.close()
        ios.close()
        writer.dispose()

        return fileToBeReturned

    }


    override fun pdfCompressor( fileTemplate: FileTemplate ) : MultipartFile? {

        var file = convertMultiPartToFile(fileTemplate.file as MultipartFile)
        val reader = PdfReader(FileInputStream(file.absolutePath.toString()))
        val stamper = PdfStamper(reader, FileOutputStream(file.absolutePath.toString().replace(".pdf", "-copressed.pdf")))
        //PdfWriter writer = stamper.wr
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

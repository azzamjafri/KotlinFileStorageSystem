package com.example.bucket.bucketservice


import com.example.bucket.controller.FileTemplate
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

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
            param.compressionQuality = 0.005f
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



    override fun convertMultiPartToFile(file: MultipartFile): File {

        var convFile = File(file.getOriginalFilename())
        var fos = FileOutputStream(convFile)

        fos.write(file.getBytes())
        fos.close()

        return convFile
    }


}

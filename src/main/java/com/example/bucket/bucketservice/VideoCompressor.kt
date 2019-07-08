package com.example.bucket.bucketservice

import com.example.bucket.controller.FileTemplate
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths


import net.bramp.ffmpeg.FFmpeg
import net.bramp.ffmpeg.FFmpegExecutor
import net.bramp.ffmpeg.FFprobe
import net.bramp.ffmpeg.builder.FFmpegBuilder
import java.io.File
import java.io.FileOutputStream
import java.io.IOException



class VideoCompressor() {

    fun videoCompressor(fileTemplate: FileTemplate) : MultipartFile {

        var file = convertMultiPartToFile(fileTemplate.file)

        val ffmpeg = FFmpeg("/home/azzam/IdeaProjects/FFmpeg/ffmpeg-cli-wrapper/src/main/java/net/bramp/ffmpeg/FFmpeg.java")
//        "/home/azzam/IdeaProjects/FFmpeg/ffmpeg-cli-wrapper/src/main/java/net/bramp/ffmpeg/FFmpeg.java"
        val ffprobe = FFprobe("/home/azzam/IdeaProjects/FFmpeg/ffmpeg-cli-wrapper/src/main/java/net/bramp/ffmpeg/FFprobe.java")

        var builder : FFmpegBuilder = FFmpegBuilder()

                .setInput(file.absolutePath.toString())     // Filename, or a FFmpegProbeResult
                .overrideOutputFiles(true) // Override the output if it exists

                .addOutput(file.absolutePath.toString().replace(".mp4", "-compressed.mp4"))   // Filename for the destination
                .setFormat("mp4")        // Format is inferred from filename, or can be set
                .setTargetSize(250_000)  // Aim for a 250KB file

                .disableSubtitle()       // No subtiles

                .setAudioChannels(1)         // Mono audio
                .setAudioCodec("aac")        // using the aac codec
                .setAudioSampleRate(48_000)  // at 48KHz
                .setAudioBitRate(32768)      // at 32 kbit/s

                .setVideoCodec("libx264")     // Video using x264
                .setVideoFrameRate(24, 1)     // at 24 frames per second
                .setVideoResolution(640, 480) // at 640x480 resolution

                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL) // Allow FFmpeg to use experimental specs
                .done()

        var executor : FFmpegExecutor = FFmpegExecutor(ffmpeg, ffprobe)

// Run a one-pass encode
        executor.createJob(builder).run()

// Or run a two-pass encode (which is better quality at the cost of being slower)
        executor.createTwoPassJob(builder).run()


        var path = Paths.get(file.absolutePath.toString().replace(".mp4", "-compressed.mp4")).toAbsolutePath()
        var name = fileTemplate.file!!.name
        var originalFileName = file.name
        var contentType = "video/mp4"
        var content: ByteArray? = null

        try {
            content = Files.readAllBytes(path)

        } catch (e: IOException) {}

        var fileToBeReturned : MultipartFile = MockMultipartFile(name,
                originalFileName, contentType, content)
        println("Returning......." + fileToBeReturned.size)
        return fileToBeReturned
    }



    fun convertMultiPartToFile(file : MultipartFile?): File {

        var convFile = File(file?.getOriginalFilename())
        var fos = FileOutputStream(convFile)

        fos.write(file?.getBytes())
        fos.close()

        return convFile
    }




//class VideoCompressor() {
//    fun videoCompressor(fileTemplate : FileTemplate) : MultipartFile? {
//
////        val factory = XXHashFactory.fastestInstance()
////
////        val data = "12345345234572".toByteArray(charset("UTF-8"))
////        val `in` = ByteArrayInputStream(data)
////
////        val seed = -0x68b84d74 // used to initialize the hash value, use whatever
//         value you want, but always the same
////        val hash32 = factory.newStreamingHash32(seed)
////        val buf = ByteArray(8) // for real-world usage, use a larger buffer, like 8192 bytes
////        while (true) {
////            val read = `in`.read(buf)
////            if (read == -1) {
////                break
////            }
////            hash32.update(buf, 0, read)
////        }
////        val hash = hash32.getValue()
//
//
////        val factory = LZ4Factory.fastestInstance()
//
//
//// compress data
//
//
//
//        val data = "12345345234572".toByteArray(charset("UTF-8"))
//        val decompressedLength = data.size
//
//        val compressor = factory.fastCompressor()
//        val maxCompressedLength = compressor.maxCompressedLength(decompressedLength)
//        val compressed = ByteArray(maxCompressedLength)
//        val compressedLength = compressor.compress(data, 0, decompressedLength, compressed, 0, maxCompressedLength)
//
//        var file = convertMultiPartToFile(fileTemplate.file)
//        val outStream = LZ4FrameOutputStream(FileOutputStream(File(fileTemplate.file.toString())))
//        outStream.write(data)
//        outStream.close()
//
//        val restored = ByteArray(decompressedLength)
//        val inStream = LZ4FrameInputStream(FileInputStream(File(fileTemplate.file.toString().replace(".mp4", "-test.mp4"))))
//        inStream.read(restored)
//        inStream.close()
//
////                .replace(".pdf", "-test.lz4")
//
//        val path = Paths.get(file.absolutePath.toString().replace(".mp4", "-test.mp4")).toAbsolutePath()
//        val name = file.absolutePath.toString().replace(".mp4", "-OUTcopressed.mp4")
//        val originalFileName = file.absolutePath.toString().replace(".mp4", "-test.mp4")
//        val contentType = "video/mp4"
//        var content: ByteArray? = null
//
//        try {
//            content = Files.readAllBytes(path)
//
//        } catch (e: IOException) {}
//
//        var fileToBeReturned : MultipartFile = MockMultipartFile(name,
//                originalFileName, contentType, content)
//
//
//
//        return fileToBeReturned
//
//
//    }
}
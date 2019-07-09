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

/*
 * Video Compression is using Library FFmpeg for compressing videos.
 *
 *  NOTE : -  FFMPEG currently not working.
 */




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
}
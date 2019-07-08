//package com.example.bucket.bucketservice
//
//import com.lowagie.text.Document
//import com.lowagie.text.pdf.PdfReader
//import com.lowagie.text.pdf.PdfStamper
//import com.lowagie.text.pdf.PdfWriter
//
//import java.io.FileOutputStream
//import java.io.IOException
//
//object Useful {
//
//    @Throws(Exception::class)
//    @JvmStatic
//    fun main(args: Array<String>) {
//        var reader = PdfReader("1.pdf")
//        var stamper = PdfStamper(reader, FileOutputStream("my.pdf"), PdfWriter.VERSION_1_5)
//        stamper.setFullCompression()
//        stamper.close()
//
//        reader = PdfReader("1.pdf")
//        stamper = PdfStamper(reader, FileOutputStream("myDecompressed.pdf"), '1')
//        Document.compress = false
//        val total = reader.numberOfPages + 1
//        for (i in 1 until total) {
//            reader.setPageContent(i, reader.getPageContent(i))
//        }
//        stamper.close()
//
//        showFileSize("1.pdf")
//        showFileSize("my.pdf")
//        showFileSize("myDecompressed.pdf")
//
//    }
//
//    @Throws(IOException::class)
//    private fun showFileSize(filename: String) {
//        val reader = PdfReader(filename)
//        print("Size ")
//        print(filename)
//        print(": ")
//        println(reader.fileLength)
//    }
//
//}

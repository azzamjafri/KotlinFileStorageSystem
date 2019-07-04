package com.example.bucket.controller

import org.springframework.web.multipart.MultipartFile
import java.io.File

class FileTemplate {

    var file : MultipartFile? = null
    var type : String? = "nothing"
//    var path : String? = ""
//    path : String = (file as File).absolutePath
    override fun toString(): String {
        return "File Template=(file=$file, type=$type)"
    }

}
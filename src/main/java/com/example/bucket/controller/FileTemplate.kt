package com.example.bucket.controller

import org.springframework.web.multipart.MultipartFile

class FileTemplate {

    var file : MultipartFile? = null
    var type : String? = "nothing"

    override fun toString(): String {
        return "File Template=(file=$file, type=$type)"
    }

}
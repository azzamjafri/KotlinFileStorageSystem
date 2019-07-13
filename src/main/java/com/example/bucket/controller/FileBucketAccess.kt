package com.example.bucket.controller

import com.example.bucket.repo.VerificationRepo
import com.example.bucket.repo.urlRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@CrossOrigin
@RestController
class FileBucketAccess(@Autowired var verification : VerificationRepo, @Autowired var urlRepo: urlRepo) {


    @GetMapping(value = ["/files/{userId}/{fileId}"])
    fun fileAccess(@PathVariable userId : Long, @PathVariable fileId : Long) : String{

        if(!(verification.findReadPermissionById(userId))) {
            println("No read permission")
            return "You don't have the read permissions !"
        }

        return urlRepo.returnFileUrl(fileId)
    }







}
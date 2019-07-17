package com.example.bucket.controller

import com.example.bucket.model.JwtUser
import com.example.bucket.security.JwtGenerator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/token")
class TokenController(private var jwtGenerator: JwtGenerator) {


    @PostMapping
    fun generate(@RequestBody jwtUser : JwtUser) : String{

        return jwtGenerator.generate(jwtUser)
    }
}
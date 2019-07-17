package com.example.bucket.model

import org.jetbrains.kotlin.load.kotlin.JvmType
import org.omg.CORBA.Object
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class JwtAuthenticationToken : UsernamePasswordAuthenticationToken {

    private var token : String

    constructor(token: String) : super(null, null){
        this.token = token
    }

    fun getToken() : String {
        return token
    }

    fun setToken(token: String) {
        this.token = token
    }

    override fun getCredentials() : Object? {
        return null
    }

    override fun getPrincipal() : Object? {
        return null
    }
}

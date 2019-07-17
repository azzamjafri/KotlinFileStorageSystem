package com.example.bucket.security

import com.example.bucket.model.JwtUser
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component

@Component
class JwtValidator {

    private var secret : String = "secret"

    fun validate(token: String) : JwtUser {

        var jwtUser : JwtUser? = null
        try {
            val body: Claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .body

            val jwtUser = JwtUser()

            jwtUser.setUserName(body.subject)
            jwtUser.setId(java.lang.Long.parseLong(body["userId"].toString()))
            jwtUser.setRole(body.get("role").toString())
        }catch (e : Exception){
            println(e)
        }

        return jwtUser
    }

}
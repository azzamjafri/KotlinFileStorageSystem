package com.example.bucket.security

import org.springframework.stereotype.Component
import io.jsonwebtoken.Jwts
import com.example.bucket.model.JwtUser
import io.jsonwebtoken.SignatureAlgorithm


@Component
class JwtGenerator {

    fun generate(jwtUser: JwtUser): String {

        val claims = Jwts.claims()
                .setSubject(jwtUser.getUserName())
        claims["userId"] = jwtUser.getId().toString()
        claims["role"] = jwtUser.getRole()


        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, "secret")
                .compact()
    }


}

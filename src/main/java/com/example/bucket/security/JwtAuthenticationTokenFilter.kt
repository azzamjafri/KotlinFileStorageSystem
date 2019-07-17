package com.example.bucket.security

import com.example.bucket.model.JwtAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import javax.jws.HandlerChain
import javax.jws.WebResult
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class JwtAuthenticationTokenFilter : AbstractAuthenticationProcessingFilter {


    constructor() : super("/**/rest/**") {}

    override fun attemptAuthentication(httpServletRequest: HttpServletRequest?, httpServletResponse : HttpServletResponse?): Authentication? {

        val header : String? = httpServletRequest?.getHeader("Authorisation")

        if(header == null || header.startsWith("Token")) {
            throw RuntimeException("JWT Token is missing !")
        }

        val authenticationToken : String = header.substring(6)

        val token : JwtAuthenticationToken = JwtAuthenticationToken(authenticationToken)




        return authenticationManager.authenticate(token)
    }


    @Override
    protected fun sucessfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        super.successfulAuthentication(request, response, chain, authResult)
        chain.doFilter(request, response)
    }

}
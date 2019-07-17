package com.example.bucket.security

import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtSuccessHandler : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(httpServletRequest: HttpServletRequest?, httpServletResponse: HttpServletResponse?, authentication: Authentication?) {
        println("Successfully Authenticated !")
    }

}
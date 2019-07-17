package com.example.bucket.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(httpServletRequest : HttpServletRequest?, httpServletResponse : HttpServletResponse?, authenticationException : AuthenticationException?) {

        httpServletResponse?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED")
    }

}
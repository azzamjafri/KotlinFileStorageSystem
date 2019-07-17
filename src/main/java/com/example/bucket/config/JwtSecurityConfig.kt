package com.example.bucket.config

import com.example.bucket.security.JwtAuthenticationEntryPoint
import com.example.bucket.security.JwtAuthenticationProvider
import com.example.bucket.security.JwtAuthenticationTokenFilter
import com.example.bucket.security.JwtSuccessHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
@Configuration
open class JwtSecurityConfig : WebSecurityConfigurerAdapter() {


    @Autowired
    private val authenticationProvider: JwtAuthenticationProvider? = null
//        @Autowired
    private val entryPoint: JwtAuthenticationEntryPoint? = null

    @Bean
    public override fun authenticationManager(): AuthenticationManager {
        return ProviderManager(listOf(authenticationProvider))
    }

    @Bean
    open fun authenticationTokenFilter(): JwtAuthenticationTokenFilter {
        val filter = JwtAuthenticationTokenFilter()
        filter.setAuthenticationManager(authenticationManager())
        filter.setAuthenticationSuccessHandler(JwtSuccessHandler())
        return filter
    }


    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {

        http.csrf().disable()
                .authorizeRequests().antMatchers("**/rest/**").authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
        http.headers().cacheControl()

    }
}
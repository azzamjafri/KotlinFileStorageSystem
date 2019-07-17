package com.example.bucket.security

import com.example.bucket.model.JwtAuthenticationToken
import com.example.bucket.model.JwtUser
import com.example.bucket.model.JwtUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider : AbstractUserDetailsAuthenticationProvider() {

    @Autowired
    private var jwtValidator = JwtValidator()

    override fun additionalAuthenticationChecks(userDetails : UserDetails?, usernamePasswordAuthenticationToken : UsernamePasswordAuthenticationToken?) {

    }

    override fun retrieveUser(userName : String?, usernamePasswordAuthenticationToken : UsernamePasswordAuthenticationToken?): UserDetails {

//        var token : JwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken
        val jwtAuthenticationToken = usernamePasswordAuthenticationToken as JwtAuthenticationToken
        val token : String = jwtAuthenticationToken.getToken()
        val jwtUser : JwtUser? = jwtValidator.validate(token)

        if(jwtUser == null ){
            throw RuntimeException("JWT Token is incorrect !")
        }

        var grantedAuthorities : List<GrantedAuthority> = AuthorityUtils
                .commaSeparatedStringToAuthorityList(jwtUser.getRole())

        return JwtUserDetails(jwtUser.getUserName(), jwtUser.getId(), token, grantedAuthorities)

    }


    @Override
    override fun supports(aClass : Class<*>) : Boolean {
        return (JwtAuthenticationToken::class.java.isAssignableFrom(aClass))
    }

}
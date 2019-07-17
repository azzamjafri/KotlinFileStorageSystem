package com.example.bucket.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtUserDetails : UserDetails {

    private var userName : String
    private var id : Long
    private var token : String
    private var authorities: Collection<GrantedAuthority>

    constructor(userName: String, id: Long, token: String, grantedAuthorities: List<GrantedAuthority>) {
        this.userName = userName
        this.id = id
        this.token = token
        this.authorities = grantedAuthorities
    }



    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }
//    fun setAuthorities(authorities : Collection<GrantedAuthority>) {
//        this.authorities = authorities
//    }

    override fun getUsername(): String {
        return userName
    }

//    fun setUsername(userName: String){
//        this.userName = userName
//    }

    fun getToken(): String {
        return token
    }
//    fun setToken(token: String){
//        this.token = token
//    }

    fun getId() : Long {
        return id
    }
//    fun setId(id: Long) {
//        this.id = id
//    }

    override fun getPassword(): String? {
        return null
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

}

package com.example.bucket.model

class JwtUser {

    private var role : String = ""
    private var id : Long = 0
    private var userName : String  = ""

    fun setUserName(userName : String) {
        this.userName = userName
    }

    fun setId(id : Long) {
        this.id = id

    }

    fun setRole(role: String) {
        this.role = role
    }

    fun getUserName() : String {
        return userName
    }

    fun getId() : Long {
        return id
    }

    fun getRole() : String {
        return role
    }

}

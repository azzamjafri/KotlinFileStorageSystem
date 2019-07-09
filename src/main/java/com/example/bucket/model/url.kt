package com.example.bucket.model

import javax.persistence.*


@Entity @Table( name = "UrlTable")
class url() {

    @Column(name = "URL")
    var fileUrl : String? = ""

    @GeneratedValue( strategy = GenerationType.AUTO )
    @Id @Column(name = "ID")
    var id : Long? = null

    companion object{
        var incrementer : Long = 0
    }

    init {
        this.id = incrementer++
    }

}
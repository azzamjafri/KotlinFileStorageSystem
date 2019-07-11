package com.example.bucket.model

import com.example.bucket.repo.urlRepo
import org.hibernate.validator.constraints.NotBlank
import javax.persistence.*


@Entity
class url() {


    @Id @GeneratedValue( strategy = GenerationType.AUTO )
    var id : Long


    @get : NotBlank
    var fileUrl : String? = ""

    companion object {
        var temp : Long = 0
    }


    init {
        id = temp++
    }

}
package com.example.bucket.model

import org.hibernate.validator.constraints.NotBlank
import javax.persistence.*


@Entity
class url() {


    @Id @GeneratedValue( strategy = GenerationType.IDENTITY )
    var id : Long


    @get : NotBlank
    var fileUrl : String? = ""

    @ManyToMany( mappedBy = "urls")
    var verification : List<Verification> = mutableListOf<Verification>()



    companion object {
        var temp : Long = 0
    }
    init {
        id = temp++
    }



}
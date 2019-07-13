package com.example.bucket.model

import com.example.bucket.repo.VerificationRepo
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class Verification {

    @Id @GeneratedValue( strategy = GenerationType.IDENTITY)
    var id : Long = 0

    @NotNull
    var verifiedUser : String = ""

    @NotNull
    var  canRead : Boolean = false

    @NotNull
    var canWrite : Boolean = false

    @ManyToMany
    var urls : List<url> = mutableListOf<url>()

}
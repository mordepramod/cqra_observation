package com.example.observationapp.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ViewMaterialModel")
data class ViewMaterialModel(
    @PrimaryKey(autoGenerate = true)
    var imagesList: Int,
    var loggedInUserId: String,
    var submitDate: String,
    var uploadDone: String,

    ){
    constructor() : this(
        0, "", "", "",

    )
}





package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables

@Entity(tableName = ApplicationDBTables.TABLE_USER)
data class UserModel(
    val access_level: String,
    val contact: String,
    val created_date: String,
    val developer_id: String,
    val email: String,
    val first_name: String,
    val last_name: String,
    val password: String,
    val project_id: String,
    @PrimaryKey
    val user_id: String,
    val user_type: String
) {
    override fun toString(): String {
        return "UserModel(access_level='$access_level', contact='$contact', created_date='$created_date', developer_id='$developer_id', email='$email', first_name='$first_name', last_name='$last_name', password='$password', project_id='$project_id', user_id='$user_id', user_type='$user_type')"
    }
}
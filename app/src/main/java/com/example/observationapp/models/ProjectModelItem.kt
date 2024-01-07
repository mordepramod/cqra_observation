package com.example.observationapp.models

data class ProjectModelItem(
    val address: String,
    val contact_number: String,
    val created_date: String,
    val developer_id: String,
    val email_id: String,
    val end_date: String,
    val gst_number: String,
    val message: String,
    val mr_name: String,
    val project_id: String,
    val project_location: String,
    val project_name: String,
    val project_type: String,
    val start_date: String,
    val status: String,
    val success: String,
    val structureModel: List<StructureModel>
) {
    override fun toString(): String {
        return "ProjectModelItem(success:$success, message:$message, address='$address', contact_number='$contact_number', created_date='$created_date', developer_id='$developer_id', email_id='$email_id', end_date='$end_date', gst_number='$gst_number', mr_name='$mr_name', project_id='$project_id', project_location='$project_location', project_name='$project_name', project_type='$project_type', start_date='$start_date', status='$status', structureModel=$structureModel)"
    }
}
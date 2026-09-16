package com.example.data

import kotlinx.coroutines.flow.Flow

class ResumeRepository(private val resumeDao: ResumeDao) {
    val allResumes: Flow<List<ResumeEntity>> = resumeDao.getAllResumes()

    fun getResume(id: String): Flow<ResumeEntity?> = resumeDao.getResumeById(id)

    suspend fun insert(resume: ResumeEntity) = resumeDao.insertResume(resume)

    suspend fun deleteById(id: String) = resumeDao.deleteResumeById(id)
}

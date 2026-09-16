with open('app/src/main/java/com/example/data/ResumeEntity.kt', 'r') as f:
    code = f.read()

new_code = """package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.model.ResumeData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@Entity(tableName = "resumes")
data class ResumeEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val lastModified: Long,
    val resumeDataJson: String
)

class ResumeConverters {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
        
    private val resumeDataAdapter = moshi.adapter(ResumeData::class.java)

    @TypeConverter
    fun fromResumeData(data: ResumeData): String = resumeDataAdapter.toJson(data)

    @TypeConverter
    fun toResumeData(json: String): ResumeData = resumeDataAdapter.fromJson(json) ?: ResumeData()
}

fun ResumeEntity.toResumeData(): ResumeData {
    val converters = ResumeConverters()
    return converters.toResumeData(this.resumeDataJson)
}

fun ResumeData.toEntity(): ResumeEntity {
    val converters = ResumeConverters()
    return ResumeEntity(
        id = this.id,
        title = this.title,
        lastModified = this.lastModified,
        resumeDataJson = converters.fromResumeData(this)
    )
}
"""

with open('app/src/main/java/com/example/data/ResumeEntity.kt', 'w') as f:
    f.write(new_code)

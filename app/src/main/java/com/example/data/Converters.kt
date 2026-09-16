package com.example.data

import androidx.room.TypeConverter
import com.example.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val resumeSettingsAdapter = moshi.adapter(ResumeSettings::class.java)
    private val personalInfoAdapter = moshi.adapter(PersonalInfo::class.java)
    
    private val stringListAdapter = moshi.adapter<List<String>>(
        Types.newParameterizedType(List::class.java, String::class.java)
    )
    
    private val stringSetAdapter = moshi.adapter<Set<String>>(
        Types.newParameterizedType(Set::class.java, String::class.java)
    )

    private val experienceListAdapter = moshi.adapter<List<ExperienceItem>>(
        Types.newParameterizedType(List::class.java, ExperienceItem::class.java)
    )

    private val educationListAdapter = moshi.adapter<List<EducationItem>>(
        Types.newParameterizedType(List::class.java, EducationItem::class.java)
    )

    // Example of handling complex fields as JSON strings
    @TypeConverter
    fun fromResumeSettings(settings: ResumeSettings): String = resumeSettingsAdapter.toJson(settings)
    @TypeConverter
    fun toResumeSettings(json: String): ResumeSettings = resumeSettingsAdapter.fromJson(json) ?: ResumeSettings()

    @TypeConverter
    fun fromPersonalInfo(info: PersonalInfo): String = personalInfoAdapter.toJson(info)
    @TypeConverter
    fun toPersonalInfo(json: String): PersonalInfo = personalInfoAdapter.fromJson(json) ?: PersonalInfo()

    @TypeConverter
    fun fromExperienceList(list: List<ExperienceItem>): String = experienceListAdapter.toJson(list)
    @TypeConverter
    fun toExperienceList(json: String): List<ExperienceItem> = experienceListAdapter.fromJson(json) ?: emptyList()

    @TypeConverter
    fun fromEducationList(list: List<EducationItem>): String = educationListAdapter.toJson(list)
    @TypeConverter
    fun toEducationList(json: String): List<EducationItem> = educationListAdapter.fromJson(json) ?: emptyList()

    @TypeConverter
    fun fromStringList(list: List<String>): String = stringListAdapter.toJson(list)
    @TypeConverter
    fun toStringList(json: String): List<String> = stringListAdapter.fromJson(json) ?: emptyList()

    @TypeConverter
    fun fromStringSet(set: Set<String>): String = stringSetAdapter.toJson(set)
    @TypeConverter
    fun toStringSet(json: String): Set<String> = stringSetAdapter.fromJson(json) ?: emptySet()
}

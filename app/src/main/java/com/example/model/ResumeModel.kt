package com.example.model

import java.util.UUID

enum class PageSize(val displayName: String, val widthPt: Int, val heightPt: Int) {
  A4("A4 (210 × 297 mm)", 595, 842),
  LETTER("US Letter (8.5 × 11 in)", 612, 792)
}

enum class SpacingDensity(val displayName: String, val scaleFactor: Float) {
  COMPACT("Compact", 0.85f),
  BALANCED("Balanced", 1.0f),
  COMFORTABLE("Comfortable", 1.15f)
}

enum class SkillDisplayType {
  TEXT,
  DOTS,
  BARS
}

enum class TemplateCategory(val displayName: String) {
  ALL("All Templates"),
  ATS("ATS / Corporate"),
  MINIMAL("Minimal & Swiss"),
  CREATIVE("Creative & Design"),
  STUDENT("Student / Fresher"),
  EXECUTIVE("Executive & Leadership"),
  GLOBAL("International & Global")
}

data class ResumeData(
  val id: String = UUID.randomUUID().toString(),
  val title: String = "My Professional CV",
  val lastModified: Long = System.currentTimeMillis(),
  val templateId: String = "ats_classic",
  val settings: ResumeSettings = ResumeSettings(),
  val personalInfo: PersonalInfo = PersonalInfo(),
  val experiences: List<ExperienceItem> = emptyList(),
  val educations: List<EducationItem> = emptyList(),
  val skills: List<SkillItem> = emptyList(),
  val projects: List<ProjectItem> = emptyList(),
  val certifications: List<CertificationItem> = emptyList(),
  val courses: List<CourseItem> = emptyList(),
  val languages: List<LanguageItem> = emptyList(),
  val awards: List<AwardItem> = emptyList(),
  val achievements: List<AchievementItem> = emptyList(),
  val volunteerWorks: List<VolunteerItem> = emptyList(),
  val internships: List<InternshipItem> = emptyList(),
  val publications: List<PublicationItem> = emptyList(),
  val organizations: List<OrganizationItem> = emptyList(),
  val references: List<ReferenceItem> = emptyList(),
  val interests: List<InterestItem> = emptyList(),
  val socialLinks: List<SocialLinkItem> = emptyList(),
  val customSections: List<CustomSectionItem> = emptyList(),
  val hiddenSections: Set<String> = emptySet(),
  val sectionOrder: List<String> = listOf(
    "personal", "summary", "experience", "education", "skills",
    "projects", "certifications", "languages", "custom"
  )
)

data class ResumeSettings(
  val pageSize: PageSize = PageSize.A4,
  val spacing: SpacingDensity = SpacingDensity.BALANCED,
  val primaryColorHex: String = "#72232B", // Careeré Burgundy default
  val fontChoice: String = "Inter / Swiss Sans",
  val showPhoto: Boolean = false,
  val photoShape: String = "Circle" // Circle, Rounded, Square
)

data class PersonalInfo(
  val fullName: String = "",
  val professionalTitle: String = "",
  val email: String = "",
  val phone: String = "",
  val city: String = "",
  val country: String = "",
  val linkedinUrl: String = "",
  val githubUrl: String = "",
  val portfolioUrl: String = "",
  val websiteUrl: String = "",
  val photoUri: String? = null,
  val summary: String = "",
  val isObjective: Boolean = false
)

data class ExperienceItem(
  val id: String = UUID.randomUUID().toString(),
  val jobTitle: String = "",
  val company: String = "",
  val location: String = "",
  val employmentType: String = "Full-time",
  val startDate: String = "",
  val endDate: String = "",
  val isCurrent: Boolean = false,
  val bullets: List<String> = emptyList()
)

data class EducationItem(
  val id: String = UUID.randomUUID().toString(),
  val degree: String = "",
  val fieldOfStudy: String = "",
  val institution: String = "",
  val location: String = "",
  val startDate: String = "",
  val endDate: String = "",
  val isCurrent: Boolean = false,
  val gpa: String = "",
  val coursework: String = "",
  val bullets: List<String> = emptyList()
)

data class SkillItem(
  val id: String = UUID.randomUUID().toString(),
  val name: String = "",
  val category: String = "Core Competencies",
  val proficiencyLevel: Int = 4, // 1 to 5
  val displayType: SkillDisplayType = SkillDisplayType.TEXT
)

data class ProjectItem(
  val id: String = UUID.randomUUID().toString(),
  val name: String = "",
  val role: String = "",
  val date: String = "",
  val description: String = "",
  val technologies: String = "",
  val projectUrl: String = "",
  val githubUrl: String = "",
  val bullets: List<String> = emptyList()
)

data class CertificationItem(
  val id: String = UUID.randomUUID().toString(),
  val name: String = "",
  val issuingOrg: String = "",
  val issueDate: String = "",
  val expiryDate: String = "",
  val credentialId: String = "",
  val credentialUrl: String = ""
)

data class CourseItem(
  val id: String = UUID.randomUUID().toString(),
  val courseName: String = "",
  val platform: String = "",
  val date: String = "",
  val description: String = ""
)

data class LanguageItem(
  val id: String = UUID.randomUUID().toString(),
  val language: String = "",
  val proficiency: String = "Professional Working" // Native, Fluent, Advanced, Intermediate, Beginner
)

data class AwardItem(
  val id: String = UUID.randomUUID().toString(),
  val title: String = "",
  val organization: String = "",
  val date: String = "",
  val description: String = ""
)

data class AchievementItem(
  val id: String = UUID.randomUUID().toString(),
  val title: String = "",
  val date: String = "",
  val description: String = ""
)

data class VolunteerItem(
  val id: String = UUID.randomUUID().toString(),
  val role: String = "",
  val organization: String = "",
  val location: String = "",
  val startDate: String = "",
  val endDate: String = "",
  val description: String = ""
)

data class InternshipItem(
  val id: String = UUID.randomUUID().toString(),
  val position: String = "",
  val company: String = "",
  val location: String = "",
  val startDate: String = "",
  val endDate: String = "",
  val description: String = "",
  val achievements: List<String> = emptyList()
)

data class PublicationItem(
  val id: String = UUID.randomUUID().toString(),
  val title: String = "",
  val publication: String = "",
  val date: String = "",
  val authors: String = "",
  val url: String = "",
  val description: String = ""
)

data class OrganizationItem(
  val id: String = UUID.randomUUID().toString(),
  val organization: String = "",
  val position: String = "",
  val startDate: String = "",
  val endDate: String = "",
  val description: String = ""
)

data class ReferenceItem(
  val id: String = UUID.randomUUID().toString(),
  val name: String = "",
  val position: String = "",
  val organization: String = "",
  val email: String = "",
  val phone: String = "",
  val availableOnRequest: Boolean = false
)

data class InterestItem(
  val id: String = UUID.randomUUID().toString(),
  val name: String = "",
  val description: String = ""
)

data class SocialLinkItem(
  val id: String = UUID.randomUUID().toString(),
  val platform: String = "LinkedIn",
  val url: String = ""
)

data class CustomSectionItem(
  val id: String = UUID.randomUUID().toString(),
  val sectionTitle: String = "Key Initiatives",
  val entries: List<CustomEntry> = emptyList()
)

data class CustomEntry(
  val id: String = UUID.randomUUID().toString(),
  val title: String = "",
  val subtitle: String = "",
  val date: String = "",
  val description: String = ""
)

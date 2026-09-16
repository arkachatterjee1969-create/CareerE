package com.example.model

object SampleProfiles {

  val softwareEngineer = ResumeData(
    title = "Senior Software Engineer CV",
    templateId = "ats_modern",
    personalInfo = PersonalInfo(
      fullName = "Alexandre Laurent",
      professionalTitle = "Staff Systems & Mobile Engineer",
      email = "alexandre.laurent@example.com",
      phone = "+33 6 12 34 56 78",
      city = "Paris",
      country = "France",
      linkedinUrl = "linkedin.com/in/alexandre-laurent",
      githubUrl = "github.com/alaurent-systems",
      portfolioUrl = "laurent.dev",
      summary = "Distinguished systems engineer with 8+ years architecting high-concurrency distributed platforms and reactive mobile applications. Proven track record reducing API latency by 42% and leading engineering squads across EMEA."
    ),
    experiences = listOf(
      ExperienceItem(
        jobTitle = "Staff Software Architect",
        company = "Vesper Systems EMEA",
        location = "Paris, France",
        startDate = "Jan 2022",
        endDate = "Present",
        isCurrent = true,
        bullets = listOf(
          "Spearheaded distributed streaming infrastructure handling 180M+ daily events with 99.995% uptime SLA.",
          "Refactored mission-critical client caching pipeline, reducing mobile data consumption by 34% and crash rates to <0.01%.",
          "Mentored 14 senior engineers across distributed teams, standardizing Kotlin multiplatform and architectural design reviews."
        )
      ),
      ExperienceItem(
        jobTitle = "Senior Backend Engineer",
        company = "Quantex Financial",
        location = "Geneva, Switzerland",
        startDate = "Sep 2018",
        endDate = "Dec 2021",
        isCurrent = false,
        bullets = listOf(
          "Engineered low-latency order execution engine in Kotlin & Go, reducing transaction execution roundtrip from 120ms to 18ms.",
          "Designed automated regression pipeline cutting deployment cycle time from 4 days to 45 minutes.",
          "Secured ISO 27001 and SOC2 compliance audits without a single blocking security finding."
        )
      )
    ),
    educations = listOf(
      EducationItem(
        degree = "Master of Science in Computer Engineering",
        fieldOfStudy = "Distributed Systems & Security",
        institution = "École Polytechnique",
        location = "Palaiseau, France",
        startDate = "2016",
        endDate = "2018",
        gpa = "Honors (Highest Distinction)",
        coursework = "Advanced Distributed Algorithms, Cryptography, Mobile Architectures"
      ),
      EducationItem(
        degree = "Bachelor of Science",
        fieldOfStudy = "Computer Science & Mathematics",
        institution = "Sorbonne University",
        location = "Paris, France",
        startDate = "2013",
        endDate = "2016",
        gpa = "Magna Cum Laude"
      )
    ),
    skills = listOf(
      SkillItem(name = "Kotlin / Android Compose", category = "Mobile & Systems", proficiencyLevel = 5),
      SkillItem(name = "Distributed Architecture", category = "Mobile & Systems", proficiencyLevel = 5),
      SkillItem(name = "Go & High Concurrency", category = "Backend", proficiencyLevel = 4),
      SkillItem(name = "PostgreSQL & SQLite", category = "Databases", proficiencyLevel = 5),
      SkillItem(name = "Kafka / Event Streams", category = "Infrastructure", proficiencyLevel = 4),
      SkillItem(name = "Docker & Kubernetes", category = "Infrastructure", proficiencyLevel = 4),
      SkillItem(name = "CI/CD & DevSecOps", category = "DevOps", proficiencyLevel = 5),
      SkillItem(name = "System Performance Tuning", category = "Engineering", proficiencyLevel = 5)
    ),
    projects = listOf(
      ProjectItem(
        name = "Hydra Reactive Stream Engine",
        role = "Creator & Lead Maintainer",
        date = "2023 - 2024",
        description = "Open-source zero-allocation streaming event dispatcher achieving 2.8M operations/sec benchmark throughput.",
        technologies = "Kotlin, Coroutines, Bytecode Instrumentation",
        projectUrl = "https://github.com/hydra-core"
      )
    ),
    certifications = listOf(
      CertificationItem(
        name = "AWS Certified Solutions Architect – Professional",
        issuingOrg = "Amazon Web Services",
        issueDate = "2023",
        expiryDate = "2026"
      )
    ),
    languages = listOf(
      LanguageItem(language = "French", proficiency = "Native"),
      LanguageItem(language = "English", proficiency = "Bilingual / Fluent (C2)"),
      LanguageItem(language = "German", proficiency = "Professional Working (B2)")
    )
  )

  val studentFresher = ResumeData(
    title = "Student & Graduate Entry CV",
    templateId = "student_ats",
    personalInfo = PersonalInfo(
      fullName = "Camille Moreau",
      professionalTitle = "Junior Financial Analyst & Economics Graduate",
      email = "camille.moreau@univ-paris.fr",
      phone = "+33 7 98 76 54 32",
      city = "Lyon",
      country = "France",
      linkedinUrl = "linkedin.com/in/camille-moreau-analyst",
      summary = "High-achieving Finance graduate with distinction in Quantitative Macroeconomics. Experienced in financial modeling, discounted cash flow (DCF) valuation, and statistical regression in Python/R. Seeking an entry-level analyst position."
    ),
    educations = listOf(
      EducationItem(
        degree = "B.Sc. in Applied Economics & Corporate Finance",
        fieldOfStudy = "Corporate Finance & Econometrics",
        institution = "Université Lumière Lyon 2",
        location = "Lyon, France",
        startDate = "2021",
        endDate = "2024",
        gpa = "3.85 / 4.0 (Top 5% of class)",
        coursework = "Financial Valuation, Econometrics, Portfolio Theory, Derivatives, Corporate Accounting"
      )
    ),
    internships = listOf(
      InternshipItem(
        position = "M&A Research & Valuation Intern",
        company = "Alliance Capital Partners",
        location = "Lyon, France",
        startDate = "May 2023",
        endDate = "Aug 2023",
        description = "Supported cross-border mid-market acquisition mandates across renewable energy and logistics sectors.",
        achievements = listOf(
          "Built three 3-statement DCF models used in final investment committee presentations for a €18M buyout mandate.",
          "Synthesized competitive market landscape briefs covering 45+ European clean-tech targets."
        )
      )
    ),
    projects = listOf(
      ProjectItem(
        name = "European Clean Energy Index Valuation",
        role = "Lead Capstone Researcher",
        date = "2024",
        description = "Conducted regression analysis on yield elasticity and carbon pricing impacts across 60 EU utilities using Python pandas & statsmodels."
      )
    ),
    skills = listOf(
      SkillItem(name = "Financial Modeling & DCF", category = "Finance", proficiencyLevel = 4),
      SkillItem(name = "Excel / VBA / Advanced Lookup", category = "Tools", proficiencyLevel = 5),
      SkillItem(name = "Python (Pandas, NumPy)", category = "Analytics", proficiencyLevel = 4),
      SkillItem(name = "Bloomberg Terminal & FactSet", category = "Finance", proficiencyLevel = 3),
      SkillItem(name = "PowerBI & Data Visuals", category = "Reporting", proficiencyLevel = 4)
    ),
    languages = listOf(
      LanguageItem(language = "French", proficiency = "Native"),
      LanguageItem(language = "English", proficiency = "Fluent (IELTS 8.0)"),
      LanguageItem(language = "Spanish", proficiency = "Intermediate (B1)")
    )
  )

  val executiveLeader = ResumeData(
    title = "Executive Vice President CV",
    templateId = "executive_director",
    personalInfo = PersonalInfo(
      fullName = "Elena von Berg",
      professionalTitle = "Chief Operating Officer & Enterprise Transformation Leader",
      email = "elena.vonberg@strategy-exec.com",
      phone = "+41 22 555 0192",
      city = "Zurich",
      country = "Switzerland",
      linkedinUrl = "linkedin.com/in/elena-vonberg-exec",
      summary = "Visionary P&L executive with 16+ years scaling multi-region operations across EMEA and North America. Led $240M operational budget, 600+ personnel, and orchestrated 3 post-merger integration programs generating $38M in annualized EBITDA synergies."
    ),
    experiences = listOf(
      ExperienceItem(
        jobTitle = "Executive Vice President of Operations",
        company = "AlpenGroup Global Logistics",
        location = "Zurich, Switzerland",
        startDate = "2019",
        endDate = "Present",
        isCurrent = true,
        bullets = listOf(
          "Full P&L responsibility for $240M division spanning Switzerland, Germany, Austria, and the UK.",
          "Transformed fragmented regional warehousing network into unified automated fulfillment hubs, lifting operating margins by 480 bps.",
          "Architected digital supply chain governance council, cutting inventory carrying costs by $22M annually."
        )
      ),
      ExperienceItem(
        jobTitle = "Managing Director, Enterprise Performance",
        company = "Strata Consulting Partners",
        location = "Frankfurt, Germany",
        startDate = "2013",
        endDate = "2019",
        isCurrent = false,
        bullets = listOf(
          "Led restructuring and operational turnarounds for 12 DAX-listed manufacturing and aerospace clients.",
          "Managed partner-track teams of 45 management consultants, consistently ranking in the 98th percentile for client retention."
        )
      )
    ),
    educations = listOf(
      EducationItem(
        degree = "Executive MBA",
        fieldOfStudy = "Global Strategic Leadership",
        institution = "INSEAD",
        location = "Fontainebleau, France",
        startDate = "2012",
        endDate = "2013"
      ),
      EducationItem(
        degree = "Diplom-Ingenieur (M.Sc.)",
        fieldOfStudy = "Industrial Engineering",
        institution = "ETH Zurich",
        location = "Zurich, Switzerland",
        startDate = "2003",
        endDate = "2008"
      )
    ),
    skills = listOf(
      SkillItem(name = "P&L Management ($200M+)", category = "Leadership", proficiencyLevel = 5),
      SkillItem(name = "Post-Merger Integration (M&A)", category = "Strategy", proficiencyLevel = 5),
      SkillItem(name = "Board Governance & Audit", category = "Governance", proficiencyLevel = 5),
      SkillItem(name = "Digital Transformation", category = "Operations", proficiencyLevel = 5),
      SkillItem(name = "Multi-Cultural Team Leadership", category = "Leadership", proficiencyLevel = 5)
    )
  )

  val allSampleProfiles = listOf(
    softwareEngineer,
    studentFresher,
    executiveLeader
  )
}

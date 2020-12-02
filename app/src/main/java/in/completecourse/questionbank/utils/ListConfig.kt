package `in`.completecourse.questionbank.utils

import `in`.completecourse.questionbank.R

object ListConfig {
    @JvmField
    val subjectIntermediateHindi = arrayOf("भौतिक विज्ञान", "रसायन विज्ञान", "गणित", "जीवविज्ञान")
    @JvmField
    val subjectIntermediateEnglsih = arrayOf("Physics", "Chemistry", "Mathematics", "Biology")
    @JvmField
    val subjectHighSchool = arrayOf("विज्ञान", "गणित", "Science", "Mathematics")
    @JvmField
    val imagesHighSchool = intArrayOf(R.drawable.ic_science, R.drawable.ic_home_mathematics)
    @JvmField
    val imagesIntermediate = intArrayOf(R.drawable.ic_home_physics, R.drawable.ic_home_chemistry, R.drawable.ic_home_mathematics, R.drawable.ic_home_biology)
}
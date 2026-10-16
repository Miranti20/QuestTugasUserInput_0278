package com.example.tugas5

/** Nilai form, terpisah dari UI supaya aturan validasi dapat diuji. */
data class Biodata(
    val namaDepan: String = "",
    val namaBelakang: String = "",
    val email: String = "",
    val tanggalLahir: Long? = null,
    val experienceIndex: Int = -1,
    val android: Boolean = false,
    val uiUx: Boolean = false,
    val web: Boolean = false
) {
    val emailValid: Boolean
        get() = EMAIL_PATTERN.matches(email.trim())

    fun isComplete(now: Long, experienceCount: Int): Boolean =
        namaDepan.isNotBlank() && namaBelakang.isNotBlank() && emailValid &&
            tanggalLahir != null && tanggalLahir <= now &&
            experienceIndex in 0 until experienceCount && (android || uiUx || web)

    private companion object {
        val EMAIL_PATTERN = Regex("^[A-Za-z0-9.!#\u0024%&'*+/=?^_`{|}~-]+@[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?(?:\\.[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?)+\u0024")
    }
}

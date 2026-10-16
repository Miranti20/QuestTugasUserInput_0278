package com.example.tugas5

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BiodataTest {
    private val now = 1_790_000_000_000L
    private val complete = Biodata("Ranti", "Mahasiswa", "ranti@example.com", now - 1, 0, android = true)

    @Test fun validFormCanProceed() {
        assertTrue(complete.isComplete(now, 3))
    }

    @Test fun emptyOrWhitespaceNamesCannotProceed() {
        assertFalse(Biodata().isComplete(now, 3))
        assertFalse(complete.copy(namaDepan = "  ").isComplete(now, 3))
        assertFalse(complete.copy(namaBelakang = " ").isComplete(now, 3))
    }

    @Test fun malformedEmailsCannotProceed() {
        listOf("", "ranti", "ranti@", "@example.com", "ranti@example", "ran ti@example.com", "ranti@-example.com").forEach {
            assertFalse(it, complete.copy(email = it).isComplete(now, 3))
        }
        assertTrue(complete.copy(email = "  ranti+ti@example.co.id  ").emailValid)
    }

    @Test fun dateMustBeSelectedAndNotInTheFuture() {
        assertFalse(complete.copy(tanggalLahir = null).isComplete(now, 3))
        assertFalse(complete.copy(tanggalLahir = now + 1).isComplete(now, 3))
        assertTrue(complete.copy(tanggalLahir = now).isComplete(now, 3))
    }

    @Test fun experienceMustBeOneOfTheAvailableOptions() {
        assertFalse(complete.copy(experienceIndex = -1).isComplete(now, 3))
        assertFalse(complete.copy(experienceIndex = 3).isComplete(now, 3))
        assertTrue(complete.copy(experienceIndex = 2).isComplete(now, 3))
    }

    @Test fun atLeastOneSkillIsRequired() {
        val noSkill = complete.copy(android = false)
        assertFalse(noSkill.isComplete(now, 3))
        assertTrue(noSkill.copy(uiUx = true).isComplete(now, 3))
        assertTrue(noSkill.copy(web = true).isComplete(now, 3))
        assertTrue(complete.copy(uiUx = true, web = true).isComplete(now, 3))
    }
}

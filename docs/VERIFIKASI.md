# Verifikasi proyek

Pengujian dilakukan pada 16 Oktober 2026 (UTC).

| Pemeriksaan | Hasil |
| --- | --- |
| Build APK debug | Berhasil |
| Build APK instrumentasi | Berhasil |
| Unit test `BiodataTest` | 6 test, 0 gagal |
| Uji perangkat `BiodataFormTest` | 1 test alur lengkap, 0 gagal; `OK (1 test)` |
| Android Lint | 0 error; 1 peringatan ketersediaan versi Compose BOM yang lebih baru |
| Build ulang setelah perubahan kode | Berhasil |
| Pemeriksaan XML resources dan whitespace diff | Berhasil |

Uji antarmuka dijalankan pada emulator **Android 9 / API 28**, resolusi 720 × 1520. Screenshot di folder `screenshots` berasal dari aplikasi yang benar-benar berjalan pada emulator tersebut. Status bar, tanggal contoh, dan navigasi sistem mengikuti konfigurasi emulator.

Alur yang diperiksa: Proceed nonaktif saat form kosong; nama diisi; email salah ditolak; email diperbaiki; tanggal dipilih melalui kalender; experience dipilih melalui dropdown; dua skill dipilih; Activity dibuat ulang; seluruh input dan pilihan tetap tersedia; Proceed membuka ringkasan; dialog ditutup; seluruh skill dihapus dan Proceed kembali nonaktif.

Build memakai konfigurasi versi bawaan repository: AGP 9.4.1, Gradle 9.6.0, Kotlin/Compose compiler 2.2.10, Compose BOM 2026.02.01, compile/target SDK 37, dan JDK 25. Kompilasi Kotlin incremental dinonaktifkan pada proyek kecil ini untuk menjaga konsistensi build ulang.

Perintah pemeriksaan:

```bash
./gradlew clean assembleDebug testDebugUnitTest lintDebug assembleDebugAndroidTest
./gradlew assembleDebug testDebugUnitTest lintDebug assembleDebugAndroidTest
```

Uji perangkat dijalankan memakai AndroidJUnitRunner melalui ADB, setara dengan menjalankan `connectedDebugAndroidTest` pada perangkat aktif:

```bash
adb shell am instrument -w -r -e class com.example.tugas5.BiodataFormTest com.example.tugas5.test/androidx.test.runner.AndroidJUnitRunner
```

Peringatan lint mengenai versi Compose BOM tidak menghalangi build. Versi yang sudah dipakai repository dipertahankan.

SHA-256 APK `FormBiodata-debug.apk` dalam paket hasil:

```text
e8f0472d4a78d05ac70bec271f8097cca8217b85b3f08eae5af74638d5b34418
```

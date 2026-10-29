# TP 6 DPBO — Flappy Alien (Game Klon Flappy Bird)

Tugas Praktikum 6 Mata Kuliah **Desain dan Pemrograman Berorientasi Objek (DPBO)**.

Program ini adalah klon game Flappy Bird dengan tema alien menggunakan Java Swing. `App` sebagai titik masuk, `Logic` untuk mekanika game & input, `View` untuk render, dan `Player`/`Pipe` sebagai model data. Pemain menekan SPACE untuk melompat, menghindari pipa, dan mencetak skor tertinggi. Game dilengkapi musik latar (BGM) dan efek suara.

## Janji

Saya Nabila Attaya Putri Cahyadi dengan NIM 2508355 mengerjakan Tugas Praktikum 6 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin

## Struktur Folder

```text
TP6DPBO2526C1/
├── README.md
├── TP6DPBO2526C1.iml
├── FlappyBird/
│   └── src/
│       ├── App.java
│       ├── Logic.java
│       ├── Pipe.java
│       ├── Player.java
│       └── View.java
│       ├── assets/
│       │   ├── background.png
│       │   ├── bird.png
│       │   ├── charFall.png
│       │   ├── charJump.png
│       │   ├── lowerPipe.png
│       │   └── upperPipe.png
│       ├── fonts/
│       │   ├── MatchaWorld.ttf
│       │   └── MatchaWorld.otf
│       └── sounds/
│           ├── garden.wav
│           ├── sfx_coin.wav
│           ├── sfx_disappear.wav
│           └── sfx_jump.wav
└── Documentation/
    └── gameplay-demo.mp4
```

## Desain / Fungsi Program

**Tema:** Flappy Alien — klon Flappy Bird dengan karakter alien, latar biru muda (cyan), pipa penghalang, font custom `MatchaWorld.ttf` dengan teks berlapis (outline), serta BGM dan efek suara.

**File program:**

- `FlappyBird/src/App.java` — titik masuk: membuat `JFrame` judul `Flappy Alien`, ukuran `360x640`, tidak bisa diubah ukurannya (non-resizable), memasang `View`, dan menampilkan window.
- `FlappyBird/src/Logic.java` — `ActionListener` + `KeyListener`: loop game 60 FPS, gravitasi, gerakan pipa, detektasi tabrakan, skor, restart, serta pemutaran suara (jump/coin/game-over/BGM).
- `FlappyBird/src/Player.java` — model pemain: posisi, ukuran, gambar, dan kecepatan vertikal.
- `FlappyBird/src/Pipe.java` — model pipa penghalang: posisi, ukuran, gambar, kecepatan, dan flag `passed`.
- `FlappyBird/src/View.java` — `JPanel` render: gambar background, pemain, semua pipa, layar menu awal dan layar game over, label skor realtime, serta font custom `MatchaWorld.ttf`.
- `FlappyBird/src/assets/` — 6 gambar game: `background.png`, `bird.png`, `charJump.png`, `charFall.png`, `lowerPipe.png`, `upperPipe.png`.
- `FlappyBird/src/fonts/MatchaWorld.ttf` — font custom untuk teks UI game (terdapat juga versi `.otf`).
- `FlappyBird/src/sounds/` — 4 file audio: BGM `garden.wav`, SFX `sfx_jump.wav`, `sfx_coin.wav`, `sfx_disappear.wav`.
- `Documentation/gameplay-demo.mp4` — rekaman gameplay program.

**Fungsi tiap komponen:**

- `App.main` — inisialisasi window dan memulai game.
- `Logic` — timer game loop 60 FPS + timer spawn pipa setiap 1500 ms; gravitasi `1`; tombol SPACE untuk melompat (`velocityY = -10`); restart dengan `R`, keluar dengan `Q`.
- `Player` — sprite berubah antara `charJump` (naik) dan `charFall` (turun); hitbox diperkecil sedikit agar near-miss secara visual tidak langsung menyebabkan game over.
- `Pipe` — pasangan pipa atas dan bawah dengan jarak celah `220`, posisi vertikal acak, bergerak ke kiri dengan `velocityX = -2`.
- `View` — render semua elemen game; layar awal menampilkan "FLAPPY ALIEN", "Press SPACE to start", "Press Q to quit"; layar game over menampilkan "GAME OVER", "Press R to restart", "Press Q to quit"; label skor di pojok kiri atas diperbarui realtime.
- Skor bertambah 1 setiap kali pemain melewati celah pipa (tandai `passed`), diputar suara koin.

## Method Program

| Method | Fungsi |
|---|---|
| `App.main` | Titik masuk: buat `JFrame("Flappy Alien")`, ukuran `360x640`, `EXIT_ON_CLOSE`, non-resizable, center layar, pasang `View`, panggil `pack()` dan tampilkan window. |
| `Logic()` | Muat semua gambar & suara, buat objek `Player`, mulai timer spawn pipa (1500 ms) dan game loop (60 FPS), putar BGM loop terus-menerus. |
| `Logic.placePipes()` | Buat pasangan pipa atas/bawah pada posisi vertikal acak dengan jarak celah 220, lalu tambahkan ke daftar pipa. |
| `Logic.loadSounds()` / `getClip()` | Muat 4 klip audio (`garden.wav`, `sfx_jump.wav`, `sfx_coin.wav`, `sfx_disappear.wav`) via `AudioSystem` dari resource. |
| `Logic.playSound()` / `playBGM()` | Putar efek suara dari awal (`setFramePosition(0)`) / loop BGM terus-menerus (`Clip.LOOP_CONTINUOUSLY`). |
| `Logic.actionPerformed()` | Dipanggil tiap frame oleh game loop timer: panggil `move()` lalu `view.repaint()`. |
| `Logic.move()` | Terapkan gravitasi, perbarui posisi pemain dan pipa, periksa tabrakan pemain-pipa (hitbox diperkecil), periksa pemain mengenai layar bawah, tambah skor saat melewati pipa, hentikan game saat game over. |
| `Logic.keyPressed()` | SPACE: mulai game saat belum mulai / lompat saat bermain; `R`: restart saat game over; `Q`: keluar program. |
| `Logic.restartGame()` | Reset skor dan posisi pemain, hapus semua pipa, hentikan timer lama, buat timer baru, spawn sepasang pipa, putar BGM ulang. |
| `View()` | Inisialisasi `JPanel` ukuran `360x640` background cyan, label skor, panel focusable, dan pasang key listener ke `Logic`. |
| `View.draw()` | Gambar background, pemain, semua pipa; gambar layar menu atau layar game over dengan teks berlapis; sinkronkan teks label skor. |
| `View.drawOutlinedString()` | Gambar teks putih dengan outline tebal (warna `72, 36, 92`) di sekitar teks agar kontras terhadap latar. |
| `Player` / `Pipe` | Getter dan setter untuk posisi, ukuran, gambar, dan kecepatan; `Player` juga menyimpan `velocityY` dan `image` saat ini. |

## Penjelasan Alur

1. Program dijalankan: `App.main` membuat window `Flappy Alien`, memasang `View`, dan `Logic` memuat aset, memulai timer, serta memutar BGM.
2. **Menu awal:** layar menampilkan "FLAPPY ALIEN", "Press SPACE to start", dan "Press Q to quit". Program menunggu input pemain.
3. **Mulai game:** tekan `SPACE` -> `gameStarted = true`, daftar pipa dibersihkan dan dipasang ulang, timer spawn di-restart agar pipa berikutnya muncul setelah jeda penuh.
4. **Bermain:** pemain jatuh ke bawah karena gravitasi; tekan `SPACE` untuk melompat ke atas (`velocityY = -10`). Semua pipa bergerak dari kanan ke kiri.
5. **Skor:** bertambah 1 setiap kali pemain melewati celah pipa (deteksi flag `passed` sekali per pasangan pipa), suara koin diputar, dan label skor di `View` diperbarui realtime.
6. **Game over:** pemain mengenai layar bawah atau menabrak pipa -> `gameOver = true`, kedua timer berhenti, suara game over diputar, BGM berhenti, dan layar "GAME OVER" ditampilkan.
7. **Restart / keluar:** saat game over, tekan `R` untuk memulai ulang game (`restartGame`) atau `Q` untuk keluar (`System.exit(0)`).
8. Semua aset (gambar, font, suara) dimuat dari `src/assets`, `src/fonts`, dan `src/sounds` sebagai resource aplikasi.

## Dokumentasi Saat Program Dijalankan
[Gameplay](https://github.com/Beilua/TP6DPBO2526C1/blob/main/Documentation/gameplay-demo.mp4)
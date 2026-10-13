package com.example.data.questions

import com.example.core.model.BadgeReward
import com.example.core.model.WorldInfo

object WorldCatalog {
    val worlds: List<WorldInfo> = listOf(
        WorldInfo(
            id = 1,
            name = "Hutan Angka",
            subtitle = "Mengenal Angka & Berhitung",
            description = "Ayo bantu Kiko menjelajah hutan pinus, menghitung biji kenari, daun, dan jamur ajaib!",
            primaryColor = 0xFF4CAF50, // Vibrant Leaf Green
            secondaryColor = 0xFF81C784,
            startLevel = 1,
            endLevel = 40,
            badgeTitle = "Penjelajah Hutan Angka"
        ),
        WorldInfo(
            id = 2,
            name = "Kebun Buah Ceria",
            subtitle = "Petualangan Buah Manis",
            description = "Petik apel merah, pisang manis, dan jeruk segar bersama Kiko di kebun penuh warna!",
            primaryColor = 0xFFFF9800, // Orange
            secondaryColor = 0xFFFFB74D,
            startLevel = 41,
            endLevel = 80,
            badgeTitle = "Petani Buah Cilik"
        ),
        WorldInfo(
            id = 3,
            name = "Ladang Sayuran",
            subtitle = "Panen Sayur Segar",
            description = "Kumpulkan wortel renyah, tomat merah, dan brokoli sehat untuk bekal petualangan!",
            primaryColor = 0xFF8BC34A, // Lime Green
            secondaryColor = 0xFFAED581,
            startLevel = 81,
            endLevel = 120,
            badgeTitle = "Sahabat Sayur Sehat"
        ),
        WorldInfo(
            id = 4,
            name = "Kerajaan Hewan",
            subtitle = "Sahabat Hewan Cerdas",
            description = "Temui kelinci lucu, burung bernyanyi, gajah baik hati, dan ikan yang riang!",
            primaryColor = 0xFF03A9F4, // Bright Sky Blue
            secondaryColor = 0xFF4FC3F7,
            startLevel = 121,
            endLevel = 160,
            badgeTitle = "Penyayang Satwa Cilik"
        ),
        WorldInfo(
            id = 5,
            name = "Hutan Tumbuhan Ajaib",
            subtitle = "Keajaiban Bunga & Alam",
            description = "Saksikan bunga matahari bermekaran dan pelajari pola pertumbuhan pohon ajaib!",
            primaryColor = 0xFF9C27B0, // Magic Purple / Violet
            secondaryColor = 0xFFBA68C8,
            startLevel = 161,
            endLevel = 200,
            badgeTitle = "Master Dunia Ceria"
        )
    )

    fun getWorldForLevel(levelNumber: Int): WorldInfo {
        return worlds.firstOrNull { levelNumber in it.startLevel..it.endLevel } ?: worlds.first()
    }

    val badges: List<BadgeReward> = listOf(
        BadgeReward("b_10", "Bintang Pemula", "Menyelesaikan 10 Level Pertama", 10, 1, "🌟"),
        BadgeReward("b_20", "Penghitung Cilik", "Menyelesaikan 20 Level", 20, 1, "🔢"),
        BadgeReward("b_30", "Sahabat Hutan", "Menyelesaikan 30 Level", 30, 1, "🌲"),
        BadgeReward("b_40", "Pahlawan Hutan Angka", "Menaklukkan Boss Hutan Angka!", 40, 1, "🏆"),

        BadgeReward("b_50", "Pemetik Apel", "Menyelesaikan 50 Level", 50, 2, "🍎"),
        BadgeReward("b_60", "Keranjang Buah", "Menyelesaikan 60 Level", 60, 2, "🍌"),
        BadgeReward("b_70", "Pakar Vitamin", "Menyelesaikan 70 Level", 70, 2, "🍊"),
        BadgeReward("b_80", "Raja Kebun Buah", "Menaklukkan Boss Kebun Buah!", 80, 2, "👑"),

        BadgeReward("b_90", "Pencari Wortel", "Menyelesaikan 90 Level", 90, 3, "🥕"),
        BadgeReward("b_100", "Sahabat Sayur 100", "Mencapai 100 Level Luar Biasa!", 100, 3, "🥦"),
        BadgeReward("b_110", "Panen Raya", "Menyelesaikan 110 Level", 110, 3, "🌽"),
        BadgeReward("b_120", "Pahlawan Ladang Sayur", "Menaklukkan Boss Ladang Sayur!", 120, 3, "🎖️"),

        BadgeReward("b_130", "Sahabat Kelinci", "Menyelesaikan 130 Level", 130, 4, "🐰"),
        BadgeReward("b_140", "Suara Rimba", "Menyelesaikan 140 Level", 140, 4, "🐘"),
        BadgeReward("b_150", "Penyelam Cilik", "Menyelesaikan 150 Level", 150, 4, "🐟"),
        BadgeReward("b_160", "Penjaga Rimba Hewan", "Menaklukkan Boss Kerajaan Hewan!", 160, 4, "🦁"),

        BadgeReward("b_170", "Bunga Mekar", "Menyelesaikan 170 Level", 170, 5, "🌻"),
        BadgeReward("b_180", "Pohon Kebijaksanaan", "Menyelesaikan 180 Level", 180, 5, "🌳"),
        BadgeReward("b_190", "Empat Daun Ajaib", "Menyelesaikan 190 Level", 190, 5, "🍀"),
        BadgeReward("b_200", "Sang Juara Dunia Ceria", "Menamatkan Seluruh 200 Level!", 200, 5, "🥇")
    )
}

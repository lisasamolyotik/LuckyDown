package com.down.lucky.model

class Level() {
    var score = 0
    var lifes = 4

    companion object {
        var time = 80000L
        var level: Int = 1
        var generationSpeed: Int = 1
        var fallingSpeed: Int = 1

        fun nextLevel() {
            level++
            if (level % 6 == 0) {
                generationSpeed = 1
                time = 80000L
                fallingSpeed++
            }
            if (generationSpeed < 4) {
                generationSpeed++
            }
            if (time > 30000L) {
                time -= 5000L
            }
        }
    }
}
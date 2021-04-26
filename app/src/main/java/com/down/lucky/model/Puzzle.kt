package com.down.lucky.model

import com.down.lucky.R

class Puzzle {
    companion object {
        private var index = if (Level.level < 30) {
            Level.level / 6
        } else {
            Level.level % 6
        }
        private val images = arrayOf(
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5
        )

        val image get() = images[index]
        var piece = Level.level % 6 - 1

        private fun nextImage() {
            if (index < 4) {
                index++
            } else {
                index = 0
            }
        }

        fun nextPiece() {
            if (piece < 5) {
                piece++
            } else {
                piece = 0
                nextImage()
            }
        }
    }
}
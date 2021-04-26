package com.down.lucky.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.graphics.drawable.toBitmap
import com.down.lucky.model.Puzzle
import com.down.lucky.model.PuzzlePiece

class PuzzleCreator(private val context: Context) {
    private var imageWidth: Int = 0
    private var imageHeight: Int = 0
    private val pieceWidth get() = imageWidth / 3
    private val pieceHeight get() = imageHeight / 2

    fun splitImage(image: ImageView) : List<PuzzlePiece> {
        imageWidth = image.width
        imageHeight = image.height

        val original = Bitmap.createScaledBitmap(
            image.drawable.toBitmap(),
            DimensionConverter.pixelsToDp(imageWidth, context),
            DimensionConverter.pixelsToDp(imageHeight, context),
            true
        )
        val pieces = mutableListOf<PuzzlePiece>()
        for (row in 0..1) {
            for (column in 0..2) {
                pieces.add(createPiece(row, column, original))
            }
        }
        return pieces
    }

    private fun createPiece(row: Int, column: Int, original: Bitmap): PuzzlePiece {
        val x = pieceWidth * column
        val y = pieceHeight * row
        val croppedBitmap = Bitmap.createBitmap(
            original,
            DimensionConverter.pixelsToDp(x, context),
            DimensionConverter.pixelsToDp(y, context),
            DimensionConverter.pixelsToDp(pieceWidth, context),
            DimensionConverter.pixelsToDp(pieceHeight, context)
        )
        val piece = PuzzlePiece(context)
        piece.setImageBitmap(croppedBitmap)
        piece.adjustViewBounds = true
        piece.xPos = x
        piece.yPos = y
        val index = row * 3 + column
        if (index > Puzzle.piece) {
            piece.alpha = 0f
        }
        return piece
    }
}
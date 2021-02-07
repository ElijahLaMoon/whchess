package whchess.syntax

import whchess.domain._
import Square._
import Piece._

import square._

object piece {
  implicit class PieceOps(piece: Piece) {

    lazy val initialSquares: Vector[Square] = piece match {

      case `White King`   => Vector(E1)
      case `Black King`   => Vector(E8)
      case `White Queen`  => Vector(D1)
      case `Black Queen`  => Vector(D8)
      case `White Bishop` => Vector(C1, F1)
      case `Black Bishop` => Vector(C8, F8)
      case `White Knight` => Vector(B1, G1)
      case `Black Knight` => Vector(B8, G8)
      case `White Rook`   => Vector(A1, H1)
      case `Black Rook`   => Vector(A8, H8)
      case `White Pawn`   => pawnSquares(A2)
      case `Black Pawn`   => pawnSquares(A7)
    }

    // private members
    private def pawnSquares(square: Square): Vector[Square] =
      (Square.values filter square.isOnSameRow).toVector
  }
}

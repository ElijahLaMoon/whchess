package whchess.syntax

import whchess.domain._
import Color._
import Square._
import PieceType._

import square._

object piece {
  implicit class PieceOps(piece: Piece) {
    def squares: Set[Square] = piece.color match {
      case White => piece.pType match {
        case King   => Set(E1)
        case Queen  => Set(D1)
        case Bishop => Set(C1, F1)
        case Knight => Set(B1, G1)
        case Rook   => Set(A1, H1)
        case Pawn   => Square.values.filter(A2.isOnSameRow).toSet
      }

      case Black => piece.pType match {
        case King   => Set(E8)
        case Queen  => Set(D8)
        case Bishop => Set(C8, F8)
        case Knight => Set(B8, G8)
        case Rook   => Set(A8, H8)
        case Pawn   => Square.values.filter(A7.isOnSameRow).toSet
      }
    }
  }
}

package whchess.domain

import enumeratum._

import Color._
import Square._
import PieceType._
import whchess.syntax.square._

sealed abstract class Piece(val color: Color, val pType: PieceType)
                           (val initialSquares: Square*) extends EnumEntry {

  lazy val symbol: Char = color match {
    case White => pType.toString.head
    case Black => pType.toString.head.toLower
  }
}

object Piece extends Enum[Piece] {

  lazy val whites: Map[Piece, Int] = pieces(White)
  lazy val blacks: Map[Piece, Int] = pieces(Black)

  case object `White King`   extends Piece(White, King)(E1)
  case object `Black King`   extends Piece(Black, King)(E8)
  case object `White Queen`  extends Piece(White, Queen)(D1)
  case object `Black Queen`  extends Piece(Black, Queen)(D8)
  case object `White Bishop` extends Piece(White, Bishop)(C1, F1)
  case object `Black Bishop` extends Piece(Black, Bishop)(C8, F8)
  case object `White Knight` extends Piece(White, Knight)(B1, G1)
  case object `Black Knight` extends Piece(Black, Knight)(B8, G8)
  case object `White Rook`   extends Piece(White, Rook)(A1, H1)
  case object `Black Rook`   extends Piece(Black, Rook)(A8, H8)
  case object `White Pawn`   extends Piece(White, Pawn)(pawnSquares(A2): _*)
  case object `Black Pawn`   extends Piece(Black, Pawn)(pawnSquares(A7): _*)

  lazy val values: IndexedSeq[Piece] = findValues

  // private members
  private def pawnSquares(square: Square): IndexedSeq[Square] =
    Square.values filter square.isOnSameRow

  private def pieces(color: Color): Map[Piece, Int] = {
    val filteredPieces = values.filter(_.color == color)

    filteredPieces.map(p => p.pType match {
      case King   => p -> 1
      case Queen  => p -> 1
      case Bishop => p -> 2
      case Knight => p -> 2
      case Rook   => p -> 2
      case Pawn   => p -> 8
    }).toMap
  }
}

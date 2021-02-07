package whchess.domain

import enumeratum._

import Color._
import PieceType._

sealed abstract class Piece(val color: Color, val pType: PieceType) extends EnumEntry {
  lazy val symbol: Char = color match {
    case White => pType.toString.head
    case Black => pType.toString.head.toLower
  }
}

object Piece extends Enum[Piece] {

  lazy val whites: Map[Piece, Int] = pieces(White)
  lazy val blacks: Map[Piece, Int] = pieces(Black)

  case object `White King`   extends Piece(White, King)
  case object `Black King`   extends Piece(Black, King)
  case object `White Queen`  extends Piece(White, Queen)
  case object `Black Queen`  extends Piece(Black, Queen)
  case object `White Bishop` extends Piece(White, Bishop)
  case object `Black Bishop` extends Piece(Black, Bishop)
  case object `White Knight` extends Piece(White, Knight)
  case object `Black Knight` extends Piece(Black, Knight)
  case object `White Rook`   extends Piece(White, Rook)
  case object `Black Rook`   extends Piece(Black, Rook)
  case object `White Pawn`   extends Piece(White, Pawn)
  case object `Black Pawn`   extends Piece(Black, Pawn)

  lazy val values: IndexedSeq[Piece] = findValues

  // private members
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

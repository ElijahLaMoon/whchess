package whchess.domain

import enumeratum._

import PieceType._

sealed abstract class Piece(val color: Color, val pType: PieceType) extends EnumEntry {
  lazy val symbol: Char = color match {
    case Color.White => pType.symbol
    case Color.Black => pType.symbol.toLower
  }
}

object Piece extends Enum[Piece] {

  lazy val whites: Map[White, Int] = pieces(White.values)
  lazy val blacks: Map[Black, Int] = pieces(Black.values)

  sealed abstract class White(override val pType: PieceType) extends Piece(Color.White, pType) with EnumEntry
  object White extends Enum[White] {

    case object `White King`   extends White(King)
    case object `White Queen`  extends White(Queen)
    case object `White Bishop` extends White(Bishop)
    case object `White Knight` extends White(Knight)
    case object `White Rook`   extends White(Rook)
    case object `White Pawn`   extends White(Pawn)

    lazy val values: IndexedSeq[White] = findValues
  }

  sealed abstract class Black(override val pType: PieceType) extends Piece(Color.Black, pType) with EnumEntry
  object Black extends Enum[Black] {

    case object `Black King`   extends Black(King)
    case object `Black Queen`  extends Black(Queen)
    case object `Black Bishop` extends Black(Bishop)
    case object `Black Knight` extends Black(Knight)
    case object `Black Rook`   extends Black(Rook)
    case object `Black Pawn`   extends Black(Pawn)

    lazy val values: IndexedSeq[Black] = findValues
  }

  lazy val values: IndexedSeq[Piece] = findValues

  // private members
  private def pieces[A <: Piece](enums: IndexedSeq[A]): Map[A, Int] = {
    enums.map(p => p.pType match {
      case King   => p -> 1
      case Queen  => p -> 1
      case Bishop => p -> 2
      case Knight => p -> 2
      case Rook   => p -> 2
      case Pawn   => p -> 8
    }).toMap
  }
}

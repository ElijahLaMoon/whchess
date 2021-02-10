package whchess.domain

import enumeratum._

sealed abstract class PieceType(val symbol: Char) extends EnumEntry

object PieceType extends Enum[PieceType] {

  case object King   extends PieceType('K')
  case object Queen  extends PieceType('Q')
  case object Bishop extends PieceType('B')
  case object Knight extends PieceType('N')
  case object Rook   extends PieceType('R')
  case object Pawn   extends PieceType('P')

  lazy val values: IndexedSeq[PieceType] = findValues
}
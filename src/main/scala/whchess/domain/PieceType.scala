package whchess.domain

import enumeratum._

sealed abstract class PieceType extends EnumEntry

object PieceType extends Enum[PieceType] {

  case object King   extends PieceType
  case object Queen  extends PieceType
  case object Bishop extends PieceType
  case object Knight extends PieceType
  case object Rook   extends PieceType
  case object Pawn   extends PieceType

  lazy val values: IndexedSeq[PieceType] = findValues
}
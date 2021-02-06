package whchess.domain

import enumeratum._

/** Represents board row */
sealed abstract class Rank(row: Int) extends EnumEntry {
  override def toString: String = s"$row"
}

object Rank extends Enum[Rank] {

  case object `1` extends Rank(1)
  case object `2` extends Rank(2)
  case object `3` extends Rank(3)
  case object `4` extends Rank(4)
  case object `5` extends Rank(5)
  case object `6` extends Rank(6)
  case object `7` extends Rank(7)
  case object `8` extends Rank(8)

  lazy val values: IndexedSeq[Rank] = findValues
}

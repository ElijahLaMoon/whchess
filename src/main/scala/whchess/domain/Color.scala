package whchess.domain

import enumeratum._

sealed trait Color extends EnumEntry

object Color extends Enum[Color] {
  case object White extends Color
  case object Black extends Color

  lazy val values: IndexedSeq[Color] = findValues
}
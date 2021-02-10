package whchess.domain

import enumeratum._

sealed abstract class Player(val color: Color) extends EnumEntry {
  override def toString: String = s"$color player"
}

object Player extends Enum[Player] {
  case object White extends Player(Color.White)
  case object Black extends Player(Color.Black)

  lazy val values: IndexedSeq[Player] = findValues
}

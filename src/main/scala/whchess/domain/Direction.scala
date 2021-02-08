package whchess.domain

import enumeratum._

sealed trait Direction extends EnumEntry

object Direction extends Enum[Direction] {

  sealed trait Straight extends Direction with EnumEntry
  object Straight extends Enum[Straight] {

    case object Up        extends Straight
    case object Down      extends Straight
    case object Left      extends Straight
    case object Right     extends Straight

    lazy val values: IndexedSeq[Straight] = findValues
  }

  sealed trait Diagonal extends Direction with EnumEntry
  object Diagonal extends Enum[Diagonal] {

    case object UpLeft    extends Diagonal
    case object UpRight   extends Diagonal
    case object DownLeft  extends Diagonal
    case object DownRight extends Diagonal

    lazy val values: IndexedSeq[Diagonal] = findValues
  }

  lazy val values: IndexedSeq[Direction] = findValues
}

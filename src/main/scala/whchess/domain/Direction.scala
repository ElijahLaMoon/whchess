package whchess.domain

import enumeratum._

sealed trait Direction extends EnumEntry

object Direction extends Enum[Direction] {

  sealed trait Straight extends Direction with EnumEntry
  object Straight extends Enum[Straight] {

    sealed trait Vertical extends Straight with EnumEntry
    object Vertical extends Enum[Vertical] {

      case object Up        extends Vertical
      case object Down      extends Vertical

      lazy val values: IndexedSeq[Vertical] = findValues
    }

    sealed trait Horizontal extends Straight with EnumEntry
    object Horizontal extends Enum[Horizontal] {

      case object Left      extends Horizontal
      case object Right     extends Horizontal

      lazy val values: IndexedSeq[Horizontal] = findValues
    }

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

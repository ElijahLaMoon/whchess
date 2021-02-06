package whchess.domain

import enumeratum._

/** Represents board column */
sealed abstract class File(column: Char) extends EnumEntry {
  override def toString: String = s"$column"
}

object File extends Enum[File] {

  case object A extends File('A')
  case object B extends File('B')
  case object C extends File('C')
  case object D extends File('D')
  case object E extends File('E')
  case object F extends File('F')
  case object G extends File('G')
  case object H extends File('H')

  lazy val values: IndexedSeq[File] = findValues
}

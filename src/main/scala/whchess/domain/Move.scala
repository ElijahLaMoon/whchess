package whchess.domain

case class Move(from: Square, to: Square) {
  override def toString: String = s"$from -> $to"
}

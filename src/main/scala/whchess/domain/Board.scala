package whchess.domain

import Piece._
import Board.Positions
import whchess.syntax.piece._

case class Board(current: Positions[Piece], playerInCharge: Player) {

case class Board private (current: Positions) {
  override def toString: String = current.map {
    case (square, op) => op match {
      case Some(piece) => s"$piece is on $square"
      case None        => s"$square is empty"
    }
  }.mkString(", ")
}

object Board {

  type Positions[A <: Piece] = Map[Square, Option[A]]

  lazy val initial: Board = Board(occupiedPositions ++ emptyPositions, Player.White)

  lazy val whitePositions: Positions[White] = squaresFor(Piece.White.values)
  lazy val blackPositions: Positions[Black] = squaresFor(Piece.Black.values)
  lazy val emptyPositions: Positions[Piece] = Square.values
    .diff(occupiedPositions.keys.toSeq)
    .map(_ -> None)
    .toMap

  // private members
  private lazy val occupiedPositions = whitePositions ++ blackPositions

  private def squaresFor[A <: Piece](coloredPieces: IndexedSeq[A]): Positions[A] = coloredPieces
    .flatMap(piece => piece.initialSquares.map(_ -> Option(piece)))
    .toMap
}

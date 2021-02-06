package whchess.domain

import Color._
import Board.Positions

import cats.data.Writer

case class Board private (current: Positions) {
  override def toString: String = current.map {
    case (square, op) => op match {
      case Some(piece) => s"$piece is on $square"
      case None        => s"$square is empty"
    }
  }.mkString(", ")
}

object Board {

  type Positions = Map[Square, Option[Piece]]

  lazy val initial = new Board(occupiedPositions ++ emptyPositions)

  // make moves and log the state of the board
  def mkMove(move: Move): Writer[List[String], Board] = ???
  def mkMoves(moves: Move*): Writer[List[String], Board] = ???

//  def apply(current: Positions): Board = new Board(current)

  lazy val whitePositions: Positions = squaresFor(White)
  lazy val blackPositions: Positions = squaresFor(Black)
  lazy val emptyPositions: Positions = Square.values
    .diff(occupiedPositions.keys.toSeq)
    .map(_ -> None)
    .toMap

  // private members
  private lazy val occupiedPositions = whitePositions ++ blackPositions

  private def squaresFor(color: Color): Positions = (color match {
      case White => Piece.whites
      case Black => Piece.blacks
    }).keys
    .flatMap(piece => piece.initialSquares.map(_ -> Option(piece)))
    .toMap
}

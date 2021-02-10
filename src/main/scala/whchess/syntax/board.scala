package whchess.syntax

import whchess.domain._
import square._

import cats.data.Writer

object board {
  implicit class BoardOps(board: Board) {

    lazy val rendered: String = {

      val allSquares = Square.values
      val rows = allSquares.map(s => allSquares.filter(s.isOnSameRow)).distinct
      val piecesOnRows = rows.map(row => row.map(board.current))
      val rowsOfPiecesAsSymbols = piecesOnRows.map { row =>
        row.map {
          case Some(piece) => piece.symbol
          case None        => ' '
        }.mkString(" ")
      }

      rowsOfPiecesAsSymbols match {
        case row1 +: row2 +: row3 +: row4 +: row5 +: row6 +: row7 +: row8 +: _ =>

          s"""|  A B C D E F G H
              |8 $row8 8
              |7 $row7 7
              |6 $row6 6
              |5 $row5 5
              |4 $row4 4
              |3 $row3 3
              |2 $row2 2
              |1 $row1 1
              |  A B C D E F G H
              |""".stripMargin
      }
    }

    // make moves and log the state of the board
    def mkMove(move: Move): Writer[List[String], Board] = {

      val pieceOnOrigSquare = board.current(move.from)
      lazy val pieceOnDestSquare = board.current(move.to)
      lazy val nextSide = Player.values
        .find(_.color != board.playerInCharge.color)
        .get

      lazy val boardWithMovedPieces: Board = Board(board.current.map { case (s, op) =>
          if (s == move.from) s -> Option.empty
          else if (s == move.to) s -> pieceOnOrigSquare
          else s -> op
        }, nextSide)

      val result = {

        if (pieceOnOrigSquare.isEmpty)
          Writer(
            List(s"$move is invalid, ${move.from} has no piece. You're still in charge"),
            board)

        // check if player owns the piece he's about to move
        else if (pieceOnOrigSquare.forall(_.color != board.playerInCharge.color))
          Writer(
            List(s"$move is invalid, ${pieceOnOrigSquare.get} is not yours. You're still in charge"),
            board)

//  TODO check if pieceOnOrigSquare is allowed to make such a move
//  TODO check if there's no pieces between original and destination squares preventing a piece from moving

        // this and the following checks are different only in log message
        else if (pieceOnDestSquare.isEmpty)
          Writer(
            List(s"${pieceOnOrigSquare.get} goes from ${move.from} to ${move.to}. $nextSide is in charge"),
            boardWithMovedPieces
          )

        else
          Writer(
            List(s"${pieceOnOrigSquare.get} goes from ${move.from} to ${move.to} and takes ${pieceOnDestSquare.get}. $nextSide is in charge"),
            boardWithMovedPieces
          )
      }

      result
    }

    // group all the moves in some collection and apply all at once
    def mkMoves(moves: Move*): Writer[List[String], Board] = ???
  }
}

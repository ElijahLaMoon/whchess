package whchess

import domain._

import scala.annotation.tailrec
import scala.util.{Try, Success, Failure}
import com.whitehatgaming.UserInputFile

import cats.data.Writer

// probably the most ugly part of this little codebase
object MoveParser {

  type Moves     = Vector[Move]
  type Logger[A] = Writer[Vector[String], A]

  lazy val validMoves:   Logger[Moves] = readMovesFrom(rawValidMoves)
  lazy val invalidMoves: Logger[Moves] = readMovesFrom(rawInvalidMoves)
  lazy val checkmate:    Logger[Moves] = readMovesFrom(rawCheckmate)

  // private members
  private type RawMove  = Vector[Int]
  private type RawMoves = Vector[RawMove]
  private type OMoves   = Vector[Option[Move]]

  private def readMovesFrom(loggedRawMoves: Logger[RawMoves]): Logger[Moves] = {
    @tailrec
    def deconstructOuterList(rawMoves: RawMoves, acc: Logger[OMoves]): Logger[OMoves] = rawMoves match {
      case head +: tail =>
        val (log, maybeMove) = parseRawMove(head).run
        deconstructOuterList(tail, acc.bimap(_ ++ log, _ :+ maybeMove))

      case _            => acc
    }

    def parseRawMove(rawMove: RawMove): Logger[Option[Move]] = rawMove match {
      case fileFrom +: rankFrom +: fileTo +: rankTo +: _ => Try {

        // according to UserInputFile documentation:
        // Files are encoded as 0 through 7 respectively,
        val files: Map[Int, File] = ((0 to 7)       zip File.values).toMap
        // and Ranks are encoded in the same fashion except in reverse order
        val ranks: Map[Int, Rank] = ((7 to 0 by -1) zip Rank.values).toMap

        val parsedFileFrom = files(fileFrom)
        val parsedRankFrom = ranks(rankFrom)
        val parsedFileTo   = files(fileTo)
        val parsedRankTo   = ranks(rankTo)

        val squareFrom = Square.lookupSquare(parsedFileFrom, parsedRankFrom)
        val squareTo   = Square.lookupSquare(parsedFileTo, parsedRankTo)

        Move(squareFrom, squareTo)
      } match {
        case Failure(_)    => Writer(Vector(s"Invalid values in $rawMove"), Option.empty)
        case Success(move) => Writer(Vector(s"$rawMove parsed to $move"),   Option(move))
      }
    }

    val (log, rawMoves) = loggedRawMoves.run
    if (log.isEmpty)
      deconstructOuterList(rawMoves, Writer(Vector.empty, Vector.empty))
        // if parsing eventually failed then every other Move is discarded,
        // successful Moves unwrapped from Option
        .map(_.takeWhile(_.nonEmpty).map(_.get))
    else
      Writer(Vector(s"Parsing failed. Cause: ${log.mkString}"), Vector.empty)
  }

  private lazy val rawValidMoves   = readRawMovesFrom(rawMovesSource)
  private lazy val rawInvalidMoves = readRawMovesFrom(rawInvalidMovesSource)
  private lazy val rawCheckmate    = readRawMovesFrom(rawCheckmateSource)

  private lazy val rawMovesSource        = "data/sample-moves.txt"
  private lazy val rawInvalidMovesSource = "data/sample-moves-invalid.txt"
  private lazy val rawCheckmateSource    = "data/checkmate.txt"

  private def readRawMovesFrom(source: String): Logger[RawMoves] = Try {
    @tailrec
    def getRawMovesFrom(input: UserInputFile, index: Int = 0, acc: RawMoves = Vector.empty): RawMoves = {
      // store each move, because consecutive calls to .nextMove yield different results
      val nextMove = input.nextMove

      if (nextMove != null)
        getRawMovesFrom(input, index + 1, acc :+ nextMove.toVector) // Vector to preserve immutability
      else acc
    }

    getRawMovesFrom(new UserInputFile(source))
  } match {
    case Failure(_)        => Writer(Vector(s"Failed reading moves from $source"), Vector.empty)
    case Success(rawMoves) => Writer(Vector.empty, rawMoves)
  }
}

package whchess.syntax

import whchess.domain._
import Direction._, Straight._, Diagonal._

object square {
  implicit class SquareOps(square: Square) {

    def isOnSameColumn(that: Square): Boolean = square.file == that.file
    def isOnSameRow(that: Square):    Boolean = square.rank == that.rank

    // straight ones
    lazy val above:      Option[Square] = getSquareFor(same)(next)
    lazy val below:      Option[Square] = getSquareFor(same)(previous)
    lazy val toLeft:     Option[Square] = getSquareFor(previous)(same)
    lazy val toRight:    Option[Square] = getSquareFor(next)(same)

    // diagonal ones
    lazy val leftAbove:  Option[Square] = getSquareFor(previous)(next)
    lazy val rightAbove: Option[Square] = getSquareFor(next)(next)
    lazy val leftBelow:  Option[Square] = getSquareFor(previous)(previous)
    lazy val rightBelow: Option[Square] = getSquareFor(next)(previous)

    lazy val surroundings: Map[Direction, Option[Square]] = Direction.values.map {
      case dir @ Up        => dir -> above
      case dir @ Down      => dir -> below
      case dir @ Left      => dir -> toLeft
      case dir @ Right     => dir -> toRight
      case dir @ UpLeft    => dir -> leftAbove
      case dir @ UpRight   => dir -> rightAbove
      case dir @ DownLeft  => dir -> leftBelow
      case dir @ DownRight => dir -> rightBelow
    }.toMap

    // private members
    private lazy val same:     Int => Int = identity[Int]
    private lazy val next:     Int => Int = _ + 1
    private lazy val previous: Int => Int = _ - 1

    private lazy val indexedFiles   = indexed(File.values)
    private lazy val indexedRanks   = indexed(Rank.values)

    private def indexed[A](enums: IndexedSeq[A]): Map[A, Int] =
      enums.zipWithIndex.toMap

    private def getSquareFor(fileIndexTransformer: Int => Int)
                            (rankIndexTransformer: Int => Int): Option[Square] = {

      def reverse[A](enums: Map[A, Int]): Map[Int, A] =
        enums.map(_.swap)

      val reversedFiles = reverse(indexedFiles)
      val reversedRanks = reverse(indexedRanks)

      val newFileIndex = fileIndexTransformer(indexedFiles(square.file))
      val newRankIndex = rankIndexTransformer(indexedRanks(square.rank))
      val file = reversedFiles.get(newFileIndex)
      val rank = reversedRanks.get(newRankIndex)

      for {
        f <- file
        r <- rank
      } yield Square.lookupSquare(f, r)
    }
  }
}
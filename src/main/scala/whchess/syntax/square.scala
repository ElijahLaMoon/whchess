package whchess.syntax

import whchess.domain._

object square {
  implicit class SquareOps(square: Square) {

    def isOnSameColumn(that: Square): Boolean = square.file == that.file
    def isOnSameRow(that: Square):    Boolean = square.rank == that.rank

    lazy val above:   Square = getSquareFor(same)(next)
    lazy val below:   Square = getSquareFor(same)(previous)
    lazy val toLeft:  Square = getSquareFor(previous)(same)
    lazy val toRight: Square = getSquareFor(next)(same)

    lazy val surroundings: Vector[Square] = {

      def filterSquaresThat(predicate: Square => Boolean): IndexedSeq[Square] =
        Square.values.filter(predicate)

      lazy val aboveRow    = filterSquaresThat(above.isOnSameRow)
      lazy val belowRow    = filterSquaresThat(below.isOnSameRow)
      lazy val leftColumn  = filterSquaresThat(toLeft.isOnSameColumn)
      lazy val rightColumn = filterSquaresThat(toRight.isOnSameColumn)

      val columns: IndexedSeq[Square] = square.file match {
        case File.A => rightColumn
        case File.H => leftColumn
        case _      => leftColumn ++ rightColumn
      }

      val rows: IndexedSeq[Square] = square.rank match {
        case Rank.`1` => aboveRow
        case Rank.`8` => belowRow
        case _        => aboveRow ++ belowRow
      }

      val straight = Vector(above, below, toLeft, toRight)
      val diagonal = columns intersect rows

      (straight ++ diagonal).filterNot(_ == square)
    }

    // private members
    private lazy val same = identity[Int] _
    private lazy val next:     Int => Int = _ + 1
    private lazy val previous: Int => Int = _ - 1

    private lazy val indexedFiles   = indexed(File.values)
    private lazy val indexedRanks   = indexed(Rank.values)

    private def indexed[A](enums: IndexedSeq[A]): Map[A, Int] =
      enums.zipWithIndex.toMap

    private def getSquareFor(fileIndexTransformer: Int => Int)
                            (rankIndexTransformer: Int => Int): Square = {

      def reverse[A](enums: Map[A, Int]): Map[Int, A] =
        enums.map(_.swap)

      val reversedFiles = reverse(indexedFiles)
      val reversedRanks = reverse(indexedRanks)

      val newFileIndex = fileIndexTransformer(indexedFiles(square.file))
      val newRankIndex = rankIndexTransformer(indexedRanks(square.rank))
      val file = reversedFiles.getOrElse(newFileIndex, square.file)
      val rank = reversedRanks.getOrElse(newRankIndex, square.rank)

      Square.values
        .find(s => s.file == file && s.rank == rank)
        .get
    }
  }
}
package whchess.domain

import enumeratum._

import File._
import Rank._

sealed abstract class Square(val file: File, val rank: Rank) extends EnumEntry {
  override def toString: String = s"$file$rank"
}

object Square extends Enum[Square] {

  def lookupSquare(file: File, rank: Rank): Square = values
    .find(s => s.file == file && s.rank == rank)
    .get

  case object A1 extends Square(A, `1`)
  case object A2 extends Square(A, `2`)
  case object A3 extends Square(A, `3`)
  case object A4 extends Square(A, `4`)
  case object A5 extends Square(A, `5`)
  case object A6 extends Square(A, `6`)
  case object A7 extends Square(A, `7`)
  case object A8 extends Square(A, `8`)
  case object B1 extends Square(B, `1`)
  case object B2 extends Square(B, `2`)
  case object B3 extends Square(B, `3`)
  case object B4 extends Square(B, `4`)
  case object B5 extends Square(B, `5`)
  case object B6 extends Square(B, `6`)
  case object B7 extends Square(B, `7`)
  case object B8 extends Square(B, `8`)
  case object C1 extends Square(C, `1`)
  case object C2 extends Square(C, `2`)
  case object C3 extends Square(C, `3`)
  case object C4 extends Square(C, `4`)
  case object C5 extends Square(C, `5`)
  case object C6 extends Square(C, `6`)
  case object C7 extends Square(C, `7`)
  case object C8 extends Square(C, `8`)
  case object D1 extends Square(D, `1`)
  case object D2 extends Square(D, `2`)
  case object D3 extends Square(D, `3`)
  case object D4 extends Square(D, `4`)
  case object D5 extends Square(D, `5`)
  case object D6 extends Square(D, `6`)
  case object D7 extends Square(D, `7`)
  case object D8 extends Square(D, `8`)
  case object E1 extends Square(E, `1`)
  case object E2 extends Square(E, `2`)
  case object E3 extends Square(E, `3`)
  case object E4 extends Square(E, `4`)
  case object E5 extends Square(E, `5`)
  case object E6 extends Square(E, `6`)
  case object E7 extends Square(E, `7`)
  case object E8 extends Square(E, `8`)
  case object F1 extends Square(F, `1`)
  case object F2 extends Square(F, `2`)
  case object F3 extends Square(F, `3`)
  case object F4 extends Square(F, `4`)
  case object F5 extends Square(F, `5`)
  case object F6 extends Square(F, `6`)
  case object F7 extends Square(F, `7`)
  case object F8 extends Square(F, `8`)
  case object G1 extends Square(G, `1`)
  case object G2 extends Square(G, `2`)
  case object G3 extends Square(G, `3`)
  case object G4 extends Square(G, `4`)
  case object G5 extends Square(G, `5`)
  case object G6 extends Square(G, `6`)
  case object G7 extends Square(G, `7`)
  case object G8 extends Square(G, `8`)
  case object H1 extends Square(H, `1`)
  case object H2 extends Square(H, `2`)
  case object H3 extends Square(H, `3`)
  case object H4 extends Square(H, `4`)
  case object H5 extends Square(H, `5`)
  case object H6 extends Square(H, `6`)
  case object H7 extends Square(H, `7`)
  case object H8 extends Square(H, `8`)

  lazy val values: IndexedSeq[Square] = findValues
}

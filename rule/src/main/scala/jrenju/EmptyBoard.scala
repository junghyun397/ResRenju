package jrenju

import jrenju.notation.{Color, Flag, Pos, Renju}

object EmptyBoard extends Board(
  boardField = Array.fill(Renju.BOARD_SIZE)(Flag.FREE),
  structFieldBlack = Array.fill(Renju.BOARD_SIZE)(0),
  structFieldWhite = Array.fill(Renju.BOARD_SIZE)(0),
  moves = 0,
  latestMove = 0,
  winner = Option.empty,
  hashKey = ZobristHash.empty
) {

  override val color: Color.Value = Color.EMPTY

  override val nextColor: Color.Value = Color.BLACK

  override val latestPos: Option[Pos] = Option.empty

}

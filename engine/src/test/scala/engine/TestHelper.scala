package engine

import renju.notation.{Direction, Flag, Pos, Renju}
import renju.{Board, BoardIO, L1Strip}

import scala.language.implicitConversions

/*
   A B C D E F G H I J K L M N O
15 . . . . . . . . . . . . . . . 15
14 . . . . . . . . . . . . . . . 14
13 . . . . . . . . . . . . . . . 13
12 . . . . . . . . . . . . . . . 12
11 . . . . . . . . . . . . . . . 11
10 . . . . . . . . . . . . . . . 10
 9 . . . . . . . . . . . . . . . 9
 8 . . . . . . . X . . . . . . . 8
 7 . . . . . . . . . . . . . . . 7
 6 . . . . . . . . . . . . . . . 6
 5 . . . . . . . . . . . . . . . 5
 4 . . . . . . . . . . . . . . . 4
 3 . . . . . . . . . . . . . . . 3
 2 . . . . . . . . . . . . . . . 2
 1 . . . . . . . . . . . . . . . 1
   A B C D E F G H I J K L M N O
 */

object TestHelper {

  implicit class T2(val source: String) {

    def t2p: Pos = Pos.fromCartesian(source).get

    def t2i: Int = source.t2p.idx

    def t2b: Board = source.t2b(Renju.BOARD_CENTER_POS.idx)

    def t2b(latestMove: Int): Board = BoardIO.fromBoardText(source, latestMove).get

    def t2s: L1Strip = new L1Strip(
      Direction.X,
      0,
      source.length,
      source
        .map(Flag.charToFlag)
        .map(_.get)
        .toArray
    )

  }

}

package renju.notation

object Renju {

  // max jvm word(32)
  val BOARD_WIDTH: Int = 15

  val BOARD_WIDTH_MAX_IDX: Int = BOARD_WIDTH - 1

  val BOARD_SIZE: Int = BOARD_WIDTH * BOARD_WIDTH

  val BOARD_CENTER_POS: Pos = Pos(this.BOARD_WIDTH / 2, this.BOARD_WIDTH / 2)

}

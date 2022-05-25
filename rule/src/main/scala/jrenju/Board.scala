package jrenju

import jrenju.Board.boardOps
import jrenju.notation.{Color, Flag, Pos, RejectReason, Renju, Rotation}
import ZobristHash.IncrementHash

import scala.language.implicitConversions

class Board(
  val boardField: Array[Byte],
  val pointFieldBlack: Array[Int],
  val pointFieldWhite: Array[Int],

  val moves: Int,
  val latestMove: Int,

  var winner: Option[Byte],

  var zobristKey: Long,
) extends Cloneable {

  def colorFlag: Byte = Flag.colorFlag(this.moves)

  def nextColorFlag: Byte = Flag.nextColorFlag(this.moves)

  def isNextColorBlack: Boolean = this.nextColorFlag == Flag.BLACK

  def color: Color.Value = Color(this.colorFlag)

  def nextColor: Color.Value = Color(this.nextColorFlag)

  def latestPos: Option[Pos] = Option(Pos.fromIdx(this.latestMove))

  def getPoints(idx: Int): (PointOps, PointOps) =
    (new PointOps(this.pointFieldBlack(idx)), new PointOps(this.pointFieldWhite(idx)))

  def validateMove(pos: Pos): Option[RejectReason.Value] = this.validateMove(pos.idx)

  def validateMove(idx: Int): Option[RejectReason.Value] = {
    val flag = this.boardField(idx)
    if (this.isNextColorBlack && Flag.isForbid(flag))
      Option(RejectReason.FORBIDDEN)
    else if (Flag.isExist(flag))
      Option(RejectReason.EXIST)
    else
      Option.empty
  }

  def makeMove(pos: Pos): Board = this.makeMove(pos.idx)

  def makeMove(idx: Int): Board = this.makeMove(idx, calculateForbid = true)

  def makeMove(idx: Int, calculateForbid: Boolean): Board = {
    val board = new Board(
      boardField = this.boardField.updated(idx, this.nextColorFlag),
      pointFieldBlack = this.pointFieldBlack.updated(idx, 0),
      pointFieldWhite = this.pointFieldWhite.updated(idx, 0),
      moves = this.moves + 1,
      latestMove = idx,
      winner = Option.empty,
      zobristKey = this.zobristKey.incrementHash(idx, this.nextColorFlag)
    )

    board.integrateStrips(board.composeStrips(idx))
    if (calculateForbid) board.calculateForbids()

    board
  }

  def rotatedKey(rotation: Rotation.Value): Long = rotation match {
    case Rotation.CLOCKWISE => 0
    case Rotation.COUNTER_CLOCKWISE => 0
    case Rotation.OVERTURN => 0
    case _ => this.zobristKey
  }

  def rotated(rotation: Rotation.Value): Board = rotation match {
    case Rotation.CLOCKWISE => this
    case Rotation.COUNTER_CLOCKWISE => this
    case Rotation.OVERTURN => this
    case _ => this
  }

  def transposedKey(): Long = this.zobristKey

  def transposed(): Board = this

  override def clone(): Board = new Board(
    boardField = this.boardField.clone(),
    pointFieldBlack = this.pointFieldBlack.clone(),
    pointFieldWhite = this.pointFieldWhite.clone(),
    moves = this.moves,
    latestMove = this.latestMove,
    winner = this.winner,
    zobristKey = this.zobristKey
  )

}

object Board {

  @inline implicit def boardOps(board: Board): BoardOps = new BoardOps(board)

  @inline implicit def structOps(board: Board): StructOps = new StructOps(board)

  val newBoard: Board = newBoard(Renju.BOARD_CENTER_POS.idx)

  def newBoard(initIdx: Int): Board = new Board(
    boardField = Array.fill(Renju.BOARD_SIZE)(Flag.FREE).updated(initIdx, Flag.BLACK),
    pointFieldBlack = Array.fill(Renju.BOARD_SIZE)(0),
    pointFieldWhite = Array.fill(Renju.BOARD_SIZE)(0),
    moves = 1,
    winner = Option.empty,
    latestMove = initIdx,
    zobristKey = ZobristHash.empty
  )

}

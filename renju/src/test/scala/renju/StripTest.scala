package renju

import org.scalatest.flatspec._
import org.scalatest.matchers._
import renju.TestHelper.S2
import renju.notation.{Color, Flag, Struct}
import renju.util.Extensions.IntExtensions

class StripTest extends AnyFlatSpec with should.Matchers {

  def both(op: (L2Strip => Array[Int], String, String) => Unit, problem: String, answer: String): Unit = {
    op(_.structStripBlack, problem, answer)
    op(
      _.structStripWhite,
      problem.replace('X', '?').replace('O', 'X').replace('?', 'O'),
      answer.replace('X', '?').replace('O', 'X').replace('?', 'O'),
    )
  }

  def black(op: (L2Strip => Array[Int], String, String) => Unit, problem: String, answer: String): Unit =
    op(_.structStripBlack, problem, answer)

  def white(op: (L2Strip => Array[Int], String, String) => Unit, problem: String, answer: String): Unit =
    op(_.structStripWhite, problem, answer)

  def open3(op: L2Strip => Array[Int], problem: String, answer: String): Unit = {
    op(problem.s2s.calculateL2Strip()).map(v => if (Struct(v).threeTotal.toBoolean) "1" else ".").mkString should be (answer)
    op(problem.reverse.s2s.calculateL2Strip()).map(v => if (Struct(v).threeTotal.toBoolean) "1" else ".").mkString should be (answer.reverse)
  }

  "L1Strip" should "resolve open-3 points" in {
    // XX

    both(open3, "...XX...", ".11..11.")

    both(open3, "...XX..X...", ".11........")

    both(open3, "...O..XX..X...", ".....1........")

    both(open3, "...XX..O...", ".11..1.....")

    both(open3, "...XX.O...", ".11.......")

    both(open3, "...O..XX..O...", ".....1..1.....")

    black(open3, "...X...XX...", "......1..11.")
    white(open3, "...O...OO...", ".....11..11.")

    black(open3, "...X...XX..O...", "......1..1.....")
    white(open3, "...O...OO..X...", ".....11..1.....")

    black(open3, "...X...XX...X...", "......1..1......")
    white(open3, "...O...OO...O...", ".....11..11.....")

    black(open3, "...X...XX.O...", "..............")
    white(open3, "...O...OO.X...", ".....11.......")

    black(open3, "...X...XX..XX..O...", "...................")
    white(open3, "...O...OO..OO..X...", ".....11......1.....")

    // X.X

    both(open3, "...X.X...", "..1.1.1..")

    both(open3, "...O.X.X..O...", "......1.1.....")

    both(open3, ".X.X...", "..1.1..")

    both(open3, "...X.X..O...", "..1.1.1.....")

    both(open3, "...X.X.O...", "..1.1......")

    both(open3, "...X...X.X.O...", "......1.1......")

    black(open3, "....X..X.X.O...", "...............")

    black(open3, "...X..X.X..X...", "...............")

    // X..X

    both(open3, "...X..X...", "....11....")

    both(open3, "...X..X..X...", "....11.11....")

    black(open3, "...X..X.X..O...", ".......1.1.....")

    black(open3, "...X..X.X...", ".......1.1..")

    black(open3, "...X..X.X.O...", "..............")
  }

  def block3(op: L2Strip => Array[Int], problem: String, answer: String): Unit = {
    op(problem.s2s.calculateL2Strip()).map(v => if (Struct(v).blockThreeTotal.toBoolean) "1" else ".").mkString should be (answer)
    op(problem.reverse.s2s.calculateL2Strip()).map(v => if (Struct(v).blockThreeTotal.toBoolean) "1" else ".").mkString should be (answer.reverse)
  }

  "L1Strip" should "resolve block-3 points" in {
    both(block3, "...XXX...", "..1...1..")

    both(block3, "...O.XXX...", "....1...11.")

    both(block3, "...O.XXX..O...", "....1...11....")

    both(block3, "...XX.X...", "..1..1.1..")

    black(block3, "...XXX..X...", ".11...1.....")
    white(block3, "...OOO..O...", "..1...1.....")
  }

  def closed4(op: L2Strip => Array[Int], problem: String, answer: String): Unit = {
    op(problem.s2s.calculateL2Strip()).map(v => if (Struct(v).closedFourTotal == 0) "." else Struct(v).closedFourTotal.toString).mkString should be (answer)
    op(problem.reverse.s2s.calculateL2Strip()).map(v => if (Struct(v).closedFourTotal == 0) "." else Struct(v).closedFourTotal.toString).mkString should be (answer.reverse)
  }

  "L1Strip" should "resolve closed-4 points" in {
    // XXX

    both(closed4, "...XXX...", ".1.....1.")

    both(closed4, "...OXXX...", ".......11.")

    both(closed4, "...O.XXX...", "....1....1.")

    both(closed4, "...O.XXX.O...", "....1...1....")

    both(closed4, "...O.XXX..O...", "....1....1....")

    black(closed4, "...XXX..XXX...", ".1....11....1.")

    black(closed4, "...X.XXX..X...", "..............")

    // XX.X

    both(closed4, "...XX.X...", "..1....1..")

    both(closed4, "...OXX.X...", "......1.1..")

    both(closed4, "...O.XX.X...", "....1....1..")

    both(closed4, "...O.XX.XO...", "....1..1.....")

    both(closed4, "...OXX.X.O...", "......1.1....")

    both(closed4, "...O.XX.XO...", "....1..1.....")

    // X.XX

    both(closed4, "...OX.XX...", ".....1..1..")

    both(closed4, "...O.X.XXO...", "....1.1......")

    both(closed4, "...OX.XX..X...", ".....1..21....")

    both(closed4, "...OXX.X.O...", "......1.1....")

    // XX..X

    both(closed4, "...XX..X...", ".....11....")

    both(closed4, "...XX..X..X", ".....11....")

    both(closed4, "...XX..X", ".....11.")

    black(closed4, "....XX..XX...", ".............")

    // complex

    black(closed4, "...X.XX.X.X...", "..1.1.........")

    black(closed4, "...OX.XX.X...", "........1.1..")

    black(closed4, "...O.XXX..O...", "....1....1....")

    black(closed4, "...XX.X.X...", "..1..1......")
  }

  "L1Strip" should "resolve closed-4 points on white" in {
    // WHITE DOUBLE 4 FORK

    white(closed4, "...O.OO..OO.O...", "..1....22....1..")

    white(closed4, "...OOO...OOO...", ".1.....2.....1.")

    white(closed4, "...OO..O.OO...", ".....12....1..")

    white(closed4, "...O.O.O.O...", "....1.2.1....")

    white(closed4, "...O.O.O.O.O...", "....1.2.2.1....")

    white(closed4, "...OOO..OO.OX...", ".1....12..1.....")

    // CLOSED 4

    white(closed4, "OO.OO.", ".....1")

    white(closed4, "XO.OO.O..", "..1..1.1.")

    white(closed4, "...XOOO..OOOX...", ".......11.......")

    white(closed4, "...X.OOO..OOOX...", "....1...11.......")

    white(closed4, "...XO.O.OOX...", ".....1.1......")

    white(closed4, "...XOOO..OOO.X...", ".......11...1....")

    white(closed4, "..XOOO..OX...", "......11.....")

    white(closed4, "...OOO...", ".1.....1.")

    white(closed4, "...O.OOO..O...", "..1.....11....")

    white(closed4, "...XOOO...", ".......11.")
  }

  def open4(op: L2Strip => Array[Int], problem: String, answer: String): Unit = {
    op(problem.s2s.calculateL2Strip()).map(v => if (Struct(v).openFourTotal.toBoolean) "1" else ".").mkString should be (answer)
    op(problem.reverse.s2s.calculateL2Strip()).map(v => if (Struct(v).openFourTotal.toBoolean) "1" else ".").mkString should be (answer.reverse)
  }

  "L1Strip" should "resolve open-4 points" in {
    // XXX

    both(open4, "...XXX...", "..1...1..")

    both(open4, "...O.XXX..O...", "........1.....")

    both(open4, "...O.XXX.O...", ".............")

    both(open4, "...XXX...XXX...", "..1...1.1...1..")

    both(open4, "...O.XXX...", "........1..")

    black(open4, "...XXX..X...", "..1.........")
    white(open4, "...OOO..O...", "..1...1.....")

    // XX.X

    both(open4, "...XX.X...", ".....1....")

    both(open4, "...OXX.X...", "...........")

    black(open4, "...XX.X.X...", "............")
    white(open4, "...OO.O.O...", ".....1......")
  }

  def five(op: L2Strip => Array[Int], problem: String, answer: String): Unit = {
    op(problem.s2s.calculateL2Strip()).map(v => if (Struct(v).fiveTotal.toBoolean) "1" else ".").mkString should be (answer)
    op(problem.reverse.s2s.calculateL2Strip()).map(v => if (Struct(v).fiveTotal.toBoolean) "1" else ".").mkString should be (answer.reverse)
  }

  "L1Strip" should "resolve move-to-win points" in {
    both(five, "...XXXX...", "..1....1..")

    both(five, "...OXXXX...", "........1..")

    both(five, "...XX.XX...", ".....1.....")

    both(five, "...XXX.X...", "......1....")

    black(five, "...XXXX.XX...", "..1..........")

    black(five, "...XXXX..XXXX...", "..1....11....1..")

    black(five, "...XXX.XX...", "............")

    white(five, "...OOO.OO...", "......1.....")
  }

  def fiveInRow(problem: String, answer: Option[Color]): Unit = {
    problem.s2s.calculateL2Strip().winner should be (answer)
    problem.reverse.s2s.calculateL2Strip().winner should be (answer)
  }

  "L1Strip" should "resolve five-in-a-row state" in {
    fiveInRow(".XXOX..OXXXX.X", Option.empty)

    fiveInRow("..XOOXO.OOOO.O", Option.empty)

    fiveInRow("XXXXX", Some(Color.Black))

    fiveInRow("OOOOO", Some(Color.White))

    fiveInRow("..OX.XXO.XXXXXO..", Some(Color.Black))

    fiveInRow("..OOOOX.OXOOOOOX", Some(Color.White))
  }

  def double4forbid(problem: String, answer: String): Unit = {
    problem.s2s.calculateL2Strip().forbidMask.map(v => if (v == Flag.FORBIDDEN_44) "4" else ".").mkString should be (answer)
    problem.reverse.s2s.calculateL2Strip().forbidMask.map(v => if (v == Flag.FORBIDDEN_44) "4" else ".").mkString should be (answer.reverse)
  }

  "L1Strip" should "resolve double-4 forbidden points" in {
    double4forbid("...X.XX..X...", ".......4.....")

    double4forbid("...X..XX.X...", ".....4.......")

    double4forbid("...XXX...XXX...", ".......4.......")

    double4forbid("...XX..X.XX...", "......4.......")

    double4forbid("...X.X.X.X...", "......4......")

    double4forbid("...X.X.X.X.X...", "......4.4......")

    double4forbid("...X.X.X.X.X.X.X.X...", "......4.4.4.4.4......")

    double4forbid("...XXX..X.XX...", "...............")

    double4forbid("...X.X.X.XX...", "..............")
  }

  def overlineForbid(problem: String, answer: String): Unit = {
    problem.s2s.calculateL2Strip().forbidMask.map(v => if (v == Flag.FORBIDDEN_6) "6" else ".").mkString should be (answer)
    problem.reverse.s2s.calculateL2Strip().forbidMask.map(v => if (v == Flag.FORBIDDEN_6) "6" else ".").mkString should be (answer.reverse)
  }

  "L1Strip" should "resolve overline forbidden points" in {
    overlineForbid("...XXX.XX...", "......6.....")

    overlineForbid("...X.XXXX...", "....6.......")

    overlineForbid("...X.XXX.XXX...", "........6......")

    overlineForbid("...OXXXX.X...", "........6....")
  }

}

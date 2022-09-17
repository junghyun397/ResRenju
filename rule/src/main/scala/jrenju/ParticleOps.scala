//noinspection ScalaUnusedSymbol

package jrenju

import jrenju.notation.{Color, Flag}

import scala.language.implicitConversions

final class FieldStatus(
  val color: Color.Value,
  val forbidKind: Option[Byte],
  val blackStruct: ParticleOps,
  val whiteStruct: ParticleOps,
) {

  def apply(flag: Byte): ParticleOps =
    flag match {
      case Flag.BLACK => this.blackStruct
      case Flag.WHITE => this.whiteStruct
      case _ => ParticleOps.empty
    }

  def apply(color: Color.Value): ParticleOps =
    color match {
      case Color.BLACK => this.blackStruct
      case Color.WHITE => this.whiteStruct
      case _ => ParticleOps.empty
    }

}

// jvm word(4bytes)
// three(4bits) blockThree(4bits) closedFour_1(4bits) closedFour_2(4bits) openFour(4bits) five(4bits) -> 3bytes
final class ParticleOps(private val x: Int) {

  // mask: 0111 0111 0111 0111 0111 0111 0000 0000
  def merged(direction: Int, that: Int): Int =
    (x & (0x7777_7700 >>> direction | ~(0xFFFF_FFFF >>> direction))) | (that >>> direction)

  def threeAt(direction: Int): Boolean = ((x >>> 31 - direction) & 0x1) == 1

  def blockThreeAt(direction: Int): Boolean = ((x >>> 27 - direction) & 0x1) == 1

  def closedFourAt(direction: Int): Boolean = ((x >>> 23 - direction) & 0x1) == 1

  def openFourAt(direction: Int): Boolean = ((x >>> 15 - direction) & 0x1) == 1

  def fiveAt(direction: Int): Boolean = ((x >>> 11 - direction) & 0x1) == 1

  def threeTotal: Int = Integer.bitCount((x >>> 28) & 0xF)

  def blockThreeTotal: Int = Integer.bitCount((x >>> 24) & 0xF)

  def closedFourTotal: Int = Integer.bitCount((x >>> 16) & 0xFF)

  def openFourTotal: Int = Integer.bitCount((x >>> 12) & 0xF)

  def fourTotal: Int = this.closedFourTotal + this.openFourTotal

  def fiveTotal: Int = Integer.bitCount((x >>> 8) & 0xF)

  override def hashCode(): Int = x

}

object ParticleOps {

  implicit def particleOps(particle: Int): ParticleOps = new ParticleOps(particle)

  val empty: ParticleOps = new ParticleOps(0)

}

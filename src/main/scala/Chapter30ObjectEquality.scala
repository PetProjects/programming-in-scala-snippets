package chapter30

/**
  * Created by Igor_Dobrovolskiy on 18.07.2017.
  */

class Point(val x: Int, val y: Int) {
  override def hashCode = (x, y).##

  override def equals(obj: scala.Any): Boolean = obj match {
    case p: Point => p.canEqual(this) && p.x == x && p.y == y
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Point]
}

object Color extends Enumeration {
  val Red, Orange, Yellow, Green, Blue, Indigo, Violet = Value
}

class ColoredPoint(x: Int, y: Int, val color: Color.Value) extends Point(x, y) {
  override def hashCode: Int = (super.hashCode, color).##

  override def equals(other: Any): Boolean = other match {
    case that: ColoredPoint => that.canEqual(this) && super.equals(that) && that.color == color
    case _ => false
  }

  override def canEqual(other: Any): Boolean = other.isInstanceOf[ColoredPoint]
}

trait Tree[+T] {
  def elem: T

  def left: Tree[T]

  def right: Tree[T]
}

object EmptyTree extends Tree[Nothing] {
  override def elem: Nothing = throw new NoSuchElementException("EmptyTree.elem")

  override def left: Tree[Nothing] = throw new NoSuchElementException("EmptyTree.left")

  override def right: Tree[Nothing] = throw new NoSuchElementException("EmptyTree.right")
}

class Branch[+T]
(
  val elem: T,
  val left: Tree[T],
  val right: Tree[T]
) extends Tree[T] {

  override def equals(other: scala.Any): Boolean = other match {
    case that: Branch[_] => that.canEqual(this) && that.elem == elem && that.left == left && that.right == right
  }

  def canEqual(other: Any) = other.isInstanceOf[Branch[_]]

  override def hashCode(): Int = (elem, left, right).##
}

class Rational(n: Int, d: Int) {
  require(d != 0)

  private val g: Int = gcd(n.abs, d.abs)
  val numer: Int = n / g
  val denom: Int = d / g

  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)

  override def equals(other: scala.Any): Boolean = other match {
    case that: Rational => that.canEqual(this) &&
      that.numer == numer &&
      that.denom == denom
    case _ => false
  }

  def canEqual(other: Any): Boolean = other.isInstanceOf[Rational]

  override def hashCode(): Int = (numer, denom).##

  override def toString: String = if (denom == 1) numer.toString else s"$numer/$denom"
}

object Chapter30ObjectEquality extends App {
  val p = new Point(1, 2)
  val cp = new ColoredPoint(1, 2, Color.Red)
  val pAnon = new Point(1, 1) {
    override val y: Int = 2
  }
  val l = List(p)
  println(s"p? - ${l contains p}; cp? - ${l contains cp}; pAnon? - ${l contains pAnon}")
}

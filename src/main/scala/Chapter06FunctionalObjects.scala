/**
  * Created by Igor_Dobrovolskiy on 05.07.2017.
  */

case class Rational(n: Int, d: Int) {
  require(d != 0)

  private val g: Int = gcd(n.abs, d.abs)
  val numer: Int = n / g
  val denom: Int = d / g

  def this(n: Int) = this(n, 1)

  override def toString: String = s"$numer/$denom ($g)"

  def +(that: Rational): Rational =
    new Rational(numer * that.denom + that.numer * denom, denom * that.denom)

  def +(i: Int): Rational =
    new Rational(numer + i * denom, denom)

  def -(that: Rational): Rational =
    new Rational(numer * that.denom - that.numer * denom, denom * that.denom)

  def -(i: Int): Rational =
    new Rational(numer - i * denom, denom)

  def *(that: Rational): Rational =
    new Rational(numer * that.numer, denom * that.denom)

  def *(i: Int): Rational =
    new Rational(numer * i, denom)

  def /(that: Rational): Rational =
    new Rational(numer * that.denom, denom * that.numer)

  def /(i: Int): Rational =
    new Rational(numer, denom * i)

  def lessThan(that: Rational): Boolean = numer * that.denom < that.numer * denom

  def max(that: Rational): Rational =
    if (lessThan(that)) that else this

  private def gcd(a: Int, b: Int): Int =
    if (b == 0) a else gcd(b, a % b)
}


object Rational {
  implicit def intToRational(i: Int): Rational = new Rational(i)
}

class Chapter06FunctionalObjects {
  private var i: Int = 0

  def mymixedmeth_~=(i: Int): Unit = this.i = i

  def `yield`: Int = i
}

object Chapter06FunctionalObjects extends App {
  println(new Rational(66, 42))
  println(new Rational(66, 42) * new Rational(7, 11))

  val a = new Chapter06FunctionalObjects
  a.mymixedmeth_~=(1)

  var b = "a"
  b += 2

  println(a.`yield`)

  //  implicit def intToRational(i: Int): Rational = new Rational(i)

  println(7 * new Rational(66, 42))
}
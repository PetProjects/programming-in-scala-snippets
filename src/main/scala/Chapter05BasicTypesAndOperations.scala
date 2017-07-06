/**
  * Created by Igor_Dobrovolskiy on 05.07.2017.
  */
class Chapter05BasicTypesAndOperations {
  private var a = 0

  def unary_+(): Int = { //+ - ! ~
    a += 1
    a
  }

  /** Operator precedence
    * (all not mentioned spec. chars)
    * * / %
    * + -
    * :
    * = !
    * < >
    * &
    * &#94; (^^)
    * |
    * (all letters)
    * (all assignment operators: ends with '=' but not in the list {<= >= != ==}
    */

  /** Operator associativity
    * left associative: NOT ends with ':'
    * right associative: ENDS with ':'
    */
}

object Chapter05BasicTypesAndOperations extends App {
  println('\u9743')
  println(
    """|Hello
       |and bye!
    """.stripMargin)

  val a = 4.567D
  val b = 0x234feL
  println(s"$a and $b")
  println(f"formatted a=$a%3.2f and b=$b%08x")

  Console printf("%d -> %c\n", 1, 'a')

  val c = new Chapter05BasicTypesAndOperations()
  println(f"${+c} ${+c}")

  println(f"${-1 >>> 1}%X")

  val d: Tuple1[Int] = null
  val e: String = null
  println(d == e)
  val f = e
  println(d eq e, f eq e)
  println('%'.toByte, '*'.toByte, '+'.toByte, ':'.toByte)

  println("sMaRt" capitalize)
}
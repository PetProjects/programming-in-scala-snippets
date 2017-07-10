/**
  * Created by Igor_Dobrovolskiy on 10.07.2017.
  */

import scala.math.{E, Pi}

sealed abstract class Expr

case class Var(name: String) extends Expr

case class Number(num: Double) extends Expr

case class UnOp(operator: String, arg: Expr) extends Expr

case class BinOp(operator: String, left: Expr, right: Expr) extends Expr

class ExprFormatter {
  // Contains operators in groups of increasing precedence
  private val opGroups =
    Array(
      Set("|", "||"),
      Set("&", "&&"),
      Set("ˆ"),
      Set("==", "!="),
      Set("<", "<=", ">", ">="),
      Set("+", ""),
      Set("*", "%")
    )
  // A mapping from operators to their precedence
  private val precedence = {
    var ops = for {
      i <- 0 until opGroups.length
      op <- opGroups(i)
    } yield op -> i
    ops.toMap
  }

  private val unaryPrecedence = opGroups.length
  private val fractionPrecedence = -1

  import Element._

  private def format(e: Expr, enclPrec: Int): Element =
    e match {
      case Var(name) => elem(name)
      case Number(num) =>
        def stripDots(s: String) =
          if (s.endsWith(".0")) s.substring(0, s.length - 2) else s

        elem(stripDots(num.toString))
      case UnOp(op, arg) =>
        elem(op) beside format(arg, unaryPrecedence)
      case BinOp("/", left, right) =>
        val top = format(left, fractionPrecedence)
        val bot = format(right, fractionPrecedence)
        val line = elem('-', top.width max bot.width, 1)
        val frac = top above line above bot
        if (enclPrec != fractionPrecedence) frac
        else elem(" ") beside frac beside elem(" ")
      case BinOp(op, left, right) =>
        val opPrec = precedence(op)
        val l = format(left, opPrec)
        val r = format(right, opPrec + 1)
        val oper = l beside elem(" " + op + " ") beside r
        if (enclPrec <= opPrec) oper
        else elem("(") beside oper beside elem(")")
    }

  def format(e: Expr): Element = format(e, 0)
}

object Chapter15CaseClassesPatternMatching extends App {
  val expr: Expr = BinOp("+", Number(1), UnOp("-", Number(2)))

  expr match {
    //case BinOp(op, left, right) => println(s"$expr is binary expression ")
    case BinOp(_, _, _) => println(s"$expr is binary expression ")
    case _ =>
  }

  println(
    E match {
      case Pi => "strange math? Pi = " + Pi
      case _ => "OK"
    }
  )

  println(
    E match {
      case pi => "strange math? Pi = " + pi
      case _ => "OK" //unreachable warning
    }
  )

  val pi = E

  println(
    E match {
      case `pi` => "strange math? Pi = " + pi
      case _ => "OK"
    }
  )

  List(0) match {
    case List(0, _*) => println("found it")
    case _ =>
  }

  ("a ", 3, "-tuple") match {
    case (a, b, c) => println("matched " + a + b + c)
    case _ =>
  }

  def generalSize(x: Any) = x match {
    case s: String => s.length
    case m: Map[_, _] => m.size
    case _ => 1
  }

  def isIntIntMap(x: Any) = x match {
    case _: Map[Int, Int] => true //type erasure issue
    case _ => false
  }

  println(
    isIntIntMap(Map(1 -> 1)),
    isIntIntMap(Map("abc" -> "abc"))

    /** BUG! */
  )

  def isStringArray(x: Any) = x match {
    case _: Array[String] => "yes" //no type erasure, the only exception is for Array[_]
    case _ => "no"
  }

  println(
    isStringArray(Array(1, 1)),

    /** no bug */
    isStringArray(Array("yep"))
  )

  /** Variable binding */
  expr match {
    case UnOp("abs", e@UnOp("abs", _)) => e
    case _ =>
  }

  /** Pattern guard */
  def simplifyAdd(e: Expr) = e match {
    case BinOp("+", x, y) if x == y => BinOp("*", x, Number(2))
    case _ => e
  }

  /** Pattern overlaps */
  def simplifyAll(expr: Expr): Expr = expr match {
    case UnOp("-", UnOp("-", e)) => simplifyAll(e) // '-' is its own inverse
    case BinOp("+", e, Number(0)) =>
      simplifyAll(e) // '0' is a neutral element for '+'
    case BinOp("*", e, Number(1)) =>
      simplifyAll(e) // '1' is a neutral element for '*'

    // Generic matches comes BELOW
    case UnOp(op, e) =>
      UnOp(op, simplifyAll(e))
    case BinOp(op, l, r) =>
      BinOp(op, simplifyAll(l), simplifyAll(r))
    case _ => expr
  }

  def describe(e: Expr): String = (e: @unchecked) match { // Exhaustivity check is suppressed
    case Number(_) => "a number"
    case Var(_) => "a variable"
  }

  /** P-n matching in var definition */
  val exp = new BinOp("*", Number(5), Number(1))
  val BinOp(op, left, right) = exp

  /** Case sequences as partial functions */
  val second: PartialFunction[List[Int], Int] = {
    case _ :: y :: _ => y
  }

  println(second.isDefinedAt(List(5, 6, 7)), second.isDefinedAt(List()))

  val f: PartialFunction[(Int, Seq[Int]), (Int, Int)] = {
    case (k: Int, v: Seq[Int]) => (k, v.length)
  }
  val f2: (Int, Seq[Int]) => (Int, Int) = {
    case (k: Int, v: Seq[Int]) => (k, v.length)
  }

  println(f, f2, f == f2)

  val m = Map("USA" -> "Washington", "Ukraine" -> "Kyiv")
  for ((country, capital) <- m) println(s"$country -> $capital")

  val fruits = List(Some("apple"), None, Some("orange"))
  for (fruit <- fruits) println(fruit) // all
  for (Some(fruit) <- fruits) println(fruit) // just not null

  def experssions: Unit = {
    val f = new ExprFormatter
    val e1 = BinOp("*", BinOp("/", Number(1), Number(2)),
      BinOp("+", Var("x"), Number(1)))
    val e2 = BinOp("+", BinOp("/", Var("x"), Number(2)),
      BinOp("/", Number(1.5), Var("x")))
    val e3 = BinOp("/", e1, e2)

    def show(e: Expr) = println(f.format(e) + "\n\n")

    for (e <- Array(
      e1, e2, e3)) show(e)
  }

  experssions

}

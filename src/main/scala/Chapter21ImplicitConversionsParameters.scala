/**
  * Created by Igor_Dobrovolskiy on 12.07.2017.
  */
class PreferredPrompt(val preference: String)

class PreferredDrink(val preference: String)

object Greeter {
  def greet(name: String)(implicit prompt: PreferredPrompt,
                          drink: PreferredDrink) = {
    println("Welcome, " + name + ". The system is ready.")
    print("But while you work, ")
    println("why not enjoy a cup of " + drink.preference + "?")
    println(prompt.preference)
  }
}

object JoesPrefs {
  implicit val prompt = new PreferredPrompt("Yes, master> ")
  implicit val drink = new PreferredDrink("tea")
}

object Chapter21ImplicitConversionsParameters extends App {

  /** Converting the receiver */

  // For new DSL
  class TwoArrowAssoc(x: Int) {
    def <-> (y: Int): Range = x to y
  }
  implicit def int2ArrowAssoc(x: Int): TwoArrowAssoc =
    new TwoArrowAssoc(x)

  val r = 1 <-> 10

  println(r)

  /** Implicit conversion to an expected type */

  implicit def rangeToInt(r: Range): Int = r.sum

  val s: Int = r
  println(s)

  /** Implicit class */
  case class Rectangle(width: Int, height: Int)
  implicit class RectangleMaker(width: Int) {
    def x(height: Int) = Rectangle(width, height)
  }
  val myRectangle = 3 x 4 // myRectangle: Rectangle

  /** Implicit pars */

  import JoesPrefs._

  Greeter.greet("Igor")

  def maxListOrdering[T](elements: List[T])
                        (implicit ordering: Ordering[T]): T =
    elements match {
      case List() =>
        throw new IllegalArgumentException("empty list!")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxListOrdering(rest)(ordering)
        if (ordering.gt(x, maxRest)) x
        else maxRest
    }

  println(maxListOrdering(List(1, 2, 3)))
  println(maxListOrdering(List(1.0, 2.0, 3.0)))
  println(maxListOrdering(List("A", "X", "s")))
  println(
    maxListOrdering(List("A", "X", "s"))
    (new Ordering[String] {
      override def compare(x: String, y: String): Int = x.toLowerCase.compare(y.toLowerCase)
    })
  )

  def maxList[T: Ordering](elements: List[T]): T =
    elements match {
      case List() =>
        throw new IllegalArgumentException("empty list!")
      case List(x) => x
      case x :: rest =>
        val maxRest = maxList(rest)
        if (implicitly[Ordering[T]].gt(x, maxRest)) x
        else maxRest
    }

  println(maxList(List(1, 2, 3)))
  println(maxList(List(1, 2, 3))(new Ordering[Int] {
    override def compare(x: Int, y: Int): Int = y - x
  }))

  /** Mocha */

  class PreferredDrink(val preference: String)

  implicit val pref = new PreferredDrink("mocha")

  def enjoy(name: String)(implicit drink: PreferredDrink) = {
    print("Welcome, " + name)
    print(". Enjoy a ")
    print(drink.preference)
    println("!")
  }

  enjoy("reader")

}

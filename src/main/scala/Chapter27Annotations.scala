import scala.annotation.tailrec

/**
  * Created by Igor_Dobrovolskiy on 17.07.2017.
  */

@SerialVersionUID(123)
class AnnotationTest {
  @volatile var a: Int = 1

  @deprecated def b() = {}

  @transient val c: Long = 5L

  @deprecated("you should use e() instead") def d() = {}

  def e() = {}

  @scala.beans.BeanProperty
  val g: Int = 9

  def h(a: Int): Int = {
    @tailrec def loop(i: Int, sum: Int): Int =
      if (i <= 0) sum
      else loop(i - 1, sum + a - i)

    loop(a, 0)
  }

  @native
  def j() ={} // for JNI integration
}

//@Serializable object An // deprecated since scala 2.9.0. Use instead:
object An extends Serializable

sealed trait Bbase

final case class Bb(a: Int, b: Char) extends Bbase
final case class Bc(c: Char) extends Bbase

object Chapter27Annotations extends App {
  println(new AnnotationTest().h(10))

  val b: Bbase = new Bb(1, 'a')

  def m(x: Bbase): Char = (b: @unchecked) match {
    case Bb(1, c) => c
  }

  val c: Char = m(b)
}

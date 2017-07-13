/**
  * Created by Igor_Dobrovolskiy on 13.07.2017.
  */

final case class at[A](a: A) {
  def at(as: Array[A]): Option[A] = as.find(_ == a)
}

object at {
  implicit def AtoAt[A](x: A): at[A] = new at(x)
}

object Chapter22ImplementingLists extends App {

//  implicit class At[A](a: A) { // alternative to "final case class at[A]..."
//    def at(as: Array[A]): Option[A] = as.find(_ == a)
//  }

  import at._

  val as = Array(1, 2, 3)
  println(1 at as)
  println(0 at as)

  1 :: Nil
}

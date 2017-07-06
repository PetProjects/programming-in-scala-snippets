import scala.collection.immutable
import scala.collection.mutable
import scala.io.Source

/**
  * Created by Igor_Dobrovolskiy on 04.07.2017.
  */
object Chapter03 {
  def multiArgFunc(is: Int*): String = is.mkString(", ")

  def main(args: Array[String]): Unit = {
    val l1 = List(1, 2, 3)
    val l2 = List(4, 5)
    val l3 = l1 ::: l2
    val ls = List("af", "Ws", "ttt")
    println(l3)
    println(l1 :+ 4)
    println(l1.head)
    println(l1.init)
    println(l2.filterNot(_ == 4))
    println(l3.sortWith((f, s) => f > s))

    var s = immutable.HashSet(1, 2, 4)
    println(s, s.getClass)
    s += 3
    println(s, s.getClass)
    val s2 = mutable.Set[Int](s.toSeq: _*)
    s2 += 7
    println(s2, s2.getClass)

    for (l <- Source.fromFile("1.txt").getLines())
      printf(f"Line length=${l.length}, word count=%%s%n", l.split("\\s+").length)

    println('@'.toString * 8)
    println("<>" * 8)
    println(multiArgFunc(1, 2, 3))
  }
}

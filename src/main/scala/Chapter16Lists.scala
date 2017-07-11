import scala.annotation.tailrec

/**
  * Created by Igor_Dobrovolskiy on 10.07.2017.
  */

object Chapter16Lists extends App {
  def isort(xs: List[Int]): List[Int] =
    if (xs.isEmpty) Nil
    else insert(xs.head, isort(xs.tail))

  //@tailrec // not possible!
  def insert(x: Int, xs: List[Int]): List[Int] =
    if (xs.isEmpty || x <= xs.head) x :: xs
    else xs.head :: insert(x, xs.tail)

  def isort2(xs: List[Int]): List[Int] = xs match {
    case List() => List()
    case x :: xs1 => insert2(x, isort(xs1))
  }

  //@tailrec // not possible!
  def insert2(x: Int, xs: List[Int]): List[Int] = xs match {
    case List() => List(x)
    case y :: ys => if (x <= y) x :: xs
    else y :: insert2(x, ys)
  }

  val l = List(10, 3, 4, 7, 1)
  println(isort(l))
  println(isort2(l))

  val l2 = List(15, 14)
  val l3 = List(-2, -5)
  println(l2 ::: l ::: l3)

  def append[T](xs: List[T], ys: List[T]): List[T] = xs match {
    case Nil => ys
    case x :: xs1 => x :: append(xs1, ys)
  }

  l.reverse

  println(l apply 2) //rare in Scala
  println(l.indices)
  println(List(List(1, 2), List(3), List(), List(4, 5)).flatten)
  println(Array("abc", "de", "", "f").map(_.toCharArray).flatten.mkString("")) //flatten of Array is Array

  println("abcde".toList.mkString("[", ",", "]"))
  val sb = new StringBuilder
  "abcde".toList addString(sb, "(", ";", ")")
  println(sb)

  val a = Array.fill(10)('_')

  "abcde" copyToArray(a, 2)
  println(a.mkString(" "))

  for (c <- "abcde".iterator) println(c)

  def msort[T](less: (T, T) => Boolean)(xs: List[T]): List[T] = {
    def merge(xs: List[T], ys: List[T]): List[T] = (xs, ys) match {
      case (Nil, _) => ys
      case (_, Nil) => xs
      case (x :: xs1, y :: ys1) => if (less(x, y)) x :: merge(xs1, ys) else y :: merge(xs, ys1)
    }

    val n = xs.length / 2
    if (n == 0) xs
    else {
      val (ys, zs) = xs splitAt n
      merge(msort(less)(ys), msort(less)(zs))
    }
  }

  println(msort[Int](_ < _)(l2 ::: l ::: l3))
  val intSort = msort[Int](_ < _) _
  println(intSort(l2 ::: l ::: l3))
  val revIntSort = msort((x: Int, y: Int) => x > y) _
  println(revIntSort(l2 ::: l ::: l3))

  val words = List("the", "quick", "brown", "fox")
  println(words.map(_.reverse))
  println(words.flatMap(_.toList))

  /** FOLDING! */

  //fold left
  println(("!" /: words) (_ + "-" + _))
  println((words.head /: words.tail) ("[" + _ + _ + "]"))

  //fold right
  println((words :\ "@") (_ + "=" + _))
  println((words.init :\ words.last) ("{" + _ + _ + "}"))

  //reverse with foldLeft

  def reverseLeft[T](xs: List[T]) =
    (List[T]() /: xs) { (ys, y) => y :: ys }

  println(reverseLeft(words))
  println((List[String]() /: words) { (ys, y) => y :: ys })

  //sortWith == msort
  println(words sortWith (_.length > _.length))

  //List's Companion methods
  println(List.range(9, 1, -3))
  println(List.fill(9)('+'))
  println(List.fill(3, 2)("<>"))
  println(List.tabulate(5)(n => n * n))
  println(List.tabulate(5, 3)(_ + _))

  println((List(10, 20), List(3, 4, 5)).zipped.map(_ * _))
  println((List(10, 20) zip List(3, 4, 5)).map { case (x, y) => x * y })
  println((List("abc", "de"), List(3, 2)).zipped.forall(_.length == _))
  println((List("abc", "de") zip List(3, 2)).forall { case (x, y) => x.length == y })
  println((List("abc", "de"), List(3, 2)).zipped.exists(_.length != _))
}

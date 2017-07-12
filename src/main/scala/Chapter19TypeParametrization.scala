import bobsdelights._

/**
  * Created by Igor_Dobrovolskiy on 11.07.2017.
  */

trait Queue[+T] {
  def head: T

  def tail: Queue[T]

  def enqueue[U >: T](x: U): Queue[U]
}

object Queue {
  def apply[T](xs: T*): Queue[T] =
    new QueueImpl[T](xs.toList, Nil)

  private class QueueImpl[+T]
  (
    private val leading: List[T],
    private val trailing: List[T]
  ) extends Queue[T] {
    private def mirror =
      if (leading.isEmpty)
        new QueueImpl(trailing.reverse, Nil)
      else
        this

    def head = mirror.leading.head

    def tail = {
      val q = mirror
      new QueueImpl(q.leading.tail, q.trailing)
    }

    def enqueue[U >: T](x: U) =
      new QueueImpl[U](leading, x :: trailing)
  }

}

class Queue2[+T] private
(
  private[this] var leading: List[T],
  private[this] var trailing: List[T]
  //  private var leading: List[T], //do not compile: Error:(43, 7) covariant type T occurs in contravariant position in type List[T] of value trailing_=
  //  private var trailing: List[T] //do not compile: Error:(43, 7) covariant type T occurs in contravariant position in type List[T] of value leading_=
) {
  private def mirror() =
    if (leading.isEmpty) {
      while (!trailing.isEmpty) {
        leading = trailing.head :: leading
        trailing = trailing.tail
      }
    }

  def head: T = {
    mirror()
    leading.head
  }

  def tail: Queue2[T] = {
    mirror()
    new Queue2(leading.tail, trailing)
  }

  def enqueue[U >: T](x: U) =
    new Queue2[U](leading, x :: trailing)
}

/** TODO: deliberate this later ......... */
abstract class Cat2[-T, +U] {
  def meow[W](volume: T, listener: Cat2[U, T]): Cat2[Cat2[U, T], U]
}


abstract class A1

class B1 extends A1

class C1 extends A1

class D1 extends C1

class CCat extends Cat2[A1, C1] {
  override def meow[W](volume: A1, listener: Cat2[C1, A1]): Cat2[Cat2[C1, A1], C1] = null
}

trait Function1C[-S, +T] {
  def apply(x: S): T
}

class OrderedString(val v:String) extends Ordered[OrderedString] {
  override def compare(that: OrderedString): Int = v.compareToIgnoreCase(that.v)

  override def toString: String = v
}

object Chapter19TypeParametrization extends App {
  val fq: Queue[Fruit] = Queue(Fruits.menu: _*)

  println(fq enqueue Fruits.Orange)
  println(fq enqueue (new Object))

  var a1: A1 = null
  val f1: C1 => A1 = (c) => {
    a1 = c;
    new C1
  }
  f1(new C1)
  f1(new D1)

  val f2: C1 => C1 = (c: C1) => {
    a1 = c;
    new C1
  }
  val f3: C1 => A1 = f2
  //  val f4: Function1C[C1, A1] = f2 // do not compile ... why??!!
  val f5: Function1[D1, A1] = f3

  val cat = new CCat
  val cat2: Cat2[Cat2[C1, A1], C1] = cat.meow(new B1, cat)
  val cat3: Cat2[CCat, C1] = cat.meow(new B1, cat)
  val cat4: Cat2[CCat, A1] = cat.meow(new D1, cat)
//  val cat5: Cat2[CCat, D1] = cat.meow(new D1, cat) // do not compile since D1 is subtype of C1 at the place of covariant +U

  def orderedMsort[T <: Ordered[T]](xs: List[T]): List[T] = {
    def merge(xs: List[T], ys: List[T]): List[T] = (xs, ys) match {
      case (Nil, _) => ys
      case (_, Nil) => xs
      case (x :: xs1, y :: ys1) =>
        if (x < y) x :: merge(xs1, ys)
        else y :: merge(xs, ys1)
    }

    val n = xs.length / 2
    if (n == 0) xs
    else {
      val (ys, zs) = xs splitAt n
      merge(orderedMsort(ys), orderedMsort(zs))
    }
  }

  val l = List(10, 3, 4, 7, 1)
//  orderedMsort(l) //do not compile: Error:(141, 3) inferred type arguments [Int] do not conform to method orderedMsort's type parameter bounds [T <: Ordered[T]]

  val l2 = List(new OrderedString("WE"), new OrderedString("are"), new OrderedString("THE"), new OrderedString("champions"))

  println(orderedMsort(l2))
}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Igor_Dobrovolskiy on 07.07.2017.
  */

trait Philosophical {
  def philosophize() = "I consume memory therefore I am!"
}

class From extends Philosophical {
  override def toString = "green"
}

//trait NoPoint(x: Int) //Trait or objects may not have parameters

class Point(val x: Int, val y: Int)

trait Rectangular {
  def topLeft: Point

  def bottomRight: Point

  def left = topLeft.x

  def right = bottomRight.x

  def width = right - left
}

class ComparableRational(n: Int, d: Int) extends Rational(n, d) with Ordered[Rational] {
  override def compare(that: Rational): Int = (numer * that.denom) - (that.numer * denom)
}

abstract class IntQueue {
  def get(): Int

  def put(i: Int)
}

class BasicIntQueue extends IntQueue {
  private val buf = new ArrayBuffer[Int]

  def get() = buf.remove(0)

  def put(i: Int) = {
    buf += i
  }
}

trait Doubling extends IntQueue {
  abstract override def put(x: Int) = {
    super.put(2 * x)
  }
}

trait Incrementing extends IntQueue {
  abstract override def put(i: Int) = {
    super.put(i + 1)
  }
}

trait Filtering extends IntQueue {
  abstract override def put(i: Int): Unit = {
    if (i >= 0) super.put(i)
  }
}

class DoubledQueue extends BasicIntQueue with Doubling

class Animal
trait Furry extends Animal
trait HasLegs extends Animal
trait FourLegged extends HasLegs
class Cat extends Animal with Furry with FourLegged

/** Cat's linearization: Cat -> FourLegged -> HasLegs -> Furry -> Animal -> AnyRef -> Any */

object Chapter12Traits extends App {
  val doubledQueue = new BasicIntQueue with Doubling //new DoubledQueue
  doubledQueue.put(1)

  println(doubledQueue.get())

  val filteredIncrementedQueue = new BasicIntQueue with Incrementing with Filtering
  filteredIncrementedQueue.put(-1); filteredIncrementedQueue.put(0); filteredIncrementedQueue.put(1)
  println(filteredIncrementedQueue.get() + " " + filteredIncrementedQueue.get())

  val incrementedFilteredQueue = new BasicIntQueue with Filtering with Incrementing
  incrementedFilteredQueue.put(-1); incrementedFilteredQueue.put(0); incrementedFilteredQueue.put(1)
  println(incrementedFilteredQueue.get() + " " + incrementedFilteredQueue.get() + " " + incrementedFilteredQueue.get())
}

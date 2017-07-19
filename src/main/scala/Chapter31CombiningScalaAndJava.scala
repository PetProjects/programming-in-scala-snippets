/**
  * Created by Igor_Dobrovolskiy on 19.07.2017.
  */


/* All next annotations are translated to Java equivalent */
//@deprecated
//@volatile
//@serializable => scala.Serializable with java.io.Serializable
//@SerialVersionUID(xL) => private final static long SerialVersionUID = xL;

import java.io._
import java.util

import scala.collection.mutable

class Reader(fname: String) {
  private val in = new BufferedReader(new FileReader(fname))

  @throws(classOf[IOException])
  def read() = in.read()
}

object Tests {
  @Ignore
  def testData = List(0, 1, -2, 5, -5)

  def test1 = {
    assert(testData == (testData.head :: testData.tail))
  }
}

object Chapter31CombiningScalaAndJava extends App {
  for {
    method <- Tests.getClass.getMethods
    if method.getName.startsWith("test")
  } {
    if (method.getAnnotation(classOf[Ignore]) != null)
      println("found Ignored method: " + method)
    else
      println("found test method: " + method)
  }

  abstract class SetAndType {
    type Elem
    val set: mutable.Set[Elem]
  }

  def javaSetToScalaSet[T](jset: util.Collection[T]): SetAndType = {
    val sset = mutable.Set.empty[T]

    val iter = jset.iterator
    while (iter.hasNext)
      sset += iter.next()

    new SetAndType {
      type Elem = T
      val set = sset
    }
  }

  val sat = javaSetToScalaSet((new Wild).contents())

  println(sat.set)

  //java 8 + Scala 2.12 integration
  trait Increaser {
    def increase(i: Int): Int
  }

  def increaseOne(inc: Increaser): Int = inc.increase(1)

  increaseOne(i => i + 7)

  val jstream = java.util.Arrays.stream(Array(1, 2, 3))
  val mappedjs = jstream.map(i => i + 1)
  println(mappedjs.toArray.mkString(", "))

  val f: Int => Int = _ + 1
  //val mappedjs2 = jstream.map(f).toArray //Error:(80, 31) type mismatch; found   : Int => Int required: java.util.function.IntUnaryOperator
  val f2: java.util.function.IntUnaryOperator = _ + 1
//  jstream.map(f2).toArray //compiles, but will fail because java stream was already iterated
}

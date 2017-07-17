import scala.collection.{IndexedSeqLike, mutable}
import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.{ArrayBuffer, Builder, MapBuilder}

/**
  * Created by Igor_Dobrovolskiy on 14.07.2017.
  */

abstract class Base

case object A extends Base

case object T extends Base

case object G extends Base

case object U extends Base

object Base {
  val fromInt: Int => Base = Array(A, T, G, U)
  val toInt: Base => Int = Map(A -> 0, T -> 1, G -> 2, U -> 3)
}

final class RNA private(val groups: Array[Int], val length: Int)
  extends IndexedSeq[Base]
  with IndexedSeqLike[Base, RNA] //KEY extension to have RNA as result in map and ++, as IndexedSeq has "with IndexedSeqLike[A, IndexedSeq[A]]"
{

  import RNA._

  // Mandatory re-implementation of ‘newBuilder‘ in ‘IndexedSeq‘
  override protected[this] def newBuilder: Builder[Base, RNA] =
    RNA.newBuilder

  // Mandatory implementation of ‘apply‘ in ‘IndexedSeq‘
  def apply(idx: Int): Base = {
    if (idx < 0 || length <= idx)
      throw new IndexOutOfBoundsException
    Base.fromInt(groups(idx / N) >> (idx % N * S) & M)
  }

  // Optional reimplementation of foreach,
  // to make it more efficient.
  override def foreach[U](f: Base => U): Unit = {
    var i = 0
    var b = 0
    while (i < length) {
      b = if (i % N == 0) groups(i / N) else b >>> S
      f(Base.fromInt(b & M))
      i += 1
    }
  }
}

object RNA {
  private val S = 2 // number of bits in group
  private val M = (1 << S) - 1 // bitmask to isolate a group
  private val N = 32 / S // number of groups in an Int
  def fromSeq(buf: Seq[Base]): RNA = {
    val groups = new Array[Int]((buf.length + N - 1) / N)
    for (i <- 0 until buf.length)
      groups(i / N) |= Base.toInt(buf(i)) << (i % N * S)
    new RNA(groups, buf.length)
  }

  def apply(bases: Base*) = fromSeq(bases)

  def newBuilder: Builder[Base, RNA] =
    new ArrayBuffer mapResult fromSeq

  implicit def canBuildFrom: CanBuildFrom[RNA, Base, RNA] = //KEY code to have RNA as result in map and ++
    new CanBuildFrom[RNA, Base, RNA] {
      def apply(): Builder[Base, RNA] = newBuilder

      def apply(from: RNA): Builder[Base, RNA] = newBuilder
    }
}

import collection._

class PrefixMap[T]
  extends mutable.Map[String, T]
    with mutable.MapLike[String, T, PrefixMap[T]] {
  var suffixes: immutable.Map[Char, PrefixMap[T]] = Map.empty
  var value: Option[T] = None

  def get(s: String): Option[T] =
    if (s.isEmpty) value
    else suffixes get (s(0)) flatMap (_.get(s substring 1))

  def withPrefix(s: String): PrefixMap[T] =
    if (s.isEmpty) this
    else {
      val leading = s(0)
      suffixes get leading match {
        case None =>
          suffixes = suffixes + (leading -> empty)
        case _ =>
      }
      suffixes(leading) withPrefix (s substring 1)
    }

  override def update(s: String, elem: T) =
    withPrefix(s).value = Some(elem)

  override def remove(s: String): Option[T] =
    if (s.isEmpty) {
      val prev = value; value = None; prev
    }
    else suffixes get (s(0)) flatMap (_.remove(s substring 1))

  def iterator: Iterator[(String, T)] =
    (for (v <- value.iterator) yield ("", v)) ++
      (for ((chr, m) <- suffixes.iterator; (s, v) <- m.iterator) yield (chr +: s, v))

  def +=(kv: (String, T)): this.type = {
    update(kv._1, kv._2); this
  }

  def -=(s: String): this.type = {
    remove(s); this
  }

  override def empty = new PrefixMap[T]
}

object PrefixMap {
  def empty[T] = new PrefixMap[T]

  def apply[T](kvs: (String, T)*): PrefixMap[T] = {
    val m: PrefixMap[T] = empty
    for (kv <- kvs)
      m += kv
    m
  }

  def newBuilder[T]: Builder[(String, T), PrefixMap[T]] =
    new MapBuilder[String, T, PrefixMap[T]](empty)

  implicit def canBuildFrom[T]: CanBuildFrom[PrefixMap[_], (String, T), PrefixMap[T]] =
    new CanBuildFrom[PrefixMap[_], (String, T), PrefixMap[T]] {
      def apply(from: PrefixMap[_]) = newBuilder[T]

      def apply() = newBuilder[T]
    }
}

object Chapter25ScalaCollectionsArchitecture extends App {
  /** ** TODO: Reread 25.2 section ending tomorrow!!!!!!!!! ******/

  val rna =  RNA(A, G, T, A, U, A)
  val rna2 = RNA(A, A, U, T, U)
  val equalList = rna zip rna2 map { case (a, b) => a == b } toList

  println(equalList)
  println((rna ++ rna2).getClass)
  println((rna map {case A => T case x => x}).getClass)

  val pm = PrefixMap("hello" -> 1, "hell" -> 2, "hey" -> 3, "hi" -> 4)
  println(pm)
  println(pm withPrefix "he")

  val pm2 = (pm map { case (k, v) => (k, "_" * v) })
  println(pm2, pm2.getClass)
}

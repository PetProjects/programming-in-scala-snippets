import scala.collection.immutable.{BitSet, HashMap, HashSet, TreeMap, TreeSet}
import scala.collection.mutable
import scala.reflect.ClassTag

/**
  * Created by Igor_Dobrovolskiy on 13.07.2017.
  */

/*

Traversable
	Iterable
		Seq
			IndexedSeq
				Vector
				ResizableArray
				GenericArray
			LinearSeq
				MutableList
				List
				Stream
			Buffer
				ListBuffer
				ArrayBuffer
		Set
			SortedSet
				TreeSet
			HashSet (mutable)
			LinkedHashSet
			HashSet (immutable)
			BitSet
			EmptySet, Set1, Set2, Set3, Set4
		Map
			SortedMap
				TreeMap
			HashMap (mutable)
			LinkedHashMap (mutable)
			HashMap (immutable)
			EmptyMap, Map1, Map2, Map3, Map4

  */


object Chapter24CollectionsInDepth extends App {
  println(List(1, 2).reduceLeft(_ + _), List(1).reduceLeft(_ + _) /* , List[Int]().reduceLeft(_ + _)  throws exception */)

  println(List(1, 2, 3, 4, 5).slice(1, 3))
  println(Set(1, 11, 19, 2, 3, 4, 5).slice(0, 3))

  val ar = Array.ofDim[Int](5)
  List(1, 2, 3) copyToArray ar
  println(ar.mkString)
  List(7, 7, 7).copyToArray(ar, 1, 100) // no exception
  println(ar.mkString)

  val buf = mutable.ArrayBuffer.empty[Int]
  List(1, 2, 3) copyToBuffer buf
  List(1, 2, 3) copyToBuffer buf //appends
  println(buf.mkString)

  ar.transform(x => 0) //clean up
  List(1, 2, 3) copyToArray ar
  List(1, 2, 3) copyToArray ar //rewrites
  println(ar.mkString)

  val ll = List(List(1), List(2, 3), List(2, 3, 4), List())
  val v = ll collect { case a :: _ :: b :: _ => a + b }
  val v2 = ll map {
    case a :: _ :: b :: _ => a + b
    case _ => -1 //throws exception otherwise
  }
  println(v.mkString(" "), "|", v2.mkString(" "))

  val l: Iterable[Int] = List(1, 2, 3, 4, 5)
  for (e <- l.grouped(3)) println(e)
  for (e <- l.sliding(3)) println(e)

  println((l zipAll(l takeRight 3, 9, 0)).mkString(","))
  println((l take 3 zipAll(l, 9, 0)).mkString(","))
  println("Same elements (toLong): " + (l sameElements l.map(_.toLong))) // true
  println("Same elements (toSet): " + (l sameElements l.map(_.toLong).toSet)) // false (order differs)

  /** PartialFunction implementation tests for Seq, Set, Map traits */
  val seq = Seq(4, 3, 2, 1)
  println(seq {
    3
  }) //returns index
  val set = Set(1, 2, 3, 4)
  println(set {
    3
  }) //returns true/false ("contains")
  val map = Map(1 -> 'a', 2 -> 'b', 3 -> 'c')
  println(map {
    3
  }) //returns value for the key
  //map {4} //NoSuchElementException

  /** Seq */
  //indexOf, lastIndexOf, indexOfSlice, lastIndexOfSlice, indexWhere, lastIndexWhere, segmentLength, prefixLength
  println(seq contains 0, //contains
    seq isDefinedAt 0) //contains in indices
  val seq2 = Seq(1, 2, 1, 1, 3)
  println(seq2 indexOfSlice Seq(1, 1))
  println(seq2 segmentLength(_ == 1, 1)) //==prefixLength with skip (do not search)

  //+:  :+  padTo
  println(seq.padTo(10, 0)) //total length 10

  println(seq.patch(1, seq2, 2)) // inserts whole seq2 in the place of 2 seq's elements
  println(seq.updated(1, 9))

  println(seq.sorted)
  println(seq sortWith (_ > _))
  println(seq sortBy (_ % 2))

  // reverse, reverseIterator, reverseMap

  //corresponds == analog of customized equals??
  println((Seq("a", "b", "c") corresponds Seq("A", "B", "C")) (_.toLowerCase == _.toLowerCase))
  println((Seq("a", "b", "c") corresponds Seq("A", "B", "C", "D")) (_.toLowerCase == _.toLowerCase)) //false
  println((Seq("a", "b", "c") corresponds Seq("A", "B")) (_.toLowerCase == _.toLowerCase)) //false
  println((Seq("a", "b", "c") corresponds Seq("A", "C", "B")) (_.toLowerCase == _.toLowerCase)) //false

  //intersect, diff, union, distinct

  /** Buffers */
  // += x, += (x, y, z)
  // ++=, +=:, ++=:, insert, insertAll
  // -= (element), remove(index), remove(i, count), trimStart, trimEnd, clear()
  // clone

  /** Sets */
  val s1 = Set(1, 2, 3)
  val s2 = Set(2, 3, 4)
  println(s1 & s2) //= intersect
  println(s1 | s2) //= union (= ++ for Traversable)
  println(s1 &~ s2) //diff, returns (1)
  println(s1 -- s2) //same, but works for Traversable

  //mutable sets
  // xs += x, returns xs with x
  // xs add x, returns true, if there was no x before, false otherwise
  // -= x, -= (x, y, z)
  // xs remove x, true if x was contained
  val mset = mutable.Set(1, 2, 3, 4)
  mset retain (_ % 2 == 0)
  println(mset)
  //clear
  //xs(x) = b, if b == true adds x, removes otherwise
  //clone
  // IMPORTANT: avoid using ++, -- on mutable sets (use +=, -= instead)

  /** Maps */
  // apply(x) throws exception if not found
  // get(x) wraps in Option()
  // getOrElse(), contains(), isDefinedAt

  val m = Map(1 -> "A", 2 -> "B", 3 -> "C")
  println(m isDefinedAt 1) // = contains (calls it)
  println(m.keys.getClass) //Iterable
  m.keysIterator //Iterator
  m.keySet //Set
  // valuesIterator
  println(m.values.getClass) //Iterable

  //filterKeys, mapValues .. return new Map

  //m + (k -> v) === m updated (k, v)
  // all next are for keys: m - k, m - (k, l, m), m -- ks

  // ***** Mutable Map *******
  // m += (k, v) returns map
  // m put (k,v) returns previously associated value wrapped in Option()
  // m getOrElseUpdate (k, d) returns v if any, d otherwise (does update in "otherwise" case only) ...
  //                                          USEFUL FOR CACHES!!! 2nd argument is "by-name" so is only executed when required

  val m2 = m.transform((k, v) => (v.toLowerCase()) (0)) // map()-like, NOT in-place, returns new map
  println(m2, m)
  //val v2 = m.map((k, v) => v(0)) // do not compile! why?!

  /** Streams */
  val stream = 1 #:: 2 #:: 3 #:: Stream.empty
  println(stream)

  def fibFrom(a: Long, b: Long): Stream[Long] =
    a #:: fibFrom(b, a + b) //no infinite recursion!

  val stream2 = fibFrom(0, 1)
  for (i <- 0 to 10) print(stream2(i) + " ");
  println(s"... ${stream2(100)}")

  /** Vectors (are immutable) */
  val vec = Vector(1, 2, 3)
  println(vec updated(2 /*index*/ , 5), vec)

  /** Immutable stacks are used rarely, since List provides the same func. */

  /** Immutable queues */
  var q = scala.collection.immutable.Queue[Int]()
  q = q.enqueue(1)
  q = q.enqueue(List(2, 3))
  val (element, queue23) = q.dequeue

  /** Ranges */
  1 to 5
  1 to 10 by 3
  1 until 3 // 1,2

  /** Hash tries : HashSet, HashMap ... 32 values in each node, similar to Vector */
  val hs = HashSet(1, 2, 3)
  val hm = HashMap(1 -> "@", 2 -> "%")
  //mutable analogs are available

  /** Red-black trees based collections: TreeSet, TreeMap, SortedSet */
  val ts = TreeSet.empty[Int]
  set + 1 + 3 + 3

  /** Bit sets */
  val bs = BitSet.empty
  println(bs + 2 + 3 + 5 + 3)
  println((bs + 2 + 5).toBitMask(0))
  println(BitSet(2, 5, 7).toBitMask(0))

  /** ListMap .. no actual use, except first elements are accessed much move frequently than the rest */

  /** ArrayBuffer -- for constructing arrays by appending to the end */

  /** ListBuffer -- for constructing lists by appending to the end */

  /** StringBuilder -- similar for strings */

  //mutable.LinkedList // deprecated!
  //mutable.DoubleLinkedList //deprecated!
  mutable.MutableList //std. for mutable.LinearSeq

  val as = mutable.ArraySeq(1, 2, 3)
  println(as) // internally is Array, but behaves as "generic"

  /** Mutable Stacks */
  val ms = mutable.Stack[Int]()
  ms.push(1)
  ms.push(2)
  ms.top //2
  ms.pop //2
  ms.top //1

  //ArrayStack is generally more preferable that mutable.Stack

  // Hash table based: HashSet, HashMap. LinkedHashSet or LinkedHashMap = Hash* + LinkedList, thus iterates in the order of adding

  mutable.WeakHashMap // wrapper for java.util.WeakHashMap ... elements which have no active reference elsewhere could be garbage collected

  //ConcurrentMap ... how to create??!

  /** Arrays */

  //Array .. although java array analog can be treated as scala.collections.Seq
  //          (by means of implicit conversion to wrapper class, scala.collections.mutable.WrappedArray)
  //  ... it's a kind of boxing for Array-s

  // Alternatively ArrayOps, is used when seq operations are applied to Array w/o explicit convertion to Seq:
  val ss: Seq[Int] = Array(1) //WrappedArray
  val aa = Array(1) map (_ * 2) //Array

  def evenElems[T: ClassTag](xs: List[T]): Array[T] = { // won't compile w/o ": ClassTag"
    val arr = new Array[T]((xs.length + 1) / 2)
    for (i <- xs.indices by 2) // indices are of Range type
      arr(i / 2) = xs(i)
    arr
  }

  /** String */

  // Similarly to Array-s, StringOps and WrappedString take place

  /** Views */
  val arr = (0 to 9).toArray
  val subarr = arr.view.slice(3, 6) // no copy
  for (i <- subarr.indices) subarr(i) = -subarr(i)
  println(arr.mkString(" "))

  /** Iterators */
  // next(), hasNext
  val it = arr.iterator
  it.foreach(_ => {})
  //  for (e <- it) print(e); println() // will fail, as it was already iterated
  // it.map will return another iterator, but 1st iterator will "fire" once "spawned iterator" was iterated over
  // val(it1, it2) = it.duplicate -- the only exception: it1 and it2 can be iterated independently, BUT IT CAN"T BE USED ANYWAY

  //it.buffered
  //it grouped size
  //it sliding size
  //it copyToBuffer buf
  //it copyToArray(arr, s?, l?)
  //it ++ jt
  //it padTo (len, x)
  // map, flatMap (iterator valued!), collect, toArray, toList, toIterable, toSeq, toIndexedSeq, toStream, toSet, toMap
  //isEmpty, nonEmpty, size -- iterates over it!!!, length -- same !!!
  //find, indexOf, indexWhere, take, drop, slice, takeWhile, dropWhile, filter, withFilter (=filter in case of iterator, needed for "for monoid")
  //partition, forall, exists, count, /:, :\, reduceLeft, reduceRight, sum, product, min, max
  //it zip jt
  //it.zipWithIndex
  //it patch (i, jt, r)
  //it sameElements jt
  //it addString/mkString ([b,]? start, sep, end)

  /** Buffered iterators */
  def skipEmptyWords(it: BufferedIterator[String]) =
    while (it.head.isEmpty) {
      it.next()
    }

  //it.buffered returns BufferedIterator

  /** Create collections */
  Seq.empty[Int]
  Seq.concat()
  Seq.fill(1)(1)
  Seq.tabulate(1)((i: Int) => i)
  Seq.range(1, 10)
  Seq.iterate(0, 10)(_ + 1)
}

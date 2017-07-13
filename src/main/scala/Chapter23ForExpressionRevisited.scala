/**
  * Created by Igor_Dobrovolskiy on 13.07.2017.
  */

trait ForMonad[A]
/*extends Iterable[A]*/ // Conflicts with trait FilterMonadic[+A, +Repr] when derived from Iterable[A]
{
  def map[B](f: A => B): ForMonad[B] // Iterable[B]?
  def flatMap[B](f: A => ForMonad[B] /** Iterable[B]? */): ForMonad[B] // Iterable[B]?
  def withFilter(p: A => Boolean): ForMonad[A]
  def foreach(b: A => Unit): Unit
}

trait ForMonad2[A] {
  def map[B](f: A => B): Iterable[B]
  def flatMap[B](f: A => Iterable[B]): Iterable[B]
  def withFilter(p: A => Boolean): ForMonad[A]
  def foreach(b: A => Unit): Unit
}

object Chapter23ForExpressionRevisited extends App {

  def isCheck(q1: (Int, Int), q2: (Int, Int)) =
    q1._1 == q2._1 ||
      q1._2 == q2._2 ||
      (q1._1 - q2._1).abs == (q1._2 - q2._2).abs

  def isSafe(queen: (Int, Int), queens: List[(Int, Int)]) =
    queens forall (q => !isCheck(queen, q))

  def queens(n: Int): List[List[(Int, Int)]] = {
    def placeQueens(k: Int): List[List[(Int, Int)]] =
      if (k == 0) List(List())
      else
        for {
          queens <- placeQueens(k - 1)
          column <- 1 to n
          queen = (k, column)
          if (isSafe(queen, queens))
        } yield queen :: queens

    placeQueens(n)
  }

  println(queens(0))
  println(queens(1))
  println(queens(2))
  println(queens(3))
  println(queens(4))
  println(queens(8))

  def forMonadExperiments ={
    val a: ForMonad[Int] = null
    for(i <- a) yield i //not compile if comment out map in ForMonad
    for(i <- a if i > 0) yield i //not compile if comment out withFilter in ForMonad
    for(i <- a; j <- a) yield i + j //not compile if comment out flatMap in ForMonad
    for(i <- a) {} //not compile if comment out foreach in ForMonad
    for(i <- a; j <- a) {} //not compile if comment out foreach in ForMonad

    val a2: ForMonad2[Int] = null
    //all compiles, no matter return value is changed
    for(i <- a2) yield i
    for(i <- a2 if i > 0) yield i
    for(i <- a2; j <- a2) yield i + j
    for(i <- a2) {}
    for(i <- a2; j <- a2) {}

    val b: AnyRef = null
    //for(i <- b) {} //EXPECTED: not compile
  }
}

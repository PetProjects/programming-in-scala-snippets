import scala.concurrent.{Future, Promise}

/**
  * Created by Igor_Dobrovolskiy on 19.07.2017.
  */
object Chapter32FuturesAndConcurrency extends App {

  //Java way of sync
  var counter = 0
  Seq(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).par.foreach(x => {
    println("thread id=" + java.lang.Thread.currentThread().getId)
    synchronized {
      counter += x
    }
  })
  println(counter)

  val TimeoutBasis = 100 // 10000

  //Scala Future-s
  import scala.concurrent.ExecutionContext.Implicits.global

  val fut = Future {
    Thread.sleep(10 * TimeoutBasis);
    21 + 21
  }
  val r = fut.map(_ + 1)
  println(r.value)
  while (r.value == None) {
    Thread.sleep(TimeoutBasis)
    print(".")
  }
  println("\n" + r.value)

  val fut1 = Future {
    Thread.sleep(10 * TimeoutBasis);
    23 + 23
  }
  val fut2 = Future {
    Thread.sleep(10 * TimeoutBasis);
    21 + 21
  }

  val ff = for { // works in parallel
    x <- fut1
    y <- fut2
  } yield x + y

  while (ff.value == None) {
    Thread.sleep(TimeoutBasis)
    print(".")
  }
  println("\n" + ff.value)

  val ff2 = for { // won't work in parallel! i.e. will take 2x time
    x <- Future {
      Thread.sleep(10 * TimeoutBasis);
      23 + 23
    }
    y <- Future {
      Thread.sleep(10 * TimeoutBasis);
      21 + 21
    }
  } yield x + y

  //Future companion object:
  Future.successful {
    21 + 21
  }
  Future.failed(new Exception("boom!"))
  Future.fromTry(util.Success {
    21 + 21
  })
  Future.fromTry(util.Failure(new Exception("baga boom!")))

  /* Promise-s */
  val pro = Promise[Int]
  pro.future
  pro.success(21 + 21)
  //  pro.failure(new Exception("promised boom!")) //can't do after success
  //  pro.complete(util.Success(1+1))
  //  pro.complete(util.Failure(new Exception("another promised boom!")))

  /* Future's aux */

  //helper func
  def wait(fut: Future[_]) = while (fut.value.isEmpty) {
    Thread.sleep(TimeoutBasis)
  }

  val fut3 = Future { 42 }
  fut3.filter(_ < 0).value //Some(Failure(NoSuchElementException)
  for (res <- fut if res < 0 /* withFilter applied */ ) yield res + 1 // Some(Failure(NoSuchElementException)
  fut3 collect { case res if res < 0 => res + 46 } //Some(Failure(NoSuchElementException)

  val failure = Future {
    42 / 0
  }
  failure.value //Some(Failure(ArithmeticException))
  failure.failed.value //Some(Success(ArithmeticException))

  val success = Future {
    42 / 1
  }
  success.failed.value //Some(Failure(NoSuchElementException))

  println(failure.fallbackTo(success)) //Future(Success(42))

  val failedFallback = Future { 42 / 0 }.fallbackTo(
    Future { val res = 42; require(res < 0); res }
  )

  wait(failedFallback); println(failedFallback.value)//Some(Failure(java.lang.ArithmeticException: / by zero))

  val successFallback = Future { 42 / 1 }.fallbackTo(
    Future { val res = 42; require(res < 0); res }
  )
  wait(successFallback); println(successFallback.value)//Some(Success(42))

  val recoveredFallback = Future { 42 / 0 }.recover {
    case _: ArithmeticException => -1 //!! If fails with not defined exception, original fail (success) propagates
  }
  wait(recoveredFallback); println(recoveredFallback.value) //Some(Success(-1))

  val safeRecoveredFallback = Future { 42 / 1 }.recover{
    case _: ArithmeticException => -1 //!! If fails with not defined exception, original fail (success) propagates
  }
  wait(safeRecoveredFallback); println(safeRecoveredFallback.value)//Some(Success(42))

  val recoverWithFallback = Future { 42 / 0 }.recoverWith {
    case _: ArithmeticException => Future{ -1 } //!! If fails with not defined exception, original fail (success) propagates
  }
  wait(recoverWithFallback); println(recoverWithFallback.value) //Some(Success(-1))

  val transFallback = Future { 23 + 23 }.transform(_ * -1, ex => new Exception("wrapped ex", ex))
  wait(transFallback); println(transFallback.value) //Some(Success(-46))

  val transFailedFallback = Future { 1 / 0 }.transform(_ * -1, ex => new Exception("wrapped ex", ex))
  wait(transFailedFallback); println(transFailedFallback.value) //Some(Failure(Exception(ArithEx))

  //since scala 2.12:
  val transFallback2 = Future { 23 + 23 }.transform{
    case util.Success(res) => util.Success(res * -2)
    case util.Failure(ex) => util.Failure(new Exception("wrapped ex", ex))
  }
  wait(transFallback2); println(transFallback2.value) //Some(Success(-92))

  val transFailedFallback2 = Future { 1 / 0 }.transform{
    case util.Success(res) => util.Success(res * -2)
    case util.Failure(ex) => util.Failure(new Exception("wrapped ex", ex))
  }
  wait(transFailedFallback2); println(transFailedFallback2.value) //Some(Failure(Exception(ArithEx))

  val transFailedToSuccessFallback = Future { 1 / 0 }.transform {
    case util.Success(res) => util.Success(res * -2)
    case util.Failure(_) => util.Success(-1)
  }
  wait(transFailedToSuccessFallback); println(transFailedToSuccessFallback.value) //Some(Success(-1))

  /* zip, fold, ... */
  println(success zip recoveredFallback)
  println(success zip failure) //either fail -- zip fails
  println(failure zip transFailedFallback) //both fail -- only 1st exception

  val futures = List(Future {
    1
  }, Future {
    2
  }, Future {
    3
  })

  //@deprecated("use Future.foldLeft instead", "2.12.0")
  val foldedFs = Future.fold(futures)(100) {(acc, num) => acc + num }
  wait(foldedFs); println("Future.fold: " + foldedFs.value)

  //@deprecated("use Future.reduceLeft instead", "2.12.0")
  val reducedFs = Future.reduce(futures) {(acc, num) => acc + num }
  wait(reducedFs); println("Future.reduce: " + reducedFs.value)

//  Future.reduce(List()) {(acc, _) => acc } //NoSuchElementException

  val fsValues = Future.sequence(futures)
  wait(fsValues); println("Future.sequence: " + fsValues.value)

  val traversedFsValues = Future.traverse(1 to 10)(i => Future{i})
  wait(traversedFsValues); println("Future.sequence: " + traversedFsValues.value)

  /* Performing side-effects */

  failure.foreach(ex => println(ex)) //no effect for fails
  success.foreach(i => println(i)) //prints

  for(i <- success) println("'for' way: " + i)

  val pf: PartialFunction[util.Try[Int], Unit] = {
    case util.Success(res) => Thread.sleep(100); println(res)
    case util.Failure(ex) => println(ex)
  }

  //it is unknown which one will be first of next two:
  success onComplete pf
  failure onComplete pf

  val pf2: PartialFunction[util.Try[Int], Unit] = {
    case util.Success(res) => println("2:" + res)
    case util.Failure(ex) => println("2:" + ex)
  }

  //no proceeding until callback completes, but if callback fails, there is no exception propagation from it:
  wait(success andThen pf2)
  wait(failure andThen pf2)

  /* Other methods, since 2.12 */

  Future { Future { 12 }}.flatten // => Future(Success(12))
  val fu1 = Future { 12 + 12}
  val fu2 = Future {"ans" + "wer"}
  fu1.zipWith(fu2) {case (num, str) => s"$num is the $str"} // Future(Success("24 is the answer"))
  success.transformWith { //similar to new signature of transform, but allows yielding Future-s, instead of Try-s
    case util.Success(r) => Future {r}
    case util.Failure(ex) => {throw new Exception}
  }

  import scala.concurrent.Await
  import scala.concurrent.duration._

  def wait2(ff: Future[_]) = Await.result(ff, 15.seconds)
}

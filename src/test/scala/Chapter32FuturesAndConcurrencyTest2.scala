import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.concurrent.ScalaFutures._


/**
  * Created by Igor_Dobrovolskiy on 19.07.2017.
  */
class Chapter32FuturesAndConcurrencyTest2 extends FlatSpec with Matchers with BeforeAndAfter {

  var fut: Future[Int] = _

  before {
    fut = Future { Thread.sleep(5000); 21 + 21}
  }

  "ScalaFutures" should "make it easy to test Future's results" in {

    fut.futureValue(Timeout(Span(6, Seconds))) should be (42)
  }
}

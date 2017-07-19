import org.scalatest._

import scala.concurrent.Future


/**
  * Created by Igor_Dobrovolskiy on 19.07.2017.
  */
class Chapter32FuturesAndConcurrencyTest extends AsyncFunSpec {

  def addSoon(addends: Int*): Future[Int] = Future { addends.sum }

  describe("addSoon") {
    it("will eventually compute a sum of passed Ints") {
      val futureSum = addSoon(1,2)

      //map assertion into future, return Future[Assertion]

      futureSum map { sum => assert(sum == 3)}
    }
  }
}



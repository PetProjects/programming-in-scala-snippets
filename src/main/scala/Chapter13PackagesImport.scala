/**
  * Created by Igor_Dobrovolskiy on 07.07.2017.
  */

package launch {
  class Booster3
}

package bobsrockets {
  package navigation {
    package launch {
      class Booster1
    }
    class MissionControl {
      val booster1 = new launch.Booster1
      val booster2 = new bobsrockets.launch.Booster2
      val booster3 = new _root_.launch.Booster3
    }
  }
  package launch {
    class Booster2
  }
}

import java.io.{File=>F, _}
import scala.util.{Failure => _, Random => R, _} //No failures are imported! )

object Chapter13PackagesImport extends App {
  val f: F = new F("./1.txt")
  println(f.getAbsolutePath)

  println(R.nextDouble())
}

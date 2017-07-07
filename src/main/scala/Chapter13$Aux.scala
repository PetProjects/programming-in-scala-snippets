/**
  * Created by Igor_Dobrovolskiy on 07.07.2017.
  * (auxiliary part on the protection qualifiers)
  */
package bobsrockets

package navigation {
  private[bobsrockets] /** thus visible to all subbackages, classes, objects in bobsrockets, including launch.Vehicle. But can't be accessed outside. */
  class Navigator {
    protected[navigation] /** within navigation package and derived classes */
    def useStarChart() ={}
    class LegOfJourney {
      private[Navigator] /** emulating private for inner class in Java */ val distance = 100
    }
    private[this] /** the most strict access: within this exact object (not class) */ var speed = 200
  }
}

package launch {
  import navigation._

  class Vehicle {
    protected val y: Int = 2
  }

  class CoolVehicle extends Vehicle{
    //Vehicle.x // Do not compile as has no sense (in scala)
    y //compiles just fine, as in class (not companion/static object)
  }

  object Vehicle{
    private[launch] val guide = new Navigator

    protected val x: Int = 1
  }


}
import org.stairwaybook.recipe._

/**
  * Created by Igor_Dobrovolskiy on 17.07.2017.
  */

object GotApples {
  def main(args: Array[String]) = {
    val db: Database =
      if (args.length > 0 && args(0) == "student")
        StudentDatabase
      else
        SimpleDatabase
    object browser extends Browser {
      //val database = db
      val database: db.type = db //singleton type!
                                 // for the last two lines to compile w/o "Error:(21, 31) type mismatch; found   : db.FoodCategory required:
                                 // browser.database.FoodCategory browser.displayCategory(category)"
    }
    val apple = SimpleDatabase.foodNamed("Apple").get
    for (recipe <- browser.recipesUsing(apple))
      println(recipe)

    for (category <- db.allCategories)
      browser.displayCategory(category)
  }
}

object Chapter29ModularProgrammingWithObjects extends App {

}

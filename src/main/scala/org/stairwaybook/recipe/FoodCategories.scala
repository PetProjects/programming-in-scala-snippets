package org.stairwaybook.recipe

/**
  * Created by Igor_Dobrovolskiy on 18.07.2017.
  */

trait FoodCategories {

  case class FoodCategory(name: String, foods: List[Food])

  def allCategories: List[FoodCategory]
}
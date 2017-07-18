package org.stairwaybook.recipe

/**
  * Created by Igor_Dobrovolskiy on 18.07.2017.
  */

abstract class Database extends FoodCategories {
  def allFoods: List[Food]
  def allRecipes: List[Recipe]
  def foodNamed(name: String) =
    allFoods.find(f => f.name == name)
}
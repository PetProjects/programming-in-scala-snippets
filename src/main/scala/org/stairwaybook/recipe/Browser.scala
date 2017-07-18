package org.stairwaybook.recipe

/**
  * Created by Igor_Dobrovolskiy on 18.07.2017.
  */

abstract class Browser {
  val database: Database

  def recipesUsing(food: Food) =
    database.allRecipes.filter(recipe =>
      recipe.ingredients.contains(food))

  def displayCategory(category: database.FoodCategory) = {
    println(category)
  }
}
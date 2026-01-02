package aoc.year2015

import aoc.common.input.AoCFileInput
import aoc.common.input.StructuredInput
import aoc.year2015.entity.Ingredient
import aoc.year2015.entity.IngredientCompanion

class Day15 : AoCFileInput<List<Ingredient>, Int>() {
    override val inputFunction
        get() =
            StructuredInput(
                regex =
                    Regex(
                        "(?<name>\\w+): capacity (?<capacity>-?\\d+), durability (?<durability>-?\\d+), " +
                            "flavor (?<flavor>-?\\d+), texture (?<texture>-?\\d+), calories (?<calories>-?\\d+)",
                    ),
                builder = IngredientCompanion::fromLine,
            )::getStructInput

    private val teaSpoons: IntArray by lazy {
        IntArray(input.size) { 0 }
    }

    /**
     * Today, you set out on the task of perfecting your milk-dunking cookie recipe.
     * All you have to do is find the right balance of ingredients.
     *
     * Your recipe leaves room for exactly 100 teaspoons of ingredients.
     * You make a list of the remaining ingredients you could use to finish the recipe (your puzzle input)
     * and their properties per teaspoon:
     *
     *     capacity (how well it helps the cookie absorb milk)
     *     durability (how well it keeps the cookie intact when full of milk)
     *     flavor (how tasty it makes the cookie)
     *     texture (how it improves the feel of the cookie)
     *     calories (how many calories it adds to the cookie)
     *
     * You can only measure ingredients in whole-teaspoon amounts accurately,
     * and you have to be accurate so you can reproduce your results in the future.
     * The total score of a cookie can be found by adding up each of the properties (negative totals become 0)
     * and then multiplying together everything except calories.
     *
     * For instance, suppose you have these two ingredients:
     *
     * Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
     * Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
     *
     * Then, choosing to use 44 teaspoons of butterscotch and 56 teaspoons of cinnamon (because the
     * amounts of each ingredient must add up to 100) would result in a cookie with the following properties:
     *
     *     A capacity of 44*-1 + 56*2 = 68
     *     A durability of 44*-2 + 56*3 = 80
     *     A flavor of 44*6 + 56*-2 = 152
     *     A texture of 44*3 + 56*-1 = 76
     *
     * Multiplying these together (68 * 80 * 152 * 76, ignoring calories for now) results in a total
     * score of 62842880, which happens to be the best score possible given these ingredients.
     * If any properties had produced a negative total, it would have instead become zero,
     * causing the whole score to multiply to zero.
     *
     * Given the ingredients in your kitchen and their properties, what is the total score of
     * the highest-scoring cookie you can make?
     */
    override fun processPartOne(): Int = iterateCookies(false)
    // result 21 367 368 for part 1

    /**
     * Your cookie recipe becomes wildly popular! Someone asks if you can make another recipe that
     * has exactly 500 calories per cookie (so they can use it as a meal replacement).
     * Keep the rest of your award-winning process the same (100 teaspoons, same ingredients, same scoring system).
     *
     * For example, given the ingredients above, if you had instead selected 40 teaspoons of butterscotch
     * and 60 teaspoons of cinnamon (which still adds to 100), the total calorie count would be 40*8 + 60*3 = 500.
     * The total score would go down, though: only 57600000, the best you can do in such trying circumstances.
     *
     * Given the ingredients in your kitchen and their properties, what is the total score of the highest-scoring
     * cookie you can make with a calorie total of 500?
     */
    override fun processPartTwo(): Int = iterateCookies(true)
    // result 1 766 400 for part 2

    private fun iterateCookies(checkCalories: Boolean): Int {
        var bestScore = -1
        do {
            findNextAvailable()
            val score = getCookieScore(checkCalories)
            if (score > bestScore) {
                bestScore = score
            }
        } while (teaSpoons.first() < 100)
        return bestScore
    }

    private fun findNextAvailable() {
        for (index in teaSpoons.lastIndex downTo 0 step 1) {
            teaSpoons[index]++
            val sum = teaSpoons.sum()
            if (sum > 100) {
                teaSpoons[index] = 0
                continue
            } else if (sum < 100) {
                if (index == teaSpoons.lastIndex) {
                    teaSpoons[teaSpoons.lastIndex] = 101 - sum
                } else {
                    teaSpoons[teaSpoons.lastIndex] = 100 - sum
                }
            }
            break
        }
    }

    private fun getCookieScore(checkCalories: Boolean): Int {
        var capacity = 0
        var durability = 0
        var flavor = 0
        var texture = 0
        var calories = 0
        input
            .forEachIndexed { index, ingredient ->
                capacity += ingredient.capacity * teaSpoons[index]
                durability += ingredient.durability * teaSpoons[index]
                flavor += ingredient.flavor * teaSpoons[index]
                texture += ingredient.texture * teaSpoons[index]
                calories += ingredient.calories * teaSpoons[index]
            }
        if (
            (capacity < 0 || durability < 0 || flavor < 0 || texture < 0) ||
            (checkCalories && calories != 500)
        ) {
            return 0
        }
        return capacity * durability * flavor * texture
    }
}

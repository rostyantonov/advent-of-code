package aoc.year2016.entity

import aoc.ksp.GenerateStructure

@GenerateStructure(multiStructure = true, discriminatorField = "cmd")
sealed class ScrambleOperation {
    abstract fun execute(chars: CharArray): CharArray

    abstract fun reverse(chars: CharArray): CharArray

    data class Position(
        val x: Int,
        val y: Int,
    ) : ScrambleOperation() {
        override fun execute(chars: CharArray): CharArray {
            val result = chars.copyOf()
            result[x] = chars[y]
            result[y] = chars[x]
            return result
        }

        override fun reverse(chars: CharArray): CharArray = execute(chars) // Self-inverse
    }

    data class Letter(
        val x: String,
        val y: String,
    ) : ScrambleOperation() {
        override fun execute(chars: CharArray): CharArray {
            val result = chars.copyOf()
            val xChar = x[0]
            val yChar = y[0]
            for (i in result.indices) {
                when (result[i]) {
                    xChar -> result[i] = yChar
                    yChar -> result[i] = xChar
                }
            }
            return result
        }

        override fun reverse(chars: CharArray): CharArray = execute(chars) // Self-inverse
    }

    data class Left(
        val steps: Int,
    ) : ScrambleOperation() {
        override fun execute(chars: CharArray): CharArray {
            val n = chars.size
            val effectiveSteps = steps % n
            if (effectiveSteps == 0) return chars.copyOf()

            val result = CharArray(n)
            // Copy from effectiveSteps to end → result[0..]
            System.arraycopy(chars, effectiveSteps, result, 0, n - effectiveSteps)
            // Copy from 0 to effectiveSteps → result[n-effectiveSteps..]
            System.arraycopy(chars, 0, result, n - effectiveSteps, effectiveSteps)
            return result
        }

        override fun reverse(chars: CharArray): CharArray {
            // Reverse of left is right
            val n = chars.size
            val effectiveSteps = steps % n
            if (effectiveSteps == 0) return chars.copyOf()

            val result = CharArray(n)
            // Copy last effectiveSteps → result[0..]
            System.arraycopy(chars, n - effectiveSteps, result, 0, effectiveSteps)
            // Copy first n-effectiveSteps → result[effectiveSteps..]
            System.arraycopy(chars, 0, result, effectiveSteps, n - effectiveSteps)
            return result
        }
    }

    data class Right(
        val steps: Int,
    ) : ScrambleOperation() {
        override fun execute(chars: CharArray): CharArray {
            val n = chars.size
            val effectiveSteps = steps % n
            if (effectiveSteps == 0) return chars.copyOf()

            val result = CharArray(n)
            // Copy last effectiveSteps → result[0..]
            System.arraycopy(chars, n - effectiveSteps, result, 0, effectiveSteps)
            // Copy first n-effectiveSteps → result[effectiveSteps..]
            System.arraycopy(chars, 0, result, effectiveSteps, n - effectiveSteps)
            return result
        }

        override fun reverse(chars: CharArray): CharArray {
            // Reverse of right is left
            val n = chars.size
            val effectiveSteps = steps % n
            if (effectiveSteps == 0) return chars.copyOf()

            val result = CharArray(n)
            // Copy from effectiveSteps to end → result[0..]
            System.arraycopy(chars, effectiveSteps, result, 0, n - effectiveSteps)
            // Copy from 0 to effectiveSteps → result[n-effectiveSteps..]
            System.arraycopy(chars, 0, result, n - effectiveSteps, effectiveSteps)
            return result
        }
    }

    data class Based(
        val letter: String,
    ) : ScrambleOperation() {
        override fun execute(chars: CharArray): CharArray {
            val letterChar = letter[0]
            val index = chars.indexOf(letterChar)
            val rotations = 1 + index + (if (index >= 4) 1 else 0)
            // Rotate right
            val n = chars.size
            val effectiveSteps = rotations % n
            if (effectiveSteps == 0) return chars.copyOf()

            val result = CharArray(n)
            // Copy last effectiveSteps → result[0..]
            System.arraycopy(chars, n - effectiveSteps, result, 0, effectiveSteps)
            // Copy first n-effectiveSteps → result[effectiveSteps..]
            System.arraycopy(chars, 0, result, effectiveSteps, n - effectiveSteps)
            return result
        }

        override fun reverse(chars: CharArray): CharArray {
            // To reverse, we need to find what the original position was
            val letterChar = letter[0]
            val currentIndex = chars.indexOf(letterChar)

            for (originalIndex in chars.indices) {
                val rotations = 1 + originalIndex + (if (originalIndex >= 4) 1 else 0)
                val newIndex = (originalIndex + rotations) % chars.size
                if (newIndex == currentIndex) {
                    // Found the original position, rotate left to undo
                    val n = chars.size
                    val effectiveSteps = rotations % n
                    if (effectiveSteps == 0) return chars.copyOf()

                    val result = CharArray(n)
                    // Copy from effectiveSteps to end → result[0..]
                    System.arraycopy(chars, effectiveSteps, result, 0, n - effectiveSteps)
                    // Copy from 0 to effectiveSteps → result[n-effectiveSteps..]
                    System.arraycopy(chars, 0, result, n - effectiveSteps, effectiveSteps)
                    return result
                }
            }

            return chars
        }
    }

    data class Reverse(
        val x: Int,
        val y: Int,
    ) : ScrambleOperation() {
        override fun execute(chars: CharArray): CharArray {
            val result = chars.copyOf()
            var left = x
            var right = y
            while (left < right) {
                val temp = result[left]
                result[left] = result[right]
                result[right] = temp
                left++
                right--
            }
            return result
        }

        override fun reverse(chars: CharArray): CharArray = execute(chars) // Self-inverse
    }

    data class Move(
        val x: Int,
        val y: Int,
    ) : ScrambleOperation() {
        override fun execute(chars: CharArray): CharArray {
            val result = chars.copyOf()
            val char = result[x]

            if (x < y) {
                // Shift elements from x+1 to y left by 1
                System.arraycopy(result, x + 1, result, x, y - x)
                result[y] = char
            } else if (x > y) {
                // Shift elements from y to x-1 right by 1
                System.arraycopy(result, y, result, y + 1, x - y)
                result[y] = char
            }
            // if x == y, no change needed
            return result
        }

        override fun reverse(chars: CharArray): CharArray {
            // Reverse of move x to y is move y to x
            val result = chars.copyOf()
            val char = result[y]

            if (y < x) {
                // Shift elements from y+1 to x left by 1
                System.arraycopy(result, y + 1, result, y, x - y)
                result[x] = char
            } else if (y > x) {
                // Shift elements from x to y-1 right by 1
                System.arraycopy(result, x, result, x + 1, y - x)
                result[x] = char
            }
            // if y == x, no change needed
            return result
        }
    }
}

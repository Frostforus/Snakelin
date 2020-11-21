enum class Direction {
    RIGHT,
    UP,
    LEFT,
    DOWN
}

data class Apple(var pos_x: Int = 5, var pos_y: Int = 5)

fun Apple.newLocation() {
    pos_x = (0..10).random()
    pos_y = (0..10).random()
    println("New Location for apple: $this")
}



data class BodySegment(var pos_x: Int, var pos_y: Int, var direction: Direction = Direction.RIGHT)

//Extention for Mutablelist
fun <T> MutableList<T>.prepend(element: T) {
    add(0, element)
}

class Snake(var length: Int = 3, val apple: Apple) {
    val snake_body: MutableList<BodySegment> = mutableListOf<BodySegment>(BodySegment(0, 0))

    init {
        for (i in 1 until length) {
            snake_body.add(BodySegment(0 - i, 0))
        }
        printCoords()
    }

    fun add_segment() {
        snake_body.add(
            BodySegment(
                snake_body[length - 1].pos_x,
                snake_body[length - 1].pos_y,
                snake_body[length - 1].direction
            )
        )
        length += 1
        printCoords()
    }

    fun changedirection(new_direction: Direction) {
        snake_body[0].direction = new_direction
    }

    fun move() {
        var xDiff = 0
        var yDiff = 0

        // Check which direction we are moving
        when (snake_body[0].direction) {
            Direction.RIGHT -> xDiff = 1
            Direction.UP -> yDiff = 1
            Direction.LEFT -> xDiff = -1
            Direction.DOWN -> yDiff = -1
        }

        // Add the first element in the direction the snake is moving
        val tempSegment =
            BodySegment(snake_body[0].pos_x + xDiff, snake_body[0].pos_y + yDiff, snake_body[0].direction)
        snake_body.prepend(tempSegment)

        // Remove last segment (this at index of length because we didnt increase the length while the length of the list increased by one)
        snake_body.removeAt(length)
        printCoords()
        checkCollision()
        appleCollision(apple = apple)
    }

    private fun checkCollision(): Boolean {
        // since you can't do a 180 you dont have to check the first
        for (i: Int in 3 until length) {
            if (snake_body[0].pos_x == snake_body[i].pos_x && snake_body[0].pos_y == snake_body[i].pos_y) {
                return true
            }
        }
        return false
    }

    private fun appleCollision(snake_index: Int = 0, apple: Apple): Boolean {
        if (snake_body[snake_index].pos_x == apple.pos_x && snake_body[snake_index].pos_y == apple.pos_y) {
            add_segment()
            apple.newLocation()
            return true
        }
        return false
    }
}

fun Snake.printCoords() {
    for ((index, segment: BodySegment) in snake_body.withIndex()) {
        println("Segment at [$index] : $segment")
    }
    println()
}

fun main() {
    val apple = Apple(2,0)
    val snake = Snake(length = 5,apple = apple)

    snake.move()
    snake.move()
    snake.move()
    snake.move()
    snake.move()
}
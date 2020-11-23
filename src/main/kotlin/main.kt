enum class Direction {
    RIGHT,
    UP,
    LEFT,
    DOWN
}

//TODO: ez mehet inkabb a boardba nincs értelme a snakeben
data class Apple(
    var pos_x: Int,
    var pos_y: Int,
    val width: Int,
    val height: Int,//trailing comma, :D
) {
    init {
        pos_x = (0 until width).random()
        pos_y = (0 until height).random()
    }
}


fun Apple.newLocation(snake: Snake) {
    pos_x = (0 until width).random()
    pos_y = (0 until height).random()
    println("Trying")
    for (i: Int in 0 until snake.snakeBody.size) {
        if (snake.appleCollision(i, this, true)) {
            newLocation(snake)
        }
    }

    println("New Location for apple: $this")
}


data class BodySegment(var pos_x: Int, var pos_y: Int, var direction: Direction = Direction.RIGHT)

//Extention for Mutablelist
fun <T> MutableList<T>.prepend(element: T) {
    add(0, element)
}

class Snake(
    private var length: Int = 3,
    val apple: Apple,//trailing comma, :D
) {

    val snakeBody: MutableList<BodySegment> = mutableListOf(BodySegment(pos_x = length, 0))

    val head: BodySegment
        inline get() = snakeBody[0]

    init {
        println(head)
        //FIXME
        for (i in 0 until length - 1) {
            snakeBody.add(BodySegment(length - 1 - i, 0))
            println("Added:${snakeBody[i]}")
        }
        //printCoords()
    }

    private fun addSegment() {
        snakeBody.add(
            BodySegment(
                snakeBody[length - 1].pos_x,
                snakeBody[length - 1].pos_y,
                snakeBody[length - 1].direction
            )
        )
        length += 1
        //printCoords()
    }

    fun changeDirection(new_direction: Direction): Boolean {
        return if ((new_direction == Direction.LEFT && head.direction == Direction.RIGHT) ||
            (new_direction == Direction.RIGHT && head.direction == Direction.LEFT) ||
            (new_direction == Direction.UP && head.direction == Direction.DOWN) ||
            (new_direction == Direction.DOWN && head.direction == Direction.UP)
        ) {
            println("Failure: Facing opposing direction. ")
            false
        } else {
            head.direction = new_direction
            println("Success!")
            true
        }
    }

    fun move() {
        var xDiff = 0
        var yDiff = 0

        // Check which direction we are moving
        when (head.direction) {
            Direction.RIGHT -> xDiff = 1
            Direction.UP -> yDiff = -1
            Direction.LEFT -> xDiff = -1
            Direction.DOWN -> yDiff = 1
        }

        // Add the first element in the direction the snake is moving
        val tempSegment =
            BodySegment(head.pos_x + xDiff, head.pos_y + yDiff, head.direction)
        snakeBody.prepend(tempSegment)

        // Remove last segment (this at index of length because we didnt increase the length while the length of the list increased by one)
        snakeBody.removeAt(length)
        //printCoords()
        //printHeadandApple()
        appleCollision(apple = apple)
    }

    fun checkCollision(width: Int, height: Int): Boolean {
        // since you can't do a 180 you dont have to check the first
        for (i: Int in 3 until length) {
            if (head.pos_x == snakeBody[i].pos_x && head.pos_y == snakeBody[i].pos_y) {
                return true
            }
        }
        // If out of map
        return (head.pos_x < 0 || width - 1 < head.pos_x || head.pos_y < 0 || height - 1 < head.pos_y)

    }

    fun appleCollision(snake_index: Int = 0, apple: Apple, calledByApple: Boolean = false): Boolean {
        if (snakeBody[snake_index].pos_x == apple.pos_x && snakeBody[snake_index].pos_y == apple.pos_y) {
            addSegment()
            if (!calledByApple) {
                apple.newLocation(this)
            }
            return true
        }
        return false
    }
}

fun Snake.printCoords() {
    for ((index, segment: BodySegment) in snakeBody.withIndex()) {
        println("Segment at [$index] : $segment")
    }
    println()
}

fun Snake.printHeadandApple() {
    println("Head at: $head")
    println("Apple at: $apple")
    println()
}
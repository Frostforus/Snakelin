fun main() {

    val addx: (Int, Int) -> Int
    fun add(x: Int, y: Int): Int {
        return x + y
    }
    addx = { x, y -> println("hi"); x + y }
    fun something(x: Int, y: Int, function: (Int, Int) -> Int) {

        print(function(x, y))
    }


    something(4, 5, addx)
}
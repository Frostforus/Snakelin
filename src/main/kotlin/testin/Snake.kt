package testin

import Apple
import BodySegment
import Direction
import Snake
import newLocation
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import javax.swing.*


//TODO: implement higher level function so we can choose between circle and square fun point to square or circle
class Board(
    //Board size
    width: Int = 300,
    height: Int = 300,
    private val SCALE: Int = 30,

    drawingFuntionProxy: Int = 0,

    //Timer Delay
    delay: Int = 250, //trailing comma, :D
) : JPanel(), ActionListener {

    private var inGame: Boolean = true
    private var timer: Timer? = null
    private var movedThisFrame: Boolean = false

    private val sizeX: Int = width / SCALE
    private val sizeY = height / SCALE
    private lateinit var drawingFunction: (Graphics, Int, Int, Color) -> Unit

    //Snake
    private val snake: Snake = Snake(apple = Apple(5, 5, sizeX, sizeY))

    val drawCircleVal: (Graphics, Int, Int, Color) -> Unit = { g, pos_x, pos_y, color
        ->
        g.color = color
        // Transformation so the middle of the board is [0,0]
        g.fillOval(getXFromArray(pos_x), getYFromArray(pos_y), SCALE, SCALE)
    }

    val drawSquareVal: (Graphics, Int, Int, Color) -> Unit = { g, pos_x, pos_y, color
        ->
        g.color = color
        // Transformation so the middle of the board is [0,0]
        g.fillRect(getXFromArray(pos_x), getYFromArray(pos_y), SCALE - 1, SCALE - 1)
    }

    val drawImageVal: (Graphics, Int, Int, Color) -> Unit = { g, pos_x, pos_y, color
        ->
        when (color) {
            Color.CYAN -> g.drawImage(
                ImageIcon("resources\\Head.png").image,
                getXFromArray(pos_x),
                getYFromArray(pos_y),
                this
            )
            Color.WHITE -> g.drawImage(
                ImageIcon("resources\\Body.png").image,
                getXFromArray(pos_x),
                getYFromArray(pos_y),
                this
            )
            Color.RED -> g.drawImage(
                ImageIcon("resources\\Apple.png").image,
                getXFromArray(pos_x),
                getYFromArray(pos_y),
                this
            )
            else -> println("bruh")
        }
    }


    init {
        drawingFunction = when (drawingFuntionProxy) {
            0 -> drawCircleVal
            1 -> drawSquareVal
            2 -> drawImageVal
            else -> drawImageVal
        }
        //This prettiness so the apple doesnt start on the snake
        snake.apple.newLocation(snake)
        //TODO: Check size is divisible (get int as length)

        addKeyListener(TAdapter())
        background = Color.black
        isFocusable = true

        preferredSize = Dimension(width, height)
        repaint()

        timer = Timer(delay, this)
        timer!!.start()
    }

    private fun drawSegment(g: Graphics, pos_x: Int, pos_y: Int, drawFunction: (Graphics, Int, Int, Color) -> Unit) {
        drawFunction(g, pos_x, pos_y, Color.WHITE)
    }

    private fun drawHead(g: Graphics, pos_x: Int, pos_y: Int, drawFunction: (Graphics, Int, Int, Color) -> Unit) {
        drawFunction(g, pos_x, pos_y, Color.CYAN)
    }

    private fun drawApple(g: Graphics, pos_x: Int, pos_y: Int, drawFunction: (Graphics, Int, Int, Color) -> Unit) {
        drawFunction(g, pos_x, pos_y, Color.RED)
    }

    // New and improved public so we can initialize on primary constructor call

    // TODO: remove
    private fun drawCircle(g: Graphics, pos_x: Int, pos_y: Int, color: Color = Color.BLUE) {
        g.color = color
        // Transformation so the middle of the board is [0,0]
        g.fillOval(getXFromArray(pos_x), getYFromArray(pos_y), SCALE, SCALE)
    }


    private fun drawSquare(g: Graphics, pos_x: Int, pos_y: Int, color: Color = Color.BLUE) {
        g.color = color
        // Transformation so the middle of the board is [0,0]
        g.fillRect(getXFromArray(pos_x), getYFromArray(pos_y), SCALE - 1, SCALE - 1)
    }


    private fun drawImage(g: Graphics, pos_x: Int, pos_y: Int, color: Color = Color.BLUE) {
        when (color) {
            Color.CYAN -> g.drawImage(
                ImageIcon("resources\\Head.png").image,
                getXFromArray(pos_x),
                getYFromArray(pos_y),
                this
            )
            Color.WHITE -> g.drawImage(
                ImageIcon("resources\\Body.png").image,
                getXFromArray(pos_x),
                getYFromArray(pos_y),
                this
            )
            Color.RED -> g.drawImage(
                ImageIcon("resources\\Apple.png").image,
                getXFromArray(pos_x),
                getYFromArray(pos_y),
                this
            )
            else -> println("bruh")
        }

    }

    private fun getXFromArray(array_pos_x: Int): Int = array_pos_x * SCALE

    private fun getYFromArray(array_pos_y: Int): Int = array_pos_y * SCALE

    private fun drawAll(g: Graphics) {
        if (inGame) {
            // Draw Head
            drawHead(g, snake.head.pos_x, snake.head.pos_y, drawingFunction)

            //Draw Body
            for (segment: BodySegment in snake.snakeBody.drop(1)) {
                drawSegment(g, segment.pos_x, segment.pos_y, drawingFunction)
            }


            // Draw Apple
            drawApple(g, snake.apple.pos_x, snake.apple.pos_y, drawingFunction)

            Toolkit.getDefaultToolkit().sync()
        } else {
            timer!!.stop()
        }
    }

    public override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        drawAll(g)
    }

    private inner class TAdapter : KeyAdapter() {

        override fun keyPressed(e: KeyEvent?) {

            // Can only change direction once a frame
            if (movedThisFrame) {
                return
            }
            //Assume this means the direction change will be successful


            val key = e!!.keyCode
            print("$key pressed: ")
            when (key) {
                KeyEvent.VK_LEFT, KeyEvent.VK_A -> {
                    println("Trying to turn left... ")
                    movedThisFrame = snake.changeDirection(Direction.LEFT)
                }

                KeyEvent.VK_RIGHT, KeyEvent.VK_D -> {
                    println("Trying to turn right... ")
                    movedThisFrame = snake.changeDirection(Direction.RIGHT)
                }

                KeyEvent.VK_UP, KeyEvent.VK_W -> {
                    println("Trying to turn up... ")
                    movedThisFrame = snake.changeDirection(Direction.UP)
                }

                KeyEvent.VK_DOWN, KeyEvent.VK_S -> {
                    println("Trying to turn down... ")
                    movedThisFrame = snake.changeDirection(Direction.DOWN)
                }

                KeyEvent.VK_SPACE -> {
                    println("Gamve over")
                    inGame = false
                }

                else -> println("Nothing happened...")

            }
        }
    }

    override fun actionPerformed(e: ActionEvent) {
        if (inGame) {
            snake.move()
        }
        //Reset moved_this_frame so they can move again
        movedThisFrame = false

        if (snake.checkCollision(sizeX, sizeY)) {
            inGame = false
            endGame()
        }
        repaint()
    }

    //TODO: implement properly
    private fun endGame() {
        println("Game over")
        timer?.stop()
    }


}

class MainFrame(
    width: Int = 300,
    height: Int = 300,
    SCALE: Int = 50,
    delay: Int = 5
) : JFrame() {

    init {
        val mainPanel = JPanel()
        val playButton = JButton("Play")
        val settingsButton = JButton("Settings")

        val box = Box.createVerticalBox()

        box.add(playButton)

        box.add(settingsButton)

        mainPanel.add(box)

        lateinit var gamePanel: Board

        add(mainPanel)

        minimumSize = Dimension(800, 800)

        playButton.addActionListener {
            mainPanel.isVisible = false
            playButton.isVisible = false
            gamePanel = Board(width, height, SCALE, drawingFuntionProxy = 3, delay = delay)
            add(gamePanel)
            gamePanel.requestFocusInWindow()
            gamePanel.isVisible = true
            pack()
            repaint();
        }



        title = "Snake"
        iconImage = ImageIcon("resources\\Head.png").image
        isResizable = false
        pack()

        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
    }


}

fun main() {

    EventQueue.invokeLater {
        val ex = MainFrame(750, 750, 20, 150)
        ex.isVisible = true
    }
}
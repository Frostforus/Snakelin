import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

import javax.swing.ImageIcon
import javax.swing.JPanel
import javax.swing.Timer


class Board : JPanel(), ActionListener {

    init {

        addKeyListener(TAdapter())
        background = Color.black
        isFocusable = true

        preferredSize = Dimension(300, 300)

    }

    private inner class TAdapter : KeyAdapter() {

        override fun keyPressed(e: KeyEvent?) {

            val key = e!!.keyCode


        }

        fun actionPerformed(p0: ActionEvent?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

}

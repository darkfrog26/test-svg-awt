package test

import java.awt.{BorderLayout, Graphics2D, RenderingHints}
import java.awt.image.BufferedImage
import java.io.File
import javax.swing.{ImageIcon, JFrame, JLabel}

object SVGImage {
  def main(args: Array[String]): Unit = {
    val frame = new JFrame("Testing")
    val svg = SVGComponent(new File("analogclock.svg"))
    frame.getContentPane.setLayout(new BorderLayout)
    frame.getContentPane.add(svg, BorderLayout.CENTER)
    frame.pack()
    frame.setVisible(true)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  }
}

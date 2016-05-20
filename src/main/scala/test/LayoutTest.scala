package test

import java.awt.Container
import java.io.File
import javax.swing.{JButton, JFrame}

import pl.metastack.metarx._

object LayoutTest {
  def create(): Unit = {
    val frame = new JFrame("Layout Test")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    init(frame.getContentPane)
    frame.setSize(500, 500)
    frame.setVisible(true)
  }

  private def init(container: Container): Unit = {
    container.setLayout(null)
    val containerWrapper = new Group(container)

    val b = new JButton("Press Me!")
    b.setSize(b.getPreferredSize.width, b.getPreferredSize.height)
    b.setLocation(25, 25)
    container.add(b)
    val buttonWrapper = new WrappedComponent(b)

    val svg = SVGComponent(new File("SlickComparison01.svg"), maintainAspectRatio = false)
//    svg.setSize(200, 150)
//    svg.setLocation(100, 100)
    container.add(svg)
    val svgWrapper = new WrappedComponent(svg)
    svgWrapper.center := containerWrapper.center
    svgWrapper.middle := containerWrapper.middle
    svgWrapper.width := containerWrapper.width
    svgWrapper.height := containerWrapper.height
  }
}
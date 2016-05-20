package test

import java.awt.event.{ComponentAdapter, ComponentEvent}
import java.awt.{Component, Container}
import javax.swing.SwingUtilities

import pl.metastack.metarx._

trait Widget {
  val x: Sub[Double] = Sub(0.0)
  val y: Sub[Double] = Sub(0.0)

  def left: Sub[Double] = x
  lazy val center: Dep[Double, Double] = x.dep(_ + (width / 2.0), _ - (width / 2.0))
  lazy val right: Dep[Double, Double] = x.dep(_ + width, _ - width)

  def top: Sub[Double] = y
  lazy val middle: Dep[Double, Double] = y.dep(_ + (height / 2.0), _ - (height / 2.0))
  lazy val bottom: Dep[Double, Double] = y.dep(_ + height, _ - height)

  val width: Sub[Double] = Sub(0.0)
  val height: Sub[Double] = Sub(0.0)
}

class WrappedComponent[C <: Component](val peer: C) extends Widget {
  x.attach { d =>
    updatePosition()
  }
  y.attach { d =>
    updatePosition()
  }
  width.attach { d =>
    updateSize()
  }
  height.attach { d =>
    updateSize()
  }

  private def updatePosition(): Unit = SwingUtilities.invokeLater(new Runnable {
    override def run(): Unit = peer.setLocation(math.round(x.get).toInt, math.round(y.get).toInt)
  })

  private def updateSize(): Unit = SwingUtilities.invokeLater(new Runnable {
    override def run(): Unit = peer.setSize(math.round(width.get).toInt, math.round(height.get).toInt)
  })
}

class Group(container: Container) {
  val width: Sub[Double] = Sub(0.0)
  val height: Sub[Double] = Sub(0.0)
  lazy val center: ReadChannel[Double] = width / 2.0
  lazy val middle: ReadChannel[Double] = height / 2.0

  container.addComponentListener(new ComponentAdapter {
    override def componentResized(e: ComponentEvent): Unit = {
      width := container.getWidth.toDouble
      height := container.getHeight.toDouble
    }
  })
}
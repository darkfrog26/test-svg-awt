package test

import java.awt._
import java.io.File

import org.apache.batik.bridge.{BridgeContext, GVTBuilder, UserAgentAdapter}
import org.apache.batik.dom.svg.SAXSVGDocumentFactory
import org.apache.batik.gvt.GraphicsNode
import org.apache.batik.util.XMLResourceDescriptor
import org.w3c.dom.svg.SVGDocument

class SVGComponent(graphicsNode: GraphicsNode, maintainAspectRatio: Boolean = true) extends Canvas {
  setPreferredSize(new Dimension(
    math.max(100, graphicsNode.getBounds.getWidth.toInt),
    math.max(100, graphicsNode.getBounds.getHeight.toInt)
  ))

  override def paint(g: Graphics): Unit = {
    super.paint(g)

    val bounds = graphicsNode.getBounds
    val scaleX = getWidth.toDouble / bounds.getWidth
    val scaleY = getHeight.toDouble / bounds.getHeight

    val g2d = g.asInstanceOf[Graphics2D]
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    if (maintainAspectRatio) {
      val min = math.min(scaleX, scaleY)
      val offsetX = (scaleX - min) * (bounds.getWidth / 2.0)
      val offsetY = (scaleY - min) * (bounds.getHeight / 2.0)
      g2d.translate(offsetX, offsetY)
      g2d.scale(min, min)
    } else {
      g2d.scale(scaleX, scaleY)
    }
    graphicsNode.paint(g2d)
    g2d.dispose()
  }
}

object SVGComponent {
  def apply(document: SVGDocument, maintainAspectRatio: Boolean): SVGComponent = {
    val userAgentAdapter = new UserAgentAdapter
    val bridgeContext = new BridgeContext(userAgentAdapter)
    val builder = new GVTBuilder
    val graphicsNode = builder.build(bridgeContext, document)
    new SVGComponent(graphicsNode, maintainAspectRatio)
  }

  def apply(url: String, maintainAspectRatio: Boolean): SVGComponent = {
    val parser = XMLResourceDescriptor.getXMLParserClassName
    val factory = new SAXSVGDocumentFactory(parser)
    val document = factory.createDocument(url).asInstanceOf[SVGDocument]
    apply(document, maintainAspectRatio)
  }

  def apply(file: File, maintainAspectRatio: Boolean): SVGComponent = {
    apply(file.toURI.toURL.toString, maintainAspectRatio)
  }
}
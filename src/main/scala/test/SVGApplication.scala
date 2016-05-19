package test

import java.awt.event.{ActionEvent, ActionListener, WindowAdapter, WindowEvent}
import java.awt.{BorderLayout, FlowLayout}
import javax.swing._

import org.apache.batik.swing.JSVGCanvas
import org.apache.batik.swing.svg.{GVTTreeBuilderAdapter, GVTTreeBuilderEvent, SVGDocumentLoaderAdapter, SVGDocumentLoaderEvent}

object SVGApplication extends JFrame("Batik") {
  val button = new JButton("Load...")
  val label = new JLabel
  val svgCanvas = new JSVGCanvas

  val panel = new JPanel(new BorderLayout)
  panel.add("North", new JPanel(new FlowLayout(FlowLayout.LEFT)) {
    add(button)
    add(label)
  })
  panel.add("Center", svgCanvas)

  button.addActionListener(new ActionListener {
    override def actionPerformed(actionEvent: ActionEvent): Unit = {
      val fc = new JFileChooser(".")
      if (fc.showOpenDialog(panel) == JFileChooser.APPROVE_OPTION) {
        val f = fc.getSelectedFile
        svgCanvas.setURI(f.toURI.toURL.toString)
      }
    }
  })
  svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter {
    override def documentLoadingCompleted(e: SVGDocumentLoaderEvent): Unit = {
      label.setText("Document loaded")
    }

    override def documentLoadingStarted(e: SVGDocumentLoaderEvent): Unit = {
      label.setText("Document loading")
    }
  })
  svgCanvas.addGVTTreeBuilderListener(new GVTTreeBuilderAdapter {
    override def gvtBuildStarted(e: GVTTreeBuilderEvent): Unit = {
      label.setText("Build started")
    }

    override def gvtBuildCompleted(e: GVTTreeBuilderEvent): Unit = {
      label.setText("Build done")
      pack()
    }
  })

  getContentPane.add(panel)
  addWindowListener(new WindowAdapter {
    override def windowClosing(e: WindowEvent): Unit = {
      System.exit(0)
    }
  })

  def main(args: Array[String]): Unit = {
    setSize(400, 400)
    setVisible(true)
  }
}

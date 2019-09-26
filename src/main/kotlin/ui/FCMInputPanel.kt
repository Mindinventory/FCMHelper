package ui

import com.intellij.ui.layout.panel
import util.Constants.CONTENT_TEXT
import util.Constants.CONTENT_TITLE
import util.Constants.FCM_SERVICE_NAME
import util.Constants.MY_FIREBASE_MESSAGING_SERVICE
import util.Constants.NEED_INSTRUCTION
import util.Constants.NOTES_INSTRUCTION
import util.Constants.PENDINGINTENT_ACTIVITY_NAME
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JTextField

class FCMInputPanel : JPanel() {

    val serviceNameTextField = JTextField(MY_FIREBASE_MESSAGING_SERVICE)
    val pendingIntentTextField = JTextField()
    val contentTitleTextField = JTextField()
    val contentTextTextField = JTextField()
    val isNeedReadMeForInstructions = JCheckBox()

    init {
        layout = BorderLayout()
        isNeedReadMeForInstructions.isSelected = true
        val panel = panel {
            row(FCM_SERVICE_NAME) { serviceNameTextField() }
            row(PENDINGINTENT_ACTIVITY_NAME) { pendingIntentTextField() }
            row(CONTENT_TITLE) { contentTitleTextField() }
            row(CONTENT_TEXT) { contentTextTextField() }
            row(NEED_INSTRUCTION) { isNeedReadMeForInstructions() }
            row(NOTES_INSTRUCTION) { }
        }
        add(panel, BorderLayout.CENTER)
    }

    override fun getPreferredSize() = Dimension(450, 110)
}

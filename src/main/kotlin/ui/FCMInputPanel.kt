package ui

import util.Constants.CONTENT_TEXT
import util.Constants.CONTENT_TITLE
import util.Constants.DEPENDENCY_VERSION
import util.Constants.DEPENDENCY_VERSION_VALUE
import util.Constants.FCM_PANEL
import util.Constants.FCM_SERVICE_NAME
import util.Constants.MY_FIREBASE_MESSAGING_SERVICE
import util.Constants.NEED_INSTRUCTION
import util.Constants.NOTES_INSTRUCTION
import util.Constants.PENDINGINTENT_ACTIVITY_NAME
import javax.swing.*
import javax.swing.BorderFactory
import java.awt.GridBagConstraints
import java.awt.Insets
import java.awt.GridBagLayout
import javax.swing.JPanel
import javax.swing.JTextField

class FCMInputPanel : JPanel() {

    private val labelServiceName = JLabel(FCM_SERVICE_NAME)
    private val labelPendingIntent = JLabel(PENDINGINTENT_ACTIVITY_NAME)
    private val labelContentTitle = JLabel(CONTENT_TITLE)
    private val labelContentText = JLabel(CONTENT_TEXT)
    private val labelVersion = JLabel(DEPENDENCY_VERSION)
    private val labelInstruction = JLabel(NOTES_INSTRUCTION)

    val isNeedReadMeForInstructions = JCheckBox(NEED_INSTRUCTION)

    val serviceNameTextField = JTextField(MY_FIREBASE_MESSAGING_SERVICE, 25)
    val dependencyVersionTextField = JTextField(DEPENDENCY_VERSION_VALUE, 25)
    val pendingIntentTextField = JTextField(25)
    val contentTitleTextField = JTextField(25)
    val contentTextTextField = JTextField(25)

    init {

        val newPanel = JPanel(GridBagLayout())

        val inset = 6
        val constraints = GridBagConstraints()
        constraints.anchor = GridBagConstraints.WEST
        constraints.insets = Insets(inset, inset, inset, inset)

        // add components to the panel
        constraints.gridx = 0
        constraints.gridy = 0
        newPanel.add(labelServiceName, constraints)
        constraints.gridx = 1
        newPanel.add(serviceNameTextField, constraints)

        constraints.gridx = 0
        constraints.gridy = 1
        newPanel.add(labelPendingIntent, constraints)
        constraints.gridx = 1
        newPanel.add(pendingIntentTextField, constraints)

        constraints.gridx = 0
        constraints.gridy = 2
        newPanel.add(labelContentTitle, constraints)
        constraints.gridx = 1
        newPanel.add(contentTitleTextField, constraints)

        constraints.gridx = 0
        constraints.gridy = 3
        newPanel.add(labelContentText, constraints)
        constraints.gridx = 1
        newPanel.add(contentTextTextField, constraints)

        constraints.gridx = 0
        constraints.gridy = 4
        newPanel.add(labelVersion, constraints)
        constraints.gridx = 1
        newPanel.add(dependencyVersionTextField, constraints)

        constraints.gridx = 0
        constraints.gridy = 5
        newPanel.add(isNeedReadMeForInstructions, constraints)

        constraints.gridwidth = 2
        constraints.gridy = 6
        constraints.anchor = GridBagConstraints.CENTER
        newPanel.add(labelInstruction, constraints)

        // set border for the panel
        newPanel.border = BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), FCM_PANEL
        )

        // add the panel to this frame
        add(newPanel)
    }
}

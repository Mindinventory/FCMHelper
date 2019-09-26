package action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ui.NewScreenDialog

class SearchAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        NewScreenDialog(event).show()
    }
}

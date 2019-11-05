package managers

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.EmptyAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import org.apache.commons.lang.StringUtils
import util.Constants
import util.Constants.FCM_NOTIFICATION

import javax.swing.*
import java.io.FileNotFoundException
import java.util.stream.Stream

class GradleManager(private val project: Project) {

    private var buildGradle: Document? = null

    private var modules = arrayOf<Any>()

    private var projectBaseDir: VirtualFile? = null

    @Throws(FileNotFoundException::class)
    fun initBuildGradle(): Boolean {
        checkFilesExist()
        val gradleVirtualFile: VirtualFile?
        if (modules.size > 1) {
            val isHaveAppModule: String? = modules.find { it == Constants.DEFAULT_MODULE_NAME } as String
            if (isHaveAppModule != null && isHaveAppModule != "") {
                gradleVirtualFile = projectBaseDir!!
                    .findChild(isHaveAppModule)!!
                    .findChild("build.gradle")
            } else {
                return false
            }
        } else if (modules.size == 1) {
            gradleVirtualFile = projectBaseDir!!
                .findChild(modules[0] as String)!!
                .findChild("build.gradle")
        } else {
            gradleVirtualFile = projectBaseDir!!
                .findChild("build.gradle")
        }
        if (gradleVirtualFile != null) {
            buildGradle = FileDocumentManager.getInstance().getDocument(gradleVirtualFile)
        }
        return true
    }

    @Throws(FileNotFoundException::class)
    private fun checkFilesExist() {

        val basePath = project.basePath
        if (StringUtils.isEmpty(basePath)) {
            throw FileNotFoundException(Constants.ERROR_BASE_PATH_NOT_FOUND)
        }

        projectBaseDir = LocalFileSystem.getInstance().findFileByPath(basePath!!)
        if (projectBaseDir == null) {
            throw FileNotFoundException(Constants.ERROR_PROJECT_BASE_DIR_NOT_FOUND)
        }

        val virtualSettingsGradle = projectBaseDir!!.findChild("settings.gradle")
        if (virtualSettingsGradle != null) {
            val settingsGradle = FileDocumentManager.getInstance().getDocument(virtualSettingsGradle)
            if (settingsGradle != null) {
                modules = readSettingsGradle(settingsGradle)
            }
        } else if (projectBaseDir!!.findChild("build.gradle") == null) {
            throw FileNotFoundException(Constants.ERROR_BUILD_GRADLE_NOT_FOUND)
        }
    }

    fun addDependency(repository: String, actionEvent: AnActionEvent) {
        val documentText = buildGradle!!.text.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val sb = StringBuilder()
        for (i in documentText.indices) {
            val line = documentText[i]

            sb
                .append(line)
                .append("\n")

            if (line.contains(Constants.DEPENDENCIES)) {
                if (line.contains("{")) {
                    sb
                        .append("\t$FCM_NOTIFICATION\n\t${Constants.IMPLEMENTATION} '")
                        .append(repository)
                        .append("'\n")
                }
            }
        }

        writeToGradle(sb, actionEvent)
    }

    private fun writeToGradle(stringBuilder: StringBuilder, actionEvent: AnActionEvent) {
        val application = ApplicationManager.getApplication()
        application.invokeLater {
            application.runWriteAction { buildGradle!!.setText(stringBuilder) }
            syncProject(actionEvent)
        }
    }

    // TODO do not allow this method called without invokeLater()
    private fun syncProject(actionEvent: AnActionEvent) {
        val androidSyncAction = getAction("Android.SyncProject")
        val refreshAllProjectAction = getAction("ExternalSystem.RefreshAllProjects")

        if (androidSyncAction != null && androidSyncAction !is EmptyAction) {
            androidSyncAction.actionPerformed(actionEvent)
        } else if (refreshAllProjectAction != null && refreshAllProjectAction !is EmptyAction) {
            refreshAllProjectAction.actionPerformed(actionEvent)
        } else {
            SwingUtilities.invokeLater {
                Messages.showInfoMessage(
                    Constants.ERROR_SYNC_FAILED,
                    Constants.ERROR_TITLE_SYNC_FAILED
                )
            }
        }
    }

    private fun getAction(actionId: String): AnAction? {
        return ActionManager.getInstance().getAction(actionId)
    }

    private fun readSettingsGradle(settingsGradle: Document): Array<Any> {
        return Stream.of(*settingsGradle.text.split("'".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray())
            .filter { s -> s.startsWith(":") }
            .map { s -> s.replace(":", "") }
            .toArray()
    }
}
